package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class MaintenanceSummaryActivity extends AppCompatActivity {

    private TextView tvOrderSummaryTitle;
    private TextView tvProductName, tvProductFeature, tvProductModel;
    private TextView tvBookingDate, tvBookingTime;
    private TextView tvCustomerName, tvCustomerPhone, tvCustomerAddress;
    private TextView tvAirCondPrice, tvTotalPrice;
    private LinearLayout layoutInstallationStatus, rowInstallationFee;
    private Button btnConfirm;
    private ImageView btnBack;

    // Bottom Navigation
    private ImageView btnNavProfile, btnNavHome, btnNavOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_summary);

        // Initialize Views
        tvOrderSummaryTitle = findViewById(R.id.tvOrderSummaryTitle);
        tvOrderSummaryTitle.setText("Booking Summary");

        TextView tvProductDetailsHeader = findViewById(R.id.tvProductDetailsHeader);
        tvProductDetailsHeader.setText("Maintenance Details");

        tvProductName = findViewById(R.id.tvProductName);
        tvProductFeature = findViewById(R.id.tvProductFeature);
        tvProductModel = findViewById(R.id.tvProductModel);
        
        ImageView imgProduct = findViewById(R.id.imgProduct);
        imgProduct.setImageResource(R.drawable.ic_maintenance);

        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvBookingTime = findViewById(R.id.tvBookingTime);
        
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerPhone = findViewById(R.id.tvCustomerPhone);
        tvCustomerAddress = findViewById(R.id.tvCustomerAddress);
        
        tvAirCondPrice = findViewById(R.id.tvAirCondPrice);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        
        layoutInstallationStatus = findViewById(R.id.layoutInstallationStatus);
        rowInstallationFee = findViewById(R.id.rowInstallationFee);
        
        btnConfirm = findViewById(R.id.btnPayNow);
        btnBack = findViewById(R.id.btnBack);

        // Bottom Nav
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavOrders = findViewById(R.id.btnNavOrders);

        // Hide installation related views for maintenance
        layoutInstallationStatus.setVisibility(View.GONE);
        rowInstallationFee.setVisibility(View.GONE);

        // Get Data
        Intent intent = getIntent();
        if (intent != null) {
            String service = intent.getStringExtra("service");
            String hp = intent.getStringExtra("hp");
            String type = intent.getStringExtra("type");
            String date = intent.getStringExtra("date");
            String time = intent.getStringExtra("time");
            String price = intent.getStringExtra("price");

            tvProductName.setText(service);
            tvProductFeature.setText("• " + hp);
            tvProductModel.setText("• " + type);
            
            tvBookingDate.setText(date);
            tvBookingTime.setText(time);
            
            tvAirCondPrice.setText(price);
            tvTotalPrice.setText(price);
        }

        // Fetch User Info (Optional, if you want to show it like Buy Summary)
        fetchUserInfo();

        // Back
        btnBack.setOnClickListener(v -> finish());

        // Confirm
        btnConfirm.setOnClickListener(v -> {
            Intent nextIntent = new Intent(MaintenanceSummaryActivity.this, PaymentMethodActivity.class);
            nextIntent.putExtra("maintenance", true);
            nextIntent.putExtra("service", intent.getStringExtra("service"));
            nextIntent.putExtra("hp", intent.getStringExtra("hp"));
            nextIntent.putExtra("maintenanceType", intent.getStringExtra("type"));
            
            String priceString = intent.getStringExtra("price");
            int maintenancePrice = parsePrice(priceString);
            
            nextIntent.putExtra("maintenancePrice", priceString);
            nextIntent.putExtra("totalPrice", maintenancePrice);
            nextIntent.putExtra("price", maintenancePrice);
            
            nextIntent.putExtra("date", intent.getStringExtra("date"));
            nextIntent.putExtra("time", intent.getStringExtra("time"));

            // Pass customer info too
            nextIntent.putExtra("customerName", tvCustomerName.getText().toString());
            nextIntent.putExtra("customerPhone", tvCustomerPhone.getText().toString());
            nextIntent.putExtra("customerAddress", tvCustomerAddress.getText().toString());

            startActivity(nextIntent);
        });

        // Bottom Nav Listeners
        btnNavHome.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("page", "home"));
            finish();
        });
        btnNavProfile.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("page", "profile"));
            finish();
        });
        btnNavOrders.setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class).putExtra("page", "orders"));
            finish();
        });
    }

    private void fetchUserInfo() {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            tvCustomerName.setText(documentSnapshot.getString("name"));
                            tvCustomerPhone.setText(documentSnapshot.getString("phone"));
                            tvCustomerAddress.setText(documentSnapshot.getString("address"));
                        }
                    });
        }
    }

    private int parsePrice(String priceText) {
        if (priceText == null || priceText.trim().isEmpty()) return 0;
        try {
            String cleaned = priceText.replace("RM", "").replace("rm", "").replace(",", "").replace(".00", "").trim();
            return Integer.parseInt(cleaned);
        } catch (Exception e) {
            return 0;
        }
    }
}
