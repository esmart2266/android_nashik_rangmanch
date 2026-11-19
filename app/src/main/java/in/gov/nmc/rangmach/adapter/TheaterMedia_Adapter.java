package in.gov.nmc.rangmach.adapter;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.VideoPlayer_Activity;
import in.gov.nmc.rangmach.databinding.RowTheaterMediaBinding;
import in.gov.nmc.rangmach.model.Theater_Media;

public class TheaterMedia_Adapter extends RecyclerView.Adapter<TheaterMedia_Adapter.ViewHolder> {
    Context mContext;
    List<Theater_Media> mediadata;

    public TheaterMedia_Adapter(Context mContext, List<Theater_Media> videos) {
        this.mContext = mContext;
        this.mediadata = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_theater_media, parent, false);
//        return new ViewHolder(view);

        RowTheaterMediaBinding binding = RowTheaterMediaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return mediadata.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Theater_Media media = mediadata.get(i);

        // make imageview roundcorners
        int radius = 20; // radius in pixels
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(new float[]{radius, radius, radius, radius, radius, radius, radius, radius}, null, null));
        viewHolder.binding.ivMedia.setBackground(shapeDrawable);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.binding.ivMedia.setClipToOutline(true);
        }
        //make imageview roundcorners end

        if (media.getType().equals("Image")) {
            if (media.getUrl() != null && !TextUtils.isEmpty(media.getUrl())) {

                Glide.with(mContext).load(media.getUrl()).error(R.drawable.no_image).thumbnail(0.15f).into(viewHolder.binding.ivMedia);

                viewHolder.binding.ivMedia.setOnClickListener(v -> {
                    Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                    intent.putExtra("title", "Preview");
                    intent.putExtra("urlPhotoClick", media.getUrl());
                    mContext.startActivity(intent);
                });
            }
        } else if (media.getType().equals("Youtube")) {
            Glide.with(mContext).load("http://img.youtube.com/vi/" + media.getUrl() + "/0.jpg").error(R.drawable.no_image).into(viewHolder.binding.ivMedia);
            viewHolder.binding.ivMedia.setOnClickListener(v -> {
                String url = media.getUrl();

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    // youtube is not installed.Will be opened in other available apps
                    Intent ijj = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + url));
                    mContext.startActivity(ijj);
                }

            });
        } else {
            Glide.with(mContext).load(media.getUrl()).error(R.drawable.no_image).thumbnail(0.15f).into(viewHolder.binding.ivMedia);
            String videourl = media.getUrl();
            viewHolder.binding.ivMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, VideoPlayer_Activity.class);
                    intent.putExtra("keyName", videourl);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    private void showAdvertisementDialog(String imageUrl, String type) {
        Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_media);

        ImageView adImage = dialog.findViewById(R.id.ivAdImage);
        ImageView closeBtn = dialog.findViewById(R.id.ivClose);

        YouTubePlayerView youTubePlayerView = dialog.findViewById(R.id.youtube_player_view);

        PlayerView videoView = dialog.findViewById(R.id.videoView);

        SimpleExoPlayer player = new SimpleExoPlayer.Builder(mContext).build();

        // Track YouTube player instance
        final YouTubePlayer[] activeYouTubePlayer = new YouTubePlayer[1];

        if (type.equals("Image")) {
            adImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(imageUrl).into(adImage);

        } else if (type.equals("Youtube")) {
            youTubePlayerView.setVisibility(View.VISIBLE);

            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(YouTubePlayer youTubePlayer) {
                    activeYouTubePlayer[0] = youTubePlayer;

                    youTubePlayer.loadVideo(imageUrl, 0);
//                youTubePlayer.loadVideo("ZqPEIMpGAHU", 0);
                }
            });
        } else if (type.equals("Video")) {
            videoView.setVisibility(View.VISIBLE);

            videoView.setPlayer(player);
            MediaItem mediaItem = MediaItem.fromUri(imageUrl);
            player.setMediaItem(mediaItem);

            // Prepare and start the playback
            player.prepare();
            player.play();
        }

        closeBtn.setOnClickListener(v -> {
            // Stop and release resources
            dialog.dismiss();
            if (player != null) {
                player.stop();
                player.release();
            }
        });


        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.iv_media)
//        AppCompatImageView iv_media;
//
//        View view;

        RowTheaterMediaBinding binding;

        ViewHolder(RowTheaterMediaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }

}