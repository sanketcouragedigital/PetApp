package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.model.PetListItems;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class PetListDetails extends AppCompatActivity implements View.OnClickListener {
    String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String listingType = "";
    //String age = "";
    String ageInMonth = "";
    String ageInYear = "";
    String gender = "";
    String description = "";
    String email = "";
    String mobileno = "";
    String AdoptionOrSellLabel = "";

    TextView petDetailsImageText;
    TextView petAdoptOrSell;
    View petDetailsImagesDividerLine;
    ImageView petDetailsFirstImageThumbnail;
    ImageView petDetailsSecondImageThumbnail;
    ImageView petDetailsThirdImageThumbnail;
    ImageView petImage;
    TextView petBreed;
    TextView petListingType;
    TextView petAge;
    TextView petAgeInMonth;
    TextView petAgeInYear;
    TextView petGender;
    TextView petPrice;
    TextView petDescription;
    View petDetailsDividerLine;
    View petListDetailsDividerLine;
    View petListDetailsCallButtonDividerLine;
    Button petDetailsCallButton;
    //Button petDetailsEmailButton;

    Bitmap petDetailsbitmap;
    Toolbar petDetailstoolbar;
    CollapsingToolbarLayout petDetailsCollapsingToolbar;
    CoordinatorLayout petDetailsCoordinatorLayout;
    AppBarLayout petDetailsAppBarLayout;
    NestedScrollView petDetailsNestedScrollView;
    private int mutedColor;
    PetListItems petListItems = new PetListItems();

    LinearLayout petListDetailsLinearLayout;
    RelativeLayout petListDetailsFirstRelativeLayout;
    RelativeLayout petListDetailsSecondRelativeLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistdetails);

        Intent intent = getIntent();
        if (null != intent) {
            firstImagePath = intent.getStringExtra("PET_FIRST_IMAGE");
            secondImagePath = intent.getStringExtra("PET_SECOND_IMAGE");
            thirdImagePath = intent.getStringExtra("PET_THIRD_IMAGE");
            breed = intent.getStringExtra("PET_BREED");
            listingType = intent.getStringExtra("PET_LISTING_TYPE");
            //age = intent.getStringExtra("PET_AGE");
            ageInMonth = intent.getStringExtra("PET_AGE_MONTH");
            ageInYear = intent.getStringExtra("PET_AGE_YEAR");
            gender = intent.getStringExtra("PET_GENDER");
            description = intent.getStringExtra("PET_DESCRIPTION");
            //email = intent.getStringExtra("POST_OWNER_EMAIL");
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
        petDetailsImageText = (TextView) findViewById(R.id.petDetailsImageText);
        petDetailsImagesDividerLine = findViewById(R.id.petDetailsImagesDividerLine);
        petDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.petDetailsFirstImageThumbnail);
        petDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.petDetailsSecondImageThumbnail);
        petDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.petDetailsThirdImageThumbnail);

        petBreed = (TextView) findViewById(R.id.petBreedInPetDetails);
        petAdoptOrSell = (TextView) findViewById(R.id.petForaAdoptionOrSell);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        //petAge = (TextView) findViewById(R.id.petAgeInPetDetails);
        petAgeInMonth = (TextView) findViewById(R.id.petAgeInMonthInPetDetails);
        petAgeInYear = (TextView) findViewById(R.id.petAgeInYearInPetDetails);
        petGender = (TextView) findViewById(R.id.petGenderInPetDetails);
        petPrice = (TextView) findViewById(R.id.petPriceInPetDetails);
        petDescription = (TextView) findViewById(R.id.petDescriptionInPetDetails);
        petDetailsDividerLine = findViewById(R.id.petDetailsDividerLine);
        petListDetailsDividerLine = findViewById(R.id.petListDetailsDividerLine);
        petDetailsCallButton = (Button) findViewById(R.id.petDetailsCallButton);
        //petDetailsEmailButton = (Button) findViewById(R.id.petDetailsEmailButton);
        petListDetailsLinearLayout = (LinearLayout) findViewById(R.id.petListDetailsLinearLayout);
        petListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petListDetailsFirstRelativeLayout);
        petListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.petListDetailsSecondRelativeLayout);
        petListDetailsCallButtonDividerLine=findViewById(R.id.petDetailsDividerLine);

        petDetailsCallButton.setOnClickListener(this);
        //petDetailsEmailButton.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        //petDetailsCollapsingToolbar.setTitle(setListingTypeTitle(listingType));

        petDetailsCollapsingToolbar.setTitle(breed);

        petAdoptOrSell.setText(setListingTypeTitle(listingType));
        petDetailsImageText.setText("Images");
        petListDetailsCallButtonDividerLine.setBackgroundResource(R.color.list_internal_divider);
        petDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }
        String breedOfPet =  breed;
        petBreed.setText(Html.fromHtml(breedOfPet));
//        String age = "<b>Age: </b>";
//        petAge.setText(Html.fromHtml(age));
        String ageOfPetInMonth =  "Months: "+ageInMonth;
        petAgeInMonth.setText(Html.fromHtml(ageOfPetInMonth));
        String ageOfPetInYear = "Years: "+ageInYear;
        petAgeInYear.setText(Html.fromHtml(ageOfPetInYear));
        String genderOfPet =  gender;
        petGender.setText(Html.fromHtml(genderOfPet));
        String descriptionOfPet =  description;
        petDescription.setText(Html.fromHtml(descriptionOfPet));
        petDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);
        petListDetailsDividerLine.setBackgroundResource(R.color.list_internal_divider);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        petDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = petListDetailsSecondRelativeLayout.getHeight();

                petListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
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
                Glide.with(petImage.getContext()).load(url).centerCrop().crossFade().into(petImage);
                Glide.with(petDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petDetailsFirstImageThumbnail);
                petDetailsFirstImageThumbnail.setOnClickListener(PetListDetails.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(petDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petDetailsSecondImageThumbnail);
                petDetailsSecondImageThumbnail.setOnClickListener(PetListDetails.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(petDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(petDetailsThirdImageThumbnail);
                petDetailsThirdImageThumbnail.setOnClickListener(PetListDetails.this);
            }
        }
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
        petDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return petDetailsbitmap;
    }

    private String setListingTypeTitle(String listingType) {
        String setListingType;
        if(listingType.equals("For Adoption")) {
            setListingType = "For Adoption";
          //String priceOfPet = "<b>Price: N/A</b>";
           // petPrice.setText(Html.fromHtml(priceOfPet));
        }
        else {
            String priceOfPet = "Price: " + listingType;
            petPrice.setText(Html.fromHtml(priceOfPet));
            setListingType = "To Sell";
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
        else if(v.getId() == R.id.petDetailsFirstImageThumbnail) {
            Glide.with(petImage.getContext()).load(firstImagePath).centerCrop().crossFade().into(petImage);
        }
        else if(v.getId() == R.id.petDetailsSecondImageThumbnail) {
            Glide.with(petImage.getContext()).load(secondImagePath).centerCrop().crossFade().into(petImage);
        }
        else if(v.getId() == R.id.petDetailsThirdImageThumbnail) {
            Glide.with(petImage.getContext()).load(thirdImagePath).centerCrop().crossFade().into(petImage);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}