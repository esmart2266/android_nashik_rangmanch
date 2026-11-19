package in.gov.nmc.rangmach;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityVerifyOtpBinding;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;

public class VerifyOTPActivity extends BaseActivity<ActivityVerifyOtpBinding> {

//    @BindView(R.id.etOtp)
//    MaterialEditText etOtp;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }


        setSupportActionBar(binding.toolbarContainer.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.toolbarContainer.toolbarTitle.setText("Verify OTP");

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = binding.etOtp.getText().toString().trim();
                if (TextUtils.isEmpty(otp)) {
                    binding.etOtp.setError("Enter OTP");
                    binding.etOtp.requestFocus();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("otp", otp);
                map.put("uuid", Utils.getDeviceName());
                verifydevice(map);
            }
        });
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_verify_otp;
//    }

    protected ActivityVerifyOtpBinding inflateBinding(LayoutInflater inflater) {
        return ActivityVerifyOtpBinding.inflate(inflater);
    }

//    @OnClick({R.id.btnSubmit})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btnSubmit: {
//                String otp = binding.etOtp.getText().toString().trim();
//                if (TextUtils.isEmpty(otp)) {
//                    etOtp.setError("Enter OTP");
//                    etOtp.requestFocus();
//                    return;
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("otp", otp);
//                map.put("uuid", Utils.getDeviceName());
//                verifydevice(map);
//                break;
//            }
//        }
//    }

    private void verifydevice(Map<String, String> map) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().verifydevice(map), new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
                    Log.e("in", "success");
                    if (userData != null) {
                        if (userData.getResponsecode() == 200) {
                            if (userData.getMessage() != null && !TextUtils.isEmpty(userData.getMessage()))
                                Utils.showShortToast(mContext, userData.getMessage());
                            if (userData.getData() != null) {
                                app.getPreferences().setLoggedInUser(userData);
                                startActivity(new Intent(mContext, MainActivity.class));
                                finish();
                            }
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
