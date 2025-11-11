package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.UserDao;
import com.example.project.models.user;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmailPhone, etPassword;
    private Button btnLogin;
    private TextView tvSignUp;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Ánh xạ view
        etEmailPhone = findViewById(R.id.etLoginEmailPhone);
        etPassword = findViewById(R.id.etLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvSignUp = findViewById(R.id.tvDontHaveAccount);

        // Khởi tạo UserDao
        userDao = new UserDao(this);

        // Xử lý login
        btnLogin.setOnClickListener(v -> loginUser());

        // Chuyển sang màn hình đăng ký
        tvSignUp.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });
    }

    private void loginUser() {
        String emailOrPhone = etEmailPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (emailOrPhone.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // Sử dụng UserDao để kiểm tra tài khoản
        user account = userDao.authRequest(emailOrPhone, password);

        if (account != null) {
            // Login thành công
            Toast.makeText(this, "Xin chào " + account.getName() + " (" + account.getRole() + ")", Toast.LENGTH_SHORT).show();

            // Chuyển sang HomeActivity
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            intent.putExtra("username", account.getName());
            startActivity(intent);
            finish();
        } else {
            // Login thất bại
            Toast.makeText(this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
        }
    }
}
