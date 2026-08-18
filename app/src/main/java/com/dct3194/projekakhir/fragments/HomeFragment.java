package com.dct3194.projekakhir.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.activities.MaintenanceActivity;
import com.dct3194.projekakhir.adapters.PromoAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private final Handler sliderHandler = new Handler(Looper.getMainLooper());
    private ViewPager2 viewPagerPromo;
    private Dialog promoDialog;

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerPromo != null) {
                int currentItem = viewPagerPromo.getCurrentItem();
                int nextItem = (currentItem + 1) % 3;
                viewPagerPromo.setCurrentItem(nextItem, true);
                sliderHandler.postDelayed(this, 3000);
            }
        }
    };

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        View cardBuy = view.findViewById(R.id.cardBuy);
        View cardMaintenance = view.findViewById(R.id.cardMaintenance);

        cardBuy.setOnClickListener(v -> getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SelectAircondFragment())
                .addToBackStack(null)
                .commit());

        cardMaintenance.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MaintenanceActivity.class);
            startActivity(intent);
        });

        // Show full screen promo dialog
        showPromoDialog();

        return view;
    }

    private void showPromoDialog() {
        if (getActivity() == null) return;

        promoDialog = new Dialog(getActivity());
        promoDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        promoDialog.setContentView(R.layout.dialog_promo);

        Window window = promoDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        viewPagerPromo = promoDialog.findViewById(R.id.viewPagerPromo);
        ImageView btnClosePromo = promoDialog.findViewById(R.id.btnClosePromo);

        List<Integer> promoImages = new ArrayList<>();
        promoImages.add(R.drawable.ic_aircond);
        promoImages.add(R.drawable.ic_maintenance);
        promoImages.add(R.drawable.circle_logo);

        PromoAdapter adapter = new PromoAdapter(promoImages);
        viewPagerPromo.setAdapter(adapter);

        btnClosePromo.setOnClickListener(v -> promoDialog.dismiss());

        promoDialog.setOnDismissListener(dialog -> sliderHandler.removeCallbacks(sliderRunnable));

        promoDialog.show();
        sliderHandler.postDelayed(sliderRunnable, 3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (promoDialog != null && promoDialog.isShowing()) {
            sliderHandler.removeCallbacks(sliderRunnable);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        }
    }
}