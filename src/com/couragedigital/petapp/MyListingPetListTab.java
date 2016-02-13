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
import com.couragedigital.petapp.Adapter.MyListingPetListAdapter;
import com.couragedigital.petapp.Connectivity.MyListingPetFetchList;
import com.couragedigital.petapp.Listeners.MyListingPetFetchListScrollListener;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.model.MyListingPetListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyListingPetListTab extends Fragment {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String urlForFetch;
    List<MyListingPetListItems> myListingPetArrayList = new ArrayList<MyListingPetListItems>();

    private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    View v;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.mylistingpetlist, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.myListingPetListView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MyListingPetListAdapter(myListingPetArrayList);
        recyclerView.setAdapter(adapter);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        url = url + "?method=showMyListingPetList&format=json&email="+ email +"&currentPage=" + current_page + "";
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new MyListingPetFetchListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = "http://storage.couragedigital.com/dev/api/petappapi.php";
                url = url + "?method=showMyListingPetList&format=json&email="+ email +"&currentPage=" + current_page + "";
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

                MyListingPetFetchList.myListingPetFetchList(myListingPetArrayList, adapter, urlForFetch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

