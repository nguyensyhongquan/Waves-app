package com.example.project.activity;

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
import com.example.project.models.cartitem;

import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartChangedListener {

    private RecyclerView recyclerCartItems;
    private TextView tvTotalPrice;
    private Button btnCheckout;
    private ImageView btnBack;

    private CartDao cartDao;
    private List<cartitem> cartList;
    private CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_activity);

        cartDao = new CartDao(this);

        recyclerCartItems = findViewById(R.id.recyclerCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnCheckout = findViewById(R.id.btnCheckout);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        loadCart();

        btnCheckout.setOnClickListener(v -> {
            Toast.makeText(this, "Checkout thành công!", Toast.LENGTH_SHORT).show();
            // Ở đây bạn có thể clear giỏ sau khi thanh toán:
            // cartDao.clearCart(); (nếu bạn thêm hàm này)
        });
    }

    private void loadCart() {
        cartList = cartDao.getAll();
        adapter = new CartAdapter(this, cartList, this);
        recyclerCartItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerCartItems.setAdapter(adapter);

        updateTotal();
    }

    private void updateTotal() {
        double total = 0;
        for (cartitem c : cartList) {
            total += c.getPrice() * c.getQuantity();
        }
        tvTotalPrice.setText(String.format("$%.2f", total));
    }

    @Override
    public void onCartUpdated() {
        loadCart(); // Cập nhật lại danh sách và tổng tiền
    }
}
