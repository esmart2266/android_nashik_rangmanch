package in.gov.nmc.rangmach;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import java.util.HashMap;
import java.util.Map;


import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityForgotPasswordBinding;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class ForgotPasswordActivity extends BaseActivity<ActivityForgotPasswordBinding> {
//    @BindView(R.id.etEmail)
//    MaterialEditText etEmail;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    protected ActivityForgotPasswordBinding inflateBinding(LayoutInflater inflater) {
        return ActivityForgotPasswordBinding.inflate(inflater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        Toolbar toolbar = binding.toolbarContainer.toolbar;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbarContainer.toolbarTitle.setText("Forgot Password");

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    binding.etEmail.setError("Enter Email ID");
                    binding.etEmail.requestFocus();
                    return;
                }
                if (!Utils.isValidEmail(email)) {
                    binding.etEmail.setError("Enter Valid Email ID");
                    binding.etEmail.requestFocus();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("username", email);
                forgetpassword(map);
            }
        });
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_forgot_password;
//    }

//    @OnClick({R.id.btn_submit})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btn_submit: {
//                String email = etEmail.getText().toString().trim();
//                if (TextUtils.isEmpty(email)) {
//                    etEmail.setError("Enter Email ID");
//                    etEmail.requestFocus();
//                    return;
//                }
//                if (!Utils.isValidEmail(email)) {
//                    etEmail.setError("Enter Valid Email ID");
//                    etEmail.requestFocus();
//                    return;
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("username", email);
//                forgetpassword(map);
//                break;
//            }
//        }
//    }

    private void forgetpassword(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().forgetpassword(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
                            finish();
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
