package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.content.Intent;
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
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TabFragmentShelterDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String shelteraddress = "";


    ImageView shelterImage;

    TextView address;
    String sheltername;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap shelterDetailsbitmap;
    Toolbar shelterDetailsToolbar;
    CollapsingToolbarLayout shelterDetailsCollapsingToolbar;
    CoordinatorLayout shelterDetailsCoordinatorLayout;
    AppBarLayout shelterDetailsAppBaLayoutr;
    NestedScrollView shelterDetailsNestedScrollView;

    LinearLayout petShelterDetailsLinearLayout;
    RelativeLayout petShelterDetailsFirstRelativeLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_shelter_details);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("SHELTER_IMAGE");
            sheltername = intent.getStringExtra("SHELTER_NAME");
            shelteraddress = intent.getStringExtra("SHELTER_ADDRESS");
            email = intent.getStringExtra("SHELTER_EMAIL");
            phoneno = intent.getStringExtra("SHELTER_CONTACT");
        }

        shelterDetailsToolbar = (Toolbar) findViewById(R.id.petShelterDetailsToolbar);
        setSupportActionBar(shelterDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shelterDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        shelterDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.shelterDetailsCollapsingToolbar);

        shelterDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.shelterDetailsCoordinatorLayout);

        shelterDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petShelterDetailsAppBar);

        shelterDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petShelterDetailsNestedScrollView);
        petShelterDetailsLinearLayout = (LinearLayout) findViewById(R.id.petShelterDetailsLinearLayout);
        petShelterDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petShelterDetailsFirstRelativeLayout);

        shelterImage = (ImageView) findViewById(R.id.petShelterHeaderImage);
        address = (TextView) findViewById(R.id.petShelterAddress);
        callbutton = (Button) findViewById(R.id.shelterDetailCallButton);
        emailbutton = (Button) findViewById(R.id.shelterDetailsEmailButton);


        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(shelteraddress);
        shelterDetailsCollapsingToolbar.setTitle(sheltername);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) shelterDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        shelterDetailsAppBaLayoutr.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petShelterDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petShelterDetailsFirstRelativeLayout.getHeight();

                petShelterDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + 200);
            }
        });

        callbutton.setOnClickListener(this);
        emailbutton.setOnClickListener(this);
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
            Glide.with(shelterImage.getContext()).load(url).centerCrop().crossFade().into(shelterImage);
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
        shelterDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return shelterDetailsbitmap;
    }

    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) shelterDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(shelterDetailsCoordinatorLayout, shelterDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentShelterDetails.this.finish();
    }


    public void onClick(View view) {

        if (view.getId() == R.id.shelterDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneno));
            startActivity(callIntent);
        }else if(view.getId() == R.id.shelterDetailsEmailButton){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }


}