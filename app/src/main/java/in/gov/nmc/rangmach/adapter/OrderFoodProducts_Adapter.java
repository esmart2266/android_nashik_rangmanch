package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowFoodItemsBinding;
import in.gov.nmc.rangmach.model.FoodOrder_Products;


public class OrderFoodProducts_Adapter extends RecyclerView.Adapter<OrderFoodProducts_Adapter.ViewHolder> {
    Context mContext;
    List<FoodOrder_Products> myAssetsOrders;
    public static int typeOfLanguage;

    public OrderFoodProducts_Adapter(Context mContext, List<FoodOrder_Products> myAssetsOrders) {
        this.mContext = mContext;
        this.myAssetsOrders = myAssetsOrders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_food_items, parent, false);
//        return new ViewHolder(view);


        RowFoodItemsBinding binding = RowFoodItemsBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (myAssetsOrders!=null) {

            final FoodOrder_Products item = myAssetsOrders.get(position);

            //Language Change
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String selected_lang = sharedPreferences.getString("languagekey", "English");

            typeOfLanguage = 0;

            if (selected_lang.equals("Hindi")) {
                typeOfLanguage = 2;

            } else if (selected_lang.equals("Marathi")) {
                typeOfLanguage = 1;
            }

            //langauge

            holder.binding.textTitle.setText(BaseActivity.GetTranslation("Title", typeOfLanguage));
            holder.binding.textPrice.setText(BaseActivity.GetTranslation("Price", typeOfLanguage));
            holder.binding.textQuantity.setText(BaseActivity.GetTranslation("Quantity", typeOfLanguage));

            holder.binding.textTitle.setText(item.getTitle());
            holder.binding.textQuantity.setText(item.getQuantity());
            holder.binding.textPrice.setText(item.getPrice());

            if (item.getFoodType().equals("Non-Veg")) {
                holder.binding.ivFoodtype.setImageResource(R.drawable.nonveg);
            } else {
                holder.binding.ivFoodtype.setImageResource(R.drawable.veg);
            }
        }

    }

    @Override
    public int getItemCount() {

        if (myAssetsOrders == null)
            return 0;

        return myAssetsOrders.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.tv_title)
//        AppCompatTextView tv_title;
//        @BindView(R.id.tv_quantity)
//        AppCompatTextView tv_quantity;
//        @BindView(R.id.tv_price)
//        AppCompatTextView tv_price;
//        @BindView(R.id.iv_foodtype)
//        ImageView iv_foodtype;
//
//        @BindView(R.id.text_title)
//        AppCompatTextView text_title;
//        @BindView(R.id.text_price)
//        AppCompatTextView text_price;
//        @BindView(R.id.text_quantity)
//        AppCompatTextView text_quantity;
//
//        View view;

        RowFoodItemsBinding binding;

        ViewHolder(RowFoodItemsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
