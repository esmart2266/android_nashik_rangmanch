package in.gov.nmc.rangmach.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowBookingPenaltyBinding;
import in.gov.nmc.rangmach.model.Booking_Penalty;

public class My_BookingPenalty_Adapter extends RecyclerView.Adapter<My_BookingPenalty_Adapter.ViewHolder> {
    Context mContext;
    List<Booking_Penalty> booking_penalties;
    List<Booking_Penalty> list_search = new ArrayList<>();
    public static int typeOfLanguage;

    public My_BookingPenalty_Adapter(Context mContext, List<Booking_Penalty> booking_penalties) {
        this.mContext = mContext;
        this.booking_penalties = booking_penalties;
        list_search.addAll(booking_penalties);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_booking_penalty, parent, false);
//        return new ViewHolder(view);

        RowBookingPenaltyBinding binding = RowBookingPenaltyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return booking_penalties.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Booking_Penalty item = booking_penalties.get(i);

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
        viewHolder.binding.purposeText.setText(BaseActivity.GetTranslation("Purpose", typeOfLanguage));
        viewHolder.binding.paidText.setText(BaseActivity.GetTranslation("Paid", typeOfLanguage));
        viewHolder.binding.paymentModeText.setText(BaseActivity.GetTranslation("Payment Mode", typeOfLanguage));
        viewHolder.binding.paymentDateTimeText.setText(BaseActivity.GetTranslation("Payment DateTime", typeOfLanguage));
        viewHolder.binding.btnPaynow.setText(BaseActivity.GetTranslation("PayNow", typeOfLanguage));

        viewHolder.binding.tvBookingId.setText(item.getBookingId());
        viewHolder.binding.tvEventDate.setText(item.getEventDate());
        viewHolder.binding.tvAmount.setText(item.getAmount());
        viewHolder.binding.tvPurpose.setText(item.getPurpose());

        if (item.getPaymentMode() == null || item.getPaymentMode().isEmpty()) {
            viewHolder.binding.tvPaymentMode.setText("NA");
        } else {
            viewHolder.binding.tvPaymentMode.setText(item.getPaymentMode());
        }

        if (item.getPaymentDateTime() == null || item.getPaymentDateTime().isEmpty()) {
            viewHolder.binding.tvPaymentDateTime.setText("NA");
        } else {
            viewHolder.binding.tvPaymentDateTime.setText(item.getPaymentDateTime());
        }

        viewHolder.binding.btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(item);
            }
        });

        if (item.getIsPaid().contains("0")) {
            viewHolder.binding.tvIsPaid.setText("No");
            viewHolder.binding.tvIsPaid.setTextColor(Color.parseColor("#FF0000"));

            viewHolder.binding.llPaymentMode.setVisibility(View.GONE);
            viewHolder.binding.llPaymentDatetime.setVisibility(View.GONE);
        } else {
            viewHolder.binding.tvIsPaid.setText("Yes");
            viewHolder.binding.tvIsPaid.setTextColor(Color.parseColor("#00cc00"));

            viewHolder.binding.llPaymentMode.setVisibility(View.VISIBLE);
            viewHolder.binding.llPaymentDatetime.setVisibility(View.VISIBLE);
        }

//        viewHolder.btn_items_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, ItemsDetails_Activity.class)
//                        .putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition())));
//            }
//        });
    }

    private void showConfirmationDialog(Booking_Penalty item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Are you sure you want to proceed?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mContext.startActivity(new Intent(mContext, PlanListActivity.class).putExtra("amount", item.getAmount()).putExtra("orderid", item.getBookingId()));
                BaseActivity.ToastDialog(mContext,"Payment Succes....!");
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing or perform any other action if user cancels
                dialog.dismiss(); // Dismiss the dialog
            }
        });
        AlertDialog dialog = builder.create();
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
//        @BindView(R.id.tv_purpose)
//        AppCompatTextView tv_purpose;
//        @BindView(R.id.tv_is_paid)
//        AppCompatTextView tv_is_paid;
//        @BindView(R.id.tv_payment_mode)
//        AppCompatTextView tv_payment_mode;
//        @BindView(R.id.tv_payment_date_time)
//        AppCompatTextView tv_payment_date_time;
//
//        @BindView(R.id.ll_payment_mode)
//        LinearLayout ll_payment_mode;
//        @BindView(R.id.ll_payment_datetime)
//        LinearLayout ll_payment_datetime;
//
//        @BindView(R.id.btn_paynow)
//        MaterialButton btn_paynow;
//
//
//        @BindView(R.id.bookingID_text)
//        AppCompatTextView bookingID_text;
//        @BindView(R.id.eventdate_text)
//        AppCompatTextView eventdate_text;
//        @BindView(R.id.amount_text)
//        AppCompatTextView amount_text;
//        @BindView(R.id.purpose_text)
//        AppCompatTextView purpose_text;
//        @BindView(R.id.paid_text)
//        AppCompatTextView paid_text;
//        @BindView(R.id.payment_mode_text)
//        AppCompatTextView payment_mode_text;
//        @BindView(R.id.paymentDateTime_text)
//        AppCompatTextView paymentDateTime_text;
//
//
//        View view;


        RowBookingPenaltyBinding binding;

        ViewHolder(RowBookingPenaltyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        booking_penalties.clear();
        if (charText.length() == 0) {
            booking_penalties.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                Booking_Penalty g = list_search.get(i);
                if (g.getEventDate().toLowerCase().contains(charText) || g.getAmount().toLowerCase().contains(charText) || g.getBookingId().toLowerCase().contains(charText) || g.getPaymentMode().toLowerCase().contains(charText) || g.getPurpose().toLowerCase().contains(charText)) {
                    booking_penalties.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}