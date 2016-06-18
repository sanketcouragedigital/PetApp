package com.couragedigital.peto;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.widget.TextView;

import com.couragedigital.peto.Adapter.ShopProductListAdapter;
import com.couragedigital.peto.Connectivity.ShopProductFetchList;
import com.couragedigital.peto.Connectivity.ShopProductRefreshFetchList;
import com.couragedigital.peto.Connectivity.UserAllDetails;
import com.couragedigital.peto.Listeners.PetFetchListScrollListener;

import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;

import com.couragedigital.peto.model.ProductListItems;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;

public class Pet_Shop_List extends BaseActivity {

    private static String url = URLInstance.getUrl();
    private ProgressDialog progressDialog;
    public List<ProductListItems> productLists = new ArrayList<ProductListItems>();

    public static RecyclerView recyclerView;
    public static TextView emptyTextView;
    LinearLayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    SwipeRefreshLayout petShopListSwipeRefreshLayout;

    public List<ProductListItems> originalproductLists = new ArrayList<ProductListItems>();
    static String urlForFetch;
    private int current_page = 1;
    String email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pet_shop_list);

        SessionManager sessionManager = new SessionManager(Pet_Shop_List.this);
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        recyclerView = (RecyclerView) findViewById(R.id.petShopList);
        emptyTextView = (TextView) findViewById(R.id.petListEmptyView);
        petShopListSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.petShopListSwipeRefreshLayout);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        try {
//            UserAllDetails.fetchContactNo(email);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        url = url+"?method=showShopProductsDetails&format=json&currentPage="+current_page+"";

        recyclerView.addOnScrollListener(new PetFetchListScrollListener(layoutManager, current_page){

            @Override
            public void onLoadMore(int current_page) {
                    url = "";
                    url = URLInstance.getUrl();
                    url = url+"?method=showShopProductsDetails&format=json&currentPage="+current_page+"";
                    grabURL(url);

            }
        });

        recyclerView.smoothScrollToPosition(0);
        adapter = new ShopProductListAdapter(productLists);
        recyclerView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Products...");
        progressDialog.show();

//        petShopListSwipeRefreshLayout.setOnRefreshListener(shopProductListSwipeRefreshListener);
//        petShopListSwipeRefreshLayout.setColorSchemeResources(
//                R.color.refresh_progress_1,
//                R.color.refresh_progress_2,
//                R.color.refresh_progress_3,
//                R.color.refresh_progress_4);

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
                ShopProductFetchList shopProductFetchList = new ShopProductFetchList(Pet_Shop_List.this);
                shopProductFetchList.productFetchList(productLists, adapter, urlForFetch, progressDialog);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
            return null;
        }
    }

//    private SwipeRefreshLayout.OnRefreshListener shopProductListSwipeRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
//        @Override
//        public void onRefresh() {
//            ProductListItems productListItems = productLists.get(0);
//            String date = productListItems.getProductPostDate();
//            //date = date.replace(" ", "+");
//            try {
//                url = "";
//                url = URLInstance.getUrl();
//                url = url+"?method=showShopProductSwipeRefreshList&format=json&date="+ URLEncoder.encode(date, "UTF-8")+"";
//            } catch (UnsupportedEncodingException e) {
//                e.printStackTrace();
//            }
//            new FetchRefreshListFromServer().execute(url);
//        }
//    };

//    public class FetchRefreshListFromServer extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... url) {
//            try {
//                urlForFetch = url[0];
//                ShopProductRefreshFetchList shopProductRefreshFetchList = new ShopProductRefreshFetchList(Pet_Shop_List.this);
//                shopProductRefreshFetchList.productRefreshFetchList(productLists, adapter, urlForFetch, petShopListSwipeRefreshLayout);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//    }
//
//    private void hideProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//            progressDialog = null;
//        }
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.petlistmenu, menu);
//        return true;
//    }



}
