package in.gov.nmc.rangmach.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowPenaltyBinding;
import in.gov.nmc.rangmach.model.Penalty;

public class Penalty_Adapter extends RecyclerView.Adapter<Penalty_Adapter.ViewHolder> {
    Context mContext;
    List<Penalty> penalties;
    public static int typeOfLanguage;

    public Penalty_Adapter(Context mContext, List<Penalty> penalties) {
        this.mContext = mContext;
        this.penalties = penalties;
    }

    @Override
    public Penalty_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_penalty, parent, false);
//        return new Penalty_Adapter.ViewHolder(view);

        RowPenaltyBinding binding = RowPenaltyBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Penalty item = penalties.get(position);

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
        holder.binding.textPenaultyFor.setText(BaseActivity.GetTranslation("Penalty For", typeOfLanguage));
        holder.binding.textEntryDateTime.setText(BaseActivity.GetTranslation("Entry Date Time", typeOfLanguage));
        holder.binding.textPaymentDate.setText(BaseActivity.GetTranslation("Payment Date", typeOfLanguage));
        holder.binding.textPaymentMode.setText(BaseActivity.GetTranslation("Payment Mode", typeOfLanguage));

        holder.binding.tvAmount.setText(item.getAmount());
        holder.binding.tvPenaultyFor.setText(item.getPenaultyFor());
        holder.binding.tvEntryDateTime.setText(item.getEntryDateTime());
        holder.binding.tvPaymentDate.setText(item.getPayment_date());
        holder.binding.tvPaymentMode.setText(item.getPayment_mode());

        holder.binding.btnPaynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(item);
            }
        });
    }

    private void showConfirmationDialog(Penalty item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(BaseActivity.GetTranslation("Are you sure you want to proceed?", typeOfLanguage));
        builder.setPositiveButton(BaseActivity.GetTranslation("Yes", typeOfLanguage), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                mContext.startActivity(new Intent(mContext, PlanListActivity.class).putExtra("amount", item.getAmount()).putExtra("orderid", item.getId()));

                BaseActivity.ToastDialog(mContext,"Payment Success...!");
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

    @Override
    public int getItemCount() {

        if (penalties == null)
            return 0;

        return penalties.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_amount)
//        AppCompatTextView tv_amount;
//        @BindView(R.id.tv_penaulty_for)
//        AppCompatTextView tv_penaulty_for;
//        @BindView(R.id.tv_entry_date_time)
//        AppCompatTextView tv_entry_date_time;
//        View view;

        RowPenaltyBinding binding;

        ViewHolder(RowPenaltyBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
