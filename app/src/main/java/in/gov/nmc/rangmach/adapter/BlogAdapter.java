package in.gov.nmc.rangmach.adapter;

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
import in.gov.nmc.rangmach.model.Blog;
import in.gov.nmc.rangmach.utils.Utils;

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    Context mContext;
    List<Blog> videos;

    public BlogAdapter(Context mContext, List<Blog> videos) {
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
        final Blog item = videos.get(i);
//        viewHolder.tvDrugName.setText(Html.fromHtml("<font color='#333333'>Drug Name : </font><font color='#666666'>" + Utils.checkNotEmpty(prescription.getName()) + "</font>"));
//        viewHolder.tvSchedule.setText(Html.fromHtml("<font color='#333333'>Schedule : </font><font color='#666666'>" + Utils.checkNotEmpty(prescription.getMorning()) + "-" +
//                Utils.checkNotEmpty(prescription.getNoon()) + "-" + Utils.checkNotEmpty(prescription.getNight()) + "</font>"));
        viewHolder.binding.tvTitle.setText(Html.fromHtml("<font color='#333333'></font><font color='#333333'>" + Utils.checkNotEmpty(item.getTitle()) + "</font>"));
        viewHolder.binding.tvDetails.setText(Html.fromHtml("<font color='#333333'></font><font color='#666666'>" + Utils.checkNotEmpty(item.getDetails()) + "</font>"));
        if (!TextUtils.isEmpty(item.getPhotolink())) {
            Glide.with(mContext).load(item.getPhotolink())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(viewHolder.binding.ivPhoto);
            viewHolder.binding.ivPhoto.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtra("title", "Preview");
                intent.putExtra("urlPhotoClick", item.getPhotolink());
                mContext.startActivity(intent);
            });
        }

        viewHolder.getView().setOnClickListener(view -> {
            String url = item.getUrl();
            if (!TextUtils.isEmpty(url)) {
                String uri = url;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                mContext.startActivity(intent);
            }
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=Hxy8BZGQ5Jo")));
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.ivPhoto)
//        ImageView ivPhoto;
//        @BindView(R.id.tv_title)
//        TextView tvTitle;
//        @BindView(R.id.tv_details)
//        TextView tvDetails;
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
