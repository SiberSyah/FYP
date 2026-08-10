package com.dct3194.projekakhir.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.activities.AddAddressActivity;
import com.dct3194.projekakhir.activities.MaintenanceSummaryActivity;
import com.dct3194.projekakhir.models.Aircond;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;
import java.util.Locale;

public class SelectDateTimeFragment extends Fragment {

    private ImageView btnBack;
    private ImageView imgAircond;
    private ImageView imgSummaryAircond;

    private AppCompatButton[] timeButtons;
    private AppCompatButton selectedButton;

    private Button btn8AM;
    private Button btn9AM;
    private Button btn11AM;
    private Button btn1PM;
    private Button btn2PM;
    private Button btn4PM;

    private Button btnContinue;

    private CalendarView calendarView;

    private TextView txtSummaryName;
    private TextView txtSummaryBrand;
    private TextView txtSummaryHP;
    private TextView txtSummaryInstall;

    private Aircond aircond;

    private boolean installation = false;

    private String selectedHP = "";

    private String selectedModel = "";
    private String selectedType = "";
    private String selectedPackage = "";

    private int selectedPrice = 0;
    private int totalPrice = 0;

    private String selectedDate = "";
    private String selectedTime = "";

    // =========================
    // Mode & Maintenance Data
    // =========================

    private String mode = "BUY";
    private String maintenanceType = "";
    private String maintenanceService = "";
    private String maintenancePrice = "";

    public SelectDateTimeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_select_date_time,
                container,
                false
        );

        btnBack = view.findViewById(R.id.btnBack);
        imgSummaryAircond =
                view.findViewById(R.id.imgSummaryAircond);

        btn8AM = view.findViewById(R.id.btn8AM);
        btn9AM = view.findViewById(R.id.btn9AM);
        btn11AM = view.findViewById(R.id.btn11AM);

        btn1PM = view.findViewById(R.id.btn1PM);
        btn2PM = view.findViewById(R.id.btn2PM);
        btn4PM = view.findViewById(R.id.btn4PM);

        btnContinue = view.findViewById(R.id.btnContinue);

        calendarView = view.findViewById(R.id.calendarView);

        txtSummaryName =
                view.findViewById(R.id.txtSummaryName);

        txtSummaryBrand =
                view.findViewById(R.id.txtSummaryBrand);

        txtSummaryHP =
                view.findViewById(R.id.txtSummaryHP);

        txtSummaryInstall =
                view.findViewById(R.id.txtSummaryInstall);

        // =========================
        // Load Arguments
        // =========================

        if (getArguments() != null) {

            mode = getArguments().getString(
                    "mode",
                    "BUY"
            );

            maintenanceType =
                    getArguments().getString(
                            "type",
                            ""
                    );

            maintenanceService =
                    getArguments().getString(
                            "service",
                            ""
                    );

            maintenancePrice =
                    getArguments().getString(
                            "price",
                            ""
                    );

            aircond =
                    (Aircond) getArguments()
                            .getSerializable("aircond");

            installation =
                    getArguments().getBoolean(
                            "installation",
                            false
                    );

            selectedHP =
                    getArguments().getString(
                            "hp",
                            "1 HP"
                    );

            selectedModel =
                    getArguments().getString(
                            "model",
                            ""
                    );

            selectedType =
                    getArguments().getString(
                            "type",
                            ""
                    );

            selectedPackage =
                    getArguments().getString(
                            "package",
                            ""
                    );

            selectedPrice =
                    getArguments().getInt(
                            "price",
                            0
                    );

            totalPrice =
                    getArguments().getInt(
                            "totalPrice",
                            0
                    );

            // =========================
            // SAFETY PRICE CALCULATION
            // =========================

            if (mode.equals("BUY") &&
                    aircond != null) {

                /*
                 * Kalau totalPrice yang dihantar
                 * adalah 0, kira semula di sini.
                 */

                if (totalPrice <= 0) {

                    if (selectedPrice > 0) {

                        totalPrice =
                                selectedPrice;

                    } else {

                        if (selectedHP.equals(
                                "1.5 HP")) {

                            selectedPrice =
                                    aircond.getPrice15HP();

                        } else if (
                                selectedHP.equals(
                                        "2.0 HP")) {

                            selectedPrice =
                                    aircond.getPrice2HP();

                        } else {

                            selectedPrice =
                                    aircond.getPrice1HP();
                        }

                        totalPrice =
                                selectedPrice;
                    }

                    // Tambah installation
                    if (installation) {

                        totalPrice +=
                                aircond.getInstallationPrice();
                    }
                }
            }

            // =========================
            // Dynamic Summary
            // =========================

            if (mode.equals("MAINTENANCE")) {

                txtSummaryName.setText(
                        maintenanceService
                );

                txtSummaryBrand.setText(
                        "Type : " + maintenanceType
                );

                txtSummaryHP.setText(
                        "HP : " + selectedHP
                );

                txtSummaryInstall.setText(
                        "Price : " + maintenancePrice
                );

                imgSummaryAircond.setImageResource(
                        R.drawable.ic_maintenance
                );

            } else {

                if (aircond != null) {

                    txtSummaryName.setText(
                            aircond.getName()
                    );

                    txtSummaryBrand.setText(
                            "Brand : " + aircond.getBrand()
                    );

                    txtSummaryHP.setText(
                            "HP : " + selectedHP
                    );

                    txtSummaryInstall.setText(
                            installation
                                    ? "Installation : Yes"
                                    : "Installation : No"
                    );

                    loadAircondImage(
                            aircond.getImageUrl()
                    );
                }
            }
        }

        // =========================
        // Calendar Date
        // =========================

        Calendar calendar =
                Calendar.getInstance();

        selectedDate =
                String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%d",
                        calendar.get(
                                Calendar.DAY_OF_MONTH
                        ),
                        calendar.get(
                                Calendar.MONTH
                        ) + 1,
                        calendar.get(
                                Calendar.YEAR
                        )
                );

        calendarView.setOnDateChangeListener(
                (view1, year, month, dayOfMonth) -> {

                    selectedDate =
                            String.format(
                                    Locale.getDefault(),
                                    "%02d/%02d/%d",
                                    dayOfMonth,
                                    month + 1,
                                    year
                            );
                }
        );

        // =========================
        // Time Slot
        // =========================

        btn8AM.setOnClickListener(v -> {

            selectedTime = "8:00 AM";

            selectTimeButton(btn8AM);
        });

        btn9AM.setOnClickListener(v -> {

            selectedTime = "9:00 AM";

            selectTimeButton(btn9AM);
        });

        btn11AM.setOnClickListener(v -> {

            selectedTime = "11:00 AM";

            selectTimeButton(btn11AM);
        });

        btn1PM.setOnClickListener(v -> {

            selectedTime = "1:00 PM";

            selectTimeButton(btn1PM);
        });

        btn2PM.setOnClickListener(v -> {

            selectedTime = "2:00 PM";

            selectTimeButton(btn2PM);
        });

        btn4PM.setOnClickListener(v -> {

            selectedTime = "4:00 PM";

            selectTimeButton(btn4PM);
        });

        // =========================
        // Back
        // =========================

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // =========================
        // Continue
        // =========================

        btnContinue.setOnClickListener(v -> {

            // =========================
            // Check Time
            // =========================

            if (selectedTime.isEmpty()) {

                Toast.makeText(
                        getContext(),
                        "Sila pilih masa perkhidmatan!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // =========================
            // Maintenance
            // =========================

            if (mode.equals("MAINTENANCE")) {

                Intent intent =
                        new Intent(
                                getActivity(),
                                MaintenanceSummaryActivity.class
                        );

                intent.putExtra(
                        "hp",
                        selectedHP
                );

                intent.putExtra(
                        "type",
                        maintenanceType
                );

                intent.putExtra(
                        "service",
                        maintenanceService
                );

                intent.putExtra(
                        "price",
                        maintenancePrice
                );

                intent.putExtra(
                        "date",
                        selectedDate
                );

                intent.putExtra(
                        "time",
                        selectedTime
                );

                startActivity(intent);

            } else {

                // =========================
                // BUY AIRCOND
                // =========================

                /*
                 * Safety check sekali lagi.
                 *
                 * Kalau masih 0, kira semula
                 * sebelum masuk Address.
                 */

                if (totalPrice <= 0 &&
                        aircond != null) {

                    if (selectedPrice <= 0) {

                        if (selectedHP.equals(
                                "1.5 HP")) {

                            selectedPrice =
                                    aircond.getPrice15HP();

                        } else if (
                                selectedHP.equals(
                                        "2.0 HP")) {

                            selectedPrice =
                                    aircond.getPrice2HP();

                        } else {

                            selectedPrice =
                                    aircond.getPrice1HP();
                        }
                    }

                    totalPrice =
                            selectedPrice;

                    if (installation) {

                        totalPrice +=
                                aircond.getInstallationPrice();
                    }
                }

                // =========================
                // Debug / Safety
                // =========================

                if (totalPrice <= 0) {

                    Toast.makeText(
                            requireContext(),
                            "Unable to calculate aircond price.",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                Intent intent =
                        new Intent(
                                getActivity(),
                                AddAddressActivity.class
                        );

                // =========================
                // Aircond
                // =========================

                intent.putExtra(
                        "aircond",
                        aircond
                );

                // =========================
                // Installation
                // =========================

                intent.putExtra(
                        "installation",
                        installation
                );

                // =========================
                // HP
                // =========================

                intent.putExtra(
                        "hp",
                        selectedHP
                );

                // =========================
                // Type
                // =========================

                intent.putExtra(
                        "type",
                        selectedType
                );

                // =========================
                // Date
                // =========================

                intent.putExtra(
                        "date",
                        selectedDate
                );

                // =========================
                // Time
                // =========================

                intent.putExtra(
                        "time",
                        selectedTime
                );

                // =========================
                // Model
                // =========================

                intent.putExtra(
                        "model",
                        selectedModel
                );

                // =========================
                // Package
                // =========================

                intent.putExtra(
                        "package",
                        selectedPackage
                );

                // =========================
                // Price
                // =========================

                intent.putExtra(
                        "price",
                        selectedPrice
                );

                // =========================
                // TOTAL PRICE
                // =========================

                intent.putExtra(
                        "totalPrice",
                        totalPrice
                );

                startActivity(intent);
            }
        });

        return view;
    }

    // =========================
    // Load Image From Firebase
    // =========================

    private void loadAircondImage(
            String imageUrl) {

        imgSummaryAircond.setImageResource(
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

                        if (!isAdded()) {
                            return;
                        }

                        Bitmap bitmap =
                                BitmapFactory.decodeByteArray(
                                        bytes,
                                        0,
                                        bytes.length
                                );

                        if (bitmap != null) {

                            imgSummaryAircond
                                    .setImageBitmap(
                                            bitmap
                                    );
                        }
                    })
                    .addOnFailureListener(e -> {

                        if (!isAdded()) {
                            return;
                        }

                        imgSummaryAircond
                                .setImageResource(
                                        R.mipmap.ic_launcher
                                );
                    });

        } catch (Exception e) {

            imgSummaryAircond.setImageResource(
                    R.mipmap.ic_launcher
            );
        }
    }

    // =========================
    // Time Slot Selection
    // =========================

    private void selectTimeButton(
            Button selectedButton) {

        Button[] buttons = {
                btn8AM,
                btn9AM,
                btn11AM,
                btn1PM,
                btn2PM,
                btn4PM
        };

        for (Button button : buttons) {

            button.setBackgroundResource(
                    R.drawable.button_outline
            );

            button.setTextColor(
                    Color.parseColor("#3158B8")
            );
        }

        if (selectedButton != null) {

            selectedButton.setBackgroundResource(
                    R.drawable.button_blue
            );

            selectedButton.setTextColor(
                    Color.WHITE
            );
        }
    }
}