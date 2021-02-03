package com.avion.app;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.avion.app.fragment.CarFragment;

public class CarViewPaggerAdapter extends FragmentPagerAdapter {
    public CarViewPaggerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return CarFragment.newInstance(0, false);
    }

    @Override
    public int getCount() {
        return 1;
    }
}
