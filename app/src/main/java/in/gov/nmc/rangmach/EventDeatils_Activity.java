package in.gov.nmc.rangmach;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.gov.nmc.rangmach.adapter.CastAdapter;
import in.gov.nmc.rangmach.adapter.EventsMediaPager_Adapter;
import in.gov.nmc.rangmach.databinding.ActivityEventDeatilsBinding;
import in.gov.nmc.rangmach.model.NowShowing;
import in.gov.nmc.rangmach.model.NowShowing_Timing;
import in.gov.nmc.rangmach.utils.Utils;

public class EventDeatils_Activity extends BaseActivity<ActivityEventDeatilsBinding> {

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.tv_subtitle)
//    AppCompatTextView tv_subtitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.viewpager)
//    LoopViewPager viewpager;
//    @BindView(R.id.ivevnt_date)
//    AppCompatTextView ivevnt_date;
//    @BindView(R.id.tvTime)
//    AppCompatTextView tvTime;
//    @BindView(R.id.tvLocation)
//    AppCompatTextView tvLocation;
//    @BindView(R.id.tvabout_details)
//    AppCompatTextView tvabout_details;
//    @BindView(R.id.tv_note)
//    AppCompatTextView tv_note;
//    @BindView(R.id.tv_likecount)
//    AppCompatTextView tv_likecount;
//    @BindView(R.id.tvhours)
//    AppCompatTextView tvhours;
//    @BindView(R.id.rv_cast_recyclerView)
//    RecyclerView rv_cast_recyclerView;
//    @BindView(R.id.btn_category)
//    AppCompatButton btn_category;
//    @BindView(R.id.btn_age)
//    AppCompatButton btn_age;
//    @BindView(R.id.btn_language)
//    AppCompatButton btn_language;
//    @BindView(R.id.btn_viewmore)
//    AppCompatTextView btn_viewmore;
//    @BindView(R.id.iv_share)
//    AppCompatImageView iv_share;
//
//    @BindView(R.id.about_text)
//    AppCompatTextView about_text;
//    @BindView(R.id.cast_text)
//    AppCompatTextView cast_text;

    private String slotID = "";
    private String selectedDate = "";

    public static int typeOfLanguage;

//    @BindView(R.id.llBottom)
//    RelativeLayout llBottom;

    NowShowing nowShowing;
    private boolean isExpanded = false;
    private static final int MESSAGE_SCROLL = 123;
    String timstr = "";
    String start;
    String end;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_event_deatils;
//    }

    protected ActivityEventDeatilsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityEventDeatilsBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        nowShowing = (NowShowing) getIntent().getSerializableExtra("eventdata");
        binding.toolbarTitle.setText(nowShowing.getTitle());
        binding.tvSubtitle.setText(nowShowing.getSubtitle());

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

//        if (selected_lang.equals("Hindi")) {
//            typeOfLanguage = 2;
//            setLocal(EventDeatils_Activity.this, "hi");
//        } else if (selected_lang.equals("Marathi")) {
//            typeOfLanguage = 1;
//            setLocal(EventDeatils_Activity.this, "mr");
//        }
        binding.btnViewmore.setText(GetTranslation("Read More", typeOfLanguage));
        binding.aboutText.setText(GetTranslation("About", typeOfLanguage));
        binding.castText.setText(GetTranslation("Cast", typeOfLanguage));

        binding.tvaboutDetails.setMaxLines(2);

        if (nowShowing.getMedia() != null && !nowShowing.getMedia().isEmpty()) {

            EventsMediaPager_Adapter eventsMediaPager_adapter = new EventsMediaPager_Adapter(mContext, nowShowing.getMedia());
            binding.viewpager.setAdapter(eventsMediaPager_adapter);

            if (nowShowing.getMedia().size() > 1) {
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

        if (nowShowing.getActor() != null && !nowShowing.getActor().isEmpty()) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            binding.rvCastRecyclerView.setLayoutManager(layoutManager);
            binding.rvCastRecyclerView.setAdapter(new CastAdapter(mContext, nowShowing.getActor()));
//        nowShowing = new NowShowing();
        }

//        llBottom.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(EventDeatils_Activity.this, TicketBookingActivity.class);
//                String centerId = nowShowing.getCenterId();
//                intent.putExtra("centerid", centerId);
////                intent.putExtra("centername", nowShowing.getName());
//                startActivity(intent);
//            }
//        });

        binding.llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Seat_Booking_Dialog();
            }
        });

        binding.ivShareDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String shareContent = "Checkout Nashik's Official App for Cultural Events\n\n" +
                        mContext.getResources().getString(R.string.app_name) +
                        "\nTitle : " + nowShowing.getTitle() +
                        "\nCategory : " + nowShowing.getCategory() +
                        "\nLanguage : " + nowShowing.getLanguage() +
                        "\nAge Group : " + nowShowing.getAgeGroup() +
                        "\nDate : " + start + " to " + end +
                        "\nTiming : " + timstr +
                        "\nDuration : " + nowShowing.getDuration() +
                        "\nAddress : " + nowShowing.getAddress() +
                        "\n\nDownload now" +
                        "\nAndroid : https://play.google.com/store/apps/details?id=in.gov.pmc.rangmach" +
                        "\niOS : https://apps.apple.com/in/app/pmc-sccbs/id6714465033";
//                    "\nPost By: " + caseData1.getPostBy();
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
                mContext.startActivity(Intent.createChooser(waIntent, "Share with"));
            }
        });

        setData();
    }

    public void Seat_Booking_Dialog() {
        Dialog dialog1 = Utils.showCustomAlertDialog(mContext, R.layout.seat_booking_custum_dialog);
        Button btnClose = dialog1.findViewById(R.id.btnClosing);
        Button btnSave = dialog1.findViewById(R.id.btnAdd);
        LinearLayout ll_selectdate = dialog1.findViewById(R.id.ll_selectdate);
        LinearLayout ll_selecttime = dialog1.findViewById(R.id.ll_selecttime);
        MaterialEditText et_time = dialog1.findViewById(R.id.et_time);
        MaterialEditText et_date = dialog1.findViewById(R.id.et_date);

        if (nowShowing.getTimings() != null && !nowShowing.getTimings().isEmpty()) {

            //Select Time
            ArrayList<NowShowing_Timing> list = (ArrayList<NowShowing_Timing>) nowShowing.getTimings();
            ArrayList<String> timelist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                timelist.add(list.get(i).getTiming());
            }
            if (timelist.size() > 0) {
                et_time.setOnClickListener(view -> {
                    new MaterialDialog.Builder(mContext)
                            .title("Select Time")
                            .items(timelist)
                            .itemsCallback((dialog, view1, which, text) -> {
                                slotID = list.get(which).getId();
                                et_time.setText(text.toString());
                            })
                            .show();
                });
                et_time.setVisibility(View.VISIBLE);
            }
        }

        //Select Date
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    String date = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;
                    selectedDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    et_date.setText(date);

                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date currentDate = new Date();


                    Date startDate = sdf.parse(nowShowing.getStartDate());
                    Calendar tempStart = Calendar.getInstance();
                    //if current date is behinde start date... the no issue
                    //else stop previous date selection

                    if (currentDate.compareTo(startDate) > 0) {
                        startDate = currentDate;
                    }
                    tempStart.setTime(startDate);
                    dpd.setMinDate(tempStart);

                    Date endDate = sdf.parse(nowShowing.getEndDate());
                    Calendar tempEnd = Calendar.getInstance();
                    tempEnd.setTime(endDate);
                    dpd.setMaxDate(tempEnd);
                } catch (Exception e) {

                }

                dpd.setTitle("Select Date");
                dpd.show(getSupportFragmentManager(), "StartDatepickerdialog");
            }
        });

        btnClose.setOnClickListener(view1 -> dialog1.dismiss());

        btnSave.setOnClickListener(view1 -> {

            if (TextUtils.isEmpty(selectedDate)) {
                et_date.setError("Please Select Date");
                et_date.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(slotID)) {
                et_time.setError("Please Select Time");
                et_time.requestFocus();
                return;
            }

            Intent intent = new Intent(EventDeatils_Activity.this, TicketBookingActivity.class);
            String centerHallId = nowShowing.getCenter_hall_id();
            intent.putExtra("centerHallid", centerHallId);
            intent.putExtra("centername", nowShowing.getName());
            intent.putExtra("slotID", slotID);
            intent.putExtra("et_time", et_time.getText().toString());
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("eventID", nowShowing.getId());
            startActivity(intent);

            dialog1.dismiss();
        });

        dialog1.show();
    }

    public void setData() {

        if (nowShowing != null) {

            //convert date format
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date d1 = sdf.parse(nowShowing.getStartDate());
                Date d2 = sdf.parse(nowShowing.getEndDate());

                SimpleDateFormat sdf2 = new SimpleDateFormat("EEE dd MMM");
                start = sdf2.format(d1);
                end = sdf2.format(d2);
                if (start.equals(end))
                {
                    binding.ivevntDate.setText(start);
                }
                else
                {
                    binding.ivevntDate.setText(start + " - " + end);
                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
            //time add

            if (nowShowing.getTimings() != null && !nowShowing.getTimings().isEmpty()) {

                for (NowShowing_Timing time : nowShowing.getTimings()) {
                    if (timstr.length() == 0) {
                        timstr = time.getTiming();
                    } else {
                        timstr += ", " + time.getTiming();
                    }
                }

                binding.tvTime.setText(timstr);

            } else {
                binding.tvTime.setText("Time Not Added");
            }

            binding.tvLocation.setText(Utils.checkNotEmpty(nowShowing.getName()));
            binding.tvaboutDetails.setText(Utils.checkNotEmpty(nowShowing.getAboutDetails()));
            binding.tvNote.setText("Note : " + Utils.checkNotEmpty(nowShowing.getNote()));
            binding.tvLikecount.setText(Utils.checkNotEmpty(nowShowing.getLikeCount()));
            binding.tvhours.setText(Utils.checkNotEmpty(nowShowing.getDuration()));
            binding.btnCategory.setText(Utils.checkNotEmpty(nowShowing.getCategory()));
            binding.btnLanguage.setText(Utils.checkNotEmpty(nowShowing.getLanguage()));
            binding.btnAge.setText(Utils.checkNotEmpty(nowShowing.getAgeGroup()));

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

//            iv_share.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    openGoogleMaps();
//                }
//            });
        }
    }

    //address dialog
    public void AddressDialog() {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.address_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        Button btnSave = dialog.findViewById(R.id.btnGoogleMap);
        AppCompatTextView tv_address = dialog.findViewById(R.id.tv_address);
        AppCompatImageView iv_copy = dialog.findViewById(R.id.iv_copy);

        tv_address.setText(Utils.checkNotEmpty(nowShowing.getAddress()));

        iv_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToCopy = tv_address.getText().toString();
                copyToClipboard(textToCopy);
                Toast.makeText(getApplicationContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });


        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            String gmap = nowShowing.getGoogle_map();
            if (gmap != null) {
//                String uri = "http://maps.google.co.in/maps?q=" + nowShowing.getGoogle_map().toString();
                String uri = nowShowing.getGoogle_map().toString();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        dialog.show();
    }

//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        MenuItem item1 = menu.findItem(R.id.action_share);
        MenuItem item2 = menu.findItem(R.id.action_filter);
        item1.setVisible(false);
        item2.setVisible(false);
        this.invalidateOptionsMenu();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        int id = item.getItemId();
        if (id == android.R.id.home) {
            Utils.hideSoftKeyboard(this);
            finish();
            return true;
        } else if (id == R.id.action_share) {
            String shareContent = "Checkout Nashik's Official App for Cultural Events\n\n" +
                    mContext.getResources().getString(R.string.app_name) +
                    "Show : " + nowShowing.getTitle() +
                    "Language : " + nowShowing.getLanguage() +
                    "Date : " + start + " to " + end +
                    "Timing : " + timstr +
                    "Category : " + nowShowing.getCategory() +
                    "Duration : " + nowShowing.getDuration() +
                    "Age Group : " + nowShowing.getAgeGroup() +
                    "Address : " + nowShowing.getAddress() +
                    "Android : https://play.google.com/store/apps/details?id=in.gov.pmc.rangmach" +
                    "iOS : https://apps.apple.com/in/app/pmc-sccbs/id6714465033";
//                    "\nPost By: " + caseData1.getPostBy();
            Intent waIntent = new Intent(Intent.ACTION_SEND);
            waIntent.setType("text/plain");
            waIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
            mContext.startActivity(Intent.createChooser(waIntent, "Share with"));
            return true;
        }


//        switch (item.getItemId()) {
//            case android.R.id.home: {
//                Utils.hideSoftKeyboard(this);
//                finish();
//                return true;
//            }
//            case R.id.action_share: {
//
//                String shareContent = mContext.getResources().getString(R.string.app_name) +
//                        "\n\nTitle : " + nowShowing.getTitle() +
//                        "\n\nCategory : " + nowShowing.getCategory() +
//                        "\n\nLanguage : " + nowShowing.getLanguage() +
//                        "\n\nAge Group : " + nowShowing.getAgeGroup() +
//                        "\n\nDate : " + start + " to " + end +
//                        "\n\nTiming : " + timstr +
//                        "\n\nDuration : " + nowShowing.getDuration() +
//                        "\n\nAddress : " + nowShowing.getAddress() +
//                        "\n\nhttps://play.google.com/store/apps/details?id=in.gov.pmc.psrs";
////                    "\nPost By: " + caseData1.getPostBy();
//                Intent waIntent = new Intent(Intent.ACTION_SEND);
//                waIntent.setType("text/plain");
//                waIntent.putExtra(Intent.EXTRA_TEXT, shareContent);
//                mContext.startActivity(Intent.createChooser(waIntent, "Share with"));
//
//            }
//        }
        return super.onOptionsItemSelected(item);
    }

}