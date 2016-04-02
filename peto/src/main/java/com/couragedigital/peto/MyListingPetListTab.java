package com.couragedigital.peto;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.couragedigital.peto.Adapter.MyListingPetListAdapter;
import com.couragedigital.peto.Connectivity.MyListingPetFetchList;
import com.couragedigital.peto.Connectivity.MyListingPetListDelete;
import com.couragedigital.peto.Listeners.MyListingPetFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.MyListingPetListItems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MyListingPetListTab extends Fragment implements MyListingPetListAdapter.OnRecyclerMyListingPetDeleteClickListener {

    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    String urlForFetch;
    List<MyListingPetListItems> myListingPetArrayList = new ArrayList<MyListingPetListItems>();

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
        adapter = new MyListingPetListAdapter(myListingPetArrayList, this);
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
                url = URLInstance.getUrl();
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

                MyListingPetFetchList myListingPetFetchList = new MyListingPetFetchList(v);
                myListingPetFetchList.myListingPetFetchList(myListingPetArrayList, adapter, urlForFetch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onRecyclerMyListingPetDeleteClick(final List<MyListingPetListItems> myListingpetLists, final MyListingPetListItems myListingPetListItem, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Delete This List!");
        alertDialogBuilder.setMessage("Are you sure you want to delete this list?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteMyListingPet(myListingpetLists, myListingPetListItem, position);
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteMyListingPet(List<MyListingPetListItems> myListingpetLists, MyListingPetListItems myListingPetListItem, int position) {
        if (myListingPetListItem != null) {
            String url = URLInstance.getUrl();
            int id = myListingPetListItem.getId();
            String email = myListingPetListItem.getPetPostOwnerEmail();
            url = url + "?method=deleteMyListingPetList&format=json&id=" + id + "&email=" + email + "";
            new DeletePetListFromServer().execute(url);
            myListingpetLists.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, myListingpetLists.size());
        }
    }

    public class DeletePetListFromServer extends AsyncTask<String, String, String> {

        String urlForFetch;

        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                MyListingPetListDelete.deleteFromRemoteServer(urlForFetch, v);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}

