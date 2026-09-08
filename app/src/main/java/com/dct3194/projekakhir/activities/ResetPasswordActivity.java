package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {


    EditText etNewPassword;
    EditText etConfirmPassword;

    MaterialButton btnUpdatePassword;

    FirebaseAuth auth;

    String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        etNewPassword = findViewById(R.id.etNewPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnUpdatePassword = findViewById(R.id.btnUpdatePassword);


        auth = FirebaseAuth.getInstance();


        email = getIntent().getStringExtra("email");



        btnUpdatePassword.setOnClickListener(v -> {


            String password =
                    etNewPassword.getText()
                            .toString()
                            .trim();


            String confirm =
                    etConfirmPassword.getText()
                            .toString()
                            .trim();



            if(TextUtils.isEmpty(password)){

                etNewPassword.setError("Enter new password");
                return;

            }



            if(!password.equals(confirm)){


                Toast.makeText(
                        this,
                        "Password does not match",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }



            if(password.length() < 6){


                Toast.makeText(
                        this,
                        "Password minimum 6 characters",
                        Toast.LENGTH_SHORT
                ).show();

                return;

            }



            /*
             * Untuk testing UI dulu.
             * Selepas ini kita sambungkan
             * Firebase password reset handler.
             */


            Toast.makeText(
                    this,
                    "Password updated successfully",
                    Toast.LENGTH_LONG
            ).show();



            Intent intent =
                    new Intent(
                            ResetPasswordActivity.this,
                            LoginActivity.class
                    );


            startActivity(intent);
            finish();



        });


    }
}