package com.couragedigital.petapp;

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
import com.couragedigital.petapp.model.MyListingPetListItems;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;


public class MyListingPetListDetails extends AppCompatActivity implements View.OnClickListener {

    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String listingType = "";
    String ageInMonth = "";
    String ageInYear = "";
    String gender = "";
    String description = "";

    TextView mlPetDetailsImageText;
    View mlPetDetailsImagesDividerLine;
    ImageView mlPetDetailsFirstImageThumbnail;
    ImageView mlPetDetailsSecondImageThumbnail;
    ImageView mlPetDetailsThirdImageThumbnail;
    ImageView mlPetImage;
    TextView mlPetBreed;
    TextView mlPetForAdoptionOrSell;
    TextView mlPetAgeInMonth;
    TextView mlPetAgeInYear;
    TextView mlPetGender;
    TextView mlPetPrice;
    TextView mlPetDescription;
    View mlPetDetailsContentDividerLine;

    Bitmap mlPetDetailsbitmap;
    Toolbar mlPetDetailstoolbar;
    CollapsingToolbarLayout mlPetDetailsCollapsingToolbar;
    CoordinatorLayout mlPetDetailsCoordinatorLayout;
    AppBarLayout mlPetDetailsAppBarLayout;
    NestedScrollView mlPetDetailsNestedScrollView;
    private int mlMutedColor;

    MyListingPetListItems petListItems = new MyListingPetListItems();

    LinearLayout myListingPetListDetailsLinearLayout;
    RelativeLayout myListingPetListDetailsFirstRelativeLayout;
    RelativeLayout myListingPetListDetailsSecondRelativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylistingpetlistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("PET_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PET_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PET_THIRD_IMAGE");
            breed = intent.getStringExtra("PET_BREED");
            listingType = intent.getStringExtra("PET_LISTING_TYPE");
            ageInMonth = intent.getStringExtra("PET_AGE_IN_MONTH");
            ageInYear = intent.getStringExtra("PET_AGE_IN_YEAR");
            gender = intent.getStringExtra("PET_GENDER");
            description = intent.getStringExtra("PET_DESCRIPTION");
        }
        mlPetDetailstoolbar = (Toolbar) findViewById(R.id.myListingPetDetailsToolbar);
        setSupportActionBar(mlPetDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mlPetDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mlPetDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.myListingPetInPetDetailsCollapsingToolbar);

        mlPetDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.myListingPetDetailsCoordinatorLayout);

        mlPetDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.myListingPetDetailsAppBar);

        mlPetDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.myListingPetDetailsNestedScrollView);

        mlPetImage = (ImageView) findViewById(R.id.myListingPetHeaderImage);
        mlPetDetailsImageText = (TextView) findViewById(R.id.myListingPetDetailsImageText);
        mlPetDetailsImagesDividerLine = findViewById(R.id.myListingPetDetailsImagesDividerLine);
        mlPetDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.myListingPetDetailsFirstImageThumbnail);
        mlPetDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.myListingPetDetailsSecondImageThumbnail);
        mlPetDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.myListingPetDetailsThirdImageThumbnail);

        mlPetDetailsContentDividerLine = findViewById(R.id.myListingPetListDetailsContentDividerLine);
        mlPetBreed = (TextView) findViewById(R.id.myListingPetBreedInPetDetails);
        mlPetForAdoptionOrSell = (TextView) findViewById(R.id.myListingPetForAdoptionOrSell);
        mlPetAgeInMonth = (TextView) findViewById(R.id.myListingPetAgeInMonthPetDetails);
        mlPetAgeInYear = (TextView) findViewById(R.id.myListingPetAgeInYearPetDetails);
        mlPetGender = (TextView) findViewById(R.id.myListingPetGenderInPetDetails);
        mlPetPrice = (TextView) findViewById(R.id.myListingPetPriceInPetDetails);
        mlPetDescription = (TextView) findViewById(R.id.myListingPetDescriptionInPetDetails);

        myListingPetListDetailsLinearLayout = (LinearLayout) findViewById(R.id.myListingPetListDetailsLinearLayout);
        myListingPetListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.myListingPetListDetailsFirstRelativeLayout);
        myListingPetListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.myListingPetListDetailsSecondRelativeLayout);

        mlPetDetailsFirstImageThumbnail.setOnClickListener(this);
        mlPetDetailsSecondImageThumbnail.setOnClickListener(this);
        mlPetDetailsThirdImageThumbnail.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        mlPetDetailsCollapsingToolbar.setTitle(breed);

        mlPetDetailsImageText.setText("Images");
        mlPetDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }


        mlPetDetailsContentDividerLine.setBackgroundResource(R.color.list_internal_divider);
        mlPetForAdoptionOrSell.setText(setListingTypeTitle(listingType));
        mlPetBreed.setText(breed);
        mlPetAgeInMonth.setText("Month : " + ageInMonth);
        mlPetAgeInYear.setText("Year : " + ageInYear);
        mlPetGender.setText(gender);
        mlPetDescription.setText(description);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mlPetDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        mlPetDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        myListingPetListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = myListingPetListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = myListingPetListDetailsSecondRelativeLayout.getHeight();

                myListingPetListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
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
                Glide.with(mlPetImage.getContext()).load(url).centerCrop().crossFade().into(mlPetImage);
                Glide.with(mlPetDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetDetailsFirstImageThumbnail);
                mlPetDetailsFirstImageThumbnail.setOnClickListener(MyListingPetListDetails.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(mlPetDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetDetailsSecondImageThumbnail);
                mlPetDetailsSecondImageThumbnail.setOnClickListener(MyListingPetListDetails.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(mlPetDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(mlPetDetailsThirdImageThumbnail);
                mlPetDetailsThirdImageThumbnail.setOnClickListener(MyListingPetListDetails.this);
            }
        }
    }

    private String setListingTypeTitle(String listingType) {
        String setListingType;
        if (listingType.equals("For Adoption")) {
            setListingType = "To Adoption";
            //String priceOfPet = "<b>Price: N/A</b>";
            // petPrice.setText(Html.fromHtml(priceOfPet));
        } else {
            String priceOfPet = "Price: " + listingType;
            mlPetPrice.setText(Html.fromHtml(priceOfPet));
            setListingType = "To Sell";
        }
        return setListingType;
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) mlPetDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(mlPetDetailsCoordinatorLayout, mlPetDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        MyListingPetListDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.myListingPetDetailsFirstImageThumbnail) {
            Glide.with(mlPetImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(mlPetImage);
        } else if (view.getId() == R.id.myListingPetDetailsSecondImageThumbnail) {
            Glide.with(mlPetImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(mlPetImage);
        } else if (view.getId() == R.id.myListingPetDetailsThirdImageThumbnail) {
            Glide.with(mlPetImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(mlPetImage);
        }
    }
}