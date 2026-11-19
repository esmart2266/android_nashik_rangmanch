package in.gov.nmc.rangmach;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.List;
import in.gov.nmc.rangmach.databinding.ActivityVideoBinding;
import in.gov.nmc.rangmach.model.Blog;
import in.gov.nmc.rangmach.utils.Utils;

public class BlogsActivity extends BaseActivity<ActivityVideoBinding> {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//    @BindView(R.id.rv_prescriptions)
//    RecyclerView rv_prescriptions;
//    @BindView(R.id.tv_error)
//    TextView tvError;
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;

    List<Blog> data;

    protected ActivityVideoBinding inflateBinding(LayoutInflater inflater) {
        return ActivityVideoBinding.inflate(inflater);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            );
        }
        try {
            Toolbar toolbar = binding.toolbarContainer.toolbar;
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            binding.toolbarContainer.toolbarTitle.setText("Blogs");

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mContext);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            binding.rvPrescriptions.setLayoutManager(layoutManager);
//        rv_prescriptions.addItemDecoration(dividerItemDecoration);
//            getvideos();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_video;
//    }

//    private void getvideos() {
//        ConnectionDetector cd = new ConnectionDetector(mContext);
//        if (cd.isConnectingToInternet()) {
//            final CustomProgressDialog cpd = new CustomProgressDialog(mContext);
//            cpd.show();
//            HashMap<String, String> map = new HashMap<>();
//            map.put("patientid", app.getPreferences().getLoggedInUser().getData().getUserId());
//            callList.add(app.getApiRequestHelper().callRetrofit(app.getApiRequestHelper().getAppService().blogs(map), new ApiRequestHelper.OnRequestComplete() {
//                @Override
//                public void onSuccess(Object object) {
//                    if (cpd.isShowing())
//                        cpd.dismiss();
//                    BlogData videoData = (BlogData) object;
//                    if (videoData != null) {
//                        if (videoData.getResponsecode() == 200) {
//                            if (videoData.getData() != null && videoData.getData().size() > 0) {
//                                data = videoData.getData();
//                                rv_prescriptions.setAdapter(new BlogAdapter(mContext, data));
//                                rv_prescriptions.setVisibility(View.VISIBLE);
//                                tvError.setVisibility(View.GONE);
//                            } else {
//                                rv_prescriptions.setVisibility(View.GONE);
//                                tvError.setVisibility(View.VISIBLE);
//                            }
//                        } else {
//                            if (videoData.getMessage() != null && !TextUtils.isEmpty(videoData.getMessage()))
//                                Utils.showLongToast(mContext, videoData.getMessage());
//                            rv_prescriptions.setVisibility(View.GONE);
//                            tvError.setVisibility(View.VISIBLE);
//                        }
//                    } else {
//                        Utils.showLongToast(mContext, Utils.UNPROPER_RESPONSE);
//                        rv_prescriptions.setVisibility(View.GONE);
//                        tvError.setVisibility(View.VISIBLE);
//                    }
//                }
//
//                @Override
//                public void onFailure(String apiResponse) {
//                    if (cpd.isShowing())
//                        cpd.dismiss();
//                    Utils.showLongToast(mContext, apiResponse);
//                    rv_prescriptions.setVisibility(View.GONE);
//                    tvError.setVisibility(View.VISIBLE);
//                }
//            }));
//        } else {
//            Utils.alert_dialog(mContext);
//            rv_prescriptions.setVisibility(View.GONE);
//            tvError.setVisibility(View.VISIBLE);
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_filer, menu);
//        MenuItem item = menu.findItem(R.id.action_filter);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Utils.hideSoftKeyboard(BlogsActivity.this);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
