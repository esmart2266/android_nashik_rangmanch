package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.BookParking_Activity;
import in.gov.nmc.rangmach.databinding.RowParkingSlotsBinding;
import in.gov.nmc.rangmach.model.CarParking;


public class Car_Parking_Adapter extends RecyclerView.Adapter<Car_Parking_Adapter.ViewHolder> {
    Context mContext;
    List<CarParking> carparkinglist;
    public static int typeOfLanguage;

    public Car_Parking_Adapter(Context mContext, List<CarParking> myAssetsOrders) {
        this.mContext = mContext;
        this.carparkinglist = myAssetsOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_parking_slots, parent, false);
//        return new ViewHolder(view);

        RowParkingSlotsBinding binding = RowParkingSlotsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return carparkinglist.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final CarParking item = carparkinglist.get(i);

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
        viewHolder.binding.btnBooknow.setText(BaseActivity.GetTranslation("Book Now", typeOfLanguage));
        viewHolder.binding.dateText.setText(BaseActivity.GetTranslation("Date", typeOfLanguage));
        viewHolder.binding.starttimeText.setText(BaseActivity.GetTranslation("Start Time", typeOfLanguage));
        viewHolder.binding.endtimeText.setText(BaseActivity.GetTranslation("End Time", typeOfLanguage));
        viewHolder.binding.remarkText.setText(BaseActivity.GetTranslation("Remark", typeOfLanguage));
        viewHolder.binding.bikeText.setText(BaseActivity.GetTranslation("Bike", typeOfLanguage));
        viewHolder.binding.carText.setText(BaseActivity.GetTranslation("Car", typeOfLanguage));
        viewHolder.binding.bustruckText.setText(BaseActivity.GetTranslation("Bus/Truck", typeOfLanguage));
        viewHolder.binding.vipText.setText(BaseActivity.GetTranslation("VIP", typeOfLanguage));
        viewHolder.binding.quantityText.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));
        viewHolder.binding.priceText.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));

        if (item.getShowVip() == 0) {
            viewHolder.binding.llVip.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llVip.setVisibility(View.VISIBLE);
        }

        if (item.getBikeQty() == 0) {
            viewHolder.binding.llBike.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llBike.setVisibility(View.VISIBLE);
        }

        if (item.getCarQty() == 0) {
            viewHolder.binding.llCar.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llCar.setVisibility(View.VISIBLE);
        }

        if (item.getBusQty() == 0) {
            viewHolder.binding.llBustruck.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llBustruck.setVisibility(View.VISIBLE);
        }

        viewHolder.binding.tvDate.setText(item.getDate());
        viewHolder.binding.tvStartTime.setText(item.getStartTime());
        viewHolder.binding.tvEndTime.setText(item.getEndTime());
        viewHolder.binding.centerName.setText(item.getName());
        viewHolder.binding.tvBikeQty.setText(item.getBikeQty() + "");
        viewHolder.binding.tvBikePrice.setText(item.getBikePrice() + "");
        viewHolder.binding.tvCarQty.setText(item.getCarQty() + "");
        viewHolder.binding.tvCarPrice.setText(item.getCarPrice() + "");
        viewHolder.binding.tvBusQty.setText(item.getBusQty() + "");
        viewHolder.binding.tvBusPrice.setText(item.getBusPrice() + "");
        viewHolder.binding.tvVipQty.setText(item.getVipQty() + "");
        viewHolder.binding.tvVipPrice.setText(item.getVipPrice() + "");
        viewHolder.binding.tvRemark.setText(item.getRemarks() + "");

        viewHolder.binding.btnBooknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContext.startActivity(new Intent(mContext, BookParking_Activity.class)
                        .putExtra("data", carparkinglist.get(viewHolder.getAdapterPosition())));
            }
        });
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

//        @BindView(R.id.tv_date)
//        AppCompatTextView tv_date;
//        @BindView(R.id.tv_start_time)
//        AppCompatTextView tv_start_time;
//        @BindView(R.id.tv_end_time)
//        AppCompatTextView tv_end_time;
//        @BindView(R.id.tv_bike_qty)
//        AppCompatTextView tv_bike_qty;
//        @BindView(R.id.tv_bike_price)
//        AppCompatTextView tv_bike_price;
//        @BindView(R.id.tv_car_qty)
//        AppCompatTextView tv_car_qty;
//        @BindView(R.id.tv_car_price)
//        AppCompatTextView tv_car_price;
//        @BindView(R.id.tv_bus_qty)
//        AppCompatTextView tv_bus_qty;
//        @BindView(R.id.tv_bus_price)
//        AppCompatTextView tv_bus_price;
//        @BindView(R.id.tv_vip_qty)
//        AppCompatTextView tv_vip_qty;
//        @BindView(R.id.tv_vip_price)
//        AppCompatTextView tv_vip_price;
//        @BindView(R.id.tv_remark)
//        AppCompatTextView tv_remark;
//        @BindView(R.id.center_name)
//        AppCompatTextView center_name;
//        @BindView(R.id.btn_booknow)
//        Button btn_booknow;
//
//
//        @BindView(R.id.ll_vip)
//        LinearLayout ll_vip;
//
//        @BindView(R.id.ll_bike)
//        LinearLayout ll_bike;
//        @BindView(R.id.ll_car)
//        LinearLayout ll_car;
//        @BindView(R.id.ll_bustruck)
//        LinearLayout ll_bustruck;
//
//        @BindView(R.id.date_text)
//        AppCompatTextView date_text;
//        @BindView(R.id.starttime_text)
//        AppCompatTextView starttime_text;
//        @BindView(R.id.endtime_text)
//        AppCompatTextView endtime_text;
//        @BindView(R.id.quantity_text)
//        AppCompatTextView quantity_text;
//        @BindView(R.id.price_text)
//        AppCompatTextView price_text;
//        @BindView(R.id.bike_text)
//        AppCompatTextView bike_text;
//        @BindView(R.id.car_text)
//        AppCompatTextView car_text;
//        @BindView(R.id.bustruck_text)
//        AppCompatTextView bustruck_text;
//        @BindView(R.id.vip_text)
//        AppCompatTextView vip_text;
//        @BindView(R.id.remark_text)
//        AppCompatTextView remark_text;
//        View view;


        RowParkingSlotsBinding binding;

        ViewHolder(RowParkingSlotsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}