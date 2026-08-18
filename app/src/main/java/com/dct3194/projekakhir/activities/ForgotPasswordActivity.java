package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etEmail;
    private Button btnSendReset;
    private TextView tvBackToLogin;

    // EmailJS Credentials
    private static final String SERVICE_ID = "service_jpih2pw";
    private static final String TEMPLATE_ID = "template_cf45pyf";
    private static final String PUBLIC_KEY = "Ms5x2z6btq1rVYk0O";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.etEmailReset);
        btnSendReset = findViewById(R.id.btnSendReset);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        btnSendReset.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();

            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                return;
            }

            String otp = generateOtp();
            sendOtpViaEmailJS(email, otp);
        });

        tvBackToLogin.setOnClickListener(v -> finish());
    }

    private String generateOtp() {
        Random random = new Random();
        int number = random.nextInt(99999);
        return String.format(Locale.getDefault(), "%05d", number);
    }

    private void sendOtpViaEmailJS(String email, String otp) {
        btnSendReset.setEnabled(false);
        Toast.makeText(this, "Sending OTP...", Toast.LENGTH_SHORT).show();

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();
        try {
            json.put("service_id", SERVICE_ID);
            json.put("template_id", TEMPLATE_ID);
            json.put("user_id", PUBLIC_KEY);

            JSONObject templateParams = new JSONObject();
            templateParams.put("to_email", email);
            templateParams.put("otp_code", otp);
            
            json.put("template_params", templateParams);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url("https://api.emailjs.com/api/v1.0/email/send")
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> {
                    btnSendReset.setEnabled(true);
                    Toast.makeText(ForgotPasswordActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                final String responseBody = response.body() != null ? response.body().string() : "No body";
                final int responseCode = response.code();
                
                runOnUiThread(() -> {
                    if (response.isSuccessful()) {
                        Toast.makeText(ForgotPasswordActivity.this, "OTP sent to " + email, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyOtpActivity.class);
                        intent.putExtra("email", email);
                        intent.putExtra("otp", otp);
                        startActivity(intent);
                    } else {
                        btnSendReset.setEnabled(true);
                        Log.e("EmailJS", "Error Code: " + responseCode + ", Message: " + responseBody);
                        Toast.makeText(ForgotPasswordActivity.this, "EmailJS Error: " + responseBody, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
