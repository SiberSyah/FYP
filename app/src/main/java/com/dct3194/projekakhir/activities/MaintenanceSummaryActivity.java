package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.dct3194.projekakhir.R;

public class MaintenanceSummaryActivity extends AppCompatActivity {

    private TextView tvSummaryPrice;
    private Button btnConfirm;
    private ImageView btnBack;

    // Row items
    private View rowService, rowHP, rowType, rowDate, rowTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_summary);

        // Initialize Views
        tvSummaryPrice = findViewById(R.id.tvSummaryPrice);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnBack = findViewById(R.id.btnBack);

        rowService = findViewById(R.id.rowService);
        rowHP = findViewById(R.id.rowHP);
        rowType = findViewById(R.id.rowType);
        rowDate = findViewById(R.id.rowDate);
        rowTime = findViewById(R.id.rowTime);

        // Set Labels
        setupRow(rowService, "Service");
        setupRow(rowHP, "HP Rating");
        setupRow(rowType, "AC Type");
        setupRow(rowDate, "Selected Date");
        setupRow(rowTime, "Selected Time");

        // Get Data
        Intent intent = getIntent();
        if (intent != null) {
            setRowValue(rowService, intent.getStringExtra("service"));
            setRowValue(rowHP, intent.getStringExtra("hp"));
            setRowValue(rowType, intent.getStringExtra("type"));
            setRowValue(rowDate, intent.getStringExtra("date"));
            setRowValue(rowTime, intent.getStringExtra("time"));
            tvSummaryPrice.setText(intent.getStringExtra("price"));
        }

        btnBack.setOnClickListener(v -> finish());
        
        btnConfirm.setOnClickListener(v -> {
            Intent nextIntent = new Intent(this, PaymentMethodActivity.class);
            startActivity(nextIntent);
        });
    }

    private void setupRow(View row, String label) {
        TextView tvLabel = row.findViewById(R.id.tvLabel);
        tvLabel.setText(label);
    }

    private void setRowValue(View row, String value) {
        TextView tvValue = row.findViewById(R.id.tvValue);
        tvValue.setText(value);
    }
}
