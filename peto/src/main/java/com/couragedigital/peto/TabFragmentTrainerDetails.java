package com.couragedigital.peto;

import android.annotation.TargetApi;
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
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class TabFragmentTrainerDetails extends AppCompatActivity implements View.OnClickListener {
    String image_path = "";
    String traineraddress = "";


    ImageView trainerImage;

    TextView address;
    String trainername;
    Button callbutton;
    Button emailbutton;
    String email;
    String phoneno;

    Bitmap trainerDetailsbitmap;
    Toolbar trainerDetailsToolbar;
    CollapsingToolbarLayout trainerDetailsCollapsingToolbar;
    CoordinatorLayout trainerDetailsCoordinatorLayout;
    AppBarLayout trainerDetailsAppBaLayoutr;
    NestedScrollView trainerDetailsNestedScrollView;

    LinearLayout petTrainerDetailsLinearLayout;
    RelativeLayout petTrainerDetailsFirstRelativeLayout;

    @TargetApi(23)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_trainer_details);

        Intent intent = getIntent();
        if (null != intent) {
            image_path = intent.getStringExtra("TRAINER_IMAGE");
            trainername = intent.getStringExtra("TRAINER_NAME");
            traineraddress = intent.getStringExtra("TRAINER_ADDRESS");
            email = intent.getStringExtra("TRAINER_EMAIL");
            phoneno = intent.getStringExtra("TRAINER_CONTACT");
        }

        trainerDetailsToolbar = (Toolbar) findViewById(R.id.petTrainerDetailsToolbar);
        setSupportActionBar(trainerDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trainerDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        trainerDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.trainerDetailsCollapsingToolbar);

        trainerDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.trainerDetailsCoordinatorLayout);

        trainerDetailsAppBaLayoutr = (AppBarLayout) findViewById(R.id.petTrainerDetailsAppBar);

        trainerDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petTrainerDetailsNestedScrollView);
        petTrainerDetailsLinearLayout = (LinearLayout) findViewById(R.id.petTrainerDetailsLinearLayout);
        petTrainerDetailsFirstRelativeLayout = (RelativeLayout) findViewById(R.id.petTrainerDetailsFirstRelativeLayout);

        trainerImage = (ImageView) findViewById(R.id.petTrainerHeaderImage);
        address = (TextView) findViewById(R.id.petTrainerAddress);
        callbutton = (Button) findViewById(R.id.trainerDetailCallButton);
        emailbutton = (Button) findViewById(R.id.trainerDetailsEmailButton);


        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(traineraddress);
        trainerDetailsCollapsingToolbar.setTitle(trainername);

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) trainerDetailsAppBaLayoutr.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        trainerDetailsAppBaLayoutr.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        petTrainerDetailsLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                Integer heightOfFirstRelativeLayout = petTrainerDetailsFirstRelativeLayout.getHeight();

                petTrainerDetailsLinearLayout.setMinimumHeight(heightOfFirstRelativeLayout + 200);
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
            Glide.with(trainerImage.getContext()).load(url).centerCrop().crossFade().into(trainerImage);
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
        trainerDetailsbitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return trainerDetailsbitmap;
    }

    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) trainerDetailsAppBaLayoutr.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(trainerDetailsCoordinatorLayout, trainerDetailsAppBaLayoutr, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentTrainerDetails.this.finish();
    }


    public void onClick(View view) {

        if (view.getId() == R.id.trainerDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:"+phoneno));
            startActivity(callIntent);
        }else if(view.getId() == R.id.trainerDetailsEmailButton){
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto",email, null));
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = TabFragmentTrainerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentTrainerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = TabFragmentTrainerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentTrainerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}