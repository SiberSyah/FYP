package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;

public class VerifyOtpActivity extends AppCompatActivity {

    private EditText etOtp1, etOtp2, etOtp3, etOtp4, etOtp5;
    private TextView tvSubtitle, tvResend, tvBackToLogin;
    private Button btnVerify;

    private String userEmail = "";
    private String generatedOtp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // Initialize Views
        etOtp1 = findViewById(R.id.etOtp1);
        etOtp2 = findViewById(R.id.etOtp2);
        etOtp3 = findViewById(R.id.etOtp3);
        etOtp4 = findViewById(R.id.etOtp4);
        etOtp5 = findViewById(R.id.etOtp5);

        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvResend = findViewById(R.id.tvResend);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);
        btnVerify = findViewById(R.id.btnVerify);

        // Get data from intent
        userEmail = getIntent().getStringExtra("email");
        generatedOtp = getIntent().getStringExtra("otp");

        if (userEmail != null) {
            tvSubtitle.setText("We sent a reset link to " + userEmail + "\nenter 5 digit code that mentioned in the email");
        }

        // Setup OTP input logic
        setupOtpInputs();

        // Verify Button
        btnVerify.setOnClickListener(v -> {
            String enteredOtp = etOtp1.getText().toString() +
                    etOtp2.getText().toString() +
                    etOtp3.getText().toString() +
                    etOtp4.getText().toString() +
                    etOtp5.getText().toString();

            if (enteredOtp.length() < 5) {
                Toast.makeText(this, "Please enter 5 digit code", Toast.LENGTH_SHORT).show();
                return;
            }

            if (enteredOtp.equals(generatedOtp)) {
                // OTP Correct -> Go to Reset Password Screen
                Intent intent = new Intent(this, ResetPasswordActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Invalid OTP code. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

        // Resend logic (optional, user can go back)
        tvResend.setOnClickListener(v -> {
            finish(); // Go back to ForgotPassword to resend
        });

        tvBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finishAffinity();
        });
    }

    private void setupOtpInputs() {
        etOtp1.addTextChangedListener(new OtpTextWatcher(etOtp1, etOtp2));
        etOtp2.addTextChangedListener(new OtpTextWatcher(etOtp2, etOtp3));
        etOtp3.addTextChangedListener(new OtpTextWatcher(etOtp3, etOtp4));
        etOtp4.addTextChangedListener(new OtpTextWatcher(etOtp4, etOtp5));
        etOtp5.addTextChangedListener(new OtpTextWatcher(etOtp5, null));
    }

    private class OtpTextWatcher implements TextWatcher {
        private EditText currentView, nextView;

        public OtpTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 1 && nextView != null) {
                nextView.requestFocus();
            }
        }
    }
}