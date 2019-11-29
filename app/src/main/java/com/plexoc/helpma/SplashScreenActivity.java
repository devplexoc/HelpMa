package com.plexoc.helpma;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.plexoc.helpma.Model.Response;
import com.plexoc.helpma.Model.User;
import com.plexoc.helpma.Service.ApiClient;
import com.plexoc.helpma.Service.AppService;
import com.plexoc.helpma.Utils.LoadingDialog;
import com.plexoc.helpma.Utils.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

import static com.plexoc.helpma.MainActivity.Token;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private Handler handler;
    private User user;
    private ApiClient apiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        apiClient = AppService.createService(ApiClient.class);
        user = new User();
        handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermission();
            }
        }, 3000);
    }

    private void showSettingsDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreenActivity.this);
            builder.setTitle("Needs Permissions");
            builder.setMessage("All Permissions are required for application to work properly.Please grant permissions from setting.");
            builder.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    GotoSettings();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void GotoSettings() {
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivityForResult(intent, 101);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void OpenLogin() {
        try {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenHome() {
        try {
            Intent intent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkPermission() {

        Dexter.withActivity(SplashScreenActivity.this)
                .withPermissions(
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport report) {
                if (report.areAllPermissionsGranted()) {
                    try {
                        if (Prefs.getString(Prefs.USER) != null) {
                            if (!Prefs.getString(Prefs.USER).isEmpty()) {
                                user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                if (user != null) {
                                    LoadingDialog.showLoadingDialog(SplashScreenActivity.this);
                                    apiClient.Login(user.Email, user.Password,user.DeviceToken).enqueue(new Callback<Response<User>>() {
                                        @Override
                                        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
                                            if (response.isSuccessful()) {
                                                Prefs.putString(Prefs.USER, new Gson().toJson(response.body().Item));
                                                user = new Gson().fromJson(Prefs.getString(Prefs.USER), User.class);
                                                OpenHome();
                                            } else {
                                                OpenLogin();
                                            }
                                            LoadingDialog.cancelLoading();
                                        }

                                        @Override
                                        public void onFailure(Call<Response<User>> call, Throwable t) {
                                            LoadingDialog.cancelLoading();
                                            OpenLogin();
                                        }
                                    });
                                } else
                                    OpenLogin();
                            } else
                                OpenLogin();
                        } else
                            OpenLogin();

                    } catch (Exception e) {
                        e.printStackTrace();
                        OpenLogin();
                    }

                }

                if (report.isAnyPermissionPermanentlyDenied()) {
                    showSettingsDialog();
                }
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                token.continuePermissionRequest();
            }
        })
                .onSameThread()
                .check();
    }
}
