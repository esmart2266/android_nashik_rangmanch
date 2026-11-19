package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import in.gov.nmc.rangmach.HallBooking_Activity;
import in.gov.nmc.rangmach.databinding.RowHallChallanBinding;
import in.gov.nmc.rangmach.model.Theater_Hall_Challan;

public class HallChallan_Adapter extends RecyclerView.Adapter<HallChallan_Adapter.ViewHolder> {
    Context mContext;
    List<Theater_Hall_Challan> hallFeesItems;
    String selectedDate;
    String slotID;
    public static int typeOfLanguage;
//    int quantity = 0;

    public HallChallan_Adapter(Context mContext, List<Theater_Hall_Challan> list, String date, String Sid) {
        this.mContext = mContext;
        this.hallFeesItems = list;
        selectedDate = date;
        slotID = Sid;
    }

    @Override
    public HallChallan_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_chart, parent, false);
//        return new ViewHolder(view);

        RowHallChallanBinding binding = RowHallChallanBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return hallFeesItems.size();
    }

    @Override
    public void onBindViewHolder(final HallChallan_Adapter.ViewHolder viewHolder, final int i) {
        final Theater_Hall_Challan item = hallFeesItems.get(i);

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
//        viewHolder.binding.tvTitle.setText(BaseActivity.GetTranslation("Title", typeOfLanguage));

        viewHolder.binding.tvTitle.setText(item.getTitle());
        viewHolder.binding.tvPrice.setText("₹ " + item.getPrice());

        if (item.getSubtitle() != null && !item.getSubtitle().trim().isEmpty()) {
            viewHolder.binding.tvSubtitle.setVisibility(View.VISIBLE);
            viewHolder.binding.tvSubtitle.setText(item.getSubtitle());
        } else {
            viewHolder.binding.tvSubtitle.setVisibility(View.GONE);
        }

        viewHolder.binding.ivMinus.setOnClickListener(v -> {

            if (item.selectedQty > 0) {
                item.selectedQty--;
                viewHolder.binding.tvQuantity.setText(String.valueOf(item.selectedQty));

                // Calculate total price after decrement
                updateTotalPrice(viewHolder, item, item.selectedQty);

                // Disable the minus button if quantity reaches 0
                if (item.selectedQty == 0) {
                    viewHolder.binding.ivMinus.setEnabled(false);
//                    Toast.makeText(mContext, "Quantity cannot be less than 0", Toast.LENGTH_SHORT).show();
                }

                ((HallBooking_Activity) mContext).Update_BookingCost();
            }
        });

        viewHolder.binding.ivAdd.setOnClickListener(v -> {
            item.selectedQty++;

            viewHolder.binding.tvQuantity.setText(String.valueOf(item.selectedQty));

            // Calculate total price after increment
            updateTotalPrice(viewHolder, item, item.selectedQty);

            // Re-enable the minus button when quantity is greater than 0
            if (item.selectedQty > 0) {
                viewHolder.binding.ivMinus.setEnabled(true);
            }

            ((HallBooking_Activity) mContext).Update_BookingCost();
        });
    }

    private void updateTotalPrice(ViewHolder viewHolder, Theater_Hall_Challan item, int quantity) {

        if (quantity > 0) {

            double pricePerItem = Double.parseDouble(item.getPrice()); // Example price per item, replace with actual price
            double sgstRate = Double.parseDouble(item.getSgst()); // SGST rate (example 9%)
            double cgstRate = Double.parseDouble(item.getCgst()); // CGST rate (example 9%)

            // Calculate total price
            double totalPrice = pricePerItem * quantity;

            // Calculate GST
            double sgstAmount = (sgstRate / 100) * totalPrice;
            double cgstAmount = (cgstRate / 100) * totalPrice;

            double totalGst = sgstAmount + cgstAmount;

            // Calculate final total price (with GST)
            double finalTotalPrice = totalPrice + sgstAmount + cgstAmount;

            item.total = finalTotalPrice;
            // Update the price TextView with total price and final price
            String priceText = String.format("Net: %.2f", totalPrice);
            String gstText = String.format("Tax: %.2f", totalGst);
            String finalText = String.format("Total: %.2f", finalTotalPrice);

            // Update the price TextView (assuming a TextView for displaying final price exists)
            viewHolder.binding.tvPrice.setText(priceText + "  " + gstText + "\n" + finalText);
            viewHolder.binding.tvPrice.setVisibility(View.VISIBLE);
        } else {
            viewHolder.binding.tvPrice.setText("₹ " + item.getPrice());
            viewHolder.binding.tvPrice.setVisibility(View.VISIBLE);
            item.total = 0;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowHallChallanBinding binding;

        ViewHolder(RowHallChallanBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }

}
