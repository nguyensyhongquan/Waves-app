package com.example.project.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.adapter.CartAdapter;
import com.example.project.dao.CartDao;
import com.example.project.dao.HistoryDao;
import com.example.project.models.cartitem;
import com.example.project.models.historyitem;

import java.util.Date;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {

    private RecyclerView recyclerCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageView btnBack;

    private CartDao cartDao;
    private List<cartitem> cartList;
    private CartAdapter cartAdapter;
    private HistoryDao historyDao;
    private int currentUserId; // user hiện tại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        // Lấy userId từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        currentUserId = prefs.getInt("userId", -1);
        if (currentUserId == -1) {
            Toast.makeText(this, "User not found!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cartDao = new CartDao(this);
        historyDao = new HistoryDao(this);

        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        loadCart();

        btnCheckout.setOnClickListener(v -> checkout());
    }

    private void loadCart() {
        cartList = cartDao.getAll(); // Lấy toàn bộ cart
        cartAdapter = new CartAdapter(this, cartList, this);
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerCartItems.setAdapter(cartAdapter);

        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (cartitem c : cartList) {
            total += c.getPrice() * c.getQuantity();
        }
        tvTotalPrice.setText(String.format("$%.2f", total));
    }

    private void checkout() {
        if (cartList == null || cartList.isEmpty()) {
            Toast.makeText(this, "Cart is empty!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lưu tất cả item vào history cùng userId
        for (cartitem c : cartList) {
            historyitem h = new historyitem();
            h.setUserid(currentUserId); // ✅ sử dụng user hiện tại
            h.setName(c.getName());
            h.setPrice(c.getPrice());
            h.setQuantity(c.getQuantity());
            h.setImage(c.getImage());
            h.setOrderdate(new Date());
            historyDao.create(h);
        }

        // Xóa toàn bộ cart
        cartDao.clearCart(); // tạo hàm clearCart trong CartDao
        cartList.clear();
        cartAdapter.notifyDataSetChanged();
        updateTotal();

        Toast.makeText(this, "Checkout successful!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCartUpdated() {
        loadCart(); // cập nhật khi thay đổi quantity/xóa item
    }
}
