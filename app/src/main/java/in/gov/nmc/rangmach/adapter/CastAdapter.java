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

import java.util.List;

import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowCastBinding;
import in.gov.nmc.rangmach.model.NowShowing_Actor;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    Context mContext;
    List<NowShowing_Actor> nowShowingList;

    public CastAdapter(Context mContext, List<NowShowing_Actor> videos) {
        this.mContext = mContext;
        this.nowShowingList = videos;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_cast, parent, false);
//        return new ViewHolder(view);

        RowCastBinding binding = RowCastBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return nowShowingList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final NowShowing_Actor item = nowShowingList.get(i);

        viewHolder.binding.tvCastname.setText(item.getName());

        if (!TextUtils.isEmpty(item.getImage())) {
//            Glide.with(mContext).load(item.getImage())
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.NONE)
//                    .into(viewHolder.iv_cast);
            Glide.with(mContext).load(item.getImage())
                    .placeholder(R.drawable.placeholder)
                    .thumbnail(0.1f)
                    .error(R.drawable.drama)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(viewHolder.binding.ivCast);

            viewHolder.binding.ivCast.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtra("title", "Preview");
                intent.putExtra("urlPhotoClick", item.getImage());
                mContext.startActivity(intent);
            });
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
//     @BindView(R.id.iv_cast)
//     CircleImageView iv_cast;
//     @BindView(R.id.tv_castname)
//     AppCompatTextView tv_castname;
//     View view;

        RowCastBinding binding;

        ViewHolder(RowCastBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
