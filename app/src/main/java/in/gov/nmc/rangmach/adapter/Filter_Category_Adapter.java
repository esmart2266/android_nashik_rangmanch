package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import in.gov.nmc.rangmach.MainActivity;
import in.gov.nmc.rangmach.databinding.RowTheaternameBinding;

public class Filter_Category_Adapter extends RecyclerView.Adapter<Filter_Category_Adapter.ViewHolder> {
    Context mContext;
    ArrayList<String> list;

    public Filter_Category_Adapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_theatername, parent, false);
//        return new ViewHolder(view);

        RowTheaternameBinding binding = RowTheaternameBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        String namelist = list.get(i);

        for (int iv = 0; iv < list.size(); iv++) {

            viewHolder.binding.theaterCheckbox.setText(namelist);
        }

        // Set the position as a tag to identify the checkbox in the listener
        viewHolder.binding.theaterCheckbox.setTag(i);

        if (MainActivity.selectedcategory.contains(namelist)) {
            viewHolder.binding.theaterCheckbox.setChecked(true);
        } else {
            viewHolder.binding.theaterCheckbox.setChecked(false);
        }

        // Add an OnCheckedChangeListener to the checkbox
        viewHolder.binding.theaterCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int itemPosition = (int) buttonView.getTag();
                String item = list.get(itemPosition);
                if (isChecked) {
//                    Toast.makeText(buttonView.getContext(), "Checked position: " + itemPosition, Toast.LENGTH_SHORT).show();
                    MainActivity.selectedcategory.add(item);
//                    viewHolder.theater_checkbox.setOnClickListener(v -> ((MainActivity) mContext).Theatercount(viewHolder.getAdapterPosition()));

                } else {
                    MainActivity.selectedcategory.remove(item);
//                    Toast.makeText(buttonView.getContext(), "Unchecked position: " + itemPosition, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.theater_checkbox)
//        AppCompatCheckBox theater_checkbox;
//        @BindView(R.id.tv_textview)
//        AppCompatTextView tv_categoryname;
//        View view;


        RowTheaternameBinding binding;

        ViewHolder(RowTheaternameBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}
