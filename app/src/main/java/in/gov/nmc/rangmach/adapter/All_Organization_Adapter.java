package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import in.gov.nmc.rangmach.AddOrganization_Details_Activity;
import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowAllOrganizationBinding;
import in.gov.nmc.rangmach.model.AllOrganizationsList;

public class All_Organization_Adapter extends RecyclerView.Adapter<All_Organization_Adapter.ViewHolder> {
    Context mContext;
    List<AllOrganizationsList> booking_penalties;
    List<AllOrganizationsList> list_search = new ArrayList<>();
    public static int typeOfLanguage;

    public All_Organization_Adapter(Context mContext, List<AllOrganizationsList> booking_penalties) {
        this.mContext = mContext;
        this.booking_penalties = booking_penalties;
        list_search.addAll(booking_penalties);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_booking_penalty, parent, false);
//        return new ViewHolder(view);

        RowAllOrganizationBinding binding = RowAllOrganizationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return booking_penalties.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final AllOrganizationsList item = booking_penalties.get(i);

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
        viewHolder.binding.textTitle.setText(BaseActivity.GetTranslation("Name", typeOfLanguage));
        viewHolder.binding.textMobile.setText(BaseActivity.GetTranslation("Mobile No", typeOfLanguage));
        viewHolder.binding.textEmail.setText(BaseActivity.GetTranslation("Email", typeOfLanguage));
        viewHolder.binding.textAddress.setText(BaseActivity.GetTranslation("Address", typeOfLanguage));
        viewHolder.binding.textGst.setText(BaseActivity.GetTranslation("GST No", typeOfLanguage));
        viewHolder.binding.textStatus.setText(BaseActivity.GetTranslation("Status", typeOfLanguage));

        viewHolder.binding.tvAddress.setText(item.getAddress());
        viewHolder.binding.tvEmail.setText(item.getEmail());
        viewHolder.binding.tvMobile.setText(item.getMobile());
        viewHolder.binding.tvGst.setText(item.getGst());
        viewHolder.binding.tvName.setText(item.getName());

//        viewHolder.btn_items_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mContext.startActivity(new Intent(mContext, ItemsDetails_Activity.class)
//                        .putExtra("data", myAssetsOrders.get(viewHolder.getAdapterPosition())));
//            }
//        });

        if (item.getIsActive().equals("1")) {
            viewHolder.binding.tvStatus.setText("Active");
            viewHolder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            viewHolder.binding.tvStatus.setText("InActive");
            viewHolder.binding.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.red));
        }


        if (item.getId().equals("0")) {
            viewHolder.binding.ivEditcard.setVisibility(View.GONE);
        } else {
            viewHolder.binding.ivEditcard.setVisibility(View.VISIBLE);
        }
        viewHolder.binding.ivEditcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Parameter :- 1)booking_id , 2)center_id, 3)hall_id  4)title, 5)event_type 6)slot_id, 7)email
                ((AddOrganization_Details_Activity) mContext).EditOrganization_Dialog(viewHolder.getAdapterPosition(), item);
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        RowAllOrganizationBinding binding;

        ViewHolder(RowAllOrganizationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }


    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        booking_penalties.clear();
        if (charText.length() == 0) {
            booking_penalties.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                AllOrganizationsList g = list_search.get(i);
                if (g.getAddress().toLowerCase().contains(charText) || g.getEmail().toLowerCase().contains(charText) || g.getName().toLowerCase().contains(charText) || g.getGst().toLowerCase().contains(charText)) {
                    booking_penalties.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}