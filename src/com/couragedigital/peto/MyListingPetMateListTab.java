package com.couragedigital.peto;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.couragedigital.peto.Adapter.MyListingPetMateListAdapter;
import com.couragedigital.peto.Connectivity.MyListingPetMateFetchList;
import com.couragedigital.peto.Listeners.MyListingPetMateFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.MyListingPetMateListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyListingPetMateListTab extends Fragment {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    SessionManager sessionManager;
    String urlForFetch;
    List<MyListingPetMateListItem> myListingPetMateArrayList = new ArrayList<MyListingPetMateListItem>();

       private String url = URLInstance.getUrl();
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

        url = url+"?method=showMyListingPetMateList&format=json&email="+email+"&currentPage="+current_page+"";
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new MyListingPetMateFetchListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
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

                MyListingPetMateFetchList.myListingPetMateFetchList(myListingPetMateArrayList, adapter, urlForFetch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}