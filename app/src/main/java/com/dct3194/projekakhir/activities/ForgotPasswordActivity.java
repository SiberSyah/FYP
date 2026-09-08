package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
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
    private MaterialButton btnSendReset;

    private FirebaseFirestore db;


    private final String FUNCTION_URL =
            "https://us-central1-encikaircond.cloudfunctions.net/sendOTPEmail";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);



        etEmail = findViewById(R.id.etEmail);
        btnSendReset = findViewById(R.id.btnSendReset);


        db = FirebaseFirestore.getInstance();



        btnSendReset.setOnClickListener(v -> {


            String email =
                    etEmail.getText()
                            .toString()
                            .trim();



            if(TextUtils.isEmpty(email)){

                etEmail.setError("Enter your email");
                return;

            }



            String otp =
                    String.valueOf(
                            100000 +
                                    new Random().nextInt(900000)
                    );



            Map<String,Object> data =
                    new HashMap<>();


            data.put("email", email);
            data.put("otp", otp);

            data.put(
                    "expireTime",
                    System.currentTimeMillis() + 600000
            );



            db.collection("password_reset")
                    .document(email)
                    .set(data)

                    .addOnSuccessListener(unused -> {


                        sendOTPEmail(email, otp);


                    })


                    .addOnFailureListener(e -> {


                        Toast.makeText(
                                this,
                                "Failed to create OTP",
                                Toast.LENGTH_SHORT
                        ).show();


                    });



        });


    }




    private void sendOTPEmail(
            String email,
            String otp
    ){


        OkHttpClient client =
                new OkHttpClient();



        String json =
                "{"
                        + "\"email\":\"" + email + "\","
                        + "\"otp\":\"" + otp + "\""
                        + "}";



        RequestBody body =
                RequestBody.create(
                        json,
                        MediaType.parse(
                                "application/json"
                        )
                );



        Request request =
                new Request.Builder()
                        .url(FUNCTION_URL)
                        .post(body)
                        .build();




        client.newCall(request)
                .enqueue(new Callback() {


                    @Override
                    public void onFailure(
                            Call call,
                            IOException e
                    ){

                        runOnUiThread(() -> {

                            Toast.makeText(
                                    ForgotPasswordActivity.this,
                                    "Failed sending OTP email",
                                    Toast.LENGTH_LONG
                            ).show();

                        });

                    }



                    @Override
                    public void onResponse(
                            Call call,
                            Response response
                    ){


                        runOnUiThread(() -> {


                            if(response.isSuccessful()){


                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        "OTP sent to your email",
                                        Toast.LENGTH_LONG
                                ).show();



                                Intent intent =
                                        new Intent(
                                                ForgotPasswordActivity.this,
                                                VerifyOTPActivity.class
                                        );


                                intent.putExtra(
                                        "email",
                                        email
                                );


                                startActivity(intent);



                            }else{


                                Toast.makeText(
                                        ForgotPasswordActivity.this,
                                        "Email failed",
                                        Toast.LENGTH_LONG
                                ).show();


                            }



                        });


                    }



                });


    }


}