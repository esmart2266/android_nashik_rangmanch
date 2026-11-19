package in.gov.nmc.rangmach.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.VideoPlayer_Activity;
import in.gov.nmc.rangmach.model.NowShowing_Media;


public class EventsMediaPager_Adapter extends PagerAdapter {
    private final int widthPixels;
    private final float scale;
    Context mContext;
    LayoutInflater mLayoutInflater;
    List<NowShowing_Media> advertisements;

    public EventsMediaPager_Adapter(Context mContext, List<NowShowing_Media> advertisements) {
        this.mContext = mContext;
        this.advertisements = advertisements;
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        widthPixels = displayMetrics.widthPixels;
        scale = displayMetrics.density;
    }

    @Override
    public int getCount() {
        if (advertisements == null)
            return 0;

        return advertisements.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = mLayoutInflater.inflate(R.layout.viewpager_image, container, false);
        ImageView iv_banner = itemView.findViewById(R.id.iv_banner);
        ImageView iv_overlay = itemView.findViewById(R.id.iv_overlay);
        TextView tvAdName = itemView.findViewById(R.id.tvAdName);

        if (advertisements != null && !advertisements.isEmpty()) {
            NowShowing_Media advertisement = advertisements.get(position);

            tvAdName.setVisibility(View.VISIBLE);

            if (advertisement.getUrl() != null) {
                if (advertisement.getType().equals("Image")) {
                    if (advertisement.getUrl() != null && !TextUtils.isEmpty(advertisement.getUrl())) {
                        Glide.with(mContext).load(advertisement.getUrl()).thumbnail(0.15f).into(iv_banner);
                        iv_banner.setOnClickListener(v -> {
                            Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                            intent.putExtra("title", "Preview");
                            intent.putExtra("urlPhotoClick", advertisement.getUrl());
                            mContext.startActivity(intent);
                        });
                    }
                } else if (advertisement.getType().equals("Youtube")) {
                    iv_overlay.setImageResource(R.drawable.youtube);

                    String imgurl = "https://img.youtube.com/vi/" + advertisement.getUrl() + "/0.jpg";
//                    Glide.with(mContext).load(imgurl).into(iv_banner);

                    Picasso.get()
                            .load(imgurl)
                            .error(R.drawable.no_image)
                            .into(iv_banner);

                    iv_banner.setOnClickListener(v -> {
                        String url = advertisement.getUrl();
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // youtube is not installed.Will be opened in other available apps
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + url));
                            mContext.startActivity(i);
                        }
                    });
                } else {

                    iv_overlay.setImageResource(R.drawable.videopreview);

                    Glide.with(mContext).load(advertisement.getUrl()).thumbnail(0.15f).into(iv_banner);
                    String videourl = advertisement.getUrl();
                    iv_banner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(mContext, VideoPlayer_Activity.class);
                            intent.putExtra("keyName", videourl);
                            mContext.startActivity(intent);
                        }
                    });
                }
            }
            container.addView(itemView);
        }
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

}