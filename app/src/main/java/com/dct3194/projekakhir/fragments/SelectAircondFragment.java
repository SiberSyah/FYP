package com.dct3194.projekakhir.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.AircondAdapter;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class SelectAircondFragment extends Fragment {

    private RecyclerView recyclerAircond;
    private AircondAdapter adapter;
    private List<Aircond> aircondList;

    private FirebaseFirestore db;

    public SelectAircondFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_select_aircond,
                container,
                false
        );

        // =========================
        // RecyclerView
        // =========================

        recyclerAircond = view.findViewById(
                R.id.recyclerAircond
        );

        recyclerAircond.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        // =========================
        // List
        // =========================

        aircondList = new ArrayList<>();

        // =========================
        // Adapter
        // =========================

        adapter = new AircondAdapter(
                aircondList,
                aircond -> {

                    Bundle bundle = new Bundle();

                    bundle.putSerializable(
                            "aircond",
                            aircond
                    );

                    AircondDetailsFragment fragment =
                            new AircondDetailsFragment();

                    fragment.setArguments(bundle);

                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(
                                    R.id.fragment_container,
                                    fragment
                            )
                            .addToBackStack(null)
                            .commit();
                }
        );

        recyclerAircond.setAdapter(adapter);

        // =========================
        // Firebase
        // =========================

        db = FirebaseFirestore.getInstance();

        loadAircondsFromFirebase();

        return view;
    }

    // =========================
    // Load Aircond From Firestore
    // =========================

    private void loadAircondsFromFirebase() {

        db.collection("airconds")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    aircondList.clear();

                    for (QueryDocumentSnapshot document :
                            queryDocumentSnapshots) {

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

                        if (name == null) {
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
                        // Models
                        // =========================

                        List<String> models =
                                (List<String>) document.get("models");

                        if (models != null) {
                            aircond.setModels(models);
                        }

                        // =========================
                        // Types
                        // =========================

                        List<String> types =
                                (List<String>) document.get("types");

                        if (types != null) {
                            aircond.setTypes(types);
                        }

                        // =========================
                        // Packages
                        // =========================

                        List<String> packages =
                                (List<String>) document.get("packages");

                        if (packages != null) {
                            aircond.setPackages(packages);
                        }

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
                                requireContext(),
                                "No aircond available.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }

                })
                .addOnFailureListener(e -> {

                    Toast.makeText(
                            requireContext(),
                            "Failed to load aircond data.",
                            Toast.LENGTH_SHORT
                    ).show();
                });
    }
}