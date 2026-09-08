package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;


public class VerifyOTPActivity extends AppCompatActivity {


    EditText otp1, otp2, otp3, otp4, otp5;

    MaterialButton btnVerify;

    TextView tvBackLogin;

    FirebaseFirestore db;

    String email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);



        otp1 = findViewById(R.id.otp1);
        otp2 = findViewById(R.id.otp2);
        otp3 = findViewById(R.id.otp3);
        otp4 = findViewById(R.id.otp4);
        otp5 = findViewById(R.id.otp5);


        btnVerify = findViewById(R.id.btnVerify);
        tvBackLogin = findViewById(R.id.tvBackLogin);



        db = FirebaseFirestore.getInstance();



        email = getIntent().getStringExtra("email");



        btnVerify.setOnClickListener(v -> {



            String otp =
                    otp1.getText().toString()
                            + otp2.getText().toString()
                            + otp3.getText().toString()
                            + otp4.getText().toString()
                            + otp5.getText().toString();



            if(TextUtils.isEmpty(otp) || otp.length() < 5){

                Toast.makeText(
                        this,
                        "Please enter OTP",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }



            db.collection("password_reset")
                    .document(email)
                    .get()

                    .addOnSuccessListener(documentSnapshot -> {


                        if(documentSnapshot.exists()){


                            String savedOTP =
                                    documentSnapshot.getString("otp");



                            if(savedOTP != null && savedOTP.equals(otp)){


                                Toast.makeText(
                                        this,
                                        "OTP Verified",
                                        Toast.LENGTH_SHORT
                                ).show();



                                Intent intent =
                                        new Intent(
                                                VerifyOTPActivity.this,
                                                ResetPasswordActivity.class
                                        );


                                intent.putExtra(
                                        "email",
                                        email
                                );


                                startActivity(intent);
                                finish();



                            }else{


                                Toast.makeText(
                                        this,
                                        "Wrong OTP",
                                        Toast.LENGTH_SHORT
                                ).show();


                            }



                        }else{


                            Toast.makeText(
                                    this,
                                    "OTP not found",
                                    Toast.LENGTH_SHORT
                            ).show();


                        }



                    })

                    .addOnFailureListener(e -> {


                        Toast.makeText(
                                this,
                                "Error checking OTP",
                                Toast.LENGTH_SHORT
                        ).show();


                    });



        });



        tvBackLogin.setOnClickListener(v -> {


            startActivity(
                    new Intent(
                            VerifyOTPActivity.this,
                            LoginActivity.class
                    )
            );


            finish();


        });


    }
}