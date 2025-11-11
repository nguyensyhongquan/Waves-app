package com.example.project.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.project.R;
import com.example.project.dao.CategoryDao;
import com.example.project.models.category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {
    private Context context;
    private List<category> list;
    private CategoryDao dao;

    public CategoryAdapter(Context context, List<category> list, CategoryDao dao) {
        this.context = context;
        this.list = list;
        this.dao = dao;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflate view nếu chưa có
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_item_category, parent, false);
        }

        ImageView img = convertView.findViewById(R.id.imgCategory);
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvDesc = convertView.findViewById(R.id.tvCategoryDesc);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        category c = list.get(position);
        tvName.setText(c.getName());
        tvDesc.setText(c.getDescription());

        // ✅ Giải mã Base64 để hiển thị ảnh
        if (c.getImage() != null && !c.getImage().isEmpty()) {
            try {
                byte[] decoded = Base64.decode(c.getImage(), Base64.DEFAULT);
                img.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
            } catch (Exception e) {
                img.setImageResource(R.drawable.ic_launcher_background);
            }
        } else {
            img.setImageResource(R.drawable.ic_launcher_background);
        }

        // ✅ Xử lý nút Delete có xác nhận
        btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Category")
                    .setMessage("Are you sure you want to delete this category?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        dao.delete(c.getId());
                        list.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        return convertView;
    }
}
