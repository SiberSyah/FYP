package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
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
import com.stripe.android.view.CardInputWidget;

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

    private TextView txtAmount;
    private Button btnPay;
    private CardInputWidget cardInputWidget;

    private int totalPrice;

    private PaymentLauncher paymentLauncher;

    private static final String FUNCTION_URL =
            "https://us-central1-encikaircond.cloudfunctions.net/createPaymentIntent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stripe_payment);

        // ==========================
        // Initialize Views
        // ==========================

        txtAmount = findViewById(R.id.txtAmount);
        btnPay = findViewById(R.id.btnPay);
        cardInputWidget = findViewById(R.id.cardInputWidget);

        // ==========================
        // Get Total Price
        // ==========================

        totalPrice = getIntent().getIntExtra("totalPrice", 0);

        txtAmount.setText("Total : RM " + totalPrice);

        // ==========================
        // Initialize Stripe
        // ==========================

        PaymentConfiguration.init(
                getApplicationContext(),
                StripeConfig.PUBLISHABLE_KEY
        );

        PaymentConfiguration paymentConfiguration =
                PaymentConfiguration.getInstance(
                        getApplicationContext()
                );

        paymentLauncher = PaymentLauncher.Companion.create(
                this,
                paymentConfiguration.getPublishableKey(),
                paymentConfiguration.getStripeAccountId(),
                this::onPaymentResult
        );

        // ==========================
        // Pay Button
        // ==========================

        btnPay.setOnClickListener(v -> {

            if (totalPrice <= 0) {

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Invalid payment amount",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            PaymentMethodCreateParams paymentMethodParams =
                    cardInputWidget.getPaymentMethodCreateParams();

            if (paymentMethodParams == null) {

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Please enter valid card details",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            createPaymentIntent(paymentMethodParams);
        });
    }

    // =========================================================
    // CREATE PAYMENT INTENT
    // =========================================================

    private void createPaymentIntent(
            PaymentMethodCreateParams paymentMethodParams) {

        btnPay.setEnabled(false);
        btnPay.setText("Processing...");

        OkHttpClient client = new OkHttpClient();

        JSONObject json = new JSONObject();

        try {

            json.put("amount", totalPrice);
            json.put("currency", "myr");

        } catch (Exception e) {

            resetPayButton();

            Toast.makeText(
                    StripePaymentActivity.this,
                    "Unable to create payment request",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json")
        );

        Request request = new Request.Builder()
                .url(FUNCTION_URL)
                .post(body)
                .addHeader(
                        "Content-Type",
                        "application/json"
                )
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(
                    Call call,
                    IOException e) {

                runOnUiThread(() -> {

                    resetPayButton();

                    Toast.makeText(
                            StripePaymentActivity.this,
                            "Connection failed. Please try again.",
                            Toast.LENGTH_LONG
                    ).show();
                });
            }

            @Override
            public void onResponse(
                    Call call,
                    Response response)
                    throws IOException {

                String responseBody =
                        response.body() != null
                                ? response.body().string()
                                : "";

                if (!response.isSuccessful()) {

                    runOnUiThread(() -> {

                        resetPayButton();

                        try {

                            JSONObject errorJson =
                                    new JSONObject(responseBody);

                            String errorMessage =
                                    errorJson.optString(
                                            "error",
                                            "Payment setup failed"
                                    );

                            Toast.makeText(
                                    StripePaymentActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();

                        } catch (Exception e) {

                            Toast.makeText(
                                    StripePaymentActivity.this,
                                    "Payment setup failed",
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                    });

                    return;
                }

                runOnUiThread(() -> {

                    try {

                        JSONObject result =
                                new JSONObject(responseBody);

                        boolean success =
                                result.optBoolean(
                                        "success",
                                        false
                                );

                        if (!success) {

                            resetPayButton();

                            String errorMessage =
                                    result.optString(
                                            "error",
                                            "Payment setup failed"
                                    );

                            Toast.makeText(
                                    StripePaymentActivity.this,
                                    errorMessage,
                                    Toast.LENGTH_LONG
                            ).show();

                            return;
                        }

                        String clientSecret =
                                result.getString(
                                        "clientSecret"
                                );

                        confirmPayment(
                                clientSecret,
                                paymentMethodParams
                        );

                    } catch (Exception e) {

                        resetPayButton();

                        Toast.makeText(
                                StripePaymentActivity.this,
                                "Invalid payment response",
                                Toast.LENGTH_LONG
                        ).show();
                    }
                });
            }
        });
    }

    // =========================================================
    // CONFIRM PAYMENT
    // =========================================================

    private void confirmPayment(
            String clientSecret,
            PaymentMethodCreateParams paymentMethodParams) {

        Toast.makeText(
                StripePaymentActivity.this,
                "Processing payment...",
                Toast.LENGTH_SHORT
        ).show();

        ConfirmPaymentIntentParams confirmParams =
                ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(
                                paymentMethodParams,
                                clientSecret
                        );

        paymentLauncher.confirm(confirmParams);
    }

    // =========================================================
    // STRIPE PAYMENT RESULT
    // =========================================================

    private void onPaymentResult(
            PaymentResult paymentResult) {

        if (paymentResult instanceof PaymentResult.Completed) {

            runOnUiThread(() -> {

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Payment successful!",
                        Toast.LENGTH_LONG
                ).show();

                openPaymentSuccess();
            });

        } else if (paymentResult instanceof PaymentResult.Canceled) {

            runOnUiThread(() -> {

                resetPayButton();

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Payment was cancelled.",
                        Toast.LENGTH_SHORT
                ).show();
            });

        } else if (paymentResult instanceof PaymentResult.Failed) {

            PaymentResult.Failed failed =
                    (PaymentResult.Failed) paymentResult;

            runOnUiThread(() -> {

                resetPayButton();

                String message =
                        failed.getThrowable() != null
                                ? failed.getThrowable().getMessage()
                                : null;

                if (message == null ||
                        message.trim().isEmpty()) {

                    message =
                            "Payment failed. Please try again.";
                }

                Toast.makeText(
                        StripePaymentActivity.this,
                        message,
                        Toast.LENGTH_LONG
                ).show();
            });
        }
    }

    // =========================================================
    // PAYMENT SUCCESS
    // =========================================================

    private void openPaymentSuccess() {

        Intent previousIntent = getIntent();

        Intent intent = new Intent(
                StripePaymentActivity.this,
                PaymentSuccessActivity.class
        );

        // ==========================
        // Booking Data
        // ==========================

        intent.putExtra(
                "aircond",
                previousIntent.getSerializableExtra("aircond")
        );

        intent.putExtra(
                "model",
                previousIntent.getStringExtra("model")
        );

        intent.putExtra(
                "hp",
                previousIntent.getStringExtra("hp")
        );

        intent.putExtra(
                "package",
                previousIntent.getStringExtra("package")
        );

        intent.putExtra(
                "price",
                previousIntent.getIntExtra("price", 0)
        );

        intent.putExtra(
                "totalPrice",
                totalPrice
        );

        intent.putExtra(
                "installation",
                previousIntent.getBooleanExtra(
                        "installation",
                        false
                )
        );

        intent.putExtra(
                "date",
                previousIntent.getStringExtra("date")
        );

        intent.putExtra(
                "time",
                previousIntent.getStringExtra("time")
        );

        // ==========================
        // Customer Information
        // ==========================

        intent.putExtra(
                "fullName",
                previousIntent.getStringExtra("fullName")
        );

        intent.putExtra(
                "phone",
                previousIntent.getStringExtra("phone")
        );

        intent.putExtra(
                "address",
                previousIntent.getStringExtra("address")
        );

        intent.putExtra(
                "city",
                previousIntent.getStringExtra("city")
        );

        intent.putExtra(
                "postcode",
                previousIntent.getStringExtra("postcode")
        );

        intent.putExtra(
                "state",
                previousIntent.getStringExtra("state")
        );

        // ==========================
        // Payment
        // ==========================

        intent.putExtra(
                "paymentMethod",
                "Stripe"
        );

        startActivity(intent);

        finish();
    }

    // =========================================================
    // RESET BUTTON
    // =========================================================

    private void resetPayButton() {

        runOnUiThread(() -> {

            btnPay.setEnabled(true);
            btnPay.setText("Pay Now");
        });
    }
}