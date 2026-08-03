package com.dct3194.projekakhir.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.AircondAdapter;
import com.dct3194.projekakhir.models.Aircond;

import java.util.ArrayList;
import java.util.List;

public class SelectAircondActivity extends AppCompatActivity {

    private RecyclerView recyclerAircond;
    private AircondAdapter adapter;
    private List<Aircond> aircondList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_aircond);

        recyclerAircond = findViewById(R.id.recyclerAircond);
        recyclerAircond.setLayoutManager(new LinearLayoutManager(this));

        aircondList = new ArrayList<>();

        aircondList.add(new Aircond(
                "Daikin FTKF25",
                "Daikin",
                R.mipmap.ic_launcher,
                1750,
                2150,
                2650,
                250
        ));

        aircondList.add(new Aircond(
                "Panasonic XPU9",
                "Panasonic",
                R.mipmap.ic_launcher,
                1899,
                2299,
                2799,
                250
        ));

        aircondList.add(new Aircond(
                "Midea Xtreme Save",
                "Midea",
                R.mipmap.ic_launcher,
                1599,
                1999,
                2399,
                250
        ));

        aircondList.add(new Aircond(
                "Haier CleanCool",
                "Haier",
                R.mipmap.ic_launcher,
                1699,
                2099,
                2499,
                250
        ));

        adapter = new AircondAdapter(aircondList, aircond -> {
            // Activity ini tidak lagi digunakan.
            // Navigation sebenar menggunakan SelectAircondFragment.
        });

        recyclerAircond.setAdapter(adapter);
    }
}