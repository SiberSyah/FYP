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

        // =========================
        // INITIALIZE TIME SLOTS
        // =========================

        Button[] slots = {
                btn8AM,
                btn9AM,
                btn11AM,
                btn1PM,
                btn2PM,
                btn4PM
        };

        for (Button btn : slots) {

            btn.setEnabled(true);
            btn.setAlpha(1f);

            btn.setTextColor(
                    Color.parseColor("#3158B8")
            );

            btn.setBackgroundResource(
                    R.drawable.button_outline
            );
        }

        btnContinue =
                view.findViewById(R.id.btnContinue);

        calendarView =
                view.findViewById(R.id.calendarView);

        txtSummaryName =
                view.findViewById(R.id.txtSummaryName);

        txtSummaryBrand =
                view.findViewById(R.id.txtSummaryBrand);

        txtSummaryHP =
                view.findViewById(R.id.txtSummaryHP);

        txtSummaryInstall =
                view.findViewById(R.id.txtSummaryInstall);

        // =========================
        // LOAD ARGUMENTS
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

                    // =========================
                    // INSTALLATION PRICE
                    // =========================

                    if (installation) {

                        totalPrice +=
                                aircond.getInstallationPrice();
                    }
                }
            }

            // =========================
            // DYNAMIC SUMMARY
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
        // CALENDAR DATE
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
        // TIME SLOTS
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
        // BACK
        // =========================

        btnBack.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .popBackStack()
        );

        // =========================
        // CONTINUE
        // =========================

        btnContinue.setOnClickListener(v -> {

            // =========================
            // CHECK TIME
            // =========================

            if (selectedTime.isEmpty()) {

                Toast.makeText(
                        getContext(),
                        "Sila pilih masa perkhidmatan!",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            Intent intent =
                    new Intent(
                            getActivity(),
                            AddAddressActivity.class
                    );

            // =====================================================
            // MAINTENANCE
            // =====================================================

            if (mode.equals("MAINTENANCE")) {

                // Convert maintenance price String -> int
                int maintenancePriceValue =
                        parsePriceToInt(
                                maintenancePrice
                        );

                // Safety check
                if (maintenancePriceValue <= 0) {

                    Toast.makeText(
                            requireContext(),
                            "Harga maintenance tidak sah: "
                                    + maintenancePrice,
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

                // =========================
                // Maintenance Flag
                // =========================

                intent.putExtra(
                        "maintenance",
                        true
                );

                // =========================
                // Maintenance HP
                // =========================

                intent.putExtra(
                        "hp",
                        selectedHP
                );

                // =========================
                // Maintenance Type
                // =========================

                intent.putExtra(
                        "type",
                        maintenanceType
                );

                // =========================
                // Maintenance Service
                // =========================

                intent.putExtra(
                        "service",
                        maintenanceService
                );

                // =================================================
                // IMPORTANT
                // =================================================
                // AddAddressActivity baca "price" sebagai INT.
                // Jadi sekarang kita hantar INT, bukan String.
                // =================================================

                intent.putExtra(
                        "price",
                        maintenancePriceValue
                );

                // Simpan juga String asal untuk screen seterusnya
                // yang mungkin perlukan paparan seperti "RM100".

                intent.putExtra(
                        "maintenancePrice",
                        maintenancePrice
                );

                // Simpan totalPrice sebagai INT juga.

                intent.putExtra(
                        "totalPrice",
                        maintenancePriceValue
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

            } else {

                // =================================================
                // BUY AIRCOND
                // =================================================

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

                    // =========================
                    // Installation
                    // =========================

                    if (installation) {

                        totalPrice +=
                                aircond.getInstallationPrice();
                    }
                }

                // =========================
                // PRICE VALIDATION
                // =========================

                if (totalPrice <= 0) {

                    Toast.makeText(
                            requireContext(),
                            "Unable to calculate aircond price.",
                            Toast.LENGTH_LONG
                    ).show();

                    return;
                }

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
                // Selected Price
                // =========================

                intent.putExtra(
                        "price",
                        selectedPrice
                );

                // =========================
                // Total Price
                // =========================

                intent.putExtra(
                        "totalPrice",
                        totalPrice
                );
            }

            // =========================
            // OPEN ADDRESS PAGE
            // =========================

            startActivity(intent);
        });

        return view;
    }

    // =====================================================
    // PARSE MAINTENANCE PRICE
    // =====================================================

    private int parsePriceToInt(String priceText) {

        if (priceText == null ||
                priceText.trim().isEmpty()) {

            return 0;
        }

        try {

            // Remove everything except digits.
            //
            // Examples:
            //
            // "100"       -> 100
            // "RM100"     -> 100
            // "RM 100"    -> 100
            // "RM 100.00" -> 10000 if decimal exists
            //
            // Most maintenance prices in this app
            // should be whole Ringgit values.

            String digitsOnly =
                    priceText.replaceAll(
                            "[^0-9]",
                            ""
                    );

            if (digitsOnly.isEmpty()) {
                return 0;
            }

            return Integer.parseInt(
                    digitsOnly
            );

        } catch (Exception e) {

            return 0;
        }
    }

    // =====================================================
    // LOAD IMAGE FROM FIREBASE
    // =====================================================

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

    // =====================================================
    // TIME SLOT SELECTION
    // =====================================================

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