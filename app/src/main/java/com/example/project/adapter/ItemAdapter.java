package com.example.project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.dao.ItemDao;
import com.example.project.models.item;
import com.example.project.models.category;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    private Context context;
    private List<item> itemList;
    private List<category> categoryList;
    private ItemDao itemDao;

    public ItemAdapter(Context context, List<item> itemList, List<category> categoryList) {
        this.context = context;
        this.itemList = itemList;
        this.categoryList = categoryList;
        this.itemDao = new ItemDao(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        item i = itemList.get(position);
        holder.tvName.setText(i.getName());
        holder.tvPrice.setText(String.format("$%.2f", i.getPrice()));

        // tìm tên category theo id
        String catName = "Unknown";
        for (category c : categoryList) {
            if (c.getId() == i.getCategoryid()) {
                catName = c.getName();
                break;
            }
        }
        holder.tvCategoryName.setText("Category: " + catName);

        if (i.getImage() != null) {
            byte[] decoded = Base64.decode(i.getImage(), Base64.DEFAULT);
            holder.imgItem.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
        } else {
            holder.imgItem.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.btnDelete.setOnClickListener(v -> confirmDelete(i, position));
    }

    private void confirmDelete(item i, int pos) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    itemDao.delete(i.getId());
                    itemList.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(context, "Deleted successfully!", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgItem;
        TextView tvName, tvCategoryName, tvPrice;
        Button btnDelete;

        public ViewHolder(View v) {
            super(v);
            imgItem = v.findViewById(R.id.imgItem);
            tvName = v.findViewById(R.id.tvName);
            tvCategoryName = v.findViewById(R.id.tvCategoryName);
            tvPrice = v.findViewById(R.id.tvPrice);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
