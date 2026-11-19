package in.gov.nmc.rangmach;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yalantis.ucrop.UCrop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import id.zelory.compressor.Compressor;
import in.gov.nmc.rangmach.adapter.HallChallan_Adapter;
import in.gov.nmc.rangmach.adapter.HallDocuments_Adapter;
import in.gov.nmc.rangmach.adapter.PaymentChart_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityHallBookingBinding;
import in.gov.nmc.rangmach.model.AllOrganizationsList;
import in.gov.nmc.rangmach.model.AllOrganizationsList_Data;
import in.gov.nmc.rangmach.model.BookAssets;
import in.gov.nmc.rangmach.model.CheckAvailableSlots_Data;
import in.gov.nmc.rangmach.model.Theater_Booking_Documents;
import in.gov.nmc.rangmach.model.Theater_Hall_Capacity;
import in.gov.nmc.rangmach.model.Theater_Hall_Challan;
import in.gov.nmc.rangmach.model.Theater_Hall_EventType;
import in.gov.nmc.rangmach.model.Theater_Hall_FeesItems;
import in.gov.nmc.rangmach.model.Theater_Hall_Timing;
import in.gov.nmc.rangmach.model.Theater_Halls;
import in.gov.nmc.rangmach.model.Theater_Holidays;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pl.aprilapps.easyphotopicker.MediaFile;
import pl.aprilapps.easyphotopicker.MediaSource;
import retrofit2.Call;

import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;
import androidx.core.app.ActivityCompat;

public class HallBooking_Activity extends BaseActivity<ActivityHallBookingBinding> {

    String mainHallName;
    private String selectedDate = "";
    String str_capacity;
    String str_eventtype;
    String str_slot_timing;
    String currentDocumentId;
    Theater_Halls selectedhall;
    List<Theater_Holidays> holidaysList;
    private String slotID = "";
    PaymentChart_Adapter paymentChart_adapter;
    HallChallan_Adapter hallChallan_adapter;
    HallDocuments_Adapter hallDocumentsAdapter;
    Map<String, String> mymap = new HashMap<>();
    private CustomProgressDialog customProgressDialog;
    public static Map<String, String> uploadedFileObjects = new HashMap<>(); // docId -> File (for multipart)

    private EasyImage easyImage;
    int PICK_PDF_FILE = 2;
    private String fileExtension1 = "";
    String imageName1 = "";
    private String imagePath1 = "";
    private static int REQUEST_AVATAR_CODE = 2345;
    private File myfile1;
    private List<Theater_Booking_Documents> theaterBookingDocuments;
    public static String selectedFeesID = "";
    public static float SelectedRent = 0f;
    public String selectedOrgID = "";

    private List<AllOrganizationsList> allOrganizationsLists;

    String termspolicy;
    private String eventIds = "";
    private String holiday = "";
    private String capacityIds = "";
    private ArrayList<String> eventNameList;
    private ArrayList<String> capacityNameList;
    String selected_lang;
    public static int typeOfLanguage;
    private List<Theater_Hall_EventType> theater_hall_eventTypes;
    private List<Theater_Hall_Capacity> theater_hall_capacity;
    String itemarraystr = "";
    String quantityarraystr = "";
    String pricearraystr = "";
    String cgstarraystr = "";
    String sgstarraystr = "";
    String totalarraystr = "";

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 101;

    public static boolean isHolidayDate = false;

    protected ActivityHallBookingBinding inflateBinding(LayoutInflater inflater) {
        return ActivityHallBookingBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        easyImage = new EasyImage.Builder(mContext).allowMultiple(false).build();

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        ask_permissions();

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(HallBooking_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(HallBooking_Activity.this, "mr");
        }
        binding.etDate.setHint(GetTranslation("Select Date", typeOfLanguage));
        binding.etTime.setHint(GetTranslation("Select Time", typeOfLanguage));
        binding.etSelectEventtype.setHint(GetTranslation("Select Event", typeOfLanguage));
        binding.etSelectSeatingCapacity.setHint(GetTranslation("Select Capacity", typeOfLanguage));
        binding.btnCheckavailability.setText(GetTranslation("Check Availability", typeOfLanguage));
        binding.btnCheckAvailableDates.setText(GetTranslation("Check Available Dates", typeOfLanguage));
        binding.paymentchartText.setText(GetTranslation("Payment Chart", typeOfLanguage));
        binding.documentTextview.setText(GetTranslation("Document Needed", typeOfLanguage));
        binding.tvBooknow.setText(GetTranslation("Book Now", typeOfLanguage));
        binding.challanitemsText.setText(GetTranslation("Challan Items", typeOfLanguage));
        binding.tvSelectOrganization.setText(GetTranslation("Select Organization", typeOfLanguage));
        binding.etOrganization.setHint(GetTranslation("Select Organization title", typeOfLanguage));

        selectedhall = (Theater_Halls) getIntent().getSerializableExtra("data");
        holidaysList = (List<Theater_Holidays>) getIntent().getSerializableExtra("holidays");

        Intent intent = getIntent();
        termspolicy = intent.getStringExtra("termspolocy");
        mainHallName = intent.getStringExtra("mainHallName");
        binding.toolbarTitle.setText(selectedhall.getTitle());

        theater_hall_eventTypes = selectedhall.getEventType();
        theater_hall_capacity = selectedhall.getCapacityArray();

        customProgressDialog = new CustomProgressDialog(mContext);

        binding.llMainbox.setVisibility(View.GONE);

        Glide.with(mContext).load(selectedhall.getImage()).skipMemoryCache(true).error(R.drawable.no_image).diskCacheStrategy(DiskCacheStrategy.NONE).into(binding.imgTheaterimg);

        //dropdown time
        if (selectedhall.getTimings() != null) {

            ArrayList<Theater_Hall_Timing> list = (ArrayList<Theater_Hall_Timing>) selectedhall.getTimings();
            ArrayList<String> timelist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                timelist.add(list.get(i).getSelectedTiming());
            }
            if (timelist.size() > 0) {
                binding.etTime.setOnClickListener(view -> {
                    new MaterialDialog.Builder(mContext).title("Time").items(timelist).itemsCallback((dialog, view1, which, text) -> {
                        slotID = list.get(which).getId();
                        binding.etTime.setText(text.toString());


                        binding.llMainbox.setVisibility(View.GONE);
                        binding.llSlotstatus.setVisibility(View.GONE);
                        binding.llOrg.setVisibility(View.GONE);
                        binding.llBottom.setVisibility(View.GONE);
                        uploadedFileObjects.clear();

                    }).show();
                });
                binding.etTime.setVisibility(View.VISIBLE);
            }
        }

//        //EventType Dropdown
//        if (selectedhall.getEventType() != null) {
//            List<String> categories = new ArrayList<>();
//            for (String obj : selectedhall.getEventType()) {
//                categories.add(obj);
//            }
//
//            etSelectEventtype.setOnClickListener(view -> new MaterialDialog.Builder(mContext)
//                    .title("Event Type")
//                    .items(categories)
//                    .itemsCallback((dialog, view1, which, text) -> etSelectEventtype.setText(text.toString()))
//                    .show());
//
//        }


        //EventType Dropdown
        ArrayList<Theater_Hall_EventType> list = (ArrayList<Theater_Hall_EventType>) theater_hall_eventTypes;
        if (theater_hall_eventTypes != null) {

            eventNameList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                eventNameList.add(list.get(i).getTitle());
            }
            if (eventNameList != null && eventNameList.size() > 0) {
                binding.etSelectEventtype.setOnClickListener(view -> {
                    new MaterialDialog.Builder(mContext).title("Select EventType").items(eventNameList).itemsCallback((dialog, view1, which, text) -> {
                        eventIds = list.get(which).getId();
                        binding.etSelectEventtype.setText(text.toString());

//                        if (selectedDate != null && !selectedDate.isEmpty() && slotID != null && !slotID.isEmpty()) {
//                            mymap.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//                            mymap.put("slotid", slotID);
//                            mymap.put("hallid", selectedhall.getId());
//                            mymap.put("event_date", selectedDate);
//                            mymap.put("event_type_id", eventIds);
//
//                            CheckSlot(mymap);
//                        } else {
//                            // Optional: Inform user that date/time must be selected first
//                            Toast.makeText(mContext, "Please select date and time first", Toast.LENGTH_SHORT).show();
//                        }

                        binding.llMainbox.setVisibility(View.GONE);
                        binding.llSlotstatus.setVisibility(View.GONE);
                        binding.llOrg.setVisibility(View.GONE);
                        binding.llBottom.setVisibility(View.GONE);
                        uploadedFileObjects.clear();

                    }).show();
                });
                binding.etSelectEventtype.setVisibility(View.VISIBLE);
            }

        } else {
            BaseActivity.ToastDialog(mContext, "EventType is not available");
        }

        //Seating Capacity Dropdown

        ArrayList<Theater_Hall_Capacity> List = (ArrayList<Theater_Hall_Capacity>) theater_hall_capacity;

        if (List != null && List.size() > 0) {
            capacityIds = List.get(0).getId();
        } else {
            BaseActivity.ToastDialog(mContext, "Capacity is not available");
        }

//        ****** Capacity OLD Working code 09-07-2025
//        if (theater_hall_capacity != null) {
//
//            capacityNameList = new ArrayList<>();
//            for (int i = 0; i < List.size(); i++) {
//                capacityNameList.add(List.get(i).getCapacity());
//            }
//            if (capacityNameList != null && capacityNameList.size() > 0) {
//                binding.etSelectSeatingCapacity.setOnClickListener(view -> {
//
//                    if (theater_hall_capacity != null) {
//                        new MaterialDialog.Builder(mContext).title("Select Capacity").items(capacityNameList).itemsCallback((dialog1, view1, which, text) -> {
//                            capacityIds = List.get(which).getId();
//                            binding.etSelectSeatingCapacity.setText(text.toString());
//                        }).show();
//                    } else {
//                        BaseActivity.ToastDialog(mContext, "Capacity is not available");
//                    }
//
//                });
//                binding.etSelectSeatingCapacity.setVisibility(View.VISIBLE);
//            }
//
//        } else {
//            BaseActivity.ToastDialog(mContext, "Capacity is not available");
//        }


//        if (selectedhall.getCapacityArray() != null) {
//            List<String> categories = new ArrayList<>();
//            for (String obj : selectedhall.getCapacityArray()) {
//                categories.add(obj);
//            }
//
//            etSelectSeatingCapacity.setOnClickListener(view -> new MaterialDialog.Builder(mContext)
//                    .title("Capacity")
//                    .items(categories)
//                    .itemsCallback((dialog, view1, which, text) -> etSelectSeatingCapacity.setText(text.toString()))
//                    .show());
//
//        }

        //Date Select

        binding.etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {

                    // Format date as dd-MM-yyyy
                    String date = String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year);
                    selectedDate = String.format("%d-%02d-%02d", year, (monthOfYear + 1), dayOfMonth);
                    binding.etDate.setText(date);

                    // Create a Calendar instance for selected date
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(year, monthOfYear, dayOfMonth);

                    // Get the day name (like Monday, Sunday)
                    SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
                    holiday = dayFormat.format(selectedCal.getTime());

//                          Toast.makeText(getApplicationContext(), "Selected Day: " + holiday, Toast.LENGTH_SHORT).show();

                    if (holiday.equalsIgnoreCase("Saturday") || holiday.equalsIgnoreCase("Sunday")) {
                        ToastDialog(mContext, GetTranslation("saturday sundday notice", typeOfLanguage));
                    }

                    binding.llMainbox.setVisibility(View.GONE);
                    binding.llSlotstatus.setVisibility(View.GONE);
                    binding.llOrg.setVisibility(View.GONE);
                    binding.llBottom.setVisibility(View.GONE);
                    uploadedFileObjects.clear();

                    paymentChart_adapter.notifyDataSetChanged();

                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));

                dpd.setMinDate(now);
                dpd.setTitle("Select Event Date");
                dpd.show(getSupportFragmentManager(), "StartDatepickerdialog");
            }
        });

//        binding.etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar now = Calendar.getInstance();
//                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
//                    String date = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;
//                    selectedDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
//                    binding.etDate.setText(date);
//
//                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
//                dpd.setMinDate(now);
//                dpd.setTitle("Select Event Date");
//                dpd.show(getSupportFragmentManager(), "StartDatepickerdialog");
//            }
//        });


//        binding.etDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar mMaxDate = Calendar.getInstance(); // current date
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(
//                        mContext,
//                        (view12, year, month, dayOfMonth) -> {
//                            String date = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
//                            selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
//                            binding.etDate.setText(date);
//                        },
//                        mMaxDate.get(Calendar.YEAR),
//                        mMaxDate.get(Calendar.MONTH),
//                        mMaxDate.get(Calendar.DAY_OF_MONTH)
//                );
//
//                // Set the maximum selectable date to today
////                datePickerDialog.getDatePicker().setMaxDate(mMaxDate.getTimeInMillis());
//
//                datePickerDialog.show();
//            }
//        });

        binding.btnCheckAvailableDates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, WebviewActivity.class);
                String centerIdRaw = selectedhall.getCenterId();
                String encodedCenterId = Base64.encodeToString(centerIdRaw.getBytes(), Base64.NO_WRAP);
//                String url = "https://smartleaders.in/pmc-psrs/Superadmin_dashbord/hall_chart?center_id=" + encodedCenterId;
                String url = BuildConfig.BASE_URL + "Superadmin_dashbord/hall_chart?center_id=" + encodedCenterId;
                intent.putExtra("website", url);
                intent.putExtra("name", selectedhall.getTitle());
                mContext.startActivity(intent);
            }
        });

        binding.btnCheckavailability.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_eventtype = binding.etSelectEventtype.getText().toString();
                str_capacity = binding.etSelectSeatingCapacity.getText().toString();
                str_slot_timing = binding.etTime.getText().toString();

                if (str_eventtype.isEmpty()) {
                    Toast.makeText(mContext, "Please Select EventType", Toast.LENGTH_SHORT).show();
                    return;
                }

//                if (str_capacity.isEmpty()) {
//                    Toast.makeText(mContext, "Please Select Event Capacity", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (selectedDate.isEmpty()) {

                    if (typeOfLanguage == 0) {
                        Toast.makeText(mContext, "Please Select Date", Toast.LENGTH_SHORT).show();
                    } else if (typeOfLanguage == 1) {
                        Toast.makeText(mContext, "कृपया तारीख निवडा", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "कृपया दिनांक चुनें", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }

                if (slotID.isEmpty()) {

                    if (typeOfLanguage == 0) {
                        Toast.makeText(mContext, "Please Select Time", Toast.LENGTH_SHORT).show();
                    } else if (typeOfLanguage == 1) {
                        Toast.makeText(mContext, "कृपया वेळ निवडा", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(mContext, "कृपया समय चुनेंं", Toast.LENGTH_SHORT).show();
                    }
                    return;
                }


                if (selectedDate != null && !selectedDate.isEmpty()) {

                    isHolidayDate = false;

                    if (holiday.equalsIgnoreCase("Saturday") || holiday.equalsIgnoreCase("Sunday")) {
                        isHolidayDate = true;
                    }


                    if (holidaysList != null && !holidaysList.isEmpty()) {
                        for (Theater_Holidays h : holidaysList) {
                            if (h != null && h.getDate() != null && h.getDate().equals(selectedDate)) {
                                isHolidayDate = true;
//                                Toast.makeText(mContext, "Selected date is a holiday: " + h.getName(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }

//                    if (isHolidayDate) {
//                        Toast.makeText(mContext, "Date is Match Holiday List and Weekend", Toast.LENGTH_LONG).show();
//                    }
                }

                paymentChart_adapter.notifyDataSetChanged();

                mymap.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
                mymap.put("slotid", slotID);
                mymap.put("hallid", selectedhall.getId());
                mymap.put("event_date", selectedDate);
                mymap.put("event_type_id", eventIds);
                mymap.put("type", "android");
                mymap.put("version", String.valueOf(BuildConfig.VERSION_CODE));
                CheckSlot(mymap);

                AllOrganizations();
            }
        });

        binding.llBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    if (app.getPreferences().getLoggedInUser().getData().getType() != null && app.getPreferences().getLoggedInUser().getData().getType().contains("Others / इतर")) {

//                        showAlertMessage("Alert", "Please confirm your account type!");
                        showAlertMessage(GetTranslation("Booking_user_check_accounttype_alert", typeOfLanguage), GetTranslation("Please confirm your account type", typeOfLanguage));
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // Logs the exception for debugging
                    showAlertMessage(GetTranslation("Booking_user_check_accounttype_alert", typeOfLanguage), "An unexpected error occurred.");
                    return;
                }

                if (app.getPreferences().getLoggedInUser().getData().getName().isEmpty() || app.getPreferences().getLoggedInUser().getData().getMobileNo().isEmpty() || app.getPreferences().getLoggedInUser().getData().getAddress().isEmpty() || app.getPreferences().getLoggedInUser().getData().getGovtId_value().isEmpty() || app.getPreferences().getLoggedInUser().getData().getCity().isEmpty()) {
//                    if (typeOfLanguage == 0) {
//                        showAlertMessage("Alert ", "Please Complete Your Profile.");
//                    } else if (typeOfLanguage == 1) {
//                        showAlertMessage("सूचना ", "कृपया तुमचे प्रोफाइल पूर्ण करा.");
//                    } else {
//                        showAlertMessage("निर्देश ", "कृपया अपना प्रोफ़ाइल पूरा करें.");
//                    }

                    showAlertMessage(GetTranslation("Booking_user_profile_complete_alert", typeOfLanguage), GetTranslation("Please Complete Your Profile", typeOfLanguage));

                } else {
                    itemarraystr = "";
                    quantityarraystr = "";
                    pricearraystr = "";
                    cgstarraystr = "";
                    sgstarraystr = "";
                    totalarraystr = "";

                    for (Theater_Hall_Challan item : selectedhall.getChallan()) {
                        if (item.selectedQty > 0) {
                            if (itemarraystr.length() == 0) {
                                itemarraystr = item.getId();
                                quantityarraystr = item.selectedQty + "";
                                pricearraystr = item.getPrice();
                                cgstarraystr = item.getCgst();
                                sgstarraystr = item.getSgst();
                                totalarraystr = item.total + "";

                            } else {
                                itemarraystr += "," + item.getId();
                                quantityarraystr += "," + item.selectedQty;
                                pricearraystr += "," + item.getPrice();
                                cgstarraystr += "," + item.getCgst();
                                sgstarraystr += "," + item.getSgst();
                                totalarraystr += "," + item.total;
                            }
                        }
                    }

//                    if (SelectedRent <= 0) {
//                        Toast.makeText(mContext, "Please Select Booking Type", Toast.LENGTH_SHORT).show();
//                        return;
//                    }
                    str_eventtype = binding.etSelectEventtype.getText().toString();
//                    str_capacity = binding.etSelectSeatingCapacity.getText().toString();
                    str_slot_timing = binding.etTime.getText().toString();
//                    str_slot_timing = binding.etTime.getText().toString();

                    if (str_eventtype.isEmpty()) {
                        Toast.makeText(mContext, "Please Select EventType", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    if (str_capacity.isEmpty()) {
//                        Toast.makeText(mContext, "Please Select Event Capacity", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    if (str_slot_timing.isEmpty()) {
                        Toast.makeText(mContext, "Please Select Event Time", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    bookasset_map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//                    bookasset_map.put("slotid", slotID);
//                    bookasset_map.put("hallid", selectedhall.getId());
//                    bookasset_map.put("event_date", selectedDate);
//                    bookasset_map.put("amount", "0");
//                    bookasset_map.put("feesid", "1");
//                    bookasset_map.put("capacity", capacityIds);
//                    bookasset_map.put("seating_capacity", str_capacity);
//                    bookasset_map.put("eventtype", eventIds);
//                    bookasset_map.put("typeofevent", str_eventtype);
//                    bookasset_map.put("slot_timing", str_slot_timing);
//                    bookasset_map.put("hallname", mainHallName + " - " + selectedhall.getTitle());
//
//                    bookasset_map.put("itemarraystr", itemarraystr);
//                    bookasset_map.put("quantityarraystr", quantityarraystr);
//                    bookasset_map.put("pricearraystr", pricearraystr);
//                    bookasset_map.put("cgstarraystr", cgstarraystr);
//                    bookasset_map.put("sgstarraystr", sgstarraystr);
//                    bookasset_map.put("totalarraystr", totalarraystr);

//                    if (uploadedFileObjects.size() == theaterBookingDocuments.size()) {
//
//                        ConfirmDialog();
//                        Toast.makeText(HallBooking_Activity.this, "Ok Proceed", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        Toast.makeText(HallBooking_Activity.this, "Not Ok", Toast.LENGTH_SHORT).show();
//                        // Show alert if documents are missing
//                        Toast.makeText(mContext, "Not Ok", Toast.LENGTH_SHORT).show();
//                        new AlertDialog.Builder(mContext)
//                                .setTitle("Incomplete Upload")
//                                .setMessage("Please upload all documents")
//                                .setPositiveButton("OK", null)
//                                .show();
//                    }


                    if (uploadedFileObjects.size() >= theaterBookingDocuments.size()) {
                        // All required documents uploaded (based on count only)
                        ConfirmDialog();
                        Toast.makeText(HallBooking_Activity.this, "Ok Proceed", Toast.LENGTH_SHORT).show();
                    } else {
                        // Some documents missing
                        new AlertDialog.Builder(mContext).setTitle("Incomplete Upload").setMessage("Please upload all documents").setPositiveButton("OK", null).show();
                    }
                }
            }
        });

        if (selectedhall.getFeesItems() != null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.recyclerView.setLayoutManager(layoutManager);
            binding.recyclerView.setNestedScrollingEnabled(false);
            paymentChart_adapter = new PaymentChart_Adapter(mContext, selectedhall.getFeesItems(), selectedDate, slotID);
            binding.recyclerView.setAdapter(paymentChart_adapter);
//        nowShowing = new NowShowing();
        }

        if (selectedhall.getChallan() != null) {

            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.challanRecyclerView.setLayoutManager(layoutManager);
            hallChallan_adapter = new HallChallan_Adapter(mContext, selectedhall.getChallan(), selectedDate, slotID);
            binding.challanRecyclerView.setAdapter(hallChallan_adapter);
//        nowShowing = new NowShowing();
        }

        binding.ivAddOrganisation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mContext.startActivity(new Intent(mContext, AddOrganization_Details_Activity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        AllOrganizations();
    }

    public void AllOrganizations() {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().get_active_org_by_user(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    AllOrganizationsList_Data data = (AllOrganizationsList_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getData() != null && data.getData().size() > 0) {
                        allOrganizationsLists = data.getData();

                        runOnUiThread(() -> {
                            if (allOrganizationsLists.size() > 0) {
                                binding.etOrganization.setText(allOrganizationsLists.get(0).getName());
                            }
                        });

                        binding.etOrganization.setOnClickListener(v -> {
                            if (allOrganizationsLists != null && !allOrganizationsLists.isEmpty()) {
                                List<String> orgNames = new ArrayList<>();
                                for (AllOrganizationsList model : allOrganizationsLists) {
                                    orgNames.add(model.getName());
                                }

                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                                builder.setTitle(GetTranslation("Select Organization title", typeOfLanguage));
                                builder.setItems(orgNames.toArray(new String[0]), (dialogInterface, which) -> {
                                    binding.etOrganization.setText(orgNames.get(which));
                                    // Optional: save selected organization ID if needed
//                                    selectedOrgID = allOrganizationsLists.get(which).getId();
                                });
                                builder.show();


                            } else {
                                binding.etOrganization.setText("Self");
                                Toast.makeText(mContext, "No organizations available", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Utils.showLongToast(mContext, apiResponse);
//                    ToastDialog(apiResponse);
                }
            }));
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void ConfirmDialog() {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.seat_confirm_dialog);
        AppCompatButton btnClose = dialog.findViewById(R.id.btnClosing);
        AppCompatButton btnok = dialog.findViewById(R.id.btnok);

        MaterialEditText etEventTitle = dialog.findViewById(R.id.etEventTitle);
        TextView tvExample = dialog.findViewById(R.id.tvExample);
        AppCompatTextView tv_SelectedDate = dialog.findViewById(R.id.tv_SelectedDate);
        AppCompatTextView tv_SelectedSlot = dialog.findViewById(R.id.tv_SelectedSlot);
        AppCompatTextView tv_SelectedAmount = dialog.findViewById(R.id.tv_SelectedAmount);
        AppCompatTextView tv_terms_policy = dialog.findViewById(R.id.tv_terms_policy);
        CheckBox Checkbook_terms = dialog.findViewById(R.id.Checkbook_terms);

        Checkbook_terms.setEnabled(false);

        AppCompatButton btntermscondition = dialog.findViewById(R.id.btntermscondition);

        AppCompatTextView confirmbooking_text = dialog.findViewById(R.id.confirmbooking_text);
        AppCompatTextView youselecteddate_text = dialog.findViewById(R.id.youselecteddate_text);
        AppCompatTextView slot_text = dialog.findViewById(R.id.slot_text);
        AppCompatTextView termsandcondition_text = dialog.findViewById(R.id.termsandcondition_text);

        confirmbooking_text.setText(GetTranslation("Confirm Booking", typeOfLanguage));
        youselecteddate_text.setText(GetTranslation("You selected Date", typeOfLanguage));
        slot_text.setText(GetTranslation("Slot", typeOfLanguage));
        termsandcondition_text.setText(GetTranslation("Terms and Conditions", typeOfLanguage));
        Checkbook_terms.setText(GetTranslation("I have read and accept the Terms and Conditions", typeOfLanguage));
        etEventTitle.setHint(GetTranslation("Please enter event title", typeOfLanguage));
        tvExample.setText(GetTranslation("Choricha Mamla", typeOfLanguage));
        btntermscondition.setText(GetTranslation("Terms and Condition", typeOfLanguage));

        etEventTitle.requestFocus();

        tv_terms_policy.setText(termspolicy);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(selectedDate);
            SimpleDateFormat sdf2 = new SimpleDateFormat("EEE dd MMM yyyy");
            String start = sdf2.format(d1);
            tv_SelectedDate.setText(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tv_SelectedSlot.setText(binding.etTime.getText().toString());
        tv_SelectedAmount.setText(SelectedRent + "");


        btntermscondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Checkbook_terms.setEnabled(true);

//                String pdfUrl = BuildConfig.BASE_URL + "nmcpolicy.pdf";
//                Intent intent = new Intent(mContext, PDFView_WebView_Activity.class);
//                intent.putExtra("website", pdfUrl);
//                mContext.startActivity(intent);

                Intent intent = new Intent(mContext, PDFView_WebView_Activity.class);
                String centerId = "https://smartleaders.in/nmcpolicy.pdf";
                intent.putExtra("website", centerId);
                mContext.startActivity(intent);
            }
        });


        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        btnok.setOnClickListener(view1 -> {

            if (Checkbook_terms.isChecked()) {

                String title = etEventTitle.getText().toString().trim();
                // Check if Event Title is not empty
                if (TextUtils.isEmpty(title)) {
                    Toast.makeText(mContext, GetTranslation("Please enter event title", typeOfLanguage), Toast.LENGTH_SHORT).show();
                    return;
                } else {
//                    bookasset_map.put("event_title", title);
//                    String orgId = "0";
//                    for (AllOrganizationsList temp : allOrganizationsLists) {
//                        String tempStr = binding.etOrganization.getText().toString();
//                        if (tempStr.equals(temp.getName())) {
//                            orgId = temp.getId();
//                        }
//                    }

                    String orgId = "0";
                    if (allOrganizationsLists != null) {
                        for (AllOrganizationsList temp : allOrganizationsLists) {
                            String tempStr = binding.etOrganization.getText().toString();
                            if (tempStr.equals(temp.getName())) {
                                orgId = temp.getId();
//                                break; // optional: stop loop once found
                            }
                        }
                    } else {
                        Log.e("HallBooking", "allOrganizationsLists is null!");
                    }

                    final String finalOrgId = orgId;
//                    bookasset_map.put("org_id", orgId);
//                    BookAssets(bookasset_map);

                    if (cd.isConnectingToInternet()) {

                        if (customProgressDialog != null) {
                            customProgressDialog.show();
                        }
                        Thread t = new Thread(() -> {
                            try {

                                String userId = app.getPreferences().getLoggedInUser().getData().getUserId();

                                float totalValue = 0f;
                                try {
                                    totalValue = Float.parseFloat(totalarraystr);
                                } catch (NumberFormatException e) {
                                    totalValue = 0f;
                                }

                                float netTotalValue = totalValue + SelectedRent;
                                String netTotal = String.format("%.2f", netTotalValue);

                                UploadFile(userId, slotID, selectedhall.getId(), selectedDate, netTotal, "1", capacityIds, str_capacity, eventIds, str_eventtype, str_slot_timing, mainHallName + " - " + selectedhall.getTitle(), itemarraystr,
//                                        quantityarraystr, pricearraystr, cgstarraystr, sgstarraystr, netTotal, title, finalOrgId,
                                        quantityarraystr, pricearraystr, cgstarraystr, sgstarraystr, totalarraystr, title, finalOrgId, "" + imageName1, imagePath1);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        t.start();
                    } else {
                        Utils.alert_dialog(mContext);
                    }

                    dialog.dismiss();
                }

            } else {
                Toast.makeText(HallBooking_Activity.this, "Please Select Terms & Condition Policy", Toast.LENGTH_SHORT).show();
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

    private void showAlertMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mContext.startActivity(new Intent(mContext, EditProfileActivity.class));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SelectedRent = 0f;
    }

    public void updateBookNowText() {
        String text = "Total Rs " + String.format("%.2f", SelectedRent) + "   " + GetTranslation("Book Now", typeOfLanguage);
        binding.tvBooknow.setText(text);
    }

    public void Update_BookingCost() {
        double total = 0;

        for (Theater_Hall_Challan item : selectedhall.getChallan()) {
            // if(item.selectedQty>0)
            {

                double pricePerItem = Double.parseDouble(item.getPrice()); // Example price per item, replace with actual price
                double sgstRate = Double.parseDouble(item.getSgst()); // SGST rate (example 9%)
                double cgstRate = Double.parseDouble(item.getCgst()); // CGST rate (example 9%)

                // Calculate total price
                double totalPrice = pricePerItem * item.selectedQty;

                // Calculate GST
                double sgstAmount = (sgstRate / 100) * totalPrice;
                double cgstAmount = (cgstRate / 100) * totalPrice;

                double totalGst = sgstAmount + cgstAmount;

                // Calculate final total price (with GST)
//                double finalTotalPrice = totalPrice + sgstAmount + cgstAmount;
                double finalTotalPrice = totalPrice + sgstAmount + cgstAmount;
                total = total + finalTotalPrice;

//                binding.tvBooknow.setText("Total Rs " + String.format("%.2f", total) + "   " + GetTranslation("Book Now", typeOfLanguage));
            }

//            binding.tvBooknow.setText("Total Rs " + String.format("%.2f", total + HallBooking_Activity.SelectedRent) + "   " + GetTranslation("Book Now", typeOfLanguage));

        }
    }

    public void OddDayRent(int position) {
        Theater_Hall_FeesItems temp = this.selectedhall.getFeesItems().get(position);
        selectedFeesID = temp.getId();
        SelectedRent = Float.parseFloat(temp.getOddDayRent()) + Float.parseFloat(temp.getDeposit());
        paymentChart_adapter.notifyDataSetChanged();
    }

    public void HolidayRent(int position) {
        Theater_Hall_FeesItems temp = this.selectedhall.getFeesItems().get(position);
        selectedFeesID = temp.getId();
        SelectedRent = Float.parseFloat(temp.getHolidayRent()) + Float.parseFloat(temp.getDeposit());
        paymentChart_adapter.notifyDataSetChanged();
    }

    public void CheckSlot(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<CheckAvailableSlots_Data> call = app.getApiRequestHelper().getAppService().checkavailableslots(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    CheckAvailableSlots_Data data = (CheckAvailableSlots_Data) object;
                    Log.e("in", "success");
                    if (data != null && data.getResponsecode() == 200) {

                        theaterBookingDocuments = data.getDocumentlist();

                        if (data.getDocumentlist() == null || data.getDocumentlist().isEmpty()) {
                            binding.llDocuments.setVisibility(View.GONE);
                        } else {
                            binding.llDocuments.setVisibility(View.VISIBLE);
                            // load into RecyclerView if needed
                        }

                        binding.ivSlotstatus.setImageResource(R.drawable.available);
                        binding.llSlotstatus.setVisibility(View.VISIBLE);

                        binding.llOrg.setVisibility(View.GONE);
                        binding.llMainbox.setVisibility(View.VISIBLE);
                        binding.llBottom.setVisibility(View.VISIBLE);

                        if (data.getDocumentlist() != null) {

                            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                            binding.documentsRecyclerView.setLayoutManager(layoutManager);
                            List<Theater_Booking_Documents> temp = new ArrayList<>();
                            temp.addAll(data.getDocumentlist());
                            hallDocumentsAdapter = new HallDocuments_Adapter(mContext, temp);
                            binding.documentsRecyclerView.setAdapter(hallDocumentsAdapter);
                        } else {

                        }

//                      Utils.showLongToast(mContext, loadSitingData.getMessage());
                        ToastDialog(mContext, data.getMessage());

                    } else {

                        binding.llMainbox.setVisibility(View.GONE);
                        binding.llBottom.setVisibility(View.GONE);
                        binding.ivSlotstatus.setImageResource(R.drawable.notavailable);
                        binding.llSlotstatus.setVisibility(View.VISIBLE);

//                        Utils.showLongToast(mContext, loadSitingData.getMessage());
                        ToastDialog(mContext, data.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    binding.llMainbox.setVisibility(View.GONE);
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
//                      Utils.showLongToast(mContext, apiResponse);
                        ToastDialog(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void OrderSuccessMessage(String msg, int orderID) {

        String orderIdStr = String.valueOf(orderID);

//        Toast.makeText(mContext, "Booking Id" + orderIdStr, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Response").setMessage(msg);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when the OK button is clicked
                dialog.dismiss(); // Close the dialog
//                mContext.startActivity(new Intent(mContext, MainActivity.class));

                if (orderID != 0) {
                    // orderID has a valid value

                    Intent intent = new Intent(mContext, OnlinePayment_WebViewActity.class);
                    intent.putExtra("bookingid", orderIdStr);
                    intent.putExtra("amount", String.valueOf(SelectedRent));
                    mContext.startActivity(intent);
                } else {
                    // Toast.makeText(mContext, "Booking Fail!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    boolean isCropEnabled = false;

//    public void openFile(int imageNumber, String documentId) {
//
//        currentDocumentId = documentId;
//
//        String[] items = new String[]{"Capture", "Select File"};
//        new androidx.appcompat.app.AlertDialog.Builder(mContext).setTitle("File").setItems(items, (dialog, item) -> {
//            switch (items[item]) {
//                case "Capture":
//                    dialog.dismiss();
//                    isCropEnabled = true;
//                    easyImage.openCameraForImage(this);
//                    break;
//                case "Select File":
//                    dialog.dismiss();
//                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("image/*");  // ✅ Only allow images
//                    startActivityForResult(intent, PICK_PDF_FILE);
//                    break;
//                case "Cancel":
//                    dialog.dismiss();
//                    break;
//            }
//        }).show();
//    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted by the user! Proceed to open camera.
                isCropEnabled = true;
                easyImage.openCameraForImage(this);
            } else {
                // Permission was denied by the user. Show a friendly toast message.
                Toast.makeText(mContext, "Camera permission is required to capture document images.", Toast.LENGTH_SHORT).show();
            }
        }
    }


    public void openFile(int imageNumber, String documentId) {
        currentDocumentId = documentId;

        String[] items = new String[]{"Capture", "Select File"};
        new androidx.appcompat.app.AlertDialog.Builder(mContext)
                .setTitle("File")
                .setItems(items, (dialog, item) -> {
                    switch (items[item]) {
                        case "Capture":
                            dialog.dismiss();

                            // Check if Camera Permission is granted
                            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                                    == PackageManager.PERMISSION_GRANTED) {
                                isCropEnabled = true;
                                easyImage.openCameraForImage(this);
                            } else {
                                // Request permission from the user dynamically
                                ActivityCompat.requestPermissions(
                                        HallBooking_Activity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        CAMERA_PERMISSION_REQUEST_CODE
                                );
                            }
                            break;

                        case "Select File":
                            dialog.dismiss();
                            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType("image/*");  // Only allow images
                            startActivityForResult(intent, PICK_PDF_FILE);
                            break;

                        case "Cancel":
                            dialog.dismiss();
                            break;
                    }
                }).show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_PDF_FILE && resultCode == RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                if (data != null && data.getData() != null) {
                    Uri uri = data.getData();

                    if (uri != null) {
                        Log.e("uti", uri.toString());
                        String mimeType = mContext.getContentResolver().getType(uri);
                        Log.e("mimeType", mimeType);

                        fileExtension1 = "jpg";
                        String filePath = createCopyAndReturnRealPath(mContext, uri, fileExtension1);
                        imagePath1 = filePath;
                        Uri cropUri = Uri.fromFile(new File(filePath));
                        if (cropUri != null) {
                            Log.e("cropUri", String.valueOf(cropUri));
                        }
                        if (!TextUtils.isEmpty(imagePath1)) {
                            fileExtension1 = imagePath1.substring(imagePath1.lastIndexOf("."));
                            fileExtension1 = fileExtension1.replace(".", "");
                            Log.e("extension", fileExtension1);
                        }
                        showCropAvatarFragment(Uri.fromFile(new File(filePath)), cropUri);

                    } else {
                        Log.e("uti", "null");
                    }
                }
            } else {
                easyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
                    @Override
                    public void onMediaFilesPicked(MediaFile[] imageFiles, MediaSource source) {
                        if (isCropEnabled) {

                            String dir = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                            SimpleDateFormat dateFormatForPhoto = new SimpleDateFormat("HHmmSS", Locale.ENGLISH);
                            String fileName = dateFormatForPhoto.format(System.currentTimeMillis()) + ".jpg";
                            File image = new File(dir + "/" + fileName);

                            imagePath1 = image.getAbsolutePath();
                            Uri cropUri = Uri.fromFile(image);
                            if (cropUri != null) {
                                Log.e("cropUri", String.valueOf(cropUri));
                            }
                            if (!TextUtils.isEmpty(imagePath1)) {
                                fileExtension1 = imagePath1.substring(imagePath1.lastIndexOf("."));
                                fileExtension1 = fileExtension1.replace(".", "");
                                Log.e("extension", fileExtension1);
                            }

                            if (requestCode == REQUEST_AVATAR_CODE && resultCode == RESULT_OK) {
                                if (cropUri != null) {
                                    imagePath1 = cropUri.getPath();
                                    // update the correct document
                                    for (Theater_Booking_Documents doc : theaterBookingDocuments) {
                                        if (doc.getDocument_id().equals(currentDocumentId)) {
                                            doc.setFilePath(imagePath1);
                                            break;
                                        }
                                    }

                                    hallDocumentsAdapter.ClearData();
                                    hallDocumentsAdapter.SetData(theaterBookingDocuments);
                                    hallDocumentsAdapter.notifyDataSetChanged();
                                }
                            }

                            uploadedFileObjects.put(currentDocumentId, imagePath1);
//                            Toast.makeText(mContext, uploadedFileObjects.toString(), Toast.LENGTH_SHORT).show();

                            showCropAvatarFragment(Uri.fromFile(imageFiles[0].getFile()), cropUri);

                        } else {

                            imagePath1 = imageFiles[0].getFile().getAbsolutePath();
                            Log.e("imagePath", imagePath1);
//                            Glide.with(mContext).load(imagePath1).into(ivdoc);
                            Uri uri = Uri.fromFile(imageFiles[0].getFile());
                            if (uri != null) {
                                Log.e("uri", String.valueOf(uri));
                            }
                            if (!TextUtils.isEmpty(imagePath1)) {
                                fileExtension1 = imagePath1.substring(imagePath1.lastIndexOf("."));
                                fileExtension1 = fileExtension1.replace(".", "");
                                Log.e("extension", fileExtension1);
                            }
                        }
                    }

                    @Override
                    public void onImagePickerError(@NonNull Throwable error, @NonNull MediaSource source) {
                        //Some error handling
                        error.printStackTrace();
                    }

                    @Override
                    public void onCanceled(@NonNull MediaSource source) {
                        //Not necessary to remove any files manually anymore
                    }
                });

                if (requestCode == REQUEST_AVATAR_CODE) {
                    if (data == null) {
                        Toast.makeText(mContext, "Failed to crop image. Please, try again later", Toast.LENGTH_SHORT).show();
                        ;
                        return;
                    }

                    Uri resultUri = null;
                    try {
                        resultUri = UCrop.getOutput(data);
                        //after cropping, we get control over here
                        imagePath1 = resultUri.getPath();
                        // update the correct document
                        for (Theater_Booking_Documents doc : theaterBookingDocuments) {
                            if (doc.getDocument_id().equals(currentDocumentId)) {
                                doc.setFilePath(imagePath1);
                                break;
                            }
                        }

                        hallDocumentsAdapter.ClearData();
                        hallDocumentsAdapter.SetData(theaterBookingDocuments);
                        hallDocumentsAdapter.notifyDataSetChanged();
                        uploadedFileObjects.put(currentDocumentId, imagePath1);
                    } catch (RuntimeException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Nullable
    public String createCopyAndReturnRealPath(@NonNull Context context, @NonNull Uri uri, String fileExtension) {
        final ContentResolver contentResolver = context.getContentResolver();
        if (contentResolver == null) return null;

        // Create file path inside app's data dir
        String filePath = context.getApplicationInfo().dataDir + File.separator + System.currentTimeMillis() + "." + fileExtension;

        myfile1 = new File(filePath);

        try {
            InputStream inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null) return null;
            OutputStream outputStream = null;
            outputStream = new FileOutputStream(myfile1);

            byte[] buf = new byte[1024];
            int len;
            while ((len = inputStream.read(buf)) > 0) outputStream.write(buf, 0, len);

            outputStream.close();
            inputStream.close();
        } catch (IOException ignore) {
            return null;
        }

        return myfile1.getAbsolutePath();

    }


    private void showCropAvatarFragment(Uri sourceUri, Uri destinationUri) {
        UCrop.Options options = new UCrop.Options();
        options.setCircleDimmedLayer(false);
        options.setShowCropFrame(true);
        options.setShowCropGrid(false);
        options.setToolbarTitle("Crop Photo");
        options.setHideBottomControls(false);
        options.setFreeStyleCropEnabled(true);
//        options.withAspectRatio(1f, 1f);
//        options.withMaxResultSize(512, 512);
        UCrop.of(sourceUri, destinationUri).withOptions(options).start(this, REQUEST_AVATAR_CODE);
    }

    void UploadFile(String userId, String slotID, String selectedHallId, String selectedDate, String amount, String feesid, String capacityIds, String str_capacity, String eventIds, String str_eventtype, String str_slot_timing, String hallname, String itemarraystr, String quantityarraystr, String pricearraystr, String cgstarraystr, String sgstarraystr, String totalarraystr, String title, String orgId, String fileName, String filePath) throws Exception {


        Map<String, String> bookasset_map = new HashMap<>();
        bookasset_map.put("userid", userId);
        bookasset_map.put("slotid", slotID);
        bookasset_map.put("hallid", selectedHallId);
        bookasset_map.put("event_date", selectedDate);
        bookasset_map.put("amount", amount);
        bookasset_map.put("feesid", feesid);
        bookasset_map.put("capacity", capacityIds);
        bookasset_map.put("seating_capacity", str_capacity);
        bookasset_map.put("eventtype", eventIds);
        bookasset_map.put("typeofevent", str_eventtype);
        bookasset_map.put("slot_timing", str_slot_timing);
        bookasset_map.put("hallname", hallname);
        bookasset_map.put("itemarraystr", itemarraystr);
        bookasset_map.put("quantityarraystr", quantityarraystr);
        bookasset_map.put("pricearraystr", pricearraystr);
        bookasset_map.put("cgstarraystr", cgstarraystr);
        bookasset_map.put("sgstarraystr", sgstarraystr);
        bookasset_map.put("totalarraystr", totalarraystr);
        bookasset_map.put("event_title", title);
        bookasset_map.put("org_id", orgId);

//      multipartRequest(BuildConfig.BASE_URL + BuildConfig.PhotoPath + "booking_assets", bookasset_map);
        multipartRequest(BuildConfig.BASE_URL + BuildConfig.PhotoPath + "booking_assets", bookasset_map);
    }

    void multipartRequest(String urlTo, Map<String, String> params) {

        new Thread(() -> {
            try {
                // Step 1: Compress images
                Map<String, String> compressedImagePathMap = new HashMap<>();

                if (uploadedFileObjects != null) {
                    for (Map.Entry<String, String> record : uploadedFileObjects.entrySet()) {
                        File file = new Compressor(mContext).setQuality(90).compressToFile(new File(record.getValue()));

                        compressedImagePathMap.put(record.getKey(), file.getAbsolutePath());
                    }
                }

                // Step 2: Create Multipart Body
                MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);

                String timestamp = String.valueOf(System.currentTimeMillis());

                // Add Images
                for (Map.Entry<String, String> record : compressedImagePathMap.entrySet()) {
                    File file = new File(record.getValue());

                    if (file.exists()) {
                        builder.addFormDataPart(record.getKey(), timestamp + ".jpg", RequestBody.create(file, MediaType.parse("image/jpeg")));
                    }
                }

                // Add Params
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    builder.addFormDataPart(entry.getKey(), entry.getValue());
                }

                RequestBody requestBody = builder.build();

                // Step 3: Create Request
                Request request = new Request.Builder().url(urlTo).post(requestBody).build();

                // Step 4: Execute
                OkHttpClient client = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();

                Response response = client.newCall(request).execute();

                String responseStr = response.body() != null ? response.body().string() : "";

                Log.e("API_RESPONSE", responseStr);

                // Step 5: Parse Response
                Gson gson = new Gson();
                BookAssets userData = gson.fromJson(responseStr, BookAssets.class);

                runOnUiThread(() -> {

                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();

                    if (userData != null) {

                        String message = userData.getMessage();

                        if (userData.getResponsecode() == 200) {

                            Integer orderId = userData.getOrderId();
                            if (orderId != null) {
                                OrderSuccessMessage(message, orderId);
                            } else {
                                OrderSuccessMessage(message, 0);
                            }

                        } else {

                            if (message != null && !TextUtils.isEmpty(message)) {
                                OrderSuccessMessage(message, 0);
                            }
                        }
                    } else {
                        Utils.showLongToast(mContext, "Invalid response from server");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();

                runOnUiThread(() -> {
                    if (customProgressDialog != null && customProgressDialog.isShowing())
                        customProgressDialog.dismiss();

                    OrderSuccessMessage(e.toString(), 0);
                });
            }
        }).start();
    }

    static byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
//            JOptionPane.showMessageDialog(null, "Could not completely read file " + file.getName());
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }

    static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public void BookAssets(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();

            Call<BookAssets> call = app.getApiRequestHelper().getAppService().bookasset(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    BookAssets bookAssets = (BookAssets) object;
                    Log.e("in", "success");
                    if (bookAssets != null && bookAssets.getResponsecode() == 200) {
//                        mContext.startActivity(new Intent(mContext, PlanListActivity.class)
//                                .putExtra("amount", SelectedRent)
//                                .putExtra("orderid", bookAssets.getOrderId()));
//                        OrderSuccessMessage(bookAssets.getMessage());

                        Utils.showLongToast(mContext, bookAssets.getMessage());
                    } else {
                        Utils.showLongToast(mContext, bookAssets.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();

                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
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