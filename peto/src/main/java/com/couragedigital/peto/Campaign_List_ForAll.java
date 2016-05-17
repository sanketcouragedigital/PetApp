package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.couragedigital.peto.Adapter.CampaignList_Adapter;
import com.couragedigital.peto.Adapter.CampaignList_ForAll_Adapter;
import com.couragedigital.peto.Connectivity.Campaign_ShowList;
import com.couragedigital.peto.Connectivity.Campaign_ShowList_ForAll;
import com.couragedigital.peto.Listeners.PetFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.CampaignListItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class Campaign_List_ForAll extends BaseActivity {

    private static String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<CampaignListItem> campaignLists = new ArrayList<CampaignListItem>();

    public static RecyclerView recyclerView;
    public static TextView emptyTextView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    static String urlForFetch;
    private int current_page = 1;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_list_forall);

        SessionManager sessionManager = new SessionManager(Campaign_List_ForAll.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        recyclerView = (RecyclerView) findViewById(R.id.campaignListForAll);
        emptyTextView = (TextView) findViewById(R.id.petListEmptyView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        try {
//            UserAllDetails.fetchContactNo(email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        url = url+"?method=showCampaignForAll&format=json&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url+"?method=showCampaignForAll&format=json&currentPage="+current_page+"";
                grabURL(url);

            }
        });

        recyclerView.smoothScrollToPosition(0);
        adapter = new CampaignList_ForAll_Adapter(campaignLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Campaign...");
        progressDialog.show();

        grabURL(url);
    }

    public void grabURL(String url) {
        new FetchListFromServer().execute(url);
    }

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                Campaign_ShowList_ForAll campaign_ShowList_ForAll = new Campaign_ShowList_ForAll(Campaign_List_ForAll.this);
                campaign_ShowList_ForAll.campaignFetchListForAll(campaignLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }




}