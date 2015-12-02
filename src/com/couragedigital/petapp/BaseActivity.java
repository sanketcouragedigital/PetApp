package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.*;
import com.couragedigital.petapp.SessionManager.SessionManager;

import java.util.HashMap;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class BaseActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FrameLayout frameLayout;
    Toolbar toolbar;
    ListView drawerList;
    ActionBarDrawerToggle drawerToggle;
    ArrayAdapter<String> arrayAdapter;
    TextView txtlogout;
    SessionManager sessionManager;

    TextView lblName;
    TextView lblEmail;

    @Override
    public void setContentView(int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.contentFrame);
        drawerList = (ListView) drawerLayout.findViewById(R.id.leftDrawer);
        View header = getLayoutInflater().inflate(R.layout.drawerheaderimage, null);
        drawerList.addHeaderView(header, null, false);
        String[] menu = {"Home", "Profile", "MyListings", "Account Setting", "FeedBack", "Share", "Login", "LogOut"};
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.drawermenulist, menu);
        drawerList.setSelector(R.color.colorPrimary);
        drawerList.setAdapter(arrayAdapter);
        toolbar = (Toolbar) drawerLayout.findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
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

        sessionManager = new SessionManager(getApplicationContext());
        lblName = (TextView) findViewById(R.id.lblname);
        lblEmail = (TextView) findViewById(R.id.lblemail);
        Toast.makeText(getApplicationContext(), "User Login Status: " + sessionManager.isLoggedIn(), Toast.LENGTH_LONG).show();

        HashMap<String, String> user = sessionManager.getUserDetails();
        String name = user.get(SessionManager.KEY_NAME); // get name
        String email = user.get(SessionManager.KEY_EMAIL);  // get email
        lblName.setText(name);   // Show user data on activity
        lblEmail.setText(email);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFromList = (drawerList.getItemAtPosition(position).toString());

                if (selectedFromList.equals("Login")) {
                    Intent gotoLogin = new Intent(BaseActivity.this, SignIn.class);
                    startActivity(gotoLogin);
                } else if (selectedFromList.equals("LogOut")) {
                    sessionManager.logoutUser();

                }
            }
        });
    }
}