package com.example.project.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.models.category;

import java.util.List;

public class CategoryHomeAdapter extends RecyclerView.Adapter<CategoryHomeAdapter.ViewHolder> {

    private Context context;
    private List<category> list;
    private OnCategoryClickListener listener;

    public interface OnCategoryClickListener {
        void onCategoryClick(category c);
    }

    public void setOnCategoryClickListener(OnCategoryClickListener listener) {
        this.listener = listener;
    }

    public CategoryHomeAdapter(Context context, List<category> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_category_home, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        category c = list.get(position);
        holder.tvName.setText(c.getName());

        if (c.getImage() != null && !c.getImage().isEmpty()) {
            try {
                byte[] decoded = Base64.decode(c.getImage(), Base64.DEFAULT);
                holder.imgCategory.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
            } catch (Exception e) {
                holder.imgCategory.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            holder.imgCategory.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onCategoryClick(c);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgCategory;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCategory = itemView.findViewById(R.id.imgCategory);
            tvName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}
