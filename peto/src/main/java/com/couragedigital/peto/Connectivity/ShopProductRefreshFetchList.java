package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Pet_Shop_List;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ProductListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShopProductRefreshFetchList {
    private static final String TAG = ShopProductRefreshFetchList.class.getSimpleName();
    private static Context context;

    public ShopProductRefreshFetchList(Pet_Shop_List productList) {
        context = productList;
    }

    public static List productRefreshFetchList(final List<ProductListItems> productLists, final RecyclerView.Adapter adapter, String url, final SwipeRefreshLayout petShopListSwipeRefreshLayout) {
        JsonObjectRequest petRefreshFetchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showShopProductDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                        ProductListItems productListItems = new ProductListItems();
                                        productListItems.setProductId(obj.getString("id"));
                                        productListItems.setFirstImagePath(obj.getString("first_image_path"));
                                        if(!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                            productListItems.setSecondImagePath(obj.getString("second_image_path"));
                                        }
                                        if(!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                            productListItems.setThirdImagePath(obj.getString("third_image_path"));
                                        }
                                        productListItems.setProductName(replaceSpecialChars(obj.getString("product_name")));
                                        productListItems.setProductPrice(replaceSpecialChars(obj.getString("product_price")));
                                        productListItems.setProductDescription(replaceSpecialChars(obj.getString("product_description")));
                                        productListItems.setProductPostDate(obj.getString("post_date"));

                                        // adding pet to pets array
                                        productLists.add(0, productListItems);
                                        adapter.notifyDataSetChanged();
//
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // notifying list Adapter about data changes
                        // so that it renders the list view with updated data
                        if(petShopListSwipeRefreshLayout.isRefreshing()) {
                            petShopListSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                if(petShopListSwipeRefreshLayout.isRefreshing()) {
                    petShopListSwipeRefreshLayout.setRefreshing(false);
                }
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petRefreshFetchRequest);
        return productLists;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
