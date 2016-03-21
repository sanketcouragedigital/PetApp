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
import com.couragedigital.peto.Adapter.WishListPetListAdapter;
import com.couragedigital.peto.Connectivity.WishListPetFetchList;
import com.couragedigital.peto.Listeners.WishListPetFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.WishListPetListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListPetListTab extends Fragment {
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String urlForFetch;
    List<WishListPetListItem> wishlistPetArrayList = new ArrayList<WishListPetListItem>();
    private String url = URLInstance.getUrl();

    int current_page = 1;
    View v;
    private String email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.wishlistpetlist, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.wishlistPetListView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WishListPetListAdapter(wishlistPetArrayList);
        recyclerView.setAdapter(adapter);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        url = url + "?method=showPetListWishList&format=json&email="+ email +"&currentPage=" + current_page + "";
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new WishListPetFetchListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showPetListWishList&format=json&email="+ email +"&currentPage=" + current_page + "";
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
                WishListPetFetchList.wishListPetFetchList(wishlistPetArrayList, adapter, urlForFetch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
