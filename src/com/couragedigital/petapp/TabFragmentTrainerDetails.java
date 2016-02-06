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

public class TabFragmentTrainerDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String traineraddress = "";


    ImageView trainerImage;

    TextView address;
    String trainername;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap trainerDetailsbitmap;
    Toolbar trainerDetailsToolbar;
    CollapsingToolbarLayout trainerDetailsCollapsingToolbar;
    CoordinatorLayout trainerDetailsCoordinatorLayout;
    AppBarLayout trainerDetailsAppBaLayoutr;
    NestedScrollView trainerDetailsNestedScrollView;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_trainer_details);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("TRAINER_IMAGE");
            trainername = intent.getStringExtra("TRAINER_NAME");
            traineraddress = intent.getStringExtra("TRAINER_ADDRESS");
            email = intent.getStringExtra("TRAINER_EMAIL");
            phoneno = intent.getStringExtra("TRAINER_CONTACT");
        }

        trainerDetailsToolbar = (Toolbar) findViewById(R.id.petTrainerDetailsToolbar);
        setSupportActionBar(trainerDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trainerDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        trainerDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.trainerDetailsCollapsingToolbar);

        trainerDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.trainerDetailsCoordinatorLayout);

        trainerDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petTrainerDetailsAppBar);

        trainerDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petTrainerDetailsNestedScrollView);

        trainerImage = (ImageView) findViewById(R.id.petTrainerHeaderImage);
        address = (TextView) findViewById(R.id.petTrainerAddress);
        callbutton = (Button) findViewById(R.id.trainerDetailCallButton);
        emailbutton = (Button) findViewById(R.id.trainerDetailsEmailButton);


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
        trainerDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        trainerImage.setImageBitmap(trainerDetailsbitmap);
        address.setText(traineraddress);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) trainerDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        trainerDetailsAppBaLayoutr.post(new Runnable() {
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
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) trainerDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(trainerDetailsCoordinatorLayout, trainerDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentTrainerDetails.this.finish();
    }


    public void onClick(View view) {

        if (view.getId() == R.id.trainerDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneno));
            startActivity(callIntent);
        }else if(view.getId() == R.id.trainerDetailsEmailButton){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }


}