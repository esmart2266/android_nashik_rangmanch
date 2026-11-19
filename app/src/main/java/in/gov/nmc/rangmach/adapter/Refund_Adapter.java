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
import in.gov.nmc.rangmach.databinding.RowRefundBinding;
import in.gov.nmc.rangmach.model.Refund;

public class Refund_Adapter extends RecyclerView.Adapter<Refund_Adapter.ViewHolder> {
    Context mContext;
    List<Refund> myAssetsOrders;
    public static int typeOfLanguage;

    public Refund_Adapter(Context mContext, List<Refund> myAssetsOrders) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
    }

    @Override
    public Refund_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_refund, parent, false);
//        return new Refund_Adapter.ViewHolder(view);

        RowRefundBinding binding = RowRefundBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Refund item = myAssetsOrders.get(position);

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
        holder.binding.textMode.setText(BaseActivity.GetTranslation("Mode", typeOfLanguage));
        holder.binding.textModeDetails.setText(BaseActivity.GetTranslation("Mode Details", typeOfLanguage));
        holder.binding.textEntryDateTime.setText(BaseActivity.GetTranslation("Entry Date Time", typeOfLanguage));
        holder.binding.textRemarks.setText(BaseActivity.GetTranslation("Remarks", typeOfLanguage));

        holder.binding.tvAmount.setText(item.getAmount());
        holder.binding.tvMode.setText(item.getPaymentMode());
        holder.binding.tvModeDetails.setText(item.getModeDetails());
        holder.binding.tvEntryDateTime.setText(item.getEntryDateTime());
        holder.binding.tvRemarks.setText(item.getRemarks());
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
//        @BindView(R.id.tv_entry_date_time)
//        AppCompatTextView tv_entry_date_time;
//        @BindView(R.id.tv_remarks)
//        AppCompatTextView tv_remarks;
//
//        View view;
        RowRefundBinding binding;

        ViewHolder(RowRefundBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}

