package com.couragedigital.peto.Connectivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.EmptyListDialoge;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.MyOrders;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.OrderListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyOrderFetchList {

    private static final String TAG = MyOrderFetchList.class.getSimpleName();
    // private static ArrayList<String> wishListPetListId = new ArrayList<String>();
    //private static  int checkRequestState;
    private static Context context;
    private static String detailsResponse;
    private static String urlContact = URLInstance.getUrl();

    public MyOrderFetchList(MyOrders orderList) {
        context = orderList;
    }

    public static List orderFetchList(final List<OrderListItems> orderLists, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {

        JsonObjectRequest orderFetchRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showOrderDetailsResponse");
                            if (jsonArray.length()==0) {
                                Intent gotoEmptyList = new Intent(context, EmptyListDialoge.class);
                                context.startActivity(gotoEmptyList);
                            }
                             else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    OrderListItems orderListItems = new OrderListItems();

                                    orderListItems.setOrderListId(obj.getString("order_id"));
                                    orderListItems.setOrderProductQuantity(obj.getString("quantity"));
                                    orderListItems.setOorderProductShipping_charges(obj.getString("shipping_charges"));
                                    orderListItems.setOrderProductTotal_price(obj.getString("total_price"));
                                    orderListItems.setOrderProductCustomer_name(obj.getString("customer_name"));
                                    orderListItems.setOrderProductCustomer_contact(obj.getString("customer_contact"));
                                    orderListItems.setOrderProductCustomer_email(obj.getString("customer_email"));
                                    orderListItems.setOrderProductBuidling_name(obj.getString("address"));
                                    orderListItems.setOrderProductArea(obj.getString("area"));
                                    orderListItems.setOrderProductCity(obj.getString("city"));
                                    orderListItems.setOrderProductPostDate(obj.getString("post_date"));
                                    orderListItems.setOrderProductId(obj.getString("id"));
                                    orderListItems.setFirstImagePath(obj.getString("first_image_path"));
                                    if (!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        orderListItems.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if (!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        orderListItems.setThirdImagePath(obj.getString("third_image_path"));
                                    }
                                    orderListItems.setOrderProductName(replaceSpecialChars(obj.getString("product_name")));
                                    orderListItems.setOrderProductPrice(obj.getString("product_price"));
                                    orderListItems.setOrderProductDescription(replaceSpecialChars(obj.getString("product_description")));
                                    orderListItems.setOrderPinCode(replaceSpecialChars(obj.getString("pincode")));

                                    // adding pet to pets array
                                    orderLists.add(orderListItems);
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(orderFetchRequest);
        return orderLists;
    }
    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }

}
