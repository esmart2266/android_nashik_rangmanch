package in.gov.nmc.rangmach.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import in.gov.nmc.rangmach.App;
import in.gov.nmc.rangmach.R;
import in.gov.nmc.rangmach.databinding.RowHomeMenuBinding;


public class MenuListAdapter extends RecyclerView.Adapter<MenuListAdapter.RecyclerViewHolder> {
    private Context mContext;
    List<String> categoryList;
    App app;

    public MenuListAdapter(Context mContext, List<String> categoryList) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        app = (App) mContext.getApplicationContext();
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_home_menu, parent, false);
//        return new RecyclerViewHolder(view);

        RowHomeMenuBinding binding = RowHomeMenuBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RecyclerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        holder.binding.tvMenuTitle.setText(categoryList.get(position));
        switch (categoryList.get(position)) {
            case "Book Appointment":
                holder.binding.ivMenu.setImageResource(R.drawable.ic_book_appointment);
                break;
            case "My Documents":
                holder.binding.ivMenu.setImageResource(R.drawable.ic_my_documents);
                break;
            case "Reminders":
                holder.binding.ivMenu.setImageResource(R.drawable.ic_reminder);
                break;
            case "Job Search":
                holder.binding.ivMenu.setImageResource(R.drawable.ic_job_search);
                break;
        }
        holder.itemView.setOnClickListener(v -> {
            switch (categoryList.get(position)) {
                case "Book Appointment":
//                    mContext.startActivity(new Intent(mContext, AllPatientsActivity.class));
                    break;
                case "My Documents":
//                    mContext.startActivity(new Intent(mContext, MyDocumentsActivity.class));
                    break;
                case "Reminders":
//                    openBrowser(mContext, "https://mahavetnet.com");
                    break;
                case "Job Search":
//                    mContext.startActivity(new Intent(mContext, AnimalOwnerListActivity.class));
                    break;
            }
        });
//        if (position == getItemCount() - 1) {
//            holder.rrRoot.setPadding((int) (16 * scale + 0.5f), 0, (int) (16 * scale + 0.5f), 0);
//        } else {
//            holder.rrRoot.setPadding((int) (16 * scale + 0.5f), 0, 0, 0);
//        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

//        @BindView(R.id.ivMenu)
//        ImageView ivMenu;
//        @BindView(R.id.tvMenuTitle)
//        TextView tvMenuTitle;

        RowHomeMenuBinding binding;

        public RecyclerViewHolder(RowHomeMenuBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public View getView() {
            return binding.getRoot();
        }
    }
}

