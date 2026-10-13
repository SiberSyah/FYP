package com.dct3194.projekakhir.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.activities.LoginActivity;
import com.dct3194.projekakhir.database.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ProfileFragment extends Fragment {

    private ShapeableImageView imgProfile;
    private View btnCameraOverlay;
    private View layoutViewMode, layoutEditMode;

    private TextView txtName, txtEmail, txtViewName, txtViewPhone, txtViewAddress, txtViewEmail;
    private EditText etEditName, etEditPhone, etEditAddress, etEditCity, etEditPoscode, etEditState;

    private Button btnTriggerEdit, btnLogout, btnSaveProfile, btnCancelEdit;

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

        // View Mode views
        imgProfile = view.findViewById(R.id.imgProfile);
        btnCameraOverlay = view.findViewById(R.id.btnCameraOverlay);
        layoutViewMode = view.findViewById(R.id.layoutViewMode);
        layoutEditMode = view.findViewById(R.id.layoutEditMode);

        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtViewName = view.findViewById(R.id.txtViewName);
        txtViewPhone = view.findViewById(R.id.txtViewPhone);
        txtViewAddress = view.findViewById(R.id.txtViewAddress);
        txtViewEmail = view.findViewById(R.id.txtViewEmail);

        btnTriggerEdit = view.findViewById(R.id.btnTriggerEdit);
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

        // Click camera overlay -> Gallery photo picker
        if (btnCameraOverlay != null) {
            btnCameraOverlay.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        }

        // Click profile image -> View photo preview
        if (imgProfile != null) {
            imgProfile.setOnClickListener(v -> showImagePreviewDialog());
        }

        // Click Edit Profile Information -> Show Edit Mode
        if (btnTriggerEdit != null) {
            btnTriggerEdit.setOnClickListener(v -> switchToEditMode());
        }

        // Click Cancel -> Return to View Mode
        if (btnCancelEdit != null) {
            btnCancelEdit.setOnClickListener(v -> switchToViewMode());
        }

        // Click Save & Enter -> Save profile and return to View Mode
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

        // Logout
        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                sessionManager.logout();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
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

        String address = sessionManager.getAddress();
        String city = sessionManager.getCity();
        String poscode = sessionManager.getPoscode();
        String state = sessionManager.getState();

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

        StringBuilder fullAddress = new StringBuilder();
        if (address != null && !address.isEmpty()) fullAddress.append(address);
        if (city != null && !city.isEmpty()) {
            if (fullAddress.length() > 0) fullAddress.append(", ");
            fullAddress.append(city);
        }
        if (poscode != null && !poscode.isEmpty()) {
            if (fullAddress.length() > 0) fullAddress.append(", ");
            fullAddress.append(poscode);
        }
        if (state != null && !state.isEmpty()) {
            if (fullAddress.length() > 0) fullAddress.append(", ");
            fullAddress.append(state);
        }
        String displayAddress = (fullAddress.length() > 0) ? fullAddress.toString() : "-";

        if (txtName != null) txtName.setText(displayName);
        if (txtEmail != null) txtEmail.setText(displayEmail);
        if (txtViewName != null) txtViewName.setText(displayName);
        if (txtViewEmail != null) txtViewEmail.setText(displayEmail);
        if (txtViewPhone != null) txtViewPhone.setText(displayPhone);
        if (txtViewAddress != null) txtViewAddress.setText(displayAddress);

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
}
