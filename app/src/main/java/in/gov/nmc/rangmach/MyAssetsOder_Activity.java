package in.gov.nmc.rangmach;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.MyAssetsOrder_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityMyAssetsOderBinding;
import in.gov.nmc.rangmach.model.Centers;
import in.gov.nmc.rangmach.model.MyAssetsOrder;
import in.gov.nmc.rangmach.model.MyAssetsOrder_Data;
import in.gov.nmc.rangmach.model.UserResponse;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import retrofit2.Call;

public class MyAssetsOder_Activity extends BaseActivity<ActivityMyAssetsOderBinding> {

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
    private List<MyAssetsOrder> myAssetsOrderList;
    private MyAssetsOrder_Adapter myAssetsOrder_adapter;
    private ArrayList<Centers> centersList;
    String selected_lang;
    public static int typeOfLanguage;
    public static boolean refreshpage = false;
//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_my_assets_oder;
//    }

    protected ActivityMyAssetsOderBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMyAssetsOderBinding.inflate(inflater);
    }

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

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        binding.toolbarTitle.setText(GetTranslation("My Bookings", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyOrders();
        binding.swipeRefreshLayout.setOnRefreshListener(this::MyOrders);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Replace this with your actual boolean condition
        if (refreshpage) {
            MyOrders();
            refreshpage = false;
        }
    }

    public void MyOrders() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().myassetsorder(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    MyAssetsOrder_Data data = (MyAssetsOrder_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getData() != null && data.getData().size() > 0) {
                        myAssetsOrderList = data.getData();
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

    public void CancalRequest_Fun(String reason, String bookId, String centerid) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("reason", reason);
        map.put("bookingid", bookId);
        map.put("center_id", centerid);
        CancelRequest_Call(map);
    }

    public void CancelRequest_Call(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<UserResponse> call = app.getApiRequestHelper().getAppService().booking_cancel_request(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserResponse userResponse = (UserResponse) object;
                    Log.e("in", "success");
                    if (userResponse != null && userResponse.getResponsecode() == 200) {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());
                        MyOrders();
                    } else {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
//                        Utils.showLongToast(mContext, apiResponse);
                        ToastDialog(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void RescheduleRequest_Fun(String bookId, String eventIds, String str_capacity, String slotID, String selectedDate, String slot_timing, String centerid) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("bookingid", bookId);
        map.put("event_date", selectedDate);
        map.put("slot_id", slotID);
        map.put("event_type", eventIds);
        map.put("event_capacity", str_capacity);
        map.put("slot_timing", slot_timing);
        map.put("center_id", centerid);
        RequestRequest_Call(map);
    }

    public void RequestRequest_Call(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<UserResponse> call = app.getApiRequestHelper().getAppService().booking_reschedule_request(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserResponse userResponse = (UserResponse) object;
                    Log.e("in", "success");
                    if (userResponse != null && userResponse.getResponsecode() == 200) {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());

                        myAssetsOrder_adapter.notifyDataSetChanged();
                    } else {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
//                        Utils.showLongToast(mContext, apiResponse);
                        ToastDialog(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void Apply_RefundRequest_Call(String booking_id, String center_id, String hall_id, String title, String event_type, String slot_id) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();

            Map<String, String> map = new HashMap<>();
            map.put("booking_id", booking_id);
            map.put("email", app.getPreferences().getLoggedInUser().getData().getemailid());
            map.put("center_id", center_id);
            map.put("hall_id", hall_id);
            map.put("title", title);
            map.put("event_type", event_type);
            map.put("slot_id", slot_id);

            Call<UserResponse> call = app.getApiRequestHelper().getAppService().refund_request(map);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserResponse userResponse = (UserResponse) object;
                    Log.e("in", "success");
                    if (userResponse != null && userResponse.getResponsecode() == 200) {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());
                    } else {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(mContext, userResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse))
//                        Utils.showLongToast(mContext, apiResponse);
                        ToastDialog(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    private void setAdapter() {
        if (myAssetsOrderList != null && myAssetsOrderList.size() > 0 && binding.recyclerView != null) {
            myAssetsOrder_adapter = new MyAssetsOrder_Adapter(mContext, myAssetsOrderList, this, app);
            binding.recyclerView.setAdapter(myAssetsOrder_adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Text Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (myAssetsOrder_adapter != null)
                    myAssetsOrder_adapter.filter(newText);

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
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