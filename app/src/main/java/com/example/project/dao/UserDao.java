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

    // 🔹 1. AuthRequest: Kiểm tra login (email + password)
    public user authRequest(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        user account = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM account WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        if (cursor.moveToFirst()) {
            account = new user();
            account.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("accountid")));
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

    // 🔹 2. GetAccountById
    public user getAccountById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        user account = null;

        Cursor cursor = db.rawQuery(
                "SELECT * FROM account WHERE accountid = ?",
                new String[]{String.valueOf(id)}
        );

        if (cursor.moveToFirst()) {
            account = new user();
            account.setUserid(cursor.getInt(cursor.getColumnIndexOrThrow("accountid")));
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

    // 🔹 3. CreateAccount
    public long createAccount(user account) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", account.getName());
        values.put("email", account.getEmail());
        values.put("password", account.getPassword());
        values.put("address", account.getAddress());
        values.put("phone", account.getPhone());
        values.put("role", account.getRole());

        long id = db.insert("account", null, values);
        db.close();
        return id; // Trả về id của account mới
    }

    // 🔹 4. UpdateAccount
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

