package in.gov.nmc.rangmach.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import in.gov.nmc.rangmach.Fragment.Assets_Hall_Fragment;
import in.gov.nmc.rangmach.Fragment.NowShowing_Fragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new  Assets_Hall_Fragment();
            case 1:
                return new  NowShowing_Fragment();
            default:
                return new  Assets_Hall_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
