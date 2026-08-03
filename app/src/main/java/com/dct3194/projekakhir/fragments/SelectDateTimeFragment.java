package com.dct3194.projekakhir.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private TextView txtSummaryName;
    private TextView txtSummaryBrand;
    private TextView txtSummaryHP;
    private TextView txtSummaryInstall;

    private Aircond aircond;

    private boolean installation = false;
    private String selectedHP = "";

    private String selectedModel = "";
    private String selectedPackage = "";
    private int selectedPrice = 0;
    private int totalPrice = 0;

    private String selectedDate = "";
    private String selectedTime = "";

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
                false);

        btnBack = view.findViewById(R.id.btnBack);
        imgSummaryAircond = view.findViewById(R.id.imgSummaryAircond);

        btn8AM = view.findViewById(R.id.btn8AM);
        btn9AM = view.findViewById(R.id.btn9AM);
        btn11AM = view.findViewById(R.id.btn11AM);

        btn1PM = view.findViewById(R.id.btn1PM);
        btn2PM = view.findViewById(R.id.btn2PM);
        btn4PM = view.findViewById(R.id.btn4PM);

        btnContinue = view.findViewById(R.id.btnContinue);

        txtSummaryName = view.findViewById(R.id.txtSummaryName);
        txtSummaryBrand = view.findViewById(R.id.txtSummaryBrand);
        txtSummaryHP = view.findViewById(R.id.txtSummaryHP);
        txtSummaryInstall = view.findViewById(R.id.txtSummaryInstall);

        if (getArguments() != null) {

            aircond = (Aircond) getArguments().getSerializable("aircond");
            installation = getArguments().getBoolean("installation", false);
            selectedHP = getArguments().getString("hp", "1 HP");

            selectedModel = getArguments().getString("model", "");
            selectedPackage = getArguments().getString("package", "");
            selectedPrice = getArguments().getInt("price", 0);
            totalPrice = getArguments().getInt("totalPrice", 0);

            if (aircond != null) {
                imgSummaryAircond.setImageResource(aircond.getImage());
                txtSummaryName.setText(aircond.getName());
                txtSummaryBrand.setText("Brand : " + aircond.getBrand());
                txtSummaryHP.setText("HP : " + selectedHP);
            }

            txtSummaryInstall.setText(
                    installation ?
                            "Installation : Yes"
                            :
                            "Installation : No");
        }

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
                        .popBackStack());

        // =========================
        // Continue
        // =========================

        btnContinue.setOnClickListener(v -> {

            // Semakan jika user belum pilih masa
            if (selectedTime.isEmpty()) {
                Toast.makeText(getContext(), "Sila pilih masa perkhidmatan!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Pindah ke skrin AddAddressActivity
            Intent intent = new Intent(getActivity(), AddAddressActivity.class);

            // Bawa sekali semua maklumat tempahan ke skrin alamat
            intent.putExtra("aircond", aircond);
            intent.putExtra("installation", installation);
            intent.putExtra("hp", selectedHP);
            intent.putExtra("date", selectedDate);
            intent.putExtra("time", selectedTime);

            intent.putExtra("model", selectedModel);
            intent.putExtra("package", selectedPackage);
            intent.putExtra("price", selectedPrice);
            intent.putExtra("totalPrice", totalPrice);

            startActivity(intent);
        });

        return view;
    }

    // =========================
    // Time Slot Selection
    // =========================

    private void selectTimeButton(Button selectedButton) {

        Button[] buttons = {
                btn8AM,
                btn9AM,
                btn11AM,
                btn1PM,
                btn2PM,
                btn4PM
        };

        for (Button button : buttons) {
            button.setBackgroundResource(R.drawable.button_outline);
            button.setTextColor(Color.parseColor("#3158B8"));
        }

        if (selectedButton != null) {
            selectedButton.setBackgroundResource(R.drawable.button_blue);
            selectedButton.setTextColor(Color.WHITE);
        }
    }
}