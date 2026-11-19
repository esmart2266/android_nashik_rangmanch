package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import in.gov.nmc.rangmach.EventDeatils_Activity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowShowsBinding;
import in.gov.nmc.rangmach.model.NowShowing;

public class AllEventsAdapter extends RecyclerView.Adapter<AllEventsAdapter.ViewHolder> {
    Context mContext;
    List<NowShowing> nowShowingList;

    public AllEventsAdapter(Context mContext, List<NowShowing> videos) {
        this.mContext = mContext;
        this.nowShowingList = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_shows, parent, false);
//        return new ViewHolder(view);

        RowShowsBinding binding = RowShowsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    public void addData(List<NowShowing> list) {
        this.nowShowingList.clear();
        this.nowShowingList.addAll(list);
    }

    @Override
    public int getItemCount() {
        if (nowShowingList == null)
            return 0;

        return nowShowingList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final NowShowing item = nowShowingList.get(i);

        //Date formating
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(item.getStartDate());
            Date d2 = sdf.parse(item.getEndDate());

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM");
            String start = sdf2.format(d1);
            String end = sdf2.format(d2);
            if (start.equals(end)) {
                viewHolder.binding.showdate.setText(start);
            } else {
                viewHolder.binding.showdate.setText(start + " - " + end);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        viewHolder.binding.tvLikecount.setText(item.getLikeCount());
        viewHolder.binding.Showtitle.setText(item.getTitle());
        viewHolder.binding.btnShowLanguage.setText(item.getLanguage());
        viewHolder.binding.btnShowType.setText(item.getCategory());


        if (!TextUtils.isEmpty(item.getImage())) {
//            Glide.with(mContext).load(item.getImage())
//                    .skipMemoryCache(true)
//                    .error(R.drawable.no_image)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(viewHolder.binding.imageView);

            Glide.with(mContext)
                    .load(item.getImage())
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.no_image)
                    .into(viewHolder.binding.imageView);


//            viewHolder.image_view.setOnClickListener(view -> {
//                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
//                intent.putExtra("title", "Preview");
//                intent.putExtra("urlPhotoClick", item.getImage());
//                mContext.startActivity(intent);
//            });
        }

        viewHolder.binding.cardView.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, EventDeatils_Activity.class)
                    .putExtra("eventdata", nowShowingList.get(viewHolder.getAdapterPosition())));
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.image_view)
//        ImageView image_view;
//        @BindView(R.id.showdate)
//        TextView showdate;
//        @BindView(R.id.tv_likecount)
//        TextView tv_likecount;
//        @BindView(R.id.Showtitle)
//        TextView Showtitle;
//        @BindView(R.id.btn_show_type)
//        Button btn_show_type;
//        @BindView(R.id.btn_show_language)
//        Button btn_show_language;
//        @BindView(R.id.card_view)
//        CardView card_view;
//        View view;

        RowShowsBinding binding;

        ViewHolder(RowShowsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
