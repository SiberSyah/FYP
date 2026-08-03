package com.dct3194.projekakhir.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dct3194.projekakhir.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View cardBuy = view.findViewById(R.id.cardBuy);

        cardBuy.setOnClickListener(v -> {

            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new SelectAircondFragment())
                    .addToBackStack(null)
                    .commit();

        });

        return view;
    }
}