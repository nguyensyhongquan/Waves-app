package com.example.project.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.project.databasehelper.DatabaseHelper;
import com.example.project.models.category;

import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private DatabaseHelper dbHelper;

    public CategoryDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // GetAll
    public List<category> getAll() {
        List<category> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, name, image, description FROM category", null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    category c = new category();
                    c.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    c.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                    c.setImage(cursor.isNull(cursor.getColumnIndexOrThrow("image")) ? null
                            : cursor.getString(cursor.getColumnIndexOrThrow("image")));
                    c.setDescription(cursor.isNull(cursor.getColumnIndexOrThrow("description")) ? null
                            : cursor.getString(cursor.getColumnIndexOrThrow("description")));
                    list.add(c);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return list;
    }

    // GetById
    public category getById(int id) {
        category term = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT id, name, image, description FROM category WHERE id = ?",
                    new String[]{String.valueOf(id)});
            if (cursor != null && cursor.moveToFirst()) {
                term = new category();
                term.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                term.setName(cursor.getString(cursor.getColumnIndexOrThrow("name")));
                term.setImage(cursor.isNull(cursor.getColumnIndexOrThrow("image")) ? null
                        : cursor.getString(cursor.getColumnIndexOrThrow("image")));
                term.setDescription(cursor.isNull(cursor.getColumnIndexOrThrow("description")) ? null
                        : cursor.getString(cursor.getColumnIndexOrThrow("description")));
            }
        } finally {
            if (cursor != null) cursor.close();
            db.close();
        }
        return term;
    }

    // Create -> returns inserted row id (or -1 if failed)
    public long create(category term) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put("name", term.getName());
            values.put("image", term.getImage());
            values.put("description", term.getDescription());
            newId = db.insert("category", null, values);
        } finally {
            db.close();
        }
        return newId;
    }

    // Delete -> returns number of rows deleted
    public int delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rows = 0;
        try {
            rows = db.delete("category", "id = ?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
        return rows;
    }
}
