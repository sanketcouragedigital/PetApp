package com.couragedigital.petapp;

import android.annotation.TargetApi;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PetClinicDetails extends AppCompatActivity implements View.OnClickListener {

    String image_path = "";
    String clinicaddress = "";
    String doctorname = "";
    String cliniccity = "";
    String clinicarea = "";

    ImageView clinicImage;
    TextView address;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap clinicDetailsbitmap;
    Toolbar clinicDetailsToolbar;
    CollapsingToolbarLayout clinicDetailsCollapsingToolbar;
    CoordinatorLayout clinicDetailsCoordinatorLayout;
    AppBarLayout clinicDetailsAppBaLayoutr;
    NestedScrollView clinicDetailsNestedScrollView;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petclinicdetails);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("CLINIC_IMAGE");
            clinicaddress = intent.getStringExtra("CLINIC_ADDRESS");
            doctorname = intent.getStringExtra("DOCTOR_NAME");
            email = intent.getStringExtra("DOCTOR_EMAIL");
            phoneno = intent.getStringExtra("DOCTOR_CONTACT");
        }

        clinicDetailsToolbar = (Toolbar) findViewById(R.id.petClinicDetailsToolbar);
        setSupportActionBar(clinicDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clinicDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        clinicDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.clinicDetailsCollapsingToolbar);

        clinicDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.clinicDetailsCoordinatorLayout);

        clinicDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petClinicDetailsAppBar);

        clinicDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petClinicDetailsNestedScrollView);

        clinicImage = (ImageView) findViewById(R.id.petClicnicHeaderImage);
        address = (TextView) findViewById(R.id.petClinicAddress);
        callbutton = (Button) findViewById(R.id.clinicDetailCallButton);
        emailbutton = (Button) findViewById(R.id.clinicDetailsEmailButton);


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
        clinicDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        clinicImage.setImageBitmap(clinicDetailsbitmap);
        clinicDetailsCollapsingToolbar.setTitle(doctorname);
        address.setText(clinicaddress);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) clinicDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        clinicDetailsAppBaLayoutr.post(new Runnable() {
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
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) clinicDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(clinicDetailsCoordinatorLayout, clinicDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        PetClinicDetails.this.finish();
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.clinicDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        } else if (view.getId() == R.id.clinicDetailsEmailButton) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }
}


