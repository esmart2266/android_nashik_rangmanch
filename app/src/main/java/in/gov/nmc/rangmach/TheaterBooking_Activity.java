package in.gov.nmc.rangmach;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.TheaterBooking_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityTheaterBookingBinding;
import in.gov.nmc.rangmach.model.AllTheater_Data;
import in.gov.nmc.rangmach.model.AllTheaters;
import in.gov.nmc.rangmach.utils.Utils;
import retrofit2.Call;

public class TheaterBooking_Activity extends BaseActivity<ActivityTheaterBookingBinding> {

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.rv_theaters)
//    RecyclerView rv_theaters;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.tv_error)
//    AppCompatTextView tv_error;

    private TheaterBooking_Adapter allEventsAdapter;
    private List<AllTheaters> allTheatersList;

    Map<String, String> map = new HashMap<>();

    protected ActivityTheaterBookingBinding inflateBinding(LayoutInflater inflater) {
        return ActivityTheaterBookingBinding.inflate(inflater);
    }


//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_theater_booking;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.rvTheaters.setLayoutManager(new LinearLayoutManager(mContext));
        binding.rvTheaters.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        AllTheaters(map);

        binding.swipeRefreshLayout.setOnRefreshListener(this::refreshdata);
    }

    public void refreshdata() {
        AllTheaters(map);
    }


    public void AllTheaters(Map<String, String> params1) {
        if (cd.isConnectingToInternet()) {
            Call<AllTheater_Data> call = app.getApiRequestHelper().getAppService().allassets(params1);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                        @Override
                        public void onSuccess(Object object) {
                            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            AllTheater_Data allProductsData = (AllTheater_Data) object;
                            Log.e("in", "success");
                            if (allProductsData != null) {
                                if (allProductsData.getResponsecode() == 200 && allProductsData.getResponsecode() != null && allProductsData.getData().size() > 0) {
                                    allTheatersList = allProductsData.getData();

                                    allEventsAdapter = new TheaterBooking_Adapter(mContext, allTheatersList);
                                    binding.rvTheaters.setAdapter(allEventsAdapter);
//                                    setSearchViewData();
                                    binding.rvTheaters.setVisibility(View.VISIBLE);
                                    binding.tvError.setVisibility(View.GONE);

                                } else {
                                    binding.rvTheaters.setVisibility(View.GONE);
                                    binding.tvError.setVisibility(View.VISIBLE);
                                }
                            } else {
                                Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                            }
                        }

                        @Override
                        public void onFailure(String apiResponse) {
                            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                                binding.swipeRefreshLayout.setRefreshing(false);
                            Log.e("in", "error " + apiResponse);
                            Utils.showLongToast(mContext, apiResponse);
                        }
                    }
            );
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