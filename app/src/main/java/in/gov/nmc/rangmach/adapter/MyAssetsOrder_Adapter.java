package in.gov.nmc.rangmach.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.gov.nmc.rangmach.App;
import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.ItemsDetails_Activity;
import in.gov.nmc.rangmach.MyAssetsOder_Activity;
import in.gov.nmc.rangmach.OnlinePayment_WebViewActity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.UploadDocuments_Activity;
import in.gov.nmc.rangmach.databinding.RowMyordersBinding;
import in.gov.nmc.rangmach.model.CancellationPojo;
import in.gov.nmc.rangmach.model.MyAssetsOrder;
import in.gov.nmc.rangmach.model.ReschedulePojo;
import in.gov.nmc.rangmach.model.Theater_Hall_Capacity;
import in.gov.nmc.rangmach.model.Theater_Hall_EventType;
import in.gov.nmc.rangmach.model.Theater_Hall_Timing;
import in.gov.nmc.rangmach.utils.Utils;

public class MyAssetsOrder_Adapter extends RecyclerView.Adapter<MyAssetsOrder_Adapter.ViewHolder> {
    Context mContext;
    App app;
    List<MyAssetsOrder> myAssetsOrders;
    List<MyAssetsOrder> list_search = new ArrayList<>();
    public static int typeOfLanguage;
    private String selectedDate = "";
    MyAssetsOder_Activity myAssetsOder_activity;
    private String slotID = "";
    private String eventIds = "";
    private String capacityIds = "";
    private ArrayList<String> eventNameList;
    private ArrayList<String> capacityNameList;
    private List<Theater_Hall_EventType> theater_hall_eventTypes;
    private List<Theater_Hall_Capacity> theater_hall_capacity;

    private static final int PERMISSION_REQUEST_CODE = 101;

    public MyAssetsOrder_Adapter(Context mContext, List<MyAssetsOrder> myAssetsOrders, MyAssetsOder_Activity myAssetsOder_activity, App app) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
        this.myAssetsOder_activity = myAssetsOder_activity;
        this.app = this.app;
        list_search.addAll(myAssetsOrders);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myorders, parent, false);
//        return new ViewHolder(view);

        RowMyordersBinding binding = RowMyordersBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return myAssetsOrders.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final MyAssetsOrder item = myAssetsOrders.get(i);

        theater_hall_eventTypes = item.getEventTypePojo();
        theater_hall_capacity = item.getCapacityArray();

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        //langauge
        viewHolder.binding.bookingidText.setText(BaseActivity.GetTranslation("Booking ID", typeOfLanguage));
        viewHolder.binding.requestDateText.setText(BaseActivity.GetTranslation("Request Date", typeOfLanguage));
        viewHolder.binding.eventDateText.setText(BaseActivity.GetTranslation("Event Date", typeOfLanguage));
        viewHolder.binding.slotsText.setText(BaseActivity.GetTranslation("Slot", typeOfLanguage));
        viewHolder.binding.amountText.setText(BaseActivity.GetTranslation("Amount", typeOfLanguage));
        viewHolder.binding.statusText.setText(BaseActivity.GetTranslation("Status", typeOfLanguage));
        viewHolder.binding.refundReasonText.setText(BaseActivity.GetTranslation("Refund Reason", typeOfLanguage));
        viewHolder.binding.refundDatetimeText.setText(BaseActivity.GetTranslation("Refund Datetime", typeOfLanguage));
        viewHolder.binding.paymentStatusText.setText(BaseActivity.GetTranslation("Payment Status", typeOfLanguage));
        viewHolder.binding.refundamounttext.setText(BaseActivity.GetTranslation("Refund Amount", typeOfLanguage));
        viewHolder.binding.rescheduleSatusText.setText(BaseActivity.GetTranslation("Reschedule Status", typeOfLanguage));
        viewHolder.binding.cancellationSatusText.setText(BaseActivity.GetTranslation("Cancellation Status", typeOfLanguage));
        viewHolder.binding.eventTypeText.setText(BaseActivity.GetTranslation("Event Type", typeOfLanguage));

        viewHolder.binding.btnPaynow.setText(BaseActivity.GetTranslation("PayNow", typeOfLanguage));
        viewHolder.binding.btnItemsDetails.setText(BaseActivity.GetTranslation("Details", typeOfLanguage));
        viewHolder.binding.btnUploadDocuments.setText(BaseActivity.GetTranslation("Upload Documents", typeOfLanguage));
        viewHolder.binding.btnCancellationRequest.setText(BaseActivity.GetTranslation("Cancellation Request", typeOfLanguage));
        viewHolder.binding.btnApplyRefund.setText(BaseActivity.GetTranslation("Request Refund", typeOfLanguage));
        viewHolder.binding.btnReschedulingRequest.setText(BaseActivity.GetTranslation("Rescheduling Request", typeOfLanguage));

        viewHolder.binding.tvBookingId.setText(item.getId());
        viewHolder.binding.tvTitle.setText(item.getTitle());
        viewHolder.binding.tvEventDate.setText(item.getEventDate());
        viewHolder.binding.tvSlot.setText(item.getSlot());
        viewHolder.binding.tvAmount.setText(item.getAmount());
        viewHolder.binding.tvStatus.setText(item.getStatus());
        viewHolder.binding.tvPaymentStatus.setText(item.getPaymentStatus());
        viewHolder.binding.tvEventType.setText(item.getEvent_type());

        if (item.getCancellation() != null) {
            viewHolder.binding.tvCancellationStatus.setText(item.getCancellation().getStatus());
            viewHolder.binding.tvCancellationStatus.setVisibility(View.VISIBLE);
            viewHolder.binding.ivCancellationDetails.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.tvCancellationStatus.setVisibility(View.GONE);
            viewHolder.binding.ivCancellationDetails.setVisibility(View.GONE);
        }

        if (item.getReschedulePojo() != null) {
            viewHolder.binding.tvRescheduleStatus.setText(item.getReschedulePojo().getStatus());
            viewHolder.binding.tvRescheduleStatus.setVisibility(View.VISIBLE);
            viewHolder.binding.ivRescheduleDetails.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.tvRescheduleStatus.setVisibility(View.GONE);
            viewHolder.binding.ivRescheduleDetails.setVisibility(View.GONE);
        }

        viewHolder.binding.ivCancellationDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getCancellation() != null) {
                    Cancellation_Details_Dialog(item.getCancellation());
                } else {
                    Toast.makeText(mContext, "Details Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewHolder.binding.ivRescheduleDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.getReschedulePojo() != null) {
                    Reschedule_Details_Dialog(item.getReschedulePojo());
                } else {
                    Toast.makeText(mContext, "Details Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String refundamt = item.getRefundAmount();

        if (refundamt.length() > 0) {
            viewHolder.binding.llRefundAmount.setVisibility(View.VISIBLE);
            viewHolder.binding.tvRefundAmount.setText(item.getRefundAmount());
        } else {
            viewHolder.binding.llRefundAmount.setVisibility(View.GONE);
            viewHolder.binding.tvRefundAmount.setText(item.getRefundAmount());
        }

        viewHolder.binding.btnItemsDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, ItemsDetails_Activity.class).putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition())));
            }
        });

//        viewHolder.binding.btnUploadDocuments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, UploadDocuments_Activity.class).putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition())));
//            }
//        });

        viewHolder.binding.btnUploadDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (hasRequiredPermissions(mContext)) {

                    mContext.startActivity(
                            new Intent(mContext, UploadDocuments_Activity.class)
                                    .putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition()))
                    );

                } else {

                    askPermissions((Activity) mContext);

                }
            }
        });

        viewHolder.binding.btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showConfirmationDialog(item);

//                String url = "https://example.com/submit";
//                String postData = "key1=value1&key2=value2"; // Your POST parameters

//                Intent intent = new Intent(mContext, OnlinePayment_WebViewActity.class);
//                intent.putExtra("url", url);
//                intent.putExtra("postData", postData);
//                mContext.startActivity(intent);

                String amt = item.getPaymentStatus().replace("₹", "").replace(",", "").trim();

                if (Float.parseFloat(amt) <= 0) {
                    String translatedText = BaseActivity.GetTranslation("No Pendings", typeOfLanguage);
                    Toast.makeText(mContext, translatedText, Toast.LENGTH_LONG).show();

                    return;
                }
                Intent intent = new Intent(mContext, OnlinePayment_WebViewActity.class);
                intent.putExtra("bookingid", item.getId());
                intent.putExtra("amount", amt);
                mContext.startActivity(intent);

            }
        });

        viewHolder.binding.btnCancellationRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShowCancellation_Dailog(item.getId(), item.getCenter_id());
            }
        });

        viewHolder.binding.btnApplyRefund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Parameter :- 1)booking_id , 2)center_id, 3)hall_id  4)title, 5)event_type 6)slot_id, 7)email
                ((MyAssetsOder_Activity) mContext).Apply_RefundRequest_Call(item.getId(), item.getCenter_id(), item.getCenter_hall_id(), item.getTitle(), item.getEvent_type(), item.getSlot());
            }
        });

        viewHolder.binding.btnReschedulingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Reschedule_Request_Dialog(item);
            }
        });

//        viewHolder.binding.tvRefundReason.setText(item.getRefundReason());
//        viewHolder.binding.tvRefundDatetime.setText(item.getRefundDatetime());

        //2023-07-21 13:24:27 date format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date d1 = sdf.parse(item.getRequestDateTime());
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM yy hh:mm a");
            String start = sdf2.format(d1);
            viewHolder.binding.tvRequestDateTime.setText(start);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (item.getPaymentStatus().contains("Failed")) {

            viewHolder.binding.tvPaymentStatus.setTextColor(Color.parseColor("#FFE64446"));
        } else {
            viewHolder.binding.tvPaymentStatus.setTextColor(Color.parseColor("#FF43A047"));
        }
    }

    private void showConfirmationDialog(MyAssetsOrder item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(BaseActivity.GetTranslation("Are you sure you want to proceed?", typeOfLanguage));
        builder.setPositiveButton(BaseActivity.GetTranslation("Yes", typeOfLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mContext.startActivity(new Intent(mContext, PlanListActivity.class).putExtra("amount", item.getAmount()).putExtra("orderid", item.getId()));
                BaseActivity.ToastDialog(mContext, "Payment Success..!");

                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton(BaseActivity.GetTranslation("Cancel", typeOfLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any other action if user cancels
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean hasRequiredPermissions(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void askPermissions(Activity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.CAMERA
                    },
                    PERMISSION_REQUEST_CODE
            );
        } else {
            ActivityCompat.requestPermissions(
                    activity,
                    new String[]{
                            Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    PERMISSION_REQUEST_CODE
            );
        }
    }

    public void ShowCancellation_Dailog(String bookingID, String centerId) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.cancel_request_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        Button btnSave = dialog.findViewById(R.id.btndone);

        MaterialEditText et_request_msg = dialog.findViewById(R.id.et_request_msg);

        AppCompatTextView cancellation_request_text = dialog.findViewById(R.id.cancellation_request_text);
        AppCompatTextView request_not_modify_text = dialog.findViewById(R.id.request_not_modify_text);
        cancellation_request_text.setText(BaseActivity.GetTranslation("Cancellation Request", typeOfLanguage));
        request_not_modify_text.setText(BaseActivity.GetTranslation("This request cannot be modified again.", typeOfLanguage));
        et_request_msg.setHint(BaseActivity.GetTranslation("Reason", typeOfLanguage));

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            String reason = et_request_msg.getText().toString().trim();

            if (TextUtils.isEmpty(reason)) {
                et_request_msg.setError("Please Enter Reason");
                et_request_msg.requestFocus();
                return;
            }

            ((MyAssetsOder_Activity) mContext).CancalRequest_Fun(reason, bookingID, centerId);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void Reschedule_Request_Dialog(MyAssetsOrder myAssetsOrder) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.reschedule_request_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        Button btnSave = dialog.findViewById(R.id.btndone);

        EditText et_date = dialog.findViewById(R.id.et_date);
        EditText et_time = dialog.findViewById(R.id.et_time);
        EditText et_select_eventtype = dialog.findViewById(R.id.et_select_eventtype);
        EditText et_select_seatingCapacity = dialog.findViewById(R.id.et_select_seatingCapacity);

        AppCompatTextView rescheduling_request_title_text = dialog.findViewById(R.id.rescheduling_request_title_text);
        rescheduling_request_title_text.setText(BaseActivity.GetTranslation("Rescheduling Request", typeOfLanguage));

        //Date Select
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = DatePickerDialog.newInstance((view1, year, monthOfYear, dayOfMonth) -> {
                    String date = "" + String.format("%02d", dayOfMonth) + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + year;
                    selectedDate = year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    et_date.setText(date);

                }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH));
                dpd.setMinDate(now);
                dpd.setTitle("Select Start Date");
                dpd.show(myAssetsOder_activity.getSupportFragmentManager(), "StartDatepickerdialog");
            }
        });

        //dropdown time
        if (myAssetsOrder.getTimings() != null) {

            ArrayList<Theater_Hall_Timing> list = (ArrayList<Theater_Hall_Timing>) myAssetsOrder.getTimings();
            ArrayList<String> timelist = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                timelist.add(list.get(i).getSelectedTiming());
            }
            if (timelist.size() > 0) {
                et_time.setOnClickListener(view -> {
                    new MaterialDialog.Builder(mContext).title("Time").items(timelist).itemsCallback((dialog1, view1, which, text) -> {
                        slotID = list.get(which).getId();
                        et_time.setText(text.toString());
                    }).show();
                });
                et_time.setVisibility(View.VISIBLE);
            }
        } else {
            BaseActivity.ToastDialog(mContext, "Timing slots is not available");
        }

//        //EventType Dropdown
//        if (myAssetsOrder.getEventType() != null) {
//            List<String> categories = new ArrayList<>();
//            for (String obj : myAssetsOrder.getEventType()) {
//                categories.add(obj);
//            }
//
//            et_select_eventtype.setOnClickListener(view -> new MaterialDialog.Builder(mContext)
//                    .title("Event Type")
//                    .items(categories)
//                    .itemsCallback((dialog1, view1, which, text) -> et_select_eventtype.setText(text.toString()))
//                    .show());
//
//        }

        //EventType Dropdown
        ArrayList<Theater_Hall_EventType> doctorList = (ArrayList<Theater_Hall_EventType>) theater_hall_eventTypes;
        eventNameList = new ArrayList<>();

        for (int i = 0; i < doctorList.size(); i++) {
            eventNameList.add(doctorList.get(i).getTitle());
        }

        if (eventNameList != null && eventNameList.size() > 0) {
            et_select_eventtype.setOnClickListener(view -> {

                if (theater_hall_eventTypes != null) {
                    new MaterialDialog.Builder(mContext).title("Select EventType").items(eventNameList).itemsCallback((dialog1, view1, which, text) -> {
                        eventIds = doctorList.get(which).getId();
                        et_select_eventtype.setText(text.toString());
                    }).show();
                } else {
                    BaseActivity.ToastDialog(mContext, "EventType is not available");
                }

            });
            et_select_eventtype.setVisibility(View.VISIBLE);
        }

        //Seating Capacity Dropdown


        ArrayList<Theater_Hall_Capacity> List = (ArrayList<Theater_Hall_Capacity>) theater_hall_capacity;

        if (List != null && List.size() > 0) {
            capacityIds = List.get(0).getId();

        } else {
            BaseActivity.ToastDialog(mContext, "Capacity is not available");
        }


//        ****** Capacity OLD Working code 09-07-2025
//        ArrayList<Theater_Hall_Capacity> List = (ArrayList<Theater_Hall_Capacity>) theater_hall_capacity;
//        capacityNameList = new ArrayList<>();
//        for (int i = 0; i < List.size(); i++) {
//            capacityNameList.add(List.get(i).getCapacity());
//        }
//
//        if (capacityNameList != null && capacityNameList.size() > 0) {
//            et_select_seatingCapacity.setOnClickListener(view -> {
//
//                if (theater_hall_capacity != null) {
//                    new MaterialDialog.Builder(mContext).title("Select Capacity").items(capacityNameList).itemsCallback((dialog1, view1, which, text) -> {
//                        capacityIds = List.get(which).getId();
//                        et_select_seatingCapacity.setText(text.toString());
//                    }).show();
//                } else {
//                    BaseActivity.ToastDialog(mContext, "Capacity is not available");
//                }
//
//            });
//            et_select_seatingCapacity.setVisibility(View.VISIBLE);
//        }


//        if (myAssetsOrder.getCapacityArray() != null) {
//            List<String> categories = new ArrayList<>();
//            for (String obj : myAssetsOrder.getCapacityArray()) {
//                categories.add(obj);
//            }
//
//            et_select_seatingCapacity.setOnClickListener(view -> new MaterialDialog.Builder(mContext)
//                    .title("Capacity")
//                    .items(categories)
//                    .itemsCallback((dialog1, view1, which, text) -> et_select_seatingCapacity.setText(text.toString()))
//                    .show());
//
//        } else {
//            BaseActivity.ToastDialog(mContext, "Capacity is not available");
//        }

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        btnSave.setOnClickListener(view -> {
            String str_eventtype = et_select_eventtype.getText().toString();
//            String str_capacity = et_select_seatingCapacity.getText().toString();
            String slot_timing = et_time.getText().toString();
            String centerid = myAssetsOrder.getCenter_id();

            if (str_eventtype.isEmpty()) {
                Toast.makeText(mContext, "Please Select Event Type", Toast.LENGTH_SHORT).show();
                return;
            }

//            if (str_capacity.isEmpty()) {
//                Toast.makeText(mContext, "Please Select Event Capacity", Toast.LENGTH_SHORT).show();
//                return;
//            }

            if (slot_timing.isEmpty()) {
                Toast.makeText(mContext, "Please Select Event Slot", Toast.LENGTH_SHORT).show();
                return;
            }

//            ((MyAssetsOder_Activity) mContext).RescheduleRequest_Fun(myAssetsOrder.getId(), str_eventtype, str_capacity, slotID, selectedDate);
            Reschedule_ConfirmationDialog(myAssetsOrder.getId(), eventIds, capacityIds, slotID, selectedDate, slot_timing, centerid);
            dialog.dismiss();

        });

        dialog.show();
    }

    private void Reschedule_ConfirmationDialog(String id, String eventIds, String str_capacity, String slotID, String selectedDate, String slot_timing, String centerid) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(BaseActivity.GetTranslation("This request cannot be modified again.", typeOfLanguage));
        builder.setPositiveButton(BaseActivity.GetTranslation("Ok", typeOfLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                ((MyAssetsOder_Activity) mContext).RescheduleRequest_Fun(id, eventIds, str_capacity, slotID, selectedDate, slot_timing, centerid);
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton(BaseActivity.GetTranslation("Cancel", typeOfLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any other action if user cancels
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void Cancellation_Details_Dialog(CancellationPojo cancellationPojo) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.cancellation_details_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);

        AppCompatTextView tv_request_datetime = dialog.findViewById(R.id.tv_request_datetime);
        AppCompatTextView tv_reason = dialog.findViewById(R.id.tv_reason);
        AppCompatTextView tv_status = dialog.findViewById(R.id.tv_status);
        AppCompatTextView tv_approved_datetime = dialog.findViewById(R.id.tv_approved_datetime);
        AppCompatTextView tv_remarks = dialog.findViewById(R.id.tv_remarks);

        tv_request_datetime.setText(cancellationPojo.getRequest_datetime());
        tv_reason.setText(cancellationPojo.getReason());
        tv_status.setText(cancellationPojo.getStatus());
        tv_approved_datetime.setText(cancellationPojo.getApproved_datetime());
        tv_remarks.setText(cancellationPojo.getRemarks());

        AppCompatTextView request_datetime_text = dialog.findViewById(R.id.request_datetime_text);
        AppCompatTextView reason_text = dialog.findViewById(R.id.reason_text);
        AppCompatTextView status_text = dialog.findViewById(R.id.status_text);
        AppCompatTextView approved_datetime_text = dialog.findViewById(R.id.approved_datetime_text);
        AppCompatTextView remarks_text = dialog.findViewById(R.id.remarks_text);

        request_datetime_text.setText(BaseActivity.GetTranslation("Request Datetime", typeOfLanguage));
        reason_text.setText(BaseActivity.GetTranslation("Reason", typeOfLanguage));
        status_text.setText(BaseActivity.GetTranslation("Status", typeOfLanguage));
        approved_datetime_text.setText(BaseActivity.GetTranslation("Approved Datetime", typeOfLanguage));
        remarks_text.setText(BaseActivity.GetTranslation("Remarks", typeOfLanguage));


        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }

    public void Reschedule_Details_Dialog(ReschedulePojo reschedulePojo) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.reschedule_details_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);

        AppCompatTextView tv_event_date = dialog.findViewById(R.id.tv_event_date);
        AppCompatTextView tv_event_type = dialog.findViewById(R.id.tv_event_type);
        AppCompatTextView tv_status = dialog.findViewById(R.id.tv_status);
        AppCompatTextView tv_approved_by = dialog.findViewById(R.id.tv_approved_by);
        AppCompatTextView tv_approved_datetime = dialog.findViewById(R.id.tv_approved_datetime);
        AppCompatTextView tv_event_capacity = dialog.findViewById(R.id.tv_event_capacity);
        AppCompatTextView tv_remarks = dialog.findViewById(R.id.tv_remarks);
        AppCompatTextView tv_slot_time = dialog.findViewById(R.id.tv_slot_time);

        tv_event_date.setText(reschedulePojo.getEvent_date());
        tv_event_type.setText(reschedulePojo.getEvent_type_title());
        tv_approved_by.setText(reschedulePojo.getApproved_by());
        tv_status.setText(reschedulePojo.getStatus());
        tv_approved_datetime.setText(reschedulePojo.getApproved_datetime());
        tv_remarks.setText(reschedulePojo.getRemarks());
        tv_event_capacity.setText(reschedulePojo.getEvent_capacity_title());
        tv_slot_time.setText(reschedulePojo.getSlot_title());

        AppCompatTextView event_date_text = dialog.findViewById(R.id.event_date_text);
        AppCompatTextView event_type_text = dialog.findViewById(R.id.event_type_text);
        AppCompatTextView event_capacity_text = dialog.findViewById(R.id.event_capacity_text);
        AppCompatTextView status_text = dialog.findViewById(R.id.status_text);
        AppCompatTextView approved_by_text = dialog.findViewById(R.id.approved_by_text);
        AppCompatTextView approved_datetime_text = dialog.findViewById(R.id.approved_datetime_text);
        AppCompatTextView remarks_text = dialog.findViewById(R.id.remarks_text);
        AppCompatTextView slot_time_text = dialog.findViewById(R.id.slot_time_text);

        event_date_text.setText(BaseActivity.GetTranslation("Event Date", typeOfLanguage));
        event_type_text.setText(BaseActivity.GetTranslation("Event Type", typeOfLanguage));
        event_capacity_text.setText(BaseActivity.GetTranslation("Event Capacity", typeOfLanguage));
        status_text.setText(BaseActivity.GetTranslation("Status", typeOfLanguage));
        approved_by_text.setText(BaseActivity.GetTranslation("Approved By", typeOfLanguage));
        approved_datetime_text.setText(BaseActivity.GetTranslation("Approved Datetime", typeOfLanguage));
        remarks_text.setText(BaseActivity.GetTranslation("Remarks", typeOfLanguage));
        slot_time_text.setText(BaseActivity.GetTranslation("Slot", typeOfLanguage));

        AppCompatTextView request_datetime_text = dialog.findViewById(R.id.request_datetime_text);

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_title)
//        AppCompatTextView tv_title;
//        @BindView(R.id.tv_bookingId)
//        AppCompatTextView tv_bookingId;
//        @BindView(R.id.tv_eventDate)
//        AppCompatTextView tv_eventDate;
//        @BindView(R.id.tv_requestDateTime)
//        AppCompatTextView tv_requestDateTime;
//        @BindView(R.id.tv_slot)
//        AppCompatTextView tv_slot;
//        @BindView(R.id.tv_amount)
//        AppCompatTextView tv_amount;
//        @BindView(R.id.tv_status)
//        AppCompatTextView tv_status;
//        @BindView(R.id.tv_payment_Status)
//        AppCompatTextView tv_payment_Status;
//        @BindView(R.id.tv_cancellation_Status)
//        AppCompatTextView tv_cancellation_Status;
//        @BindView(R.id.iv_cancellation_details)
//        ImageView iv_cancellation_details;
//        @BindView(R.id.tv_reschedule_Status)
//        AppCompatTextView tv_reschedule_Status;
//        @BindView(R.id.iv_reschedule_details)
//        ImageView iv_reschedule_details;
//
//
//        @BindView(R.id.tv_refund_amount)
//        AppCompatTextView tv_refund_amount;
//        @BindView(R.id.tv_refund_reason)
//        AppCompatTextView tv_refund_reason;
//        @BindView(R.id.tv_refund_datetime)
//        AppCompatTextView tv_refund_datetime;
//        @BindView(R.id.tv_eventType)
//        AppCompatTextView tv_eventType;
//
//        @BindView(R.id.btn_items_details)
//        MaterialButton btn_items_details;
//        @BindView(R.id.btn_paynow)
//        MaterialButton btn_paynow;
//        @BindView(R.id.btn_UploadDocuments)
//        MaterialButton btn_UploadDocuments;
//        @BindView(R.id.btn_Cancellation_request)
//        MaterialButton btn_Cancellation_request;
//        @BindView(R.id.btn_rescheduling_request)
//        MaterialButton btn_rescheduling_request;
//
//        @BindView(R.id.ll_refund_amount)
//        LinearLayout ll_refund_amount;
//
//
//        @BindView(R.id.bookingid_text)
//        AppCompatTextView bookingid_text;
//        @BindView(R.id.requestDate_text)
//        AppCompatTextView requestDate_text;
//        @BindView(R.id.event_date_text)
//        AppCompatTextView event_date_text;
//        @BindView(R.id.slots_text)
//        AppCompatTextView slots_text;
//        @BindView(R.id.amount_text)
//        AppCompatTextView amount_text;
//        @BindView(R.id.status_text)
//        AppCompatTextView status_text;
//        @BindView(R.id.refund_reason_text)
//        AppCompatTextView refund_reason_text;
//        @BindView(R.id.refund_datetime_text)
//        AppCompatTextView refund_datetime_text;
//        @BindView(R.id.paymentStatus_text)
//        AppCompatTextView paymentStatus_text;
//        @BindView(R.id.refundamounttext)
//        AppCompatTextView RefundAmount_text;
//        @BindView(R.id.cancellation_satus_text)
//        AppCompatTextView cancellation_satus_text;
//        @BindView(R.id.reschedule_satus_text)
//        AppCompatTextView reschedule_satus_text;
//        @BindView(R.id.event_type_text)
//        AppCompatTextView event_type_text;
//
//        View view;

        RowMyordersBinding binding;

        ViewHolder(RowMyordersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        myAssetsOrders.clear();
        if (charText.length() == 0) {
            myAssetsOrders.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                MyAssetsOrder g = list_search.get(i);
                if (g.getEventDate().toLowerCase().contains(charText) || g.getAmount().toLowerCase().contains(charText) || g.getId().toLowerCase().contains(charText) || g.getStatus().toLowerCase().contains(charText) || g.getEvent_type().toLowerCase().contains(charText)) {
                    myAssetsOrders.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}