package com.couragedigital.petapp;

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

public class PetMateListDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String breed = "";
    String age = "";
    String gender = "";
    String description = "";
    String email = "";
    String mobileno = "";

    ImageView petMateImage;
    TextView petMateBreed;
    TextView petMateAge;
    TextView petMateGender;
    TextView petMateDescription;
    View petMateDetailsDividerLine;
    Button petMateDetailsCallButton;
    Button petMateDetailsEmailButton;

    Bitmap petMateDetailsbitmap;
    Toolbar petMateDetailstoolbar;
    CollapsingToolbarLayout petMateDetailsCollapsingToolbar;
    CoordinatorLayout petMateDetailsCoordinatorLayout;
    AppBarLayout petMateDetailsAppBarLayout;
    NestedScrollView petMateDetailsNestedScrollView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmatelistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("PET_MATE_IMAGE");
            breed = intent.getStringExtra("PET_MATE_BREED");
            age = intent.getStringExtra("PET_MATE_AGE");
            gender = intent.getStringExtra("PET_MATE_GENDER");
            description = intent.getStringExtra("PET_MATE_DESCRIPTION");
            email = intent.getStringExtra("POST_OWNER_EMAIL");
            mobileno = intent.getStringExtra("POST_OWNER_MOBILENO");
        }

        petMateDetailstoolbar = (Toolbar) findViewById(R.id.petMateDetailsToolbar);
        setSupportActionBar(petMateDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petMateDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        petMateDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.petMateListingTypeInPetDetailsCollapsingToolbar);

        petMateDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.petMateDetailsCoordinatorLayout);

        petMateDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petMateDetailsAppBar);

        petMateDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petMateDetailsNestedScrollView);

        petMateImage = (ImageView) findViewById(R.id.petMateHeaderImage);
        petMateBreed = (TextView) findViewById(R.id.petMateBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        petMateAge = (TextView) findViewById(R.id.petMateAgeInPetDetails);
        petMateGender = (TextView) findViewById(R.id.petMateGenderInPetDetails);
        petMateDescription = (TextView) findViewById(R.id.petMateDescriptionInPetDetails);
        petMateDetailsDividerLine = findViewById(R.id.petMateDetailsDividerLine);
        petMateDetailsCallButton = (Button) findViewById(R.id.petMateDetailsCallButton);
        petMateDetailsEmailButton = (Button) findViewById(R.id.petMateDetailsEmailButton);

        petMateDetailsCallButton.setOnClickListener(this);
        petMateDetailsEmailButton.setOnClickListener(this);

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
        petMateDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        petMateImage.setImageBitmap(petMateDetailsbitmap);
        petMateDetailsCollapsingToolbar.setTitle("For Mating");
        String breedOfPet = "<b>Breed: </b>" + breed;
        petMateBreed.setText(Html.fromHtml(breedOfPet));
        String ageOfPet = "<b>Age: </b>" + age;
        petMateAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender: </b>" + gender;
        petMateGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet = "<b>Description: </b>" + description;
        petMateDescription.setText(Html.fromHtml(descriptionOfPet));
        petMateDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petMateDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;


        petMateDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) petMateDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(petMateDetailsCoordinatorLayout, petMateDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        PetMateListDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.petMateDetailsCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+mobileno));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.petMateDetailsEmailButton) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }
}