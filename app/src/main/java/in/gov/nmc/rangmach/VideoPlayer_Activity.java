package in.gov.nmc.rangmach;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

import in.gov.nmc.rangmach.databinding.ActivityVideoPlayerBinding;
import in.gov.nmc.rangmach.utils.Utils;

public class VideoPlayer_Activity extends AppCompatActivity {
    String rot = "1";

    SimpleExoPlayer player;

    private ActivityVideoPlayerBinding binding;
    // Your Video URL
//    String videoUrl = "https://esmartsolution.in/kelkar_19years.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        // Inflate the layout using ViewBinding
        binding = ActivityVideoPlayerBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setSupportActionBar(binding.toolbarContainer.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        setContentView(R.layout.activity_video_player);

//        ((AppCompatActivity)this).getSupportActionBar().setTitle("Home");

        String videoUrl = getIntent().getExtras().getString("keyName","defaultKey");

        player = new SimpleExoPlayer.Builder(this).build();

        PlayerView videoView = findViewById(R.id.videoView);
        videoView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);

        // Prepare and start the playback
        player.prepare();
        player.play();
//        player.stop();

//        Uri uri = Uri.parse(videoUrl);
//        videoView.setVideoURI(uri);
//        MediaController mediaController = new MediaController(this);
//        mediaController.setAnchorView(videoView);
//        mediaController.setMediaPlayer(videoView);
//        videoView.setMediaController(mediaController);
//        videoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            player.pause();
            player = null;
        }
        catch (Exception e)
        {
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            player.pause();
            player = null;
        }
        catch (Exception e)
        {
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.hideSoftKeyboard(VideoPlayer_Activity.this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}