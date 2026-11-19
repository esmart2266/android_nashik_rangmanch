package in.gov.nmc.rangmach.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.MyRefund_Activity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowBookingRefundBinding;
import in.gov.nmc.rangmach.model.Booking_Refund;
import in.gov.nmc.rangmach.utils.Utils;

public class My_BookingRefund_Adapter extends RecyclerView.Adapter<My_BookingRefund_Adapter.ViewHolder> {
    Context mContext;
    List<Booking_Refund> booking_refunds;
    List<Booking_Refund> list_search = new ArrayList<>();
    public static int typeOfLanguage;

    public My_BookingRefund_Adapter(Context mContext, List<Booking_Refund> booking_refunds) {
        this.mContext = mContext;
        this.booking_refunds = booking_refunds;
        list_search.addAll(booking_refunds);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_booking_refund, parent, false);
//        return new ViewHolder(view);

        RowBookingRefundBinding binding = RowBookingRefundBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return booking_refunds.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Booking_Refund item = booking_refunds.get(i);

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
        viewHolder.binding.bookingIDText.setText(BaseActivity.GetTranslation("Booking ID", typeOfLanguage));
        viewHolder.binding.eventdateText.setText(BaseActivity.GetTranslation("Event Date", typeOfLanguage));
        viewHolder.binding.amountText.setText(BaseActivity.GetTranslation("Amount", typeOfLanguage));
        viewHolder.binding.detailsText.setText(BaseActivity.GetTranslation("Details", typeOfLanguage));
        viewHolder.binding.paymentModeText.setText(BaseActivity.GetTranslation("Payment Mode", typeOfLanguage));
        viewHolder.binding.entryDateText.setText(BaseActivity.GetTranslation("Entry Date", typeOfLanguage));
        viewHolder.binding.remarksText.setText(BaseActivity.GetTranslation("Remark", typeOfLanguage));
        viewHolder.binding.statusText.setText(BaseActivity.GetTranslation("Status", typeOfLanguage));

        viewHolder.binding.tvBookingId.setText(item.getBookingId());
        viewHolder.binding.tvEventDate.setText(item.getEventDate());
        viewHolder.binding.tvAmount.setText(item.getAmount());
        viewHolder.binding.tvPaymentMode.setText(item.getPaymentMode());
        viewHolder.binding.tvDetails.setText(item.getModeDetails());
        viewHolder.binding.tvEntrydate.setText(item.getEntryDate());
        viewHolder.binding.tvRemarks.setText(item.getRemarks());

        if (item.getIsConfirmedByCustomer() != null && item.getIsConfirmedByCustomer().contains("0")) {
            viewHolder.binding.btnIsConfirmedByCustomer.setText(BaseActivity.GetTranslation("Not Confirmed", typeOfLanguage));
            viewHolder.binding.btnIsConfirmedByCustomer.setBackgroundColor(Color.parseColor("#FF0000"));

            if (item.getBookingId() != null && item.getId() != null) {
                viewHolder.binding.btnIsConfirmedByCustomer.setOnClickListener(view ->
                        ((MyRefund_Activity) mContext).AlertDialogforPending(item.getBookingId(), item.getId()));
            }
        } else if (item.getIsConfirmedByCustomer() != null) {
            viewHolder.binding.btnIsConfirmedByCustomer.setText(BaseActivity.GetTranslation("Confirmed", typeOfLanguage));
            viewHolder.binding.btnIsConfirmedByCustomer.setBackgroundColor(Color.parseColor("#16D52A"));

            viewHolder.binding.btnIsConfirmedByCustomer.setOnClickListener(view ->
                    ToastDialog("Already confirmed"));
        }


//        viewHolder.btn_isConfirmedByCustomer.setOnClickListener(view -> ((MyRefund_Activity) mContext).ConfirmationPending(booking_refunds));

//        viewHolder.btn_items_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, ItemsDetails_Activity.class)
//                        .putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition())));
//            }
//        });
    }

    private void ToastDialog(String message) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.toast_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        AppCompatTextView tv_address = dialog.findViewById(R.id.tv_msg);

        tv_address.setText(message);
        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
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

//        @BindView(R.id.tv_bookingId)
//        AppCompatTextView tv_bookingId;
//        @BindView(R.id.tv_eventDate)
//        AppCompatTextView tv_eventDate;
//        @BindView(R.id.tv_amount)
//        AppCompatTextView tv_amount;
//        @BindView(R.id.tv_payment_mode)
//        AppCompatTextView tv_payment_mode;
//        @BindView(R.id.tv_remarks)
//        AppCompatTextView tv_remarks;
//        @BindView(R.id.tv_details)
//        AppCompatTextView tv_details;
//        @BindView(R.id.tv_entrydate)
//        AppCompatTextView tv_entrydate;
//
//        @BindView(R.id.ll_payment_mode)
//        LinearLayout ll_payment_mode;
//        @BindView(R.id.ll_payment_datetime)
//        LinearLayout ll_payment_datetime;
//
//        @BindView(R.id.btn_isConfirmedByCustomer)
//        Button btn_isConfirmedByCustomer;
//
//        @BindView(R.id.bookingID_text)
//        AppCompatTextView bookingID_text;
//        @BindView(R.id.eventdate_text)
//        AppCompatTextView eventdate_text;
//        @BindView(R.id.amount_text)
//        AppCompatTextView amount_text;
//        @BindView(R.id.details_text)
//        AppCompatTextView details_text;
//        @BindView(R.id.payment_mode_text)
//        AppCompatTextView payment_mode_text;
//        @BindView(R.id.entry_date_text)
//        AppCompatTextView entry_date_text;
//        @BindView(R.id.remarks_text)
//        AppCompatTextView remarks_text;
//        @BindView(R.id.status_text)
//        AppCompatTextView status_text;
//        View view;

        RowBookingRefundBinding binding;

        ViewHolder(RowBookingRefundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        booking_refunds.clear();
        if (charText.length() == 0) {
            booking_refunds.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                Booking_Refund g = list_search.get(i);
                if (g.getEventDate().toLowerCase().contains(charText) || g.getAmount().toLowerCase().contains(charText) || g.getBookingId().toLowerCase().contains(charText) || g.getPaymentMode().toLowerCase().contains(charText) ||
                        g.getRemarks().toLowerCase().contains(charText) || g.getIsConfirmedByCustomer().toLowerCase().contains(charText)
                ) {
                    booking_refunds.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}