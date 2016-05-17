package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.peto.Adapter.CampaignList_Adapter;
import com.couragedigital.peto.Adapter.ShopProductListAdapter;
import com.couragedigital.peto.Connectivity.Campaign_ShowList;
import com.couragedigital.peto.Connectivity.UserAllDetails;
import com.couragedigital.peto.Listeners.PetFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.CampaignListItem;
import com.couragedigital.peto.model.ProductListItems;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Campaign_List extends BaseActivity implements View.OnClickListener{

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
    FloatingActionButton createCampaignFabButton;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaign_list);

        createCampaignFabButton = (FloatingActionButton) this.findViewById(R.id.createCampaignFormFab);
        createCampaignFabButton.setOnClickListener(this);

        SessionManager sessionManager = new SessionManager(Campaign_List.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        recyclerView = (RecyclerView) findViewById(R.id.campaignList);
        emptyTextView = (TextView) findViewById(R.id.petListEmptyView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        try {
//            UserAllDetails.fetchContactNo(email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        url = url+"?method=showCampaignDetails&format=json&currentPage="+current_page+"&email="+email+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                url = "";
                url = URLInstance.getUrl();
                url = url+"?method=showCampaignDetails&format=json&currentPage="+current_page+"&email="+email+"";
                grabURL(url);

            }
        });

        recyclerView.smoothScrollToPosition(0);
        adapter = new CampaignList_Adapter(campaignLists);
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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.createCampaignFormFab) {
            Intent gotoCreateCampaign = new Intent(Campaign_List.this,Campaign_Form.class);
            startActivity(gotoCreateCampaign);
        }
    }

    public class FetchListFromServer extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... url) {
            try {
                urlForFetch = url[0];
                Campaign_ShowList campaign_ShowList = new Campaign_ShowList(Campaign_List.this);
                campaign_ShowList.campaignFetchList(campaignLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }




}