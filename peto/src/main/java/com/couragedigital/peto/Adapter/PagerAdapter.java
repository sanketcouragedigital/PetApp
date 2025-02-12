package com.couragedigital.peto.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.couragedigital.peto.TabFragmentTrainer;
import com.couragedigital.peto.TabFragmentGroomer;
import com.couragedigital.peto.TabFragmentShelter;


public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
    /*    if(position==0){
            TabFragmentTrainer tab1 = new TabFragmentTrainer();
            return tab1;
        } else if(position==1){
            TabFragmentGroomer tab2 = new TabFragmentGroomer();
            return tab2;
        }if(position==2){
            TabFragmentShelter tab3 = new TabFragmentShelter();
            return tab3;
        } else if(position==3){
            TabFragmentStores tab4 = new TabFragmentStores();
            return tab4;
        }else{
            return null;
        }
*/
        switch (position) {
            case 0:
                TabFragmentTrainer tab1 = new TabFragmentTrainer();
                return tab1;
            case 1:
                TabFragmentGroomer tab2 = new TabFragmentGroomer();
                return tab2;
            case 2:
                TabFragmentShelter tab3 = new TabFragmentShelter();
                return tab3;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}