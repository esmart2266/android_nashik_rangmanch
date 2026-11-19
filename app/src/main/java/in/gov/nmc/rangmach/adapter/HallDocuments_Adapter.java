package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.gov.nmc.rangmach.HallBooking_Activity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowBookingDocumentsCardBinding;
import in.gov.nmc.rangmach.model.Theater_Booking_Documents;

public class HallDocuments_Adapter extends RecyclerView.Adapter<HallDocuments_Adapter.ViewHolder> {
    Context mContext;
    List<Theater_Booking_Documents> theaterBookingDocuments;
    String currentDocumentId;


    public HallDocuments_Adapter(Context mContext, List<Theater_Booking_Documents> myAssetsOrders) {
        this.mContext = mContext;
        this.theaterBookingDocuments = myAssetsOrders;

    }


    public void ClearData() {
        if (theaterBookingDocuments != null) {
            theaterBookingDocuments.clear();
        }

    }

    public void SetData(List<Theater_Booking_Documents> myAssetsOrders) {
        List<Theater_Booking_Documents> temp = new ArrayList<>();
        temp.addAll(myAssetsOrders);
        this.theaterBookingDocuments = temp;
    }

    @Override
    public HallDocuments_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_upload_documents, parent, false);
//        return new HallDocuments_Adapter.ViewHolder(view);

        RowBookingDocumentsCardBinding binding = RowBookingDocumentsCardBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {

        if (theaterBookingDocuments == null)
            return 0;

        return theaterBookingDocuments.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Theater_Booking_Documents item = theaterBookingDocuments.get(i);

        currentDocumentId = item.getDocument_id();

        viewHolder.binding.tvTitle.setText(item.getTitle());

        String filePath = item.getFilePath();

        if (filePath != null && !filePath.isEmpty()) {

            // Use Uri for all types
            Uri imageUri;
            if (filePath.startsWith("content://") || filePath.startsWith("file://")) {
                imageUri = Uri.parse(filePath);
            } else {
                File file = new File(filePath);
                imageUri = Uri.fromFile(file);
            }

            Glide.with(mContext)
                    .load(imageUri)
                    .error(R.drawable.no_image)
                    .thumbnail(0.15f)
                    .into(viewHolder.binding.ivimage);

//            viewHolder.binding.ivimage.setOnClickListener(v -> showImagePreviewDialog(imageUri));

            Log.e("Adapter", "Loading image: " + filePath);

        } else {
            viewHolder.binding.ivimage.setImageResource(R.drawable.no_image);
            Log.e("Adapter", "No file path for " + item.getDocument_id());
        }

        viewHolder.binding.ivupload.setOnClickListener(view -> ((HallBooking_Activity) mContext).openFile(viewHolder.getAdapterPosition(), item.getDocument_id()));

    }


    private void showImagePreviewDialog(Uri imageUri) {
        if (imageUri == null) return;

        ImageView imageView = new ImageView(mContext);
        imageView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        ));
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        Glide.with(mContext)
                .load(imageUri)
                .error(R.drawable.no_image)
                .into(imageView);

        new androidx.appcompat.app.AlertDialog.Builder(mContext)
                .setView(imageView)
                .setPositiveButton("Close", (dialog, which) -> dialog.dismiss())
                .show();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowBookingDocumentsCardBinding binding;

        ViewHolder(RowBookingDocumentsCardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }


}





