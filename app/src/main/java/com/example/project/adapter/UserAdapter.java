package com.example.project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.dao.UserDao;
import com.example.project.models.user;

import java.util.List;

public class UserAdapter extends BaseAdapter {
    private Context context;
    private List<user> list;
    private UserDao dao;

    public UserAdapter(Context context, List<user> list, UserDao dao) {
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
        return list.get(position).getUserid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.admin_user_row, parent, false);

        TextView tvUserName = convertView.findViewById(R.id.tvUserName);
        TextView tvUserEmail = convertView.findViewById(R.id.tvUserEmail);
        TextView tvUserPhone = convertView.findViewById(R.id.tvUserPhone);
        Button btnLockUnlock = convertView.findViewById(R.id.btnLockUnlock);

        user u = list.get(position);

        tvUserName.setText(u.getName());
        tvUserEmail.setText(u.getEmail());
        tvUserPhone.setText(u.getPhone());

        // Nếu role = null => bị khóa
        if (u.getRole() == null) {
            btnLockUnlock.setText("Bỏ khóa");
            btnLockUnlock.setBackgroundColor(0xFFE57373); // Đỏ nhạt
        } else {
            btnLockUnlock.setText("Khóa");
            btnLockUnlock.setBackgroundColor(0xFF81C784); // Xanh lá
        }

        btnLockUnlock.setOnClickListener(v -> {
            if (u.getRole() == null) {
                // Mở khóa
                u.setRole("User");
            } else {
                // Khóa tài khoản
                u.setRole(null);
            }
            dao.updateAccount(u); // cập nhật DB
            notifyDataSetChanged();
        });

        return convertView;
    }
}
