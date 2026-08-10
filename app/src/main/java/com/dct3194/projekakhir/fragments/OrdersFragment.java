package com.dct3194.projekakhir.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.OrderAdapter;
import com.dct3194.projekakhir.models.Order;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {

    private RecyclerView recyclerOrders;
    private LinearLayout layoutNoOrders;

    private OrderAdapter orderAdapter;
    private final List<Order> orderList = new ArrayList<>();

    private FirebaseFirestore db;

    public OrdersFragment() {
        // Required empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_orders,
                container,
                false
        );

        // =========================
        // Initialize Views
        // =========================

        recyclerOrders =
                view.findViewById(
                        R.id.recyclerOrders
                );

        layoutNoOrders =
                view.findViewById(
                        R.id.layoutNoOrders
                );

        // =========================
        // Firebase
        // =========================

        db = FirebaseFirestore.getInstance();

        // =========================
        // RecyclerView
        // =========================

        recyclerOrders.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        orderAdapter =
                new OrderAdapter(orderList);

        recyclerOrders.setAdapter(
                orderAdapter
        );

        // =========================
        // Load Orders
        // =========================

        loadOrders();

        return view;
    }

    // =========================================================
    // LOAD ALL ORDERS
    // =========================================================

    private void loadOrders() {

        db.collection("orders")
                .get()

                .addOnSuccessListener(
                        queryDocumentSnapshots -> {

                            orderList.clear();

                            // =========================
                            // Read Every Order
                            // =========================

                            queryDocumentSnapshots
                                    .forEach(documentSnapshot -> {

                                        try {

                                            Order order =
                                                    documentSnapshot
                                                            .toObject(
                                                                    Order.class
                                                            );

                                            if (order == null) {
                                                return;
                                            }

                                            // Use Firestore
                                            // document ID
                                            // as Order ID
                                            order.setOrderId(
                                                    documentSnapshot
                                                            .getId()
                                            );

                                            orderList.add(
                                                    order
                                            );

                                        } catch (Exception e) {

                                            e.printStackTrace();
                                        }
                                    });

                            // =========================
                            // Update RecyclerView
                            // =========================

                            orderAdapter.notifyDataSetChanged();

                            // =========================
                            // Show / Hide Empty State
                            // =========================

                            if (orderList.isEmpty()) {

                                recyclerOrders.setVisibility(
                                        View.GONE
                                );

                                layoutNoOrders.setVisibility(
                                        View.VISIBLE
                                );

                            } else {

                                recyclerOrders.setVisibility(
                                        View.VISIBLE
                                );

                                layoutNoOrders.setVisibility(
                                        View.GONE
                                );
                            }
                        }
                )

                .addOnFailureListener(
                        e -> {

                            e.printStackTrace();

                            orderList.clear();

                            orderAdapter.notifyDataSetChanged();

                            recyclerOrders.setVisibility(
                                    View.GONE
                            );

                            layoutNoOrders.setVisibility(
                                    View.VISIBLE
                            );

                            Toast.makeText(
                                    requireContext(),
                                    "Failed to load orders",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                );
    }

    // =========================================================
    // REFRESH WHEN RETURNING TO ORDERS
    // =========================================================

    @Override
    public void onResume() {

        super.onResume();

        if (db != null &&
                orderAdapter != null) {

            loadOrders();
        }
    }
}