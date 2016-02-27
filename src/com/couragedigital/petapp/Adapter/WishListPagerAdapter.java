package com.couragedigital.petapp.Adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.couragedigital.petapp.WishListPetListTab;
import com.couragedigital.petapp.WishListPetMateListTab;

public class WishListPagerAdapter extends FragmentStatePagerAdapter {
    private int noOfTabs;

    public WishListPagerAdapter(FragmentManager fm, int noOfTabs) {
        super(fm);
        this.noOfTabs = noOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        if (i == 0) {

            WishListPetListTab viewPetTab = new WishListPetListTab();
            return viewPetTab;

        } else if (i == 1) {
            WishListPetMateListTab petMateTab= new WishListPetMateListTab();
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
