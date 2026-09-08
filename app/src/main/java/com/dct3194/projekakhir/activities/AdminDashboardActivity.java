package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.AdminOrderAdapter;
import com.dct3194.projekakhir.models.Order;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AdminDashboardActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ImageView btnMenu;
    private NavigationView navigationView;

    private RecyclerView rvAdminOrders;
    private List<Order> orderList;
    private AdminOrderAdapter adapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // =========================
        // CONNECT XML
        // =========================
        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu = findViewById(R.id.btnMenu);
        navigationView = findViewById(R.id.navigationView);
        rvAdminOrders = findViewById(R.id.rvAdminOrders);

        // =========================
        // RECYCLERVIEW SETUP
        // =========================
        rvAdminOrders.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapter = new AdminOrderAdapter(orderList);
        rvAdminOrders.setAdapter(adapter);

        // =========================
        // FIREBASE SETUP & LOAD
        // =========================
        db = FirebaseFirestore.getInstance();
        loadOrders();

        // =========================
        // OPEN SIDE MENU
        // =========================
        btnMenu.setOnClickListener(v -> {
            drawerLayout.openDrawer(GravityCompat.START);
        });

        // =========================
        // MENU CLICK & NAVIGATION
        // =========================
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_dashboard) {
                item.setChecked(true);
                // Kekal di dashboard
            } else if (id == R.id.nav_orders) {
                item.setChecked(true);
                /*
                Intent intent = new Intent(
                        AdminDashboardActivity.this,
                        OrderActivity.class
                );
                startActivity(intent);
                */
                Toast.makeText(this, "Orders Page", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_products) {
                item.setChecked(true);
                /*
                Intent intent = new Intent(
                        AdminDashboardActivity.this,
                        ProductActivity.class
                );
                startActivity(intent);
                */
                Toast.makeText(this, "Products Page", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_services) {
                item.setChecked(true);
                /*
                Intent intent = new Intent(
                        AdminDashboardActivity.this,
                        ServiceActivity.class
                );
                startActivity(intent);
                */
                Toast.makeText(this, "Services Page", Toast.LENGTH_SHORT).show();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // =========================
        // DEFAULT HIGHLIGHT DASHBOARD
        // =========================
        navigationView.setCheckedItem(R.id.nav_dashboard);
    }

    // =========================================================
    // LOAD ORDERS FROM FIRESTORE
    // =========================================================
    private void loadOrders() {
        db.collection("orders")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orderList.clear();

                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        Order order = document.toObject(Order.class);
                        if (order != null) {
                            orderList.add(order);
                        }
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(
                            this,
                            "Failed load orders: " + e.getMessage(),
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}