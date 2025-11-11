package com.example.project.activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.example.project.adapter.CategoryAdapter;
import com.example.project.dao.CategoryDao;
import com.example.project.models.category;

import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private Button btnAddCategory;
    private ListView lvCategory;
    private CategoryDao categoryDao;
    private CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_category);

        btnBack = findViewById(R.id.btnBackHome);
        btnAddCategory = findViewById(R.id.btnAddCategory);
        lvCategory = findViewById(R.id.lvCategory);

        categoryDao = new CategoryDao(this);
        List<category> list = categoryDao.getAll();

        adapter = new CategoryAdapter(this, list, categoryDao);
        lvCategory.setAdapter(adapter);

        btnBack.setOnClickListener(v -> finish());

        btnAddCategory.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddCategoryActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list khi quay lại từ màn AddCategory
        List<category> list = categoryDao.getAll();
        adapter = new CategoryAdapter(this, list, categoryDao);
        lvCategory.setAdapter(adapter);
    }
}
