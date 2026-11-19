package in.gov.nmc.rangmach;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.stetho.Stetho;

import java.io.InputStream;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.preferences.Preferences;
import in.gov.nmc.rangmach.utility.UnsafeOkHttpClient;
import okhttp3.OkHttpClient;

public class App extends MultiDexApplication {
    private ApiRequestHelper apiRequestHelper;
    private Preferences preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        doInit();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (BuildConfig.DEBUG) {
            Stetho.initialize(Stetho.newInitializerBuilder(this)
                    .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                    .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                    //.enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                    .build());
        }

        OkHttpClient client = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Glide.get(this).getRegistry().replace(
                GlideUrl.class,
                InputStream.class,
                new OkHttpUrlLoader.Factory(client)
        );
        //Android 8.0
        createNotificationChannelForOreo();
    }

    private void doInit() {
        this.preferences = new Preferences(this);
        this.apiRequestHelper = ApiRequestHelper.init(this);
    }

    public synchronized ApiRequestHelper getApiRequestHelper() {
        return apiRequestHelper;
    }

    public synchronized Preferences getPreferences() {
        return preferences;
    }

    public void createNotificationChannelForOreo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            String id = "id_doctor_network";
            // The user-visible name of the channel.
            CharSequence name = "App Updates";
            // The user-visible description of the channel.
            String description = "Notifications regarding our app";
            int importance = NotificationManager.IMPORTANCE_MAX;
            NotificationChannel mChannel = new NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(mChannel);
            }
        }
    }
}
