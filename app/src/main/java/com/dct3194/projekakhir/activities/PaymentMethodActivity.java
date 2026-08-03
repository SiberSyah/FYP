package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class PaymentMethodActivity extends AppCompatActivity {

    // Payment Method
    private CardView cardOnlineBanking;
    private CardView cardCard;

    private RadioButton radioOnlineBanking;
    private RadioButton radioCard;

    private Button btnConfirmPayment;

    // Bottom Navigation
    private LinearLayout navProfile;
    private LinearLayout navHome;
    private LinearLayout navOrders;

    private ImageView iconProfile;
    private ImageView iconHome;
    private ImageView iconOrders;

    // Booking
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

    // Payment
    private String paymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        // ==========================
        // Initialize Views
        // ==========================

        cardOnlineBanking = findViewById(R.id.cardOnlineBanking);
        cardCard = findViewById(R.id.cardCard);

        radioOnlineBanking = findViewById(R.id.radioOnlineBanking);
        radioCard = findViewById(R.id.radioCard);

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

        // ==========================
        // Payment Selection
        // ==========================

        cardOnlineBanking.setOnClickListener(v -> {

            radioOnlineBanking.setChecked(true);
            radioCard.setChecked(false);

            paymentMethod = "Online Banking";
        });

        cardCard.setOnClickListener(v -> {

            radioCard.setChecked(true);
            radioOnlineBanking.setChecked(false);

            paymentMethod = "Credit / Debit Card";
        });

        radioOnlineBanking.setOnClickListener(v -> {

            radioOnlineBanking.setChecked(true);
            radioCard.setChecked(false);

            paymentMethod = "Online Banking";
        });

        radioCard.setOnClickListener(v -> {

            radioCard.setChecked(true);
            radioOnlineBanking.setChecked(false);

            paymentMethod = "Credit / Debit Card";
        });

        // ==========================
        // Confirm Payment
        // ==========================

        btnConfirmPayment.setOnClickListener(v -> {

            if (paymentMethod.isEmpty()) {

                Toast.makeText(
                        PaymentMethodActivity.this,
                        "Please select a payment method",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // ==========================
            // Credit / Debit Card
            // ==========================

            if (paymentMethod.equals("Credit / Debit Card")) {

                Intent intent = new Intent(
                        PaymentMethodActivity.this,
                        StripePaymentActivity.class);
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

                return;
            }

            // ==========================
            // Online Banking
            // ==========================

            Intent intent = new Intent(
                    PaymentMethodActivity.this,
                    ToyyibPayActivity.class);

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
        });

        // ==========================
        // Bottom Navigation
        // ==========================

        navHome.setOnClickListener(v -> {

            selectHome();

            Intent intent = new Intent(
                    PaymentMethodActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navOrders.setOnClickListener(v -> {

            selectOrders();

            Intent intent = new Intent(
                    PaymentMethodActivity.this,
                    MainActivity.class);

            intent.putExtra("page", "orders");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(intent);
            finish();
        });

        navProfile.setOnClickListener(v -> {

            selectProfile();

            Intent intent = new Intent(
                    PaymentMethodActivity.this,
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