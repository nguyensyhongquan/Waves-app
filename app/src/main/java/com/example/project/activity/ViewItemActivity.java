package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.ItemAdapter;
import com.example.project.dao.CategoryDao;
import com.example.project.dao.ItemDao;
import com.example.project.models.category;
import com.example.project.models.item;

import java.util.List;

public class ViewItemActivity extends AppCompatActivity {

    private RecyclerView rvItemList;
    private Button btnAddItem, btnBack;
    private ItemDao itemDao;
    private CategoryDao categoryDao;
    private List<item> items;
    private List<category> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_item);

        rvItemList = findViewById(R.id.rvItemList);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnBack = findViewById(R.id.btnBack);

        itemDao = new ItemDao(this);
        categoryDao = new CategoryDao(this);

        loadData();

        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddItemActivity.class);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // reload khi quay lại
    }

    private void loadData() {
        items = itemDao.getAll();
        categories = categoryDao.getAll();
        ItemAdapter adapter = new ItemAdapter(this, items, categories);
        rvItemList.setLayoutManager(new LinearLayoutManager(this));
        rvItemList.setAdapter(adapter);
    }
}
