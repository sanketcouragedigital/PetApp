package com.couragedigital.petapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TabFragmentStoresDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String storesaddress = "";


    ImageView storesImage;

    TextView address;
    String storesname;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap storesDetailsbitmap;
    Toolbar storesDetailsToolbar;
    CollapsingToolbarLayout storesDetailsCollapsingToolbar;
    CoordinatorLayout storesDetailsCoordinatorLayout;
    AppBarLayout storesDetailsAppBaLayoutr;
    NestedScrollView storesDetailsNestedScrollView;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_stores_details);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("STORES_IMAGE");
            storesname = intent.getStringExtra("STORES_NAME");
            storesaddress = intent.getStringExtra("STORES_ADDRESS");
            email = intent.getStringExtra("STORES_EMAIL");
            phoneno = intent.getStringExtra("STORES_CONTACT");
        }

        storesDetailsToolbar = (Toolbar) findViewById(R.id.petStoresDetailsToolbar);
        setSupportActionBar(storesDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        storesDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        storesDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.storesDetailsCollapsingToolbar);

        storesDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.storesDetailsCoordinatorLayout);

        storesDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petStoresDetailsAppBar);

        storesDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petStoresDetailsNestedScrollView);

        storesImage = (ImageView) findViewById(R.id.petStoresHeaderImage);
        address = (TextView) findViewById(R.id.petStoresAddress);
        callbutton = (Button) findViewById(R.id.storesDetailCallButton);
        emailbutton = (Button) findViewById(R.id.storesDetailsEmailButton);


        InputStream in = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
            in = new BufferedInputStream(new URL(image_path).openStream(), 4 * 1024);
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
        BufferedOutputStream out = new BufferedOutputStream(dataStream, 4 * 1024);
        try {
            copy(in, out);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] data = dataStream.toByteArray();
        storesDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        storesImage.setImageBitmap(storesDetailsbitmap);
        address.setText(storesaddress);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) storesDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        storesDetailsAppBaLayoutr.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        callbutton.setOnClickListener(this);
        emailbutton.setOnClickListener(this);
    }
    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) storesDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(storesDetailsCoordinatorLayout, storesDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentStoresDetails.this.finish();
    }


    public void onClick(View view) {

        if (view.getId() == R.id.storesDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneno));
            startActivity(callIntent);
        }else if(view.getId() == R.id.storesDetailsEmailButton){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }


}