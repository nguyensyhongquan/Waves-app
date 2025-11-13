package com.example.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.CategoryHomeAdapter;
import com.example.project.adapter.FoodAdapter;
import com.example.project.dao.CategoryDao;
import com.example.project.dao.ItemDao;
import com.example.project.models.category;
import com.example.project.models.item;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerFoodList;
    private FoodAdapter foodAdapter;
    private List<item> itemList;
    private ItemDao itemDao;

    private RecyclerView recyclerCategory;
    private CategoryHomeAdapter categoryAdapter;
    private List<category> categoryList;
    private CategoryDao categoryDao;

    private EditText etSearch;
    private LinearLayout navHome, navCart, navOrder, navProfile, navOut;
    private ImageView imgBanner;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        // Lấy userid từ Intent
        currentUserId = getIntent().getIntExtra("userid", -1);
        // DAO
        itemDao = new ItemDao(this);
        categoryDao = new CategoryDao(this);

        // Views
        etSearch = findViewById(R.id.etSearch);
        imgBanner = findViewById(R.id.imgBanner);

        navHome = findViewById(R.id.navHome);
        navCart = findViewById(R.id.navCart);
        navOrder = findViewById(R.id.navOrder);
        navProfile = findViewById(R.id.navProfile);
        navOut = findViewById(R.id.btnLogout);

        recyclerFoodList = findViewById(R.id.recyclerFoodList);
        recyclerFoodList.setLayoutManager(new LinearLayoutManager(this));

        recyclerCategory = findViewById(R.id.recyclerCategory);
        recyclerCategory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        // Load món ăn trước (để adapter không null khi click category)
        itemList = itemDao.getAll();
        foodAdapter = new FoodAdapter(this, itemList);
        recyclerFoodList.setAdapter(foodAdapter);

        // Load category
        categoryList = categoryDao.getAll();
        categoryAdapter = new CategoryHomeAdapter(this, categoryList);
        recyclerCategory.setAdapter(categoryAdapter);

        // Category click listener
        categoryAdapter.setOnCategoryClickListener(c -> {
            int categoryId = c.getId(); // hoặc c.id
            itemList = itemDao.getByCategory(categoryId);
            foodAdapter.updateList(itemList);
        });

        // Navigation
        setupBottomNav();

        // Search
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchItem(etSearch.getText().toString().trim());
            }
            return false;
        });
    }

    private void searchItem(String keyword) {
        if (keyword.isEmpty()) itemList = itemDao.getAll();
        else itemList = itemDao.searchByName(keyword);
        foodAdapter.updateList(itemList);
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
        navOut.setOnClickListener(v -> {
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
