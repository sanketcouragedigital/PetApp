package com.couragedigital.peto;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.couragedigital.peto.Adapter.DrawerAdapterForNgo;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Adapter.DrawerAdapter;
import com.couragedigital.peto.model.DrawerItems;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class BaseActivity extends AppCompatActivity {

    Toolbar toolbar;
    ActionBarDrawerToggle drawerToggle;
    TextView txtlogout;
    SessionManager sessionManager;
    TextView lblName;
    TextView lblEmail;
    RecyclerView listItems;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    DrawerLayout drawer;
    FrameLayout frameLayout;
    LinearLayout linearLayout;
    DrawerAdapterForNgo drawerAdapterForNgo;
    DrawerAdapter drawerAdapter;
    String isNgo;

    public ArrayList<DrawerItems> itemArrayList;
    public ArrayList<DrawerItems> itemSelectedArrayListForNgo;

    public ArrayList<DrawerItems> itemArrayListForNgo;
    public ArrayList<DrawerItems> itemSelectedArrayList;

    @Override
    public void setContentView(int layoutResID) {
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer, null);
        frameLayout = (FrameLayout) drawer.findViewById(R.id.contentFrame);
        linearLayout = (LinearLayout) drawer.findViewById(R.id.drawerlinearlayout);
        listItems = (RecyclerView) drawer.findViewById(R.id.drawerListItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        listItems.setLayoutManager(linearLayoutManager);

        SessionManager sessionManagerNgo = new SessionManager(BaseActivity.this);
        HashMap<String, String> userOrNgo = sessionManagerNgo.getUserDetails();
        isNgo = userOrNgo.get(SessionManager.KEY_NGO);

        if (isNgo == null) {
            sessionManager = new SessionManager(BaseActivity.this);
            sessionManager.logoutUser();

        } else {
            //for ngo user
            if (isNgo.equals("Yes")) {
                final String[] tittleForNgo = new String[]{"Home", "Edit Profile", "My Listings", "WishList", "My Orders", "My Campaigns" ,"Feedback", "Share", "Legal", "LogOut"};

                final int[] iconsForNgo = new int[]{R.drawable.home, R.drawable.profile, R.drawable.mylisting, R.drawable.favourite, R.drawable.ordertruck, R.drawable.campaign, 0, 0, 0, 0};
                itemArrayListForNgo = new ArrayList<DrawerItems>();
                for (int i = 0; i < tittleForNgo.length; i++) {
                    DrawerItems drawerItems = new DrawerItems();
                    drawerItems.setTittle(tittleForNgo[i]);
                    drawerItems.setIcons(iconsForNgo[i]);
                    itemArrayListForNgo.add(drawerItems);
                }

                final int[] selectediconsForNgo = new int[]{R.drawable.home_red, R.drawable.profile_red, R.drawable.mylisting_red, R.drawable.favourite_enable, R.drawable.ordertruck_red,  R.drawable.campaign_red, 0, 0, 0, 0};
                itemSelectedArrayListForNgo = new ArrayList<DrawerItems>();
                for (int i = 0; i < tittleForNgo.length; i++) {
                    DrawerItems drawerItems = new DrawerItems();
                    drawerItems.setTittle(tittleForNgo[i]);
                    drawerItems.setIcons(selectediconsForNgo[i]);
                    itemSelectedArrayListForNgo.add(drawerItems);
                }
                drawerAdapterForNgo = new DrawerAdapterForNgo(itemArrayListForNgo, itemSelectedArrayListForNgo, drawer);
                getLayoutInflater().inflate(layoutResID, frameLayout, true);
                getLayoutInflater().inflate(layoutResID, linearLayout, true);
                drawer.setClickable(true);
                drawerAdapterForNgo.notifyDataSetChanged();
                listItems.setAdapter(drawerAdapterForNgo);
            }
            // for normal user
            else {
                final String[] tittle = new String[]{"Home", "Edit Profile", "My Listings", "WishList", "My Orders", "Feedback", "Share", "Legal", "LogOut"};
                final int[] icons = new int[]{R.drawable.home, R.drawable.profile, R.drawable.mylisting, R.drawable.favourite, R.drawable.ordertruck, 0, 0, 0, 0};
                itemArrayList = new ArrayList<DrawerItems>();
                for (int i = 0; i < tittle.length; i++) {
                    DrawerItems drawerItems = new DrawerItems();
                    drawerItems.setTittle(tittle[i]);
                    drawerItems.setIcons(icons[i]);
                    itemArrayList.add(drawerItems);
                }
                final int[] selectedicons = new int[]{R.drawable.home_red, R.drawable.profile_red, R.drawable.mylisting_red, R.drawable.favourite_enable, R.drawable.ordertruck_red, 0, 0, 0, 0};
                itemSelectedArrayList = new ArrayList<DrawerItems>();
                for (int i = 0; i < tittle.length; i++) {
                    DrawerItems drawerItems = new DrawerItems();
                    drawerItems.setTittle(tittle[i]);
                    drawerItems.setIcons(selectedicons[i]);
                    itemSelectedArrayList.add(drawerItems);
                }
                drawerAdapter = new DrawerAdapter(itemArrayList, itemSelectedArrayList, drawer);
                getLayoutInflater().inflate(layoutResID, frameLayout, true);
                getLayoutInflater().inflate(layoutResID, linearLayout, true);
                drawer.setClickable(true);
                drawerAdapter.notifyDataSetChanged();
                listItems.setAdapter(drawerAdapter);
            }
        }

        toolbar = (Toolbar) drawer.findViewById(R.id.app_bar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        drawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
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
        drawer.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        super.setContentView(drawer);
        sessionManager = new SessionManager(getApplicationContext());
        lblName = (TextView) findViewById(R.id.lblname);
        lblEmail = (TextView) findViewById(R.id.lblemail);

        HashMap<String, String> user = sessionManager.getUserDetails();

        String email = user.get(SessionManager.KEY_EMAIL);  // get email
     //   lblName.setText(name);   // Show user data on activity
        lblEmail.setText(email);
        changeAppFont();
    }

    public void changeAppFont() {
        setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Regular.ttf");

    }

    public static void setDefaultFont(Context context,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(context.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    public static void replaceFont(String staticTypefaceFieldName,
                                   final Typeface newTypeface) {

        if (Build.VERSION.SDK_INT > 16) {
            try {
                final Field staticField = Typeface.class.getDeclaredField(staticTypefaceFieldName);
                staticField.setAccessible(true);
                staticField.set(null, newTypeface);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}