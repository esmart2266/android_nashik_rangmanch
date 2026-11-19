package in.gov.nmc.rangmach;

import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import in.gov.nmc.rangmach.adapter.Items_Adapter;
import in.gov.nmc.rangmach.adapter.PaymentDetails_Adapter;
import in.gov.nmc.rangmach.adapter.Penalty_Adapter;
import in.gov.nmc.rangmach.adapter.Refund_Adapter;
import in.gov.nmc.rangmach.databinding.ActivityItemsDetailsBinding;
import in.gov.nmc.rangmach.model.MyAssetsOrder;
import in.gov.nmc.rangmach.utils.Utils;

public class ItemsDetails_Activity extends BaseActivity<ActivityItemsDetailsBinding> {

    MyAssetsOrder items;
    public static int typeOfLanguage;
    Items_Adapter items_adapter;


    protected ActivityItemsDetailsBinding inflateBinding(LayoutInflater inflater) {
        return ActivityItemsDetailsBinding.inflate(inflater);
    }

//    @Override
//    protected int getActivityLayout() {
//        return R.layout.activity_items_details;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        items = (MyAssetsOrder) getIntent().getSerializableExtra("data");

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {

            typeOfLanguage = 1;
        }

        binding.toolbarTitle.setText(GetTranslation("Booking Details", typeOfLanguage));
        binding.textBookingItems.setText(GetTranslation("Booking Items", typeOfLanguage));
        binding.textPaymentdetails.setText(GetTranslation("Payment Details", typeOfLanguage));
        binding.textRefundsummary.setText(GetTranslation("Refund Summary", typeOfLanguage));
        binding.textPenaltysummary.setText(GetTranslation("Penalty Summary", typeOfLanguage));

        if (items.getItems() != null) {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            binding.recyclerView.setAdapter(new Items_Adapter(mContext, items.getItems()));

            binding.tvError.setVisibility(View.GONE);
            binding.llItems.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.VISIBLE);

        } else {

            binding.llItems.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.GONE);
        }

        if (items.getPaymentDetails() != null) {
            binding.recyclerViewPaymentDetails.setLayoutManager(new LinearLayoutManager(mContext));
            binding.recyclerViewPaymentDetails.setAdapter(new PaymentDetails_Adapter(mContext, items.getPaymentDetails()));

            binding.tvError.setVisibility(View.GONE);
            binding.llPaymentdetails.setVisibility(View.VISIBLE);
            binding.recyclerViewPaymentDetails.setVisibility(View.VISIBLE);

        } else {

            binding.llPaymentdetails.setVisibility(View.GONE);
            binding.recyclerViewPaymentDetails.setVisibility(View.GONE);
        }

        if (items.getRefund() != null) {
            binding.recyclerViewRefundSummary.setLayoutManager(new LinearLayoutManager(mContext));
            binding.recyclerViewRefundSummary.setAdapter(new Refund_Adapter(mContext, items.getRefund()));

            binding.tvError.setVisibility(View.GONE);
            binding.llRefund.setVisibility(View.VISIBLE);
            binding.recyclerViewRefundSummary.setVisibility(View.VISIBLE);

        } else {
            binding.llRefund.setVisibility(View.GONE);
            binding.recyclerViewRefundSummary.setVisibility(View.GONE);
        }

        if (items.getPenalty() != null) {
            binding.recyclerViewPenaltySummary.setLayoutManager(new LinearLayoutManager(mContext));
            binding.recyclerViewPenaltySummary.setAdapter(new Penalty_Adapter(mContext, items.getPenalty()));

            binding.tvError.setVisibility(View.GONE);
            binding.llPenalty.setVisibility(View.VISIBLE);
            binding.recyclerViewPenaltySummary.setVisibility(View.VISIBLE);

        } else {
            binding.llPenalty.setVisibility(View.GONE);
            binding.recyclerViewPenaltySummary.setVisibility(View.GONE);
        }

        if (items.getPenalty() == null && items.getPaymentDetails() == null && items.getItems() == null && items.getRefund() == null) {
            binding.tvError.setVisibility(View.VISIBLE);
        } else {
            binding.tvError.setVisibility(View.GONE);
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