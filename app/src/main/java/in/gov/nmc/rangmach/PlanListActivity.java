package in.gov.nmc.rangmach;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afollestad.materialdialogs.MaterialDialog;

import in.gov.nmc.rangmach.databinding.ActivityUsersBinding;
import in.gov.nmc.rangmach.model.Data;
import in.gov.nmc.rangmach.utility.AvenuesParams;

public class PlanListActivity extends BaseActivity<ActivityUsersBinding> {

//    @BindView(R.id.toolbar_title)
//    TextView toolbarTitle;
//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.recyclerView)
//    RecyclerView recyclerView;
//    @BindView(R.id.tv_error)
//    TextView tvError;
//    @BindView(R.id.progressBar)
//    ProgressBar progressBar;
//
//    @BindView(R.id.ll_main)
//    LinearLayout ll_main;
//    @BindView(R.id.swipeRefreshLayout)
//    SwipeRefreshLayout swipeRefreshLayout;

    public static PlanListActivity activity;
    private int transactionId;
    private String amount = "";
    Float total = 0f;

    protected ActivityUsersBinding inflateBinding(LayoutInflater inflater) {
        return ActivityUsersBinding.inflate(inflater);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }

        activity = this;
        binding.llMain.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        transactionId = getIntent().getIntExtra("orderid", 0);
        total = getIntent().getFloatExtra("amount", 0);

        binding.toolbarTitle.setText("Subscribe");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));

        startWebViewActivity();
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_users;
//    }


    @SuppressLint("SuspiciousIndentation")
    public void startWebViewActivity() {
        Intent intent = new Intent(mContext, Payment_WebActivity.class);
        intent.putExtra(AvenuesParams.ACCESS_CODE, "AVSR02IB31AU16RSUA");
        intent.putExtra(AvenuesParams.MERCHANT_ID, "325689");
        intent.putExtra(AvenuesParams.ORDER_ID, transactionId + "");
        intent.putExtra(AvenuesParams.CURRENCY, "INR");
        intent.putExtra(AvenuesParams.AMOUNT, "1");
        intent.putExtra(AvenuesParams.REDIRECT_URL, BuildConfig.BASE_URL + "pmc-psrs/paymentgateway/ccavResponseHandler.php");
        intent.putExtra(AvenuesParams.CANCEL_URL, BuildConfig.BASE_URL + "pmc-psrs/paymentgateway/ccavResponseHandler.php");
        intent.putExtra(AvenuesParams.RSA_KEY_URL, BuildConfig.BASE_URL + "pmc-psrs/paymentgateway/GetRSA.php");
        intent.putExtra(AvenuesParams.BILLING_COUNTRY, "India");
        intent.putExtra(AvenuesParams.BILLING_STATE, "Maharashtra");
        intent.putExtra(AvenuesParams.BILLING_CITY, "Nashik");
        intent.putExtra(AvenuesParams.BILLING_ZIP, "411001");
        if (app.getPreferences().getLoggedInUser().getData() != null) {
            Data data = app.getPreferences().getLoggedInUser().getData();

            intent.putExtra(AvenuesParams.BILLING_NAME, getName(data));
            intent.putExtra(AvenuesParams.BILLING_ADDRESS, data.getAddress());
            if (data.getMobileNo() != null)
                intent.putExtra(AvenuesParams.BILLING_TEL, data.getMobileNo());
            if (data.getemailid() != null)
                intent.putExtra(AvenuesParams.BILLING_EMAIL, data.getemailid());
            intent.putExtra(AvenuesParams.DELIVERY_NAME, getName(data));
            intent.putExtra(AvenuesParams.DELIVERY_ADDRESS, data.getAddress());
            intent.putExtra(AvenuesParams.DELIVERY_CITY, data.getCity());
            intent.putExtra(AvenuesParams.DELIVERY_STATE, "Maharashtra");
//            if (data.getZipcode() != null)
//                intent.putExtra(AvenuesParams.DELIVERY_ZIP, data.getZipcode());
            intent.putExtra(AvenuesParams.DELIVERY_ZIP, "411001");
            intent.putExtra(AvenuesParams.DELIVERY_COUNTRY, "India");
            intent.putExtra(AvenuesParams.DELIVERY_TEL, data.getMobileNo());
        }
//        String billing_notes = items + " and " + vendorids + " and " + qty;
//        billing_notes = billing_notes + " and " + userorderdatetime;
        intent.putExtra(AvenuesParams.BILLING_NOTES, "");
        startActivityForResult(intent, 121);
    }

    private String getName(Data data) {
        String name = "";
        if (!TextUtils.isEmpty(data.getName())) {
            name = data.getName();
        }
        if (!TextUtils.isEmpty(data.getName())) {
            name = name + " " + data.getName();
        }
        return name;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 121 && resultCode == RESULT_OK) {
                String status = data.getStringExtra("status");
                if (status.equalsIgnoreCase("Transaction Declined!")) {
                    new MaterialDialog.Builder(mContext)
                            .content(status)
                            .cancelable(false)
                            .positiveText("Retry")
                            .negativeText("Cancel")
                            .autoDismiss(false)
                            .onPositive((dialog, which) -> {
                            })
                            .onNegative((dialog, which) -> {
                                dialog.dismiss();
                                startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }).show();
                } else if (status.equalsIgnoreCase("Transaction Successful!")) {
                    new MaterialDialog.Builder(mContext)
                            .content(status)
                            .cancelable(false)
                            .positiveText("Ok")
                            .autoDismiss(false)
                            .onPositive((dialog, which) -> {
                                dialog.dismiss();
                                startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }).show();
                } else if (status.equalsIgnoreCase("Transaction Cancelled!")) {
                    new MaterialDialog.Builder(mContext)
                            .content(status)
                            .positiveText("Ok")
                            .cancelable(false)
                            .autoDismiss(false)
                            .onPositive((dialog, which) -> {
                                dialog.dismiss();
                                startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }).show();
                } else {
                    new MaterialDialog.Builder(mContext)
                            .content(status)
                            .positiveText("Ok")
                            .autoDismiss(false)
                            .cancelable(false)
                            .onPositive((dialog, which) -> {
                                dialog.dismiss();
                                startActivity(new Intent(mContext, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            }).show();
                }
            } else {
//                btnProceedToPay.setClickable(true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_cancel, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_cancel) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        activity = null;
        super.onDestroy();
    }
}
