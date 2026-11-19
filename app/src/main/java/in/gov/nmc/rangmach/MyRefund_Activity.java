package in.gov.nmc.rangmach;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
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
import android.widget.Button;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.My_BookingRefund_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityMyRefundBinding;
import in.gov.nmc.rangmach.model.Booking_Refund;
import in.gov.nmc.rangmach.model.Booking_Refund_Data;
import in.gov.nmc.rangmach.model.UserResponse;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import retrofit2.Call;

public class MyRefund_Activity extends BaseActivity<ActivityMyRefundBinding> {

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

    protected ActivityMyRefundBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMyRefundBinding.inflate(inflater);
    }

    private List<Booking_Refund> booking_refunds;
    private My_BookingRefund_Adapter myBookingRefundAdapter;

    String selected_lang;
    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_my_refund;
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

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(MyRefund_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(MyRefund_Activity.this, "mr");
        }

        binding.toolbarTitle.setText(GetTranslation("My Refund", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyRefunds();
        binding.swipeRefreshLayout.setOnRefreshListener(this::MyRefunds);
    }

    public void MyRefunds() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().myrefunds(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    Booking_Refund_Data data = (Booking_Refund_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getAllRefunds() != null && data.getAllRefunds().size() > 0) {

                        booking_refunds = data.getAllRefunds();
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
//                    ToastDialog(apiResponse);
                }
            }));
        } else {
            if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                binding.swipeRefreshLayout.setRefreshing(false);
            Utils.alert_dialog(mContext);
        }
    }

    private void setAdapter() {
        if (booking_refunds != null && booking_refunds.size() > 0 && binding.recyclerView != null) {
            myBookingRefundAdapter = new My_BookingRefund_Adapter(mContext, booking_refunds);
            binding.recyclerView.setAdapter(myBookingRefundAdapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    public void AlertDialogforPending(String bookId, String refundId) {
        new AlertDialog.Builder(mContext).setTitle(GetTranslation("Please confirm", typeOfLanguage)).setMessage(GetTranslation("This request cannot be modified again.", typeOfLanguage)).setPositiveButton(android.R.string.yes, (dialog, which) -> {

                    Map<String, String> map = new HashMap<>();
                    map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
                    map.put("bookingid", bookId);
                    map.put("refundid", refundId);
                    confirmrefund(map);
                }).setNegativeButton(android.R.string.no, null) // Dismiss on cancel
                .show();
    }

    public void ConfirmationPending(String bookId, String refundId) {
        Map<String, String> map = new HashMap<>();
        map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
        map.put("bookingid", bookId);
        map.put("refundid", refundId);
        confirmrefund(map);
    }


    public void confirmrefund(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<UserResponse> call = app.getApiRequestHelper().getAppService().confirmrefund(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserResponse userResponse = (UserResponse) object;
                    Log.e("in", "success");
                    if (userResponse != null && userResponse.getResponsecode() == 200) {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(userResponse.getMessage());
                        MyRefunds();

                    } else {
//                        Utils.showLongToast(mContext, userResponse.getMessage());
                        ToastDialog(userResponse.getMessage());
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    if (!TextUtils.isEmpty(apiResponse)) Utils.showLongToast(mContext, apiResponse);
                    ToastDialog(apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    private void ToastDialog(String message) {
        Dialog dialog = Utils.showCustomAlertDialog(mContext, R.layout.toast_dialog);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        AppCompatTextView tv_address = dialog.findViewById(R.id.tv_msg);

        tv_address.setText(message);
        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }


//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

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

                if (myBookingRefundAdapter != null) myBookingRefundAdapter.filter(newText);

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