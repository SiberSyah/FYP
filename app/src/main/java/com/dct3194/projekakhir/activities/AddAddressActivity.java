package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class AddAddressActivity extends AppCompatActivity {

    // Customer Information
    private EditText etFullName, etPhone, etAddress, etCity, etPoscode, etState;
    private Button btnContinue;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // ===========================
        // Customer Information
        // ===========================
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etPoscode = findViewById(R.id.etPoscode);
        etState = findViewById(R.id.etState);

        btnContinue = findViewById(R.id.btnContinue);

        // ===========================
        // Bottom Navigation
        // ===========================
        navProfile = findViewById(R.id.navProfile);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);

        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        // Set ikon Home sebagai aktif (biru) secara default untuk booking flow
        selectHome();

        // ===========================
        // Receive Booking Data
        // ===========================
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

        btnContinue.setOnClickListener(v -> saveCustomerInfoAndProceed());

        // ===========================
        // Bottom Navigation
        // ===========================

        navHome.setOnClickListener(v -> {

            selectHome();

            Intent intent = new Intent(AddAddressActivity.this, MainActivity.class);
            intent.putExtra("page", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navOrders.setOnClickListener(v -> {

            selectOrders();

            Intent intent = new Intent(AddAddressActivity.this, MainActivity.class);
            intent.putExtra("page", "orders");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {

            selectProfile();

            Intent intent = new Intent(AddAddressActivity.this, MainActivity.class);
            intent.putExtra("page", "profile");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });
    }

    private void saveCustomerInfoAndProceed() {

        String fullName = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String postcode = etPoscode.getText().toString().trim();
        String state = etState.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            etFullName.setError("Please enter full name");
            etFullName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Please enter phone number");
            etPhone.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Please enter address");
            etAddress.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(city)) {
            etCity.setError("Please enter city");
            etCity.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(postcode)) {
            etPoscode.setError("Please enter postcode");
            etPoscode.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(state)) {
            etState.setError("Please enter state");
            etState.requestFocus();
            return;
        }

        Intent intent = new Intent(AddAddressActivity.this, BuySummaryActivity.class);

        // Customer Information
        intent.putExtra("fullName", fullName);
        intent.putExtra("phone", phone);
        intent.putExtra("address", address);
        intent.putExtra("city", city);
        intent.putExtra("postcode", postcode);
        intent.putExtra("state", state);

        // Booking Information
        intent.putExtra("aircond", aircond);
        intent.putExtra("model", selectedModel);
        intent.putExtra("hp", selectedHP);
        intent.putExtra("package", selectedPackage);
        intent.putExtra("price", selectedPrice);
        intent.putExtra("totalPrice", totalPrice);
        intent.putExtra("installation", installation);
        intent.putExtra("date", selectedDate);
        intent.putExtra("time", selectedTime);

        startActivity(intent);
    }

    // ===========================
    // Bottom Navigation Icon Color
    // ===========================

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