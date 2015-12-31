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
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PetMetListDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String breed = "";
    String age = "";
    String gender = "";
    String description = "";
    String email = "";
    String mobileno = "";

    ImageView petMetImage;
    TextView petMetBreed;
    TextView petMetAge;
    TextView petMetGender;
    TextView petMetDescription;
    View petMetDetailsDividerLine;
    Button petMetDetailsCallButton;
    Button petMetDetailsEmailButton;

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
            age = intent.getStringExtra("PET_MET_AGE");
            gender = intent.getStringExtra("PET_MET_GENDER");
            description = intent.getStringExtra("PET_MET_DESCRIPTION");
            email = intent.getStringExtra("POST_OWNER_EMAIL");
            mobileno = intent.getStringExtra("POST_OWNER_MOBILENO");
        }

        petMetDetailstoolbar = (Toolbar) findViewById(R.id.petMetDetailsToolbar);
        setSupportActionBar(petMetDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petMetDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        petMetDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.petMetListingTypeInPetDetailsCollapsingToolbar);

        petMetDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.petMetDetailsCoordinatorLayout);

        petMetDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petMetDetailsAppBar);

        petMetDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petMetDetailsNestedScrollView);

        petMetImage = (ImageView) findViewById(R.id.petMetHeaderImage);
        petMetBreed = (TextView) findViewById(R.id.petMetBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        petMetAge = (TextView) findViewById(R.id.petMetAgeInPetDetails);
        petMetGender = (TextView) findViewById(R.id.petMetGenderInPetDetails);
        petMetDescription = (TextView) findViewById(R.id.petMetDescriptionInPetDetails);
        petMetDetailsDividerLine = findViewById(R.id.petMetDetailsDividerLine);
        petMetDetailsCallButton = (Button) findViewById(R.id.petMetDetailsCallButton);
        petMetDetailsEmailButton = (Button) findViewById(R.id.petMetDetailsEmailButton);

        petMetDetailsCallButton.setOnClickListener(this);
        petMetDetailsEmailButton.setOnClickListener(this);

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
        petMetDetailsCollapsingToolbar.setTitle("For Mating");
        String breedOfPet = "<b>Breed: </b>" + breed;
        petMetBreed.setText(Html.fromHtml(breedOfPet));
        String ageOfPet = "<b>Age: </b>" + age;
        petMetAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender: </b>" + gender;
        petMetGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet = "<b>Description: </b>" + description;
        petMetDescription.setText(Html.fromHtml(descriptionOfPet));
        petMetDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

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

    @Override
    public void onBackPressed() {
        PetMetListDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.petMetDetailsCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+mobileno));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.petMetDetailsEmailButton) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }
}