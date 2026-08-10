package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.graphics.Color;
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
    // Payment Views
    // ==========================

    private TextView txtAmount;
    private Button btnPay;

    private EditText etCardHolder;
    private EditText etCardNumber;
    private EditText etExpiry;
    private EditText etCvc;

    // ==========================
    // Payment Data
    // ==========================

    private int totalPrice;

    private String paymentIntentId;

    // ==========================
    // Stripe
    // ==========================

    private PaymentLauncher paymentLauncher;

    private static final String FUNCTION_URL =
            "https://us-central1-encikaircond.cloudfunctions.net/createPaymentIntent";

    // ==========================
    // Colors
    // ==========================

    private static final int COLOR_TEXT =
            Color.parseColor("#17213A");

    private static final int COLOR_HINT =
            Color.parseColor("#7A8496");

    // ==========================
    // ON CREATE
    // ==========================

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_stripe_payment
        );

        // ==========================
        // Initialize Views
        // ==========================

        txtAmount =
                findViewById(R.id.txtAmount);

        btnPay =
                findViewById(R.id.btnPay);

        etCardHolder =
                findViewById(R.id.etCardHolder);

        etCardNumber =
                findViewById(R.id.etCardNumber);

        etExpiry =
                findViewById(R.id.etExpiry);

        etCvc =
                findViewById(R.id.etCvc);

        // ==========================
        // Get Total Price
        // ==========================

        totalPrice =
                getIntent().getIntExtra(
                        "totalPrice",
                        0
                );

        txtAmount.setText(
                "Total : RM " + totalPrice
        );

        // ==========================
        // Input Styling
        // ==========================

        applyInputStyling();

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

        paymentLauncher =
                PaymentLauncher.Companion.create(
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
                    createCardPaymentMethod();

            if (paymentMethodParams == null) {
                return;
            }

            createPaymentIntent(
                    paymentMethodParams
            );
        });
    }

    // =========================================================
    // INPUT STYLING
    // =========================================================

    private void applyInputStyling() {

        EditText[] fields = {
                etCardHolder,
                etCardNumber,
                etExpiry,
                etCvc
        };

        for (EditText editText : fields) {

            if (editText == null) {
                continue;
            }

            editText.setTextColor(
                    COLOR_TEXT
            );

            editText.setHintTextColor(
                    COLOR_HINT
            );

            editText.setAlpha(1.0f);
        }
    }

    // =========================================================
    // CREATE CARD PAYMENT METHOD
    // =========================================================

    private PaymentMethodCreateParams createCardPaymentMethod() {

        String cardHolder =
                etCardHolder.getText()
                        .toString()
                        .trim();

        String cardNumber =
                etCardNumber.getText()
                        .toString()
                        .replace(" ", "")
                        .trim();

        String expiry =
                etExpiry.getText()
                        .toString()
                        .trim();

        String cvc =
                etCvc.getText()
                        .toString()
                        .trim();

        // ==========================
        // Validate Cardholder
        // ==========================

        if (cardHolder.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter cardholder name",
                    Toast.LENGTH_SHORT
            ).show();

            etCardHolder.requestFocus();

            return null;
        }

        // ==========================
        // Validate Card Number
        // ==========================

        if (cardNumber.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter card number",
                    Toast.LENGTH_SHORT
            ).show();

            etCardNumber.requestFocus();

            return null;
        }

        if (cardNumber.length() < 13 ||
                cardNumber.length() > 19) {

            Toast.makeText(
                    this,
                    "Please enter a valid card number",
                    Toast.LENGTH_SHORT
            ).show();

            etCardNumber.requestFocus();

            return null;
        }

        // ==========================
        // Validate Expiry
        // ==========================

        if (expiry.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter expiry date",
                    Toast.LENGTH_SHORT
            ).show();

            etExpiry.requestFocus();

            return null;
        }

        /*
         * Accept both formats:
         *
         * 12/28
         * 1228
         *
         * If user enters 1228,
         * convert internally to 12/28.
         */

        expiry = expiry.replace(" ", "");

        if (expiry.matches("\\d{4}")) {

            expiry =
                    expiry.substring(0, 2)
                            + "/"
                            + expiry.substring(2, 4);
        }

        if (!expiry.matches("\\d{2}/\\d{2}")) {

            Toast.makeText(
                    this,
                    "Expiry date must be MM/YY",
                    Toast.LENGTH_SHORT
            ).show();

            etExpiry.requestFocus();

            return null;
        }

        // ==========================
        // Parse Expiry
        // ==========================

        String[] expiryParts =
                expiry.split("/");

        int expiryMonth;
        int expiryYear;

        try {

            expiryMonth =
                    Integer.parseInt(
                            expiryParts[0]
                    );

            expiryYear =
                    Integer.parseInt(
                            expiryParts[1]
                    );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Invalid expiry date",
                    Toast.LENGTH_SHORT
            ).show();

            etExpiry.requestFocus();

            return null;
        }

        if (expiryMonth < 1 ||
                expiryMonth > 12) {

            Toast.makeText(
                    this,
                    "Invalid expiry month",
                    Toast.LENGTH_SHORT
            ).show();

            etExpiry.requestFocus();

            return null;
        }

        // Convert YY → YYYY

        expiryYear = 2000 + expiryYear;

        // ==========================
        // Validate CVC
        // ==========================

        if (cvc.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please enter CVC",
                    Toast.LENGTH_SHORT
            ).show();

            etCvc.requestFocus();

            return null;
        }

        if (cvc.length() < 3 ||
                cvc.length() > 4) {

            Toast.makeText(
                    this,
                    "Please enter a valid CVC",
                    Toast.LENGTH_SHORT
            ).show();

            etCvc.requestFocus();

            return null;
        }

        // ==========================
        // Create Stripe Card Params
        // ==========================

        try {

            PaymentMethodCreateParams.Card card =
                    new PaymentMethodCreateParams.Card.Builder()
                            .setNumber(cardNumber)
                            .setExpiryMonth(expiryMonth)
                            .setExpiryYear(expiryYear)
                            .setCvc(cvc)
                            .build();

            return PaymentMethodCreateParams.create(
                    card
            );

        } catch (Exception e) {

            Toast.makeText(
                    this,
                    "Invalid card details",
                    Toast.LENGTH_SHORT
            ).show();

            return null;
        }
    }

    // =========================================================
    // CREATE PAYMENT INTENT
    // =========================================================

    private void createPaymentIntent(
            PaymentMethodCreateParams paymentMethodParams) {

        btnPay.setEnabled(false);

        btnPay.setText(
                "Processing..."
        );

        OkHttpClient client =
                new OkHttpClient();

        JSONObject json =
                new JSONObject();

        try {

            /*
             * IMPORTANT:
             *
             * Android sends the amount in RM.
             *
             * Example:
             *
             * RM 10
             * ↓
             * amount = 10
             *
             * Firebase Function converts:
             *
             * 10 × 100
             * ↓
             * 1000 sen
             *
             * Therefore DO NOT multiply by 100 here.
             */

            json.put(
                    "amount",
                    totalPrice
            );

            json.put(
                    "currency",
                    "myr"
            );

        } catch (Exception e) {

            resetPayButton();

            Toast.makeText(
                    StripePaymentActivity.this,
                    "Unable to create payment request",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        // ==========================
        // Request Body
        // ==========================

        RequestBody body =
                RequestBody.create(
                        json.toString(),
                        MediaType.parse(
                                "application/json"
                        )
                );

        // ==========================
        // Firebase Function Request
        // ==========================

        Request request =
                new Request.Builder()
                        .url(FUNCTION_URL)
                        .post(body)
                        .addHeader(
                                "Content-Type",
                                "application/json"
                        )
                        .build();

        // ==========================
        // Send Request
        // ==========================

        client.newCall(request)
                .enqueue(
                        new Callback() {

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

                                // ==========================
                                // HTTP Error
                                // ==========================

                                if (!response.isSuccessful()) {

                                    runOnUiThread(() -> {

                                        resetPayButton();

                                        try {

                                            JSONObject errorJson =
                                                    new JSONObject(
                                                            responseBody
                                                    );

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

                                // ==========================
                                // Firebase Response
                                // ==========================

                                runOnUiThread(() -> {

                                    try {

                                        JSONObject result =
                                                new JSONObject(
                                                        responseBody
                                                );

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

                                        // ==========================
                                        // Client Secret
                                        // ==========================

                                        String clientSecret =
                                                result.getString(
                                                        "clientSecret"
                                                );

                                        // ==========================
                                        // Payment Intent ID
                                        // ==========================

                                        paymentIntentId =
                                                result.optString(
                                                        "paymentIntentId",
                                                        ""
                                                );

                                        // ==========================
                                        // Confirm Payment
                                        // ==========================

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
                        }
                );
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

        ConfirmPaymentIntentParams
                confirmParams =
                ConfirmPaymentIntentParams
                        .createWithPaymentMethodCreateParams(
                                paymentMethodParams,
                                clientSecret
                        );

        paymentLauncher.confirm(
                confirmParams
        );
    }

    // =========================================================
    // STRIPE PAYMENT RESULT
    // =========================================================

    private void onPaymentResult(
            PaymentResult paymentResult) {

        // ==========================
        // SUCCESS
        // ==========================

        if (paymentResult
                instanceof PaymentResult.Completed) {

            runOnUiThread(() -> {

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Payment successful!",
                        Toast.LENGTH_LONG
                ).show();

                openPaymentSuccess();
            });
        }

        // ==========================
        // CANCELLED
        // ==========================

        else if (
                paymentResult
                        instanceof PaymentResult.Canceled
        ) {

            runOnUiThread(() -> {

                resetPayButton();

                Toast.makeText(
                        StripePaymentActivity.this,
                        "Payment was cancelled.",
                        Toast.LENGTH_SHORT
                ).show();
            });
        }

        // ==========================
        // FAILED
        // ==========================

        else if (
                paymentResult
                        instanceof PaymentResult.Failed
        ) {

            PaymentResult.Failed failed =
                    (PaymentResult.Failed)
                            paymentResult;

            runOnUiThread(() -> {

                resetPayButton();

                String message =
                        failed.getThrowable() != null
                                ? failed.getThrowable()
                                .getMessage()
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

        Intent previousIntent =
                getIntent();

        Intent intent =
                new Intent(
                        StripePaymentActivity.this,
                        PaymentSuccessActivity.class
                );

        // ==========================
        // Aircond
        // ==========================

        intent.putExtra(
                "aircond",
                previousIntent.getSerializableExtra(
                        "aircond"
                )
        );

        // ==========================
        // Model
        // ==========================

        intent.putExtra(
                "model",
                previousIntent.getStringExtra(
                        "model"
                )
        );

        // ==========================
        // HP
        // ==========================

        intent.putExtra(
                "hp",
                previousIntent.getStringExtra(
                        "hp"
                )
        );

        // ==========================
        // Type
        // ==========================

        intent.putExtra(
                "type",
                previousIntent.getStringExtra(
                        "type"
                )
        );

        // ==========================
        // Package
        // ==========================

        intent.putExtra(
                "package",
                previousIntent.getStringExtra(
                        "package"
                )
        );

        // ==========================
        // Price
        // ==========================

        intent.putExtra(
                "price",
                previousIntent.getIntExtra(
                        "price",
                        0
                )
        );

        intent.putExtra(
                "totalPrice",
                totalPrice
        );

        // ==========================
        // Installation
        // ==========================

        intent.putExtra(
                "installation",
                previousIntent.getBooleanExtra(
                        "installation",
                        false
                )
        );

        // ==========================
        // Booking Date
        // ==========================

        intent.putExtra(
                "date",
                previousIntent.getStringExtra(
                        "date"
                )
        );

        // ==========================
        // Booking Time
        // ==========================

        intent.putExtra(
                "time",
                previousIntent.getStringExtra(
                        "time"
                )
        );

        // ==========================
        // Customer
        // ==========================

        String customerName =
                previousIntent.getStringExtra(
                        "customerName"
                );

        String customerPhone =
                previousIntent.getStringExtra(
                        "customerPhone"
                );

        String customerAddress =
                previousIntent.getStringExtra(
                        "customerAddress"
                );

        intent.putExtra(
                "customerName",
                customerName
        );

        intent.putExtra(
                "customerPhone",
                customerPhone
        );

        intent.putExtra(
                "customerAddress",
                customerAddress
        );

        // ==========================
        // Legacy Customer Keys
        // ==========================

        intent.putExtra(
                "fullName",
                customerName
        );

        intent.putExtra(
                "phone",
                customerPhone
        );

        intent.putExtra(
                "address",
                customerAddress
        );

        // ==========================
        // Payment Method
        // ==========================

        intent.putExtra(
                "paymentMethod",
                "Stripe"
        );

        // ==========================
        // Payment Intent ID
        // ==========================

        intent.putExtra(
                "paymentIntentId",
                paymentIntentId
        );

        // ==========================
        // Payment Status
        // ==========================

        intent.putExtra(
                "paymentStatus",
                "successful"
        );

        // ==========================
        // Open Success Page
        // ==========================

        startActivity(intent);

        finish();
    }

    // =========================================================
    // RESET PAY BUTTON
    // =========================================================

    private void resetPayButton() {

        runOnUiThread(() -> {

            btnPay.setEnabled(true);

            btnPay.setText(
                    "Pay Now"
            );
        });
    }
}