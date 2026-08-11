package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class AddAddressActivity extends AppCompatActivity {

    // =========================
    // Customer Information
    // =========================

    private EditText etFullName;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etCity;
    private EditText etPoscode;
    private EditText etState;

    private Button btnContinue;

    // =========================
    // Bottom Navigation
    // =========================

    private ImageView iconProfile;
    private ImageView iconHome;
    private ImageView iconOrders;

    // =========================
    // Booking Data
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

    private boolean maintenance;
    private String service;
    private String maintenanceType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_add_address
        );

        // =========================
        // Initialize Views
        // =========================

        etFullName =
                findViewById(R.id.etFullName);

        etPhone =
                findViewById(R.id.etPhone);

        etAddress =
                findViewById(R.id.etAddress);

        etCity =
                findViewById(R.id.etCity);

        etPoscode =
                findViewById(R.id.etPoscode);

        etState =
                findViewById(R.id.etState);

        btnContinue =
                findViewById(R.id.btnContinue);

        // =========================
        // Bottom Navigation Icons
        // =========================

        iconProfile =
                findViewById(R.id.iconProfile);

        iconHome =
                findViewById(R.id.iconHome);

        iconOrders =
                findViewById(R.id.iconOrders);

        // =========================
        // Customer Information Page
        // =========================
        // This page belongs to the
        // buying flow, so Home is
        // selected just like the
        // other buying pages.

        selectHome();

        // =========================
        // Receive Booking Data
        // =========================

        Intent previousIntent =
                getIntent();

        maintenance =
                previousIntent.getBooleanExtra(
                        "maintenance",
                        false
                );

        service =
                previousIntent.getStringExtra(
                        "service"
                );

        maintenanceType =
                previousIntent.getStringExtra(
                        "type"
                );

        aircond =
                (Aircond) previousIntent
                        .getSerializableExtra("aircond");

        model =
                previousIntent.getStringExtra(
                        "model"
                );

        hp =
                previousIntent.getStringExtra(
                        "hp"
                );

        selectedType =
                previousIntent.getStringExtra(
                        "type"
                );

        selectedPackage =
                previousIntent.getStringExtra(
                        "package"
                );

        price =
                previousIntent.getIntExtra(
                        "price",
                        0
                );

        totalPrice =
                previousIntent.getIntExtra(
                        "totalPrice",
                        0
                );

        installation =
                previousIntent.getBooleanExtra(
                        "installation",
                        false
                );

        date =
                previousIntent.getStringExtra(
                        "date"
                );

        time =
                previousIntent.getStringExtra(
                        "time"
                );

        // =========================
        // Continue
        // =========================

        btnContinue.setOnClickListener(v -> {

            if (!validateForm()) {
                return;
            }

            // =========================
            // Customer Information
            // =========================

            String customerName =
                    etFullName
                            .getText()
                            .toString()
                            .trim();

            String customerPhone =
                    etPhone
                            .getText()
                            .toString()
                            .trim();

            String customerAddress =
                    etAddress
                            .getText()
                            .toString()
                            .trim()
                            + ", "
                            + etCity
                            .getText()
                            .toString()
                            .trim()
                            + ", "
                            + etPoscode
                            .getText()
                            .toString()
                            .trim()
                            + ", "
                            + etState
                            .getText()
                            .toString()
                            .trim();

            // =========================
            // Go To Summary
            // =========================

            Intent intent;

            if (maintenance) {

                intent = new Intent(
                        AddAddressActivity.this,
                        MaintenanceSummaryActivity.class
                );

            } else {

                intent = new Intent(
                        AddAddressActivity.this,
                        BuySummaryActivity.class
                );
            }

            // =========================
            // Maintenance Data
            // =========================

            intent.putExtra(
                    "maintenance",
                    maintenance
            );

            intent.putExtra(
                    "service",
                    service
            );

            intent.putExtra(
                    "maintenanceType",
                    maintenanceType
            );

            // =========================
            // Product
            // =========================

            intent.putExtra(
                    "aircond",
                    aircond
            );

            intent.putExtra(
                    "model",
                    model
            );

            intent.putExtra(
                    "hp",
                    hp
            );

            intent.putExtra(
                    "type",
                    selectedType
            );

            intent.putExtra(
                    "package",
                    selectedPackage
            );

            // =========================
            // Price
            // =========================

            intent.putExtra(
                    "price",
                    price
            );

            intent.putExtra(
                    "totalPrice",
                    totalPrice
            );

            // =========================
            // Installation
            // =========================

            intent.putExtra(
                    "installation",
                    installation
            );

            // =========================
            // Booking
            // =========================

            intent.putExtra(
                    "date",
                    date
            );

            intent.putExtra(
                    "time",
                    time
            );

            // =========================
            // Customer
            // =========================

            intent.putExtra(
                    "customerName",
                    customerName
            );

            intent.putExtra(
                    "customerPhone",
                    customerPhone
            );

            intent.putExtra(
                    "customerAddress",
                    customerAddress
            );

            startActivity(intent);
        });

        // =========================
        // Bottom Navigation
        // =========================

        findViewById(R.id.navHome)
                .setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    AddAddressActivity.this,
                                    MainActivity.class
                            );

                    intent.putExtra(
                            "page",
                            "home"
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );

                    startActivity(intent);
                });

        findViewById(R.id.navProfile)
                .setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    AddAddressActivity.this,
                                    MainActivity.class
                            );

                    intent.putExtra(
                            "page",
                            "profile"
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );

                    startActivity(intent);
                });

        findViewById(R.id.navOrders)
                .setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    AddAddressActivity.this,
                                    MainActivity.class
                            );

                    intent.putExtra(
                            "page",
                            "orders"
                    );

                    intent.setFlags(
                            Intent.FLAG_ACTIVITY_CLEAR_TOP
                    );

                    startActivity(intent);
                });
    }

    // =========================================================
    // RESET BOTTOM NAVIGATION ICONS
    // =========================================================

    private void resetIcons() {

        if (iconProfile != null) {

            iconProfile.setImageTintList(
                    ColorStateList.valueOf(
                            Color.GRAY
                    )
            );
        }

        if (iconHome != null) {

            iconHome.setImageTintList(
                    ColorStateList.valueOf(
                            Color.GRAY
                    )
            );
        }

        if (iconOrders != null) {

            iconOrders.setImageTintList(
                    ColorStateList.valueOf(
                            Color.GRAY
                    )
            );
        }
    }

    // =========================================================
    // SELECT HOME
    // =========================================================

    private void selectHome() {

        resetIcons();

        if (iconHome != null) {

            iconHome.setImageTintList(
                    ColorStateList.valueOf(
                            Color.parseColor(
                                    "#3158B8"
                            )
                    )
            );
        }
    }

    // =========================================================
    // SELECT PROFILE
    // =========================================================

    private void selectProfile() {

        resetIcons();

        if (iconProfile != null) {

            iconProfile.setImageTintList(
                    ColorStateList.valueOf(
                            Color.parseColor(
                                    "#3158B8"
                            )
                    )
            );
        }
    }

    // =========================================================
    // SELECT ORDERS
    // =========================================================

    private void selectOrders() {

        resetIcons();

        if (iconOrders != null) {

            iconOrders.setImageTintList(
                    ColorStateList.valueOf(
                            Color.parseColor(
                                    "#3158B8"
                            )
                    )
            );
        }
    }

    // =========================================================
    // Validate Form
    // =========================================================

    private boolean validateForm() {

        if (etFullName
                .getText()
                .toString()
                .trim()
                .isEmpty()

                || etPhone
                .getText()
                .toString()
                .trim()
                .isEmpty()

                || etAddress
                .getText()
                .toString()
                .trim()
                .isEmpty()

                || etCity
                .getText()
                .toString()
                .trim()
                .isEmpty()

                || etPoscode
                .getText()
                .toString()
                .trim()
                .isEmpty()

                || etState
                .getText()
                .toString()
                .trim()
                .isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        return true;
    }
}