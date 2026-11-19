package in.gov.nmc.rangmach;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

import in.gov.nmc.rangmach.model.LanguagePOJO;
import in.gov.nmc.rangmach.utils.ConnectionDetector;
import in.gov.nmc.rangmach.utils.Utils;
import retrofit2.Call;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    public App app;
    public Context mContext;
    public ConnectionDetector cd;
    //    private Unbinder unbinder;
    public List<Call> callList = new ArrayList<Call>();
    private final static String TAG = "BaseActivity";
    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(getActivityLayout());
        binding = inflateBinding(getLayoutInflater());
        setContentView(binding.getRoot());

//        unbinder = ButterKnife.bind(this);
        app = (App) getApplication();
        mContext = this;
        cd = new ConnectionDetector(mContext);
        if (Build.VERSION.SDK_INT >= 23) {   //Android M Or Over
            ask_permissions();
        }
    }

//    protected abstract int getActivityLayout();

    protected abstract T inflateBinding(LayoutInflater inflater);

    public void ask_permissions() {
        ActivityCompat.requestPermissions(BaseActivity.this,
                new String[]{
                        Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.CAMERA
                },
                1);
    }

    public static void ToastDialog(Context context, String message) {
        Dialog dialog = Utils.showCustomAlertDialog(context, R.layout.toast_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        AppCompatTextView tv_address = dialog.findViewById(R.id.tv_msg);

        tv_address.setText(message);
        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }

    public static String GetTranslation(String input, int typeNeeded) {
        //type needed  0-> English    1-> marathi   2-> Hindi
        String translatedStr = input;

        if (LoginActivity.languagePOJOS != null) {
            for (LanguagePOJO obj : LoginActivity.languagePOJOS) {
                if (obj.getKey().equals(input)) {
                    if (typeNeeded == 0) {
                        translatedStr = obj.getEnglish();
                    } else if (typeNeeded == 1) {
                        translatedStr = obj.getMarathi();
                    } else if (typeNeeded == 2) {
                        translatedStr = obj.getHindi();
                    }
                    break;
                }
            }
        }
        return translatedStr;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    displayLocationSettingsRequest(BaseActivity.this);
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {
//permission is denied (and never ask again is  checked)
                    //shouldShowRequestPermissionRationale will return false
//                    Toast.makeText(SplashActivity.this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbinder.unbind();
        if (callList.size() > 0) {
            for (Call call : callList) {
                if (call != null && call.isExecuted())
                    call.cancel();
            }
        }
    }

    public static void showToast(Context context, String message, int duration) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.custom_toast_layout, null);

        TextView text = layout.findViewById(R.id.custom_toast_text);
        text.setText(message);

        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

//    public static void showAdvertisementDialog(String imageUrl, String type) {
//        Dialog dialog = new Dialog(mContext);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setCancelable(false);
//        dialog.setContentView(R.layout.dialog_media);
//
//        ImageView adImage = dialog.findViewById(R.id.ivAdImage);
//        ImageView closeBtn = dialog.findViewById(R.id.ivClose);
//
//        YouTubePlayerView youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);
//
//        PlayerView videoView = dialog.findViewById(R.id.videoView);
//
//        SimpleExoPlayer player = new SimpleExoPlayer.Builder(mContext).build();
//
//        // Track YouTube player instance
//        final YouTubePlayer[] activeYouTubePlayer = new YouTubePlayer[1];
//
//        if (type.equals("Image")) {
//            adImage.setVisibility(View.VISIBLE);
//            Glide.with(mContext).load(imageUrl).into(adImage);
//
//        } else if (type.equals("Youtube")) {
//            youTubePlayerView.setVisibility(View.VISIBLE);
//
//            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
//                @Override
//                public void onReady(YouTubePlayer youTubePlayer) {
//                    activeYouTubePlayer[0] = youTubePlayer;
//
//                    youTubePlayer.loadVideo(imageUrl, 0);
////                youTubePlayer.loadVideo("ZqPEIMpGAHU", 0);
//                }
//            });
//        } else if (type.equals("Video")) {
//            videoView.setVisibility(View.VISIBLE);
//
//            videoView.setPlayer(player);
//            MediaItem mediaItem = MediaItem.fromUri(imageUrl);
//            player.setMediaItem(mediaItem);
//
//            // Prepare and start the playback
//            player.prepare();
//            player.play();
//        }
//
//        closeBtn.setOnClickListener(v -> {
//            // Stop and release resources
//            dialog.dismiss();
//            if (player != null) {
//                player.stop();
//                player.release();
//            }
//        });
//
//
//        dialog.show();
//    }

    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
                if (networkCapabilities != null) {
                    return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
                }
            } else {
                // For devices below API level 23
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null) {
                    return activeNetworkInfo.isConnected();
                }
            }
        }

        return false;
    }

}
