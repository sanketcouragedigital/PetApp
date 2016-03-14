package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.petapp.model.MyListingPetMateListItem;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class MyListingPetMateListDetails extends AppCompatActivity implements View.OnClickListener {

    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String ageInMonth = "";
    String ageInYear = "";
    String gender = "";
    String description = "";

    TextView mlPetMateDetailsImageText;
    View mlPetMateDetailsImagesDividerLine;
    ImageView mlPetMateDetailsFirstImageThumbnail;
    ImageView mlPetMateDetailsSecondImageThumbnail;
    ImageView mlPetMateDetailsThirdImageThumbnail;
    ImageView mlPetMateImage;
    TextView mlPetMateBreed;
    TextView mlPetMateAgeInMonth;
    TextView mlPetMateAgeInYear;
    TextView mlPetMateGender;
    TextView mlPetMateDescription;
    View mlPetMateDetailsContentDividerLine;

    Bitmap mlPetMateDetailsbitmap;
    Toolbar mlPetMateDetailstoolbar;
    CollapsingToolbarLayout mlPetMateDetailsCollapsingToolbar;
    CoordinatorLayout mlPetMateDetailsCoordinatorLayout;
    AppBarLayout mlPetMateDetailsAppBarLayout;
    NestedScrollView mlPetMateDetailsNestedScrollView;

    MyListingPetMateListItem petMateListItems = new MyListingPetMateListItem();

    LinearLayout myListingPetMateListDetailsLinearLayout;
    RelativeLayout myListingPetMateListDetailsFirstRelativeLayout;
    RelativeLayout myListingPetMateListDetailsSecondRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylistingpetmatelistdetail);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("PET_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PET_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PET_THIRD_IMAGE");
            breed = intent.getStringExtra("PET_MATE_BREED");
            ageInMonth = intent.getStringExtra("PET_MATE_IN_MONTH");
            ageInYear = intent.getStringExtra("PET_MATE_IN_YEAR");
            gender = intent.getStringExtra("PET_MATE_GENDER");
            description = intent.getStringExtra("PET_MATE_DESCRIPTION");
        }

        mlPetMateDetailstoolbar = (Toolbar) findViewById(R.id.myListingPetMateDetailsToolbar);
        setSupportActionBar(mlPetMateDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mlPetMateDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mlPetMateDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.myListingPetMateDetailsCollapsingToolbar);

        mlPetMateDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.myListingPetMateDetailsCoordinatorLayout);

        mlPetMateDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.myListingPetMateDetailsAppBar);

        mlPetMateDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.myListingPetMateDetailsNestedScrollView);

        mlPetMateImage = (ImageView) findViewById(R.id.myListingPetMateDetailHeaderImage);
        mlPetMateDetailsImageText = (TextView) findViewById(R.id.myListingPetMateDetailsImageText);
        mlPetMateDetailsImagesDividerLine = findViewById(R.id.myListingPetMateDetailsImagesDividerLine);
        mlPetMateDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsFirstImageThumbnail);
        mlPetMateDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsSecondImageThumbnail);
        mlPetMateDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.myListingPetMateDetailsThirdImageThumbnail);

        mlPetMateBreed = (TextView) findViewById(R.id.myListingPetMateBreedInPetDetails);
        mlPetMateAgeInMonth = (TextView) findViewById(R.id.myListingPetAgeInMonthPetDetails);
        mlPetMateAgeInYear = (TextView) findViewById(R.id.myListingPetAgeInYearPetDetails);
        mlPetMateGender = (TextView) findViewById(R.id.myListingPetMateGenderInPetDetails);
        mlPetMateDescription = (TextView) findViewById(R.id.myListingPetMateDescriptionInPetDetails);
        mlPetMateDetailsContentDividerLine = findViewById(R.id.myListingPetMateDetailsContentDividerLine);

        myListingPetMateListDetailsLinearLayout = (LinearLayout) findViewById(R.id.myListingPetMateListDetailsLinearLayout);
        myListingPetMateListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.myListingPetMateListDetailsFirstRelativeLayout);
        myListingPetMateListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.myListingPetMateListDetailsSecondRelativeLayout);

        mlPetMateDetailsFirstImageThumbnail.setOnClickListener(this);
        mlPetMateDetailsSecondImageThumbnail.setOnClickListener(this);
        mlPetMateDetailsThirdImageThumbnail.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        mlPetMateDetailsCollapsingToolbar.setTitle(breed);

        mlPetMateDetailsImageText.setText("Images");
        mlPetMateDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }
        mlPetMateDetailsContentDividerLine.setBackgroundResource(R.color.list_internal_divider);
        mlPetMateBreed.setText(breed);
        mlPetMateAgeInMonth.setText("Month : " + ageInMonth);
        mlPetMateAgeInYear.setText(" Year : " + ageInYear);
        mlPetMateGender.setText(gender);
        mlPetMateDescription.setText(description);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mlPetMateDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        mlPetMateDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        myListingPetMateListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = myListingPetMateListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = myListingPetMateListDetailsSecondRelativeLayout.getHeight();

                myListingPetMateListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
            }
        });
    }

    public class FetchImageFromServer extends AsyncTask<String, String, String> {
        String urlForFetch;
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
            } catch (Exception e) {
                e.printStackTrace();
            }
            return urlForFetch;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            if(urlForFetch.equals(firstImagePath)) {
                Glide.with(mlPetMateImage.getContext()).load(url).centerCrop().crossFade().into(mlPetMateImage);
                Glide.with(mlPetMateDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetMateDetailsFirstImageThumbnail);
                mlPetMateDetailsFirstImageThumbnail.setOnClickListener(MyListingPetMateListDetails.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(mlPetMateDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetMateDetailsSecondImageThumbnail);
                mlPetMateDetailsSecondImageThumbnail.setOnClickListener(MyListingPetMateListDetails.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(mlPetMateDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetMateDetailsThirdImageThumbnail);
                mlPetMateDetailsThirdImageThumbnail.setOnClickListener(MyListingPetMateListDetails.this);
            }
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mlPetMateDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(mlPetMateDetailsCoordinatorLayout, mlPetMateDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    @Override
    public void onBackPressed() {
        MyListingPetMateListDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.myListingPetMateDetailsFirstImageThumbnail) {
            Glide.with(mlPetMateImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(mlPetMateImage);
        } else if (v.getId() == R.id.myListingPetMateDetailsSecondImageThumbnail) {
            Glide.with(mlPetMateImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(mlPetMateImage);
        } else if (v.getId() == R.id.myListingPetMateDetailsThirdImageThumbnail) {
            Glide.with(mlPetMateImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(mlPetMateImage);
        }
    }
}