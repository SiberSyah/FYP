package com.dct3194.projekakhir.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Order;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {

    private List<Order> orderList;

    public AdminOrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_admin_order,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Order order = orderList.get(position);

        // =========================
        // Masa Order
        // =========================
        holder.tvOrderTime.setText(
                order.getTime() != null ? order.getTime() : "-"
        );

        // =========================
        // Nama Pelanggan
        // =========================
        holder.tvCustomerName.setText(
                order.getCustomerName() != null ? order.getCustomerName() : "Customer"
        );

        // =========================
        // Jenis / Model Aircond
        // =========================
        holder.tvAircond.setText(
                order.getProductName() != null ? order.getProductName() : (order.getAircondType() != null ? order.getAircondType() : "Aircond Unit")
        );

        // =========================
        // Kapasiti HP / Package
        // =========================
        holder.tvType.setText(
                order.getHp() != null ? order.getHp() : "-"
        );

        // =========================
        // Alamat Pelanggan
        // =========================
        holder.tvAddress.setText(
                order.getCustomerAddress() != null ? order.getCustomerAddress() : "-"
        );

        // =========================
        // Status & Warna Badge
        // =========================
        String status = order.getStatus() != null ? order.getStatus() : "Pending";
        holder.tvStatus.setText(status);

        if (status.equalsIgnoreCase("Completed")) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#3158B8"));
        } else if (status.equalsIgnoreCase("Pending")) {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#FF9800"));
        } else {
            holder.tvStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
        }
    }

    @Override
    public int getItemCount() {
        return orderList != null ? orderList.size() : 0;
    }

    // =========================
    // ViewHolder
    // =========================
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvOrderTime;
        TextView tvCustomerName;
        TextView tvAircond;
        TextView tvType;
        TextView tvAddress;
        TextView tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvOrderTime = itemView.findViewById(R.id.tvOrderTime);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
            tvAircond = itemView.findViewById(R.id.tvAircond);
            tvType = itemView.findViewById(R.id.tvType);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}