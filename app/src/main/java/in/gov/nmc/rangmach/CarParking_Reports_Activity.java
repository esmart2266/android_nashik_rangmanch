package in.gov.nmc.rangmach;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import in.gov.nmc.rangmach.adapter.Car_Parking_Reports_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityCarParkingReportsBinding;
import in.gov.nmc.rangmach.model.CarParkingReport;
import in.gov.nmc.rangmach.model.CarParkingReport_Data;
import in.gov.nmc.rangmach.utils.Utils;

public class CarParking_Reports_Activity extends BaseActivity<ActivityCarParkingReportsBinding> {

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//    @BindView(R.id.tv_error)
//    TextView tv_error;

    private List<CarParkingReport> carParkingReportList;
    private Car_Parking_Reports_Adapter car_parking_reports_adapter;

    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_car_parking_reports;
//    }

    protected ActivityCarParkingReportsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityCarParkingReportsBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        binding.toolbarTitle.setText(GetTranslation("Car Parking Report", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        CarParking_Call();
        binding.swipeRefreshLayout.setOnRefreshListener(this::CarParking_Call);

    }


    public void CarParking_Call() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().parkingreport(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    CarParkingReport_Data data = (CarParkingReport_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getData() != null &&
                            data.getData().size() > 0) {

                        carParkingReportList = data.getData();
                        setAdapter();

                    } else if (binding.recyclerView != null && binding.tvError != null) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.tvError.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    setAdapter();
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    Utils.showLongToast(mContext, apiResponse);
                }
            }));
        } else {
            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                binding.swipeRefreshLayout.setRefreshing(false);
            Utils.alert_dialog(mContext);
        }
    }


    private void setAdapter() {
        if (carParkingReportList != null && carParkingReportList.size() > 0 && binding.recyclerView != null) {
            car_parking_reports_adapter = new Car_Parking_Reports_Adapter(mContext, carParkingReportList);
            binding.recyclerView.setAdapter(car_parking_reports_adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                Utils.hideSoftKeyboard(this);
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}