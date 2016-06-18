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

public class TabFragmentShelterDetails extends AppCompatActivity implements View.OnClickListener {

    String image_path = "";
    String shelteraddress = "";
    String shelterdescription = "";
    String sheltercity = "";
    String shelterarea = "";
    String shelternotes;
    String ServiceType="Shelter";

    ImageView shelterImage;
    TextView address;
    TextView shelternotestxt;
    TextView description;
    ImageButton callbutton;
    ImageButton locationMapButton;
    ImageButton rateAndReviewButton;
    String phoneno;

    Bitmap shelterDetailsbitmap;
    Toolbar shelterDetailsToolbar;
    CollapsingToolbarLayout shelterDetailsCollapsingToolbar;
    CoordinatorLayout shelterDetailsCoordinatorLayout;
    AppBarLayout shelterDetailsAppBarLayout;
    NestedScrollView shelterDetailsNestedScrollView;

    String shelterName;
    String shelterId;
    String email;
    String latitude;
    String longitude;

    String shelterEmail;
    String city;
    String area;

    ProgressDialog progressDialog = null;

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    LinearLayout shelterLinearLayout;
    RelativeLayout shelterFirstRelativeLayout;
    RelativeLayout shelterSecondRelativeLayout;
    TextView reviewLabelTextView;

    public List<ReviewsListItems> serviceReviewsListItemsArrayList = new ArrayList<ReviewsListItems>();
    String url = URLInstance.getUrl();
    private int current_page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_shelter_details);

        shelterDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.shelterDetailsCollapsingToolbar);
        shelterDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.shelterDetailsCoordinatorLayout);
        shelterDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petshelterDetailsAppBar);
        shelterDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petshelterDetailsNestedScrollView);
        recyclerView = (RecyclerView) findViewById(R.id.shelterRateNReview);
        shelterImage = (ImageView) findViewById(R.id.petshelterHeaderImage);
        address = (TextView) findViewById(R.id.petshelterAddress);
        shelternotestxt = (TextView) findViewById(R.id.petshelterNotes);
        description = (TextView) findViewById(R.id.petshelterDescription);
        callbutton = (ImageButton) findViewById(R.id.shelterDetailCallButton);
        locationMapButton = (ImageButton) findViewById(R.id.shelterDetailsMapButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.shelterDetailsRateNReviewButton);
        shelterLinearLayout = (LinearLayout) findViewById(R.id.containerLayout);
        shelterFirstRelativeLayout = (RelativeLayout) findViewById(R.id.shelterFirstRelativeLayout);
        shelterSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);
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
            shelterId = intent.getStringExtra("SHELTER_ID");
            shelterName = intent.getStringExtra("SHELTER_NAME");
            image_path = intent.getStringExtra("SHELTER_IMAGE");
            shelteraddress = intent.getStringExtra("SHELTER_ADDRESS");
            shelterdescription = intent.getStringExtra("SHELTER_DESCRIPTION");
            shelternotes = intent.getStringExtra("SHELTER_NOTES");
            phoneno = intent.getStringExtra("SHELTER_CONTACT");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
            area = intent.getStringExtra("SHELTER_AREA");
            city = intent.getStringExtra("SHELTER_CITY");
            shelterEmail = intent.getStringExtra("TRAINER_EMAIL");
        }

        shelterDetailsToolbar = (Toolbar) findViewById(R.id.petshelterDetailsToolbar);
        setSupportActionBar(shelterDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        shelterDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        shelterDetailsCollapsingToolbar.setTitle(shelterName);
        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(shelteraddress);
        description.setText(shelterdescription);
        if(shelternotes.equals("null") || shelternotes.equals("")) {
            shelternotestxt.setText("Not Mentioned");
        }
        else {
            shelternotestxt.setText(shelternotes);
        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) shelterDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        shelterDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        shelterLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                reviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (reviewAdapter.getItemCount() != 0) {
                            ReviewInstance reviewInstance = new ReviewInstance();
                            Integer shelterFirstRelativeLayoutHeight = shelterFirstRelativeLayout.getHeight();
                            Integer shelterSecondRelativeLayoutHeight = shelterSecondRelativeLayout.getHeight();
                            Integer shelterReviewTextViewHeight = reviewLabelTextView.getHeight();
                            if(reviewInstance.getRelativeLayoutHeightInstance() != 0) {
                                Integer relativeLayoutOfReview = reviewInstance.getRelativeLayoutHeightInstance();
                                Integer totalHeightOfRelativeLayout = relativeLayoutOfReview * reviewAdapter.getItemCount();
                                shelterLinearLayout.setMinimumHeight(shelterFirstRelativeLayoutHeight + shelterSecondRelativeLayoutHeight + shelterReviewTextViewHeight + totalHeightOfRelativeLayout + 400);
                            }
                            else {
                                shelterLinearLayout.setMinimumHeight(shelterFirstRelativeLayoutHeight + shelterSecondRelativeLayoutHeight + shelterReviewTextViewHeight + 400);
                            }
                        }
                    }
                });
            }
        });

        callbutton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);
        locationMapButton.setOnClickListener(this);

        url = url + "?method=showPetServiceReviews&format=json&currentPage=" + current_page + "&serviceListId=" + shelterId + "&serviceType=" + ServiceType + "";
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
            Glide.with(shelterImage.getContext()).load(url).centerCrop().crossFade().into(shelterImage);
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
                ShowServiceFeedback showServiceFeedback = new ShowServiceFeedback(TabFragmentShelterDetails.this);
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
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) shelterDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(shelterDetailsCoordinatorLayout, shelterDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentShelterDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.shelterDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        }else if (view.getId() == R.id.shelterDetailsRateNReviewButton) {

            Intent gotorateAndReviewPage = new Intent(TabFragmentShelterDetails.this,ServiceRateNReview.class);
            gotorateAndReviewPage.putExtra("selectedId",shelterId);
            gotorateAndReviewPage.putExtra("selectedName",shelterName);
            gotorateAndReviewPage.putExtra("ServiceType",ServiceType);
            startActivity(gotorateAndReviewPage);
        } else if (view.getId() == R.id.shelterDetailsMapButton) {
            Intent gotoshelterMap = new Intent(TabFragmentShelterDetails.this,ClinicMap.class);
            gotoshelterMap.putExtra("selectedId",shelterId);
            gotoshelterMap.putExtra("selectedName",shelterName);
            gotoshelterMap.putExtra("selectedLat",latitude);
            gotoshelterMap.putExtra("selectedLong",longitude);
            startActivity(gotoshelterMap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = TabFragmentShelterDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentShelterDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = TabFragmentShelterDetails.this.getPackageManager();
        ComponentName component = new ComponentName(TabFragmentShelterDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}