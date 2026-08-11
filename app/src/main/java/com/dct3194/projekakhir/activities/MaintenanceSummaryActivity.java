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
    private View rowService;
    private View rowHP;
    private View rowType;
    private View rowDate;
    private View rowTime;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_maintenance_summary
        );

        // =========================
        // Initialize Views
        // =========================

        tvSummaryPrice =
                findViewById(R.id.tvSummaryPrice);

        btnConfirm =
                findViewById(R.id.btnConfirm);

        btnBack =
                findViewById(R.id.btnBack);

        rowService =
                findViewById(R.id.rowService);

        rowHP =
                findViewById(R.id.rowHP);

        rowType =
                findViewById(R.id.rowType);

        rowDate =
                findViewById(R.id.rowDate);

        rowTime =
                findViewById(R.id.rowTime);

        // =========================
        // Set Labels
        // =========================

        setupRow(
                rowService,
                "Service"
        );

        setupRow(
                rowHP,
                "HP Rating"
        );

        setupRow(
                rowType,
                "AC Type"
        );

        setupRow(
                rowDate,
                "Selected Date"
        );

        setupRow(
                rowTime,
                "Selected Time"
        );

        // =========================
        // Get Data
        // =========================

        Intent intent =
                getIntent();

        if (intent != null) {

            customerName =
                    intent.getStringExtra(
                            "customerName"
                    );

            customerPhone =
                    intent.getStringExtra(
                            "customerPhone"
                    );

            customerAddress =
                    intent.getStringExtra(
                            "customerAddress"
                    );

            setRowValue(
                    rowService,
                    intent.getStringExtra(
                            "service"
                    )
            );

            setRowValue(
                    rowHP,
                    intent.getStringExtra(
                            "hp"
                    )
            );

            setRowValue(
                    rowType,
                    intent.getStringExtra(
                            "type"
                    )
            );

            setRowValue(
                    rowDate,
                    intent.getStringExtra(
                            "date"
                    )
            );

            setRowValue(
                    rowTime,
                    intent.getStringExtra(
                            "time"
                    )
            );

            String priceText =
                    intent.getStringExtra(
                            "price"
                    );

            tvSummaryPrice.setText(
                    priceText != null
                            ? priceText
                            : "RM 0"
            );
        }

        // =========================
        // Back
        // =========================

        btnBack.setOnClickListener(v ->
                finish()
        );

        // =========================
        // Confirm
        // =========================

        btnConfirm.setOnClickListener(v -> {

            Intent nextIntent =
                    new Intent(
                            MaintenanceSummaryActivity.this,
                            PaymentMethodActivity.class
                    );
            // =========================
            // Maintenance
            // =========================

            nextIntent.putExtra(
                    "maintenance",
                    true
            );

            nextIntent.putExtra(
                    "maintenanceType",
                    getIntent().getStringExtra(
                            "type"
                    )
            );

            nextIntent.putExtra(
                    "service",
                    getIntent().getStringExtra(
                            "service"
                    )
            );

            nextIntent.putExtra(
                    "hp",
                    getIntent().getStringExtra(
                            "hp"
                    )
            );

            // =========================
            // Maintenance Price
            // =========================

            String priceString =
                    getIntent().getStringExtra(
                            "price"
                    );

            int maintenancePrice =
                    parsePrice(
                            priceString
                    );

            // Keep original price string
            nextIntent.putExtra(
                    "maintenancePrice",
                    priceString
            );

            // IMPORTANT:
            // PaymentMethodActivity uses
            // totalPrice for Stripe.
            nextIntent.putExtra(
                    "totalPrice",
                    maintenancePrice
            );

            // Also pass price
            nextIntent.putExtra(
                    "price",
                    maintenancePrice
            );

            // =========================
            // Customer Information
            // =========================

            nextIntent.putExtra(
                    "customerName",
                    customerName
            );

            nextIntent.putExtra(
                    "customerPhone",
                    customerPhone
            );

            nextIntent.putExtra(
                    "customerAddress",
                    customerAddress
            );

            // =========================
            // Booking
            // =========================

            nextIntent.putExtra(
                    "date",
                    getIntent().getStringExtra(
                            "date"
                    )
            );

            nextIntent.putExtra(
                    "time",
                    getIntent().getStringExtra(
                            "time"
                    )
            );

            // =========================
            // Open Payment
            // =========================

            startActivity(
                    nextIntent
            );
        });
    }

    // =========================
    // Parse Maintenance Price
    // =========================

    private int parsePrice(
            String priceText) {

        if (priceText == null ||
                priceText.trim().isEmpty()) {

            return 0;
        }

        try {

            String cleaned =
                    priceText
                            .replace(
                                    "RM",
                                    ""
                            )
                            .replace(
                                    "rm",
                                    ""
                            )
                            .replace(
                                    ",",
                                    ""
                            )
                            .replace(
                                    ".00",
                                    ""
                            )
                            .trim();

            return Integer.parseInt(
                    cleaned
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =========================
    // Setup Row
    // =========================

    private void setupRow(
            View row,
            String label) {

        TextView tvLabel =
                row.findViewById(
                        R.id.tvLabel
                );

        tvLabel.setText(
                label
        );
    }

    // =========================
    // Set Row Value
    // =========================

    private void setRowValue(
            View row,
            String value) {

        TextView tvValue =
                row.findViewById(
                        R.id.tvValue
                );

        tvValue.setText(
                value != null
                        ? value
                        : ""
        );
    }
}