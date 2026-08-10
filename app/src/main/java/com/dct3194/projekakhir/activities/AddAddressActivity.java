package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class AddAddressActivity extends AppCompatActivity {

    // Customer Information
    private EditText etFullName;
    private EditText etPhone;
    private EditText etAddress;
    private EditText etCity;
    private EditText etPoscode;
    private EditText etState;

    private Button btnContinue;

    // Booking Data
    private Aircond aircond;

    private String model;
    private String hp;
    private String selectedPackage;

    private int price;
    private int totalPrice;

    private boolean installation;

    private String date;
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        // ==========================
        // Initialize Views
        // ==========================

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etPoscode = findViewById(R.id.etPoscode);
        etState = findViewById(R.id.etState);

        btnContinue = findViewById(R.id.btnContinue);

        // ==========================
        // Receive Booking Data
        // ==========================

        Intent previousIntent = getIntent();

        aircond = (Aircond) previousIntent.getSerializableExtra("aircond");

        model = previousIntent.getStringExtra("model");
        hp = previousIntent.getStringExtra("hp");
        selectedPackage = previousIntent.getStringExtra("package");

        price = previousIntent.getIntExtra("price", 0);
        totalPrice = previousIntent.getIntExtra("totalPrice", 0);

        installation = previousIntent.getBooleanExtra(
                "installation",
                false
        );

        date = previousIntent.getStringExtra("date");
        time = previousIntent.getStringExtra("time");

        // ==========================
        // Continue
        // ==========================

        btnContinue.setOnClickListener(v -> {

            if (!validateForm()) {
                return;
            }

            // ==========================
            // Customer Information
            // ==========================

            String customerName =
                    etFullName.getText().toString().trim();

            String customerPhone =
                    etPhone.getText().toString().trim();

            String customerAddress =
                    etAddress.getText().toString().trim()
                            + ", "
                            + etCity.getText().toString().trim()
                            + ", "
                            + etPoscode.getText().toString().trim()
                            + ", "
                            + etState.getText().toString().trim();

            // ==========================
            // Go To Buy Summary
            // ==========================

            Intent intent = new Intent(
                    AddAddressActivity.this,
                    BuySummaryActivity.class
            );

            // Product
            intent.putExtra("aircond", aircond);
            intent.putExtra("model", model);
            intent.putExtra("hp", hp);
            intent.putExtra("package", selectedPackage);

            // Price
            intent.putExtra("price", price);
            intent.putExtra("totalPrice", totalPrice);

            // Installation
            intent.putExtra("installation", installation);

            // Booking
            intent.putExtra("date", date);
            intent.putExtra("time", time);

            // Customer
            intent.putExtra("customerName", customerName);
            intent.putExtra("customerPhone", customerPhone);
            intent.putExtra("customerAddress", customerAddress);

            startActivity(intent);
        });

        // ==========================
        // Bottom Navigation
        // ==========================

        findViewById(R.id.navHome).setOnClickListener(v -> {

            Intent intent = new Intent(
                    AddAddressActivity.this,
                    MainActivity.class
            );

            intent.putExtra("page", "home");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {

            Intent intent = new Intent(
                    AddAddressActivity.this,
                    MainActivity.class
            );

            intent.putExtra("page", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });

        findViewById(R.id.navOrders).setOnClickListener(v -> {

            Intent intent = new Intent(
                    AddAddressActivity.this,
                    MainActivity.class
            );

            intent.putExtra("page", "orders");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
        });
    }

    // ==========================
    // Validate Form
    // ==========================

    private boolean validateForm() {

        if (etFullName.getText().toString().trim().isEmpty()
                || etPhone.getText().toString().trim().isEmpty()
                || etAddress.getText().toString().trim().isEmpty()
                || etCity.getText().toString().trim().isEmpty()
                || etPoscode.getText().toString().trim().isEmpty()
                || etState.getText().toString().trim().isEmpty()) {

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