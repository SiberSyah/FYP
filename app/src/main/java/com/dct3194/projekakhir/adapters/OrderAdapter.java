package com.dct3194.projekakhir.adapters;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.activities.OrderStatusActivity;
import com.dct3194.projekakhir.models.Order;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class OrderAdapter
        extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private static final String TAG = "OrderAdapter";

    private final List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    // =========================
    // Create ViewHolder
    // =========================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_order_card,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    // =========================
    // Bind Data
    // =========================

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Order order = orderList.get(position);

        // =========================
        // Order ID
        // =========================

        String orderId =
                order.getOrderId();

        if (orderId == null ||
                orderId.trim().isEmpty()) {

            orderId = "";
        }

        final String finalOrderId = orderId;

        // =========================
        // Order Type
        // =========================

        String orderType =
                order.getOrderType();

        if (orderType == null ||
                orderType.trim().isEmpty()) {

            orderType = "Buy";
        }

        final String finalOrderType =
                orderType;

        holder.txtOrderType.setText(
                finalOrderType
        );

        // =========================
        // Status
        // =========================

        String status =
                order.getStatus();

        if (status == null ||
                status.trim().isEmpty()) {

            status = "Pending";
        }

        final String finalStatus =
                status;

        holder.txtStatusBadge.setText(
                finalStatus
        );

        // =========================
        // Product Name
        // =========================

        String productName =
                order.getProductName();

        if (productName == null ||
                productName.trim().isEmpty()) {

            productName = "Aircond Service";
        }

        final String finalProductName =
                productName;

        holder.txtOrderName.setText(
                finalProductName
        );

        // =========================
        // Date & Time
        // =========================

        String date =
                order.getDate();

        String time =
                order.getTime();

        String dateTime = "";

        if (date != null &&
                !date.trim().isEmpty()) {

            dateTime = date;
        }

        if (time != null &&
                !time.trim().isEmpty()) {

            if (!dateTime.isEmpty()) {
                dateTime += " | ";
            }

            dateTime += time;
        }

        holder.txtOrderDateTime.setText(
                dateTime
        );

        // =========================
        // Order Type Icon
        // =========================

        if (finalOrderType.equalsIgnoreCase(
                "Maintenance")) {

            holder.imgOrderTypeIcon.setImageResource(
                    R.drawable.ic_maintenance
            );

        } else {

            holder.imgOrderTypeIcon.setImageResource(
                    R.drawable.ic_orders
            );
        }

        // =========================
        // Default Product Image
        // =========================

        holder.imgOrderProduct.setImageResource(
                R.drawable.ic_aircond
        );

        String imageUrl =
                order.getImageUrl();

        holder.imgOrderProduct.setTag(
                imageUrl
        );

        // =========================
        // Firebase Storage Image
        // =========================

        if (imageUrl != null &&
                !imageUrl.trim().isEmpty()) {

            try {

                StorageReference imageReference =
                        FirebaseStorage
                                .getInstance()
                                .getReferenceFromUrl(
                                        imageUrl
                                );

                imageReference
                        .getBytes(
                                5 * 1024 * 1024
                        )
                        .addOnSuccessListener(
                                bytes -> {

                                    Object currentTag =
                                            holder.imgOrderProduct
                                                    .getTag();

                                    if (imageUrl.equals(
                                            currentTag)) {

                                        Bitmap bitmap =
                                                BitmapFactory
                                                        .decodeByteArray(
                                                                bytes,
                                                                0,
                                                                bytes.length
                                                        );

                                        if (bitmap != null) {

                                            holder.imgOrderProduct
                                                    .setImageBitmap(
                                                            bitmap
                                                    );
                                        }
                                    }
                                }
                        )
                        .addOnFailureListener(
                                e -> {

                                    Log.e(
                                            TAG,
                                            "Failed to load order image: "
                                                    + e.getMessage()
                                    );

                                    Object currentTag =
                                            holder.imgOrderProduct
                                                    .getTag();

                                    if (imageUrl.equals(
                                            currentTag)) {

                                        holder.imgOrderProduct
                                                .setImageResource(
                                                        R.drawable.ic_aircond
                                                );
                                    }
                                }
                        );

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "Invalid image URL: "
                                + imageUrl,
                        e
                );

                holder.imgOrderProduct
                        .setImageResource(
                                R.drawable.ic_aircond
                        );
            }
        }

        // =========================
        // Card Click
        // =========================

        holder.itemView.setOnClickListener(
                v -> {

                    Intent intent =
                            new Intent(
                                    v.getContext(),
                                    OrderStatusActivity.class
                            );

                    // =========================
                    // Firestore Order ID
                    // =========================

                    intent.putExtra(
                            "orderId",
                            finalOrderId
                    );

                    // =========================
                    // Current Data
                    // =========================

                    intent.putExtra(
                            "status",
                            finalStatus
                    );

                    intent.putExtra(
                            "orderType",
                            finalOrderType
                    );

                    intent.putExtra(
                            "productName",
                            finalProductName
                    );

                    intent.putExtra(
                            "date",
                            date
                    );

                    intent.putExtra(
                            "time",
                            time
                    );

                    v.getContext()
                            .startActivity(intent);
                }
        );
    }

    // =========================
    // Item Count
    // =========================

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // =========================
    // ViewHolder
    // =========================

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        ImageView imgOrderTypeIcon;
        ImageView imgOrderProduct;

        TextView txtOrderType;
        TextView txtStatusBadge;
        TextView txtOrderName;
        TextView txtOrderDateTime;

        public ViewHolder(
                @NonNull View itemView) {

            super(itemView);

            imgOrderTypeIcon =
                    itemView.findViewById(
                            R.id.imgOrderTypeIcon
                    );

            imgOrderProduct =
                    itemView.findViewById(
                            R.id.imgOrderProduct
                    );

            txtOrderType =
                    itemView.findViewById(
                            R.id.txtOrderType
                    );

            txtStatusBadge =
                    itemView.findViewById(
                            R.id.txtStatusBadge
                    );

            txtOrderName =
                    itemView.findViewById(
                            R.id.txtOrderName
                    );

            txtOrderDateTime =
                    itemView.findViewById(
                            R.id.txtOrderDateTime
                    );
        }
    }
}