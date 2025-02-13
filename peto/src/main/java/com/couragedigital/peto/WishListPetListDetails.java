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
import com.couragedigital.peto.model.WishListPetListItem;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
public class WishListPetListDetails extends AppCompatActivity implements View.OnClickListener {
    
	String firstImagePath = "";
    String secondImagePath = "";
    String thirdImagePath = "";
    String breed = "";
    String listingType = "";
    String ageInMonth = "";
    String ageInYear = "";
    String gender = "";
    String description = "";
    String alternateNo = "";
    String name = "";

    TextView wlPetDetailsImageText;
    View wlPetDetailsImagesDividerLine;
    ImageView wlPetDetailsFirstImageThumbnail;
    ImageView wlPetDetailsSecondImageThumbnail;
    ImageView wlPetDetailsThirdImageThumbnail;
    ImageView wlPetImage;
    TextView wlPetBreed;
    TextView wlPetForAdoptionOrSell;
    TextView wlPetAgeInMonth;
    TextView wlPetAgeInYear;
    TextView wlPetGender;
    TextView wlPetPrice;
    TextView wlPetDescription;
    TextView wlPetName;
    View wlPetDetailsContentDividerLine;
    View wlPetDetailsButtonDividerLine;
    Button wlalternateNo;

    Bitmap wlPetDetailsbitmap;
    Toolbar wlPetDetailstoolbar;
    CollapsingToolbarLayout wlPetDetailsCollapsingToolbar;
    CoordinatorLayout wlPetDetailsCoordinatorLayout;
    AppBarLayout wlPetDetailsAppBarLayout;
    NestedScrollView wlPetDetailsNestedScrollView;

    LinearLayout petWishListDetailsLinearLayout;
    RelativeLayout petWishListDetailsFirstRelativeLayout;
    RelativeLayout petWishListDetailsSecondRelativeLayout;

    private int wlMutedColor;
    WishListPetListItem petListItems = new WishListPetListItem();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlistpetlistdetails);

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
            alternateNo = intent.getStringExtra("ALTERNATE_NO");
            name = intent.getStringExtra("NAME");
        }
        wlPetDetailstoolbar = (Toolbar) findViewById(R.id.wishlistPetDetailsToolbar);
        setSupportActionBar(wlPetDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        wlPetDetailstoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        wlPetDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.wishlistPetInPetDetailsCollapsingToolbar);

        wlPetDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.wishlistPetDetailsCoordinatorLayout);

        wlPetDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.wishlistPetDetailsAppBar);

        wlPetDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.wishlistPetDetailsNestedScrollView);

        wlPetImage = (ImageView) findViewById(R.id.wishlistPetHeaderImage);
        wlPetDetailsImageText = (TextView) findViewById(R.id.wishlistPetDetailsImageText);
        wlPetDetailsImagesDividerLine = findViewById(R.id.wishlistPetDetailsImagesDividerLine);
        wlPetDetailsFirstImageThumbnail = (ImageView) findViewById(R.id.wishlistPetDetailsFirstImageThumbnail);
        wlPetDetailsSecondImageThumbnail = (ImageView) findViewById(R.id.wishlistPetDetailsSecondImageThumbnail);
        wlPetDetailsThirdImageThumbnail = (ImageView) findViewById(R.id.wishlistPetDetailsThirdImageThumbnail);

        wlPetDetailsContentDividerLine = findViewById(R.id.wishlistPetListDetailsContentDividerLine);
        wlPetDetailsButtonDividerLine = findViewById(R.id.wishlistPetButonDividerLine);
        wlPetBreed = (TextView) findViewById(R.id.wishlistPetBreedInPetDetails);
        wlPetForAdoptionOrSell = (TextView) findViewById(R.id.wishlistPetForAdoptionOrSell);
        wlPetAgeInMonth = (TextView) findViewById(R.id.wishlistPetAgeInMonthPetDetails);
        wlPetAgeInYear = (TextView) findViewById(R.id.wishlistPetAgeInYearPetDetails);
        wlPetGender = (TextView) findViewById(R.id.wishlistPetGenderInPetDetails);
        wlPetPrice = (TextView) findViewById(R.id.wishlistPetPriceInPetDetails);
        wlPetDescription = (TextView) findViewById(R.id.wishlistPetDescriptionInPetDetails);
        wlalternateNo = (Button)findViewById(R.id.pWishListCallButton);
        wlPetName = (TextView) findViewById(R.id.petPostedBy);
        petWishListDetailsLinearLayout = (LinearLayout) findViewById(R.id.petWishListDetailsLinearLayout);
        petWishListDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petWishListDetailsFirstRelativeLayout);
        petWishListDetailsSecondRelativeLayout = (RelativeLayout) findViewById(R.id.petWishListDetailsSecondRelativeLayout);

        wlPetDetailsFirstImageThumbnail.setOnClickListener(this);
        wlPetDetailsSecondImageThumbnail.setOnClickListener(this);
        wlPetDetailsThirdImageThumbnail.setOnClickListener(this);
        wlalternateNo.setOnClickListener(this);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, firstImagePath);
        wlPetDetailsCollapsingToolbar.setTitle(breed);

        wlPetDetailsImageText.setText("Images of :" + breed);
        //wlPetName.setText("Posted By :" + name);
        wlPetDetailsImagesDividerLine.setBackgroundResource(R.color.list_internal_divider);
        wlPetDetailsFirstImageThumbnail.setImageBitmap(wlPetDetailsbitmap);
        if(secondImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, secondImagePath);
        }
        if(thirdImagePath != null) {
            new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, thirdImagePath);
        }

        wlPetDetailsContentDividerLine.setBackgroundResource(R.color.list_internal_divider);
        wlPetDetailsButtonDividerLine.setBackgroundResource(R.color.list_internal_divider);
        wlPetForAdoptionOrSell.setText(setListingTypeTitle(listingType));
        wlPetBreed.setText(breed);
        wlPetAgeInMonth.setText("Month : " + ageInMonth);
        wlPetAgeInYear.setText("Year : " + ageInYear);
        wlPetGender.setText(gender);
        wlPetDescription.setText(description);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) wlPetDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        wlPetDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petWishListDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petWishListDetailsFirstRelativeLayout.getHeight();
                Integer heightOfSecondRelativeLayout = petWishListDetailsSecondRelativeLayout.getHeight();

                petWishListDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + heightOfSecondRelativeLayout + 200);
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
                Glide.with(wlPetImage.getContext()).load(url).centerCrop().crossFade().into(wlPetImage);
                Glide.with(wlPetDetailsFirstImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(wlPetDetailsFirstImageThumbnail);
                wlPetDetailsFirstImageThumbnail.setOnClickListener(WishListPetListDetails.this);
            }
            if(urlForFetch.equals(secondImagePath)) {
                Glide.with(wlPetDetailsSecondImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(wlPetDetailsSecondImageThumbnail);
                wlPetDetailsSecondImageThumbnail.setOnClickListener(WishListPetListDetails.this);
            }
            if(urlForFetch.equals(thirdImagePath)) {
                Glide.with(wlPetDetailsThirdImageThumbnail.getContext()).load(url).centerCrop().crossFade().into(wlPetDetailsThirdImageThumbnail);
                wlPetDetailsThirdImageThumbnail.setOnClickListener(WishListPetListDetails.this);
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
        wlPetDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return wlPetDetailsbitmap;
    }

    private String setListingTypeTitle(String listingType) {
        String setListingType;
        if (listingType.equals("For Adoption")) {
            setListingType = "To Adoption";
            //String priceOfPet = "<b>Price: N/A</b>";
            // petPrice.setText(Html.fromHtml(priceOfPet));
        } else {
            String priceOfPet = "Price: " + listingType;
            wlPetPrice.setText(Html.fromHtml(priceOfPet));
            setListingType = "To Sell";
        }
        return setListingType;
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) wlPetDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(wlPetDetailsCoordinatorLayout, wlPetDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
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
        WishListPetListDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.wishlistPetDetailsFirstImageThumbnail) {
            wlPetDetailsbitmap = getBitmapImageFromURL(firstImagePath);
            wlPetImage.setImageBitmap(wlPetDetailsbitmap);
        } else if (view.getId() == R.id.wishlistPetDetailsSecondImageThumbnail) {
            wlPetDetailsbitmap = getBitmapImageFromURL(secondImagePath);
            wlPetImage.setImageBitmap(wlPetDetailsbitmap);
        } else if (view.getId() == R.id.wishlistPetDetailsThirdImageThumbnail) {
            wlPetDetailsbitmap = getBitmapImageFromURL(thirdImagePath);
            wlPetImage.setImageBitmap(wlPetDetailsbitmap);
        }else if (view.getId() == R.id.pWishListCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + alternateNo));
            startActivity(callIntent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = WishListPetListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(WishListPetListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = WishListPetListDetails.this.getPackageManager();
        ComponentName component = new ComponentName(WishListPetListDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}