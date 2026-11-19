package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityTicketBookingBinding;
import in.gov.nmc.rangmach.model.BookAssets;
import in.gov.nmc.rangmach.model.Column;
import in.gov.nmc.rangmach.model.LoadSitingData;
import in.gov.nmc.rangmach.model.Row;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import retrofit2.Call;

public class TicketBookingActivity extends BaseActivity<ActivityTicketBookingBinding> implements View.OnClickListener {
    ViewGroup layout;

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.tv_subtitle)
//    AppCompatTextView tv_subtitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
////  @BindView(R.id.scrollView)
////  ScrollView scrollView;
//    @BindView(R.id.btnZoomIn)
//    ImageButton btnZoomIn;
//    @BindView(R.id.btnZoomOut)
//    ImageButton btnZoomOut;
//    @BindView(R.id.btn_booknow)
//    AppCompatButton btn_booknow;
//    @BindView(R.id.container)
//    FrameLayout container;
//
//    @BindView(R.id.tv_show_date)
//    TextView tv_show_date;
//    @BindView(R.id.tv_show_time)
//    TextView tv_show_time;
//    @BindView(R.id.tv_booked_seats)
//    TextView tv_booked_seats;
//    @BindView(R.id.tv_selected_seats)
//    TextView tv_selected_seats;
//    @BindView(R.id.tv_available_seats)
//    TextView tv_available_seats;

    private List<Row> row =  new ArrayList<>();
    private List<Column> columns = new ArrayList<>();
    ArrayList<String> bookedSeats = new ArrayList<>();
    ArrayList<String> blockedSeats = new ArrayList<>();

    ArrayList<String> selectedrowList = new ArrayList<>();
    ArrayList<String> selectedcolList = new ArrayList<>();

    int offset = 0, duration = 100;
    float scaleX = 1.0f, scaleY = 1.0f;
    float maxZoomLimit = 2.6f, minZoomLimit = -5f;

    private FloatingActionButton zoomButton;
    private float currentScale = 1.0f;
    private float zoomFactor = 1.5f;
    int selectedCount = 0;


    String seats = "_UUUUUUAAAAARRRR_/"
            + "_________________/"
            + "UU__AAAARRRRR__RR/"
            + "UU__UUUAAAAAA__AA/"
            + "AA__AAAAAAAAA__AA/"
            + "AA__AARUUUURR__AA/"
            + "UU__UUUA_RRRR__AA/"
            + "AA__AAAA_RRAA__UU/"
            + "AA__AARR_UUUU__RR/"
            + "AA__UUAA_UURR__RR/"
            + "_________________/"
            + "UU_AAAAAAAUUUU_RR/"
            + "RR_AAAAAAAAAAA_AA/"
            + "AA_UUAAAAAUUUU_AA/"
            + "AA_AAAAAAUUUUU_AA/"
            + "_________________/";

    List<TextView> seatViewList = new ArrayList<>();
    int seatSize = 70;
    int seatGaping = 10;

    int STATUS_AVAILABLE = 1;
    int STATUS_SELECTED = 2;
    int STATUS_RESERVED = 3;
    String selectedIds = "";
    String centerHallid;
    String centername;
    String timemid;
    String EventDate;
    String eventID;
    String show_time;


    protected ActivityTicketBookingBinding inflateBinding(LayoutInflater inflater) {
        return ActivityTicketBookingBinding.inflate(inflater);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mScaleFactor *= scaleGestureDetector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));
//            scrollView.setScaleX(mScaleFactor);
//            scrollView.setScaleY(mScaleFactor);
            return true;
        }
    }


//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_ticket_booking;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ticket_booking);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Intent intent = getIntent();
        centerHallid = intent.getStringExtra("centerHallid");
        centername = intent.getStringExtra("centername");
        timemid = intent.getStringExtra("slotID");
        EventDate = intent.getStringExtra("selectedDate");
        eventID = intent.getStringExtra("eventID");
        show_time = intent.getStringExtra("et_time");
        binding.toolbarTitle.setText(centername);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(EventDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE dd MMM yyyy");
            String start = sdf2.format(d1);
            binding.tvShowDate.setText(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.tvShowTime.setText(show_time);

//        int selectedCount = Integer.parseInt(selectedIds);
//        tv_selected_seats.setText(selectedCount);

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        layout = findViewById(R.id.layoutSeat);
        seats = "/" + seats;

//        btnZoomIn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zoomOut(scrollView);
//            }
//        });
//
//        btnZoomOut.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                zoomIn(scrollView);
//            }
//        });

        binding.btnBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (selectedIds.isEmpty()) {
                    showToast(mContext, "Please Confirm Your Seats", Toast.LENGTH_SHORT);

                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
                map.put("seats", selectedIds);
                map.put("eventid", eventID);
                map.put("event_date", EventDate);
                map.put("amount", "100");
                map.put("timeid", timemid);
                BookSeats(map);

            }
        });

        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("hall_id", centerHallid);
        map.put("event_date", EventDate);
        map.put("timeid", timemid);
        map.put("eventid", eventID);
        LoadSitingAPI(map);

//        LinearLayout layoutSeat = new LinearLayout(this);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        layoutSeat.setOrientation(LinearLayout.VERTICAL);
//        layoutSeat.setLayoutParams(params);
//        layoutSeat.setPadding(8 * seatGaping, 8 * seatGaping, 8 * seatGaping, 8 * seatGaping);
//        layout.addView(layoutSeat);

        LinearLayout layout = null;

        int count = 0;

//        for (int index = 0; index < seats.length(); index++) {
//            if (seats.charAt(index) == '/') {
//                layout = new LinearLayout(this);
//                layout.setOrientation(LinearLayout.HORIZONTAL);
//                layoutSeat.addView(layout);
//            } else if (seats.charAt(index) == 'U') {
//                count++;
//                TextView view = new TextView(this);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
//                view.setLayoutParams(layoutParams);
//                view.setPadding(0, 0, 0, 2 * seatGaping);
//                view.setId(count);
//                view.setGravity(Gravity.CENTER);
//                view.setBackgroundResource(R.drawable.ic_seats_booked);
//                view.setTextColor(Color.WHITE);
//                view.setTag(STATUS_SELECTED);
//                view.setText(count + "");
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
//                layout.addView(view);
//                seatViewList.add(view);
//                view.setOnClickListener((View.OnClickListener) this);
//            } else if (seats.charAt(index) == 'A') {
//                count++;
//                TextView view = new TextView(this);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
//                view.setLayoutParams(layoutParams);
//                view.setPadding(0, 0, 0, 2 * seatGaping);
//                view.setId(count);
//                view.setGravity(Gravity.CENTER);
//                view.setBackgroundResource(R.drawable.ic_seats_book);
//                view.setText(count + "");
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
//                view.setTextColor(Color.BLACK);
//                view.setTag(STATUS_AVAILABLE);
//                layout.addView(view);
//                seatViewList.add(view);
//                view.setOnClickListener((View.OnClickListener) this);
//            } else if (seats.charAt(index) == 'R') {
//                count++;
//                TextView view = new TextView(this);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
//                view.setLayoutParams(layoutParams);
//                view.setPadding(0, 0, 0, 2 * seatGaping);
//                view.setId(count);
//                view.setGravity(Gravity.CENTER);
//                view.setBackgroundResource(R.drawable.ic_seats_reserved);
//                view.setText(count + "");
//                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
//                view.setTextColor(Color.WHITE);
//                view.setTag(STATUS_RESERVED);
//                layout.addView(view);
//                seatViewList.add(view);
//                view.setOnClickListener((View.OnClickListener) this);
//            } else if (seats.charAt(index) == '_') {
//                TextView view = new TextView(this);
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
//                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
//                view.setLayoutParams(layoutParams);
//                view.setBackgroundColor(Color.TRANSPARENT);
//                view.setText("");
//                layout.addView(view);
//            }
//        }
    }

    Map<Integer, String> idLabelMap = new HashMap();

    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;

    @SuppressLint("ResourceAsColor")
    void SetFrame()
    {
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());


        int availableCount = 0;

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(2 * seatGaping, 2 * seatGaping, 2 * seatGaping, 2 * seatGaping);
        layout.addView(layoutSeat);

        LinearLayout layout = null;
        //new line  here
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layoutSeat.addView(layout);
        idLabelMap.clear();

        int id = 1;
        for(int i = 0; i< row.size(); i++ ) {
            Row rowitem = row.get(i);
            String rowTitle = rowitem.getLabel();

            if (rowTitle.toLowerCase().contains("balcony") || rowTitle.toLowerCase().contains("gap"))
            {
                //fill horizontal row
                String titleToFillIn = "";
                if(rowTitle.toLowerCase().contains("balcony"))
                {
                    titleToFillIn = "Balcony";
                }
                //keep blank
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize * row.size() , seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText(titleToFillIn);
                view.setTextColor(R.color.white);
                view.setTextSize(15);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                {
                    view.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                }
                else{
                    view.setGravity(Gravity.CENTER_HORIZONTAL);
                }
                layout.addView(view);

                //new line  here
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
                continue;
            }

            for (int j = 0; j<columns.size(); j++) {
                Column columnitem = columns.get(j);
                String colTitle = columnitem.getLabel();

                String seatLabelStr = rowTitle + colTitle;

                if (bookedSeats.contains(seatLabelStr))
                {
                    //this seat is already purchased/booked by other person
                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setLayoutParams(layoutParams);
                    view.setPadding(0, 0, 0, 2 * seatGaping);
                    view.setId(id);

                    idLabelMap.put(id, rowTitle +","+ colTitle);
                    id++;
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.ic_seats_b);
                    view.setTextColor(Color.BLACK);
                    view.setTag(STATUS_AVAILABLE);

                    String LableStr = rowTitle + colTitle;

                    view.setText(LableStr);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                    layout.addView(view);
                    seatViewList.add(view);


                }
                else if (blockedSeats.contains(seatLabelStr))
                {
                    //this seat is blocked/kept reserved or set to blank by admin
                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    view.setText("");
                    layout.addView(view);
                }

                else if (colTitle.toLowerCase().contains("gap"))
                {
                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setLayoutParams(layoutParams);
                    view.setBackgroundColor(Color.TRANSPARENT);
                    view.setText("");
                    layout.addView(view);
                }
                else
                {
                    availableCount++;
                    TextView view = new TextView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                    layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                    view.setLayoutParams(layoutParams);
                    view.setPadding(0, 0, 0, 2 * seatGaping);
                    view.setId(id);

                    idLabelMap.put(id, rowTitle +","+ colTitle);
                    id++;
                    view.setGravity(Gravity.CENTER);
                    view.setBackgroundResource(R.drawable.ic_seats_book);
                    view.setTextColor(Color.BLACK);
                    view.setTag(STATUS_AVAILABLE);

                    String LableStr = rowTitle + colTitle;

                    view.setText(LableStr);
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                    layout.addView(view);
                    seatViewList.add(view);
                    view.setOnClickListener((View.OnClickListener) this);
                }
            }

            //new line started here
            layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layoutSeat.addView(layout);
        }
        binding.tvAvailableSeats.setText("Available "+"("+availableCount+""+")");
    }

    //seat onclick event
    @Override
    public void onClick(View view) {
        int tag = (int) view.getTag();

        if ( tag == STATUS_AVAILABLE) {
            selectedCount++;
            selectedIds = selectedIds + view.getId() + ",";
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
            view.setBackgroundResource(R.drawable.ic_seats_selected);
            view.setTag(STATUS_SELECTED);
        } else if ((int) view.getTag() == STATUS_SELECTED) {
            selectedCount--;
            selectedIds = selectedIds.replace(+view.getId() + ",", "");
//            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
            view.setTag(STATUS_AVAILABLE);
            view.setBackgroundResource(R.drawable.ic_seats_book);
        } else if ((int) view.getTag() == STATUS_RESERVED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Reserved", Toast.LENGTH_SHORT).show();
        }
        binding.tvSelectedSeats.setText("Selected "+"("+selectedCount+""+")");
    }

    Set<String> selecteSeats = new HashSet();

    //API CALL LoadSitingAPI
    public void LoadSitingAPI(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<LoadSitingData> call = app.getApiRequestHelper().getAppService().loadseatings(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    LoadSitingData loadSitingData = (LoadSitingData) object;
                    Log.e("in", "success");
                    if (loadSitingData != null && loadSitingData.getResponsecode() == 200) {

                        if (loadSitingData.getRows() != null) {
                            row = loadSitingData.getRows();
                        }

                        if (loadSitingData.getColumns() != null){
                            columns = loadSitingData.getColumns();
                        }

                        if (loadSitingData.getBooked() != null){
                            bookedSeats = (ArrayList<String>) loadSitingData.getBooked();
                        }

                        if (loadSitingData.getBlocked() != null){
                            blockedSeats = (ArrayList<String>) loadSitingData.getBlocked();
                        }

                        int seatcount = bookedSeats.size();
                        binding.tvBookedSeats.setText("Booked "+"("+seatcount+""+")");

                        SetFrame();
                        Utils.showLongToast(mContext, loadSitingData.getMessage());
                    } else {
                        Utils.showLongToast(mContext, loadSitingData.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
                        Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void BookSeats(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<BookAssets> call = app.getApiRequestHelper().getAppService().bookseats(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    BookAssets object1 = (BookAssets) object;
                    Log.e("in", "success");
                    if (object1 != null && object1.getResponsecode() == 200) {

                        mContext.startActivity(new Intent(mContext, PlanListActivity.class)
                                .putExtra("amount","1")
                                .putExtra("orderid",object1.getOrderId()));

                        Utils.showLongToast(mContext, object1.getMessage());
                    } else {
                        Utils.showLongToast(mContext, object1.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
                        Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    private void zoomIn(View v) {
        if (scaleX < maxZoomLimit && scaleY < maxZoomLimit) {
            Animation animation = new ScaleAnimation(scaleX, (scaleX + 0.2f), scaleY, (scaleY + 0.2f), 50, 50);
            scaleX += 0.2f;
            scaleY += 0.2f;
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setDuration(duration);
            animation.setStartOffset(offset);
            animation.setFillAfter(true);
            v.startAnimation(animation);
        }
    }

    private void zoomOut(View v) {
        if (scaleX > minZoomLimit && scaleY > minZoomLimit) {
            float x = v.getScaleX();
            float y = v.getScaleY();
            Animation animation = new ScaleAnimation(scaleX, (scaleX - 0.2f), scaleY, (scaleY - 0.2f), 50, 50);
            scaleY -= 0.2f;
            scaleX -= 0.2f;
            animation.setInterpolator(new DecelerateInterpolator());
            animation.setDuration(duration);
            animation.setStartOffset(offset);
            animation.setFillAfter(true);
            v.startAnimation(animation);
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Check if the orientation has changed to landscape
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Handle landscape orientation
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Handle portrait orientation
        }
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