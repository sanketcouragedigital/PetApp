package com.couragedigital.petapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.couragedigital.petapp.Adapter.PetListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchList;
import com.couragedigital.petapp.Listeners.PetFetchListScrollListener;
import com.couragedigital.petapp.model.PetListItems;

import java.util.ArrayList;
import java.util.List;


public class WishListPetList extends Activity {

    private static final String TAG = PetList.class.getSimpleName();

    // http://c/dev/api/petappapi.php?method=showPetDetails&format=json
    //private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
    private static String url = "http:/192.168.1.3/PetAppAPI/api/petappapi.php";
    private ProgressDialog progressDialog;
    public List<PetListItems> petLists = new ArrayList<PetListItems>();

    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    static String urlForFetch;

    private int current_page = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wishlistpetlist);

        recyclerView = (RecyclerView) findViewById(R.id.petList);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = url+"?method=showPetDetails&format=json&currentPage="+current_page+"";
                grabURL(url);
            }
        });

        recyclerView.smoothScrollToPosition(0);

        //recyclerView.fling(0,1);

        adapter = new PetListAdapter(petLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets...");
        progressDialog.show();

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
                PetFetchList.petFetchList(petLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
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
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

}