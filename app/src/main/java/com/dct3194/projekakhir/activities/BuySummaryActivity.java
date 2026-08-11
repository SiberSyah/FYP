package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class BuySummaryActivity extends AppCompatActivity {

    // =========================
    // Product
    // =========================

    private TextView tvProductName;
    private TextView tvProductFeature;
    private TextView tvProductModel;

    // =========================
    // Price
    // =========================

    private TextView tvInstallationTitle;
    private TextView tvInstallationDesc;
    private TextView tvAirCondPrice;
    private TextView tvInstallationPrice;
    private TextView tvTotalPrice;

    private LinearLayout rowInstallationFee;

    // =========================
    // Customer Information
    // =========================

    private TextView tvCustomerName;
    private TextView tvCustomerPhone;
    private TextView tvCustomerAddress;

    // =========================
    // Booking Information
    // =========================

    private TextView tvBookingDate;
    private TextView tvBookingTime;

    // =========================
    // Button
    // =========================

    private Button btnPayNow;

    // =========================
    // Bottom Navigation
    // =========================

    private ImageView btnNavProfile;
    private ImageView btnNavHome;
    private ImageView btnNavOrders;

    // =========================
    // Data
    // =========================

    private Aircond aircond;

    private String model;
    private String hp;
    private String selectedType;
    private String selectedPackage;

    private int price;
    private int totalPrice;

    private boolean installation;

    private String date;
    private String time;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    // =========================
    // Colors
    // =========================

    private static final int COLOR_ACTIVE =
            Color.parseColor("#3158B8");

    private static final int COLOR_INACTIVE =
            Color.GRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_buy_summary
        );

        // =========================
        // Initialize Views
        // =========================

        tvProductName =
                findViewById(R.id.tvProductName);

        tvProductFeature =
                findViewById(R.id.tvProductFeature);

        tvProductModel =
                findViewById(R.id.tvProductModel);

        tvInstallationTitle =
                findViewById(R.id.tvInstallationTitle);

        tvInstallationDesc =
                findViewById(R.id.tvInstallationDesc);

        tvAirCondPrice =
                findViewById(R.id.tvAirCondPrice);

        tvInstallationPrice =
                findViewById(R.id.tvInstallationPrice);

        tvTotalPrice =
                findViewById(R.id.tvTotalPrice);

        rowInstallationFee =
                findViewById(R.id.rowInstallationFee);

        tvCustomerName =
                findViewById(R.id.tvCustomerName);

        tvCustomerPhone =
                findViewById(R.id.tvCustomerPhone);

        tvCustomerAddress =
                findViewById(R.id.tvCustomerAddress);

        tvBookingDate =
                findViewById(R.id.tvBookingDate);

        tvBookingTime =
                findViewById(R.id.tvBookingTime);

        btnPayNow =
                findViewById(R.id.btnPayNow);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // =========================
        // Bottom Navigation
        // =========================

        btnNavProfile =
                findViewById(R.id.btnNavProfile);

        btnNavHome =
                findViewById(R.id.btnNavHome);

        btnNavOrders =
                findViewById(R.id.btnNavOrders);

        // =========================
        // Set Bottom Navigation
        // =========================

        selectHome();

        // =========================
        // Bottom Navigation Click
        // =========================

        btnNavProfile.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            BuySummaryActivity.this,
                            MainActivity.class
                    );

            intent.putExtra(
                    "page",
                    "profile"
            );

            startActivity(intent);

            finish();
        });

        btnNavHome.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            BuySummaryActivity.this,
                            MainActivity.class
                    );

            intent.putExtra(
                    "page",
                    "home"
            );

            startActivity(intent);

            finish();
        });

        btnNavOrders.setOnClickListener(v -> {

            Intent intent =
                    new Intent(
                            BuySummaryActivity.this,
                            MainActivity.class
                    );

            intent.putExtra(
                    "page",
                    "orders"
            );

            startActivity(intent);

            finish();
        });

        // =========================
        // Receive Intent
        // =========================

        Intent intent =
                getIntent();

        aircond =
                (Aircond) intent.getSerializableExtra(
                        "aircond"
                );

        model =
                intent.getStringExtra(
                        "model"
                );

        hp =
                intent.getStringExtra(
                        "hp"
                );

        selectedType =
                intent.getStringExtra(
                        "type"
                );

        selectedPackage =
                intent.getStringExtra(
                        "package"
                );

        price =
                intent.getIntExtra(
                        "price",
                        0
                );

        totalPrice =
                intent.getIntExtra(
                        "totalPrice",
                        0
                );

        installation =
                intent.getBooleanExtra(
                        "installation",
                        false
                );

        date =
                intent.getStringExtra(
                        "date"
                );

        time =
                intent.getStringExtra(
                        "time"
                );

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

        // =========================
        // Product Information
        // =========================

        String brand = "";

        if (aircond != null) {

            brand =
                    aircond.getBrand();
        }

        String fullProductName =
                (
                        brand
                                + " "
                                + (
                                aircond != null
                                        ? aircond.getName()
                                        : ""
                        )
                ).trim();

        tvProductName.setText(
                fullProductName
        );

        // =========================
        // Model
        // =========================

        if (model != null &&
                !model.isEmpty()) {

            tvProductModel.setText(
                    "• Model : " + model
            );
        }

        // =========================
        // Product Features
        // =========================

        StringBuilder feature =
                new StringBuilder();

        if (hp != null &&
                !hp.isEmpty()) {

            feature.append("• ")
                    .append(hp);
        }

        if (selectedType != null &&
                !selectedType.isEmpty()) {

            if (feature.length() > 0) {

                feature.append("\n");
            }

            feature.append("• ")
                    .append(selectedType);
        }

        if (selectedPackage != null &&
                !selectedPackage.isEmpty()) {

            if (feature.length() > 0) {

                feature.append("\n");
            }

            feature.append("• ")
                    .append(selectedPackage);
        }

        tvProductFeature.setText(
                feature.toString()
        );

        // =========================
        // Aircond Price
        // =========================

        tvAirCondPrice.setText(
                String.format(
                        "RM %,d",
                        price
                )
        );

        // =========================
        // Installation
        // =========================

        if (installation) {

            tvInstallationTitle.setText(
                    "Installation :"
            );

            tvInstallationDesc.setText(
                    "Professional Installation Included"
            );

            rowInstallationFee.setVisibility(
                    android.view.View.VISIBLE
            );

            int installationFee = 0;

            if (aircond != null) {

                installationFee =
                        aircond.getInstallationPrice();
            }

            tvInstallationPrice.setText(
                    String.format(
                            "RM %,d",
                            installationFee
                    )
            );

        } else {

            tvInstallationTitle.setText(
                    "Option :"
            );

            tvInstallationDesc.setText(
                    "Unit Only"
            );

            rowInstallationFee.setVisibility(
                    android.view.View.GONE
            );
        }

        // =========================
        // Total Price
        // =========================

        tvTotalPrice.setText(
                String.format(
                        "RM %,d",
                        totalPrice
                )
        );

        // =========================
        // Customer Information
        // =========================

        if (tvCustomerName != null &&
                customerName != null) {

            tvCustomerName.setText(
                    customerName
            );
        }

        if (tvCustomerPhone != null &&
                customerPhone != null) {

            tvCustomerPhone.setText(
                    customerPhone
            );
        }

        if (tvCustomerAddress != null &&
                customerAddress != null) {

            tvCustomerAddress.setText(
                    customerAddress
            );
        }

        // =========================
        // Booking Information
        // =========================

        if (tvBookingDate != null &&
                date != null) {

            tvBookingDate.setText(
                    date
            );
        }

        if (tvBookingTime != null &&
                time != null) {

            tvBookingTime.setText(
                    time
            );
        }

        // =========================
        // Pay Now
        // =========================

        btnPayNow.setOnClickListener(v -> {

            Intent paymentIntent =
                    new Intent(
                            BuySummaryActivity.this,
                            PaymentMethodActivity.class
                    );

            // =========================
            // Product
            // =========================

            paymentIntent.putExtra(
                    "aircond",
                    aircond
            );

            paymentIntent.putExtra(
                    "model",
                    model
            );

            paymentIntent.putExtra(
                    "hp",
                    hp
            );

            paymentIntent.putExtra(
                    "type",
                    selectedType
            );

            paymentIntent.putExtra(
                    "package",
                    selectedPackage
            );

            // =========================
            // Price
            // =========================

            paymentIntent.putExtra(
                    "price",
                    price
            );

            paymentIntent.putExtra(
                    "totalPrice",
                    totalPrice
            );

            // =========================
            // Installation
            // =========================

            paymentIntent.putExtra(
                    "installation",
                    installation
            );

            // =========================
            // Booking
            // =========================

            paymentIntent.putExtra(
                    "date",
                    date
            );

            paymentIntent.putExtra(
                    "time",
                    time
            );

            // =========================
            // Customer
            // =========================

            paymentIntent.putExtra(
                    "customerName",
                    customerName
            );

            paymentIntent.putExtra(
                    "customerPhone",
                    customerPhone
            );

            paymentIntent.putExtra(
                    "customerAddress",
                    customerAddress
            );

            startActivity(
                    paymentIntent
            );
        });
    }

    // =========================================================
    // Bottom Navigation - Reset
    // =========================================================

    private void resetNavigationIcons() {

        btnNavProfile.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_INACTIVE
                )
        );

        btnNavHome.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_INACTIVE
                )
        );

        btnNavOrders.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_INACTIVE
                )
        );
    }

    // =========================================================
    // Select Home
    // =========================================================

    private void selectHome() {

        resetNavigationIcons();

        btnNavHome.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_ACTIVE
                )
        );
    }

    // =========================================================
    // Select Profile
    // =========================================================

    private void selectProfile() {

        resetNavigationIcons();

        btnNavProfile.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_ACTIVE
                )
        );
    }

    // =========================================================
    // Select Orders
    // =========================================================

    private void selectOrders() {

        resetNavigationIcons();

        btnNavOrders.setImageTintList(
                ColorStateList.valueOf(
                        COLOR_ACTIVE
                )
        );
    }
}