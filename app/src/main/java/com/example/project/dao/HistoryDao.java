package com.example.project.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.databasehelper.DatabaseHelper;
import com.example.project.models.historyitem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryDao {
    private DatabaseHelper dbHelper;
    // match the format you used when inserting
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public HistoryDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Helper: parse date string safely with fallback
    private Date parseDateSafe(String s) {
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;

        // try format yyyy-MM-dd
        try {
            return sdf.parse(s);
        } catch (ParseException ignored) {}

        // fallback: try parse as long (epoch millis stored as text)
        try {
            long epoch = Long.parseLong(s);
            return new Date(epoch);
        } catch (NumberFormatException ignored) {}

        // fallback: try common datetime pattern "yyyy-MM-dd HH:mm:ss"
        try {
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf2.parse(s);
        } catch (ParseException ignored) {}

        // couldn't parse
        return null;
    }

    // Lấy toàn bộ lịch sử
    public List<historyitem> getAll() {
        List<historyitem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM historyitem", null);
            if (cursor.moveToFirst()) {
                do {
                    historyitem h = new historyitem();
                    h.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    h.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
                    h.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    h.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    h.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                    h.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));

                    String dateStr = null;
                    int idxDate = cursor.getColumnIndex("orderdate");
                    if (idxDate != -1) {
                        dateStr = cursor.getString(idxDate);
                    }
                    Date parsed = parseDateSafe(dateStr);
                    h.setOrderdate(parsed);

                    list.add(h);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    // Thêm mới (orderdate = thời gian hiện tại)
    public long create(historyitem h) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("userid", h.getUserid());
            values.put("name", h.getName());
            values.put("price", h.getPrice());
            values.put("quantity", h.getQuantity());
            values.put("image", h.getImage());

            // Lưu theo format yyyy-MM-dd để tương thích code cũ
            String currentDate = sdf.format(new Date());
            values.put("orderdate", currentDate);

            newId = db.insert("historyitem", null, values);
        } finally {
            db.close();
        }
        return newId;
    }

    // Lấy lịch sử theo userid
    public List<historyitem> getOrderByUserId(int userid) {
        List<historyitem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM historyitem WHERE userid = ?", new String[]{String.valueOf(userid)});
            if (cursor.moveToFirst()) {
                do {
                    historyitem h = new historyitem();
                    h.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    h.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
                    h.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    h.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    h.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                    h.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));

                    String dateStr = null;
                    int idxDate = cursor.getColumnIndex("orderdate");
                    if (idxDate != -1) {
                        dateStr = cursor.getString(idxDate);
                    }
                    Date parsed = parseDateSafe(dateStr);
                    h.setOrderdate(parsed);

                    list.add(h);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }

    // Tổng doanh thu hôm nay
    public double getRevenueByDay() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String today = sdf.format(new Date()); // yyyy-MM-dd
            cursor = db.rawQuery(
                    "SELECT SUM(price * quantity) AS total FROM historyitem WHERE orderdate = ?",
                    new String[]{today}
            );
            if (cursor.moveToFirst()) {
                // nếu total null, getDouble sẽ trả 0.0
                int idx = cursor.getColumnIndex("total");
                if (idx != -1 && !cursor.isNull(idx)) {
                    total = cursor.getDouble(idx);
                }
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return total;
    }

    // Tổng doanh thu tháng này
    public double getRevenueByMonth() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String month = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
            cursor = db.rawQuery(
                    "SELECT SUM(price * quantity) AS total FROM historyitem WHERE substr(orderdate, 1, 7) = ?",
                    new String[]{month}
            );
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex("total");
                if (idx != -1 && !cursor.isNull(idx)) {
                    total = cursor.getDouble(idx);
                }
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return total;
    }

    // Tổng doanh thu năm hiện tại
    public double getRevenueByYear() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        double total = 0;
        Cursor cursor = null;

        try {
            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            cursor = db.rawQuery(
                    "SELECT SUM(price * quantity) AS total FROM historyitem WHERE substr(orderdate, 1, 4) = ?",
                    new String[]{year}
            );
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex("total");
                if (idx != -1 && !cursor.isNull(idx)) {
                    total = cursor.getDouble(idx);
                }
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return total;
    }
}
