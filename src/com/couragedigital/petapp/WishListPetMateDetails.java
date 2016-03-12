package com.couragedigital.petapp;

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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.couragedigital.petapp.model.MyListingPetMateListItem;
import com.couragedigital.petapp.model.WishListPetMateListItem;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class WishListPetMateDetails extends AppCompatActivity implements View.OnClickListener {
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String ageInMonth = "";
    String ageInYear = "";
    String gender = "";
    String description = "";
    String alternateNo = "";
    String name = "";

    TextView wlPetMateDetailsImageText;
    View wlPetMateDetailsImagesDividerLine;
    ImageView wlPetMateDetailsFirstImageThumbnail;
    ImageView wlPetMateDetailsSecondImageThumbnail;
    ImageView wlPetMateDetailsThirdImageThumbnail;
    ImageView wlPetMateImage;
    TextView wlPetMateBreed;
    TextView wlPetMateAgeInMonth;
    TextView wlPetMateAgeInYear;
    TextView wlPetMateGender;
    TextView wlPetMateDescription;
    TextView wlPetMateName;
    View wlPetMateDetailsContentDividerLine;
    Button wlPetListalternateNo;

    Bitmap wlPetMateDetailsbitmap;
    Toolbar wlPetMateDetailstoolbar;
    CollapsingToolbarLayout wlPetMateDetailsCollapsingToolbar;
    CoordinatorLayout wlPetMateDetailsCoordinatorLayout;
    AppBarLayout wlPetMateDetailsAppBarLayout;
    NestedScrollView wlPetMateDetailsNestedScrollView;

    WishListPetMateListItem petMateListItems = new WishListPetMateListItem();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlistpetmatedetails);

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
            alternateNo=intent.getStringExtra("ALTERNATE_NO");
            name=intent.getStringExtra("NAME");
        }

        wlPetMateDetailstoolbar = (Toolbar) findViewById(R.id.wishlistPetMateDetailsToolbar);
        setSupportActionBar(wlPetMateDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wlPetMateDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wlPetMateDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.wishlistPetMateDetailsCollapsingToolbar);

        wlPetMateDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.wishlistPetMateDetailsCoordinatorLayout);

        wlPetMateDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.wishlistPetMateDetailsAppBar);

        wlPetMateDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.wishlistPetMateDetailsNestedScrollView);

        wlPetMateImage = (ImageView) findViewById(R.id.wishlistPetMateDetailHeaderImage);
        wlPetMateDetailsImageText = (TextView) findViewById(R.id.wishlistPetMateDetailsImageText);
        wlPetMateDetailsImagesDividerLine = findViewById(R.id.wishlistPetMateDetailsImagesDividerLine);
        wlPetMateDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.wishlistPetMateDetailsFirstImageThumbnail);
        wlPetMateDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.wishlistPetMateDetailsSecondImageThumbnail);
        wlPetMateDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.wishlistPetMateDetailsThirdImageThumbnail);

        wlPetMateBreed = (TextView) findViewById(R.id.wishlistPetMateBreedInPetDetails);
        wlPetMateAgeInMonth = (TextView) findViewById(R.id.wishlistPetAgeInMonthPetDetails);
        wlPetMateAgeInYear = (TextView) findViewById(R.id.wishlistPetAgeInYearPetDetails);
        wlPetMateGender = (TextView) findViewById(R.id.wishlistPetMateGenderInPetDetails);
        wlPetMateDescription = (TextView) findViewById(R.id.wishlistPetMateDescriptionInPetDetails);
        wlPetMateDetailsContentDividerLine = findViewById(R.id.wishlistPetMateDetailsContentDividerLine);
        wlPetListalternateNo =(Button) findViewById(R.id.pmWishListCallButton);
        wlPetMateName = (TextView) findViewById(R.id.petMatePostedBy);

        wlPetMateDetailsFirstImageThumbnail.setOnClickListener(this);
        wlPetMateDetailsSecondImageThumbnail.setOnClickListener(this);
        wlPetMateDetailsThirdImageThumbnail.setOnClickListener(this);
        wlPetListalternateNo.setOnClickListener(this);

        wlPetMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
        wlPetMateImage.setImageBitmap(wlPetMateDetailsbitmap);
        wlPetMateDetailsCollapsingToolbar.setTitle("For Mating");

        wlPetMateDetailsImageText.setText("Images of " + breed);
        //wlPetMateName.setText("Posted By :" + name);
        wlPetMateDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        wlPetMateDetailsFirstImageThumbnail.setImageBitmap(wlPetMateDetailsbitmap);
        if (secondImagePath != null) {
            wlPetMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            wlPetMateDetailsSecondImageThumbnail.setImageBitmap(wlPetMateDetailsbitmap);
        }
        if (thirdImagePath != null) {
            wlPetMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            wlPetMateDetailsThirdImageThumbnail.setImageBitmap(wlPetMateDetailsbitmap);
        }
        wlPetMateDetailsContentDividerLine.setBackgroundResource(R.color.list_internal_divider);
        wlPetMateBreed.setText(breed);
        wlPetMateAgeInMonth.setText("Month : " + ageInMonth);
        wlPetMateAgeInYear.setText(" Year : " + ageInYear);
        wlPetMateGender.setText(gender);
        wlPetMateDescription.setText(description);


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) wlPetMateDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        wlPetMateDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });
    }

    private Bitmap getBitmapImageFromURL(String imagePath) {
        InputStream in = null;
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
            in = new BufferedInputStream(new URL(imagePath).openStream(), 4 * 1024);
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
        wlPetMateDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return wlPetMateDetailsbitmap;
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) wlPetMateDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(wlPetMateDetailsCoordinatorLayout, wlPetMateDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        WishListPetMateDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.wishlistPetMateDetailsFirstImageThumbnail) {
            wlPetMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
            wlPetMateImage.setImageBitmap(wlPetMateDetailsbitmap);
        } else if (v.getId() == R.id.wishlistPetMateDetailsSecondImageThumbnail) {
            wlPetMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            wlPetMateImage.setImageBitmap(wlPetMateDetailsbitmap);
        } else if (v.getId() == R.id.wishlistPetMateDetailsThirdImageThumbnail) {
            wlPetMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            wlPetMateImage.setImageBitmap(wlPetMateDetailsbitmap);
        } else if (v.getId() == R.id.pmWishListCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + alternateNo));
            startActivity(callIntent);
        }
    }

}