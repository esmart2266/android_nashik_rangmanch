package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import in.gov.nmc.rangmach.BaseActivity;
import in.gov.nmc.rangmach.TheaterDetails_Activity;
import in.gov.nmc.rangmach.WebviewActivity;
import in.gov.nmc.rangmach.databinding.RowAllTheatersBinding;
import in.gov.nmc.rangmach.model.AllTheaters;

public class TheaterBooking_Adapter extends RecyclerView.Adapter<TheaterBooking_Adapter.ViewHolder> {
    Context mContext;
    List<AllTheaters> alltheaterlist;
    List<AllTheaters> list_search = new ArrayList<>();
    public static int typeOfLanguage;

    public TheaterBooking_Adapter(Context mContext, List<AllTheaters> videos) {
        this.mContext = mContext;
        this.alltheaterlist = videos;
        list_search.addAll(videos);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_all_theaters, parent, false);
//        return new ViewHolder(view);

        RowAllTheatersBinding binding = RowAllTheatersBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        if (alltheaterlist == null)
            return 0;

        return alltheaterlist.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final AllTheaters item = alltheaterlist.get(i);

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

        viewHolder.binding.totalhallassetsText.setText(BaseActivity.GetTranslation("Total Hall/Assets", typeOfLanguage));
        viewHolder.binding.carparkingText.setText(BaseActivity.GetTranslation("Car Parking", typeOfLanguage));
        viewHolder.binding.addressText.setText(BaseActivity.GetTranslation("Address", typeOfLanguage));
        viewHolder.binding.btnDetails.setText(BaseActivity.GetTranslation("Details", typeOfLanguage));
        viewHolder.binding.btnBooknow.setText(BaseActivity.GetTranslation("Book Now", typeOfLanguage));

//        if (selected_lang.equals("Hindi")) {
////            setLocal(mContext, "hi");
//
//            viewHolder.totalhallassets_text.setText("कुल हॉल");
//            viewHolder.carparking_text.setText("कार पार्किंग");
//            viewHolder.address_text.setText("पता");
//
//            viewHolder.btn_details.setText("विवरण");
//            viewHolder.btn_booknow.setText("अभी बुक करें");
//
//        }
//        else if (selected_lang.equals("Marathi")) {
////            setLocal(mContext, "mr");
//
//            viewHolder.totalhallassets_text.setText("एकूण हॉल");
//            viewHolder.carparking_text.setText("कार पार्किंग");
//            viewHolder.address_text.setText("पत्ता");
//
//            viewHolder.btn_details.setText("तपशील");
//            viewHolder.btn_booknow.setText("आता बुक करा");
//
//        }
//        else {
////            setLocal(mContext, "en");
//
//            viewHolder.totalhallassets_text.setText("Total Hall/Assets");
//            viewHolder.carparking_text.setText("Car Parking");
//            viewHolder.address_text.setText("Address");
//
//            viewHolder.btn_details.setText("Details");
//            viewHolder.btn_booknow.setText("Book Now");
//        }

        viewHolder.binding.tvTheatername.setText(item.getName());

//        int count = item.getHalls().size();
        int count = (item.getHalls() != null) ? item.getHalls().size() : 0;

        viewHolder.binding.tvTotalhalls.setText(count + "");
        viewHolder.binding.tvCarparking.setText(item.getParkingCount());
        viewHolder.binding.tvAddress.setText(item.getAddress());

        viewHolder.binding.llRecyclerview.setVisibility(View.GONE);

        if (item.getMedia() != null) {

            viewHolder.binding.llRecyclerview.setVisibility(View.VISIBLE);
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            viewHolder.binding.recyclerView.setLayoutManager(layoutManager);
            viewHolder.binding.recyclerView.setAdapter(new TheaterMedia_Adapter(mContext, item.getMedia()));

        } else {
            viewHolder.binding.llRecyclerview.setVisibility(View.GONE);
        }

        viewHolder.binding.mainCardview.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, TheaterDetails_Activity.class)
                    .putExtra("data", alltheaterlist.get(viewHolder.getAdapterPosition())));
        });

        viewHolder.binding.btnBooknow.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, TheaterDetails_Activity.class)
                    .putExtra("data", alltheaterlist.get(viewHolder.getAdapterPosition())));
        });

//        viewHolder.btn_booknow.setOnClickListener(view -> mContext.startActivity(new Intent(mContext, TheaterDetails_Activity.class)
//                .putExtra("halldata", (Serializable) alltheaterlist.get(viewHolder.getAdapterPosition()).getHalls())
//                .putExtra("media",(Serializable) alltheaterlist.get(viewHolder.getAdapterPosition()).getMedia())
//                .putExtra("descripition",alltheaterlist.get(viewHolder.getAdapterPosition()).getDescription())));

        viewHolder.binding.btnDetails.setVisibility(View.VISIBLE);
        viewHolder.binding.btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, WebviewActivity.class);
                String centerId = item.getWebsiteLink();
                intent.putExtra("website", centerId);
                intent.putExtra("name", item.getName());
                mContext.startActivity(intent);
            }
        });
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

//        @BindView(R.id.ll_recyclerview)
//        LinearLayout ll_recyclerview;
//        @BindView(R.id.recyclerView)
//        RecyclerView recyclerView;
//        @BindView(R.id.tv_theatername)
//        AppCompatTextView tv_theatername;
//        @BindView(R.id.tv_totalhalls)
//        AppCompatTextView tv_totalhalls;
//        @BindView(R.id.tv_carparking)
//        AppCompatTextView tv_carparking;
//        @BindView(R.id.tvAddress)
//        AppCompatTextView tvAddress;
//        @BindView(R.id.btn_details)
//        AppCompatButton btn_details;
//        @BindView(R.id.btn_booknow)
//        AppCompatButton btn_booknow;
//
//        @BindView(R.id.totalhallassets_text)
//        TextView totalhallassets_text;
//        @BindView(R.id.carparking_text)
//        TextView carparking_text;
//        @BindView(R.id.address_text)
//        TextView address_text;
//
//        View view;

        RowAllTheatersBinding binding;

        ViewHolder(RowAllTheatersBinding binding) {
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
        alltheaterlist.clear();
        if (charText.length() == 0) {
            alltheaterlist.addAll(list_search);
        } else {
            for (int i = 0; i < list_search.size(); i++) {
                AllTheaters g = list_search.get(i);
                if (g.getName().toLowerCase().contains(charText) || g.getAddress().toLowerCase().contains(charText) ||
                        g.getCity().toLowerCase().contains(charText)
                ) {
                    alltheaterlist.add(list_search.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }
}
