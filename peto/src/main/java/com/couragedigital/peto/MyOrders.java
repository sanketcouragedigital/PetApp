package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;
import com.couragedigital.peto.Adapter.MyOrderListAdapter;
import com.couragedigital.peto.Adapter.ShopProductListAdapter;
import com.couragedigital.peto.Connectivity.MyOrderFetchList;
import com.couragedigital.peto.Connectivity.ShopProductFetchList;
import com.couragedigital.peto.Connectivity.ShopProductRefreshFetchList;
import com.couragedigital.peto.Listeners.PetFetchListScrollListener;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.model.OrderListItems;
import com.couragedigital.peto.model.ProductListItems;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyOrders extends BaseActivity
{


    private static String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<OrderListItems> orderLists = new ArrayList<OrderListItems>();

    public static RecyclerView recyclerView;
    public static TextView emptyTextView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    public List<OrderListItems> originalorderLists = new ArrayList<OrderListItems>();
    static String urlForFetch;
    private int current_page = 1;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorders);

        SessionManager sessionManager = new SessionManager(MyOrders.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        recyclerView = (RecyclerView) findViewById(R.id.myOrdersList);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        url = url+"?method=showUserOrders&format=json&currentPage="+current_page+"&email="+email+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {

                url = "";
                url = URLInstance.getUrl();
                url = url+"?method=showUserOrders&format=json&currentPage="+current_page+"&email="+email+"";
                grabURL(url);

            }
        });

        recyclerView.smoothScrollToPosition(0);
        adapter = new MyOrderListAdapter(orderLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Orders...");
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
                MyOrderFetchList myOrderFetchList = new MyOrderFetchList(MyOrders.this);
                myOrderFetchList.orderFetchList(orderLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

    // to add symbol on app bar [like filter symbol]
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.petlistmenu, menu);
        return true;
    }



}
