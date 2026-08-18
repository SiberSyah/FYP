package com.dct3194.projekakhir.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class AircondDetailsFragment extends Fragment {

    private ImageView imgAircond;
    private ImageView btnBack;

    private TextView txtName;
    private TextView txtBrand;

    // =========================
    // Model Options
    // =========================

    private TextView btnFTKF;
    private TextView btnFTKU;
    private TextView btnFTKM;

    // =========================
    // Horsepower Options
    // =========================

    private TextView btn1HP;
    private TextView btn15HP;
    private TextView btn2HP;

    // =========================
    // Type Options
    // =========================

    private TextView btnInverter;
    private TextView btnNonInverter;

    // =========================
    // Package Options
    // =========================

    private TextView btnUnitOnly;
    private TextView btnWithInstall;

    // =========================
    // Bottom Views
    // =========================

    private TextView txtEstimatedPrice;
    private Button btnContinue;

    // =========================
    // Aircond Data
    // =========================

    private Aircond aircond;

    private int basePrice = 0;
    private boolean installation = false;

    private String selectedModel = "";
    private String selectedHP = "1.0 HP";
    private String selectedType = "";
    private String selectedPackage = "";

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
                false
        );

        // =========================
        // Header & Image
        // =========================

        imgAircond = view.findViewById(R.id.imgAircond);
        btnBack = view.findViewById(R.id.btnBack);

        // =========================
        // Titles
        // =========================

        txtName = view.findViewById(R.id.txtName);
        txtBrand = view.findViewById(R.id.txtBranS);

        // =========================
        // Model Options
        // =========================

        btnFTKF = view.findViewById(R.id.btnFTKF);
        btnFTKU = view.findViewById(R.id.btnFTKU);
        btnFTKM = view.findViewById(R.id.btnFTKM);

        // =========================
        // HP Options
        // =========================

        btn1HP = view.findViewById(R.id.btn1HP);
        btn15HP = view.findViewById(R.id.btn15HP);
        btn2HP = view.findViewById(R.id.btn2HP);

        // =========================
        // Type Options
        // =========================

        btnInverter = view.findViewById(R.id.btnInverter);
        btnNonInverter = view.findViewById(R.id.btnNonInverter);

        // =========================
        // Package Options
        // =========================

        btnUnitOnly = view.findViewById(R.id.btnUnitOnly);
        btnWithInstall = view.findViewById(R.id.btnWithInstall);

        // =========================
        // Bottom Views
        // =========================

        txtEstimatedPrice =
                view.findViewById(R.id.txtEstimatedPrice);

        btnContinue =
                view.findViewById(R.id.btnContinue);

        // =========================
        // Load Aircond From Bundle
        // =========================

        if (getArguments() != null) {

            aircond =
                    (Aircond) getArguments()
                            .getSerializable("aircond");

            if (aircond != null) {

                // =========================
                // Basic Information
                // =========================

                txtName.setText(
                        aircond.getName()
                );

                txtBrand.setText(
                        aircond.getBrand()
                );

                // =========================
                // Initial Price
                // =========================

                basePrice =
                        aircond.getPrice1HP();

                // =========================
                // Load Firebase Image
                // =========================

                loadAircondImage(
                        aircond.getImageUrl()
                );

                // =========================
                // Load Firestore Options
                // =========================

                setupModels();
                setupTypes();
                setupPackages();

                // =========================
                // Initial Price
                // =========================

                updatePrice();
            }
        }

        // =========================
        // HP Initial State
        // =========================

        selectButton(
                btn1HP,
                btn15HP,
                btn2HP
        );

        // =========================
        // HP Listeners
        // =========================

        setupHPListeners();

        // =========================
        // Back Navigation
        // =========================

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // =========================
        // Continue Navigation
        // =========================

        btnContinue.setOnClickListener(v -> {

            SelectDateTimeFragment fragment =
                    new SelectDateTimeFragment();

            Bundle bundle = new Bundle();

            bundle.putSerializable(
                    "aircond",
                    aircond
            );

            bundle.putBoolean(
                    "installation",
                    installation
            );

            bundle.putString(
                    "hp",
                    selectedHP
            );

            bundle.putString(
                    "type",
                    selectedType
            );

            bundle.putString(
                    "model",
                    selectedModel
            );

            bundle.putString(
                    "package",
                    selectedPackage
            );

            bundle.putInt(
                    "price",
                    basePrice
            );

            bundle.putInt(
                    "totalPrice",
                    totalPrice
            );

            fragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(
                            R.id.fragment_container,
                            fragment
                    )
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    // =====================================================
    // SETUP MODELS FROM FIRESTORE
    // =====================================================

    private void setupModels() {

        List<String> models = aircond.getModels();

        // =========================
        // Hide Semua Dahulu
        // =========================

        btnFTKF.setVisibility(View.GONE);
        btnFTKU.setVisibility(View.GONE);
        btnFTKM.setVisibility(View.GONE);

        if (models == null || models.isEmpty()) {
            return;
        }

        // =========================
        // Model 0
        // =========================

        if (models.size() > 0) {

            btnFTKF.setVisibility(View.VISIBLE);
            btnFTKF.setText(models.get(0));

            btnFTKF.setOnClickListener(v -> {

                selectedModel =
                        models.get(0);

                selectButton(
                        btnFTKF,
                        btnFTKU,
                        btnFTKM
                );
            });

            selectedModel =
                    models.get(0);
        }

        // =========================
        // Model 1
        // =========================

        if (models.size() > 1) {

            btnFTKU.setVisibility(View.VISIBLE);
            btnFTKU.setText(models.get(1));

            btnFTKU.setOnClickListener(v -> {

                selectedModel =
                        models.get(1);

                selectButton(
                        btnFTKU,
                        btnFTKF,
                        btnFTKM
                );
            });
        }

        // =========================
        // Model 2
        // =========================

        if (models.size() > 2) {

            btnFTKM.setVisibility(View.VISIBLE);
            btnFTKM.setText(models.get(2));

            btnFTKM.setOnClickListener(v -> {

                selectedModel =
                        models.get(2);

                selectButton(
                        btnFTKM,
                        btnFTKF,
                        btnFTKU
                );
            });
        }

        // =========================
        // IMPORTANT
        // Apply Style AFTER
        // All Buttons Are Visible
        // =========================

        selectButton(
                btnFTKF,
                btnFTKU,
                btnFTKM
        );
    }

    // =====================================================
    // SETUP TYPES FROM FIRESTORE
    // =====================================================

    private void setupTypes() {

        List<String> types =
                aircond.getTypes();

        // =========================
        // Hide Semua Dahulu
        // =========================

        btnInverter.setVisibility(View.GONE);
        btnNonInverter.setVisibility(View.GONE);

        if (types == null || types.isEmpty()) {
            return;
        }

        // =========================
        // Type 0
        // =========================

        if (types.size() > 0) {

            btnInverter.setVisibility(View.VISIBLE);
            btnInverter.setText(types.get(0));

            btnInverter.setOnClickListener(v -> {

                selectedType =
                        types.get(0);

                selectButton(
                        btnInverter,
                        btnNonInverter
                );

                updatePrice();
            });

            selectedType =
                    types.get(0);
        }

        // =========================
        // Type 1
        // =========================

        if (types.size() > 1) {

            btnNonInverter.setVisibility(View.VISIBLE);
            btnNonInverter.setText(types.get(1));

            btnNonInverter.setOnClickListener(v -> {

                selectedType =
                        types.get(1);

                selectButton(
                        btnNonInverter,
                        btnInverter
                );

                updatePrice();
            });
        }

        // =========================
        // IMPORTANT
        // Apply Style AFTER
        // All Buttons Are Visible
        // =========================

        selectButton(
                btnInverter,
                btnNonInverter
        );
    }

    // =====================================================
    // SETUP PACKAGES FROM FIRESTORE
    // =====================================================

    private void setupPackages() {

        List<String> packages =
                aircond.getPackages();

        // =========================
        // Hide Semua Dahulu
        // =========================

        btnUnitOnly.setVisibility(View.GONE);
        btnWithInstall.setVisibility(View.GONE);

        if (packages == null || packages.isEmpty()) {
            return;
        }

        // =========================
        // Package 0
        // =========================

        if (packages.size() > 0) {

            btnUnitOnly.setVisibility(View.VISIBLE);
            btnUnitOnly.setText(packages.get(0));

            btnUnitOnly.setOnClickListener(v -> {

                selectedPackage =
                        packages.get(0);

                installation = false;

                selectButton(
                        btnUnitOnly,
                        btnWithInstall
                );

                updatePrice();
            });

            selectedPackage =
                    packages.get(0);

            installation = false;
        }

        // =========================
        // Package 1
        // =========================

        if (packages.size() > 1) {

            btnWithInstall.setVisibility(View.VISIBLE);
            btnWithInstall.setText(packages.get(1));

            btnWithInstall.setOnClickListener(v -> {

                selectedPackage =
                        packages.get(1);

                installation = true;

                selectButton(
                        btnWithInstall,
                        btnUnitOnly
                );

                updatePrice();
            });
        }

        // =========================
        // IMPORTANT
        // Apply Style AFTER
        // All Buttons Are Visible
        // =========================

        selectButton(
                btnUnitOnly,
                btnWithInstall
        );
    }

    // =====================================================
    // HP LISTENERS
    // =====================================================

    private void setupHPListeners() {

        // =========================
        // 1.0 HP
        // =========================

        btn1HP.setOnClickListener(v -> {

            if (aircond != null) {

                basePrice =
                        aircond.getPrice1HP();
            }

            selectedHP = "1.0 HP";

            selectButton(
                    btn1HP,
                    btn15HP,
                    btn2HP
            );

            updatePrice();
        });

        // =========================
        // 1.5 HP
        // =========================

        btn15HP.setOnClickListener(v -> {

            if (aircond != null) {

                basePrice =
                        aircond.getPrice15HP();
            }

            selectedHP = "1.5 HP";

            selectButton(
                    btn15HP,
                    btn1HP,
                    btn2HP
            );

            updatePrice();
        });

        // =========================
        // 2.0 HP
        // =========================

        btn2HP.setOnClickListener(v -> {

            if (aircond != null) {

                basePrice =
                        aircond.getPrice2HP();
            }

            selectedHP = "2.0 HP";

            selectButton(
                    btn2HP,
                    btn1HP,
                    btn15HP
            );

            updatePrice();
        });
    }

    // =====================================================
    // LOAD IMAGE FROM FIREBASE STORAGE
    // =====================================================

    private void loadAircondImage(String imageUrl) {

        imgAircond.setImageResource(
                R.mipmap.ic_launcher
        );

        if (imageUrl == null ||
                imageUrl.trim().isEmpty()) {

            return;
        }

        try {

            StorageReference imageReference =
                    FirebaseStorage
                            .getInstance()
                            .getReferenceFromUrl(
                                    imageUrl
                            );

            imageReference
                    .getBytes(5 * 1024 * 1024)
                    .addOnSuccessListener(bytes -> {

                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(
                                        bytes,
                                        0,
                                        bytes.length
                                );

                        if (bitmap != null) {

                            imgAircond.setImageBitmap(
                                    bitmap
                            );
                        }
                    })
                    .addOnFailureListener(e -> {

                        imgAircond.setImageResource(
                                R.mipmap.ic_launcher
                        );
                    });

        } catch (Exception e) {

            imgAircond.setImageResource(
                    R.mipmap.ic_launcher
            );
        }
    }

    // =====================================================
    // BUTTON SELECTION
    // =====================================================

    private void selectButton(
            TextView active,
            TextView... inactives) {

        if (active == null ||
                active.getVisibility() != View.VISIBLE) {

            return;
        }

        // =========================
        // SELECTED BUTTON
        // =========================

        applyButtonStyle(
                active,
                "#1565C0",
                Color.WHITE
        );

        // =========================
        // UNSELECTED BUTTONS
        // =========================

        for (TextView inactive : inactives) {

            if (inactive != null &&
                    inactive.getVisibility() == View.VISIBLE) {

                applyButtonStyle(
                        inactive,
                        "#E0E9FA",
                        Color.parseColor("#1565C0")
                );
            }
        }
    }

    // =====================================================
    // APPLY SAME BUTTON STYLE
    // =====================================================

    private void applyButtonStyle(
            TextView button,
            String backgroundColor,
            int textColor) {

        if (button == null) {
            return;
        }

        // =========================
        // Remove Default Android
        // Button Appearance
        // =========================

        button.setBackgroundTintList(null);
        button.setStateListAnimator(null);
        button.setElevation(0f);
        button.setTranslationZ(0f);

        // =========================
        // Flat Background
        // With Corner Radius
        // =========================

        GradientDrawable background =
                new GradientDrawable();

        background.setColor(
                Color.parseColor(backgroundColor)
        );

        // Convert 12dp to pixels for rounded corners
        float cornerRadiusPx = 12 * button.getResources().getDisplayMetrics().density;
        background.setCornerRadius(cornerRadiusPx);

        button.setBackground(background);

        // =========================
        // Text
        // =========================

        button.setTextColor(textColor);

        // =========================
        // Remove Extra Elevation
        // =========================

        button.setAlpha(1.0f);
    }

    // =====================================================
    // UPDATE TOTAL PRICE
    // =====================================================

    private void updatePrice() {

        int total = basePrice;

        if (installation &&
                aircond != null) {

            total +=
                    aircond.getInstallationPrice();
        }

        totalPrice = total;

        txtEstimatedPrice.setText(
                "RM" +
                        String.format(
                                "%,d",
                                total
                        )
        );
    }
}