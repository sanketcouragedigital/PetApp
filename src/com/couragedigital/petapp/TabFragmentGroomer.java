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
import com.couragedigital.petapp.Adapter.GroomerListAdapter;
import com.couragedigital.petapp.Adapter.TrainerListAdapter;
import com.couragedigital.petapp.Connectivity.PetFetchGroomerList;
import com.couragedigital.petapp.Connectivity.PetFetchTrainerList;
import com.couragedigital.petapp.Listeners.PetFetchTrainerListScrollListener;
import com.couragedigital.petapp.model.GroomerListItem;
import com.couragedigital.petapp.model.TrainerListItem;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentGroomer extends Fragment {
    ProgressDialog progressDialog;
    String urlForFetch;
    List<GroomerListItem> groomerListItemsArrayList = new ArrayList<GroomerListItem>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private String url = "http://192.168.0.4/PetAPI/api/petappapi.php";
    //private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    int value;
    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_groomer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.petGroomerList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new GroomerListAdapter(groomerListItemsArrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        //progressDialog.setMessage("Fetching List Of Groomer...");
        //progressDialog.show();

        url = url + "?method=showPetGroomer&format=json&currentPage=" + current_page + " ";

        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new PetFetchTrainerListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = url + "?method=showPetGroomer&format=json&currentPage=" + current_page + " ";
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

                PetFetchGroomerList.petFetchGroomerList(groomerListItemsArrayList, adapter, urlForFetch, progressDialog);
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
