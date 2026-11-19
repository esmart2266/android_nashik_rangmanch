package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.HallBooking_Activity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowHallsBinding;
import in.gov.nmc.rangmach.model.Theater_Hall_Timing;
import in.gov.nmc.rangmach.model.Theater_Halls;
import in.gov.nmc.rangmach.model.Theater_Holidays;


public class HallDetails_Adapter extends RecyclerView.Adapter<HallDetails_Adapter.ViewHolder> {
    Context mContext;
    List<Theater_Halls> nowShowingList;
    List<Theater_Holidays> theaterHolidaysList;
    String termspolicy;
    public static int typeOfLanguage;
    String mainHallName;

    public HallDetails_Adapter(Context mContext, String termspolicy, List<Theater_Halls> theaterHalls, String mainHallName, List<Theater_Holidays> theaterHolidaysList) {
        this.mContext = mContext;
        this.nowShowingList = theaterHalls;
        this.termspolicy = termspolicy;
        this.mainHallName = mainHallName;
        this.theaterHolidaysList = theaterHolidaysList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_halls, parent, false);
//        return new ViewHolder(view);

        RowHallsBinding binding = RowHallsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return nowShowingList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Theater_Halls item = nowShowingList.get(i);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(mContext, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(mContext, "mr");
        }

        //langauge
        viewHolder.binding.balconyText.setText(BaseActivity.GetTranslation("Balcony", typeOfLanguage));
        viewHolder.binding.timeslotsText.setText(BaseActivity.GetTranslation("Time Slots", typeOfLanguage));
        viewHolder.binding.groundText.setText(BaseActivity.GetTranslation("Ground", typeOfLanguage));
        viewHolder.binding.btncheckbooking.setText(BaseActivity.GetTranslation("Check Schedule For Booking", typeOfLanguage));

        viewHolder.binding.tvTheatername.setText(item.getTitle());
        viewHolder.binding.tvgroundfloor.setText(item.getGroundFloor());
        viewHolder.binding.tvbalcony.setText(item.getBalcony());

        String timstr = "";
        if (item.getTimings() != null) {

            for (Theater_Hall_Timing time : item.getTimings()) {
                if (timstr.length() == 0) {
                    timstr = time.getSelectedTiming();
                } else {
                    timstr += ", " + time.getSelectedTiming();
                }
            }

            viewHolder.binding.tvtimeslots.setText(timstr);

        } else {
            viewHolder.binding.tvtimeslots.setText("Time Not Added");
        }


        //make imageview roundcorners
        int radius = 20; // radius in pixels
        ShapeDrawable shapeDrawable = new ShapeDrawable(new RoundRectShape(
                new float[]{radius, radius, radius, radius, radius, radius, radius, radius},
                null, null));
        viewHolder.binding.ivtheater.setBackground(shapeDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            viewHolder.binding.ivtheater.setClipToOutline(true);
        }
        //make imageview roundcorners end


        if (!TextUtils.isEmpty(item.getImage())) {

            Glide.with(mContext).load(item.getImage())
                    .placeholder(R.drawable.placeholder)
                    .thumbnail(0.1f)
                    .error(R.drawable.drama)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .skipMemoryCache(true)
                    .into(viewHolder.binding.ivtheater);

            viewHolder.binding.ivtheater.setOnClickListener(view -> {
                Intent intent = new Intent(mContext, FullScreenImageActivity.class);
                intent.putExtra("title", "Preview");
                intent.putExtra("urlPhotoClick", item.getImage());
                mContext.startActivity(intent);

            });
        }

        viewHolder.binding.btncheckbooking.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, HallBooking_Activity.class)
                    .putExtra("termspolocy", termspolicy)
                    .putExtra("mainHallName", mainHallName)
                    .putExtra("holidays", (Serializable) theaterHolidaysList)
                    .putExtra("data", nowShowingList.get(viewHolder.getAdapterPosition())));

        });
    }

//    public void setLocal(Context context, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = context.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.ivtheater)
//        AppCompatImageView ivtheater;
//        @BindView(R.id.tvbalcony)
//        AppCompatTextView tvbalcony;
//
//        @BindView(R.id.tvgroundfloor)
//        AppCompatTextView tvgroundfloor;
//        @BindView(R.id.tvtimeslots)
//        AppCompatTextView tvtimeslots;
//        @BindView(R.id.btncheckbooking)
//        AppCompatTextView btncheckbooking;
//        @BindView(R.id.tv_theatername)
//        AppCompatTextView tv_theatername;
//
//
//        @BindView(R.id.balcony_text)
//        AppCompatTextView balcony_text;
//        @BindView(R.id.ground_text)
//        AppCompatTextView ground_text;
//        @BindView(R.id.timeslots_text)
//        AppCompatTextView timeslots_text;
//
//        View view;

        RowHallsBinding binding;

        ViewHolder(RowHallsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
