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
        import com.couragedigital.peto.Adapter.TrainerListAdapter;
        import com.couragedigital.peto.Connectivity.PetFetchTrainerList;
        import com.couragedigital.peto.Listeners.PetFetchTrainerListScrollListener;
        import com.couragedigital.peto.Singleton.URLInstance;
        import com.couragedigital.peto.model.TrainerListItem;

        import java.util.ArrayList;
        import java.util.List;

public class TabFragmentTrainer extends Fragment {
    ProgressDialog progressDialog;
    String urlForFetch;
    List<TrainerListItem> trainerListItemsArrayList = new ArrayList<TrainerListItem>();
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    private String url = URLInstance.getUrl();
    //private String url = "http://storage.couragedigital.com/dev/api/petappapi.php";

    int current_page = 1;
    int value;
    private View inflatedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_fragment_trainer, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = (RecyclerView) getView().findViewById(R.id.petTrainerList);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new TrainerListAdapter(trainerListItemsArrayList);
        recyclerView.setAdapter(adapter);
        progressDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
     //   progressDialog.setMessage("Fetching List Of Trainer...");
       // progressDialog.show();

        url = url + "?method=showPetTrainer&format=json&currentPage=" + current_page + " ";

        recyclerView.smoothScrollToPosition(0);
        recyclerView.addOnScrollListener(new PetFetchTrainerListScrollListener(linearLayoutManager, current_page) {
            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url + "?method=showPetTrainer&format=json&currentPage=" + current_page + " ";
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

                PetFetchTrainerList.petFetchTrainerList(trainerListItemsArrayList, adapter, urlForFetch, progressDialog);
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
