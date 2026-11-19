package in.gov.nmc.rangmach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.gov.nmc.rangmach.adapter.All_Organization_Adapter;
import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityAddOrganizationDetailsBinding;
import in.gov.nmc.rangmach.model.AllOrganizationsList;
import in.gov.nmc.rangmach.model.AllOrganizationsList_Data;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class AddOrganization_Details_Activity extends BaseActivity<ActivityAddOrganizationDetailsBinding> {

    public static int typeOfLanguage;
    private List<AllOrganizationsList> allOrganizationsLists;
    private All_Organization_Adapter allOrganizationAdapter;

    protected ActivityAddOrganizationDetailsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityAddOrganizationDetailsBinding.inflate(inflater);
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
//            setLocal(MyRefund_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(MyRefund_Activity.this, "mr");
        }

        binding.toolbarTitle.setText(GetTranslation("All Organization", typeOfLanguage));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        AllOrganizations();
        binding.swipeRefreshLayout.setOnRefreshListener(this::AllOrganizations);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AllOrganizations();
    }

    public void AllOrganizations() {
        if (cd.isConnectingToInternet()) {
            binding.progressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());
//            map.put("userid", "52");

            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().get_org_by_user(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (binding.swipeRefreshLayout != null && binding.swipeRefreshLayout.isRefreshing())
                        binding.swipeRefreshLayout.setRefreshing(false);
                    if (binding.progressBar != null) binding.progressBar.setVisibility(View.GONE);
                    AllOrganizationsList_Data data = (AllOrganizationsList_Data) object;
                    if (data != null && data.getResponsecode() == 200 && data.getData() != null && data.getData().size() > 0) {

                        allOrganizationsLists = data.getData();
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
        if (allOrganizationsLists != null && allOrganizationsLists.size() > 0 && binding.recyclerView != null) {
            allOrganizationAdapter = new All_Organization_Adapter(mContext, allOrganizationsLists);
            binding.recyclerView.setAdapter(allOrganizationAdapter);
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.tvError.setVisibility(View.GONE);
        } else if (binding.recyclerView != null && binding.tvError != null) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.tvError.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);

        MenuItem item1 = menu.findItem(R.id.action_add);
        item1.setVisible(true);
        this.invalidateOptionsMenu();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            Utils.hideSoftKeyboard(this);
            finish();
            return true;
        } else if (id == R.id.action_add) {

            AddOrganization_Dialog();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void AddOrganization_Dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(this).inflate(R.layout.add_organization_form_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        MaterialEditText etName = view.findViewById(R.id.etName);
        MaterialEditText etMobile = view.findViewById(R.id.etMobile);
        MaterialEditText etEmail = view.findViewById(R.id.etEmail);
        MaterialEditText etGst = view.findViewById(R.id.etGst);
        MaterialEditText etAddress = view.findViewById(R.id.etAddress);
        CheckBox cb_isActive = view.findViewById(R.id.cb_isActive);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnClose = view.findViewById(R.id.btnClose);
        cb_isActive.setActivated(true);
        etName.setHint(GetTranslation("Name", typeOfLanguage));
        etMobile.setHint(GetTranslation("Mobile No", typeOfLanguage));
        etEmail.setHint(GetTranslation("Email", typeOfLanguage));
        etGst.setHint(GetTranslation("GST No", typeOfLanguage));
        etAddress.setHint(GetTranslation("Address", typeOfLanguage));


        btnSave.setHint(GetTranslation("Save", typeOfLanguage));
        btnClose.setHint(GetTranslation("Close", typeOfLanguage));


        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String gst = etGst.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Name required");
                etName.requestFocus();
                return;
            }

            if (mobile.length() != 10) {
                etMobile.setError("Enter valid 10-digit mobile");
                etMobile.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                etEmail.setError("Enter valid email");
//                etEmail.requestFocus();
//                return;
            }

            if (gst.isEmpty()) {
//                etGst.setError("GST required");
//                etGst.requestFocus();
//                return;
            }

            if (address.isEmpty()) {
//                etAddress.setError("Address required");
//                etAddress.requestFocus();
//                return;
            }

            Map<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());

            if (cb_isActive.isChecked()) {
                map.put("isActive", "1");
            } else {
                map.put("isActive", "0");
            }

            map.put("name", name);
            map.put("mobile", mobile);
            map.put("email", email);
            map.put("address", address);
            map.put("gst", gst);
            CreateUserOrg(map);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void CreateUserOrg(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().create_user_org(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
                            AllOrganizations();
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showLongToast(mContext, userData.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

    public void EditOrganization_Dialog(int position, AllOrganizationsList item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.add_organization_form_dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        MaterialEditText etName = view.findViewById(R.id.etName);
        MaterialEditText etMobile = view.findViewById(R.id.etMobile);
        MaterialEditText etEmail = view.findViewById(R.id.etEmail);
        MaterialEditText etGst = view.findViewById(R.id.etGst);
        MaterialEditText etAddress = view.findViewById(R.id.etAddress);
        CheckBox cb_isActive = view.findViewById(R.id.cb_isActive);
        Button btnSave = view.findViewById(R.id.btnSave);
        Button btnClose = view.findViewById(R.id.btnClose);

        etName.setHint(GetTranslation("Name", typeOfLanguage));
        etMobile.setHint(GetTranslation("Mobile No", typeOfLanguage));
        etEmail.setHint(GetTranslation("Email", typeOfLanguage));
        etGst.setHint(GetTranslation("GST No", typeOfLanguage));
        etAddress.setHint(GetTranslation("Address", typeOfLanguage));
        cb_isActive.setText(GetTranslation("isActive", typeOfLanguage));

        btnSave.setHint(GetTranslation("Save", typeOfLanguage));
        btnClose.setHint(GetTranslation("Close", typeOfLanguage));

        if (item != null) {
            etName.setText(item.getName());
            etMobile.setText(item.getMobile());
            etEmail.setText(item.getEmail());
            etGst.setText(item.getGst());
            etAddress.setText(item.getAddress());

            if (item.getIsActive().equals("1")) {
                cb_isActive.setChecked(true);
            } else {
                cb_isActive.setChecked(false);
            }

        }

        btnClose.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btnSave.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String mobile = etMobile.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String gst = etGst.getText().toString().trim();
            String address = etAddress.getText().toString().trim();

            if (name.isEmpty()) {
                etName.setError("Name required");
                etName.requestFocus();
                return;
            }

            if (mobile.length() != 10) {
                etMobile.setError("Enter valid 10-digit mobile");
                etMobile.requestFocus();
                return;
            }

//            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//                etEmail.setError("Enter valid email");
//                etEmail.requestFocus();
//                return;
//            }

//            if (gst.isEmpty()) {
//                etGst.setError("GST required");
//                etGst.requestFocus();
//                return;
//            }

//            if (address.isEmpty()) {
//                etAddress.setError("Address required");
//                etAddress.requestFocus();
//                return;
//            }

            Map<String, String> map = new HashMap<>();
            map.put("user_id", app.getPreferences().getLoggedInUser().getData().getUserId());

            if (cb_isActive.isChecked()) {
                map.put("isActive", "1");
            } else {
                map.put("isActive", "0");
            }

            map.put("id", item.getId());
            map.put("name", name);
            map.put("mobile", mobile);
            map.put("email", email);
            map.put("address", address);
            map.put("gst", gst);
            EditUserOrg(map, position, item);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void EditUserOrg(Map<String, String> map, int position, AllOrganizationsList item) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().update_user_org(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
                            AllOrganizations();
                        } else {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showLongToast(mContext, userData.getMessage());
                        }
                    } else {
                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
                    }
                }

                @Override
                public void onFailure(String apiResponse) {
                    if (pd.isShowing()) pd.dismiss();
                    Log.e("in", "error " + apiResponse);
                    Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }

}