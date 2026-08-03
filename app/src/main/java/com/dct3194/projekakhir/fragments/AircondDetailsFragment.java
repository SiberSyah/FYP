package com.dct3194.projekakhir.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.models.Aircond;

public class AircondDetailsFragment extends Fragment {

    private ImageView imgAircond;
    private ImageView btnBack;

    private TextView txtName;
    private TextView txtBrand;

    // 1. Model Options
    private TextView btnFTKF, btnFTKU, btnFTKM;

    // 2. Horsepower Options
    private TextView btn1HP, btn15HP, btn2HP;

    // 3. Package Options (Unit Only / Unit + Installation)
    private TextView btnUnitOnly, btnWithInstall;

    // Estimated Price & Action
    private TextView txtEstimatedPrice;
    private Button btnContinue;

    private Aircond aircond;

    private int basePrice = 0;
    private boolean installation = false;
    private String selectedModel = "FTKF Series";
    private String selectedHP = "1.0 HP";
    private String selectedPackage = "Unit Only";

    private int totalPrice = 0;

    public AircondDetailsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_aircond_details,
                container,
                false);

        // Header & Image
        imgAircond = view.findViewById(R.id.imgAircond);
        btnBack = view.findViewById(R.id.btnBack);

        // Titles
        txtName = view.findViewById(R.id.txtName);
        txtBrand = view.findViewById(R.id.txtBranS);

        // 1. Select Model Views
        btnFTKF = view.findViewById(R.id.btnFTKF);
        btnFTKU = view.findViewById(R.id.btnFTKU);
        btnFTKM = view.findViewById(R.id.btnFTKM);

        // 2. Select HP Views
        btn1HP = view.findViewById(R.id.btn1HP);
        btn15HP = view.findViewById(R.id.btn15HP);
        btn2HP = view.findViewById(R.id.btn2HP);

        // 3. Select Package Views
        btnUnitOnly = view.findViewById(R.id.btnUnitOnly);
        btnWithInstall = view.findViewById(R.id.btnWithInstall);

        // Bottom Views
        txtEstimatedPrice = view.findViewById(R.id.txtEstimatedPrice);
        btnContinue = view.findViewById(R.id.btnContinue);

        // Load Aircond Data from Bundle
        if (getArguments() != null) {
            aircond = (Aircond) getArguments().getSerializable("aircond");

            if (aircond != null) {
                imgAircond.setImageResource(aircond.getImage());
                txtName.setText(aircond.getName());
                txtBrand.setText(aircond.getBrand());
                basePrice = aircond.getPrice1HP();
            }
        }

        // Setup Initial Selected UI State
        selectButton(btnFTKF, btnFTKU, btnFTKM);
        selectButton(btn1HP, btn15HP, btn2HP);
        selectButton(btnUnitOnly, btnWithInstall);
        updatePrice();

        // =========================
        // 1. MODEL LISTENERS
        // =========================
        btnFTKF.setOnClickListener(v -> {
            selectedModel = "FTKF Series";
            selectButton(btnFTKF, btnFTKU, btnFTKM);
        });

        btnFTKU.setOnClickListener(v -> {
            selectedModel = "FTKU Series";
            selectButton(btnFTKU, btnFTKF, btnFTKM);
        });

        btnFTKM.setOnClickListener(v -> {
            selectedModel = "FTKM Series";
            selectButton(btnFTKM, btnFTKF, btnFTKU);
        });

        // =========================
        // 2. HP LISTENERS
        // =========================
        btn1HP.setOnClickListener(v -> {
            if (aircond != null) basePrice = aircond.getPrice1HP();
            selectedHP = "1.0 HP";
            selectButton(btn1HP, btn15HP, btn2HP);
            updatePrice();
        });

        btn15HP.setOnClickListener(v -> {
            if (aircond != null) basePrice = aircond.getPrice15HP();
            selectedHP = "1.5 HP";
            selectButton(btn15HP, btn1HP, btn2HP);
            updatePrice();
        });

        btn2HP.setOnClickListener(v -> {
            if (aircond != null) basePrice = aircond.getPrice2HP();
            selectedHP = "2.0 HP";
            selectButton(btn2HP, btn1HP, btn15HP);
            updatePrice();
        });

        // =========================
        // 3. PACKAGE LISTENERS
        // =========================
        btnUnitOnly.setOnClickListener(v -> {
            installation = false;
            selectedPackage = "Unit Only";
            selectButton(btnUnitOnly, btnWithInstall);
            updatePrice();
        });

        btnWithInstall.setOnClickListener(v -> {
            installation = true;
            selectedPackage = "Unit + Installation";
            selectButton(btnWithInstall, btnUnitOnly);
            updatePrice();
        });

        // Back Navigation
        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack());

        // Continue Navigation
        btnContinue.setOnClickListener(v -> {
            SelectDateTimeFragment fragment = new SelectDateTimeFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("aircond", aircond);
            bundle.putBoolean("installation", installation);
            bundle.putString("hp", selectedHP);
            bundle.putString("model", selectedModel);
            bundle.putString("package", selectedPackage);
            bundle.putInt("price", basePrice);
            bundle.putInt("totalPrice", totalPrice);

            fragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // Tukar warna butang yang dipilih (Active vs Inactive)
    private void selectButton(TextView active, TextView... inactives) {
        active.setBackgroundColor(Color.parseColor("#1565C0"));
        active.setTextColor(Color.WHITE);

        for (TextView inactive : inactives) {
            inactive.setBackgroundColor(Color.parseColor("#E0E9FA"));
            inactive.setTextColor(Color.parseColor("#1565C0"));
        }
    }

    // Kemaskini jumlah harga
    private void updatePrice() {
        int total = basePrice;
        if (installation) {
            total += 250; // Kos pemasangan
        }
        totalPrice = total;
        txtEstimatedPrice.setText("RM" + String.format("%,d", total));
    }
}