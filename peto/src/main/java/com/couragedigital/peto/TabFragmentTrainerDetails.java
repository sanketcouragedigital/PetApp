package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.Adapter.ServiceListAdapter;
import com.couragedigital.peto.Adapter.TrainerListAdapter;
import com.couragedigital.peto.Connectivity.ShowServiceFeedback;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Singleton.ReviewInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.ReviewsListItems;
import com.couragedigital.peto.model.TrainerListItem;
//import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TabFragmentTrainerDetails extends AppCompatActivity implements View.OnClickListener {

    String image_path = "";
    String traineraddress = "";
    String trainerdescription = "";
    String trainercity = "";
    String trainerarea = "";
    String trainernotes;
    String ServiceType="Trainer";
    String trainerName;
    String trainerId;
    String email;
    String latitude;
    String longitude;

    ImageView trainerImage;
    TextView address;
    TextView description;

    TextView trainernotestxt;
    ImageButton callbutton;
    ImageButton locationMapButton;
    ImageButton rateAndReviewButton;
    String phoneno;

    Bitmap trainerDetailsbitmap;
    Toolbar trainerDetailsToolbar;
    CollapsingToolbarLayout trainerDetailsCollapsingToolbar;
    CoordinatorLayout trainerDetailsCoordinatorLayout;
    AppBarLayout trainerDetailsAppBarLayout;
    NestedScrollView trainerDetailsNestedScrollView;

    String trainerEmail;
    String city;
    String area;

    ProgressDialog progressDialog = null;

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    LinearLayout trainerLinearLayout;
    RelativeLayout trainerFirstRelativeLayout;
    RelativeLayout trainerSecondRelativeLayout;
    TextView reviewLabelTextView;

    public List<ReviewsListItems> serviceReviewsListItemsArrayList = new ArrayList<ReviewsListItems>();
    String url = URLInstance.getUrl();
    private int current_page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabfragment_trainer_details);

        trainerDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.trainerDetailsCollapsingToolbar);
        trainerDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.trainerDetailsCoordinatorLayout);
        trainerDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.pettrainerDetailsAppBar);
        trainerDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.pettrainerDetailsNestedScrollView);
        recyclerView = (RecyclerView) findViewById(R.id.trainerRateNReview);
        trainerImage = (ImageView) findViewById(R.id.pettrainerHeaderImage);
        address = (TextView) findViewById(R.id.pettrainerAddress);
        description = (TextView) findViewById(R.id.pettrainerDescription);
        trainernotestxt = (TextView) findViewById(R.id.pettrainerNotes);
        callbutton = (ImageButton) findViewById(R.id.trainerDetailCallButton);
        locationMapButton = (ImageButton) findViewById(R.id.trainerDetailsMapButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.trainerDetailsRateNReviewButton);
        trainerLinearLayout = (LinearLayout) findViewById(R.id.containerLayout);
        trainerFirstRelativeLayout = (RelativeLayout) findViewById(R.id.trainerFirstRelativeLayout);
        trainerSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);
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
            trainerId = intent.getStringExtra("TRAINER_ID");
            trainerName = intent.getStringExtra("TRAINER_NAME");
            image_path = intent.getStringExtra("TRAINER_IMAGE");
            traineraddress = intent.getStringExtra("TRAINER_ADDRESS");
            trainerdescription = intent.getStringExtra("TRAINER_DESCRIPTION");
            trainernotes = intent.getStringExtra("TRAINER_NOTES");
            phoneno = intent.getStringExtra("TRAINER_CONTACT");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
            area = intent.getStringExtra("TRAINER_AREA");
            city = intent.getStringExtra("TRAINER_CITY");
            trainerEmail = intent.getStringExtra("TRAINER_EMAIL");
        }

        trainerDetailsToolbar = (Toolbar) findViewById(R.id.pettrainerDetailsToolbar);
        setSupportActionBar(trainerDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        trainerDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        trainerDetailsCollapsingToolbar.setTitle(trainerName);

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(traineraddress);
        description.setText(trainerdescription);


        if(trainernotes.equals("null") || trainernotes.equals("")) {
            trainernotestxt.setText("Not Mentioned");
        }
        else {
            trainernotestxt.setText(trainernotes);
        }


        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) trainerDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        trainerDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        trainerLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                reviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (reviewAdapter.getItemCount() != 0) {
                            ReviewInstance reviewInstance = new ReviewInstance();
                            Integer trainerFirstRelativeLayoutHeight = trainerFirstRelativeLayout.getHeight();
                            Integer trainerSecondRelativeLayoutHeight = trainerSecondRelativeLayout.getHeight();
                            Integer trainerReviewTextViewHeight = reviewLabelTextView.getHeight();
                            if(reviewInstance.getRelativeLayoutHeightInstance() != 0) {
                                Integer relativeLayoutOfReview = reviewInstance.getRelativeLayoutHeightInstance();
                                Integer totalHeightOfRelativeLayout = relativeLayoutOfReview * reviewAdapter.getItemCount();
                                trainerLinearLayout.setMinimumHeight(trainerFirstRelativeLayoutHeight + trainerSecondRelativeLayoutHeight + trainerReviewTextViewHeight + totalHeightOfRelativeLayout + 400);
                            }
                            else {
                                trainerLinearLayout.setMinimumHeight(trainerFirstRelativeLayoutHeight + trainerSecondRelativeLayoutHeight + trainerReviewTextViewHeight + 400);
                            }
                        }
                    }
                });
            }
        });

        callbutton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);
        locationMapButton.setOnClickListener(this);

        url = url + "?method=showPetServiceReviews&format=json&currentPage=" + current_page + "&serviceListId=" + trainerId + "&serviceType=" + ServiceType + "";
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
            Glide.with(trainerImage.getContext()).load(url).centerCrop().crossFade().into(trainerImage);
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
                ShowServiceFeedback showServiceFeedback = new ShowServiceFeedback(TabFragmentTrainerDetails.this);
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
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) trainerDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(trainerDetailsCoordinatorLayout, trainerDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        TabFragmentTrainerDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.trainerDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        }else if (view.getId() == R.id.trainerDetailsRateNReviewButton) {

            Intent gotorateAndReviewPage = new Intent(TabFragmentTrainerDetails.this,ServiceRateNReview.class);
            gotorateAndReviewPage.putExtra("selectedId",trainerId);
            gotorateAndReviewPage.putExtra("selectedName",trainerName);
            gotorateAndReviewPage.putExtra("ServiceType",ServiceType);
            startActivity(gotorateAndReviewPage);
        } else if (view.getId() == R.id.trainerDetailsMapButton) {
            Intent gototrainerMap = new Intent(TabFragmentTrainerDetails.this,ClinicMap.class);
            gototrainerMap.putExtra("selectedtrainerId",trainerId);
            gototrainerMap.putExtra("selectedtrainerName",trainerName);
            gototrainerMap.putExtra("selectedLat",latitude);
            gototrainerMap.putExtra("selectedLong",longitude);
            startActivity(gototrainerMap);
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


