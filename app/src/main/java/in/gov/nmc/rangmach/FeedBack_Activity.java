package in.gov.nmc.rangmach;


import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import java.util.HashMap;
import java.util.Map;

import in.gov.nmc.rangmach.api.ApiRequestHelper;
import in.gov.nmc.rangmach.databinding.ActivityFeedBackBinding;
import in.gov.nmc.rangmach.model.UserData;
import in.gov.nmc.rangmach.utils.Utils;
import in.gov.nmc.rangmach.widget.materialprogress.CustomProgressDialog;
import retrofit2.Call;

public class FeedBack_Activity extends BaseActivity<ActivityFeedBackBinding> {
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.etFeedback)
//    MaterialEditText etFeedback;
//    @BindView(R.id.ratingBar1)
//    RatingBar crb1;
//    @BindView(R.id.ratingBar2)
//    RatingBar crb2;
//    @BindView(R.id.ratingBar3)
//    RatingBar crb3;
//    @BindView(R.id.ratingBar4)
//    RatingBar crb4;
//
//    @BindView(R.id.text1)
//    TextView text1;
//    @BindView(R.id.text2)
//    TextView text2;
//    @BindView(R.id.text3)
//    TextView text3;
//    @BindView(R.id.text4)
//    TextView text4;

    String selected_lang;
    public static int typeOfLanguage;



    protected ActivityFeedBackBinding inflateBinding(LayoutInflater inflater) {
        return ActivityFeedBackBinding.inflate(inflater);
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_white_24);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(FeedBack_Activity.this, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(FeedBack_Activity.this, "mr");
        }

        toolbar.setTitle(GetTranslation("Feedback", typeOfLanguage));

        binding.text1.setText(GetTranslation("Did you like new booking system through mobile app?", typeOfLanguage));
        binding.text2.setText(GetTranslation("Did you like new payment and refund process?", typeOfLanguage));
        binding.text3.setText(GetTranslation("Did you like early exclusive schedule of upcoming shows and events?", typeOfLanguage));
        binding.text4.setText(GetTranslation("How likely would you share this app to other Nashikers?", typeOfLanguage));
        binding.etFeedback.setHint(GetTranslation("Write your feedback", typeOfLanguage));

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String feedback = binding.etFeedback.getText().toString().trim();
                if (TextUtils.isEmpty(feedback)) {
                    binding.etFeedback.setError("Please Write your feedback");
                    binding.etFeedback.requestFocus();
                    return;
                }
                Map<String, String> map = new HashMap<>();
                map.put("customerid", app.getPreferences().getLoggedInUser().getData().getUserId());
                map.put("rate1", "" + binding.ratingBar1.getRating());
                map.put("rate2", "" + binding.ratingBar2.getRating());
                map.put("rate3", "" + binding.ratingBar3.getRating());
                map.put("rate4", "" + binding.ratingBar4.getRating());
                map.put("feedback", feedback);
                savefeedback(map);
            }
        });
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_feed_back;
//    }

//    @OnClick({R.id.btnSubmit})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btnSubmit:
//                String feedback = binding.etFeedback.getText().toString().trim();
//                if (TextUtils.isEmpty(feedback)) {
//                    binding.etFeedback.setError("Please Write your feedback");
//                    binding.etFeedback.requestFocus();
//                    return;
//                }
//                Map<String, String> map = new HashMap<>();
//                map.put("customerid", app.getPreferences().getLoggedInUser().getData().getUserId());
//                map.put("rate1", "" + binding.ratingBar1.getRating());
//                map.put("rate2", "" + binding.ratingBar2.getRating());
//                map.put("rate3", "" + binding.ratingBar3.getRating());
//                map.put("rate4", "" + binding.ratingBar4.getRating());
//                map.put("feedback", feedback);
//                savefeedback(map);
//                break;
//        }
//    }

    public void savefeedback(Map<String, String> params) {
        if (cd.isConnectingToInternet()) {
            CustomProgressDialog pd = new CustomProgressDialog(mContext);
            pd.show();
            Call<UserData> call = app.getApiRequestHelper().getAppService().savefeedback(params);
            app.getApiRequestHelper().callRetrofit(call, new ApiRequestHelper.OnRequestComplete() {
                @Override
                public void onSuccess(Object object) {
                    if (pd.isShowing()) pd.dismiss();
                    UserData userData = (UserData) object;
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
                    if (!TextUtils.isEmpty(apiResponse))
                        Utils.showLongToast(mContext, apiResponse);
                }
            });
        } else {
            Utils.alert_dialog(mContext);
        }
    }
//
//    public void setLocal(Activity activity, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = activity.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

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


