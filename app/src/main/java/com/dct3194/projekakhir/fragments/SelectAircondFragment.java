package com.dct3194.projekakhir.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.adapters.AircondAdapter;
import com.dct3194.projekakhir.models.Aircond;

import java.util.ArrayList;
import java.util.List;

public class SelectAircondFragment extends Fragment {

    private RecyclerView recyclerAircond;
    private AircondAdapter adapter;
    private List<Aircond> aircondList;

    public SelectAircondFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_select_aircond, container, false);

        recyclerAircond = view.findViewById(R.id.recyclerAircond);
        recyclerAircond.setLayoutManager(new LinearLayoutManager(getContext()));

        // Tambah semua 7 Aircond sebijik macam dalam Figma
        aircondList = new ArrayList<>();

        aircondList.add(new Aircond(
                "Daikin X-Sense R32",
                "Daikin",
                R.mipmap.ic_launcher,
                1750, 2150, 2650, 250
        ));

        aircondList.add(new Aircond(
                "Panasonic Aero Series",
                "Panasonic",
                R.mipmap.ic_launcher,
                1520, 1920, 2420, 250
        ));

        aircondList.add(new Aircond(
                "Samsung TurboCool",
                "Samsung",
                R.mipmap.ic_launcher,
                1350, 1750, 2250, 250
        ));

        aircondList.add(new Aircond(
                "Midea Aurora",
                "Midea",
                R.mipmap.ic_launcher,
                2100, 2500, 3000, 250
        ));

        aircondList.add(new Aircond(
                "Hisense SmartCool",
                "Hisense",
                R.mipmap.ic_launcher,
                1290, 1690, 2190, 250
        ));

        aircondList.add(new Aircond(
                "Acson A5WMY15JR",
                "Acson",
                R.mipmap.ic_launcher,
                1680, 2080, 2580, 250
        ));

        aircondList.add(new Aircond(
                "LG DualCool Inverter",
                "LG",
                R.mipmap.ic_launcher,
                1299, 1699, 2199, 250
        ));

        adapter = new AircondAdapter(aircondList, aircond -> {
            Bundle bundle = new Bundle();
            bundle.putSerializable("aircond", aircond);

            AircondDetailsFragment fragment = new AircondDetailsFragment();
            fragment.setArguments(bundle);

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        recyclerAircond.setAdapter(adapter);

        return view;
    }
}