package in.gov.nmc.rangmach;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import in.gov.nmc.rangmach.databinding.ActivityYoutubePlayerBinding;
import in.gov.nmc.rangmach.utils.Utils;

public class YoutubePlayer_Activity extends BaseActivity<ActivityYoutubePlayerBinding> {

    private Toolbar toolbar;
    private TextView toolbarTitle;

    private YouTubePlayerView youTubePlayerView;

    protected ActivityYoutubePlayerBinding inflateBinding(LayoutInflater inflater) {
        return ActivityYoutubePlayerBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        toolbar = binding.toolbar;
        toolbarTitle = binding.toolbarTitle;
//        setContentView(R.layout.activity_youtube);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Typeface typeface = ResourcesCompat.getFont(mContext, R.font.kanitmedium);
        toolbarTitle.setTypeface(typeface);

        Intent intent = getIntent();
        String shorturl = intent.getStringExtra("shorturl");
        String name = intent.getStringExtra("name");
        toolbarTitle.setText(name);

        youTubePlayerView = findViewById(R.id.youtube_player_view);

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(shorturl, 0);
//                youTubePlayer.loadVideo("ZqPEIMpGAHU", 0);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Utils.hideSoftKeyboard(this);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}