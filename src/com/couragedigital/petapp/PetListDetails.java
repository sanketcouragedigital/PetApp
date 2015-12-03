package com.couragedigital.petapp;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.*;
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

public class PetListDetails extends AppCompatActivity {
    String image_path = "";
    String breed = "";
    String listingType = "";
    String age = "";
    String gender = "";
    String description = "";

    ImageView petImage;
    TextView petBreed;
    TextView petListingType;
    TextView petAge;
    TextView petGender;
    TextView petDescription;

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
        }

        petDetailstoolbar = (Toolbar) findViewById(R.id.petDetailsToolbar);
        setSupportActionBar(petDetailstoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        petDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.petListingTypeInPetDetailsCollapsingToolbar);

        petDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.petDetailsCoordinatorLayout);

        petDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petDetailsAppBar);

        petDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petDetailsNestedScrollView);

        petImage = (ImageView) findViewById(R.id.petHeaderImage);
        petBreed = (TextView) findViewById(R.id.petBreedInPetDetails);
        //petListingType = (TextView) findViewById(R.id.petListingTypeInPetDetails);
        petAge = (TextView) findViewById(R.id.petAgeInPetDetails);
        petGender = (TextView) findViewById(R.id.petGenderInPetDetails);
        petDescription = (TextView) findViewById(R.id.petDescriptionInPetDetails);

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
        petDetailsCollapsingToolbar.setTitle(listingType);
        petBreed.setText(breed);
        String ageOfPet = "<b>Pet Age :- </b>" + age;
        petAge.setText(Html.fromHtml(ageOfPet));
        String genderOfPet = "<b>Gender :- </b>" + gender;
        petGender.setText(Html.fromHtml(genderOfPet));
        petDescription.setText(description);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        petDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        /*CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        lp.height = petDetailsbitmap.getHeight();
        petDetailsAppBarLayout.setLayoutParams(lp);
        petImage.setImageBitmap(petDetailsbitmap);*/

        /*DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);*/

        /*PetProfileScrollBehavior petProfileScrollBehavior = new PetProfileScrollBehavior(getApplicationContext(), petDetailsbitmap);
        petProfileScrollBehavior.layoutDependsOn(petDetailsCoordinatorLayout, petDetailsAppBarLayout, petDetailsNestedScrollView);
        petProfileScrollBehavior.onNestedPreScroll(petDetailsCoordinatorLayout, petDetailsAppBarLayout, null, 0, petDetailsbitmap.getHeight() - pxToDp(512), new int[2]);
        petProfileScrollBehavior.onStartNestedScroll(petDetailsCoordinatorLayout, petDetailsAppBarLayout, petImage, petDetailsNestedScrollView, View.SCROLL_AXIS_VERTICAL);
        petProfileScrollBehavior.onNestedScroll(petDetailsCoordinatorLayout, petDetailsAppBarLayout, petDetailsNestedScrollView, metrics.widthPixels, petDetailsAppBarLayout.getHeight(), 0, petDetailsbitmap.getHeight() - petDetailsAppBarLayout.getHeight());
        petProfileScrollBehavior.onMeasureChild(petDetailsCoordinatorLayout, petDetailsAppBarLayout, metrics.widthPixels, metrics.widthPixels, petDetailsbitmap.getHeight(), dpToPx(512));*/

        //petDetailsAppBarLayout.setExpanded(true);
        /*CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = new AppBarLayout.Behavior();
        behavior.setTopAndBottomOffset(0);
        behavior.onNestedPreScroll(petDetailsCoordinatorLayout, petDetailsAppBarLayout, null, 0, petDetailsbitmap.getHeight() - dpToPx(512), new int[2]);
        params.setBehavior(behavior);
        petDetailsAppBarLayout.setLayoutParams(params);*/

        /*petDetailsAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                if(i > 0) {
                    appBarLayout.setExpanded(true);
                    CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) petDetailsAppBarLayout.getLayoutParams();
                    lp.height = petDetailsbitmap.getHeight();
                    petDetailsAppBarLayout.setLayoutParams(lp);
                }
            }
        });*/

        /*Palette.from(petDetailsbitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                mutedColor = palette.getMutedColor(R.color.colorPrimary);
                petDetailsCollapsingToolbar.setContentScrimColor(mutedColor);
            }
        });*/
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
}