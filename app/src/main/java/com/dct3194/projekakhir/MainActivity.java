package com.dct3194.projekakhir;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dct3194.projekakhir.fragments.HomeFragment;
import com.dct3194.projekakhir.fragments.OrdersFragment;
import com.dct3194.projekakhir.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navProfile, navHome, navOrders;
    private ImageView iconProfile, iconHome, iconOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navProfile = findViewById(R.id.navProfile);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);

        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        String page = getIntent().getStringExtra("page");

        if ("profile".equals(page)) {
            loadFragment(new ProfileFragment());
            selectProfile();

        } else if ("orders".equals(page)) {
            loadFragment(new OrdersFragment());
            selectOrders();

        } else {
            loadFragment(new HomeFragment());
            selectHome();
        }

        navProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment());
            selectProfile();
        });

        navHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment());
            selectHome();
        });

        navOrders.setOnClickListener(v -> {
            Intent intent = new Intent(this, com.dct3194.projekakhir.activities.OrderStatusActivity.class);
            startActivity(intent);
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void resetIcons() {
        iconProfile.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));

        iconHome.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));

        iconOrders.setImageTintList(
                ColorStateList.valueOf(Color.GRAY));
    }

    private void selectProfile() {
        resetIcons();

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }

    private void selectHome() {
        resetIcons();

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }

    private void selectOrders() {
        resetIcons();

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")));
    }
}