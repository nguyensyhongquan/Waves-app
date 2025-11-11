package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        ImageView img = convertView.findViewById(R.id.imgCategory);
        TextView tvName = convertView.findViewById(R.id.tvCategoryName);
        TextView tvDesc = convertView.findViewById(R.id.tvCategoryDesc);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        category c = list.get(position);
        tvName.setText(c.getName());
        tvDesc.setText(c.getDescription());
        Glide.with(context).load(c.getImage()).placeholder(R.drawable.ic_launcher_background).into(img);

        btnDelete.setOnClickListener(v -> {
            dao.delete(c.getId());
            list.remove(position);
            notifyDataSetChanged();
        });

        return convertView;
    }
}