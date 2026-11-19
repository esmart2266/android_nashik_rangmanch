package in.gov.nmc.rangmach;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.concurrent.Executors;


public class SplashActivity extends AppCompatActivity {
    String[] permission = new String[]{
            Manifest.permission.POST_NOTIFICATIONS
    };
    boolean permission_post_notification = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        try {
            new in.gov.nmc.rangmach.utility.SSLHelper().setupSSL(getApplicationContext());
        } catch (Exception e) {

        }

        set_handler();
        clearGlideCache();

        if (!permission_post_notification) {
            requestPermissionNotification();
        }
    }

    private void set_handler() {
        int SPLASH_TIME_OUT = 3000;
        new Handler().postDelayed(this::startNextActivity, SPLASH_TIME_OUT);
    }

    private void clearGlideCache() {
        // Clear Disk Cache (background thread)
        Executors.newSingleThreadExecutor().execute(() -> {
            Log.d("Glide", "Clearing disk cache...");
            Glide.get(getApplicationContext()).clearDiskCache();

            new Handler(Looper.getMainLooper()).post(() ->
                    Log.d("Glide", "Disk cache cleared")
            );
        });

        // Clear Memory Cache (main thread)
        Log.d("Glide", "Clearing memory cache...");
        Glide.get(getApplicationContext()).clearMemory();
    }

    private void startNextActivity() {
        if (LoginActivity.loginActivity != null && !LoginActivity.loginActivity.isFinishing()) {
            LoginActivity.loginActivity.finish();
        }
        startActivity(new Intent(SplashActivity.this, LoginActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        finish();
    }

    public void requestPermissionNotification() {

        if (ContextCompat.checkSelfPermission(mContext, permission[0]) == PackageManager.PERMISSION_GRANTED) {
            permission_post_notification = true;
        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
//                    Log.d("Permission","insise else first time don't allow");
//                } else {
//                    Log.d("Permission","insise else 2nd time don't allow");
//                }
//            }
            requestPermissionLauncherNotification.launch(permission[0]);
        }
    }

    private ActivityResultLauncher<String> requestPermissionLauncherNotification =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    permission_post_notification = true;
                } else {
                    permission_post_notification = false;
//                    showPermissionDialog("Notification Permission");
                }
            });

}
