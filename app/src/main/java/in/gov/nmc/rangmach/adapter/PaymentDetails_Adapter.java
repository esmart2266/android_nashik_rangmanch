package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowPaymentdetailsBinding;
import in.gov.nmc.rangmach.model.PaymentDetail;

public class PaymentDetails_Adapter extends RecyclerView.Adapter<PaymentDetails_Adapter.ViewHolder> {
    Context mContext;
    List<PaymentDetail> myAssetsOrders;
    public static int typeOfLanguage;

    public PaymentDetails_Adapter(Context mContext, List<PaymentDetail> myAssetsOrders) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
    }

    @Override
    public PaymentDetails_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_paymentdetails, parent, false);
//        return new PaymentDetails_Adapter.ViewHolder(view);

        RowPaymentdetailsBinding binding = RowPaymentdetailsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final PaymentDetail item = myAssetsOrders.get(position);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        holder.binding.textAmount.setText(BaseActivity.GetTranslation("Amount", typeOfLanguage));
        holder.binding.textModeDetails.setText(BaseActivity.GetTranslation("Mode Details", typeOfLanguage));
        holder.binding.textMode.setText(BaseActivity.GetTranslation("Payment Mode", typeOfLanguage));
        holder.binding.textPaymentDateTime.setText(BaseActivity.GetTranslation("Payment DateTime", typeOfLanguage));

        holder.binding.tvAmount.setText(item.getAmount());
        holder.binding.tvMode.setText(item.getMode());
        holder.binding.tvModeDetails.setText(item.getModeDetails());
        holder.binding.tvPaymentDatetime.setText(item.getPaymentDatetime());
    }

    @Override
    public int getItemCount() {

        if (myAssetsOrders == null)
            return 0;

        return myAssetsOrders.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_amount)
//        AppCompatTextView tv_amount;
//        @BindView(R.id.tv_mode)
//        AppCompatTextView tv_mode;
//        @BindView(R.id.tv_mode_details)
//        AppCompatTextView tv_mode_details;
//        @BindView(R.id.tv_payment_datetime)
//        AppCompatTextView tv_payment_datetime;
//
//        View view;


        RowPaymentdetailsBinding binding;

        ViewHolder(RowPaymentdetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
