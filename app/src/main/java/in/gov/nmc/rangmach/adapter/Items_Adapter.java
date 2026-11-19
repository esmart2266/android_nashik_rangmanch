package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.databinding.RowItemsBinding;
import in.gov.nmc.rangmach.model.ListItems;

public class Items_Adapter extends RecyclerView.Adapter<Items_Adapter.ViewHolder> {
    Context mContext;
    List<ListItems> myAssetsOrders;
    public static int typeOfLanguage;

    public Items_Adapter(Context mContext, List<ListItems> myAssetsOrders) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
    }

    @Override
    public Items_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_items, parent, false);
//        return new Items_Adapter.ViewHolder(view);

        RowItemsBinding binding = RowItemsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {

        if (myAssetsOrders == null)
            return 0;

        return myAssetsOrders.size();
    }

    @Override
    public void onBindViewHolder(final Items_Adapter.ViewHolder viewHolder, final int i) {
        final ListItems item = myAssetsOrders.get(i);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }

        viewHolder.binding.titleText.setText(BaseActivity.GetTranslation("Title", typeOfLanguage));
        viewHolder.binding.totalText.setText(BaseActivity.GetTranslation("Total", typeOfLanguage));
        viewHolder.binding.createdAtText.setText(BaseActivity.GetTranslation("Create At", typeOfLanguage));
        viewHolder.binding.priceText.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
        viewHolder.binding.quantityText.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));

        viewHolder.binding.tvTitle.setText(item.getTitle());
        viewHolder.binding.tvQuantity.setText(item.getQuantity());
        viewHolder.binding.tvPrice.setText(item.getPrice());
        viewHolder.binding.tvTotal.setText(item.getTotal());
        viewHolder.binding.tvCreatedAt.setText(item.getCreated_at());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_title)
//        AppCompatTextView tv_title;
//        @BindView(R.id.tv_quantity)
//        AppCompatTextView tv_quantity;
//        @BindView(R.id.tv_price)
//        AppCompatTextView tv_price;
//        @BindView(R.id.tv_total)
//        AppCompatTextView tv_total;
//        @BindView(R.id.tv_created_at)
//        AppCompatTextView tv_created_at;
//
//        View view;

        RowItemsBinding binding;

        ViewHolder(RowItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
