package com.couragedigital.peto;

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
import com.couragedigital.peto.Adapter.GroomerListAdapter;
import com.couragedigital.peto.Connectivity.PetFetchGroomerList;
import com.couragedigital.peto.Listeners.PetFetchTrainerListScrollListener;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.GroomerListItem;

import java.util.ArrayList;
import java.util.List;

public class TabFragmentGroomer extends Fragment {
    ProgressDialog progressDialog;
    String urlForFetch;
    List<GroomerListItem> groomerListItemsArrayList = new ArrayList<GroomerListItem>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private String url = URLInstance.getUrl();
    //private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    int value;
    private View inflatedView;
    private View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.tab_fragment_groomer, container, false);
        return v;
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
                url = "";
                url = URLInstance.getUrl();
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

                PetFetchGroomerList petFetchGroomerList = new PetFetchGroomerList(v);
                petFetchGroomerList.petFetchGroomerList(groomerListItemsArrayList, adapter, urlForFetch, progressDialog);
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
