package in.gov.nmc.rangmach;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.AllFoodProducts_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityFoodOrderBinding;
import in.gov.nmc.rangmach.model.Centers;
import in.gov.nmc.rangmach.model.FoodProduct;
import in.gov.nmc.rangmach.model.FoodProduct_Data;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class FoodOrder_Activity extends BaseActivity<ActivityFoodOrderBinding> {

//    @BindView(R.id.toolbarTitle)
//    AppCompatTextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;
//    @BindView(R.id.tv_error)
//    TextView tv_error;
//
//    @BindView(R.id.bottom_relativelayout)
//    RelativeLayout bottom_relativelayout;
//
//    @BindView(R.id.paynow)
//    LinearLayout paynow;
//    @BindView(R.id.tv_totalAmt)
//    TextView tv_totalAmt;
//    @BindView(R.id.text_proceedtoPay)
//    TextView text_proceedtoPay;
//
//
//    @BindView(R.id.et_select_eventtype)
//    MaterialEditText et_select_eventtype;

    //    Centers centers;
    private ArrayList<Centers> centersList;
    private String centerID = "";
    private ArrayList<String> eventNameList;
    private List<FoodProduct> foodProductList;
    private AllFoodProducts_Adapter adapter;

    Map<String, String> foodorder_Map = new HashMap();

    double totalamt = 0;

    public static int typeOfLanguage;


    protected ActivityFoodOrderBinding inflateBinding(LayoutInflater inflater) {
        return ActivityFoodOrderBinding.inflate(inflater);
    }


//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_food_order;
//    }

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

        Intent intent = getIntent();
        centersList = intent.getParcelableArrayListExtra("data");

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");
        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        binding.toolbarTitle.setText(GetTranslation("Food Order", typeOfLanguage));

        binding.etSelectEventtype.setHint(GetTranslation("Please Select Center", typeOfLanguage));
        binding.textProceedtoPay.setText(GetTranslation("PROCEED TO CHECKOUT", typeOfLanguage));

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        binding.paynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (foodorder_Map.isEmpty()) {
                    BaseActivity.ToastDialog(mContext, "No Item Selected");
                    return;
                }

                ConfirmationDialog(mContext, foodorder_Map);
//                FoodOrder_Call(foodorder_Map);
            }
        });

        //EventType Dropdown
        ArrayList<Centers> list = (ArrayList<Centers>) centersList;
        if (centersList != null) {

            eventNameList = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                eventNameList.add(list.get(i).getName());
            }
            if (eventNameList != null && eventNameList.size() > 0) {
                binding.etSelectEventtype.setOnClickListener(view -> {
                    new MaterialDialog.Builder(mContext)
                            .title("Please Select Center")
                            .items(eventNameList)
                            .itemsCallback((dialog, view1, which, text) -> {
                                centerID = list.get(which).getId();
                                binding.etSelectEventtype.setText(text.toString());
//                                Toast.makeText(mContext, text.toString(), Toast.LENGTH_SHORT).show();
                                binding.bottomRelativelayout.setVisibility(View.VISIBLE);
                                if (!centerID.isEmpty()) {
                                    binding.tvTotalAmt.setText(GetTranslation("Cart Total", typeOfLanguage) + " :₹ 0");
                                    AllFoodProducts(centerID);
                                }
                            })
                            .show();
                });
                binding.etSelectEventtype.setVisibility(View.VISIBLE);
            }

        } else {
            BaseActivity.ToastDialog(mContext, "Centers is not available");
        }

        binding.swipeRefreshLayout.setOnRefreshListener(this::RefreshPage);
    }

    public void RefreshPage() {
        AllFoodProducts(centerID);
    }


    public void AllFoodProducts(String centerid) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
            map.put("center_id", centerid);
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().foodorderdetails(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (pd.isShowing()) pd.dismiss();
                    FoodProduct_Data data = (FoodProduct_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getFoodProduct() != null &&
                            data.getFoodProduct().size() > 0) {

                        foodProductList = data.getFoodProduct();
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
                    if (pd.isShowing()) pd.dismiss();
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
        if (foodProductList != null && foodProductList.size() > 0 && binding.recyclerView != null) {
            adapter = new AllFoodProducts_Adapter(mContext, foodProductList);
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }

    public void ItemsTotalFunction(double total, Map<String, Integer> itemQtyMap) {

        totalamt = total;

        foodorder_Map.clear();
        String itemId_str = "";
        String qty_str = "";
        String prices_str = "";
        String sgst_str = "";
        String cgst_str = "";

//        Toast.makeText(mContext, total + "", Toast.LENGTH_SHORT).show();
//        tv_totalAmt.setText(String.format("Cart Total:₹ %.2f", total));
        binding.tvTotalAmt.setText(GetTranslation(String.format("Cart Total:₹ %.2f", total), typeOfLanguage));

        if (!itemQtyMap.isEmpty()) {

            for (Map.Entry<String, Integer> entry : itemQtyMap.entrySet()) {
                String id = entry.getKey();
                int qty = entry.getValue();

                for (FoodProduct foodProduct : foodProductList) {
                    String itemid = foodProduct.getId();

                    if (itemid.equals(id)) {
                        String price = foodProduct.getPrice();
                        float sgst = Float.parseFloat(foodProduct.getSgst());
                        float cgst = Float.parseFloat(foodProduct.getCgst());

                        if (itemId_str.length() == 0) {
                            itemId_str = itemid;
                            qty_str = String.valueOf(qty);
                            prices_str = price;
                            sgst_str = String.valueOf(sgst);
                            cgst_str = String.valueOf(cgst);
                        } else {
                            itemId_str += "," + itemid;
                            qty_str += "," + qty;
                            prices_str += "," + price;
                            sgst_str += "," + sgst;
                            cgst_str += "," + cgst;
                        }
                    }
                }
            }

            foodorder_Map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
            foodorder_Map.put("center_id", centerID);
            foodorder_Map.put("total", String.format("%.2f", total));
            foodorder_Map.put("item_id", itemId_str);
            foodorder_Map.put("quantities", qty_str);
            foodorder_Map.put("prices", prices_str);
            foodorder_Map.put("cgst", cgst_str);
            foodorder_Map.put("sgst", sgst_str);
        }
    }


//    public void ConfirmationDialog(Context context, Map<String, String> foodorder_Map) {
//
//        new MaterialAlertDialogBuilder(context)
//                .setTitle("Confirm Order")
//                .setMessage("Proceed to pay ₹ "+String.format("%.2f", totalamt))
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        // Handle the OK button click
//                        FoodOrder_Call(foodorder_Map);
//                        dialog.dismiss();
//                    }
//                })
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
//    }


    public void ConfirmationDialog(Context context, Map<String, String> foodorder_Map) {
        Dialog dialog = Utils.showCustomAlertDialog(context, R.layout.alert_dialog);
        Button btnProceed = dialog.findViewById(R.id.btnProceed);
        Button btnClose = dialog.findViewById(R.id.btnClosing);
        AppCompatTextView tv_title = dialog.findViewById(R.id.tv_title);
        AppCompatTextView tv_msg = dialog.findViewById(R.id.tv_msg);

        tv_title.setText("Confirm Order");
        tv_msg.setText("Proceed to pay ₹ " + String.format("%.2f", totalamt));

        btnProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FoodOrder_Call(foodorder_Map);
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(view1 -> dialog.dismiss());

        dialog.show();
    }


    private void FoodOrder_Call(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().createfoodorder(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
//
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
//                                Utils.showShortToast(mContext, userData.getMessage());
                                BaseActivity.ToastDialog(mContext, userData.getMessage());
                            finish();
                            if (pd.isShowing()) pd.dismiss();

                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                BaseActivity.ToastDialog(mContext, userData.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    BaseActivity.ToastDialog(mContext, apiResponse);
                    Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setQueryHint("Text Here to Search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                if (adapter != null)
                    adapter.filter(newText);

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