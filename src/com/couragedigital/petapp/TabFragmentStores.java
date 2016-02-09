package com.couragedigital.petapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.couragedigital.petapp.Adapter.StoresListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchStoresList;
import com.couragedigital.petapp.Listeners.PetFetchStoresListScrollListener;
import com.couragedigital.petapp.model.StoresListItem;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentStores extends Fragment {
    ProgressDialog progressDialog;
    String urlForFetch;
    List<StoresListItem> storesListItemsArrayList = new ArrayList<StoresListItem>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
    //private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    int value;
    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_stores, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.petStoresList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new StoresListAdapter(storesListItemsArrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        //progressDialog.setMessage("Fetching List Of Stores...");
        //progressDialog.show();

        url = url + "?method=showPetStores&format=json&currentPage=" + current_page + " ";

        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new PetFetchStoresListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = "http://storage.couragedigital.com/dev/api/petappapi.php";
                url = url + "?method=showPetStores&format=json&currentPage=" + current_page + " ";
                grabURL(url);
            }
        });

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

                PetFetchStoresList.petFetchStoresList(storesListItemsArrayList, adapter, urlForFetch, progressDialog);
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
