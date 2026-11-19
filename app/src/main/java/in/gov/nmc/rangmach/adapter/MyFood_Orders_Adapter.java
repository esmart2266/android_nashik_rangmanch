package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowMyfoodOrdersBinding;
import in.gov.nmc.rangmach.model.CustomerFoodOrders;

public class MyFood_Orders_Adapter extends RecyclerView.Adapter<MyFood_Orders_Adapter.ViewHolder> {
    Context mContext;
    List<CustomerFoodOrders> foodOrdersList;
    public static int typeOfLanguage;

    public MyFood_Orders_Adapter(Context mContext, List<CustomerFoodOrders> myAssetsOrders) {
        this.mContext = mContext;
        this.foodOrdersList = myAssetsOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_myfood_orders, parent, false);
//        return new ViewHolder(view);

        RowMyfoodOrdersBinding binding = RowMyfoodOrdersBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return foodOrdersList.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final CustomerFoodOrders item = foodOrdersList.get(i);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
//            setLocal(mContext, "hi");
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
//            setLocal(mContext, "mr");
        }

        //langauge

        viewHolder.binding.paymentModeText.setText(BaseActivity.GetTranslation("Payment Mode :", typeOfLanguage));
        viewHolder.binding.textId.setText(BaseActivity.GetTranslation("Order ID :", typeOfLanguage));
        viewHolder.binding.textTotal.setText(BaseActivity.GetTranslation("Total :", typeOfLanguage));
        viewHolder.binding.textOrderDateTime.setText(BaseActivity.GetTranslation("Order DateTime :", typeOfLanguage));


        viewHolder.binding.centerName.setText(item.getCenterName());
        viewHolder.binding.tvTotal.setText(item.getTotal());
        viewHolder.binding.tvOrderDateTime.setText(item.getOrderDateTime());
        viewHolder.binding.tvPaymentMode.setText(item.getPaymentMode());
        viewHolder.binding.tvId.setText(item.getId());


        viewHolder.binding.rvProducts.setVisibility(View.GONE);

        if (item.getProducts() != null && !item.getProducts().isEmpty()) {

            viewHolder.binding.rvProducts.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            viewHolder.binding.rvProducts.setLayoutManager(layoutManager);
            viewHolder.binding.rvProducts.setAdapter(new OrderFoodProducts_Adapter(mContext, item.getProducts()));
            try{
                viewHolder.binding.arIndicator.attachTo(viewHolder.binding.rvProducts,true);
            } catch (Exception e) {

            }

        } else {
            viewHolder.binding.rvProducts.setVisibility(View.GONE);
        }

    }


//    public void setLocal(Context context, String langcode) {
//        Locale locale = new Locale(langcode);
//        locale.setDefault(locale);
//        Resources resources = context.getResources();
//        Configuration config = resources.getConfiguration();
//        config.setLocale(locale);
//        resources.updateConfiguration(config, resources.getDisplayMetrics());
//    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.center_name)
//        AppCompatTextView center_name;
//        @BindView(R.id.tv_total)
//        AppCompatTextView tv_total;
//        @BindView(R.id.tv_order_date_time)
//        AppCompatTextView tv_order_date_time;
//        @BindView(R.id.tv_payment_mode)
//        AppCompatTextView tv_payment_mode;
//        @BindView(R.id.tv_id)
//        AppCompatTextView tv_orderid;
//        @BindView(R.id.rv_products)
//        RecyclerView recyclerView_products;
//        @BindView(R.id.ar_indicator)
//        ARIndicatorView ar_indicator;
//
//
//        @BindView(R.id.text_id)
//        AppCompatTextView text_id;
//        @BindView(R.id.text_total)
//        AppCompatTextView text_total;
//        @BindView(R.id.text_order_date_time)
//        AppCompatTextView text_order_date_time;
//        @BindView(R.id.payment_mode_text)
//        AppCompatTextView payment_mode_text;
//
//        View view;


        RowMyfoodOrdersBinding binding;

        ViewHolder(RowMyfoodOrdersBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}