package com.dct3194.projekakhir.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.dct3194.projekakhir.MainActivity;
import com.dct3194.projekakhir.R;

public class OrderStatusActivity extends AppCompatActivity {

    private int currentStatus = 2; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        updateTimeline(currentStatus);

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
    }

    private void updateTimeline(int statusIndex) {
        setupStep(findViewById(R.id.stepConfirmed), 0, statusIndex, 
                "Order Confirmed", "Your Order has been confirmed", R.drawable.ic_check_circle, true);

        setupStep(findViewById(R.id.stepAssigned), 1, statusIndex, 
                "Technician Assigned", "John Wilson Assigned", R.drawable.ic_check_circle, true);

        setupStep(findViewById(R.id.stepOnTheWay), 2, statusIndex, 
                "On The Way", "Technician is heading to your location", R.drawable.ic_truck, true);

        setupStep(findViewById(R.id.stepCompleted), 3, statusIndex, 
                "Completed", "Service is Completed", R.drawable.ic_check_circle, false);
    }

    private void setupStep(View view, int stepIndex, int currentStatus, String title, String desc, int iconRes, boolean showLine) {
        TextView txtTitle = view.findViewById(R.id.txtStatusTitle);
        TextView txtDesc = view.findViewById(R.id.txtStatusDesc);
        ImageView imgIcon = view.findViewById(R.id.imgStatusIcon);
        View viewLine = view.findViewById(R.id.viewLine);
        View iconContainer = view.findViewById(R.id.layoutIconContainer);

        txtTitle.setText(title);
        txtDesc.setText(desc);
        imgIcon.setImageResource(iconRes);

        String circleColor;
        String iconColor;
        String lineColor;

        if (stepIndex < currentStatus) {
            // Completed Steps before current
            circleColor = "#D7F1D8"; // Very Light Green
            iconColor = "#50B054";   // Medium Green
            lineColor = "#3158B8";   // Blue Line (connecting to active)
        } else if (stepIndex == currentStatus) {
            // Current Active Step
            circleColor = "#1D449A"; // Dark Blue Circle
            iconColor = "#FFFFFF";   // White Icon
            lineColor = "#9EABB1";   // Grey Line (to future)
        } else {
            // Future Pending Steps
            circleColor = "#607D8B"; // Dark Grey Circle (as in image for completed/pending)
            iconColor = "#FFFFFF";   // White Icon
            lineColor = "#9EABB1";
        }
        
        // Special case for image match: if current is 2 (On way), step 2 should be dark blue.
        // If step 4 (Completed) is the one in the image it's dark grey.
        if (stepIndex == 3) {
            circleColor = "#607D8B";
            iconColor = "#FFFFFF";
        }

        GradientDrawable shape = new GradientDrawable();
        shape.setShape(GradientDrawable.OVAL);
        shape.setColor(Color.parseColor(circleColor));
        iconContainer.setBackground(shape);

        imgIcon.setColorFilter(Color.parseColor(iconColor));
        
        if (viewLine != null) {
            viewLine.setBackgroundColor(Color.parseColor(lineColor));
            viewLine.setVisibility(showLine ? View.VISIBLE : View.INVISIBLE);
        }
    }
}
