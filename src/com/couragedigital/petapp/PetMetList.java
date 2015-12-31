package com.couragedigital.petapp;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import com.couragedigital.petapp.Adapter.PetListAdapter;
import com.couragedigital.petapp.Adapter.PetMetListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchList;
import com.couragedigital.petapp.Connectivity.PetMetFetchList;
import com.couragedigital.petapp.Connectivity.PetMetRefreshFetchList;
import com.couragedigital.petapp.Connectivity.PetRefreshFetchList;
import com.couragedigital.petapp.Listeners.PetFetchListScrollListener;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ProgressDialog;
import android.app.Activity;
import android.os.Bundle;
import com.couragedigital.petapp.SessionManager.SessionManager;


public class PetMetList extends BaseActivity {

    private static final String TAG = PetList.class.getSimpleName();

    // http://c/dev/api/petappapi.php?method=showPetDetails&format=json
    private static String url;
    private ProgressDialog progressDialog;
    public List<com.couragedigital.petapp.model.PetMetList> petMetLists = new ArrayList<com.couragedigital.petapp.model.PetMetList>();
    /*private ListView petlistView;
    public PetListAdapter Adapter;*/

    public List<com.couragedigital.petapp.model.PetMetList> petmetlistForFilter = new ArrayList<com.couragedigital.petapp.model.PetMetList>();
    public PetListAdapter adapterForFilter;

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout petListSwipeRefreshLayout;

    public List<com.couragedigital.petapp.model.PetMetList> originalpetLists = new ArrayList<com.couragedigital.petapp.model.PetMetList>();

    static String urlForFetch;

    private int current_page = 1;

    public String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petmetlist);

        /*petlistView = (ListView) findViewById(R.id.petList);
        Adapter = new PetListAdapter(this, petLists);
        petlistView.setAdapter(Adapter);*/

        recyclerView = (RecyclerView) findViewById(R.id.petList);
        petListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petListSwipeRefreshLayout);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        url = "http://storage.couragedigital.com/dev/api/petappapi.php?method=showPetMetDetails&format=json&email="+email+"&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = "http://storage.couragedigital.com/dev/api/petappapi.php?method=showPetMetDetails&format=json&email="+email+"&currentPage="+current_page+"";
                grabURL(url);
            }
        });

        recyclerView.smoothScrollToPosition(0);

        //recyclerView.fling(0,1);

        adapter = new PetMetListAdapter(petMetLists);
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
    }

    public void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetMetFetchList.petMetFetchList(petMetLists, adapter, urlForFetch, progressDialog);
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
            com.couragedigital.petapp.model.PetMetList petMetList = petMetLists.get(0);
            String date = petMetList.getPetMetPostDate();
            //date = date.replace(" ", "+");
            try {
                url = "http://storage.couragedigital.com/dev/api/petappapi.php?method=showPetMetSwipeRefreshList&format=json&date="+ URLEncoder.encode(date, "UTF-8")+"&email="+email+"";
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
                PetMetRefreshFetchList.petMetRefreshFetchList(petMetLists, adapter, urlForFetch, petListSwipeRefreshLayout);
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