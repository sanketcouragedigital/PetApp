package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.couragedigital.peto.Adapter.ClinicReviewsListAdapter;
import com.couragedigital.peto.Connectivity.ShowClinicFeedback;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Singleton.ClinicReviewInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.ClinicReviewsListItems;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PetClinicDetails extends AppCompatActivity implements View.OnClickListener {

    String image_path = "";
    String clinicaddress = "";
    String doctorname = "";
    String cliniccity = "";
    String clinicarea = "";
    String clinicnotes;


    ImageView clinicImage;
    TextView address;
    TextView clinicnotestxt;
    ImageButton callbutton;
    ImageButton locationMapButton;
    ImageButton rateAndReviewButton;
    String phoneno;

    Bitmap clinicDetailsbitmap;
    Toolbar clinicDetailsToolbar;
    CollapsingToolbarLayout clinicDetailsCollapsingToolbar;
    CoordinatorLayout clinicDetailsCoordinatorLayout;
    AppBarLayout clinicDetailsAppBarLayout;
    NestedScrollView clinicDetailsNestedScrollView;

    String clinicName;
    String clinicId;
    String email;
    String latitude;
    String longitude;

    ProgressDialog progressDialog = null;

    RecyclerView recyclerView;
    RecyclerView.Adapter reviewAdapter;
    LinearLayoutManager linearLayoutManager;

    LinearLayout clinicLinearLayout;
    RelativeLayout clinicFirstRelativeLayout;
    RelativeLayout clinicSecondRelativeLayout;
    TextView reviewLabelTextView;

    public List<ClinicReviewsListItems> clinicReviewsListItemsArrayList = new ArrayList<ClinicReviewsListItems>();
    String url = URLInstance.getUrl();
    private int current_page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.clinic_details_new);

        clinicDetailsCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.clinicDetailsCollapsingToolbar);
        clinicDetailsCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.clinicDetailsCoordinatorLayout);
        clinicDetailsAppBarLayout = (AppBarLayout) findViewById(R.id.petClinicDetailsAppBar);
        clinicDetailsNestedScrollView = (NestedScrollView) findViewById(R.id.petClinicDetailsNestedScrollView);
        recyclerView = (RecyclerView) findViewById(R.id.clinicRateNReview);
        clinicImage = (ImageView) findViewById(R.id.petClicnicHeaderImage);
        address = (TextView) findViewById(R.id.petClinicAddress);
        clinicnotestxt = (TextView) findViewById(R.id.petClinicNotes);
        callbutton = (ImageButton) findViewById(R.id.clinicDetailCallButton);
        locationMapButton = (ImageButton) findViewById(R.id.clinicDetailsMapButton);
        rateAndReviewButton= (ImageButton) findViewById(R.id.clinicDetailsRateNReviewButton);
        clinicLinearLayout = (LinearLayout) findViewById(R.id.containerLayout);
        clinicFirstRelativeLayout = (RelativeLayout) findViewById(R.id.clinicFirstRelativeLayout);
        clinicSecondRelativeLayout = (RelativeLayout) findViewById(R.id.addresslayout);
        reviewLabelTextView = (TextView) findViewById(R.id.reviewsLabel);

      // recyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
        //                .color(R.color.list_internal_divider).build());

        reviewAdapter = new ClinicReviewsListAdapter(clinicReviewsListItemsArrayList);
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(reviewAdapter);
        //new FetchReviewsList(adapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, clinicReviewsListItemsArrayList);
//        LinearLayout.LayoutParams params = new
//                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                ViewGroup.LayoutParams.WRAP_CONTENT);
//        // calculate height of RecyclerView based on child count
//        int i = adapter.getItemCount();
//        params.height = 400 * i;
//        // set height of RecyclerView
//        recyclerView.setLayoutParams(params);

        Intent intent = getIntent();
        if (null != intent) {
            clinicId = intent.getStringExtra("CLINIC_ID");
            clinicName = intent.getStringExtra("CLINIC_NAME");
            image_path = intent.getStringExtra("CLINIC_IMAGE");
            clinicaddress = intent.getStringExtra("CLINIC_ADDRESS");
            doctorname = intent.getStringExtra("DOCTOR_NAME");
            clinicnotes = intent.getStringExtra("CLINIC_NOTES");
            phoneno = intent.getStringExtra("DOCTOR_CONTACT");
            latitude = intent.getStringExtra("LATITUDE");
            longitude = intent.getStringExtra("LONGITUDE");
        }

        clinicDetailsToolbar = (Toolbar) findViewById(R.id.petClinicDetailsToolbar);
        setSupportActionBar(clinicDetailsToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        clinicDetailsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        new FetchImageFromServer().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, image_path);
        address.setText(clinicaddress);
        if(clinicnotes.equals("null") || clinicnotes.equals("")) {
            clinicnotestxt.setText("Unknown");
        }
        else {
            clinicnotestxt.setText(clinicnotes);
        }

        if(doctorname.equals("null") || doctorname.equals("")) {
            clinicDetailsCollapsingToolbar.setTitle(clinicName);
        }
        else {
            clinicDetailsCollapsingToolbar.setTitle(doctorname);
        }

        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) clinicDetailsAppBarLayout.getLayoutParams();
        layoutParams.height = getResources().getDisplayMetrics().widthPixels;

        clinicDetailsAppBarLayout.post(new Runnable() {
            @Override
            public void run() {
                int heightPx = getResources().getDisplayMetrics().heightPixels / 4;
                setAppBarOffset(heightPx);
            }
        });

        clinicLinearLayout.post(new Runnable() {
            @Override
            public void run() {
                reviewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        super.onChanged();
                        if (reviewAdapter.getItemCount() != 0) {
                            ClinicReviewInstance clinicReviewInstance = new ClinicReviewInstance();
                            Integer clinicFirstRelativeLayoutHeight = clinicFirstRelativeLayout.getHeight();
                            Integer clinicSecondRelativeLayoutHeight = clinicSecondRelativeLayout.getHeight();
                            Integer clinicReviewTextViewHeight = reviewLabelTextView.getHeight();
                            if(clinicReviewInstance.getRelativeLayoutHeightInstance() != 0) {
                                Integer relativeLayoutOfReview = clinicReviewInstance.getRelativeLayoutHeightInstance();
                                Integer totalHeightOfRelativeLayout = relativeLayoutOfReview * reviewAdapter.getItemCount();
                                clinicLinearLayout.setMinimumHeight(clinicFirstRelativeLayoutHeight + clinicSecondRelativeLayoutHeight + clinicReviewTextViewHeight + totalHeightOfRelativeLayout + 400);
                            }
                            else {
                                clinicLinearLayout.setMinimumHeight(clinicFirstRelativeLayoutHeight + clinicSecondRelativeLayoutHeight + clinicReviewTextViewHeight + 400);
                            }
                        }
                    }
                });
            }
        });

        callbutton.setOnClickListener(this);
        rateAndReviewButton.setOnClickListener(this);
        locationMapButton.setOnClickListener(this);

        url = url + "?method=showClinicReviews&format=json&currentPage=" + current_page + "&clinicId=" + clinicId + "";
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
            Glide.with(clinicImage.getContext()).load(url).centerCrop().crossFade().into(clinicImage);
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
                ShowClinicFeedback showClinicFeedback = new ShowClinicFeedback(PetClinicDetails.this);
                showClinicFeedback.showClinicReviews(clinicReviewsListItemsArrayList, reviewAdapter, urlForFetch);
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
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) clinicDetailsAppBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        behavior.onNestedPreScroll(clinicDetailsCoordinatorLayout, clinicDetailsAppBarLayout, null, 0, i, new int[]{0, 0});
    }

    @Override
    public void onBackPressed() {
        PetClinicDetails.this.finish();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.clinicDetailCallButton) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneno));
            startActivity(callIntent);
        }else if (view.getId() == R.id.clinicDetailsRateNReviewButton) {
           Intent gotorateAndReviewPage = new Intent(PetClinicDetails.this,ClinicRateNReview.class);
            gotorateAndReviewPage.putExtra("selectedClinicId",clinicId);
            gotorateAndReviewPage.putExtra("selectedClinicName",clinicName);
            startActivity(gotorateAndReviewPage);
        } else if (view.getId() == R.id.clinicDetailsMapButton) {
            Intent gotoClinicMap = new Intent(PetClinicDetails.this,ClinicMap.class);
            gotoClinicMap.putExtra("selectedClinicId",clinicId);
            gotoClinicMap.putExtra("selectedClinicName",clinicName);
            gotoClinicMap.putExtra("selectedClinicLat",latitude);
            gotoClinicMap.putExtra("selectedClinicLong",longitude);
            startActivity(gotoClinicMap);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetClinicDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetClinicDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetClinicDetails.this.getPackageManager();
        ComponentName component = new ComponentName(PetClinicDetails.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}


