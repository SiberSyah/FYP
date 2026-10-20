package com.dct3194.projekakhir;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.adapters.ChatAdapter;
import com.dct3194.projekakhir.fragments.HomeFragment;
import com.dct3194.projekakhir.fragments.OrdersFragment;
import com.dct3194.projekakhir.fragments.ProfileFragment;
import com.dct3194.projekakhir.fragments.SelectDateTimeFragment;
import com.dct3194.projekakhir.models.ChatMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

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
    // SHOW CHAT BOT DIALOG WITH RECYCLERVIEW & CUSTOM ADAPTER
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
        RecyclerView rvChat = chatDialog.findViewById(R.id.rvChat);
        EditText etChatMessage = chatDialog.findViewById(R.id.etChatMessage);
        ImageView btnSendChat = chatDialog.findViewById(R.id.btnSendChat);

        Button chipOption1 = chatDialog.findViewById(R.id.chipOption1);
        Button chipOption2 = chatDialog.findViewById(R.id.chipOption2);
        Button chipOption3 = chatDialog.findViewById(R.id.chipOption3);

        List<ChatMessage> messageList = new ArrayList<>();
        // Add initial bot welcome message
        messageList.add(new ChatMessage("Selamat datang! Saya bot pembantu Encik Aircond. Ada apa-apa yang boleh saya bantu anda hari ini?", false));

        ChatAdapter chatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);

        if (rvChat != null) {
            rvChat.setLayoutManager(layoutManager);
            rvChat.setAdapter(chatAdapter);
        }

        if (btnCloseChat != null) {
            btnCloseChat.setOnClickListener(v -> chatDialog.dismiss());
        }

        View.OnClickListener handleSend = v -> {
            if (etChatMessage == null) return;
            String text = etChatMessage.getText().toString().trim();
            if (text.isEmpty()) return;

            processUserMessage(text, chatAdapter, rvChat);
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
            chipOption1.setOnClickListener(v -> processUserMessage("Aircond tak sejuk", chatAdapter, rvChat));
        }
        if (chipOption2 != null) {
            chipOption2.setOnClickListener(v -> processUserMessage("Servis biasa", chatAdapter, rvChat));
        }
        if (chipOption3 != null) {
            chipOption3.setOnClickListener(v -> processUserMessage("Cuci kimia", chatAdapter, rvChat));
        }

        chatDialog.show();
    }

    private void processUserMessage(String userText, ChatAdapter chatAdapter, RecyclerView rvChat) {
        if (chatAdapter == null || rvChat == null) return;

        // Add User ChatMessage (Right Side Bubble)
        chatAdapter.addMessage(new ChatMessage(userText, true));
        rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

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

        // Delayed Bot Response (Left Side Bubble)
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            chatAdapter.addMessage(new ChatMessage(botReply, false));
            rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        }, 500);
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
