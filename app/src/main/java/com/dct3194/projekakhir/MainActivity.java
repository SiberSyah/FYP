package com.dct3194.projekakhir;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dct3194.projekakhir.fragments.HomeFragment;
import com.dct3194.projekakhir.fragments.OrdersFragment;
import com.dct3194.projekakhir.fragments.ProfileFragment;
import com.dct3194.projekakhir.fragments.SelectDateTimeFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private LinearLayout navProfile, navHome, navOrders;
    private ImageView iconProfile, iconHome, iconOrders;
    private FloatingActionButton fabChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ==========================
        // Check App Update
        // ==========================
        UpdateChecker.checkForUpdate(this);

        navProfile = findViewById(R.id.navProfile);
        navHome = findViewById(R.id.navHome);
        navOrders = findViewById(R.id.navOrders);

        iconProfile = findViewById(R.id.iconProfile);
        iconHome = findViewById(R.id.iconHome);
        iconOrders = findViewById(R.id.iconOrders);

        fabChat = findViewById(R.id.fabChat);
        if (fabChat != null) {
            fabChat.setOnClickListener(v -> showChatBotDialog());
        }

        String page = getIntent().getStringExtra("page");
        String openFragment = getIntent().getStringExtra("openFragment");

        if ("maintenanceDate".equals(openFragment)) {

            SelectDateTimeFragment fragment =
                    new SelectDateTimeFragment();

            Bundle bundle = new Bundle();

            bundle.putString(
                    "mode",
                    "MAINTENANCE"
            );

            bundle.putString(
                    "hp",
                    getIntent().getStringExtra("hp")
            );

            bundle.putString(
                    "type",
                    getIntent().getStringExtra("type")
            );

            bundle.putString(
                    "service",
                    getIntent().getStringExtra("service")
            );

            bundle.putString(
                    "price",
                    getIntent().getStringExtra("price")
            );

            fragment.setArguments(bundle);

            loadFragment(fragment);

            selectHome();

        } else if ("profile".equals(page)) {

            loadFragment(new ProfileFragment());
            selectProfile();

        } else if ("orders".equals(page)) {

            loadFragment(new OrdersFragment());
            selectOrders();

        } else {

            loadFragment(new ProfileFragment());
            selectProfile();
        }

        // ==========================
        // Bottom Navigation
        // ==========================

        navProfile.setOnClickListener(v -> {

            loadFragment(new ProfileFragment());
            selectProfile();
        });

        navHome.setOnClickListener(v -> {

            loadFragment(new HomeFragment());
            selectHome();
        });

        navOrders.setOnClickListener(v -> {

            loadFragment(new OrdersFragment());
            selectOrders();
        });
    }

    // =========================================================
    // SHOW CHAT BOT DIALOG
    // =========================================================
    private void showChatBotDialog() {
        Dialog chatDialog = new Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        chatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        chatDialog.setContentView(R.layout.dialog_chat_bot);

        Window window = chatDialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        ImageView btnCloseChat = chatDialog.findViewById(R.id.btnCloseChat);
        LinearLayout layoutMessages = chatDialog.findViewById(R.id.layoutMessages);
        ScrollView scrollChat = chatDialog.findViewById(R.id.scrollChat);
        EditText etChatMessage = chatDialog.findViewById(R.id.etChatMessage);
        ImageView btnSendChat = chatDialog.findViewById(R.id.btnSendChat);

        Button chipOption1 = chatDialog.findViewById(R.id.chipOption1);
        Button chipOption2 = chatDialog.findViewById(R.id.chipOption2);
        Button chipOption3 = chatDialog.findViewById(R.id.chipOption3);

        if (btnCloseChat != null) {
            btnCloseChat.setOnClickListener(v -> chatDialog.dismiss());
        }

        View.OnClickListener handleSend = v -> {
            if (etChatMessage == null) return;
            String text = etChatMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            processUserMessage(text, layoutMessages, scrollChat);
            etChatMessage.setText("");
        };

        if (btnSendChat != null) {
            btnSendChat.setOnClickListener(handleSend);
        }

        if (etChatMessage != null) {
            etChatMessage.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handleSend.onClick(v);
                    return true;
                }
                return false;
            });
        }

        if (chipOption1 != null) {
            chipOption1.setOnClickListener(v -> processUserMessage("Aircond tak sejuk", layoutMessages, scrollChat));
        }
        if (chipOption2 != null) {
            chipOption2.setOnClickListener(v -> processUserMessage("Servis biasa", layoutMessages, scrollChat));
        }
        if (chipOption3 != null) {
            chipOption3.setOnClickListener(v -> processUserMessage("Cuci kimia", layoutMessages, scrollChat));
        }

        chatDialog.show();
    }

    private void processUserMessage(String userText, LinearLayout layoutMessages, ScrollView scrollChat) {
        if (layoutMessages == null || scrollChat == null) return;

        // Add User Bubble
        addMessageBubble(layoutMessages, scrollChat, userText, true);

        // Determine Bot Reply
        String lower = userText.toLowerCase();
        String botReply;

        if (lower.contains("tak sejuk") || lower.contains("panas")) {
            botReply = "Masalah aircond tak sejuk biasanya berpunca daripada gas berkurangan, penapis kotor, atau masalah kompresor. Anda boleh membuat tempahan pemeriksaan & servis di menu Aircond Maintenance!";
        } else if (lower.contains("servis biasa") || lower.contains("biasa")) {
            botReply = "Servis biasa merangkumi pembersihan penapis udara, cuci penutup hadapan, dan semakan tekanan gas. Harga bermula dari RM80 sahaja.";
        } else if (lower.contains("cuci kimia") || lower.contains("chemical")) {
            botReply = "Cuci Kimia (Chemical Overhaul) membersihkan keseluruhan komponen dalaman aircond secara mendalam untuk menghilangkan kotoran degil & bau buruk. Harga bermula dari RM150.";
        } else if (lower.contains("harga") || lower.contains("berapa")) {
            botReply = "Harga perkhidmatan kami:\n• Servis Biasa: RM80\n• Isipadu Gas: RM60\n• Cuci Kimia: RM150";
        } else {
            botReply = "Terima kasih atas mesej anda! Pasukan Encik Aircond sedia membantu. Sila pilih perkhidmatan di skrin utama atau beritahu kami jika anda ingin membuat tempahan.";
        }

        // Delayed Bot Response
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            addMessageBubble(layoutMessages, scrollChat, botReply, false);
        }, 600);
    }

    private void addMessageBubble(LinearLayout container, ScrollView scroll, String text, boolean isUser) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.topMargin = 12;
        params.bottomMargin = 12;

        if (isUser) {
            params.gravity = Gravity.END;
            row.setBackgroundResource(R.drawable.button_blue);
        } else {
            params.gravity = Gravity.START;
            row.setBackgroundResource(R.drawable.bg_input_field);
        }

        row.setLayoutParams(params);
        row.setPadding(32, 24, 32, 24);

        TextView tvText = new TextView(this);
        tvText.setText(text);
        tvText.setTextSize(14f);
        tvText.setTextColor(isUser ? Color.WHITE : Color.parseColor("#2D3748"));

        row.addView(tvText);
        container.addView(row);

        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    // ==========================
    // Load Fragment
    // ==========================

    private void loadFragment(Fragment fragment) {

        getSupportFragmentManager()
                .beginTransaction()
                .replace(
                        R.id.fragment_container,
                        fragment
                )
                .commit();
    }

    // ==========================
    // Bottom Navigation Icons
    // ==========================

    private void resetIcons() {

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.GRAY
                )
        );
    }

    private void selectProfile() {

        resetIcons();

        iconProfile.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")
                )
        );
    }

    private void selectHome() {

        resetIcons();

        iconHome.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")
                )
        );
    }

    private void selectOrders() {

        resetIcons();

        iconOrders.setImageTintList(
                ColorStateList.valueOf(
                        Color.parseColor("#3158B8")
                )
        );
    }
}
