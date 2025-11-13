package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.dao.CartDao;
import com.example.project.models.cartitem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<cartitem> cartList;
    private CartDao cartDao;
    private OnCartChangedListener listener;

    public interface OnCartChangedListener {
        void onCartUpdated();
    }

    public CartAdapter(Context context, List<cartitem> cartList, OnCartChangedListener listener) {
        this.context = context;
        this.cartList = cartList;
        this.listener = listener;
        this.cartDao = new CartDao(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        cartitem item = cartList.get(position);

        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));
        holder.tvQuantity.setText(String.valueOf(item.getQuantity()));

        // Load ảnh bằng Picasso (bạn cần import Picasso lib)
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Picasso.get().load(item.getImage())
                    .placeholder(R.drawable.placeholder)
                    .into(holder.imgItem);
        }

        // Nút tăng
        holder.btnAdd.setOnClickListener(v -> {
            cartDao.addQuantity(item.getId());
            listener.onCartUpdated();
        });

        // Nút giảm
        holder.btnMinus.setOnClickListener(v -> {
            cartDao.returnQuantity(item.getId());
            listener.onCartUpdated();
        });

        // Nút xóa
        holder.btnDelete.setOnClickListener(v -> {
            cartDao.delete(item.getId());
            listener.onCartUpdated();
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName, tvPrice, tvQuantity;
        ImageButton btnAdd, btnMinus, btnDelete;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            imgItem = itemView.findViewById(R.id.imgCartFood);
            tvName = itemView.findViewById(R.id.tvCartFoodName);
            tvPrice = itemView.findViewById(R.id.tvCartFoodPrice);
            tvQuantity = itemView.findViewById(R.id.tvQuantity);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}
