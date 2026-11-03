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
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            SelectDateTimeFragment fragment = new SelectDateTimeFragment();

            Bundle bundle = new Bundle();
            bundle.putString("mode", "MAINTENANCE");
            bundle.putString("hp", getIntent().getStringExtra("hp"));
            bundle.putString("type", getIntent().getStringExtra("type"));
            bundle.putString("service", getIntent().getStringExtra("service"));
            bundle.putString("price", getIntent().getStringExtra("price"));

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

    // =========================================================================
    // CIRI CHATBOT IN-APP PERSISTENT (TERSEDIA DI SEMUA HALAMAN)
    // =========================================================================
    public void showChatBotDialog() {
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

        // Mesej sapaan rasmi
        String welcomeMessage = "Hai! Saya pembantu maya EncikAircond ❄️\n\n" +
                "Ada yang boleh saya bantu hari ini?\n" +
                "• Taip saiz bilik (cth: 12x10) untuk kira BTU & HP aircond.\n" +
                "• Atau beritahu jika ada masalah aircond (cth: tak sejuk, bocor).";

        messageList.add(new ChatMessage(welcomeMessage, false));

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
            chipOption1.setOnClickListener(v -> processUserMessage("Bilik 12 x 15 kaki", chatAdapter, rvChat));
        }
        if (chipOption2 != null) {
            chipOption2.setOnClickListener(v -> processUserMessage("Bilik 10 x 10 kaki", chatAdapter, rvChat));
        }
        if (chipOption3 != null) {
            chipOption3.setOnClickListener(v -> processUserMessage("Panduan HP Aircond", chatAdapter, rvChat));
        }

        chatDialog.show();
    }

    private void processUserMessage(String userText, ChatAdapter chatAdapter, RecyclerView rvChat) {
        if (chatAdapter == null || rvChat == null || userText == null || userText.trim().isEmpty()) return;

        // 1. Tambah gelembung mesej pengguna
        chatAdapter.addMessage(new ChatMessage(userText, true));
        rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);

        // 2. Jana jawapan chatbot berasaskan logik tempatan (Offline/Local)
        String botReply = generateBotReply(userText);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            chatAdapter.addMessage(new ChatMessage(botReply, false));
            rvChat.smoothScrollToPosition(chatAdapter.getItemCount() - 1);
        }, 300);
    }

    private String generateBotReply(String input) {
        if (input == null) return "";
        String lower = input.trim().toLowerCase();

        // 1. Semak bentuk saiz bilik (Contoh: 12x10, 12 x 10, 15*20, 10 darab 10, dll.)
        Pattern dimPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(?:x|\\*|darab|kaki\\s*x)?\\s*(\\d+(?:\\.\\d+)?)");
        Matcher dimMatcher = dimPattern.matcher(lower);

        if (dimMatcher.find()) {
            try {
                String g1 = dimMatcher.group(1);
                String g2 = dimMatcher.group(2);
                if (g1 != null && g2 != null) {
                    double length = Double.parseDouble(g1);
                    double width = Double.parseDouble(g2);
                    if (length > 0 && width > 0) {
                        double area = length * width;
                        double btu = area * 60;
                        String hp;
                        String hpNote;

                        if (btu <= 9000) {
                            hp = "1.0 HP";
                            hpNote = "Sesuai untuk bilik tidur kecil atau ruang sederhana (hingga 150 sq ft).";
                        } else if (btu <= 12000) {
                            hp = "1.5 HP";
                            hpNote = "Sesuai untuk bilik utama / ruang tamu sederhana (150 - 200 sq ft).";
                        } else if (btu <= 18000) {
                            hp = "2.0 HP";
                            hpNote = "Sesuai untuk ruang tamu besar / pejabat (200 - 300 sq ft).";
                        } else if (btu <= 24000) {
                            hp = "2.5 HP";
                            hpNote = "Sesuai untuk ruang terbuka luas (300 - 400 sq ft).";
                        } else {
                            hp = "3.0 HP ke atas (atau 2 unit aircond)";
                            hpNote = "Keluasan sangat besar. Disyorkan meletakkan 2 unit aircond untuk liputan optimum.";
                        }

                        return String.format(
                                Locale.US,
                                "Hasil Pengiraan BTU & HP Aircond ❄️:\n\n" +
                                        "• Saiz Bilik: %.0f ft x %.0f ft = %.0f sq ft\n" +
                                        "• Keperluan BTU: %.0f BTU/h (Formula: Area x 60)\n" +
                                        "• Cadangan Aircond: %s\n\n" +
                                        "💡 %s\n\n" +
                                        "Tip: Jika bilik terdedah pada cahaya matahari terik atau ramainya penghuni, digalakkan memilih HP yang satu tingkat lebih tinggi!",
                                length, width, area, btu, hp, hpNote
                        );
                    }
                }
            } catch (Exception ignored) {
            }
        }

        // 2. Semak keluasan sq ft secara terus (Contoh: "150 sqft", "200 kaki")
        Pattern sqftPattern = Pattern.compile("(\\d+(?:\\.\\d+)?)\\s*(?:sqft|sq ft|kaki persegi)");
        Matcher sqftMatcher = sqftPattern.matcher(lower);
        if (sqftMatcher.find()) {
            try {
                String g1 = sqftMatcher.group(1);
                if (g1 != null) {
                    double area = Double.parseDouble(g1);
                    if (area > 0 && area < 5000) {
                        double btu = area * 60;
                        String hp;
                        if (btu <= 9000) hp = "1.0 HP";
                        else if (btu <= 12000) hp = "1.5 HP";
                        else if (btu <= 18000) hp = "2.0 HP";
                        else if (btu <= 24000) hp = "2.5 HP";
                        else hp = "3.0 HP ke atas";

                        return String.format(
                                Locale.US,
                                "Hasil Pengiraan BTU & HP Aircond ❄️:\n\n" +
                                        "• Keluasan Bilik: %.0f sq ft\n" +
                                        "• Keperluan BTU: %.0f BTU/h (Formula: Area x 60)\n" +
                                        "• Cadangan Aircond: %s\n\n" +
                                        "Sila rujuk menu tempahan EncikAircond untuk memilih unit aircond berpandukan HP di atas!",
                                area, btu, hp
                        );
                    }
                }
            } catch (Exception ignored) {
            }
        }

        // 3. Panduan HP Aircond
        if (lower.contains("panduan") || lower.contains("hp aircond") || lower.contains("hp berap")) {
            return "Panduan Pemilihan HP Aircond EncikAircond ❄️:\n\n" +
                    "• Bilik Kecil (< 150 sq ft) ➡️ 1.0 HP (9,000 BTU)\n" +
                    "• Bilik Sederhana (150 - 200 sq ft) ➡️ 1.5 HP (12,000 BTU)\n" +
                    "• Ruang Tamu / Bilik Besar (200 - 300 sq ft) ➡️ 2.0 HP (18,000 BTU)\n" +
                    "• Ruang Tamu Luas (300 - 400 sq ft) ➡️ 2.5 HP (24,000 BTU)\n\n" +
                    "Sila taip saiz bilik anda (contoh: 12x10) untuk pengiraan automatik!";
        }

        // 4. Kata Kunci Aduan / Masalah Aircond
        if (lower.contains("tak sejuk") || lower.contains("panas") || lower.contains("kurang sejuk") || lower.contains("angin je")) {
            return "Bagi masalah Aircond Tak Sejuk ❄️:\n\n" +
                    "Punca-punca biasa:\n" +
                    "1. Penapis udara (filter) kotor atau tersumbat.\n" +
                    "2. Gas refrigerant berkurangan atau bocor.\n" +
                    "3. Kompresor outdoor tidak berfungsi.\n\n" +
                    "👉 Cadangan: Lakukan Servis Biasa (RM80) atau Isian Gas (RM60) di menu tempahan EncikAircond!";
        }

        if (lower.contains("bocor") || lower.contains("menitis") || lower.contains("air keluar") || lower.contains("leaking")) {
            return "Bagi masalah Aircond Bocor / Air Menitis 💧:\n\n" +
                    "Punca-punca biasa:\n" +
                    "1. Saluran paip buang air tersumbat dengan habuk & lendir.\n" +
                    "2. Takungan air indoor kotor.\n" +
                    "3. Penebalan ais pada cooling coil.\n\n" +
                    "👉 Cadangan: Lakukan Cuci Kimia (Chemical Overhaul - RM150) untuk membersihkan saluran paip secara mendalam!";
        }

        if (lower.contains("bising") || lower.contains("bunyi") || lower.contains("bergetar") || lower.contains("noisy")) {
            return "Bagi masalah Aircond Bising / Berbunyi 🔊:\n\n" +
                    "Punca-punca biasa:\n" +
                    "1. Kipas indoor/outdoor kotor atau longgar.\n" +
                    "2. Kompresor bermasalah atau permukaan skru terendur.\n\n" +
                    "👉 Cadangan: Tempah juruteknik EncikAircond untuk pemeriksaan dan penyelengaraan!";
        }

        if (lower.contains("bau") || lower.contains("berbau") || lower.contains("busuk") || lower.contains("masam")) {
            return "Bagi masalah Aircond Berbau 🤢:\n\n" +
                    "Punca-punca biasa:\n" +
                    "1. Terdapat kulat/bakteria terperangkap pada gelung penyejuk (cooling coil).\n" +
                    "2. Penapis udara kotor.\n\n" +
                    "👉 Cadangan: Lakukan Cuci Kimia (Chemical Overhaul - RM150) untuk membunuh bakteria & mencuci kotoran degil!";
        }

        if (lower.contains("harga") || lower.contains("caj") || lower.contains("kos") || lower.contains("bayaran") || lower.contains("price")) {
            return "Senarai Harga Perkhidmatan EncikAircond 💰:\n\n" +
                    "• Servis Biasa: RM80\n" +
                    "• Isipadu Gas: RM60\n" +
                    "• Cuci Kimia (Chemical Overhaul): RM150\n" +
                    "• Pemasangan Aircond Baru: Bermula RM250\n\n" +
                    "Anda boleh membuat tempahan terus melalui aplikasi ini!";
        }

        if (lower.contains("servis") || lower.contains("tempah") || lower.contains("booking") || lower.contains("pasang")) {
            return "Untuk membuat tempahan servis atau pemasangan 🛠️:\n\n" +
                    "Sila pilih menu 'Aircond Maintenance' atau 'Select Aircond' di Skrin Utama (Home) untuk menetapkan tarikh & masa juruteknik pilihan anda.";
        }

        if (lower.contains("salam") || lower.contains("hai") || lower.contains("hello") || lower.contains("hi")) {
            return "Hai! Selamat datang ke EncikAircond ❄️\n\n" +
                    "Ada apa yang boleh saya bantu anda hari ini?\n" +
                    "• Taip saiz bilik (contoh: 12x10) untuk kira BTU & HP aircond.\n" +
                    "• Atau beritahu jika ada masalah aircond (contoh: tak sejuk, bocor).";
        }

        // Jawapan lalai (Default)
        return "Terima kasih atas mesej anda! ❄️\n\n" +
                "Saya boleh bantu anda dengan:\n" +
                "1. Pengiraan BTU & HP Aircond (Taip saiz bilik cth: 12x10).\n" +
                "2. Jawapan kerosakan aircond (cth: tak sejuk, bocor, bising, berbau).\n" +
                "3. Maklumat harga & tempahan servis EncikAircond.";
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
