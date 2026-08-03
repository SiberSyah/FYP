package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class AddPaymentActivity extends AppCompatActivity {

    // Card Information
    private EditText etCardHolder;
    private EditText etCardNumber;
    private EditText etExpiry;
    private EditText etCVV;

    private Button btnConfirmPayment;

    // Bottom Navigation
    private LinearLayout navProfile;
    private LinearLayout navHome;
    private LinearLayout navOrders;

    private ImageView iconProfile;
    private ImageView iconHome;
    private ImageView iconOrders;

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

    // Customer
    private String customerName;
    private String customerPhone;
    private String customerAddress;

    // Payment Method
    private String paymentMethod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        // ==========================
        // Initialize Views
        // ==========================

        etCardHolder = findViewById(R.id.etCardHolder);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCVV = findViewById(R.id.etCVV);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        navProfile = findViewById(R.id.navProfile);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);

        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        selectHome();

        // ==========================
        // Receive Intent
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

        customerName = previousIntent.getStringExtra("customerName");
        customerPhone = previousIntent.getStringExtra("customerPhone");
        customerAddress = previousIntent.getStringExtra("customerAddress");

        paymentMethod = previousIntent.getStringExtra("paymentMethod");

        // ==========================
        // Confirm Payment
        // ==========================

        btnConfirmPayment.setOnClickListener(v -> {

            String cardHolder = etCardHolder.getText().toString().trim();
            String cardNumber = etCardNumber.getText().toString().trim();
            String expiry = etExpiry.getText().toString().trim();
            String cvv = etCVV.getText().toString().trim();

            if (cardHolder.isEmpty()) {
                etCardHolder.setError("Enter card holder name");
                etCardHolder.requestFocus();
                return;
            }

            if (cardNumber.isEmpty()) {
                etCardNumber.setError("Enter card number");
                etCardNumber.requestFocus();
                return;
            }

            if (cardNumber.length() < 16) {
                etCardNumber.setError("Invalid card number");
                etCardNumber.requestFocus();
                return;
            }

            if (expiry.isEmpty()) {
                etExpiry.setError("Enter expiry date");
                etExpiry.requestFocus();
                return;
            }

            if (cvv.isEmpty()) {
                etCVV.setError("Enter CVV");
                etCVV.requestFocus();
                return;
            }

            if (cvv.length() < 3) {
                etCVV.setError("Invalid CVV");
                etCVV.requestFocus();
                return;
            }

            Intent intent = new Intent(
                    AddPaymentActivity.this,
                    PaymentSuccessActivity.class);

            // Customer
            intent.putExtra("customerName", customerName);
            intent.putExtra("customerPhone", customerPhone);
            intent.putExtra("customerAddress", customerAddress);

            // Booking
            intent.putExtra("aircond", aircond);
            intent.putExtra("model", selectedModel);
            intent.putExtra("hp", selectedHP);
            intent.putExtra("package", selectedPackage);
            intent.putExtra("price", selectedPrice);
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("installation", installation);
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);

            // Payment
            intent.putExtra("paymentMethod", paymentMethod);

            startActivity(intent);
            finish();
        });

        // ==========================
        // Bottom Navigation
        // ==========================

        navHome.setOnClickListener(v -> {

            selectHome();

            Intent intent = new Intent(
                    AddPaymentActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navOrders.setOnClickListener(v -> {

            selectOrders();

            Intent intent = new Intent(
                    AddPaymentActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "orders");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {

            selectProfile();

            Intent intent = new Intent(
                    AddPaymentActivity.this,
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