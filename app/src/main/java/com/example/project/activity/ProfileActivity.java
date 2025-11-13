package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.UserDao;
import com.example.project.models.user;

public class ProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnUpdateProfile;
    private ImageView btnBack;

    private UserDao userDao;
    private user currentUser;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // DAO
        userDao = new UserDao(this);

        // Views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnUpdateProfile = findViewById(R.id.btnUpdateProfile);
        btnBack = findViewById(R.id.btnBack);

        // Lấy userId từ SharedPreferences
        userId = getSharedPreferences("AppPrefs", MODE_PRIVATE).getInt("userId", -1);
        if (userId == -1) {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy thông tin user
        currentUser = userDao.getAccountById(userId);
        if (currentUser != null) {
            loadUserData();
        } else {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Update profile
        btnUpdateProfile.setOnClickListener(v -> updateProfile());

        // Nút back về HomeActivity
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadUserData() {
        etName.setText(currentUser.getName());
        etEmail.setText(currentUser.getEmail());
        etPhone.setText(currentUser.getPhone());
        etAddress.setText(currentUser.getAddress());
    }

    private void updateProfile() {
        currentUser.setName(etName.getText().toString().trim());
        currentUser.setEmail(etEmail.getText().toString().trim());
        currentUser.setPhone(etPhone.getText().toString().trim());
        currentUser.setAddress(etAddress.getText().toString().trim());

        int rows = userDao.updateAccount(currentUser);
        if (rows > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Update failed!", Toast.LENGTH_SHORT).show();
        }
    }
}
