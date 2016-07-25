package com.couragedigital.peto;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import android.widget.Toast;
import com.couragedigital.peto.Connectivity.*;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Listeners.PetFetchListScrollListener;
import com.couragedigital.peto.Adapter.PetListAdapter;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Shortner.GoogleURLShortener;
import com.couragedigital.peto.Singleton.FilterPetListInstance;
import com.couragedigital.peto.Singleton.ProfileURLInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.PetListItems;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;

public class PetList extends BaseActivity implements PetListAdapter.OnRecyclerPetListShareClickListener, View.OnClickListener {

    private static String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<PetListItems> petLists = new ArrayList<PetListItems>();

    public static RecyclerView recyclerView;
    public static TextView emptyTextView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout petListSwipeRefreshLayout;

    static String urlForFetch;

    private int current_page = 1;

    int filterState = 0;

    int FILTER_STATE_RESULT = 1;

    int requestState;

    public String email;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAdoptionAndPriceList = new ArrayList<String>();

    CallbackManager callbackManager;
    private LoginManager facebookLoginManager;
    List<String> facebookPermissionNeeds;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 2;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 3;
    private PetListItems petListItems;

    public static FloatingActionButton petListFormFAB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlist);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        facebookPermissionNeeds = Arrays.asList("publish_actions");

        facebookLoginManager = LoginManager.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.petList);
        emptyTextView = (TextView) findViewById(R.id.petListEmptyView);
        petListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petListSwipeRefreshLayout);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        url = url + "?method=showPetDetails&format=json&currentPage=" + current_page + "&email=" + email + "";

        requestState = 0;

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page) {

            @Override
            public void onLoadMore(int current_page) {
                if (filterState == 0) {
                    url = "";
                    url = URLInstance.getUrl();
                    url = url + "?method=showPetDetails&format=json&currentPage=" + current_page + "";
                    grabURL(url);
                }
            }
        });

        recyclerView.smoothScrollToPosition(0);

        adapter = new PetListAdapter(petLists, this);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets...");
        progressDialog.show();

        petListSwipeRefreshLayout.setOnRefreshListener(petListSwipeRefreshListener);
        petListSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3,
                R.color.refresh_progress_4);

        grabURL(url);

        petListFormFAB = (FloatingActionButton) findViewById(R.id.addPetFabButton);
        petListFormFAB.setOnClickListener(this);
    }

    public void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    @Override
    public void onRecyclerPetListShareClick(final PetListItems petListItems) {
        this.petListItems = petListItems;
        if(ActivityCompat.checkSelfPermission(PetList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        }
        else {
            if(ActivityCompat.checkSelfPermission(PetList.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            }
            else {
                shareList(petListItems);
            }
        }
    }

    public void shareList(final PetListItems petListItems) {

        String petListingTypeString = null;
        String price = null;
        if(petListItems.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
            price = "0 Rs.";
        }
        else {
            petListingTypeString = "TO SELL";
            price = petListItems.getListingType() + " Rs.";
        }

        String shareURL = ProfileURLInstance.getUrl();

        final String contentURL = shareURL + "?method=profileOfPet&breed="+petListItems.getPetBreed()+"&imageURL="+petListItems.getFirstImagePath()+"&gender="+petListItems.getPetGender()+"&year="+petListItems.getPetAgeInYear()+"&month="+petListItems.getPetAgeInMonth()+"&petType="+petListingTypeString+"&price="+price+"&mobileNo="+petListItems.getPetPostOwnerMobileNo()+"&description="+petListItems.getPetDescription();

        final String shortURL = GoogleURLShortener.shortner(contentURL);

        final Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("image/*");

        final List<ResolveInfo> activities = this.getPackageManager().queryIntentActivities(i, 0);

        List<String> appNames = new ArrayList<String>();
        for (ResolveInfo info : activities) {
            appNames.add(info.loadLabel(this.getPackageManager()).toString());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Share using...");
        builder.setItems(appNames.toArray(new CharSequence[appNames.size()]), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                ResolveInfo info = activities.get(item);
                if (info.activityInfo.packageName.equals("com.facebook.katana")) {
                    facebookLoginManager.logInWithPublishPermissions(PetList.this, facebookPermissionNeeds);
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent facebookContent = new ShareLinkContent.Builder()
                                .setContentTitle("Check out this pet!")
                                .setContentUrl(Uri.parse(shortURL))
                                .setImageUrl(Uri.parse(petListItems.getFirstImagePath()))
                                .build();
                        ShareDialog.show(PetList.this, facebookContent);
                    }
                }
                else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    URL url = null;
                    InputStream input = null;
                    try {
                        url = new URL(petListItems.getFirstImagePath());

                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        input = connection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Bitmap immutableBpm = BitmapFactory.decodeStream(input);
                    Bitmap mutableBitmap = immutableBpm.copy(Bitmap.Config.ARGB_8888, true);

                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "", null);
                    Uri imagePath = Uri.parse(path);

                    i.putExtra(Intent.EXTRA_TEXT, "Check out this Pet!" + shortURL);
                    i.putExtra(Intent.EXTRA_STREAM, imagePath);
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    i.setPackage(info.activityInfo.packageName);
                    startActivity(i);
                }
                /*else if (info.activityInfo.packageName.equals("com.twitter.android")) {
                    // Twitter was chosen
                }
                else if(info.activityInfo.packageName.equals("com.google.android.apps.plus")) {
                    // Google+ was chosen
                }*
                // start the selected activity
                /*i.setPackage(info.activityInfo.packageName);
                startActivity(i);*/
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClick(View view) {
        Intent gotoformupload = new Intent(view.getContext(), PetForm.class);
        view.getContext().startActivity(gotoformupload);
    }

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetFetchList petFetchList = new PetFetchList(PetList.this);
                petFetchList.petFetchList(petLists, adapter, urlForFetch, requestState, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    private SwipeRefreshLayout.OnRefreshListener petListSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            PetListItems petListItems = petLists.get(0);
            String date = petListItems.getPetPostDate();
            //date = date.replace(" ", "+");
            try {
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showPetSwipeRefreshList&format=json&date=" + URLEncoder.encode(date, "UTF-8") + "";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            new FetchRefreshListFromServer().execute(url);
        }
    };

    public class FetchRefreshListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetRefreshFetchList petRefreshFetchList = new PetRefreshFetchList(PetList.this);
                petRefreshFetchList.petRefreshFetchList(petLists, adapter, urlForFetch, petListSwipeRefreshLayout);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petlistmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            Intent filterClassIntent = new Intent(PetList.this, PetListFilter.class);
            startActivityForResult(filterClassIntent, FILTER_STATE_RESULT);
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle res = data.getExtras();
            filterState = res.getInt("Filter_State");
            if (filterState == 0) {
                petListSwipeRefreshLayout.setEnabled(true);
                petLists.clear();
                adapter.notifyDataSetChanged();
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showPetDetails&format=json&currentPage=" + current_page + "";
                recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page) {

                    @Override
                    public void onLoadMore(int current_page) {
                        url = "";
                        url = URLInstance.getUrl();
                        url = url + "?method=showPetDetails&format=json&currentPage=" + current_page + "";
                        grabURL(url);
                    }
                });

                petListSwipeRefreshLayout.setOnRefreshListener(petListSwipeRefreshListener);
                petListSwipeRefreshLayout.setColorSchemeResources(
                        R.color.refresh_progress_1,
                        R.color.refresh_progress_2,
                        R.color.refresh_progress_3,
                        R.color.refresh_progress_4);

                grabURL(url);
            } else if (filterState == 1) {
                petListSwipeRefreshLayout.setEnabled(false);
                FilterPetListInstance filterPetListInstance = new FilterPetListInstance();
                filterSelectedInstanceCategoryList = filterPetListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetListInstance.getFilterGenderListInstance();
                filterSelectedInstanceAdoptionAndPriceList = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
                new FetchFilterPetListFromServer().execute();

                recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page) {

                    @Override
                    public void onLoadMore(int current_page) {
                        FilterFetchPetList filterFetchPetList = new FilterFetchPetList(PetList.this);
                        filterFetchPetList.filterFetchPetList(petLists, adapter, email, current_page, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList, filterSelectedInstanceAdoptionAndPriceList);
                    }
                });
            }
        }
    }

    public class FetchFilterPetListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FilterFetchPetList filterFetchPetList = new FilterFetchPetList(PetList.this);
                filterFetchPetList.filterFetchPetList(petLists, adapter, email, current_page, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList, filterSelectedInstanceAdoptionAndPriceList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetList.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST);
        }
    }

    private void requestWriteStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetList.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        } else {
            // Camera permission has not been granted yet. Request it directly.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    WRITE_STORAGE_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if(requestCode == WRITE_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            } else {
                Toast.makeText(PetList.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareList(petListItems);
            } else {
                Toast.makeText(PetList.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    public void onBackPressed() {
        if (filterState == 1) {
            FilterPetListDeleteMemCacheObject filterPetListDeleteMemCacheObject = new FilterPetListDeleteMemCacheObject(PetList.this);
            filterPetListDeleteMemCacheObject.deletePetListFilterObject(email);
            FilterPetListInstance filterPetListInstance = new FilterPetListInstance();
            filterSelectedInstanceCategoryList = filterPetListInstance.getFilterCategoryListInstance();
            filterSelectedInstanceBreedList = filterPetListInstance.getFilterBreedListInstance();
            filterSelectedInstanceAgeList = filterPetListInstance.getFilterAgeListInstance();
            filterSelectedInstanceGenderList = filterPetListInstance.getFilterGenderListInstance();
            filterSelectedInstanceAdoptionAndPriceList = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
            filterSelectedInstanceCategoryList.clear();
            filterSelectedInstanceBreedList.clear();
            filterSelectedInstanceAgeList.clear();
            filterSelectedInstanceGenderList.clear();
            filterSelectedInstanceAdoptionAndPriceList.clear();
            filterPetListInstance.setFilterCategoryListInstance(filterSelectedInstanceCategoryList);
            filterPetListInstance.setFilterBreedListInstance(filterSelectedInstanceBreedList);
            filterPetListInstance.setFilterAgeListInstance(filterSelectedInstanceAgeList);
            filterPetListInstance.setFilterGenderListInstance(filterSelectedInstanceGenderList);
            filterPetListInstance.setFilterAdoptionAndPriceListInstance(filterSelectedInstanceAdoptionAndPriceList);
        }
        PetList.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetList.this.getPackageManager();
        ComponentName component = new ComponentName(PetList.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetList.this.getPackageManager();
        ComponentName component = new ComponentName(PetList.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}