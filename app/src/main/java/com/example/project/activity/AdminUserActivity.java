package com.example.project.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.example.project.adapter.UserAdapter;
import com.example.project.dao.UserDao;
import com.example.project.models.user;
import com.example.project.R;

import java.util.List;

public class AdminUserActivity extends Activity {
    private ListView listViewUser;
    private Button btnBack;
    private UserDao dao;
    private UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_user);

        listViewUser = findViewById(R.id.listViewUser);
        btnBack = findViewById(R.id.btnBack);

        dao = new UserDao(this);
        List<user> users = dao.getAllOtherThanAdmin(); // lấy toàn bộ user
        adapter = new UserAdapter(this, users, dao);
        listViewUser.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());
    }
}
