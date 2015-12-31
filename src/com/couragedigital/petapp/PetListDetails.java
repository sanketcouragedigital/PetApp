package com.couragedigital.petapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.*;
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

public class PetListDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String breed = "";
    String listingType = "";
    String age = "";
    String gender = "";
    String description = "";
    String email = "";
    String mobileno = "";

    ImageView petImage;
    TextView petBreed;
    TextView petListingType;
    TextView petAge;
    TextView petGender;
    TextView petPrice;
    TextView petDescription;
    View petDetailsDividerLine;
    Button petDetailsCallButton;
    Button petDetailsEmailButton;

    Bitmap petDetailsbitmap;
    Toolbar petDetailstoolbar;
    CollapsingToolbarLayout petDetailsCollapsingToolbar;
    CoordinatorLayout petDetailsCoordinatorLayout;
    AppBarLayout petDetailsAppBarLayout;
    NestedScrollView petDetailsNestedScrollView;
    private int mutedColor;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("PET_IMAGE");
            breed = intent.getStringExtra("PET_BREED");
            listingType = intent.getStringExtra("PET_LISTING_TYPE");
            age = intent.getStringExtra("PET_AGE");
            gender = intent.getStringExtra("PET_GENDER");
            description = intent.getStringExtra("PET_DESCRIPTION");
            email = intent.getStringExtra("POST_OWNER_EMAIL");
            mobileno = intent.getStringExtra("POST_OWNER_MOBILENO");
        }

        petDetailstoolbar = (Toolbar) findViewById(R.id.petDetailsToolbar);
        setSupportActionBar(petDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        petDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        petDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.petListingTypeInPetDetailsCollapsingToolbar);

        petDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.petDetailsCoordinatorLayout);

        petDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petDetailsAppBar);

        petDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petDetailsNestedScrollView);

        petImage = (ImageView) findViewById(R.id.petHeaderImage);
        petBreed = (TextView) findViewById(R.id.petBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        petAge = (TextView) findViewById(R.id.petAgeInPetDetails);
        petGender = (TextView) findViewById(R.id.petGenderInPetDetails);
        petPrice = (TextView) findViewById(R.id.petPriceInPetDetails);
        petDescription = (TextView) findViewById(R.id.petDescriptionInPetDetails);
        petDetailsDividerLine = findViewById(R.id.petDetailsDividerLine);
        petDetailsCallButton = (Button) findViewById(R.id.petDetailsCallButton);
        petDetailsEmailButton = (Button) findViewById(R.id.petDetailsEmailButton);

        petDetailsCallButton.setOnClickListener(this);
        petDetailsEmailButton.setOnClickListener(this);

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
        petDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        petImage.setImageBitmap(petDetailsbitmap);
        petDetailsCollapsingToolbar.setTitle(setListingTypeTitle(listingType));
        String breedOfPet = "<b>Breed: </b>" + breed;
        petBreed.setText(Html.fromHtml(breedOfPet));
        String ageOfPet = "<b>Age: </b>" + age;
        petAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender: </b>" + gender;
        petGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet = "<b>Description: </b>" + description;
        petDescription.setText(Html.fromHtml(descriptionOfPet));
        petDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        petDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        /*Palette.from(petDetailsbitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getMutedColor(R.color.colorPrimary);
                petDetailsCollapsingToolbar.setContentScrimColor(mutedColor);
            }
        });*/
    }

    private String setListingTypeTitle(String listingType) {
        String setListingType;
        if(listingType.equals("For Adoption")) {
            setListingType = "For Adoption";
            String priceOfPet = "<b>Price: N/A</b>";
            petPrice.setText(Html.fromHtml(priceOfPet));
        }
        else {
            String priceOfPet = "<b>Price: </b>" + listingType;
            petPrice.setText(Html.fromHtml(priceOfPet));
            setListingType = "For Sell";
        }
        return setListingType;
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(petDetailsCoordinatorLayout, petDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        PetListDetails.this.finish();
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.petDetailsCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+mobileno));
            startActivity(callIntent);
        }
        else if(v.getId() == R.id.petDetailsEmailButton) {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }
}