package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.UploadDocuments_Activity;
import in.gov.nmc.rangmach.databinding.RowUploadDocumentsBinding;
import in.gov.nmc.rangmach.model.DocumentsPOJO;

public class DocumentUpload_Adapter extends RecyclerView.Adapter<DocumentUpload_Adapter.ViewHolder> {
    Context mContext;
    List<DocumentsPOJO> myAssetsOrders;
    String BookId;

    public DocumentUpload_Adapter(Context mContext, List<DocumentsPOJO> myAssetsOrders, String bookId) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
        this.BookId = bookId;
    }

    @Override
    public DocumentUpload_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_documents, parent, false);
//        return new DocumentUpload_Adapter.ViewHolder(view);

        RowUploadDocumentsBinding binding = RowUploadDocumentsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {

        if (myAssetsOrders == null)
            return 0;

        return myAssetsOrders.size();
    }

    @Override
    public void onBindViewHolder(final DocumentUpload_Adapter.ViewHolder viewHolder, final int i) {
        final DocumentsPOJO item = myAssetsOrders.get(i);

        viewHolder.binding.tvTitle.setText(item.getTitle());

        String remarks = item.getRemarks();
        if (remarks != null && !remarks.isEmpty()) {
            viewHolder.binding.tvRemarks.setText(remarks);
        } else {
            viewHolder.binding.tvRemarks.setText("NA");
        }

        if (item.getIsApproved() != null && item.getIsApproved().contains("1")) {
            viewHolder.binding.tvStatus.setText("Approved");
            viewHolder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            viewHolder.binding.tvStatus.setText("Not-Approved");
            viewHolder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }

//        Glide.with(mContext).load(item.getPath())
//                .error(R.drawable.no_image)
//                .thumbnail(0.15f)
//                .into(viewHolder.binding.ivimage);

//        String image = "http://smartleaders.in/pmc-psrs/photos/bookingdocumentphotos/1759555357_1_1759555353734.jpg";

        Glide.with(mContext).load(item.getPath())
                .placeholder(R.drawable.no_image)
                .thumbnail(0.1f)
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .into(viewHolder.binding.ivimage);

        viewHolder.binding.ivimage.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FullScreenImageActivity.class);
            intent.putExtra("title", "Preview");
            intent.putExtra("urlPhotoClick", item.getPath());
            mContext.startActivity(intent);
        });

//      viewHolder.ivupload.setOnClickListener(view -> ((UploadDocuments_Activity) mContext).openfile(BookId,item.getId()));

        viewHolder.binding.ivupload.setOnClickListener(view -> ((UploadDocuments_Activity) mContext).openfile(viewHolder.getAdapterPosition(), BookId, item.getId()));
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tvTitle)
//        AppCompatTextView tvTitle;
//        @BindView(R.id.ivimage)
//        AppCompatImageView ivimage;
//        @BindView(R.id.ivupload)
//        AppCompatImageView ivupload;
//
//
//        View view;

        RowUploadDocumentsBinding binding;

        ViewHolder(RowUploadDocumentsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}



