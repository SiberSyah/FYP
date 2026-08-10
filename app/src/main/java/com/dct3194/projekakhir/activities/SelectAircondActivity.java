package com.dct3194.projekakhir.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.AircondAdapter;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectAircondActivity extends AppCompatActivity {

    private RecyclerView recyclerAircond;
    private AircondAdapter adapter;
    private List<Aircond> aircondList;

    private FirebaseFirestore db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_aircond);

        // =========================
        // Initialize RecyclerView
        // =========================

        recyclerAircond = findViewById(R.id.recyclerAircond);

        recyclerAircond.setLayoutManager(
                new LinearLayoutManager(this)
        );

        // =========================
        // Initialize List
        // =========================

        aircondList = new ArrayList<>();

        // =========================
        // Initialize Adapter
        // =========================

        adapter = new AircondAdapter(
                aircondList,
                aircond -> {
                    // Activity ini tidak lagi digunakan
                    // untuk navigation.
                    // Navigation sebenar menggunakan
                    // SelectAircondFragment.
                }
        );

        recyclerAircond.setAdapter(adapter);

        // =========================
        // Initialize Firestore
        // =========================

        db = FirebaseFirestore.getInstance();

        // =========================
        // Load Aircond
        // =========================

        loadAircondsFromFirebase();
    }

    // =========================
    // Load Aircond From Firestore
    // =========================

    private void loadAircondsFromFirebase() {

        db.collection("airconds")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    aircondList.clear();

                    for (QueryDocumentSnapshot document
                            : queryDocumentSnapshots) {

                        // =========================
                        // Available
                        // =========================

                        Boolean available =
                                document.getBoolean("available");

                        if (available != null && !available) {
                            continue;
                        }

                        // =========================
                        // Basic Information
                        // =========================

                        String name =
                                document.getString("name");

                        String brand =
                                document.getString("brand");

                        String imageUrl =
                                document.getString("imageUrl");

                        // =========================
                        // Prices
                        // =========================

                        Long price1HPValue =
                                document.getLong("price1HP");

                        Long price15HPValue =
                                document.getLong("price15HP");

                        Long price2HPValue =
                                document.getLong("price2HP");

                        Long installationFeeValue =
                                document.getLong("installationFee");

                        // =========================
                        // Convert Numbers
                        // =========================

                        int price1HP =
                                price1HPValue != null
                                        ? price1HPValue.intValue()
                                        : 0;

                        int price15HP =
                                price15HPValue != null
                                        ? price15HPValue.intValue()
                                        : 0;

                        int price2HP =
                                price2HPValue != null
                                        ? price2HPValue.intValue()
                                        : 0;

                        int installationFee =
                                installationFeeValue != null
                                        ? installationFeeValue.intValue()
                                        : 0;

                        // =========================
                        // Safety Check
                        // =========================

                        if (name == null || name.trim().isEmpty()) {
                            name = "Aircond";
                        }

                        if (brand == null) {
                            brand = "";
                        }

                        if (imageUrl == null) {
                            imageUrl = "";
                        }

                        // =========================
                        // Create Aircond
                        // =========================

                        Aircond aircond = new Aircond(
                                name,
                                brand,
                                imageUrl,
                                price1HP,
                                price15HP,
                                price2HP,
                                installationFee
                        );

                        // =========================
                        // Add To List
                        // =========================

                        aircondList.add(aircond);
                    }

                    // =========================
                    // Refresh RecyclerView
                    // =========================

                    adapter.notifyDataSetChanged();

                    // =========================
                    // No Data
                    // =========================

                    if (aircondList.isEmpty()) {

                        Toast.makeText(
                                SelectAircondActivity.this,
                                "No aircond available.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            SelectAircondActivity.this,
                            "Failed to load aircond data.",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}