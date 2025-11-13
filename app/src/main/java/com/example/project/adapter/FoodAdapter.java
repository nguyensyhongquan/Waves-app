package com.example.project.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.dao.CartDao;
import com.example.project.models.cartitem;
import com.example.project.models.item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private final Context context;
    private List<item> itemList;
    private final CartDao cartDao;

    public FoodAdapter(Context context, List<item> itemList) {
        this.context = context;
        this.itemList = itemList;
        this.cartDao = new CartDao(context);
    }

    public void updateList(List<item> newList) {
        this.itemList = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        item currentItem = itemList.get(position);
        if (currentItem == null) return;

        holder.tvName.setText(currentItem.getName());
        holder.tvPrice.setText(String.format("$%.2f", currentItem.getPrice()));

        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            try {
                byte[] decoded = Base64.decode(currentItem.getImage(), Base64.DEFAULT);
                holder.imgFood.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
            } catch (Exception e) {
                holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.imgFood.setImageResource(R.drawable.ic_launcher_background);
        }

        // Sự kiện thêm vào giỏ hàng
        holder.btnAddToCart.setOnClickListener(v -> {
            cartitem existingItem = cartDao.getCartItemByItemId(currentItem.getId());
            if (existingItem != null) {
                existingItem.setQuantity(existingItem.getQuantity() + 1);
                cartDao.updateCartItem(existingItem);
                Toast.makeText(context, R.string.cart_updated, Toast.LENGTH_SHORT).show();
            } else {
                cartitem newItem = new cartitem(
                        currentItem.getId(),
                        currentItem.getName(),
                        currentItem.getPrice(),
                        1,
                        currentItem.getImage()
                );
                cartDao.insertCartItem(newItem);
                Toast.makeText(context, R.string.added_to_cart, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice;
        Button btnAddToCart;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
