package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

public class OrderStatusActivity extends AppCompatActivity {

    private int currentStatus = 0;

    private FirebaseFirestore db;
    private ListenerRegistration orderListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        // =========================
        // Firebase
        // =========================

        db = FirebaseFirestore.getInstance();

        // =========================
        // Back
        // =========================

        findViewById(R.id.btnBack)
                .setOnClickListener(v -> finish());

        // =========================
        // Load Order Status
        // =========================

        String orderId =
                getIntent().getStringExtra("orderId");

        if (orderId != null &&
                !orderId.trim().isEmpty()) {

            listenToOrder(orderId);

        } else {

            // Fallback to status passed
            // from OrderAdapter

            String status =
                    getIntent().getStringExtra("status");

            currentStatus =
                    convertStatusToIndex(status);

            updateTimeline(currentStatus);
        }

        // =========================
        // Bottom Navigation
        // =========================

        findViewById(R.id.navHome)
                .setOnClickListener(v -> {

                    Intent intent =
                            new Intent(
                                    this,
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
                                    this,
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
    }

    // =========================================================
    // LISTEN TO FIREBASE ORDER
    // =========================================================

    private void listenToOrder(
            String orderId) {

        orderListener =
                db.collection("orders")
                        .document(orderId)
                        .addSnapshotListener(
                                (snapshot, error) -> {

                                    if (error != null) {

                                        // Keep current UI
                                        // if Firebase has error

                                        return;
                                    }

                                    if (snapshot == null ||
                                            !snapshot.exists()) {

                                        return;
                                    }

                                    updateFromFirestore(
                                            snapshot
                                    );
                                }
                        );
    }

    // =========================================================
    // UPDATE FROM FIRESTORE
    // =========================================================

    private void updateFromFirestore(
            DocumentSnapshot snapshot) {

        String status =
                snapshot.getString("status");

        currentStatus =
                convertStatusToIndex(status);

        updateTimeline(
                currentStatus
        );
    }

    // =========================================================
    // CONVERT FIREBASE STATUS
    // =========================================================

    private int convertStatusToIndex(
            String status) {

        if (status == null ||
                status.trim().isEmpty()) {

            return 0;
        }

        if (status.equalsIgnoreCase(
                "Pending")) {

            return 0;

        } else if (status.equalsIgnoreCase(
                "Confirmed")) {

            return 0;

        } else if (status.equalsIgnoreCase(
                "Order Confirmed")) {

            return 0;

        } else if (status.equalsIgnoreCase(
                "Technician Assigned")) {

            return 1;

        } else if (status.equalsIgnoreCase(
                "On The Way")) {

            return 2;

        } else if (status.equalsIgnoreCase(
                "Completed")) {

            return 3;
        }

        return 0;
    }

    // =========================================================
    // UPDATE TIMELINE
    // =========================================================

    private void updateTimeline(
            int statusIndex) {

        setupStep(
                findViewById(
                        R.id.stepConfirmed
                ),
                0,
                statusIndex,
                "Order Confirmed",
                "Your Order has been confirmed",
                R.drawable.ic_check_circle,
                true
        );

        setupStep(
                findViewById(
                        R.id.stepAssigned
                ),
                1,
                statusIndex,
                "Technician Assigned",
                "John Wilson Assigned",
                R.drawable.ic_check_circle,
                true
        );

        setupStep(
                findViewById(
                        R.id.stepOnTheWay
                ),
                2,
                statusIndex,
                "On The Way",
                "Technician is heading to your location",
                R.drawable.ic_truck,
                true
        );

        setupStep(
                findViewById(
                        R.id.stepCompleted
                ),
                3,
                statusIndex,
                "Completed",
                "Service is Completed",
                R.drawable.ic_check_circle,
                false
        );
    }

    // =========================================================
    // SETUP TIMELINE STEP
    // =========================================================

    private void setupStep(
            View view,
            int stepIndex,
            int currentStatus,
            String title,
            String desc,
            int iconRes,
            boolean showLine) {

        TextView txtTitle =
                view.findViewById(
                        R.id.txtStatusTitle
                );

        TextView txtDesc =
                view.findViewById(
                        R.id.txtStatusDesc
                );

        ImageView imgIcon =
                view.findViewById(
                        R.id.imgStatusIcon
                );

        View viewLine =
                view.findViewById(
                        R.id.viewLine
                );

        View iconContainer =
                view.findViewById(
                        R.id.layoutIconContainer
                );

        txtTitle.setText(title);

        txtDesc.setText(desc);

        imgIcon.setImageResource(
                iconRes
        );

        String circleColor;
        String iconColor;
        String lineColor;

        // =========================
        // Completed Steps
        // =========================

        if (stepIndex < currentStatus) {

            circleColor = "#D7F1D8";
            iconColor = "#50B054";
            lineColor = "#3158B8";

            // =========================
            // Current Step
            // =========================

        } else if (stepIndex == currentStatus) {

            circleColor = "#1D449A";
            iconColor = "#FFFFFF";
            lineColor = "#9EABB1";

            // =========================
            // Future Steps
            // =========================

        } else {

            circleColor = "#607D8B";
            iconColor = "#FFFFFF";
            lineColor = "#9EABB1";
        }

        // =========================
        // Completed Step Special Case
        // =========================

        if (stepIndex == 3 &&
                currentStatus != 3) {

            circleColor = "#607D8B";
            iconColor = "#FFFFFF";
        }

        // =========================
        // Circle
        // =========================

        GradientDrawable shape =
                new GradientDrawable();

        shape.setShape(
                GradientDrawable.OVAL
        );

        shape.setColor(
                Color.parseColor(
                        circleColor
                )
        );

        iconContainer.setBackground(
                shape
        );

        // =========================
        // Icon
        // =========================

        imgIcon.setColorFilter(
                Color.parseColor(
                        iconColor
                )
        );

        // =========================
        // Connecting Line
        // =========================

        if (viewLine != null) {

            viewLine.setBackgroundColor(
                    Color.parseColor(
                            lineColor
                    )
            );

            viewLine.setVisibility(
                    showLine
                            ? View.VISIBLE
                            : View.INVISIBLE
            );
        }
    }

    // =========================================================
    // REMOVE FIREBASE LISTENER
    // =========================================================

    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (orderListener != null) {

            orderListener.remove();

            orderListener = null;
        }
    }
}