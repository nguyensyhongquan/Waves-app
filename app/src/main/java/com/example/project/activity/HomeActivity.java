package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.FoodAdapter;
import com.example.project.dao.ItemDao;
import com.example.project.models.item;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerFoodList;
    private FoodAdapter adapter;
    private List<item> itemList;
    private ItemDao itemDao;

    private EditText etSearch;
    private LinearLayout navHome, navCart, navOrder, navProfile, navInformation;
    private ImageView imgBanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Khởi tạo DAO
        itemDao = new ItemDao(this);

        // Ánh xạ view
        recyclerFoodList = findViewById(R.id.recyclerFoodList);
        etSearch = findViewById(R.id.etSearch);
        imgBanner = findViewById(R.id.imgBanner);

        navHome = findViewById(R.id.navHome);
        navCart = findViewById(R.id.navCart);
        navOrder = findViewById(R.id.navOrder);
        navProfile = findViewById(R.id.navProfile);
        navInformation = findViewById(R.id.navinformation);

        // Load dữ liệu món ăn từ DB
        itemList = itemDao.getAll();

        // Setup RecyclerView
        recyclerFoodList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FoodAdapter(this, itemList);
        recyclerFoodList.setAdapter(adapter);

        // Navigation
        setupBottomNav();

        // Tìm kiếm
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = etSearch.getText().toString().trim();
            searchItem(keyword);
            return true;
        });
    }

    private void searchItem(String keyword) {
        if (keyword.isEmpty()) {
            itemList = itemDao.getAll();
        } else {
            itemList = itemDao.searchByName(keyword);
        }
        adapter.updateList(itemList);
    }

    private void setupBottomNav() {
        navHome.setOnClickListener(v ->
                Toast.makeText(this, "You are already on Home", Toast.LENGTH_SHORT).show());

        navCart.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, CartActivity.class)));

        navOrder.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, OrderHistoryActivity.class)));

        navProfile.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, ProfileActivity.class)));

        navInformation.setOnClickListener(v ->
                startActivity(new Intent(HomeActivity.this, InformationActivity.class)));
    }
}
