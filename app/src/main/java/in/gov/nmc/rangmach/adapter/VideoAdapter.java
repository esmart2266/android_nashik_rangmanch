package in.gov.nmc.rangmach.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.databinding.VideoRowBinding;
import in.gov.nmc.rangmach.model.Video;
import in.gov.nmc.rangmach.utils.Utils;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    Context mContext;
    List<Video> videos;

    public VideoAdapter(Context mContext, List<Video> videos) {
        this.mContext = mContext;
        this.videos = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_row, parent, false);
//        return new ViewHolder(view);

        VideoRowBinding binding = VideoRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Video video = videos.get(i);
//        viewHolder.tvDrugName.setText(Html.fromHtml("<font color='#333333'>Drug Name : </font><font color='#666666'>" + Utils.checkNotEmpty(prescription.getName()) + "</font>"));
//        viewHolder.tvSchedule.setText(Html.fromHtml("<font color='#333333'>Schedule : </font><font color='#666666'>" + Utils.checkNotEmpty(prescription.getMorning()) + "-" +
//                Utils.checkNotEmpty(prescription.getNoon()) + "-" + Utils.checkNotEmpty(prescription.getNight()) + "</font>"));
        viewHolder.binding.tvTitle.setText(Html.fromHtml("<font color='#333333'>Title : </font><font color='#666666'>" + Utils.checkNotEmpty(video.getTitle()) + "</font>"));
        viewHolder.binding.tvDetails.setText(Html.fromHtml("<font color='#333333'>Details : </font><font color='#666666'>" + Utils.checkNotEmpty(video.getDetails()) + "</font>"));

        if (!TextUtils.isEmpty(video.getUrl())) {
            String urlOfPhoto = "https://img.youtube.com/vi/"+video.getUrl()+"/0.jpg";
            Glide.with(mContext).load(urlOfPhoto)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.binding.ivPhoto);
            viewHolder.binding.ivPhoto.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtra("title", "Preview");
                intent.putExtra("urlPhotoClick",urlOfPhoto);
                mContext.startActivity(intent);
            });
        }


        viewHolder.getView().setOnClickListener(view -> {
            String url = video.getUrl();
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube://" + url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                // youtube is not installed.Will be opened in other available apps
                Intent i1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://youtube.com/watch?v=" + url));
                mContext.startActivity(i1);
            }
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.tv_title)
//        TextView tvTitle;
//        @BindView(R.id.tv_details)
//        TextView tvDetails;
//        @BindView(R.id.ivPhoto)
//        ImageView ivPhoto;
//        Context mContext;
//        View view;

        VideoRowBinding binding;

        ViewHolder(VideoRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
