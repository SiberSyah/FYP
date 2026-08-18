package com.dct3194.projekakhir;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateChecker {

    private static final String VERSION_URL =
            "https://encikaircond-update.web.app/version.json";

    public static void checkForUpdate(Context context) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(VERSION_URL)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // Internet gagal - jangan ganggu user.
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                if (!response.isSuccessful() || response.body() == null) {
                    return;
                }

                try {

                    String json = response.body().string();

                    JSONObject updateInfo =
                            new JSONObject(json);

                    long latestVersionCode =
                            updateInfo.getLong("versionCode");

                    String latestVersionName =
                            updateInfo.getString("versionName");

                    String apkUrl =
                            updateInfo.getString("apkUrl");

                    PackageInfo packageInfo =
                            context.getPackageManager()
                                    .getPackageInfo(
                                            context.getPackageName(),
                                            0
                                    );

                    long currentVersionCode;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                        currentVersionCode =
                                packageInfo.getLongVersionCode();
                    } else {
                        currentVersionCode =
                                packageInfo.versionCode;
                    }

                    if (latestVersionCode > currentVersionCode) {

                        ((Activity) context).runOnUiThread(() -> {

                            showUpdateDialog(
                                    context,
                                    latestVersionName,
                                    apkUrl
                            );

                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void showUpdateDialog(
            Context context,
            String versionName,
            String apkUrl) {

        new AlertDialog.Builder(context)
                .setTitle("New Update Available")
                .setMessage(
                        "A new version of EncikAircond is available.\n\n"
                                + "Version "
                                + versionName
                                + "\n\n"
                                + "Update the app to get the latest features and improvements."
                )
                .setCancelable(false)
                .setPositiveButton(
                        "UPDATE NOW",
                        (dialog, which) -> {

                            downloadAndInstall(
                                    context,
                                    apkUrl
                            );

                        }
                )
                .setNegativeButton(
                        "LATER",
                        null
                )
                .show();
    }

    private static void downloadAndInstall(
            Context context,
            String apkUrl) {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(apkUrl)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(
                    Call call,
                    IOException e) {

                ((Activity) context).runOnUiThread(() -> {

                    new AlertDialog.Builder(context)
                            .setTitle("Update Failed")
                            .setMessage(
                                    "Unable to download the update. Please check your internet connection."
                            )
                            .setPositiveButton("OK", null)
                            .show();

                });
            }

            @Override
            public void onResponse(
                    Call call,
                    Response response)
                    throws IOException {

                if (!response.isSuccessful()
                        || response.body() == null) {

                    return;
                }

                File apkFile = new File(
                        context.getExternalCacheDir(),
                        "EncikAircond.apk"
                );

                try (InputStream inputStream =
                             response.body().byteStream();
                     FileOutputStream outputStream =
                             new FileOutputStream(apkFile)) {

                    byte[] buffer = new byte[8192];

                    int length;

                    while ((length =
                            inputStream.read(buffer)) != -1) {

                        outputStream.write(
                                buffer,
                                0,
                                length
                        );
                    }

                    outputStream.flush();

                    Uri apkUri =
                            FileProvider.getUriForFile(
                                    context,
                                    context.getPackageName()
                                            + ".fileprovider",
                                    apkFile
                            );

                    Intent installIntent =
                            new Intent(
                                    Intent.ACTION_VIEW
                            );

                    installIntent.setDataAndType(
                            apkUri,
                            "application/vnd.android.package-archive"
                    );

                    installIntent.addFlags(
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    );

                    installIntent.addFlags(
                            Intent.FLAG_ACTIVITY_NEW_TASK
                    );

                    context.startActivity(
                            installIntent
                    );

                } catch (Exception e) {

                    e.printStackTrace();

                    ((Activity) context).runOnUiThread(() -> {

                        new AlertDialog.Builder(context)
                                .setTitle("Update Failed")
                                .setMessage(
                                        "The update could not be installed."
                                )
                                .setPositiveButton(
                                        "OK",
                                        null
                                )
                                .show();

                    });
                }
            }
        });
    }
}