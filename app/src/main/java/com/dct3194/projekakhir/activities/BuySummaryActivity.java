package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class BuySummaryActivity extends AppCompatActivity {

    private TextView tvOrderSummaryTitle;
    private TextView tvProductDetailsHeader;
    private ImageView imgProduct;

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

    private LinearLayout layoutInstallationStatus;
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
    private String model, hp, selectedType, selectedPackage;
    private int price, totalPrice;
    private boolean installation;
    private String date, time;
    private String customerName, customerPhone, customerAddress;

    private boolean isMaintenance = false;
    private String maintenanceService, maintenanceHP, maintenanceType, maintenancePrice;

    private static final int COLOR_ACTIVE = Color.parseColor("#3158B8");
    private static final int COLOR_INACTIVE = Color.GRAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_summary);

        // Initialize Views
        tvOrderSummaryTitle = findViewById(R.id.tvOrderSummaryTitle);
        tvProductDetailsHeader = findViewById(R.id.tvProductDetailsHeader);
        imgProduct = findViewById(R.id.imgProduct);

        tvProductName = findViewById(R.id.tvProductName);
        tvProductFeature = findViewById(R.id.tvProductFeature);
        tvProductModel = findViewById(R.id.tvProductModel);

        tvInstallationTitle = findViewById(R.id.tvInstallationTitle);
        tvInstallationDesc = findViewById(R.id.tvInstallationDesc);
        tvAirCondPrice = findViewById(R.id.tvAirCondPrice);
        tvInstallationPrice = findViewById(R.id.tvInstallationPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);

        layoutInstallationStatus = findViewById(R.id.layoutInstallationStatus);
        rowInstallationFee = findViewById(R.id.rowInstallationFee);

        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);

        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingTime = findViewById(R.id.tvBookingTime);

        btnPayNow = findViewById(R.id.btnPayNow);
        
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Bottom Navigation
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavOrders = findViewById(R.id.btnNavOrders);

        selectHome();

        // Nav Click Listeners
        btnNavProfile.setOnClickListener(v -> navigateToMain("profile"));
        btnNavHome.setOnClickListener(v -> navigateToMain("home"));
        btnNavOrders.setOnClickListener(v -> navigateToMain("orders"));

        // Receive Intent
        Intent intent = getIntent();
        isMaintenance = intent.getBooleanExtra("maintenance", false);

        if (isMaintenance) {
            setupMaintenanceView(intent);
        } else {
            setupBuyView(intent);
        }

        // Pay Now Click
        btnPayNow.setOnClickListener(v -> {
            Intent paymentIntent = new Intent(BuySummaryActivity.this, PaymentMethodActivity.class);
            
            paymentIntent.putExtra("maintenance", isMaintenance);
            paymentIntent.putExtra("date", date);
            paymentIntent.putExtra("time", time);
            paymentIntent.putExtra("customerName", tvCustomerName.getText().toString());
            paymentIntent.putExtra("customerPhone", tvCustomerPhone.getText().toString());
            paymentIntent.putExtra("customerAddress", tvCustomerAddress.getText().toString());

            if (isMaintenance) {
                paymentIntent.putExtra("service", maintenanceService);
                paymentIntent.putExtra("hp", maintenanceHP);
                paymentIntent.putExtra("maintenanceType", maintenanceType);
                paymentIntent.putExtra("maintenancePrice", maintenancePrice);
                
                int mPrice = parsePrice(maintenancePrice);
                paymentIntent.putExtra("price", mPrice);
                paymentIntent.putExtra("totalPrice", mPrice);
            } else {
                paymentIntent.putExtra("aircond", aircond);
                paymentIntent.putExtra("model", model);
                paymentIntent.putExtra("hp", hp);
                paymentIntent.putExtra("type", selectedType);
                paymentIntent.putExtra("package", selectedPackage);
                paymentIntent.putExtra("price", price);
                paymentIntent.putExtra("totalPrice", totalPrice);
                paymentIntent.putExtra("installation", installation);
            }
            startActivity(paymentIntent);
        });
    }

    private void setupMaintenanceView(Intent intent) {
        tvOrderSummaryTitle.setText("Booking Summary");
        tvProductDetailsHeader.setText("Maintenance Details");
        imgProduct.setImageResource(R.drawable.ic_maintenance);
        
        layoutInstallationStatus.setVisibility(View.GONE);
        rowInstallationFee.setVisibility(View.GONE);
        
        maintenanceService = intent.getStringExtra("service");
        maintenanceHP = intent.getStringExtra("hp");
        maintenanceType = intent.getStringExtra("type");
        maintenancePrice = intent.getStringExtra("price");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        
        tvProductName.setText(maintenanceService);
        tvProductFeature.setText("• " + maintenanceHP);
        tvProductModel.setText("• " + maintenanceType);
        
        tvBookingDate.setText(date);
        tvBookingTime.setText(time);
        
        tvAirCondPrice.setText(maintenancePrice);
        tvTotalPrice.setText(maintenancePrice);
        
        fetchUserInfo();
    }

    private void setupBuyView(Intent intent) {
        aircond = (Aircond) intent.getSerializableExtra("aircond");
        model = intent.getStringExtra("model");
        hp = intent.getStringExtra("hp");
        selectedType = intent.getStringExtra("type");
        selectedPackage = intent.getStringExtra("package");
        price = intent.getIntExtra("price", 0);
        totalPrice = intent.getIntExtra("totalPrice", 0);
        installation = intent.getBooleanExtra("installation", false);
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        customerName = intent.getStringExtra("customerName");
        customerPhone = intent.getStringExtra("customerPhone");
        customerAddress = intent.getStringExtra("customerAddress");

        String brand = (aircond != null) ? aircond.getBrand() : "";
        String name = (aircond != null) ? aircond.getName() : "";
        tvProductName.setText((brand + " " + name).trim());
        
        tvProductModel.setText("• Model : " + (model != null ? model : ""));
        
        StringBuilder feature = new StringBuilder();
        if (hp != null) feature.append("• ").append(hp);
        if (selectedType != null) feature.append("\n• ").append(selectedType);
        if (selectedPackage != null) feature.append("\n• ").append(selectedPackage);
        tvProductFeature.setText(feature.toString());

        tvAirCondPrice.setText(String.format("RM %,d", price));
        tvTotalPrice.setText(String.format("RM %,d", totalPrice));

        if (installation) {
            tvInstallationTitle.setText("Installation :");
            tvInstallationDesc.setText("Professional Installation Included");
            rowInstallationFee.setVisibility(View.VISIBLE);
            int installFee = (aircond != null) ? aircond.getInstallationPrice() : 0;
            tvInstallationPrice.setText(String.format("RM %,d", installFee));
        } else {
            tvInstallationTitle.setText("Option :");
            tvInstallationDesc.setText("Unit Only");
            rowInstallationFee.setVisibility(View.GONE);
        }

        tvCustomerName.setText(customerName);
        tvCustomerPhone.setText(customerPhone);
        tvCustomerAddress.setText(customerAddress);
        tvBookingDate.setText(date);
        tvBookingTime.setText(time);
    }

    private void fetchUserInfo() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(doc -> {
                        if (doc.exists()) {
                            tvCustomerName.setText(doc.getString("name"));
                            tvCustomerPhone.setText(doc.getString("phone"));
                            tvCustomerAddress.setText(doc.getString("address"));
                        }
                    });
        }
    }

    private int parsePrice(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) return 0;
        try {
            return Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
        } catch (Exception e) { return 0; }
    }

    private void navigateToMain(String page) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("page", page);
        startActivity(intent);
        finish();
    }

    private void resetNavigationIcons() {
        ColorStateList inactive = ColorStateList.valueOf(COLOR_INACTIVE);
        btnNavProfile.setImageTintList(inactive);
        btnNavHome.setImageTintList(inactive);
        btnNavOrders.setImageTintList(inactive);
    }

    private void selectHome() {
        resetNavigationIcons();
        btnNavHome.setImageTintList(ColorStateList.valueOf(COLOR_ACTIVE));
    }
}
