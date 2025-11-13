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
            Intent intent = new Intent(this, AdminCategoryActivity.class);
            startActivity(intent);
        });

        btnAllItemMenu.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminViewItemActivity.class);
            startActivity(intent);
        });

        btnSold.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });

        btnUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminUserActivity.class);
            startActivity(intent);
        });

        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(AdminHomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {
            // 🔹 Xóa userId trong SharedPreferences
            getSharedPreferences("AppPrefs", MODE_PRIVATE)
                    .edit()
                    .remove("userId")
                    .apply();

            // 🔹 Thông báo đăng xuất
            Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();

            // 🔹 Chuyển về trang đăng nhập
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            // 🔹 Đóng activity hiện tại
            finish();
        });
    }
}
