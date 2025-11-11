package com.example.project.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.HistoryAdapter;
import com.example.project.dao.HistoryDao;
import com.example.project.models.historyitem;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private Button btnBackHome;
    private HistoryDao historyDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_sold);

        rvHistory = findViewById(R.id.rvHistory);
        btnBackHome = findViewById(R.id.btnBackHome);
        historyDao = new HistoryDao(this);

        loadHistory();

        btnBackHome.setOnClickListener(v -> finish()); // quay lại home
    }

    private void loadHistory() {
        List<historyitem> list = historyDao.getAll();
        HistoryAdapter adapter = new HistoryAdapter(this, list);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);
    }
}