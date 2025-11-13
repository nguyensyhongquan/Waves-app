package com.example.project.dao;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.databasehelper.DatabaseHelper;
import com.example.project.models.item;
import com.example.project.models.category;


import java.util.ArrayList;
import java.util.List;
public class ItemDao {
    private DatabaseHelper dbHelper;

    public ItemDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // GetAll
    public List<item> getAll() {
        List<item> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, name, description, price, image, categoryid FROM item", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    item i = new item();
                    i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    i.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    i.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    i.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    i.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    i.setCategoryid(cursor.getInt(cursor.getColumnIndexOrThrow("categoryid")));
                    list.add(i);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    // GetById
    public item getById(int id) {
        item i = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, name, description, price, image, categoryid FROM item WHERE id = ?",
                    new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                i = new item();
                i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                i.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                i.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                i.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                i.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                i.setCategoryid(cursor.getInt(cursor.getColumnIndexOrThrow("categoryid")));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return i;
    }

    // Create
    public long create(item i) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("name", i.getName());
            values.put("description", i.getDescription());
            values.put("price", i.getPrice());
            values.put("image", i.getImage());
            values.put("categoryid", i.getCategoryid());
            newId = db.insert("item", null, values);
        } finally {
            db.close();
        }
        return newId;
    }

    // Update
    public int update(item i) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            ContentValues values = new ContentValues();
            values.put("name", i.getName());
            values.put("description", i.getDescription());
            values.put("price", i.getPrice());
            values.put("image", i.getImage());
            values.put("categoryid", i.getCategoryid());
            rows = db.update("item", values, "id = ?", new String[]{String.valueOf(i.getId())});
        } finally {
            db.close();
        }
        return rows;
    }

    // Delete
    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            rows = db.delete("item", "id = ?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
        return rows;
    }


    // Search by name (không phân biệt hoa thường, tìm theo một phần)
    public List<item> searchByName(String name) {
        List<item> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Dùng LOWER để không phân biệt hoa thường, LIKE để tìm một phần
            cursor = db.rawQuery(
                    "SELECT id, name, description, price, image, categoryid FROM item WHERE LOWER(name) LIKE ?",
                    new String[]{"%" + name.toLowerCase() + "%"}
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    item i = new item();
                    i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    i.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    i.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    i.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    i.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    i.setCategoryid(cursor.getInt(cursor.getColumnIndexOrThrow("categoryid")));
                    list.add(i);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }

        return list;
    }
    public List<item> getByCategory(int categoryId) {
        List<item> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(
                    "SELECT id, name, description, price, image FROM item WHERE categoryid = ?",
                    new String[]{String.valueOf(categoryId)}
            );
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    item i = new item();
                    i.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    i.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    i.setDescription(cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    i.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow("price")));
                    i.setImage(cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    list.add(i);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }


}
