package com.dct3194.projekakhir.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.activities.LoginActivity;
import com.dct3194.projekakhir.adapters.ChatAdapter;
import com.dct3194.projekakhir.database.SessionManager;
import com.dct3194.projekakhir.models.ChatMessage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileFragment extends Fragment {

    private ShapeableImageView imgProfile;
    private View layoutViewMode, layoutEditMode;
    private View btnTriggerEdit, btnNotifications;
    private View btnSecurity, btnTheme;
    private View btnOpenChatbotMenu, btnContactUs, btnPrivacyPolicy;

    private TextView txtName, txtSubInfo;
    private EditText etEditName, etEditPhone, etEditAddress, etEditCity, etEditPoscode, etEditState;

    private Button btnLogout, btnSaveProfile, btnCancelEdit;

    private SessionManager sessionManager;
    private ActivityResultLauncher<String> galleryLauncher;

    public ProfileFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sessionManager = new SessionManager(requireContext());

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        saveAndDisplayProfileImage(uri);
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Header Views
        imgProfile = view.findViewById(R.id.imgProfile);
        View btnCameraOverlay = view.findViewById(R.id.btnCameraOverlay);
        txtName = view.findViewById(R.id.txtName);
        txtSubInfo = view.findViewById(R.id.txtSubInfo);

        // View Mode & Edit Mode containers
        layoutViewMode = view.findViewById(R.id.layoutViewMode);
        layoutEditMode = view.findViewById(R.id.layoutEditMode);

        // Group 1 Menu Items
        btnTriggerEdit = view.findViewById(R.id.btnTriggerEdit);
        btnNotifications = view.findViewById(R.id.btnNotifications);

        // Group 2 Menu Items
        btnSecurity = view.findViewById(R.id.btnSecurity);
        btnTheme = view.findViewById(R.id.btnTheme);

        // Group 3 Menu Items
        btnOpenChatbotMenu = view.findViewById(R.id.btnOpenChatbotMenu);
        btnContactUs = view.findViewById(R.id.btnContactUs);
        btnPrivacyPolicy = view.findViewById(R.id.btnPrivacyPolicy);

        // Logout
        btnLogout = view.findViewById(R.id.btnLogout);

        // Edit Mode views
        etEditName = view.findViewById(R.id.etEditName);
        etEditPhone = view.findViewById(R.id.etEditPhone);
        etEditAddress = view.findViewById(R.id.etEditAddress);
        etEditCity = view.findViewById(R.id.etEditCity);
        etEditPoscode = view.findViewById(R.id.etEditPoscode);
        etEditState = view.findViewById(R.id.etEditState);

        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnCancelEdit = view.findViewById(R.id.btnCancelEdit);

        // Header action icons
        View btnNotificationHeader = view.findViewById(R.id.btnNotificationHeader);
        if (btnNotificationHeader != null) {
            btnNotificationHeader.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Notifications: ON", Toast.LENGTH_SHORT).show()
            );
        }

        // Click camera overlay -> Gallery photo picker
        if (btnCameraOverlay != null) {
            btnCameraOverlay.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        }

        // Click profile image -> View photo preview
        if (imgProfile != null) {
            imgProfile.setOnClickListener(v -> showImagePreviewDialog());
        }

        // Click "Edit profile information" -> Show Edit Mode
        if (btnTriggerEdit != null) {
            btnTriggerEdit.setOnClickListener(v -> switchToEditMode());
        }

        // Menu item click handlers
        if (btnNotifications != null) {
            btnNotifications.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Notifications are enabled", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnSecurity != null) {
            btnSecurity.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Account Security: Secure", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnTheme != null) {
            btnTheme.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Current Theme: Light mode", Toast.LENGTH_SHORT).show()
            );
        }

        if (btnContactUs != null) {
            btnContactUs.setOnClickListener(v ->
                    Toast.makeText(getContext(), "EncikAircond Hotline: +60 11-2580 6807", Toast.LENGTH_LONG).show()
            );
        }

        if (btnPrivacyPolicy != null) {
            btnPrivacyPolicy.setOnClickListener(v ->
                    Toast.makeText(getContext(), "Privacy Policy: Your data is protected by EncikAircond", Toast.LENGTH_SHORT).show()
            );
        }

        // Click Cancel -> Return to View Mode
        if (btnCancelEdit != null) {
            btnCancelEdit.setOnClickListener(v -> switchToViewMode());
        }

        // Click Save -> Save profile and return to View Mode
        if (btnSaveProfile != null) {
            btnSaveProfile.setOnClickListener(v -> saveProfileChanges());
        }

        // Hitting enter on state field saves profile
        if (etEditState != null) {
            etEditState.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    saveProfileChanges();
                    return true;
                }
                return false;
            });
        }

        // Logout button
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                sessionManager.logout();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        // Open Chatbot Dialog from Menu Item
        if (btnOpenChatbotMenu != null) {
            btnOpenChatbotMenu.setOnClickListener(v -> showChatBotDialog());
        }

        loadUserData();
        switchToViewMode();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserData();
    }

    private void loadUserData() {
        if (sessionManager == null || getContext() == null) return;

        String name = sessionManager.getUsername();
        String email = sessionManager.getEmail();
        String phone = sessionManager.getPhone();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (email == null || email.isEmpty()) email = user.getEmail();
            if (name == null || name.isEmpty()) {
                if (user.getDisplayName() != null && !user.getDisplayName().isEmpty()) {
                    name = user.getDisplayName();
                } else if (user.getEmail() != null) {
                    int atPos = user.getEmail().indexOf("@");
                    if (atPos > 0) name = user.getEmail().substring(0, atPos);
                }
            }
        }

        String displayName = (name != null && !name.isEmpty()) ? name : "User";
        String displayEmail = (email != null && !email.isEmpty()) ? email : "guest@email.com";
        String displayPhone = (phone != null && !phone.isEmpty()) ? phone : "-";

        String subInfoText = displayEmail + " | " + displayPhone;

        if (txtName != null) txtName.setText(displayName);
        if (txtSubInfo != null) txtSubInfo.setText(subInfoText);

        loadProfilePhoto();
    }

    private void switchToEditMode() {
        if (layoutViewMode != null) layoutViewMode.setVisibility(View.GONE);
        if (layoutEditMode != null) layoutEditMode.setVisibility(View.VISIBLE);

        String currentName = sessionManager.getUsername();
        String currentPhone = sessionManager.getPhone();
        String currentAddress = sessionManager.getAddress();
        String currentCity = sessionManager.getCity();
        String currentPoscode = sessionManager.getPoscode();
        String currentState = sessionManager.getState();

        if (etEditName != null) etEditName.setText(currentName);
        if (etEditPhone != null) etEditPhone.setText(currentPhone);
        if (etEditAddress != null) etEditAddress.setText(currentAddress);
        if (etEditCity != null) etEditCity.setText(currentCity);
        if (etEditPoscode != null) etEditPoscode.setText(currentPoscode);
        if (etEditState != null) etEditState.setText(currentState);
    }

    private void switchToViewMode() {
        if (layoutEditMode != null) layoutEditMode.setVisibility(View.GONE);
        if (layoutViewMode != null) layoutViewMode.setVisibility(View.VISIBLE);
        loadUserData();
    }

    private void saveProfileChanges() {
        if (etEditName == null || etEditPhone == null) return;

        String newName = etEditName.getText().toString().trim();
        String newPhone = etEditPhone.getText().toString().trim();
        String newAddress = etEditAddress != null ? etEditAddress.getText().toString().trim() : "";
        String newCity = etEditCity != null ? etEditCity.getText().toString().trim() : "";
        String newPoscode = etEditPoscode != null ? etEditPoscode.getText().toString().trim() : "";
        String newState = etEditState != null ? etEditState.getText().toString().trim() : "";

        if (newName.isEmpty()) {
            Toast.makeText(getContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = sessionManager.getUserId();
        String email = sessionManager.getEmail();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            if (uid == null || uid.isEmpty()) uid = user.getUid();
            if (email == null || email.isEmpty()) email = user.getEmail();
        }

        // Save into SessionManager
        sessionManager.createLoginSession(
                uid != null ? uid : "",
                newName,
                email != null ? email : "",
                newPhone
        );

        sessionManager.saveAddressDetails(newAddress, newCity, newPoscode, newState);

        // Save into Firestore if logged in
        if (uid != null && !uid.isEmpty()) {
            Map<String, Object> updates = new HashMap<>();
            updates.put("username", newName);
            updates.put("phone", newPhone);
            updates.put("address", newAddress);
            updates.put("city", newCity);
            updates.put("poscode", newPoscode);
            updates.put("state", newState);

            FirebaseFirestore.getInstance().collection("users")
                    .document(uid)
                    .update(updates);
        }

        Toast.makeText(getContext(), "Profile updated successfully!", Toast.LENGTH_SHORT).show();

        // Return to View Mode
        switchToViewMode();
    }

    private void loadProfilePhoto() {
        if (sessionManager == null || getContext() == null || imgProfile == null) return;

        String savedPath = sessionManager.getProfileImagePath();
        if (savedPath != null) {
            File file = new File(savedPath);
            if (file.exists()) {
                imgProfile.setPadding(0, 0, 0, 0);
                imgProfile.setImageURI(null);
                imgProfile.setImageURI(Uri.fromFile(file));
                return;
            }
        }

        imgProfile.setPadding(0, 0, 0, 0);
        imgProfile.setImageResource(R.drawable.yusrilogo);
    }

    private void saveAndDisplayProfileImage(Uri uri) {
        if (getContext() == null || imgProfile == null) return;

        try {
            InputStream inputStream = requireContext().getContentResolver().openInputStream(uri);
            if (inputStream == null) return;

            File file = new File(requireContext().getFilesDir(), "profile_picture.jpg");
            FileOutputStream outputStream = new FileOutputStream(file);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

            sessionManager.setProfileImagePath(file.getAbsolutePath());

            imgProfile.setPadding(0, 0, 0, 0);
            imgProfile.setImageURI(null);
            imgProfile.setImageURI(Uri.fromFile(file));

            Toast.makeText(getContext(), "Profile photo updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Failed to update profile photo", Toast.LENGTH_SHORT).show();
        }
    }

    private void showImagePreviewDialog() {
        if (getContext() == null) return;

        Dialog dialog = new Dialog(requireContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_image_preview);

        ImageView imgPreview = dialog.findViewById(R.id.imgPreview);
        ImageView btnClose = dialog.findViewById(R.id.btnClosePreview);

        String savedPath = sessionManager.getProfileImagePath();
        if (savedPath != null && new File(savedPath).exists()) {
            imgPreview.setImageURI(Uri.fromFile(new File(savedPath)));
        } else {
            imgPreview.setImageResource(R.drawable.yusrilogo);
        }

        if (btnClose != null) {
            btnClose.setOnClickListener(v -> dialog.dismiss());
        }

        dialog.show();
    }

    // =========================================================================
    // CIRI CHATBOT IN-APP (TANPA API KEY - SMART LOCAL LOGIC)
    // =========================================================================
    private void showChatBotDialog() {
        if (getContext() == null) return;

        Dialog chatDialog = new Dialog(requireContext(), android.R.style.Theme_Light_NoTitleBar_Fullscreen);
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
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
}
