package com.dct3194.projekakhir.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.dct3194.projekakhir.R;
import com.dct3194.projekakhir.database.SessionManager;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFullName, etPhone;
    private Button btnSave;
    private ImageView btnBack;
    private ShapeableImageView imgProfile;
    private View layoutProfilePhoto, btnCameraOverlay;

    private SessionManager sessionManager;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        sessionManager = new SessionManager(this);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        saveAndDisplayProfileImage(uri);
                    }
                }
        );

        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);
        imgProfile = findViewById(R.id.imgProfile);
        layoutProfilePhoto = findViewById(R.id.layoutProfilePhoto);
        btnCameraOverlay = findViewById(R.id.btnCameraOverlay);

        if (etFullName != null) etFullName.setText(sessionManager.getUsername());
        if (etPhone != null) etPhone.setText(sessionManager.getPhone());

        loadProfilePhoto();

        btnBack.setOnClickListener(v -> finish());

        View.OnClickListener openGalleryListener = v -> galleryLauncher.launch("image/*");
        if (btnCameraOverlay != null) btnCameraOverlay.setOnClickListener(openGalleryListener);
        if (layoutProfilePhoto != null) layoutProfilePhoto.setOnClickListener(openGalleryListener);

        btnSave.setOnClickListener(v -> {
            String name = etFullName.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                sessionManager.createLoginSession(
                        sessionManager.getUserId() != null ? sessionManager.getUserId() : "",
                        name,
                        sessionManager.getEmail(),
                        phone
                );
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void loadProfilePhoto() {
        if (imgProfile == null) return;

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

        int paddingPx = Math.round(16 * getResources().getDisplayMetrics().density);
        imgProfile.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        imgProfile.setImageResource(R.drawable.ic_person);
    }

    private void saveAndDisplayProfileImage(Uri uri) {
        if (imgProfile == null) return;

        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return;

            File file = new File(getFilesDir(), "profile_picture.jpg");
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

            Toast.makeText(this, "Profile photo updated", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to update profile photo", Toast.LENGTH_SHORT).show();
        }
    }
}
