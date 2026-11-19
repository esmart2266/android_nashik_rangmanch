package in.gov.nmc.rangmach;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.HashMap;
import java.util.List;

import in.gov.nmc.rangmach.adapter.My_BookingPayment_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityMyPaymentsBinding;
import in.gov.nmc.rangmach.model.Booking_Payment;
import in.gov.nmc.rangmach.model.Booking_Payment_Data;
import in.gov.nmc.rangmach.utils.Utils;

public class MyPayments_Activity extends BaseActivity<ActivityMyPaymentsBinding> {


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

    private List<Booking_Payment> booking_payments;
    private My_BookingPayment_Adapter my_bookingPayment_adapter;

    String selected_lang;
    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_my_payments;
//    }

    protected ActivityMyPaymentsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMyPaymentsBinding.inflate(inflater);
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
//            setLocal(MyPayments_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(MyPayments_Activity.this, "mr");
        }

        binding.toolbarTitle.setText(GetTranslation("My Payments", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyPenalties();
        binding.swipeRefreshLayout.setOnRefreshListener(this::MyPenalties);
        binding.toolbarTitle.setText("My Payments");
    }

    public void MyPenalties() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().mypayments(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    Booking_Payment_Data data = (Booking_Payment_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getAllPayments() != null &&
                            data.getAllPayments().size() > 0) {

                        booking_payments = data.getAllPayments();
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
        if (booking_payments != null && booking_payments.size() > 0 && binding.recyclerView != null) {
            my_bookingPayment_adapter = new My_BookingPayment_Adapter(mContext, booking_payments);
            binding.recyclerView.setAdapter(my_bookingPayment_adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
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

                if (my_bookingPayment_adapter != null)
                    my_bookingPayment_adapter.filter(newText);

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