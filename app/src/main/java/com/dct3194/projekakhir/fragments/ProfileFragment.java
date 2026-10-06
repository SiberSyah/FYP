package com.dct3194.projekakhir.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class ProfileFragment extends Fragment {

    private ShapeableImageView imgProfile;
    private View btnCameraOverlay;
    private TextView txtName, txtEmail, txtPhone, txtAddress;
    private Button btnLogout;

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

        imgProfile = view.findViewById(R.id.imgProfile);
        btnCameraOverlay = view.findViewById(R.id.btnCameraOverlay);
        txtName = view.findViewById(R.id.txtName);
        txtEmail = view.findViewById(R.id.txtEmail);
        txtPhone = view.findViewById(R.id.txtPhone);
        txtAddress = view.findViewById(R.id.txtAddress);
        btnLogout = view.findViewById(R.id.btnLogout);

        // Click camera icon -> Change photo from gallery
        if (btnCameraOverlay != null) {
            btnCameraOverlay.setOnClickListener(v -> galleryLauncher.launch("image/*"));
        }

        // Click profile image -> View full photo preview
        if (imgProfile != null) {
            imgProfile.setOnClickListener(v -> showImagePreviewDialog());
        }

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
            if ((name == null || name.isEmpty()) && user.getDisplayName() != null) name = user.getDisplayName();
        }

        if (txtName != null && name != null && !name.isEmpty()) txtName.setText(name);
        if (txtEmail != null && email != null && !email.isEmpty()) txtEmail.setText(email);
        if (txtPhone != null && phone != null && !phone.isEmpty()) txtPhone.setText(phone);

        loadProfilePhoto();
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

        // Default profile picture logo
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
