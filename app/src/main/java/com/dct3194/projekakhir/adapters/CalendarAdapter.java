package com.dct3194.projekakhir.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.CalendarDate;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder> {

    private final List<CalendarDate> calendarList;
    private final OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(int position);
    }

    public CalendarAdapter(List<CalendarDate> calendarList,
                           OnDateClickListener listener) {
        this.calendarList = calendarList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                 int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_calendar_day, parent, false);

        return new CalendarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder,
                                 int position) {

        CalendarDate item = calendarList.get(position);

        holder.txtDay.setText(item.getDay());

        if (!item.isCurrentMonth()) {

            holder.txtDay.setText("");

            holder.cardDay.setCardBackgroundColor(Color.TRANSPARENT);

            holder.itemView.setOnClickListener(null);

            return;
        }

        holder.txtDay.setTextColor(Color.BLACK);

        if (item.isSelected()) {

            holder.cardDay.setCardBackgroundColor(
                    Color.parseColor("#3158B8"));

            holder.txtDay.setTextColor(Color.WHITE);

        } else {

            holder.cardDay.setCardBackgroundColor(Color.TRANSPARENT);

            holder.txtDay.setTextColor(Color.BLACK);

        }

        holder.itemView.setOnClickListener(v -> {

            for (CalendarDate date : calendarList) {
                date.setSelected(false);
            }

            item.setSelected(true);

            notifyDataSetChanged();

            if (listener != null) {
                listener.onDateClick(position);
            }

        });

    }

    @Override
    public int getItemCount() {
        return calendarList.size();
    }

    static class CalendarViewHolder extends RecyclerView.ViewHolder {

        CardView cardDay;
        TextView txtDay;

        CalendarViewHolder(@NonNull View itemView) {
            super(itemView);

            cardDay = itemView.findViewById(R.id.cardDay);
            txtDay = itemView.findViewById(R.id.txtDay);
        }
    }
}