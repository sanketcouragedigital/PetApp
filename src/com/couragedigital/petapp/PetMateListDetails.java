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
import com.couragedigital.petapp.model.PetMateListItems;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PetMateListDetails extends AppCompatActivity implements View.OnClickListener {
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String age = "";
    String ageInMonthForPetMate = "";
    String ageInYearForPetMate = "";
    String gender = "";
    String description = "";
    //String email = "";
    String mobileno = "";

    TextView petMateDetailsImageText;
    View petMateDetailsImagesDividerLine;
    ImageView petMateDetailsFirstImageThumbnail;
    ImageView petMateDetailsSecondImageThumbnail;
    ImageView petMateDetailsThirdImageThumbnail;
    ImageView petMateImage;
    TextView petMateBreed;
    TextView petMateAge;
    TextView petMateAgeInMonth;
    TextView petMateAgeInYear;
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

    PetMateListItems petMateListItems = new PetMateListItems();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmatelistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("PET_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PET_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PET_THIRD_IMAGE");
            breed = intent.getStringExtra("PET_MATE_BREED");
            //age = intent.getStringExtra("PET_MATE_AGE");
            ageInMonthForPetMate = intent.getStringExtra("PET_MATE_AGE_INMONTH");
            ageInYearForPetMate = intent.getStringExtra("PET_MATE_AGE_INYEAR");
            gender = intent.getStringExtra("PET_MATE_GENDER");
            description = intent.getStringExtra("PET_MATE_DESCRIPTION");
            //email = intent.getStringExtra("POST_OWNER_EMAIL");
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
        petMateDetailsImageText = (TextView) findViewById(R.id.petMateDetailsImageText);
        petMateDetailsImagesDividerLine = findViewById(R.id.petMateDetailsImagesDividerLine);
        petMateDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.petMateDetailsFirstImageThumbnail);
        petMateDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.petMateDetailsSecondImageThumbnail);
        petMateDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.petMateDetailsThirdImageThumbnail);

        petMateBreed = (TextView) findViewById(R.id.petMateBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        //petMateAge = (TextView) findViewById(R.id.petMateAgeInPetDetails);
        petMateAgeInMonth = (TextView) findViewById(R.id.petMateAgeInMonthInPetDetails);
        petMateAgeInYear = (TextView) findViewById(R.id.petMateAgeInYearInPetDetails);
        petMateGender = (TextView) findViewById(R.id.petMateGenderInPetDetails);
        petMateDescription = (TextView) findViewById(R.id.petMateDescriptionInPetDetails);
        petMateDetailsDividerLine = findViewById(R.id.petMateDetailsDividerLine);
        petMateDetailsCallButton = (Button) findViewById(R.id.petMateDetailsCallButton);
        //petMateDetailsEmailButton = (Button) findViewById(R.id.petMateDetailsEmailButton);

        petMateDetailsFirstImageThumbnail.setOnClickListener(this);
        petMateDetailsSecondImageThumbnail.setOnClickListener(this);
        petMateDetailsThirdImageThumbnail.setOnClickListener(this);
        petMateDetailsCallButton.setOnClickListener(this);
      //  petMateDetailsEmailButton.setOnClickListener(this);

        petMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
        petMateImage.setImageBitmap(petMateDetailsbitmap);
        petMateDetailsCollapsingToolbar.setTitle(breed);

        petMateDetailsImageText.setText("Images of " + breed);
        petMateDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        petMateDetailsFirstImageThumbnail.setImageBitmap(petMateDetailsbitmap);
        if(secondImagePath != null) {
            petMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            petMateDetailsSecondImageThumbnail.setImageBitmap(petMateDetailsbitmap);
        }
        if(thirdImagePath != null) {
            petMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            petMateDetailsThirdImageThumbnail.setImageBitmap(petMateDetailsbitmap);
        }
        String breedOfPet ="<b>Breed: </b>"+ breed;
        petMateBreed.setText(Html.fromHtml(breedOfPet));
        //String ageOfPet = "<b>Age: </b>" + age;
        //petMateAge.setText(Html.fromHtml(ageOfPet));
        String ageOfPetInMonth = "<b>Months: </b>"+ageInMonthForPetMate;
        petMateAgeInMonth.setText(Html.fromHtml(ageOfPetInMonth));
        String ageOfPetInYear = "<b>Years: </b>"+ageInYearForPetMate;
        petMateAgeInYear.setText(Html.fromHtml(ageOfPetInYear));
        String genderOfPet =  gender;
        petMateGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet =  description;
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
        petMateDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return petMateDetailsbitmap;
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
        /*else if(v.getId() == R.id.petMateDetailsEmailButton) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
        else if(v.getId() == R.id.petMateDetailsFirstImageThumbnail) {
            petMateDetailsbitmap = getBitmapImageFromURL(firstImagePath);
            petMateImage.setImageBitmap(petMateDetailsbitmap);
        }
        else if(v.getId() == R.id.petMateDetailsSecondImageThumbnail) {
            petMateDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            petMateImage.setImageBitmap(petMateDetailsbitmap);
        }
        else if(v.getId() == R.id.petMateDetailsThirdImageThumbnail) {
            petMateDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            petMateImage.setImageBitmap(petMateDetailsbitmap);
        }*/
    }
}