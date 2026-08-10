package com.dct3194.projekakhir.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AircondAdapter
        extends RecyclerView.Adapter<AircondAdapter.ViewHolder> {

    private static final String TAG = "AircondAdapter";

    public interface OnAircondClickListener {
        void onAircondClick(Aircond aircond);
    }

    private final List<Aircond> aircondList;
    private final OnAircondClickListener listener;

    public AircondAdapter(
            List<Aircond> aircondList,
            OnAircondClickListener listener) {

        this.aircondList = aircondList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aircond, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        Aircond aircond = aircondList.get(position);

        // =========================
        // Basic Information
        // =========================

        holder.txtName.setText(aircond.getName());
        holder.txtSpec.setText(aircond.getBrand());

        holder.txtPrice.setText(
                "From RM " + aircond.getPrice1HP()
        );

        // =========================
        // Default Image
        // =========================

        holder.imgAircond.setImageResource(
                R.mipmap.ic_launcher
        );

        // Simpan URL pada ImageView
        holder.imgAircond.setTag(
                aircond.getImageUrl()
        );

        // =========================
        // Firebase Image
        // =========================

        String imageUrl = aircond.getImageUrl();

        Log.d(
                TAG,
                "========================================"
        );

        Log.d(
                TAG,
                "Aircond: " + aircond.getName()
        );

        Log.d(
                TAG,
                "Brand: " + aircond.getBrand()
        );

        Log.d(
                TAG,
                "Image URL: " + imageUrl
        );

        if (imageUrl == null ||
                imageUrl.trim().isEmpty()) {

            Log.e(
                    TAG,
                    "Image URL kosong untuk: "
                            + aircond.getName()
            );

        } else {

            try {

                Log.d(
                        TAG,
                        "Mula getReferenceFromUrl..."
                );

                StorageReference imageReference =
                        FirebaseStorage
                                .getInstance()
                                .getReferenceFromUrl(imageUrl);

                Log.d(
                        TAG,
                        "Storage Reference berjaya: "
                                + imageReference.getPath()
                );

                imageReference
                        .getBytes(5 * 1024 * 1024)

                        .addOnSuccessListener(bytes -> {

                            Log.d(
                                    TAG,
                                    "IMAGE DOWNLOAD BERJAYA: "
                                            + aircond.getName()
                            );

                            Object currentTag =
                                    holder.imgAircond.getTag();

                            if (imageUrl.equals(currentTag)) {

                                Bitmap bitmap =
                                        BitmapFactory.decodeByteArray(
                                                bytes,
                                                0,
                                                bytes.length
                                        );

                                if (bitmap != null) {

                                    holder.imgAircond
                                            .setImageBitmap(bitmap);

                                    Log.d(
                                            TAG,
                                            "Bitmap berjaya dipaparkan: "
                                                    + aircond.getName()
                                    );

                                } else {

                                    Log.e(
                                            TAG,
                                            "Bitmap NULL untuk: "
                                                    + aircond.getName()
                                    );
                                }

                            } else {

                                Log.d(
                                        TAG,
                                        "ViewHolder sudah berubah. "
                                                + "Gambar tidak dipaparkan."
                                );
                            }

                        })

                        .addOnFailureListener(e -> {

                            Log.e(
                                    TAG,
                                    "IMAGE DOWNLOAD GAGAL: "
                                            + aircond.getName()
                            );

                            Log.e(
                                    TAG,
                                    "Error message: "
                                            + e.getMessage()
                            );

                            Log.e(
                                    TAG,
                                    "Error class: "
                                            + e.getClass().getName(),
                                    e
                            );

                            Object currentTag =
                                    holder.imgAircond.getTag();

                            if (imageUrl.equals(currentTag)) {

                                holder.imgAircond.setImageResource(
                                        R.mipmap.ic_launcher
                                );
                            }
                        });

            } catch (Exception e) {

                Log.e(
                        TAG,
                        "EXCEPTION getReferenceFromUrl untuk: "
                                + aircond.getName()
                );

                Log.e(
                        TAG,
                        "Error message: "
                                + e.getMessage()
                );

                Log.e(
                        TAG,
                        "Error class: "
                                + e.getClass().getName(),
                        e
                );

                holder.imgAircond.setImageResource(
                        R.mipmap.ic_launcher
                );
            }
        }

        // =========================
        // Card Click
        // =========================

        holder.card.setOnClickListener(v -> {

            if (listener != null) {
                listener.onAircondClick(aircond);
            }
        });
    }

    @Override
    public int getItemCount() {
        return aircondList.size();
    }

    // =========================
    // ViewHolder
    // =========================

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView imgAircond;
        TextView txtName;
        TextView txtSpec;
        TextView txtPrice;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            card = itemView.findViewById(
                    R.id.cardAircond
            );

            imgAircond = itemView.findViewById(
                    R.id.imgAircond
            );

            txtName = itemView.findViewById(
                    R.id.txtName
            );

            txtSpec = itemView.findViewById(
                    R.id.txtSpec
            );

            txtPrice = itemView.findViewById(
                    R.id.txtPrice
            );
        }
    }
}