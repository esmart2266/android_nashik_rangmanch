package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowBookingPaymentBinding;
import in.gov.nmc.rangmach.model.Booking_Payment;

public class My_BookingPayment_Adapter extends RecyclerView.Adapter<My_BookingPayment_Adapter.ViewHolder> {
    Context mContext;
    List<Booking_Payment> booking_payments;
    List<Booking_Payment> list_search = new ArrayList<>();
    public static int typeOfLanguage;

    public My_BookingPayment_Adapter(Context mContext, List<Booking_Payment> booking_payments) {
        this.mContext = mContext;
        this.booking_payments = booking_payments;
        list_search.addAll(booking_payments);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_booking_payment, parent, false);
//        return new ViewHolder(view);

        RowBookingPaymentBinding binding = RowBookingPaymentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return booking_payments.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Booking_Payment item = booking_payments.get(i);

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
        viewHolder.binding.receiptNoText.setText(BaseActivity.GetTranslation("Receipt No", typeOfLanguage));
        viewHolder.binding.amountText.setText(BaseActivity.GetTranslation("Amount", typeOfLanguage));
        viewHolder.binding.paymentModeText.setText(BaseActivity.GetTranslation("Payment Mode", typeOfLanguage));
        viewHolder.binding.modeDetailsText.setText(BaseActivity.GetTranslation("Mode Details", typeOfLanguage));
        viewHolder.binding.paymentDateText.setText(BaseActivity.GetTranslation("Payment Date", typeOfLanguage));

        viewHolder.binding.tvBookingId.setText(item.getId());
        viewHolder.binding.tvAmount.setText(item.getAmount());
        viewHolder.binding.tvPaymentMode.setText(item.getMode());
        viewHolder.binding.tvReceiptNo.setText(item.getReceiptNo());
        viewHolder.binding.tvPaymentDate.setText(item.getPaymentDate());
        viewHolder.binding.tvModeDetails.setText(item.getModeDetails());

    }
//
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
//        @BindView(R.id.tv_receiptNo)
//        AppCompatTextView tv_receiptNo;
//        @BindView(R.id.tv_amount)
//        AppCompatTextView tv_amount;
//        @BindView(R.id.tv_payment_mode)
//        AppCompatTextView tv_payment_mode;
//        @BindView(R.id.tv_mode_details)
//        AppCompatTextView tv_mode_details;
//        @BindView(R.id.tv_payment_date)
//        AppCompatTextView tv_payment_date;
//
//        @BindView(R.id.bookingID_text)
//        AppCompatTextView bookingID_text;
//        @BindView(R.id.receiptNo_text)
//        AppCompatTextView receiptNo_text;
//        @BindView(R.id.amount_text)
//        AppCompatTextView amount_text;
//        @BindView(R.id.payment_mode_text)
//        AppCompatTextView payment_mode_text;
//        @BindView(R.id.modeDetails_text)
//        AppCompatTextView modeDetails_text;
//        @BindView(R.id.paymentDate_text)
//        AppCompatTextView paymentDate_text;
//
//
//        View view;

        RowBookingPaymentBinding binding;

        ViewHolder(RowBookingPaymentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        booking_payments.clear();
        if (charText.length() == 0) {
            booking_payments.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                Booking_Payment g = list_search.get(i);
                if (g.getAmount().toLowerCase().contains(charText) || g.getId().toLowerCase().contains(charText) || g.getPaymentDate().toLowerCase().contains(charText) || g.getMode().toLowerCase().contains(charText) ||
                        g.getReceiptNo().toLowerCase().contains(charText)
                ) {
                    booking_payments.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}