package com.couragedigital.peto;

import android.Manifest;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.couragedigital.peto.Adapter.PetMateListAdapter;
import com.couragedigital.peto.Connectivity.FilterFetchPetMateList;
import com.couragedigital.peto.Connectivity.FilterPetMateListDeleteMemCacheObject;
import com.couragedigital.peto.Connectivity.PetMateFetchList;
import com.couragedigital.peto.Connectivity.PetMateRefreshFetchList;
import com.couragedigital.peto.InternetConnectivity.NetworkChangeReceiver;
import com.couragedigital.peto.Listeners.PetMateFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Shortner.GoogleURLShortener;
import com.couragedigital.peto.Singleton.FilterPetMateListInstance;
import com.couragedigital.peto.Singleton.ProfileURLInstance;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.PetListItems;
import com.couragedigital.peto.model.PetMateListItems;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

public class PetMateList extends BaseActivity implements PetMateListAdapter.OnRecyclerPetMateListShareClickListener {

    private static final String TAG = PetMateList.class.getSimpleName();

    // http://c/dev/api/petappapi.php?method=showPetDetails&format=json
    // "http://storage.couragedigital.com/dev/api/petappapi.php"
    private static String url;
    private ProgressDialog progressDialog;
    public List<PetMateListItems> petMateLists = new ArrayList<PetMateListItems>();
    /*private ListView petlistView;
    public PetListAdapter Adapter;*/

    public List<PetMateListItems> petMateListForFilter = new ArrayList<PetMateListItems>();
    public PetMateListAdapter adapterForFilter;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout petMateListSwipeRefreshLayout;

    public List<PetMateListItems> originalpetLists = new ArrayList<PetMateListItems>();

    static String urlForFetch;

    private int current_page = 1;

    public String email;

    int filterState = 0;

    int FILTER_STATE_RESULT = 1;

    int requestState;

    public String petListId;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();

    CallbackManager callbackManager;
    private LoginManager facebookLoginManager;
    List<String> facebookPermissionNeeds;

    private static final int READ_STORAGE_PERMISSION_REQUEST = 2;
    private static final int WRITE_STORAGE_PERMISSION_REQUEST = 3;
    private PetMateListItems petMateListItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmatelist);

        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        facebookPermissionNeeds = Arrays.asList("publish_actions");

        facebookLoginManager = LoginManager.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.petMateList);
        petMateListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petMateListSwipeRefreshLayout);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);
        url = "";
        url = URLInstance.getUrl();
        url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";

        requestState = 0;

        recyclerView.addOnScrollListener(new PetMateFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                if(filterState == 0) {
                    url = "";
                    url = URLInstance.getUrl();
                    url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";
                    grabURL(url);
                }
            }
        });

        recyclerView.smoothScrollToPosition(0);

        //recyclerView.fling(0,1);

        adapter = new PetMateListAdapter(petMateLists, this);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets...");
        progressDialog.show();

        petMateListSwipeRefreshLayout.setOnRefreshListener(petMateListSwipeRefreshListener);
        petMateListSwipeRefreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3,
                R.color.refresh_progress_4);

        grabURL(url);
    }

    public void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    @Override
    public void onRecyclerPetMateListShareClick(final PetMateListItems petMateListItems) {
        this.petMateListItems = petMateListItems;
        if(ActivityCompat.checkSelfPermission(PetMateList.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestWriteStoragePermission();
        }
        else {
            if(ActivityCompat.checkSelfPermission(PetMateList.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestReadStoragePermission();
            }
            else {
                shareList(petMateListItems);
            }
        }
    }

    public void shareList(final PetMateListItems petMateListItems) {

        String petListingTypeString = "";
        String price = "";

        String shareURL = ProfileURLInstance.getUrl();

        final String contentURL = shareURL + "?method=profileOfPetMate&breed="+petMateListItems.getPetMateBreed()+"&imageURL="+petMateListItems.getFirstImagePath()+"&gender="+petMateListItems.getPetMateGender()+"&year="+petMateListItems.getPetMateAgeInYear()+"&month="+petMateListItems.getPetMateAgeInMonth()+"&petType="+petListingTypeString+"&price="+price+"&mobileNo="+petMateListItems.getPetMatePostOwnerEmail()+"&description="+petMateListItems.getPetMateDescription();

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
                    facebookLoginManager.logInWithPublishPermissions(PetMateList.this, facebookPermissionNeeds);
                    if (ShareDialog.canShow(ShareLinkContent.class)) {
                        ShareLinkContent facebookContent = new ShareLinkContent.Builder()
                                .setContentTitle("Check out this pet!")
                                .setContentUrl(Uri.parse(shortURL))
                                .setImageUrl(Uri.parse(petMateListItems.getFirstImagePath()))
                                .build();
                        ShareDialog.show(PetMateList.this, facebookContent);
                    }
                }
                else {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    URL url = null;
                    InputStream input = null;
                    try {
                        url = new URL(petMateListItems.getFirstImagePath());

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

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetMateFetchList petMateFetchList = new PetMateFetchList(PetMateList.this);
                petMateFetchList.petMateFetchList(petMateLists, adapter, urlForFetch, requestState, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    private SwipeRefreshLayout.OnRefreshListener petMateListSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            PetMateListItems petMateListItems = petMateLists.get(0);
            String date = petMateListItems.getPetMatePostDate();
            //date = date.replace(" ", "+");
            try {
                url = "";
                url = URLInstance.getUrl();
                url = url+"?method=showPetMateSwipeRefreshList&format=json&date="+ URLEncoder.encode(date, "UTF-8")+"&email="+email+"";
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
                PetMateRefreshFetchList petMateRefreshFetchList = new PetMateRefreshFetchList(PetMateList.this);
                petMateRefreshFetchList.petMateRefreshFetchList(petMateLists, adapter, urlForFetch, petMateListSwipeRefreshLayout);
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
            Intent filterClassIntent = new Intent(PetMateList.this, PetMateListFilter.class);
            startActivityForResult(filterClassIntent, FILTER_STATE_RESULT);
        }
        return true;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Bundle res = data.getExtras();
            filterState = res.getInt("Filter_State");
            if(filterState == 0) {
                petMateListSwipeRefreshLayout.setEnabled(true);
                petMateLists.clear();
                adapter.notifyDataSetChanged();
                url = "";
                url = URLInstance.getUrl();
                url = url+"?method=showPetMateDetails&format=json&" +
                        "email="+email+"&currentPage="+current_page+"";
                recyclerView.addOnScrollListener(new PetMateFetchListScrollListener(layoutManager, current_page){

                    @Override
                    public void onLoadMore(int current_page) {
                        url = "";
                        url = URLInstance.getUrl();
                        url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";
                        grabURL(url);
                    }
                });

                petMateListSwipeRefreshLayout.setOnRefreshListener(petMateListSwipeRefreshListener);
                petMateListSwipeRefreshLayout.setColorSchemeResources(
                        R.color.refresh_progress_1,
                        R.color.refresh_progress_2,
                        R.color.refresh_progress_3,
                        R.color.refresh_progress_4);

                grabURL(url);
            }
            else if(filterState == 1) {
                petMateListSwipeRefreshLayout.setEnabled(false);
                FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();
                filterSelectedInstanceCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetMateListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetMateListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetMateListInstance.getFilterGenderListInstance();
                new FetchFilterPetMateListFromServer().execute();

                recyclerView.addOnScrollListener(new PetMateFetchListScrollListener(layoutManager, current_page){

                    @Override
                    public void onLoadMore(int current_page) {
                        FilterFetchPetMateList filterFetchPetMateList = new FilterFetchPetMateList(PetMateList.this);
                        filterFetchPetMateList.filterFetchPetMateList(petMateLists, adapter, email, current_page, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList);
                    }
                });
            }
        }
    }

    public class FetchFilterPetMateListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FilterFetchPetMateList filterFetchPetMateList = new FilterFetchPetMateList(PetMateList.this);
                filterFetchPetMateList.filterFetchPetMateList(petMateLists, adapter, email, current_page, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void requestReadStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(PetMateList.this,
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
            ActivityCompat.requestPermissions(PetMateList.this,
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
                Toast.makeText(PetMateList.this, "Write storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == READ_STORAGE_PERMISSION_REQUEST) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                shareList(petMateListItems);
            } else {
                Toast.makeText(PetMateList.this, "Read storage permission was NOT granted.", Toast.LENGTH_SHORT).show();
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
        if(filterState == 1) {
            FilterPetMateListDeleteMemCacheObject filterPetMateListDeleteMemCacheObject = new FilterPetMateListDeleteMemCacheObject(PetMateList.this);
            filterPetMateListDeleteMemCacheObject.deletePetMateListFilterObject(email);
            FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();
            filterSelectedInstanceCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
            filterSelectedInstanceBreedList = filterPetMateListInstance.getFilterBreedListInstance();
            filterSelectedInstanceAgeList = filterPetMateListInstance.getFilterAgeListInstance();
            filterSelectedInstanceGenderList = filterPetMateListInstance.getFilterGenderListInstance();
            filterSelectedInstanceCategoryList.clear();
            filterSelectedInstanceBreedList.clear();
            filterSelectedInstanceAgeList.clear();
            filterSelectedInstanceGenderList.clear();
            filterPetMateListInstance.setFilterCategoryListInstance(filterSelectedInstanceCategoryList);
            filterPetMateListInstance.setFilterBreedListInstance(filterSelectedInstanceBreedList);
            filterPetMateListInstance.setFilterAgeListInstance(filterSelectedInstanceAgeList);
            filterPetMateListInstance.setFilterGenderListInstance(filterSelectedInstanceGenderList);
        }
        PetMateList.this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PackageManager pm = PetMateList.this.getPackageManager();
        ComponentName component = new ComponentName(PetMateList.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
    }

    @Override
    protected void onResume() {
        super.onResume();
        PackageManager pm = PetMateList.this.getPackageManager();
        ComponentName component = new ComponentName(PetMateList.this, NetworkChangeReceiver.class);
        pm.setComponentEnabledSetting(component, PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.GET_ACTIVITIES);
    }
}