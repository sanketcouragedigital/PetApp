package com.couragedigital.petapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.couragedigital.petapp.Adapter.PetMateListAdapter;
import com.couragedigital.petapp.Connectivity.FilterFetchPetMateList;
import com.couragedigital.petapp.Connectivity.PetMateFetchList;
import com.couragedigital.petapp.Connectivity.PetMateRefreshFetchList;
import com.couragedigital.petapp.Listeners.PetMateFetchListScrollListener;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Singleton.FilterPetMateListInstance;
import com.couragedigital.petapp.model.PetMateListItems;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;

public class PetMateList extends BaseActivity {

    private static final String TAG = PetMateList.class.getSimpleName();

    // http://c/dev/api/petappapi.php?method=showPetDetails&format=json
    // "http://storage.couragedigital.com/dev/api/petappapi.php"
    private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
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

    int filterState;

    int FILTER_STATE_RESULT = 1;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmatelist);

        /*petlistView = (ListView) findViewById(R.id.petList);
        Adapter = new PetListAdapter(this, petLists);
        petlistView.setAdapter(Adapter);*/

        recyclerView = (RecyclerView) findViewById(R.id.petMateList);
        petMateListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petMateListSwipeRefreshLayout);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetMateFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";
                grabURL(url);
            }
        });

        recyclerView.smoothScrollToPosition(0);

        //recyclerView.fling(0,1);

        adapter = new PetMateListAdapter(petMateLists);
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

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetMateFetchList.petMateFetchList(petMateLists, adapter, urlForFetch, progressDialog);
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
                PetMateRefreshFetchList.petMateRefreshFetchList(petMateLists, adapter, urlForFetch, petMateListSwipeRefreshLayout);
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
                petMateLists.clear();
                adapter.notifyDataSetChanged();
                url = url+"?method=showPetMateDetails&format=json&email="+email+"&currentPage="+current_page+"";
                recyclerView.addOnScrollListener(new PetMateFetchListScrollListener(layoutManager, current_page){

                    @Override
                    public void onLoadMore(int current_page) {
                        url = "";
                        url = "http://storage.couragedigital.com/dev/api/petappapi.php";
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
                FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();
                filterSelectedInstanceCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetMateListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetMateListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetMateListInstance.getFilterGenderListInstance();
                new FetchFilterPetMateListFromServer().execute();
            }
        }
    }

    public class FetchFilterPetMateListFromServer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                FilterFetchPetMateList.filterFetchPetMateList(petMateLists, adapter, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList, email);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petlistmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter) {
            PetListInstance petListInstance = new PetListInstance(Adapter, petLists, petlistView);
            Intent filterClassIntent = new Intent(PetList.this, PetListFilter.class);
            //filterClassIntent.putExtra("PET_LISTS", (Parcelable) petLists);
            //filterClassIntent.putExtra("PET_ADAPTER", (Parcelable) Adapter);
            startActivity(filterClassIntent);
        }
        return true;
    }*/

    /*@Override
    public void onRestart() {
        super.onRestart();
        /*if(PetListInstance.getListInstance() != originalpetLists) {
            petLists = PetListInstance.getListInstance();
            Adapter = new PetListAdapter(petLists);
            recyclerView.setAdapter(Adapter);
            Adapter.notifyDataSetChanged();
        }
        Bundle savedInstanceState = null;
        onCreate(savedInstanceState);

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}