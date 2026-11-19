package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.HallBooking_Activity;
import in.gov.nmc.rangmach.databinding.RowPaymentChartBinding;
import in.gov.nmc.rangmach.model.Theater_Hall_FeesItems;

public class PaymentChart_Adapter extends RecyclerView.Adapter<PaymentChart_Adapter.ViewHolder> {
    Context mContext;
    List<Theater_Hall_FeesItems> hallFeesItems;
    String selectedDate;
    String slotID;
    public static int typeOfLanguage;

    public PaymentChart_Adapter(Context mContext, List<Theater_Hall_FeesItems> videos, String date, String Sid) {
        this.mContext = mContext;
        this.hallFeesItems = videos;
        selectedDate = date;
        slotID = Sid;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_chart, parent, false);
//        return new ViewHolder(view);

        RowPaymentChartBinding binding = RowPaymentChartBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return hallFeesItems.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final Theater_Hall_FeesItems item = hallFeesItems.get(i);

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
        viewHolder.binding.availableforText.setText(BaseActivity.GetTranslation("Available For", typeOfLanguage));
        viewHolder.binding.depositeText.setText(BaseActivity.GetTranslation("Deposit", typeOfLanguage));
        viewHolder.binding.oddDayRentText.setText(BaseActivity.GetTranslation("Odd Day Rent", typeOfLanguage));
        viewHolder.binding.holidayRentText.setText(BaseActivity.GetTranslation("Holiday Rent", typeOfLanguage));

        viewHolder.binding.tvAvailablefor.setText(item.getItem());
        viewHolder.binding.tvDeposit.setText(item.getDeposit());
        viewHolder.binding.tvOddRent.setText(item.getOddDayRent());
        viewHolder.binding.tvHolidayRent.setText(item.getHolidayRent());


        if (HallBooking_Activity.isHolidayDate) {  //Holiday Rent
            viewHolder.binding.cbHolidayRent.setChecked(false);
            viewHolder.binding.cbOddDayRent.setChecked(false);
            viewHolder.binding.cbDeposit.setChecked(true);

            HallBooking_Activity.selectedFeesID = item.getId();
//            HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit()) + Float.parseFloat(item.getHolidayRent());
            HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit());

            ((HallBooking_Activity) mContext).updateBookNowText();


//            Toast.makeText(mContext, "isHolidayDate = " + HallBooking_Activity.isHolidayDate, Toast.LENGTH_SHORT).show();
        } else {  //Odd Day Rent
            viewHolder.binding.cbHolidayRent.setChecked(false);
            viewHolder.binding.cbOddDayRent.setChecked(false);
            viewHolder.binding.cbDeposit.setChecked(true);

            HallBooking_Activity.selectedFeesID = item.getId();
//            HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit()) + Float.parseFloat(item.getOddDayRent());
            HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit());

            ((HallBooking_Activity) mContext).updateBookNowText();

//            Toast.makeText(mContext, "isHolidayDate = " + HallBooking_Activity.isHolidayDate, Toast.LENGTH_SHORT).show();
        }

//        viewHolder.tv_OddRent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((HallBooking_Activity) mContext).OddDayRent(viewHolder.getAdapterPosition());
//                viewHolder.img_oddrent.setVisibility(View.VISIBLE);
//                viewHolder.img_holidayrent.setVisibility(View.GONE);
//
//                HallBooking_Activity.selectedFeesID = item.getId();
//                HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit()) + Float.parseFloat(item.getOddDayRent());
//
//
//            }
//        });
//
//
//        viewHolder.tv_HolidayRent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                viewHolder.img_oddrent.setVisibility(View.GONE);
//                viewHolder.img_holidayrent.setVisibility(View.VISIBLE);
//
//                HallBooking_Activity.selectedFeesID = item.getId();
//                HallBooking_Activity.SelectedRent = Float.parseFloat(item.getDeposit()) + Float.parseFloat(item.getHolidayRent());
//
//                ((HallBooking_Activity) mContext).HolidayRent(viewHolder.getAdapterPosition());
//            }
//        });
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

        RowPaymentChartBinding binding;

        ViewHolder(RowPaymentChartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
