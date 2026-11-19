package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowParkingReportsBinding;
import in.gov.nmc.rangmach.model.CarParkingReport;


public class Car_Parking_Reports_Adapter extends RecyclerView.Adapter<Car_Parking_Reports_Adapter.ViewHolder> {
    Context mContext;
    List<CarParkingReport> carParkingReportList;
    public static int typeOfLanguage;

    public Car_Parking_Reports_Adapter(Context mContext, List<CarParkingReport> myAssetsOrders) {
        this.mContext = mContext;
        this.carParkingReportList = myAssetsOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_parking_reports, parent, false);
//        return new ViewHolder(view);

        RowParkingReportsBinding binding = RowParkingReportsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return carParkingReportList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final CarParkingReport item = carParkingReportList.get(i);

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

        viewHolder.binding.textId.setText(BaseActivity.GetTranslation("ID", typeOfLanguage));
        viewHolder.binding.textTotal.setText(BaseActivity.GetTranslation("Total", typeOfLanguage));
        viewHolder.binding.textBookingDatetime.setText(BaseActivity.GetTranslation("Booking DateTime :", typeOfLanguage));
        viewHolder.binding.textQuantity.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));
        viewHolder.binding.textQuantity2.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));
        viewHolder.binding.textQuantity3.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));
        viewHolder.binding.textQuantity4.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));
        viewHolder.binding.textPrice.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
        viewHolder.binding.textPrice2.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
        viewHolder.binding.textPrice3.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
        viewHolder.binding.textPrice4.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
        viewHolder.binding.textBike.setText(BaseActivity.GetTranslation("Bike", typeOfLanguage));
        viewHolder.binding.textBikenumber.setText(BaseActivity.GetTranslation("Bike Number", typeOfLanguage));
        viewHolder.binding.textCar.setText(BaseActivity.GetTranslation("Car", typeOfLanguage));
        viewHolder.binding.textCarnumber.setText(BaseActivity.GetTranslation("Car Number", typeOfLanguage));
        viewHolder.binding.textbus.setText(BaseActivity.GetTranslation("Bus/Truck", typeOfLanguage));
        viewHolder.binding.textBusnumber.setText(BaseActivity.GetTranslation("Bus Number", typeOfLanguage));
        viewHolder.binding.textVip.setText(BaseActivity.GetTranslation("VIP", typeOfLanguage));
        viewHolder.binding.textVipnumber.setText(BaseActivity.GetTranslation("VIP Number", typeOfLanguage));


        if (item.getBikeQty()==0) {
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

        if (item.getVipQty() == 0) {
            viewHolder.binding.llVip.setVisibility(View.GONE);
        } else {
            viewHolder.binding.llVip.setVisibility(View.VISIBLE);
        }

        viewHolder.binding.tvEntryDatetime.setText(item.getEntryDatetime());
        viewHolder.binding.centerName.setText(item.getName());
        viewHolder.binding.tvBikeQty.setText(item.getBikeQty()+"");
        viewHolder.binding.tvBikePrice.setText(item.getBikePrice() + "");
        viewHolder.binding.tvCarQty.setText(item.getCarQty() + "");
        viewHolder.binding.tvCarPrice.setText(item.getCarPrice() + "");
        viewHolder.binding.tvBusQty.setText(item.getBusQty() + "");
        viewHolder.binding.tvBusPrice.setText(item.getBusPrice() + "");
        viewHolder.binding.tvVipQty.setText(item.getVipQty() + "");
        viewHolder.binding.tvVipPrice.setText(item.getVipPrice() + "");

        viewHolder.binding.tvId.setText(item.getBusQty()+"");
        viewHolder.binding.tvTotal.setText(item.getTotal() + "");
        viewHolder.binding.tvBikeNumbers.setText(item.getBikeNumbers());
        viewHolder.binding.tvCarNumbers.setText(item.getCarNumbers());
        viewHolder.binding.tvBusNumbers.setText(item.getBusNumbers());
        viewHolder.binding.tvVipNumbers.setText(item.getVipNumbers());

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
        
//        @BindView(R.id.center_name)
//        AppCompatTextView center_name;
//        @BindView(R.id.tv_entry_datetime)
//        AppCompatTextView tv_entry_datetime;
//        @BindView(R.id.tv_id)
//        AppCompatTextView tv_id;
//        @BindView(R.id.tv_total)
//        AppCompatTextView tv_total;
//
//
//        @BindView(R.id.tv_bike_qty)
//        AppCompatTextView tv_bike_qty;
//        @BindView(R.id.tv_bike_price)
//        AppCompatTextView tv_bike_price;
//        @BindView(R.id.tv_bike_numbers)
//        AppCompatTextView tv_bike_numbers;
//
//
//        @BindView(R.id.tv_car_qty)
//        AppCompatTextView tv_car_qty;
//        @BindView(R.id.tv_car_price)
//        AppCompatTextView tv_car_price;
//        @BindView(R.id.tv_car_numbers)
//        AppCompatTextView tv_car_numbers;
//
//
//        @BindView(R.id.tv_bus_qty)
//        AppCompatTextView tv_bus_qty;
//        @BindView(R.id.tv_bus_price)
//        AppCompatTextView tv_bus_price;
//        @BindView(R.id.tv_bus_numbers)
//        AppCompatTextView tv_bus_numbers;
//
//
//        @BindView(R.id.tv_vip_qty)
//        AppCompatTextView tv_vip_qty;
//        @BindView(R.id.tv_vip_price)
//        AppCompatTextView tv_vip_price;
//        @BindView(R.id.tv_vip_numbers)
//        AppCompatTextView tv_vip_numbers;
//
//
//        @BindView(R.id.ll_vip)
//        LinearLayout ll_vip;
//        @BindView(R.id.ll_bike)
//        LinearLayout ll_bike;
//        @BindView(R.id.ll_car)
//        LinearLayout ll_car;
//        @BindView(R.id.ll_bustruck)
//        LinearLayout ll_bustruck;
//
//
//        @BindView(R.id.text_id)
//        AppCompatTextView text_id;
//        @BindView(R.id.text_total)
//        AppCompatTextView text_total;
//        @BindView(R.id.text_booking_datetime)
//        AppCompatTextView text_booking_datetime;
//        @BindView(R.id.text_quantity)
//        AppCompatTextView text_quantity;
//        @BindView(R.id.text_price)
//        AppCompatTextView text_price;
//
//        @BindView(R.id.text_quantity2)
//        AppCompatTextView text_quantity2;
//        @BindView(R.id.text_price2)
//        AppCompatTextView text_price2;
//
//        @BindView(R.id.text_quantity3)
//        AppCompatTextView text_quantity3;
//        @BindView(R.id.text_price3)
//        AppCompatTextView text_price3;
//
//        @BindView(R.id.text_quantity4)
//        AppCompatTextView text_quantity4;
//        @BindView(R.id.text_price4)
//        AppCompatTextView text_price4;
//
//        @BindView(R.id.text_bike)
//        AppCompatTextView text_bike;
//        @BindView(R.id.text_bikenumber)
//        AppCompatTextView text_bikenumber;
//        @BindView(R.id.text_car)
//        AppCompatTextView text_car;
//        @BindView(R.id.text_carnumber)
//        AppCompatTextView text_carnumber;
//        @BindView(R.id.textbus)
//        AppCompatTextView textbus;
//        @BindView(R.id.text_busnumber)
//        AppCompatTextView text_busnumber;
//        @BindView(R.id.text_vip)
//        AppCompatTextView text_vip;
//        @BindView(R.id.text_vipnumber)
//        AppCompatTextView text_vipnumber;
//
//        View view;


        RowParkingReportsBinding binding;

        ViewHolder(RowParkingReportsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}