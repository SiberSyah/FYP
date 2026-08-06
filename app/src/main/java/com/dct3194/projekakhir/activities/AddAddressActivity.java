package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;

public class AddAddressActivity extends AppCompatActivity {

    private EditText etFullName, etPhone, etAddress, etCity, etPoscode, etState;
    private Button btnContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        etCity = findViewById(R.id.etCity);
        etPoscode = findViewById(R.id.etPoscode);
        etState = findViewById(R.id.etState);
        btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            if (validateForm()) {
                Intent intent = new Intent(this, PaymentMethodActivity.class);
                // Pass data if needed
                startActivity(intent);
            }
        });

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "home");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navOrders).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "orders");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private boolean validateForm() {
        if (etFullName.getText().toString().isEmpty() ||
            etPhone.getText().toString().isEmpty() ||
            etAddress.getText().toString().isEmpty() ||
            etCity.getText().toString().isEmpty() ||
            etPoscode.getText().toString().isEmpty() ||
            etState.getText().toString().isEmpty()) {
            
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
