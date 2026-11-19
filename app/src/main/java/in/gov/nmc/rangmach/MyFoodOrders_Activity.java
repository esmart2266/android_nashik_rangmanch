package in.gov.nmc.rangmach;

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

import in.gov.nmc.rangmach.adapter.MyFood_Orders_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityMyFoodOrdersBinding;
import in.gov.nmc.rangmach.model.CustomerFoodOrders;
import in.gov.nmc.rangmach.model.CustomerFoodOrders_Data;
import in.gov.nmc.rangmach.utils.Utils;

public class MyFoodOrders_Activity extends BaseActivity<ActivityMyFoodOrdersBinding> {

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

    private List<CustomerFoodOrders> customerFoodOrdersList;
    private MyFood_Orders_Adapter food_orders_adapter;

    public static int typeOfLanguage;

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_my_food_orders;
//    }


    protected ActivityMyFoodOrdersBinding inflateBinding(LayoutInflater inflater) {
        return ActivityMyFoodOrdersBinding.inflate(inflater);
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

        binding.toolbarTitle.setText(GetTranslation("My Food Orders", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        MyFoodOrders();
        binding.swipeRefreshLayout.setOnRefreshListener(this::MyFoodOrders);
    }

    public void MyFoodOrders() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().customerfoodorder(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    CustomerFoodOrders_Data data = (CustomerFoodOrders_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getFoodProduct() != null &&
                            data.getFoodProduct().size() > 0) {

                        customerFoodOrdersList = data.getFoodProduct();
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
        if (customerFoodOrdersList != null && customerFoodOrdersList.size() > 0 && binding.recyclerView != null) {
            food_orders_adapter = new MyFood_Orders_Adapter(mContext, customerFoodOrdersList);
            binding.recyclerView.setAdapter(food_orders_adapter);
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