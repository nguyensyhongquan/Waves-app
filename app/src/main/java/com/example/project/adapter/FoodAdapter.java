package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.models.item;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private Context context;
    private List<item> itemList;

    public FoodAdapter(Context context, List<item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    public void updateList(List<item> newList) {
        itemList = newList;
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
        holder.tvName.setText(currentItem.getName());
        holder.tvPrice.setText("$" + currentItem.getPrice());

        // Load ảnh từ drawable theo tên
        if (currentItem.getImage() != null && !currentItem.getImage().isEmpty()) {
            int resId = context.getResources().getIdentifier(
                    currentItem.getImage(), "drawable", context.getPackageName());
            if (resId != 0) {
                holder.imgFood.setImageResource(resId);
            } else {
                holder.imgFood.setImageResource(R.drawable.placeholder);
            }
        } else {
            holder.imgFood.setImageResource(R.drawable.placeholder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFood;
        TextView tvName, tvPrice;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFood = itemView.findViewById(R.id.imgFood);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
        }
    }
}
