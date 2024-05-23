package com.rodmat95.trucksgps.Controller;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.rodmat95.trucksgps.Main.Home.DriversFragment;
import com.rodmat95.trucksgps.Main.Home.TrucksFragment;

public class PagerController extends FragmentPagerAdapter {
    int numOfTabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numOfTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TrucksFragment();
            case 1:
                return new DriversFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}