package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;

public class WelcomeActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;
    private TextView tvAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        tvAdmin = findViewById(R.id.tvAdmin);

        // Login
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Register
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Admin Login
        tvAdmin.setOnClickListener(v -> {
            Intent intent = new Intent(WelcomeActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });
    }
}