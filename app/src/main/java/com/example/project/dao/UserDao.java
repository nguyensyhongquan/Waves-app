package com.example.project.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.databasehelper.DatabaseHelper;
import com.example.project.models.user;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // 🔹 Kiểm tra login: có thể dùng email hoặc phone + password
    public user authRequest(String emailOrPhone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        user account = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user WHERE (email = ? OR phone = ?) AND password = ?",
                new String[]{emailOrPhone, emailOrPhone, password}
        );

        if (cursor.moveToFirst()) {
            account = new user();
            account.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
            account.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            account.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            account.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            account.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            account.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            account.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        }

        cursor.close();
        db.close();
        return account; // Trả về null nếu không tìm thấy
    }

    // 🔹 Lấy account theo id
    public user getAccountById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        user account = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM user WHERE userid = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            account = new user();
            account.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("userid")));
            account.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
            account.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("email")));
            account.setPassword(cursor.getString(cursor.getColumnIndexOrThrow("password")));
            account.setAddress(cursor.getString(cursor.getColumnIndexOrThrow("address")));
            account.setPhone(cursor.getString(cursor.getColumnIndexOrThrow("phone")));
            account.setRole(cursor.getString(cursor.getColumnIndexOrThrow("role")));
        }

        cursor.close();
        db.close();
        return account;
    }

    // 🔹 Tạo account mới
    public long createAccount(user account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("email", account.getEmail());
        values.put("password", account.getPassword());
        values.put("address", account.getAddress());
        values.put("phone", account.getPhone());
        values.put("role", account.getRole());

        long id = db.insert("user", null, values);
        db.close();
        return id; // Trả về id của account mới
    }

    // 🔹 Cập nhật account
    public int updateAccount(user account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("email", account.getEmail());
        values.put("password", account.getPassword());
        values.put("address", account.getAddress());
        values.put("phone", account.getPhone());
        values.put("role", account.getRole());

        int rows = db.update("user", values, "userid = ?", new String[]{String.valueOf(account.getUserid())});
        db.close();
        return rows; // Trả về số dòng được cập nhật
    }
}
