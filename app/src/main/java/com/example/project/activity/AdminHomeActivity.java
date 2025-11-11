package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.HistoryDao;

public class AdminHomeActivity extends AppCompatActivity {

    private TextView tvRevenueDay, tvRevenueMonth, tvRevenueYear;
    private Button btnViewMenu, btnAllItemMenu, btnSold, btnUser, btnProfile, btnLogout;
    private HistoryDao historyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);

        // Ánh xạ TextView
        tvRevenueDay = findViewById(R.id.tvRevenueDay);
        tvRevenueMonth = findViewById(R.id.tvRevenueMonth);
        tvRevenueYear = findViewById(R.id.tvRevenueYear);

        // Ánh xạ Button
        btnViewMenu = findViewById(R.id.btnViewMenu);
        btnAllItemMenu = findViewById(R.id.btnAllItemMenu);
        btnSold = findViewById(R.id.btnSold);
        btnUser = findViewById(R.id.btnUser);
        btnProfile = findViewById(R.id.btnProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Lấy dữ liệu doanh thu
        historyDao = new HistoryDao(this);
        double revenueDay = historyDao.getRevenueByDay();
        double revenueMonth = historyDao.getRevenueByMonth();
        double revenueYear = historyDao.getRevenueByYear();

        tvRevenueDay.setText(String.format("%.0f$", revenueDay));
        tvRevenueMonth.setText(String.format("%.0f$", revenueMonth));
        tvRevenueYear.setText(String.format("%.0f$", revenueYear));

        // Sự kiện cho các nút
        btnViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, CategoryActivity.class);
            startActivity(intent);
        });

        btnViewMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, ViewItemActivity.class);
            startActivity(intent);
        });

        btnSold.setOnClickListener(v -> {
            // Intent intent = new Intent(this, SoldActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Sold", Toast.LENGTH_SHORT).show();
        });

        btnUser.setOnClickListener(v -> {
            // Intent intent = new Intent(this, UserListActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "User", Toast.LENGTH_SHORT).show();
        });

        btnProfile.setOnClickListener(v -> {
            // Intent intent = new Intent(this, AdminProfileActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        });

        btnLogout.setOnClickListener(v -> {
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
