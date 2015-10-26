package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

public class BaseActivity extends AppCompatActivity   {
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    Toolbar toolbar;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    ArrayAdapter<String> arrayAdapter;

    @Override
    public void setContentView(int layoutResID) {

        drawerLayout = (DrawerLayout)  getLayoutInflater().inflate(R.layout.drawer,null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.contentFrame);
        drawerList = (ListView)drawerLayout.findViewById(R.id.leftDrawer);
        View header = getLayoutInflater().inflate(R.layout.drawerheaderimage, null);
        drawerList.addHeaderView(header, null, false);
        String[] menu={"Home","Profile","Setting","Login","LogOut"};
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,menu);
        drawerList.setSelector(R.color.colorPrimary);
        drawerList.setAdapter(arrayAdapter);
        toolbar = (Toolbar) drawerLayout.findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(drawerLayout);
		
		drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (drawerList.getItemAtPosition(position).toString());

                if(selectedFromList.equals("Log In")){
                    Intent gotoLogin =new Intent(BaseActivity.this,SignIn.class);
                    startActivity(gotoLogin);
                }
            }
        });
    }
}