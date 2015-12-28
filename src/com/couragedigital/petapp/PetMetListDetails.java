package com.couragedigital.petapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PetMetListDetails extends AppCompatActivity {
    String image_path = "";
    String breed = "";
    String listingType = "";
    String age = "";
    String gender = "";
    String description = "";

    ImageView petMetImage;
    TextView petMetBreed;
    TextView petMetListingType;
    TextView petMetAge;
    TextView petMetGender;
    TextView petMetDescription;

    Bitmap petMetDetailsbitmap;
    Toolbar petMetDetailstoolbar;
    CollapsingToolbarLayout petMetDetailsCollapsingToolbar;
    CoordinatorLayout petMetDetailsCoordinatorLayout;
    AppBarLayout petMetDetailsAppBarLayout;
    NestedScrollView petMetDetailsNestedScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmetlistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("PET_MET_IMAGE");
            breed = intent.getStringExtra("PET_MET_BREED");
            listingType = "For Mating";
            age = intent.getStringExtra("PET_MET_AGE");
            gender = intent.getStringExtra("PET_MET_GENDER");
            description = intent.getStringExtra("PET_MET_DESCRIPTION");
        }

        petMetDetailstoolbar = (Toolbar) findViewById(R.id.petMetDetailsToolbar);
        setSupportActionBar(petMetDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        petMetDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.petListingTypeInPetDetailsCollapsingToolbar);

        petMetDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.petDetailsCoordinatorLayout);

        petMetDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petDetailsAppBar);

        petMetDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petDetailsNestedScrollView);

        petMetImage = (ImageView) findViewById(R.id.petHeaderImage);
        petMetBreed = (TextView) findViewById(R.id.petBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        petMetAge = (TextView) findViewById(R.id.petAgeInPetDetails);
        petMetGender = (TextView) findViewById(R.id.petGenderInPetDetails);
        petMetDescription = (TextView) findViewById(R.id.petDescriptionInPetDetails);

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
        petMetDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        petMetImage.setImageBitmap(petMetDetailsbitmap);
        petMetDetailsCollapsingToolbar.setTitle(listingType);
        petMetBreed.setText(breed);
        String ageOfPet = "<b>Pet Age :- </b>" + age;
        petMetAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender :- </b>" + gender;
        petMetGender.setText(Html.fromHtml(genderOfPet));
        petMetDescription.setText(description);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petMetDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;


        petMetDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) petMetDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(petMetDetailsCoordinatorLayout, petMetDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}