package com.mslji.mybluetooth.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.mslji.mybluetooth.fragment.RoportFragment;

import com.mslji.mybluetooth.fragment.FormFragment;
import com.mslji.mybluetooth.fragment.TableFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    public TabsAdapter(FragmentManager fm, int NoofTabs){
        super(fm);
        this.mNumOfTabs = NoofTabs;
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
    @Override
    public Fragment getItem(int position){
        switch (position){
            case 0:
                TableFragment about = new TableFragment();

                return about;



            case 1:
                FormFragment home = new FormFragment();
                return home;
            case 2:
                RoportFragment reportff = new RoportFragment();
                return reportff;

            default:
                return null;
        }
    }
}
