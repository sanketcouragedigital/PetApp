package com.couragedigital.peto;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.model.PetMateListItems;

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
    View petMateListDetailsDividerLine;
    Button petMateDetailsCallButton;
    Button petMateDetailsEmailButton;

    Bitmap petMateDetailsbitmap;
    Toolbar petMateDetailstoolbar;
    CollapsingToolbarLayout petMateDetailsCollapsingToolbar;
    CoordinatorLayout petMateDetailsCoordinatorLayout;
    AppBarLayout petMateDetailsAppBarLayout;
    NestedScrollView petMateDetailsNestedScrollView;

    PetMateListItems petMateListItems = new PetMateListItems();

    LinearLayout petMateListDetailsLinearLayout;
    RelativeLayout petMateListDetailsFirstRelativeLayout;
    RelativeLayout petMateListDetailsSecondRelativeLayout;

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
            ageInMonthForPetMate = intent.getStringExtra("PET_MATE_AGE_MONTH");
            ageInYearForPetMate = intent.getStringExtra("PET_MATE_AGE_YEAR");
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
        petMateListDetailsDividerLine = findViewById(R.id.petMateListDetailsDividerLine);
        petMateDetailsCallButton = (Button) findViewById(R.id.petMateDetailsCallButton);
        //petMateDetailsEmailButton = (Button) findViewById(R.id.petMateDetailsEmailButton);
        petMateListDetailsLinearLayout = (LinearLayout) findViewById(R.id.petMateListDetailsLinearLayout);
        petMateListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petMateListDetailsFirstRelativeLayout);
        petMateListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.petMateListDetailsSecondRelativeLayout);

        petMateDetailsCallButton.setOnClickListener(this);
      //  petMateDetailsEmailButton.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        petMateDetailsCollapsingToolbar.setTitle(breed);

        petMateDetailsImageText.setText("Images");
        petMateDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }
        petMateBreed.setText(breed);
        //String ageOfPet = "<b>Age: </b>" + age;
        //petMateAge.setText(Html.fromHtml(ageOfPet));
        String ageOfPetInMonth = "Months: "+ageInMonthForPetMate;
        petMateAgeInMonth.setText(Html.fromHtml(ageOfPetInMonth));
        String ageOfPetInYear = "Years: "+ageInYearForPetMate;
        petMateAgeInYear.setText(Html.fromHtml(ageOfPetInYear));
        String genderOfPet =  gender;
        petMateGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet =  description;
        petMateDescription.setText(Html.fromHtml(descriptionOfPet));
        petMateDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);
        petMateListDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petMateDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;


        petMateDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petMateListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petMateListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = petMateListDetailsSecondRelativeLayout.getHeight();

                petMateListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
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
                Glide.with(petMateImage.getContext()).load(url).centerCrop().crossFade().into(petMateImage);
                Glide.with(petMateDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petMateDetailsFirstImageThumbnail);
                petMateDetailsFirstImageThumbnail.setOnClickListener(PetMateListDetails.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(petMateDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petMateDetailsSecondImageThumbnail);
                petMateDetailsSecondImageThumbnail.setOnClickListener(PetMateListDetails.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(petMateDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petMateDetailsThirdImageThumbnail);
                petMateDetailsThirdImageThumbnail.setOnClickListener(PetMateListDetails.this);
            }
        }
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
        else if(v.getId() == R.id.petMateDetailsFirstImageThumbnail) {
            Glide.with(petMateImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(petMateImage);
        }
        else if(v.getId() == R.id.petMateDetailsSecondImageThumbnail) {
            Glide.with(petMateImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(petMateImage);
        }
        else if(v.getId() == R.id.petMateDetailsThirdImageThumbnail) {
            Glide.with(petMateImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(petMateImage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetMateListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetMateListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetMateListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetMateListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}