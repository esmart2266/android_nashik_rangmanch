package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import in.gov.nmc.rangmach.R;

public class CustomSpinnerAdapter extends ArrayAdapter<String> {
    private Context context;
    private String[] items;

    public CustomSpinnerAdapter(Context context, int resource, String[] items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, parent, false);

        ImageView iconImageView = row.findViewById(R.id.spinner_icon);
        TextView textView = row.findViewById(R.id.spinner_text);

        iconImageView.setVisibility(View.GONE); // Hide the icon in the closed spinner view
        textView.setText(items[position]);

        return row;
    }
}

