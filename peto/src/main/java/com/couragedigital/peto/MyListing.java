package com.couragedigital.peto;

import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.couragedigital.peto.Adapter.MyListingPagerAdapter;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;


public class MyListing extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Toolbar myListingToolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylisting);

        myListingToolbar = (Toolbar) findViewById(R.id.myListingToolbar);
        setSupportActionBar(myListingToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myListingToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.mylistingtablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Pet List"));
        tabLayout.addTab(tabLayout.newTab().setText("Pet Mate List "));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) findViewById(R.id.mylistingpager);
        MyListingPagerAdapter adapter = new MyListingPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
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
    public void onBackPressed() {
        MyListing.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = MyListing.this.getPackageManager();
        ComponentName component = new ComponentName(MyListing.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = MyListing.this.getPackageManager();
        ComponentName component = new ComponentName(MyListing.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}