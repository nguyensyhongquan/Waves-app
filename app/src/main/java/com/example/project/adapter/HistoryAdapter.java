package com.example.project.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.project.R;
import com.example.project.models.historyitem;
import com.example.project.models.user;
import com.example.project.dao.UserDao;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private Context context;
    private List<historyitem> historyList;
    private UserDao userDao;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public HistoryAdapter(Context context, List<historyitem> historyList) {
        this.context = context;
        this.historyList = historyList;
        this.userDao = new UserDao(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.admin_item_sold, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        historyitem h = historyList.get(position);

        holder.tvItemName.setText(h.getName());
        holder.tvPriceQty.setText(String.format(Locale.getDefault(), "$%.2f x%d", h.getPrice(), h.getQuantity()));

        // 🧍 Lấy tên người dùng theo userid
        user u = userDao.getAccountById(h.getUserid());
        holder.tvBuyerName.setText("Buyer: " + (u != null ? u.getName() : "Unknown"));

        // 🗓️ Format ngày
        if (h.getOrderdate() != null)
            holder.tvOrderDate.setText(sdf.format(h.getOrderdate()));
        else
            holder.tvOrderDate.setText("No date");

        // 🖼️ Giải mã ảnh Base64
        if (h.getImage() != null && !h.getImage().isEmpty()) {
            byte[] decoded = Base64.decode(h.getImage(), Base64.DEFAULT);
            holder.imgHistoryItem.setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.length));
        } else {
            holder.imgHistoryItem.setImageResource(R.drawable.ic_launcher_background);
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgHistoryItem;
        TextView tvItemName, tvBuyerName, tvPriceQty, tvOrderDate;

        public ViewHolder(View v) {
            super(v);
            imgHistoryItem = v.findViewById(R.id.imgHistoryItem);
            tvItemName = v.findViewById(R.id.tvItemName);
            tvBuyerName = v.findViewById(R.id.tvBuyerName);
            tvPriceQty = v.findViewById(R.id.tvPriceQty);
            tvOrderDate = v.findViewById(R.id.tvOrderDate);
        }
    }
}
