package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.couragedigital.petapp.Adapter.ClinicListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchClinicList;
import com.couragedigital.petapp.Listeners.PetFetchClinicListScrollListener;
import com.couragedigital.petapp.model.*;

import java.util.ArrayList;
import java.util.List;


public class PetClinic extends BaseActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    LinearLayoutManager linearLayoutManager;

    private static String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
    private ProgressDialog progressDialog;
    public List<PetClinicList> clinicListItemsArrayList = new ArrayList<PetClinicList>();

    static String urlForFetch;
    private int current_page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petclinic);

        recyclerView = (RecyclerView) findViewById(R.id.petClinicList);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        url = url + "?method=showClinicDetails&format=json&currentPage=" + current_page + "";

        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new PetFetchClinicListScrollListener(linearLayoutManager, current_page) {

            @Override
            public void onLoadMore(int current_page) {
                url = url + "?method=showClinicDetails&format=json&currentPage=" + current_page + "";
                grabURL(url);
            }
        });

        adapter = new ClinicListAdapter(clinicListItemsArrayList);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Clinics...");
        progressDialog.show();

        grabURL(url);
    }

    private void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    private class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                PetFetchClinicList.petFetchClinicList(clinicListItemsArrayList, adapter, urlForFetch, progressDialog);
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

}

