package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentSuccessActivity extends AppCompatActivity {

    // Buttons
    private Button btnViewOrder;
    private Button btnBackHome;

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

    // Customer Information
    private String fullName;
    private String phone;
    private String address;

    private String city;
    private String postcode;
    private String state;

    // Payment
    private String paymentMethod;
    private String paymentIntentId;

    // Maintenance
    private boolean maintenance = false;
    private String maintenanceType;
    private String maintenanceService;
    private String maintenancePrice;

    // Firebase
    private FirebaseFirestore db;

    private boolean orderSaving = false;

    // Maintenance
    private boolean maintenance = false;
    private String maintenanceType;
    private String maintenanceService;
    private String maintenancePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_payment_success
        );

        // ==========================
        // Initialize Views
        // ==========================

        btnViewOrder =
                findViewById(R.id.btnViewOrder);

        btnBackHome =
                findViewById(R.id.btnBackHome);

        navProfile =
                findViewById(R.id.navProfile);

        navHome =
                findViewById(R.id.navHome);

        navOrders =
                findViewById(R.id.navOrders);

        iconProfile =
                findViewById(R.id.iconProfile);

        iconHome =
                findViewById(R.id.iconHome);

        iconOrders =
                findViewById(R.id.iconOrders);

        // ==========================
        // Firebase
        // ==========================

        db =
                FirebaseFirestore.getInstance();

        // ==========================
        // Home Aktif
        // ==========================

        selectHome();

        // ==========================
        // Receive Data
        // ==========================

        Intent previousIntent =
                getIntent();

        aircond =
                (Aircond)
                        previousIntent
                                .getSerializableExtra(
                                        "aircond"
                                );

        selectedModel =
                previousIntent.getStringExtra(
                        "model"
                );

        selectedHP =
                previousIntent.getStringExtra(
                        "hp"
                );

        selectedPackage =
                previousIntent.getStringExtra(
                        "package"
                );

        selectedPrice =
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

        selectedDate =
                previousIntent.getStringExtra(
                        "date"
                );

        selectedTime =
                previousIntent.getStringExtra(
                        "time"
                );

        // ==========================
        // Customer Information
        // ==========================

        fullName =
                previousIntent.getStringExtra(
                        "customerName"
                );

        if (fullName == null ||
                fullName.trim().isEmpty()) {

            fullName =
                    previousIntent.getStringExtra(
                            "fullName"
                    );
        }

        phone =
                previousIntent.getStringExtra(
                        "customerPhone"
                );

        if (phone == null ||
                phone.trim().isEmpty()) {

            phone =
                    previousIntent.getStringExtra(
                            "phone"
                    );
        }

        address =
                previousIntent.getStringExtra(
                        "customerAddress"
                );

        if (address == null ||
                address.trim().isEmpty()) {

            address =
                    previousIntent.getStringExtra(
                            "address"
                    );
        }

        city =
                previousIntent.getStringExtra(
                        "city"
                );

        postcode =
                previousIntent.getStringExtra(
                        "postcode"
                );

        state =
                previousIntent.getStringExtra(
                        "state"
                );

        // ==========================
        // Payment
        // ==========================

        paymentMethod =
                previousIntent.getStringExtra(
                        "paymentMethod"
                );

        if (paymentMethod == null ||
                paymentMethod.trim().isEmpty()) {

            paymentMethod =
                    "Stripe";
        }

        paymentIntentId =
                previousIntent.getStringExtra(
                        "paymentIntentId"
                );

        // ==========================
        // Maintenance
        // ==========================

        maintenance =
                previousIntent.getBooleanExtra(
                        "maintenance",
                        false
                );

        maintenanceType =
                previousIntent.getStringExtra(
                        "type"
                );

        maintenanceService =
                previousIntent.getStringExtra(
                        "service"
                );

        maintenancePrice =
                previousIntent.getStringExtra(
                        "maintenancePrice"
                );

        /*
         * Safety:
         *
         * Kalau PaymentMethodActivity tak pass
         * boolean maintenance tetapi masih pass
         * service/maintenancePrice, kita anggap
         * order tersebut sebagai maintenance.
         */

        if (!maintenance) {

            if (maintenanceService != null &&
                    !maintenanceService
                            .trim()
                            .isEmpty()) {

                maintenance = true;
            }

            if (maintenancePrice != null &&
                    !maintenancePrice
                            .trim()
                            .isEmpty()) {

                maintenance = true;
            }
        }

        // ==========================
        // SAVE ORDER
        // ==========================

        saveOrderToFirestore();

        // Maintenance Data
        maintenance = previousIntent.getBooleanExtra(
                "maintenance",
                false
        );
        maintenanceType = previousIntent.getStringExtra("type");
        maintenanceService = previousIntent.getStringExtra("service");
        maintenancePrice = previousIntent.getStringExtra("maintenancePrice");

        // ==========================
        // View Order
        // ==========================

        btnViewOrder.setOnClickListener(v -> {

            openOrders();
        });

        // ==========================
        // Back To Home
        // ==========================

        btnBackHome.setOnClickListener(v -> {

            openHome();
        });

        // ==========================
        // Bottom Navigation
        // ==========================

        navHome.setOnClickListener(v -> {

            selectHome();

            openHome();
        });

        navOrders.setOnClickListener(v -> {

            selectOrders();

            openOrders();
        });

        navProfile.setOnClickListener(v -> {

            selectProfile();

            openProfile();
        });
    }

    // =========================================================
    // SAVE ORDER TO FIRESTORE
    // =========================================================

    private void saveOrderToFirestore() {

        if (orderSaving) {
            return;
        }

        // =====================================================
        // IMPORTANT:
        // Order mesti ada user yang sedang login
        // =====================================================

        FirebaseUser user =
                FirebaseAuth
                        .getInstance()
                        .getCurrentUser();

        if (user == null) {

            orderSaving = false;

            Toast.makeText(
                    this,
                    "Please login before placing an order.",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        orderSaving = true;

        // ==========================
        // Create Order Data
        // ==========================

        Map<String, Object> order =
                new HashMap<>();

        // ==========================
        // USER ID
        // ==========================

        order.put(
                "userId",
                user.getUid()
        );

        // ==========================
        // Order Type
        // ==========================

        if (maintenance) {

            order.put(
                    "orderType",
                    "maintenance"
            );

        } else {

            order.put(
                    "orderType",
                    "aircond"
            );
        }

        // ==========================
        // Product Information
        // ==========================

        if (aircond != null) {

            order.put(
                    "productName",
                    aircond.getName()
            );

            order.put(
                    "brand",
                    aircond.getBrand()
            );

            order.put(
                    "imageUrl",
                    aircond.getImageUrl()
            );
        }

        // ==========================
        // Aircond Options
        // ==========================

        if (selectedModel != null) {

            order.put(
                    "model",
                    selectedModel
            );
        }

        if (selectedHP != null) {

            order.put(
                    "hp",
                    selectedHP
            );
        }

        if (selectedPackage != null) {

            order.put(
                    "package",
                    selectedPackage
            );
        }

        order.put(
                "installation",
                installation
        );

        // ==========================
        // Maintenance Information
        // ==========================

        if (maintenance) {

            if (maintenanceType != null) {

                order.put(
                        "maintenanceType",
                        maintenanceType
                );
            }

            if (maintenanceService != null) {

                order.put(
                        "maintenanceService",
                        maintenanceService
                );
            }

            if (maintenancePrice != null) {

                order.put(
                        "maintenancePrice",
                        maintenancePrice
                );
            }
        }

        // ==========================
        // Price
        // ==========================

        order.put(
                "price",
                selectedPrice
        );

        order.put(
                "totalPrice",
                totalPrice
        );

        // ==========================
        // Customer
        // ==========================

        if (fullName != null) {

            order.put(
                    "customerName",
                    fullName
            );
        }

        if (phone != null) {

            order.put(
                    "customerPhone",
                    phone
            );
        }

        if (address != null) {

            order.put(
                    "customerAddress",
                    address
            );
        }

        if (city != null) {

            order.put(
                    "city",
                    city
            );
        }

        if (postcode != null) {

            order.put(
                    "postcode",
                    postcode
            );
        }

        if (state != null) {

            order.put(
                    "state",
                    state
            );
        }

        // ==========================
        // Booking
        // ==========================

        if (selectedDate != null) {

            order.put(
                    "date",
                    selectedDate
            );
        }

        if (selectedTime != null) {

            order.put(
                    "time",
                    selectedTime
            );
        }

        // ==========================
        // Initial Order Status
        // ==========================

        // Payment has been completed successfully,
        // so the order starts as Confirmed.
        order.put(
                "status",
                "Confirmed"
        );

        // ==========================
        // Payment
        // ==========================

        order.put(
                "paymentMethod",
                paymentMethod
        );

        order.put(
                "paymentStatus",
                "successful"
        );

        if (paymentIntentId != null &&
                !paymentIntentId
                        .trim()
                        .isEmpty()) {

            order.put(
                    "paymentIntentId",
                    paymentIntentId
            );
        }

        // ==========================
        // Timestamp
        // ==========================

        order.put(
                "createdAt",
                FieldValue.serverTimestamp()
        );

        // ==========================
        // Document Reference
        // ==========================

        DocumentReference orderReference;

        if (paymentIntentId != null &&
                !paymentIntentId
                        .trim()
                        .isEmpty()) {

            /*
             * Use Stripe PaymentIntent ID
             * as Firestore document ID.
             *
             * This helps prevent duplicate
             * order documents.
             */

            orderReference =
                    db.collection("orders")
                            .document(
                                    paymentIntentId
                            );

        } else {

            orderReference =
                    db.collection("orders")
                            .document();
        }

        // ==========================
        // Save
        // ==========================

        orderReference
                .set(order)
                .addOnSuccessListener(unused -> {

                    orderSaving = false;

                    Toast.makeText(
                            PaymentSuccessActivity.this,
                            "Order saved successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                })
                .addOnFailureListener(e -> {

                    orderSaving = false;

                    Toast.makeText(
                            PaymentSuccessActivity.this,
                            "Payment successful, but order could not be saved.",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // =========================================================
    // OPEN ORDERS
    // =========================================================

    private void openOrders() {

        Intent intent =
                new Intent(
                        PaymentSuccessActivity.this,
                        MainActivity.class
                );

        intent.putExtra(
                "page",
                "orders"
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // OPEN HOME
    // =========================================================

    private void openHome() {

        Intent intent =
                new Intent(
                        PaymentSuccessActivity.this,
                        MainActivity.class
                );

        intent.putExtra(
                "page",
                "home"
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // OPEN PROFILE
    // =========================================================

    private void openProfile() {

        Intent intent =
                new Intent(
                        PaymentSuccessActivity.this,
                        MainActivity.class
                );

        intent.putExtra(
                "page",
                "profile"
        );

        intent.putExtra(
                "page",
                "profile"
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_CLEAR_TOP
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // BOTTOM NAVIGATION ICON
    // =========================================================

    private void resetIcons() {

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );
    }

    private void selectProfile() {

        resetIcons();

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor(
                                "#3158B8"
                        )
                )
        );
    }

    private void selectHome() {

        resetIcons();

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor(
                                "#3158B8"
                        )
                )
        );
    }

    private void selectOrders() {

        resetIcons();

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor(
                                "#3158B8"
                        )
                )
        );
    }
}
