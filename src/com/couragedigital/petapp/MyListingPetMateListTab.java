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
import com.couragedigital.petapp.Adapter.MyListingPetMateListAdapter;
import com.couragedigital.petapp.Connectivity.MyListingPetMateFetchList;
import com.couragedigital.petapp.Listeners.MyListingPetMateFetchListScrollListener;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.model.MyListingPetMateListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListingPetMateListTab extends Fragment {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    SessionManager sessionManager;
    ProgressDialog progressDialog;
    String urlForFetch;
    List<MyListingPetMateListItem> myListingPetMateArrayList = new ArrayList<MyListingPetMateListItem>();

       private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
 //   private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    public String email;
    View v;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mylistingpetmatelist, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        recyclerView = (RecyclerView) getView().findViewById(R.id.myListingPetMateListView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        adapter = new MyListingPetMateListAdapter(myListingPetMateArrayList);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets Mate...");
        progressDialog.show();

        url = url+"?method=showMyListingPetMateList&format=json&email="+email+"&currentPage="+current_page+"";
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new MyListingPetMateFetchListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = "http://storage.couragedigital.com/dev/api/petappapi.php";
                url = url + "?method=showMyListingPetMateList&format=json&email="+email+"&currentPage="+current_page+"";
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

                MyListingPetMateFetchList.myListingPetMateFetchList(myListingPetMateArrayList, adapter, urlForFetch, progressDialog);
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