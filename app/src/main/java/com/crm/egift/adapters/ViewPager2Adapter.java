package com.crm.egift.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.crm.egift.fragments.SpendFragment;

public class ViewPager2Adapter extends FragmentStateAdapter {


    public ViewPager2Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return  new SpendFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
