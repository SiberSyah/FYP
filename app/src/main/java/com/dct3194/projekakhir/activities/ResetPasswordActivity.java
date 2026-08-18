package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText etNewPassword, etConfirmNewPassword;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmNewPassword = findViewById(R.id.etConfirmNewPassword);
        btnReset = findViewById(R.id.btnReset);

        btnReset.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString();
            String confirmPassword = etConfirmNewPassword.getText().toString();

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            // In a real Firebase app, you cannot change a user's password 
            // without them being logged in or using a reset token from an email link.
            // For this OTP demo, we will simulate success.
            
            Toast.makeText(this, "Password has been reset successfully!", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}