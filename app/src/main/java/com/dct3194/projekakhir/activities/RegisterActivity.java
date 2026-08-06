package com.dct3194.projekakhir.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword, etPhone;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> {
            if (validateFields()) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private boolean validateFields() {
        if (etUsername.getText().toString().isEmpty() ||
            etEmail.getText().toString().isEmpty() ||
            etPassword.getText().toString().isEmpty() ||
            etConfirmPassword.getText().toString().isEmpty() ||
            etPhone.getText().toString().isEmpty()) {
            
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return false;
        }
        
        return true;
    }
}
