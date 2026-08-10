package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etPhone;

    private Button btnRegister;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // =========================
        // Initialize Views
        // =========================

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        etPhone = findViewById(R.id.etPhone);

        btnRegister = findViewById(R.id.btnRegister);

        // =========================
        // Firebase
        // =========================

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // =========================
        // Register Button
        // =========================

        btnRegister.setOnClickListener(v -> {

            if (validateFields()) {
                registerUser();
            }
        });
    }

    // =========================================================
    // VALIDATE
    // =========================================================

    private boolean validateFields() {

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString();

        String confirmPassword =
                etConfirmPassword.getText()
                        .toString();

        String phone =
                etPhone.getText()
                        .toString()
                        .trim();

        if (username.isEmpty()
                || email.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()
                || phone.isEmpty()) {

            Toast.makeText(
                    this,
                    "Please fill in all fields",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(email)
                .matches()) {

            Toast.makeText(
                    this,
                    "Please enter a valid email",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (password.length() < 6) {

            Toast.makeText(
                    this,
                    "Password must be at least 6 characters",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        if (!password.equals(confirmPassword)) {

            Toast.makeText(
                    this,
                    "Passwords do not match",
                    Toast.LENGTH_SHORT
            ).show();

            return false;
        }

        return true;
    }

    // =========================================================
    // REGISTER USER
    // =========================================================

    private void registerUser() {

        String username =
                etUsername.getText()
                        .toString()
                        .trim();

        String email =
                etEmail.getText()
                        .toString()
                        .trim();

        String password =
                etPassword.getText()
                        .toString();

        String phone =
                etPhone.getText()
                        .toString()
                        .trim();

        btnRegister.setEnabled(false);

        // =========================
        // Firebase Authentication
        // =========================

        auth.createUserWithEmailAndPassword(
                        email,
                        password
                )

                .addOnSuccessListener(
                        authResult -> {

                            FirebaseUser user =
                                    authResult.getUser();

                            if (user == null) {

                                btnRegister.setEnabled(true);

                                Toast.makeText(
                                        this,
                                        "Registration failed",
                                        Toast.LENGTH_SHORT
                                ).show();

                                return;
                            }

                            String userId =
                                    user.getUid();

                            // =========================
                            // Save User Profile
                            // =========================

                            Map<String, Object> userData =
                                    new HashMap<>();

                            userData.put(
                                    "userId",
                                    userId
                            );

                            userData.put(
                                    "username",
                                    username
                            );

                            userData.put(
                                    "email",
                                    email
                            );

                            userData.put(
                                    "phone",
                                    phone
                            );

                            userData.put(
                                    "createdAt",
                                    System.currentTimeMillis()
                            );

                            db.collection("users")
                                    .document(userId)
                                    .set(userData)

                                    .addOnSuccessListener(
                                            unused -> {

                                                Toast.makeText(
                                                        this,
                                                        "Registration Successful. Please login.",
                                                        Toast.LENGTH_SHORT
                                                ).show();

                                                // =========================
                                                // GO TO LOGIN
                                                // =========================

                                                Intent intent =
                                                        new Intent(
                                                                RegisterActivity.this,
                                                                LoginActivity.class
                                                        );

                                                intent.setFlags(
                                                        Intent.FLAG_ACTIVITY_CLEAR_TOP |
                                                                Intent.FLAG_ACTIVITY_NEW_TASK
                                                );

                                                startActivity(intent);

                                                finish();
                                            }
                                    )

                                    .addOnFailureListener(
                                            e -> {

                                                btnRegister.setEnabled(true);

                                                Toast.makeText(
                                                        this,
                                                        "Account created but profile save failed",
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                    );
                        }
                )

                .addOnFailureListener(
                        e -> {

                            btnRegister.setEnabled(true);

                            Toast.makeText(
                                    this,
                                    e.getMessage(),
                                    Toast.LENGTH_LONG
                            ).show();
                        }
                );
    }
}