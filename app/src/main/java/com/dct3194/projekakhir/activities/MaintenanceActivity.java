package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import java.util.Locale;

public class MaintenanceActivity extends AppCompatActivity {

    private TextView btnHP10, btnHP15, btnHP20, btnHP25;
    private TextView btnInverter, btnNonInverter;
    private TextView btnBasic, btnChemicalWash, btnOverhaul;

    private LinearLayout layoutPrice;
    private TextView tvPriceValue;
    private TextView btnNext;

    // Bottom Navigation Icons
    private ImageView iconProfile;
    private ImageView iconHome;
    private ImageView iconOrders;

    private String selectedHP = "";
    private String selectedType = "";
    private String selectedService = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance);

        // Initialize HP Buttons
        btnHP10 = findViewById(R.id.btnHP10);
        btnHP15 = findViewById(R.id.btnHP15);
        btnHP20 = findViewById(R.id.btnHP20);
        btnHP25 = findViewById(R.id.btnHP25);

        // Initialize Type Buttons
        btnInverter = findViewById(R.id.btnInverter);
        btnNonInverter = findViewById(R.id.btnNonInverter);

        // Initialize Service Buttons
        btnBasic = findViewById(R.id.btnBasic);
        btnChemicalWash = findViewById(R.id.btnChemicalWash);
        btnOverhaul = findViewById(R.id.btnOverhaul);

        // Initialize Other Views
        layoutPrice = findViewById(R.id.layoutPrice);
        tvPriceValue = findViewById(R.id.tvPriceValue);
        btnNext = findViewById(R.id.btnNext);

        // Initialize Bottom Navigation Icons
        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        // Set Home icon as active by default
        selectHome();

        btnNext.setOnClickListener(v -> {

            if (selectedHP.isEmpty() ||
                    selectedType.isEmpty() ||
                    selectedService.isEmpty()) {

                android.widget.Toast.makeText(
                        this,
                        "Please select all options first",
                        android.widget.Toast.LENGTH_SHORT
                ).show();

                return;
            }

            // Open MainActivity with SelectDateTimeFragment in MAINTENANCE mode
            Intent intent = new Intent(
                    MaintenanceActivity.this,
                    MainActivity.class
            );

            intent.putExtra("openFragment", "maintenanceDate");
            intent.putExtra("mode", "MAINTENANCE");
            intent.putExtra("hp", selectedHP);
            intent.putExtra("type", selectedType);
            intent.putExtra("service", selectedService);
            intent.putExtra("price", tvPriceValue.getText().toString());

            startActivity(intent);
        });

        // Set Click Listeners
        setHPListeners();
        setTypeListeners();
        setServiceListeners();

        // Header Back Icon
        findViewById(R.id.tvHeaderTitle).setOnClickListener(v -> finish());

        // Bottom Navigation Listeners
        findViewById(R.id.navHome).setOnClickListener(v -> {
            selectHome();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "home");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            selectProfile();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        findViewById(R.id.navOrders).setOnClickListener(v -> {
            selectOrders();
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "orders");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private void setHPListeners() {
        View.OnClickListener listener = v -> {
            resetHPButtons();
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.white));
            selectedHP = ((TextView) v).getText().toString();
            updateUI();
        };
        btnHP10.setOnClickListener(listener);
        btnHP15.setOnClickListener(listener);
        btnHP20.setOnClickListener(listener);
        btnHP25.setOnClickListener(listener);
    }

    private void resetHPButtons() {
        TextView[] buttons = {btnHP10, btnHP15, btnHP20, btnHP25};
        for (TextView btn : buttons) {
            btn.setBackgroundResource(R.drawable.bg_button_unselected);
            btn.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
        }
    }

    private void setTypeListeners() {
        View.OnClickListener listener = v -> {
            resetTypeButtons();
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.white));
            selectedType = ((TextView) v).getText().toString();
            updateUI();
        };
        btnInverter.setOnClickListener(listener);
        btnNonInverter.setOnClickListener(listener);
    }

    private void resetTypeButtons() {
        TextView[] buttons = {btnInverter, btnNonInverter};
        for (TextView btn : buttons) {
            btn.setBackgroundResource(R.drawable.bg_button_unselected);
            btn.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
        }
    }

    private void setServiceListeners() {
        View.OnClickListener listener = v -> {
            resetServiceButtons();
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((TextView) v).setTextColor(ContextCompat.getColor(this, R.color.white));
            selectedService = ((TextView) v).getText().toString();
            updateUI();
        };
        btnBasic.setOnClickListener(listener);
        btnChemicalWash.setOnClickListener(listener);
        btnOverhaul.setOnClickListener(listener);
    }

    private void resetServiceButtons() {
        TextView[] buttons = {btnBasic, btnChemicalWash, btnOverhaul};
        for (TextView btn : buttons) {
            btn.setBackgroundResource(R.drawable.bg_button_unselected);
            btn.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
        }
    }

    private void updateUI() {
        if (!selectedHP.isEmpty() && !selectedType.isEmpty() && !selectedService.isEmpty()) {
            calculateAndDisplayPrice();
            tvPriceValue.setVisibility(View.VISIBLE);
        }
    }

    private void calculateAndDisplayPrice() {
        double price = 0;
        // Service base price
        if (selectedService.contains("Basic")) price = 80.0;
        else if (selectedService.contains("Wash")) price = 150.0;
        else if (selectedService.contains("Overhaul")) price = 250.0;

        // HP extra charge
        if (selectedHP.contains("1.5")) price += 20.0;
        else if (selectedHP.contains("2.0")) price += 50.0;
        else if (selectedHP.contains("2.5")) price += 80.0;

        // Type extra charge
        if (selectedType.equalsIgnoreCase("INVERTER")) price += 10.0;

        tvPriceValue.setText(String.format(Locale.getDefault(), "RM %.2f", price));
    }

    // =========================================================
    // BOTTOM NAVIGATION ICON COLOR LOGIC
    // =========================================================

    private void resetIcons() {
        if (iconProfile != null) {
            iconProfile.setImageTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if (iconHome != null) {
            iconHome.setImageTintList(ColorStateList.valueOf(Color.GRAY));
        }
        if (iconOrders != null) {
            iconOrders.setImageTintList(ColorStateList.valueOf(Color.GRAY));
        }
    }

    private void selectHome() {
        resetIcons();
        if (iconHome != null) {
            iconHome.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3158B8")));
        }
    }

    private void selectProfile() {
        resetIcons();
        if (iconProfile != null) {
            iconProfile.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3158B8")));
        }
    }

    private void selectOrders() {
        resetIcons();
        if (iconOrders != null) {
            iconOrders.setImageTintList(ColorStateList.valueOf(Color.parseColor("#3158B8")));
        }
    }
}