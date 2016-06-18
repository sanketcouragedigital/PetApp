package com.couragedigital.peto;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.Adapter.ServiceListAdapter;
import com.couragedigital.peto.Connectivity.ShowServiceFeedback;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Singleton.ReviewInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.ReviewsListItems;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentGroomerDetails extends AppCompatActivity implements View.OnClickListener {

    String image_path = "";
    String groomeraddress = "";
    String groomerdescription = "";
    String groomercity = "";
    String groomerarea = "";
    String groomernotes;
    String ServiceType="Groomer";

    ImageView groomerImage;
    TextView address;
    TextView groomernotestxt;
    TextView description;
    ImageButton callbutton;
    ImageButton locationMapButton;
    ImageButton rateAndReviewButton;
    String phoneno;

    Bitmap groomerDetailsbitmap;
    Toolbar groomerDetailsToolbar;
    CollapsingToolbarLayout groomerDetailsCollapsingToolbar;
    CoordinatorLayout groomerDetailsCoordinatorLayout;
    AppBarLayout groomerDetailsAppBarLayout;
    NestedScrollView groomerDetailsNestedScrollView;

    String groomerName;
    String groomerId;
    String email;
    String latitude;
    String longitude;

    String groomerEmail;
    String city;
    String area;

    ProgressDialog progressDialog = null;

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    LinearLayout groomerLinearLayout;
    RelativeLayout groomerFirstRelativeLayout;
    RelativeLayout groomerSecondRelativeLayout;
    TextView reviewLabelTextView;

    public List<ReviewsListItems> serviceReviewsListItemsArrayList = new ArrayList<ReviewsListItems>();
    String url = URLInstance.getUrl();
    private int current_page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_groomer_details);

        groomerDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.groomerDetailsCollapsingToolbar);
        groomerDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.groomerDetailsCoordinatorLayout);
        groomerDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petgroomerDetailsAppBar);
        groomerDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petgroomerDetailsNestedScrollView);
        recyclerView = (RecyclerView) findViewById(R.id.groomerRateNReview);
        groomerImage = (ImageView) findViewById(R.id.petgroomerHeaderImage);
        address = (TextView) findViewById(R.id.petgroomerAddress);
        groomernotestxt = (TextView) findViewById(R.id.petgroomerNotes);
        description = (TextView) findViewById(R.id.petgroomerDescription);
        callbutton = (ImageButton) findViewById(R.id.groomerDetailCallButton);
        locationMapButton = (ImageButton) findViewById(R.id.groomerDetailsMapButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.groomerDetailsRateNReviewButton);
        groomerLinearLayout = (LinearLayout) findViewById(R.id.containerLayout);
        groomerFirstRelativeLayout = (RelativeLayout) findViewById(R.id.groomerFirstRelativeLayout);
        groomerSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);
        reviewLabelTextView = (TextView) findViewById(R.id.reviewsLabel);

        // recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
        //                .color(R.color.list_internal_divider).build());

        reviewAdapter = new ServiceListAdapter(serviceReviewsListItemsArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);

        Intent intent = getIntent();
        if (null != intent) {
            groomerId = intent.getStringExtra("GROOMER_ID");
            groomerName = intent.getStringExtra("GROOMER_NAME");
            image_path = intent.getStringExtra("GROOMER_IMAGE");
            groomeraddress = intent.getStringExtra("GROOMER_ADDRESS");
            groomerdescription = intent.getStringExtra("GROOMER_DESCRIPTION");
            groomernotes = intent.getStringExtra("GROOMER_NOTES");
            phoneno = intent.getStringExtra("GROOMER_CONTACT");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
            area = intent.getStringExtra("GROOMER_AREA");
            city = intent.getStringExtra("GROOMER_CITY");
            groomerEmail = intent.getStringExtra("GROOMER_EMAIL");
        }

        groomerDetailsToolbar = (Toolbar) findViewById(R.id.petgroomerDetailsToolbar);
        setSupportActionBar(groomerDetailsToolbar);
        description.setText(groomerdescription);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        groomerDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        groomerDetailsCollapsingToolbar.setTitle(groomerName);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(groomeraddress);
        if(groomernotes.equals("null") || groomernotes.equals("")) {
            groomernotestxt.setText("Not Mentioned");
        }
        else {
            groomernotestxt.setText(groomernotes);
        }


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) groomerDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        groomerDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        groomerLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                reviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (reviewAdapter.getItemCount() != 0) {
                            ReviewInstance reviewInstance = new ReviewInstance();
                            Integer groomerFirstRelativeLayoutHeight = groomerFirstRelativeLayout.getHeight();
                            Integer groomerSecondRelativeLayoutHeight = groomerSecondRelativeLayout.getHeight();
                            Integer groomerReviewTextViewHeight = reviewLabelTextView.getHeight();
                            if(reviewInstance.getRelativeLayoutHeightInstance() != 0) {
                                Integer relativeLayoutOfReview = reviewInstance.getRelativeLayoutHeightInstance();
                                Integer totalHeightOfRelativeLayout = relativeLayoutOfReview * reviewAdapter.getItemCount();
                                groomerLinearLayout.setMinimumHeight(groomerFirstRelativeLayoutHeight + groomerSecondRelativeLayoutHeight + groomerReviewTextViewHeight + totalHeightOfRelativeLayout + 400);
                            }
                            else {
                                groomerLinearLayout.setMinimumHeight(groomerFirstRelativeLayoutHeight + groomerSecondRelativeLayoutHeight + groomerReviewTextViewHeight + 400);
                            }
                        }
                    }
                });
            }
        });

        callbutton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);
        locationMapButton.setOnClickListener(this);

        url = url + "?method=showPetServiceReviews&format=json&currentPage=" + current_page + "&serviceListId=" + groomerId + "&serviceType=" + ServiceType + "";
        grabURL(url);
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
            Glide.with(groomerImage.getContext()).load(url).centerCrop().crossFade().into(groomerImage);
        }
    }

    public void grabURL(String url) {
        new FetchReviewList().execute(url);
    }

    public class FetchReviewList extends AsyncTask<String, String, String> {
        private String urlForFetch;

        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                ShowServiceFeedback showServiceFeedback = new ShowServiceFeedback(TabFragmentGroomerDetails.this);
                showServiceFeedback.showServiceReviews(serviceReviewsListItemsArrayList, reviewAdapter, urlForFetch, ServiceType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void copy(InputStream in, BufferedOutputStream out) throws IOException {
        byte[] b = new byte[4 * 1024];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }

    private void setAppBarOffset(int i) {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) groomerDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(groomerDetailsCoordinatorLayout, groomerDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentGroomerDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.groomerDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        }else if (view.getId() == R.id.groomerDetailsRateNReviewButton) {

            Intent gotorateAndReviewPage = new Intent(TabFragmentGroomerDetails.this,ServiceRateNReview.class);
            gotorateAndReviewPage.putExtra("selectedId",groomerId);
            gotorateAndReviewPage.putExtra("selectedName",groomerName);
            gotorateAndReviewPage.putExtra("ServiceType",ServiceType);
            startActivity(gotorateAndReviewPage);
        } else if (view.getId() == R.id.groomerDetailsMapButton) {
            Intent gotogroomerMap = new Intent(TabFragmentGroomerDetails.this,ClinicMap.class);
            gotogroomerMap.putExtra("selectedId",groomerId);
            gotogroomerMap.putExtra("selectedName",groomerName);
            gotogroomerMap.putExtra("selectedLat",latitude);
            gotogroomerMap.putExtra("selectedLong",longitude);
            startActivity(gotogroomerMap);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = TabFragmentGroomerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentGroomerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = TabFragmentGroomerDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentGroomerDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}