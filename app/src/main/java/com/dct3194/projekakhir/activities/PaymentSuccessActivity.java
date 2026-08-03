package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class PaymentSuccessActivity extends AppCompatActivity {

    // Buttons
    private Button btnViewOrder, btnBackHome;

    // Bottom Navigation
    private LinearLayout navProfile, navHome, navOrders;
    private ImageView iconProfile, iconHome, iconOrders;

    // Booking Data
    private Aircond aircond;
    private String selectedModel;
    private String selectedHP;
    private String selectedPackage;
    private int selectedPrice;
    private int totalPrice;
    private boolean installation;
    private String selectedDate;
    private String selectedTime;

    // Customer Information
    private String fullName;
    private String phone;
    private String address;
    private String city;
    private String postcode;
    private String state;

    // Payment
    private String paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        // ==========================
        // Initialize Views
        // ==========================

        btnViewOrder = findViewById(R.id.btnViewOrder);
        btnBackHome = findViewById(R.id.btnBackHome);

        navProfile = findViewById(R.id.navProfile);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);

        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        // Home aktif
        selectHome();

        // ==========================
        // Receive Data
        // ==========================

        Intent previousIntent = getIntent();

        aircond = (Aircond) previousIntent.getSerializableExtra("aircond");

        selectedModel = previousIntent.getStringExtra("model");
        selectedHP = previousIntent.getStringExtra("hp");
        selectedPackage = previousIntent.getStringExtra("package");
        selectedPrice = previousIntent.getIntExtra("price", 0);
        totalPrice = previousIntent.getIntExtra("totalPrice", 0);

        installation = previousIntent.getBooleanExtra("installation", false);
        selectedDate = previousIntent.getStringExtra("date");
        selectedTime = previousIntent.getStringExtra("time");

        fullName = previousIntent.getStringExtra("fullName");
        phone = previousIntent.getStringExtra("phone");
        address = previousIntent.getStringExtra("address");
        city = previousIntent.getStringExtra("city");
        postcode = previousIntent.getStringExtra("postcode");
        state = previousIntent.getStringExtra("state");

        paymentMethod = previousIntent.getStringExtra("paymentMethod");

        // ==========================
        // View Order
        // ==========================

        btnViewOrder.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PaymentSuccessActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "orders");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });

        // ==========================
        // Back To Home
        // ==========================

        btnBackHome.setOnClickListener(v -> {

            Intent intent = new Intent(
                    PaymentSuccessActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(intent);
            finish();
        });

        // ==========================
        // Bottom Navigation
        // ==========================

        navHome.setOnClickListener(v -> {

            selectHome();

            Intent intent = new Intent(
                    PaymentSuccessActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navOrders.setOnClickListener(v -> {

            selectOrders();

            Intent intent = new Intent(
                    PaymentSuccessActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "orders");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {

            selectProfile();

            Intent intent = new Intent(
                    PaymentSuccessActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "profile");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

    }

    // ==========================
    // Bottom Navigation Icon
    // ==========================

    private void resetIcons() {

        iconProfile.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));

        iconHome.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));

        iconOrders.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));
    }

    private void selectProfile() {

        resetIcons();

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }

    private void selectHome() {

        resetIcons();

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }

    private void selectOrders() {

        resetIcons();

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }
}