package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.gov.nmc.rangmach.BuildConfig;
import in.gov.nmc.rangmach.FoodOrder_Activity;
import in.gov.nmc.rangmach.FullScreenImageActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowFoodorderBinding;
import in.gov.nmc.rangmach.model.FoodProduct;


public class AllFoodProducts_Adapter extends RecyclerView.Adapter<AllFoodProducts_Adapter.ViewHolder> {
    Context mContext;
    List<FoodProduct> foodlist;
    List<FoodProduct> list_search = new ArrayList<>();
    public static int typeOfLanguage;
    private double totalPrice;

    Map<String, Integer> itemQtyMap = new HashMap();

    public AllFoodProducts_Adapter(Context mContext, List<FoodProduct> myAssetsOrders) {
        this.mContext = mContext;
        this.foodlist = myAssetsOrders;
        list_search.addAll(foodlist);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_foodorder, parent, false);
//        return new ViewHolder(view);

        RowFoodorderBinding binding = RowFoodorderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final FoodProduct item = foodlist.get(i);

        //Language Change
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String selected_lang = sharedPreferences.getString("languagekey", "English");

        typeOfLanguage = 0;

        if (selected_lang.equals("Hindi")) {
            typeOfLanguage = 2;
        } else if (selected_lang.equals("Marathi")) {
            typeOfLanguage = 1;
        }


        if (item.getFoodType().equals("Non-Veg")) {
            viewHolder.binding.foodMark.setImageResource(R.drawable.nonveg);
        } else {
            viewHolder.binding.foodMark.setImageResource(R.drawable.veg);
        }

        String imageurl = BuildConfig.BASE_URL + BuildConfig.API_PATH + item.getImage();

        Glide.with(mContext).load(imageurl).
                error(R.drawable.no_image).
                thumbnail(0.15f).
                into(viewHolder.binding.ivItem);

        viewHolder.binding.ivItem.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, FullScreenImageActivity.class);
            intent.putExtra("title", "Preview");
            intent.putExtra("urlPhotoClick", imageurl);
            mContext.startActivity(intent);
        });

//        //langauge
//        viewHolder.btn_booknow.setText(BaseActivity.GetTranslation("Book Now", typeOfLanguage));

        viewHolder.binding.menuItemName.setText(item.getTitle());
        viewHolder.binding.menuItemCategory.setText(item.getSubtitle());
        viewHolder.binding.menuItemPrice.setText("₹ " + item.getPrice());


        double basePrice = Double.parseDouble(item.getPrice());
        viewHolder.binding.tvItemTotal.setText(String.format("Total Price: ₹%.2f", basePrice));


        if (itemQtyMap.containsKey(item.getId())) {
            viewHolder.binding.tvQuantity.setText(String.valueOf(itemQtyMap.get(item.getId())));
        } else {
            viewHolder.binding.tvQuantity.setText("0");
        }


        viewHolder.binding.btnIncrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = viewHolder.getAdapterPosition();
                int qty = 1;
                if (itemQtyMap.containsKey(foodlist.get(index).getId())) {
                    qty = itemQtyMap.get(foodlist.get(index).getId());
                    qty++;
                    viewHolder.binding.tvQuantity.setText(String.valueOf(qty));
                    itemQtyMap.put(foodlist.get(index).getId(), qty);
                } else {
                    viewHolder.binding.tvQuantity.setText(String.valueOf(qty));
                    itemQtyMap.put(foodlist.get(index).getId(), qty);
                }
                float gstPer = Float.parseFloat(foodlist.get(index).getCgst()) + Float.parseFloat(foodlist.get(index).getSgst());

                totalPrice += Float.parseFloat(foodlist.get(index).getPrice());
                totalPrice += (gstPer / 100f) * Float.parseFloat(foodlist.get(index).getPrice());


                float totalPrice_in = Float.parseFloat(foodlist.get(index).getPrice()) * qty;
                float gsttotal = (gstPer / 100f) * totalPrice_in;

                if (gstPer > 0) {
                    viewHolder.binding.tvItemGST.setVisibility(View.VISIBLE);
                    viewHolder.binding.tvItemGST.setText("GST " + gstPer + " %");
                }

                viewHolder.binding.tvItemTotal.setText(String.format("Total Price: ₹ %.2f", totalPrice_in + gsttotal));

                ((FoodOrder_Activity) mContext).ItemsTotalFunction(totalPrice, itemQtyMap);
            }
        });

        viewHolder.binding.btnDecrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int index = viewHolder.getAdapterPosition();
                float gstPer = Float.parseFloat(foodlist.get(index).getCgst()) + Float.parseFloat(foodlist.get(index).getSgst());

                int qty = 0;
                if (itemQtyMap.containsKey(foodlist.get(index).getId())) {
                    qty = itemQtyMap.get(foodlist.get(index).getId());
                    qty--;


                    totalPrice -= Float.parseFloat(foodlist.get(index).getPrice());
                    totalPrice -= (gstPer / 100f) * Float.parseFloat(foodlist.get(index).getPrice());

//                    totalPrice -= Float.parseFloat(foodlist.get(index).getPrice());

                    viewHolder.binding.tvQuantity.setText(String.valueOf(qty));
                    itemQtyMap.put(foodlist.get(index).getId(), qty);
                    if (qty == 0) {
                        itemQtyMap.remove(foodlist.get(index).getId());
                    }
                } else {
                    viewHolder.binding.tvQuantity.setText(String.valueOf(0));
                }


                float totalPrice_de = 0;
                totalPrice_de = Float.parseFloat(foodlist.get(index).getPrice()) * qty;
//                // Update the TextView to display the new total price

//                float gstPer = Float.parseFloat(foodlist.get(index).getCgst()) + Float.parseFloat(foodlist.get(index).getSgst());
                float gsttotal = (gstPer / 100f) * totalPrice_de;
                if (gstPer > 0) {
                    viewHolder.binding.tvItemTotal.setVisibility(View.VISIBLE);
                    viewHolder.binding.tvItemGST.setText("GST " + gstPer + " %");
                }

                viewHolder.binding.tvItemTotal.setText(String.format("Total Price: ₹ %.2f", totalPrice_de + gsttotal));
                ((FoodOrder_Activity) mContext).ItemsTotalFunction(totalPrice, itemQtyMap);
            }
        });

//        viewHolder.btn2.setOnClickListener((ElegantNumberButton.OnClickListener) view -> {
//            String number = viewHolder.btn2.getNumber();
//            viewHolder.text_view.setText(number);
//            viewHolder.btn2.setNumber(number);
//
//        });


//        viewHolder.btn2.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
//            @Override
//            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
//                // Calculate new total price based on the newValue
//                double totalPrice = basePrice * newValue;
//
//                // Update the TextView to display the new total price
//                viewHolder.menuItemPrice.setText(String.format("Total Price: $%.2f", totalPrice));
//            }
//        });


//        viewHolder.btn_booknow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, BookParking_Activity.class)
//                        .putExtra("data", foodlist.get(viewHolder.getAdapterPosition())));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return foodlist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.menuItemName)
//        MaterialTextView menuItemName;
//        @BindView(R.id.menuItemCategory)
//        MaterialTextView menuItemCategory;
//        @BindView(R.id.menuItemPrice)
//        MaterialTextView menuItemPrice;
////        @BindView(R.id.text_view)
////        MaterialTextView text_view;
////        @BindView(R.id.number_button2)
////        ElegantNumberButton btn2;
//
//        @BindView(R.id.iv_item)
//        ImageView iv_item;
//        @BindView(R.id.foodMark)
//        ImageView foodMark;
//
//
//        @BindView(R.id.btn_decrement)
//        AppCompatImageButton btnDecrement;
//        @BindView(R.id.btn_increment)
//        AppCompatImageButton btnIncrement;
//        @BindView(R.id.tv_quantity)
//        TextView tvQuantity;
//        @BindView(R.id.tv_itemTotal)
//        TextView tv_itemTotal;
//        @BindView(R.id.tv_itemGST)
//        TextView tv_itemGST;
//
//
//        View view;

        RowFoodorderBinding binding;

        ViewHolder(RowFoodorderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        foodlist.clear();
        if (charText.length() == 0) {
            foodlist.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                FoodProduct g = list_search.get(i);
                if (g.getTitle().toLowerCase().contains(charText) | g.getSubtitle().toLowerCase().contains(charText) | g.getPrice().toLowerCase().contains(charText)) {
                    foodlist.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}