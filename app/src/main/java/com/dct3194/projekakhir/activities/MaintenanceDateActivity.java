package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;

import java.util.Calendar;
import java.util.Locale;

public class MaintenanceDateActivity extends AppCompatActivity {

    private ImageView btnBack, imgSummaryAircond;
    private CalendarView calendarView;
    private Button btn8AM, btn9AM, btn11AM, btn1PM, btn2PM, btn4PM;
    private Button btnContinue;

    // Bottom Navigation Views
    private ImageView btnNavProfile;
    private ImageView btnNavHome;
    private ImageView btnNavOrders;

    private TextView txtSummaryName, txtSummaryBrand, txtSummaryHP, txtSummaryInstall;

    private String hp = "";
    private String type = "";
    private String service = "";
    private String price = "";

    private String selectedDate = "";
    private String selectedTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_date);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        calendarView = findViewById(R.id.calendarView);
        imgSummaryAircond = findViewById(R.id.imgSummaryAircond);

        btn8AM = findViewById(R.id.btn8AM);
        btn9AM = findViewById(R.id.btn9AM);
        btn11AM = findViewById(R.id.btn11AM);
        btn1PM = findViewById(R.id.btn1PM);
        btn2PM = findViewById(R.id.btn2PM);
        btn4PM = findViewById(R.id.btn4PM);

        btnContinue = findViewById(R.id.btnContinue);

        // Initialize Bottom Navigation Views
        btnNavProfile = findViewById(R.id.btnNavProfile);
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavOrders = findViewById(R.id.btnNavOrders);

        txtSummaryName = findViewById(R.id.txtSummaryName);
        txtSummaryBrand = findViewById(R.id.txtSummaryBrand);
        txtSummaryHP = findViewById(R.id.txtSummaryHP);
        txtSummaryInstall = findViewById(R.id.txtSummaryInstall);

        // Set Home icon active by default
        selectHome();

        // Set Default Image
        imgSummaryAircond.setImageResource(R.drawable.ic_maintenance);

        // Get Data from MaintenanceActivity
        Intent intent = getIntent();
        if (intent != null) {
            hp = intent.getStringExtra("hp");
            type = intent.getStringExtra("type");
            service = intent.getStringExtra("service");
            price = intent.getStringExtra("price");

            txtSummaryName.setText(service);
            txtSummaryBrand.setText("Type : " + type);
            txtSummaryHP.setText("HP : " + hp);
            txtSummaryInstall.setText("Price : " + price);
        }

        // Default Date (Today)
        Calendar calendar = Calendar.getInstance();
        selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d",
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.YEAR));

        // Calendar Listener
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, month + 1, year);
        });

        // Time Buttons Listeners
        btn8AM.setOnClickListener(v -> selectTime("8:00 AM", btn8AM));
        btn9AM.setOnClickListener(v -> selectTime("9:00 AM", btn9AM));
        btn11AM.setOnClickListener(v -> selectTime("11:00 AM", btn11AM));
        btn1PM.setOnClickListener(v -> selectTime("1:00 PM", btn1PM));
        btn2PM.setOnClickListener(v -> selectTime("2:00 PM", btn2PM));
        btn4PM.setOnClickListener(v -> selectTime("4:00 PM", btn4PM));

        // Back Button
        btnBack.setOnClickListener(v -> finish());

        // Continue Button
        btnContinue.setOnClickListener(v -> {
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a time slot!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent nextIntent = new Intent(
                    this,
                    AddAddressActivity.class
            );

            nextIntent.putExtra(
                    "maintenance",
                    true
            );

            nextIntent.putExtra(
                    "hp",
                    hp
            );

            nextIntent.putExtra(
                    "type",
                    type
            );

            nextIntent.putExtra(
                    "service",
                    service
            );

            nextIntent.putExtra(
                    "price",
                    price
            );

            nextIntent.putExtra(
                    "date",
                    selectedDate
            );

            nextIntent.putExtra(
                    "time",
                    selectedTime
            );

            startActivity(nextIntent);
        });

        // Bottom Navigation Listeners
        if (btnNavHome != null) {
            btnNavHome.setOnClickListener(v -> {
                selectHome();
                Intent navIntent = new Intent(this, MainActivity.class);
                navIntent.putExtra("page", "home");
                navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(navIntent);
                finish();
            });
        }

        if (btnNavProfile != null) {
            btnNavProfile.setOnClickListener(v -> {
                selectProfile();
                Intent navIntent = new Intent(this, MainActivity.class);
                navIntent.putExtra("page", "profile");
                navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(navIntent);
                finish();
            });
        }

        if (btnNavOrders != null) {
            btnNavOrders.setOnClickListener(v -> {
                selectOrders();
                Intent navIntent = new Intent(this, MainActivity.class);
                navIntent.putExtra("page", "orders");
                navIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(navIntent);
                finish();
            });
        }
    }

    private void selectTime(String time, Button selectedBtn) {
        selectedTime = time;

        Button[] buttons = {btn8AM, btn9AM, btn11AM, btn1PM, btn2PM, btn4PM};
        for (Button btn : buttons) {
            btn.setBackgroundResource(R.drawable.button_outline);
            btn.setTextColor(Color.parseColor("#3158B8"));
        }

        selectedBtn.setBackgroundResource(R.drawable.button_blue);
        selectedBtn.setTextColor(Color.WHITE);
    }

    // =========================================================
    // BOTTOM NAVIGATION ICON COLOR LOGIC
    // =========================================================

    private void resetIcons() {
        if (btnNavProfile != null) {
            btnNavProfile.setColorFilter(Color.GRAY);
        }
        if (btnNavHome != null) {
            btnNavHome.setColorFilter(Color.GRAY);
        }
        if (btnNavOrders != null) {
            btnNavOrders.setColorFilter(Color.GRAY);
        }
    }

    private void selectHome() {
        resetIcons();
        if (btnNavHome != null) {
            btnNavHome.setColorFilter(Color.parseColor("#3158B8"));
        }
    }

    private void selectProfile() {
        resetIcons();
        if (btnNavProfile != null) {
            btnNavProfile.setColorFilter(Color.parseColor("#3158B8"));
        }
    }

    private void selectOrders() {
        resetIcons();
        if (btnNavOrders != null) {
            btnNavOrders.setColorFilter(Color.parseColor("#3158B8"));
        }
    }
}