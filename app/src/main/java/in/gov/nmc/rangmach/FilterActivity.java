package in.gov.nmc.rangmach;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;

import in.gov.nmc.rangmach.Fragment.NowShowing_Fragment;
import in.gov.nmc.rangmach.adapter.Filter_Category_Adapter;
import in.gov.nmc.rangmach.adapter.Filter_Langauge_Adapter;
import in.gov.nmc.rangmach.adapter.Filter_Theater_Adapter;
import in.gov.nmc.rangmach.databinding.ActivityFilterBinding;

public class FilterActivity extends BaseActivity<ActivityFilterBinding> {

//    @BindView(R.id.toolbar)
//    Toolbar toolbar;
//
//    @BindView(R.id.toolbarTitle)
//    TextView toolbarTitle;
//
//    @BindView(R.id.theater_recyclerview)
//    RecyclerView theater_recyclerview;
//
//    @BindView(R.id.category_recyclerview)
//    RecyclerView category_recyclerview;
//
//    @BindView(R.id.language_recyclerview)
//    RecyclerView language_recyclerview;
//
//    @BindView(R.id.llBottom)
//    RelativeLayout llBottom;


    protected ActivityFilterBinding inflateBinding(LayoutInflater inflater) {
        return ActivityFilterBinding.inflate(inflater);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_filter;
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
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ArrayList<String> TheaterList = getIntent().getStringArrayListExtra("theaterSet");
        ArrayList<String> CategoryList = getIntent().getStringArrayListExtra("categorySet");
        ArrayList<String> LangaugeList = getIntent().getStringArrayListExtra("languageSet");

        binding.llBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                finish();
//                overridePendingTransition(R.anim.slidetop, R.anim.slidebottom);

//                        .replace(R.id.fragment_container, new NowShowing_Fragment())
                openMyFragment();
            }
        });


        if (TheaterList != null) {
            binding.theaterRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            binding.theaterRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            binding.theaterRecyclerview.setAdapter(new Filter_Theater_Adapter(mContext, TheaterList));
        } else {
            binding.theaterRecyclerview.setVisibility(View.GONE);
        }


        if (CategoryList != null) {
            binding.categoryRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            binding.categoryRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            binding.categoryRecyclerview.setAdapter(new Filter_Category_Adapter(mContext, CategoryList));
        } else {
            binding.categoryRecyclerview.setVisibility(View.GONE);
        }


        if (LangaugeList != null) {
            binding.languageRecyclerview.setLayoutManager(new LinearLayoutManager(mContext));
            binding.languageRecyclerview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            binding.languageRecyclerview.setAdapter(new Filter_Langauge_Adapter(mContext, LangaugeList));
        } else {
            binding.languageRecyclerview.setVisibility(View.GONE);
        }

    }

    private void openMyFragment() {
        NowShowing_Fragment myFragment = new NowShowing_Fragment();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(
                R.anim.slidetop, // Enter animation
                R.anim.slidebottom   // Exit animation
        );

        fragmentTransaction.replace(R.id.fragment_container, myFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        finish();
    }

}