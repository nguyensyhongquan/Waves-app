package com.example.project.databasehelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "project_demo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  Bảng User
        db.execSQL("CREATE TABLE user (" +
                "userid INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "email TEXT, " +
                "password TEXT, " +
                "address TEXT, " +
                "phone TEXT, " +
                "role TEXT)");

        //  Bảng Category
        db.execSQL("CREATE TABLE category (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "image TEXT, " +
                "description TEXT)");

        //  Bảng Item
        db.execSQL("CREATE TABLE item (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "description TEXT, " +
                "price REAL, " +
                "image TEXT, " +
                "categoryid INTEGER, " +
                "FOREIGN KEY(categoryid) REFERENCES category(id))");

        //  Bảng CartItem
        db.execSQL("CREATE TABLE cartitem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER, " +
                "name TEXT, " +
                "price REAL, " +
                "quantity INTEGER, " +
                "image TEXT, " +
                "FOREIGN KEY(userid) REFERENCES user(userid))");

        //  Bảng HistoryItem
        db.execSQL("CREATE TABLE historyitem (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "userid INTEGER, " +
                "name TEXT, " +
                "price REAL, " +
                "quantity INTEGER, " +
                "image TEXT, " +
                "orderdate TEXT, " +
                "FOREIGN KEY(userid) REFERENCES user(userid))");

        // -------------------------------------------------


        //  Dữ liệu mẫu User
        db.execSQL("INSERT INTO user (name, email, password, address, phone, role) VALUES " +
                "('Admin', 'admin@gmail.com', '123456', 'Hà Nội', '0123456789', 'Admin'), " +
                "('Huy Đậu', 'huy@gmail.com', '123456', 'Đà Nẵng', '0987654321', 'User');");

        // 🏷 Dữ liệu mẫu Category
        db.execSQL("INSERT INTO category (name, image, description) VALUES " +
                "('Đồ uống', 'drink.png', 'Các loại nước giải khát'), " +
                "('Thức ăn nhanh', 'fastfood.png', 'Đồ ăn nhẹ tiện lợi'), " +
                "('Bánh kẹo', 'snack.png', 'Các loại bánh và snack');");

        //  Dữ liệu mẫu Item
        db.execSQL("INSERT INTO item (name, description, price, image, categoryid) VALUES " +
                "('Coca-Cola', 'Nước ngọt có gas 330ml', 10000, 'coca.png', 1), " +
                "('Pepsi', 'Nước ngọt có gas 330ml', 9000, 'pepsi.png', 1), " +
                "('Khoai tây chiên', 'Snack vị phô mai', 15000, 'chips.png', 2), " +
                "('Gà rán', 'Gà rán giòn rụm', 30000, 'chicken.png', 2), " +
                "('Bánh Oreo', 'Bánh quy kem chocolate', 12000, 'oreo.png', 3);");

        //  Dữ liệu mẫu CartItem (giỏ hàng của user thứ 2)
        db.execSQL("INSERT INTO cartitem (userid, name, price, quantity, image) VALUES " +
                "(2, 'Coca-Cola', 10000, 2, 'coca.png'), " +
                "(2, 'Bánh Oreo', 12000, 1, 'oreo.png');");

        //  Dữ liệu mẫu HistoryItem (lịch sử mua hàng)
        db.execSQL("INSERT INTO historyitem (userid, name, price, quantity, image, orderdate) VALUES " +
                "(2, 'Pepsi', 9000, 3, 'pepsi.png', '2025-11-10'), " +
                "(2, 'Gà rán', 30000, 1, 'chicken.png', '2025-11-09');");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS historyitem");
        db.execSQL("DROP TABLE IF EXISTS cartitem");
        db.execSQL("DROP TABLE IF EXISTS item");
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS user");
        onCreate(db);
    }
}

