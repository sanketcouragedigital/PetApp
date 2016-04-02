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
import com.couragedigital.peto.Adapter.WishListPetMateListAdapter;
import com.couragedigital.peto.Connectivity.WishListPetMateFetchList;
import com.couragedigital.peto.Connectivity.WishListPetMateListDelete;
import com.couragedigital.peto.Listeners.WishListPetMateFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.WishListPetMateListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WishListPetMateListTab extends Fragment implements WishListPetMateListAdapter.OnRecyclerWishListPetMateDeleteClickListener {
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    SessionManager sessionManager;
    String urlForFetch;
    List<WishListPetMateListItem> wishListPetMateArrayList = new ArrayList<WishListPetMateListItem>();

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
        v = inflater.inflate(R.layout.wishlistpetmatelist, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.wishlistPetMateListView);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new WishListPetMateListAdapter(wishListPetMateArrayList, this);
        recyclerView.setAdapter(adapter);

        sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);



        url = url+"?method=showPetMateWishList&format=json&email="+email+"&currentPage="+current_page+"";
        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new WishListPetMateFetchListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showPetMateWishList&format=json&email="+email+"&currentPage="+current_page+"";
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
                WishListPetMateFetchList wishListPetMateFetchList = new WishListPetMateFetchList(v);
                wishListPetMateFetchList.wishListPetMateFetchList(wishListPetMateArrayList, adapter, urlForFetch);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public void onRecyclerWishListPetMateDeleteClick(final List<WishListPetMateListItem> wishListPetMateListItems, final WishListPetMateListItem wishListPetMateListItem, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Delete This List!");
        alertDialogBuilder.setMessage("Are you sure you want to delete this list?");
        alertDialogBuilder.setCancelable(true);
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteWishListPetMate(wishListPetMateListItems, wishListPetMateListItem, position);
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

    private void deleteWishListPetMate(List<WishListPetMateListItem> wishListPetMateListItems, WishListPetMateListItem wishListPetMateListItem, int position) {
        if(wishListPetMateListItem != null) {
            SessionManager sessionManager = new SessionManager(v.getContext());
            HashMap<String, String> user = sessionManager.getUserDetails();
            String email = user.get(SessionManager.KEY_EMAIL);
            String petMateListId = String.valueOf(wishListPetMateListItem.getId());
            new DeletePetMateListFromServer().execute(email, petMateListId);
            wishListPetMateListItems.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyItemRangeChanged(position, wishListPetMateListItems.size());
        }
    }

    public class DeletePetMateListFromServer extends AsyncTask<String, String, String> {
        String email;
        String petMateListId;
        @Override
        protected String doInBackground(String... params) {
            try {
                email = params[0];
                petMateListId = params[1];
                WishListPetMateListDelete wishListPetMateListDelete = new WishListPetMateListDelete(v);
                wishListPetMateListDelete.deleteWishListPetMateFromServer(email, petMateListId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
