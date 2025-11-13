package com.example.project.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.databasehelper.DatabaseHelper;
import com.example.project.models.cartitem;

import java.util.ArrayList;
import java.util.List;

public class CartDao {
    private DatabaseHelper dbHelper;

    public CartDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Lấy toàn bộ giỏ hàng
    public List<cartitem> getAll() {
        List<cartitem> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM cartitem", null);
            if (cursor.moveToFirst()) {
                do {
                    cartitem c = new cartitem();
                    c.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    c.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
                    c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    c.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    c.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                    c.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    list.add(c);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }

    // Thêm mới (quantity mặc định = 1)
    public long create(cartitem c) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = -1;

        try {
            ContentValues values = new ContentValues();
            values.put("userid", c.getUserid());
            values.put("name", c.getName());
            values.put("price", c.getPrice());
            values.put("quantity", 1); // quantity mặc định là 1
            values.put("image", c.getImage());
            newId = db.insert("cartitem", null, values);
        } finally {
            db.close();
        }

        return newId;
    }

    // Tăng số lượng thêm 1
    public int addQuantity(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT quantity FROM cartitem WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                quantity += 1;
                ContentValues values = new ContentValues();
                values.put("quantity", quantity);
                rows = db.update("cartitem", values, "id = ?", new String[]{String.valueOf(id)});
            }
            cursor.close();
        } finally {
            db.close();
        }
        return rows;
    }

    // Giảm số lượng đi 1 (nếu = 0 thì xóa luôn)
    public int returnQuantity(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            Cursor cursor = db.rawQuery("SELECT quantity FROM cartitem WHERE id = ?", new String[]{String.valueOf(id)});
            if (cursor.moveToFirst()) {
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                if (quantity > 1) {
                    quantity -= 1;
                    ContentValues values = new ContentValues();
                    values.put("quantity", quantity);
                    rows = db.update("cartitem", values, "id = ?", new String[]{String.valueOf(id)});
                } else {
                    // Nếu quantity = 1 mà trừ đi thì xóa luôn sản phẩm
                    rows = db.delete("cartitem", "id = ?", new String[]{String.valueOf(id)});
                }
            }
            cursor.close();
        } finally {
            db.close();
        }
        return rows;
    }

    // Xóa sản phẩm khỏi giỏ
    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            rows = db.delete("cartitem", "id = ?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
        return rows;
    }
    // Tính tổng tiền giỏ hàng
    public double getTotalPrice() {
        double total = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT SUM(price * quantity) AS total FROM cartitem",
                    null
            );
            if (cursor.moveToFirst()) {
                total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return total;
    }
    // Lấy cart item theo itemId
    public cartitem getCartItemByItemId(int itemId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        cartitem c = null;

        try {
            cursor = db.rawQuery("SELECT * FROM cartitem WHERE id = ?", new String[]{String.valueOf(itemId)});
            if (cursor.moveToFirst()) {
                c = new cartitem();
                c.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                c.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
                c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                c.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                c.setQuantity(cursor.getInt(cursor.getColumnIndexOrThrow("quantity")));
                c.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return c;
    }

    // Thêm 1 cart item mới
    public long insertCartItem(cartitem c) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId;
        try {
            ContentValues values = new ContentValues();
            values.put("userid", c.getUserid());
            values.put("name", c.getName());
            values.put("price", c.getPrice());
            values.put("quantity", c.getQuantity());
            values.put("image", c.getImage());
            newId = db.insert("cartitem", null, values);
        } finally {
            db.close();
        }
        return newId;
    }
    public void clearCart() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("cartitem", null, null);
        db.close();
    }


    // Cập nhật lại cart item (ví dụ khi thay đổi số lượng)
    public int updateCartItem(cartitem c) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows;
        try {
            ContentValues values = new ContentValues();
            values.put("userid", c.getUserid());
            values.put("name", c.getName());
            values.put("price", c.getPrice());
            values.put("quantity", c.getQuantity());
            values.put("image", c.getImage());
            rows = db.update("cartitem", values, "id = ?", new String[]{String.valueOf(c.getId())});
        } finally {
            db.close();
        }
        return rows;
    }

}
