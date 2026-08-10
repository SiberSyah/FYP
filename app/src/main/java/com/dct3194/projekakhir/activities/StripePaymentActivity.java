package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.StripeConfig;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.model.ConfirmPaymentIntentParams;
import com.stripe.android.model.PaymentMethodCreateParams;
import com.stripe.android.payments.paymentlauncher.PaymentLauncher;
import com.stripe.android.payments.paymentlauncher.PaymentResult;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StripePaymentActivity extends AppCompatActivity {

    // ==========================
    // Views
    // ==========================

    private TextView txtAmount;
    private Button btnPay;

    private EditText etCardHolder;
    private EditText etCardNumber;
    private EditText etExpiry;
    private EditText etCvc;

    // ==========================
    // Payment
    // ==========================

    private int totalPrice;

    // ==========================
    // Stripe
    // ==========================

    private PaymentLauncher paymentLauncher;

    private static final String FUNCTION_URL =
            "https://us-central1-encikaircond.cloudfunctions.net/createPaymentIntent";

    // ==========================
    // ON CREATE
    // ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stripe_payment);

        txtAmount = findViewById(R.id.txtAmount);
        btnPay = findViewById(R.id.btnPay);

        etCardHolder = findViewById(R.id.etCardHolder);
        etCardNumber = findViewById(R.id.etCardNumber);
        etExpiry = findViewById(R.id.etExpiry);
        etCvc = findViewById(R.id.etCvc);

        totalPrice = getIntent().getIntExtra("totalPrice", 0);
        txtAmount.setText("Total : RM " + totalPrice);

        // Init Stripe
        PaymentConfiguration.init(
                getApplicationContext(),
                StripeConfig.PUBLISHABLE_KEY
        );

        PaymentConfiguration paymentConfiguration =
                PaymentConfiguration.getInstance(getApplicationContext());

        paymentLauncher = PaymentLauncher.Companion.create(
                this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                this::onPaymentResult
        );

        btnPay.setOnClickListener(v -> {

            if (totalPrice <= 0) {
                Toast.makeText(StripePaymentActivity.this, "Invalid payment amount", Toast.LENGTH_SHORT).show();
                return;
            }

            String cardNumber = etCardNumber.getText().toString().replaceAll("\\s+", "").trim();
            String expVal = etExpiry.getText().toString().trim();
            String cvc = etCvc.getText().toString().trim();

            if (cardNumber.isEmpty() || cardNumber.length() < 13) {
                Toast.makeText(StripePaymentActivity.this, "Please enter a valid card number", Toast.LENGTH_SHORT).show();
                return;
            }

            if (expVal.isEmpty() || !expVal.contains("/")) {
                Toast.makeText(StripePaymentActivity.this, "Please enter expiration date (MM/YY)", Toast.LENGTH_SHORT).show();
                return;
            }

            if (cvc.isEmpty() || cvc.length() < 3) {
                Toast.makeText(StripePaymentActivity.this, "Please enter a valid CVC", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] expParts = expVal.split("/");
            if (expParts.length != 2) {
                Toast.makeText(StripePaymentActivity.this, "Invalid expiration date format", Toast.LENGTH_SHORT).show();
                return;
            }

            int expMonth;
            int expYear;

            try {
                expMonth = Integer.parseInt(expParts[0].trim());
                String yearStr = expParts[1].trim();
                expYear = Integer.parseInt(yearStr.length() == 2 ? "20" + yearStr : yearStr);
            } catch (NumberFormatException e) {
                Toast.makeText(StripePaymentActivity.this, "Invalid expiration month or year", Toast.LENGTH_SHORT).show();
                return;
            }

            // Cipta parameter Stripe secara manual daripada EditText
            // ✅ KOD YANG BETUL (Hanya 1 line params):
            PaymentMethodCreateParams.Card cardParams = new PaymentMethodCreateParams.Card.Builder()
                    .setNumber(cardNumber)
                    .setExpiryMonth(expMonth)
                    .setExpiryYear(expYear)
                    .setCvc(cvc)
                    .build();

            PaymentMethodCreateParams params = PaymentMethodCreateParams.create(cardParams);

            createPaymentIntent(params);
        });
    }

    // =========================================================
    // CREATE PAYMENT INTENT
    // =========================================================

    private void createPaymentIntent(PaymentMethodCreateParams paymentMethodParams) {
        btnPay.setEnabled(false);
        btnPay.setText("Processing...");

        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();

        try {
            json.put("amount", totalPrice);
            json.put("currency", "myr");
        } catch (Exception e) {
            resetPayButton();
            Toast.makeText(StripePaymentActivity.this, "Unable to create payment request", Toast.LENGTH_LONG).show();
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(FUNCTION_URL)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    resetPayButton();
                    Toast.makeText(StripePaymentActivity.this, "Connection failed. Please try again.", Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (!response.isSuccessful()) {
                    runOnUiThread(() -> {
                        resetPayButton();
                        Toast.makeText(StripePaymentActivity.this, "Payment setup failed", Toast.LENGTH_LONG).show();
                    });
                    return;
                }

                runOnUiThread(() -> {
                    try {
                        JSONObject result = new JSONObject(responseBody);
                        boolean success = result.optBoolean("success", false);

                        if (!success) {
                            resetPayButton();
                            Toast.makeText(StripePaymentActivity.this, "Payment failed", Toast.LENGTH_LONG).show();
                            return;
                        }

                        String clientSecret = result.getString("clientSecret");
                        confirmPayment(clientSecret, paymentMethodParams);

                    } catch (Exception e) {
                        resetPayButton();
                        Toast.makeText(StripePaymentActivity.this, "Invalid payment response", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    // =========================================================
    // CONFIRM PAYMENT
    // =========================================================

    private void confirmPayment(String clientSecret, PaymentMethodCreateParams paymentMethodParams) {
        Toast.makeText(StripePaymentActivity.this, "Processing payment...", Toast.LENGTH_SHORT).show();

        ConfirmPaymentIntentParams confirmParams = ConfirmPaymentIntentParams
                .createWithPaymentMethodCreateParams(
                        paymentMethodParams,
                        clientSecret
                );

        paymentLauncher.confirm(confirmParams);
    }

    // =========================================================
    // STRIPE PAYMENT RESULT
    // =========================================================

    private void onPaymentResult(PaymentResult paymentResult) {
        if (paymentResult instanceof PaymentResult.Completed) {
            runOnUiThread(() -> {
                Toast.makeText(StripePaymentActivity.this, "Payment successful!", Toast.LENGTH_LONG).show();
                openPaymentSuccess();
            });
        } else if (paymentResult instanceof PaymentResult.Canceled) {
            runOnUiThread(() -> {
                resetPayButton();
                Toast.makeText(StripePaymentActivity.this, "Payment was cancelled.", Toast.LENGTH_SHORT).show();
            });
        } else if (paymentResult instanceof PaymentResult.Failed) {
            PaymentResult.Failed failed = (PaymentResult.Failed) paymentResult;
            runOnUiThread(() -> {
                resetPayButton();
                String message = failed.getThrowable() != null
                        ? failed.getThrowable().getMessage()
                        : "Payment failed. Please try again.";
                Toast.makeText(StripePaymentActivity.this, message, Toast.LENGTH_LONG).show();
            });
        }
    }

    private void resetPayButton() {
        btnPay.setEnabled(true);
        btnPay.setText("Pay Now");
    }

    private void openPaymentSuccess() {
        startActivity(new Intent(StripePaymentActivity.this, PaymentSuccessActivity.class));
        finish();
    }
}