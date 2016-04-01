package com.couragedigital.peto.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.couragedigital.peto.MyListingPetMateListTab;
import com.couragedigital.peto.MyListingPetListTab;

public class MyListingPagerAdapter extends FragmentStatePagerAdapter {
    private int noOfTabs;

    public MyListingPagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {

            MyListingPetListTab viewPetTab = new MyListingPetListTab();
            return viewPetTab;

        } else if (i == 1) {

            MyListingPetMateListTab petMateTab = new MyListingPetMateListTab();
            return petMateTab;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return noOfTabs;
    }
}
