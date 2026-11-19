package in.gov.nmc.rangmach;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Scroller;
import android.widget.Toast;

import java.lang.reflect.Field;

import in.gov.nmc.rangmach.adapter.HallDetails_Adapter;
import in.gov.nmc.rangmach.adapter.TheaterMediaPager_Adapter;
import in.gov.nmc.rangmach.databinding.ActivityTheaterDetailsBinding;
import in.gov.nmc.rangmach.model.AllTheaters;
import in.gov.nmc.rangmach.utils.Utils;

public class TheaterDetails_Activity extends BaseActivity<ActivityTheaterDetailsBinding> {

    AllTheaters allTheaters;
    private boolean isExpanded = false;
    private static final int MESSAGE_SCROLL = 123;

    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_theater_details;
//    }

    protected ActivityTheaterDetailsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityTheaterDetailsBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(TheaterDetails_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(TheaterDetails_Activity.this, "mr");
        }
        binding.btnViewmore.setText(GetTranslation("Read More", typeOfLanguage));
        binding.aboutText.setText(GetTranslation("About", typeOfLanguage));
        binding.asstesHallsText.setText(GetTranslation("Assets/Halls", typeOfLanguage));

        allTheaters = (AllTheaters) getIntent().getSerializableExtra("data");
        binding.toolbarTitle.setText(allTheaters.getName());
        binding.tvaboutDetails.setMaxLines(2);

        if (allTheaters.getMedia() != null) {

            TheaterMediaPager_Adapter theaterMediaPager_adapter = new TheaterMediaPager_Adapter(mContext, allTheaters.getMedia());
            binding.viewpager.setAdapter(theaterMediaPager_adapter);

            if (allTheaters.getMedia().size() > 1) {
                set_slider_animation();
            } else {
                try {
                    stopAutoPlay();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else {
            binding.viewpager.setVisibility(View.GONE);
        }

        if (allTheaters.getHalls() != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.recyclerView.setLayoutManager(layoutManager);
            String termspolicy = allTheaters.getPolicy_terms();
            binding.recyclerView.setAdapter(new HallDetails_Adapter(mContext, termspolicy, allTheaters.getHalls(), allTheaters.getName(), allTheaters.getTheaterHolidays()));
//        nowShowing = new NowShowing();
        }
        setData();
    }

    public void setData() {

        if (allTheaters != null) {

            binding.tvLocation.setText(Utils.checkNotEmpty(allTheaters.getAddress()));
            binding.tvaboutDetails.setText(Utils.checkNotEmpty(allTheaters.getDescription()));

            binding.btnViewmore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (binding.tvaboutDetails.getMaxLines() == 2) {
                        binding.tvaboutDetails.setMaxLines(Integer.MAX_VALUE);
                        binding.btnViewmore.setText(GetTranslation("Read Less", typeOfLanguage));
                    } else {
                        // If text view is expanded, truncate with ellipsis
                        binding.tvaboutDetails.setMaxLines(2);
                        binding.btnViewmore.setText(GetTranslation("Read More", typeOfLanguage));
                    }
                }
            });

            binding.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AddressDialog();
                }
            });
        }
    }

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    //address
    public void AddressDialog() {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.address_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        Button btnSave = dialog.findViewById(R.id.btnGoogleMap);
        AppCompatTextView tv_address = dialog.findViewById(R.id.tv_address);
        AppCompatImageView iv_copy = dialog.findViewById(R.id.iv_copy);

        tv_address.setText(Utils.checkNotEmpty(allTheaters.getAddress()));

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = tv_address.getText().toString();
                copyToClipboard(textToCopy);
                Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        if (allTheaters.getGoogleMap().isEmpty()) {
            showToast(mContext, "Google Map Not Available", Toast.LENGTH_SHORT);
            return;
        } else {

        }

        btnSave.setOnClickListener(view -> {
            String gmap = allTheaters.getGoogleMap();
            if (gmap != null) {
//                String uri = "http://maps.google.co.in/maps?q=" + allTheaters.getGoogleMap().toString();
                String uri = allTheaters.getGoogleMap().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            } else {

            }
        });

        dialog.show();
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void set_slider_animation() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            int animationDuration = 1000;
            FixedSpeedScroller scroller = new FixedSpeedScroller(binding.viewpager.getContext(), new AccelerateDecelerateInterpolator(), animationDuration);
            mScroller.set(binding.viewpager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        if (binding.viewpager != null && binding.viewpager.getAdapter() != null && binding.viewpager.getAdapter().getCount() > 1)
            startAutoPlay();
        else
            stopAutoPlay();
        binding.viewpager.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    stopAutoPlay();
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (binding.viewpager != null && binding.viewpager.getAdapter() != null && binding.viewpager.getAdapter().getCount() > 1)
                        startAutoPlay();
                    else
                        stopAutoPlay();
                    break;

            }
            return false;
        });
    }

    public class FixedSpeedScroller extends Scroller {
        int duration;

        public FixedSpeedScroller(Context context, int duration) {
            super(context);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, int duration) {
            super(context, interpolator);
            this.duration = duration;
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel, int duration) {
            super(context, interpolator, flywheel);
            this.duration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, this.duration);
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_SCROLL) {
                if (binding.viewpager != null) {
                    binding.viewpager.setCurrentItem(binding.viewpager.getCurrentItem() + 1);
                    startAutoPlay();
                }
            }
        }
    };

    public void stopAutoPlay() {
        handler.removeMessages(MESSAGE_SCROLL);
    }

    public void startAutoPlay() {
        stopAutoPlay();
        int homeColumnScrollInterval = 5;
        handler.sendEmptyMessageDelayed(MESSAGE_SCROLL, homeColumnScrollInterval * 1000);
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