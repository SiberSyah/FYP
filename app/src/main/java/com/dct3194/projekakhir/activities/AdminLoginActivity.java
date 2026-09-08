package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class AdminLoginActivity extends AppCompatActivity {


    private EditText etAdminEmail;
    private EditText etAdminPassword;
    private Button btnAdminLogin;

    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_login);


        etAdminEmail = findViewById(R.id.etAdminEmail);
        etAdminPassword = findViewById(R.id.etAdminPassword);
        btnAdminLogin = findViewById(R.id.btnAdminLogin);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();



        btnAdminLogin.setOnClickListener(v -> {


            String email =
                    etAdminEmail.getText()
                            .toString()
                            .trim();


            String password =
                    etAdminPassword.getText()
                            .toString();



            if(email.isEmpty() || password.isEmpty()){

                Toast.makeText(
                        this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }



            btnAdminLogin.setEnabled(false);



            auth.signInWithEmailAndPassword(
                            email,
                            password
                    )


                    .addOnSuccessListener(authResult -> {


                        String uid =
                                auth.getCurrentUser().getUid();



                        // Check admin collection

                        db.collection("admins")
                                .document(uid)
                                .get()


                                .addOnSuccessListener(documentSnapshot -> {


                                    if(documentSnapshot.exists()){


                                        Toast.makeText(
                                                this,
                                                "Admin Login Successful",
                                                Toast.LENGTH_SHORT
                                        ).show();



                                        Intent intent =
                                                new Intent(
                                                        AdminLoginActivity.this,
                                                        AdminDashboardActivity.class
                                                );


                                        startActivity(intent);
                                        finish();



                                    }else{


                                        auth.signOut();

                                        btnAdminLogin.setEnabled(true);


                                        Toast.makeText(
                                                this,
                                                "This account is not admin",
                                                Toast.LENGTH_LONG
                                        ).show();


                                    }


                                });



                    })


                    .addOnFailureListener(e -> {


                        btnAdminLogin.setEnabled(true);


                        Toast.makeText(
                                this,
                                "Invalid email or password",
                                Toast.LENGTH_LONG
                        ).show();



                    });


        });


    }

}