package com.example.project.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.dao.UserDao;
import com.example.project.models.user;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmailPhone, etPassword, etConfirmPassword;
    private Button btnCreateAccount;

    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        etName = findViewById(R.id.etSignupName);
        etEmailPhone = findViewById(R.id.etSignupEmailPhone);
        etPassword = findViewById(R.id.etSignupPassword);
        etConfirmPassword = findViewById(R.id.etSignupConfirmPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        userDao = new UserDao(this);

        btnCreateAccount.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = etName.getText().toString().trim();
        String emailPhone = etEmailPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (name.isEmpty() || emailPhone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kiểm tra tài khoản đã tồn tại (theo email hoặc phone)
        if (userDao.authRequest(emailPhone, password) != null) {
            Toast.makeText(this, "Tài khoản đã tồn tại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo user mới
        user newUser = new user();
        newUser.setName(name);
        newUser.setEmail(emailPhone);
        newUser.setPhone(emailPhone);
        newUser.setPassword(password);
        newUser.setRole("user"); // role mặc định

        long id = userDao.createAccount(newUser);
        if (id > 0) {
            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            finish(); // quay về LoginActivity
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}
