package com.dct3194.projekakhir.adapters;

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

import java.util.List;

public class AircondAdapter extends RecyclerView.Adapter<AircondAdapter.ViewHolder> {

    public interface OnAircondClickListener {
        void onAircondClick(Aircond aircond);
    }

    private final List<Aircond> aircondList;
    private final OnAircondClickListener listener;

    public AircondAdapter(List<Aircond> aircondList,
                          OnAircondClickListener listener) {
        this.aircondList = aircondList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_aircond, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Aircond aircond = aircondList.get(position);

        holder.imgAircond.setImageResource(aircond.getImage());
        holder.txtName.setText(aircond.getName());

        holder.txtSpec.setText(aircond.getBrand());

        holder.txtPrice.setText("From RM " + aircond.getPrice1HP());

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

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        ImageView imgAircond;
        TextView txtName, txtSpec, txtPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            card = itemView.findViewById(R.id.cardAircond);
            imgAircond = itemView.findViewById(R.id.imgAircond);
            txtName = itemView.findViewById(R.id.txtName);
            txtSpec = itemView.findViewById(R.id.txtSpec);
            txtPrice = itemView.findViewById(R.id.txtPrice);
        }
    }
}