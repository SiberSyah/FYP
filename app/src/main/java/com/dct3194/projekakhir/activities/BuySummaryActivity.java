package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class BuySummaryActivity extends AppCompatActivity {

    // Product
    private TextView tvProductName;
    private TextView tvProductFeature;
    private TextView tvProductModel;

    // Price
    private TextView tvInstallationTitle;
    private TextView tvInstallationDesc;
    private TextView tvAirCondPrice;
    private TextView tvInstallationPrice;
    private TextView tvTotalPrice;

    private LinearLayout rowInstallationFee;

    // Customer Information
    private TextView tvCustomerName;
    private TextView tvCustomerPhone;
    private TextView tvCustomerAddress;

    // Booking Information
    private TextView tvBookingDate;
    private TextView tvBookingTime;

    // Button
    private Button btnPayNow;

    // Data
    private Aircond aircond;
    private String model;
    private String hp;
    private String selectedPackage;

    private int price;
    private int totalPrice;

    private boolean installation;

    private String date;
    private String time;

    private String customerName;
    private String customerPhone;
    private String customerAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_summary);

        // ==========================
        // Initialize Views
        // ==========================

        tvProductName = findViewById(R.id.tvProductName);
        tvProductFeature = findViewById(R.id.tvProductFeature);
        tvProductModel = findViewById(R.id.tvProductModel);

        tvInstallationTitle = findViewById(R.id.tvInstallationTitle);
        tvInstallationDesc = findViewById(R.id.tvInstallationDesc);

        tvAirCondPrice = findViewById(R.id.tvAirCondPrice);
        tvInstallationPrice = findViewById(R.id.tvInstallationPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        rowInstallationFee = findViewById(R.id.rowInstallationFee);

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);

        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingTime = findViewById(R.id.tvBookingTime);

        btnPayNow = findViewById(R.id.btnPayNow);

        // ==========================
        // Receive Intent
        // ==========================

        Intent intent = getIntent();

        aircond = (Aircond) intent.getSerializableExtra("aircond");

        model = intent.getStringExtra("model");
        hp = intent.getStringExtra("hp");
        selectedPackage = intent.getStringExtra("package");

        price = intent.getIntExtra("price", 0);
        totalPrice = intent.getIntExtra("totalPrice", 0);

        installation = intent.getBooleanExtra("installation", false);

        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");

        customerName = intent.getStringExtra("customerName");
        customerPhone = intent.getStringExtra("customerPhone");
        customerAddress = intent.getStringExtra("customerAddress");

        // ==========================
        // Product Information
        // ==========================

        String brand = "";

        if (aircond != null) {
            brand = aircond.getBrand();
        }

        String fullProductName = (brand + " " + (model != null ? model : "")).trim();

        tvProductName.setText(fullProductName);

        if (model != null) {
            tvProductModel.setText("• Model : " + model);
        }

        StringBuilder feature = new StringBuilder();

        if (hp != null) {
            feature.append("• ").append(hp);
        }

        if (selectedPackage != null) {

            if (feature.length() > 0) {
                feature.append("\n");
            }

            feature.append("• ").append(selectedPackage);
        }

        tvProductFeature.setText(feature.toString());

        // ==========================
        // Price
        // ==========================

        tvAirCondPrice.setText(String.format("RM %,d", price));

        if (installation) {

            tvInstallationTitle.setText("Installation :");
            tvInstallationDesc.setText("Professional Installation Included");

            rowInstallationFee.setVisibility(View.VISIBLE);

            tvInstallationPrice.setText(
                    String.format("RM %,d", 250));

        } else {

            tvInstallationTitle.setText("Option :");
            tvInstallationDesc.setText("Unit Only");

            rowInstallationFee.setVisibility(View.GONE);
        }

        tvTotalPrice.setText(
                String.format("RM %,d", totalPrice));

        // ==========================
        // Customer Information
        // ==========================

        if (tvCustomerName != null && customerName != null) {
            tvCustomerName.setText(customerName);
        }

        if (tvCustomerPhone != null && customerPhone != null) {
            tvCustomerPhone.setText(customerPhone);
        }

        if (tvCustomerAddress != null && customerAddress != null) {
            tvCustomerAddress.setText(customerAddress);
        }

        // ==========================
        // Booking Information
        // ==========================

        if (tvBookingDate != null && date != null) {
            tvBookingDate.setText(date);
        }

        if (tvBookingTime != null && time != null) {
            tvBookingTime.setText(time);
        }

        // ==========================
        // Pay Now
        // ==========================

        btnPayNow.setOnClickListener(v -> {

            Intent paymentIntent = new Intent(
                    BuySummaryActivity.this,
                    PaymentMethodActivity.class);

            // Product
            paymentIntent.putExtra("aircond", aircond);
            paymentIntent.putExtra("model", model);
            paymentIntent.putExtra("hp", hp);
            paymentIntent.putExtra("package", selectedPackage);

            // Price
            paymentIntent.putExtra("price", price);
            paymentIntent.putExtra("totalPrice", totalPrice);

            // Installation
            paymentIntent.putExtra("installation", installation);

            // Booking
            paymentIntent.putExtra("date", date);
            paymentIntent.putExtra("time", time);

            // Customer
            paymentIntent.putExtra("customerName", customerName);
            paymentIntent.putExtra("customerPhone", customerPhone);
            paymentIntent.putExtra("customerAddress", customerAddress);

            startActivity(paymentIntent);
        });

    }
}