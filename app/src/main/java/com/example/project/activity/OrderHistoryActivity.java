package com.example.project.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.HistoryAdapter;
import com.example.project.dao.HistoryDao;
import com.example.project.models.historyitem;
import android.widget.ImageView;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerHistory;
    private HistoryAdapter adapter;
    private HistoryDao historyDao;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_history_activity);

        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);
        if(currentUserId == -1){
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recyclerHistory = findViewById(R.id.recyclerHistory);
        recyclerHistory.setLayoutManager(new LinearLayoutManager(this));

        historyDao = new HistoryDao(this);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        loadOrderHistory();
    }

    private void loadOrderHistory() {
        List<historyitem> historyList = historyDao.getOrderByUserId(currentUserId);

        if(historyList.isEmpty()){
            Toast.makeText(this, "No orders found!", Toast.LENGTH_SHORT).show();
        }

        adapter = new HistoryAdapter(this, historyList);
        recyclerHistory.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadOrderHistory(); // reload khi quay lại màn hình
    }
}
