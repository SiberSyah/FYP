package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;

public class AddressActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_address);

        Button btnAddAddress = findViewById(R.id.btnAddAddress);
        btnAddAddress.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddAddressActivity.class);
            startActivity(intent);
        });

        // Bottom Navigation
        findViewById(R.id.navHome).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "home");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navProfile).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "profile");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        findViewById(R.id.navOrders).setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("page", "orders");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
