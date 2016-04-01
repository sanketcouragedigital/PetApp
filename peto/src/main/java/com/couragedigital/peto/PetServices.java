package com.couragedigital.peto;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.couragedigital.peto.Adapter.PagerAdapter;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;

public class PetServices extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petservices);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Pet Trainer"));
        tabLayout.addTab(tabLayout.newTab().setText("Pet Groomer"));
        tabLayout.addTab(tabLayout.newTab().setText("Pet Shelter"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetServices.this.getPackageManager();
        ComponentName component = new ComponentName(PetServices.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetServices.this.getPackageManager();
        ComponentName component = new ComponentName(PetServices.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}