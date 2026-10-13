package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.database.SessionManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private TextView tvAdminLogin;
    private TextView tvForgotPassword;

    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // =========================
        // Initialize Views
        // =========================
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);
        tvAdminLogin = findViewById(R.id.tvAdminLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        // =========================
        // Navigation Listeners
        // =========================
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvAdminLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, AdminLoginActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // =========================
        // Firebase
        // =========================
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // =========================
        // Login Button
        // =========================
        btnLogin.setOnClickListener(v -> {
            String usernameOrEmail = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (usernameOrEmail.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please fill in all fields",
                        Toast.LENGTH_SHORT
                ).show();
                return;
            }

            btnLogin.setEnabled(false);

            // =========================
            // Login Check
            // =========================
            if (usernameOrEmail.contains("@")) {
                // User entered email
                loginWithEmail(usernameOrEmail, password);
            } else {
                // User entered username
                findEmailByUsername(usernameOrEmail, password);
            }
        });
    }

    // =========================================================
    // LOGIN USING EMAIL
    // =========================================================
    private void loginWithEmail(String email, String password) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(authResult -> {
                    FirebaseUser firebaseUser = authResult.getUser();
                    if (firebaseUser != null) {
                        String uid = firebaseUser.getUid();
                        db.collection("users").document(uid).get()
                                .addOnSuccessListener(documentSnapshot -> {
                                    String username = documentSnapshot.getString("username");
                                    String userEmail = documentSnapshot.getString("email");
                                    String phone = documentSnapshot.getString("phone");

                                    if (userEmail == null || userEmail.isEmpty()) {
                                        userEmail = firebaseUser.getEmail();
                                    }
                                    if (username == null || username.isEmpty()) {
                                        username = firebaseUser.getDisplayName();
                                    }

                                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                    sessionManager.createLoginSession(
                                            uid,
                                            username != null ? username : "User",
                                            userEmail != null ? userEmail : email,
                                            phone != null ? phone : ""
                                    );

                                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    openMainActivity();
                                })
                                .addOnFailureListener(e -> {
                                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                                    sessionManager.createLoginSession(
                                            uid,
                                            "User",
                                            firebaseUser.getEmail() != null ? firebaseUser.getEmail() : email,
                                            ""
                                    );
                                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    openMainActivity();
                                });
                    } else {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                        openMainActivity();
                    }
                })
                .addOnFailureListener(e -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(
                            this,
                            "Invalid email or password",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // =========================================================
    // FIND EMAIL USING USERNAME
    // =========================================================
    private void findEmailByUsername(String username, String password) {
        db.collection("users")
                .whereEqualTo("username", username)
                .limit(1)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (querySnapshot.isEmpty()) {
                        btnLogin.setEnabled(true);
                        Toast.makeText(
                                this,
                                "Username not found",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    String email = querySnapshot
                            .getDocuments()
                            .get(0)
                            .getString("email");

                    if (email == null || email.trim().isEmpty()) {
                        btnLogin.setEnabled(true);
                        Toast.makeText(
                                this,
                                "User email not found",
                                Toast.LENGTH_LONG
                        ).show();
                        return;
                    }

                    loginWithEmail(email, password);
                })
                .addOnFailureListener(e -> {
                    btnLogin.setEnabled(true);
                    Toast.makeText(
                            this,
                            "Unable to login. Please try again.",
                            Toast.LENGTH_LONG
                    ).show();
                });
    }

    // =========================================================
    // OPEN MAIN ACTIVITY
    // =========================================================
    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
