package com.dct3194.projekakhir.database;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "EncikAircondSession";

    private static final String KEY_LOGGED_IN = "loggedIn";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";

    private final SharedPreferences preferences;

    public SessionManager(Context context) {

        preferences = context.getSharedPreferences(
                PREF_NAME,
                Context.MODE_PRIVATE
        );
    }

    // =========================
    // Save Login Session
    // =========================

    public void createLoginSession(
            String userId,
            String username,
            String email,
            String phone) {

        preferences.edit()
                .putBoolean(KEY_LOGGED_IN, true)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_USERNAME, username)
                .putString(KEY_EMAIL, email)
                .putString(KEY_PHONE, phone)
                .apply();
    }

    // =========================
    // Login Status
    // =========================

    public boolean isLoggedIn() {

        return preferences.getBoolean(
                KEY_LOGGED_IN,
                false
        );
    }

    // =========================
    // Get User ID
    // =========================

    public String getUserId() {

        return preferences.getString(
                KEY_USER_ID,
                null
        );
    }

    // =========================
    // Get Username
    // =========================

    public String getUsername() {

        return preferences.getString(
                KEY_USERNAME,
                ""
        );
    }

    // =========================
    // Get Email
    // =========================

    public String getEmail() {

        return preferences.getString(
                KEY_EMAIL,
                ""
        );
    }

    // =========================
    // Get Phone
    // =========================

    public String getPhone() {

        return preferences.getString(
                KEY_PHONE,
                ""
        );
    }

    // =========================
    // Clear Session
    // =========================

    public void logout() {

        preferences.edit()
                .clear()
                .apply();
    }
}