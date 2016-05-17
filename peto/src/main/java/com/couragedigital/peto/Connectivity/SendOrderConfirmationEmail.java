package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.*;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SendOrderConfirmationEmail {

    private static Context context = null;
    private static String orderId;
    private static String productId;
    private static String productQty;
    private static String productShippingCharges;
    private static String productTotalPrice;
    private static String custName;
    private static String custContactNo;
    private static String custEmail;
    private static String custAddress;
    private static String custArea;
    private static String custCity;
    private static String custPincode;
    private static String productName;
    private static String Productprice;

    private static String OrderResponse;

    private static String method;
    private static String format;
    private static String oerderGenerateResponse;
    public static PaymentActivity productListActivity;

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;


    public String SendConfirmationEmail(String orderedId, String prodId, String nameofProduct, String price, String prodQty, String prodShippingCharges, String prodTotalPrice, String name, String contactNo, String email, String address, String area, String city, String pincode) throws Exception {

        method = "sendOrderEmail";
        format = "json";
        orderId = orderedId;
        productId = prodId;
        productName = nameofProduct;
        Productprice = price;
        productQty = prodQty;
        productShippingCharges = prodShippingCharges;
        productTotalPrice = prodTotalPrice;
        custName = name;
        custContactNo = contactNo;
        custEmail = email;
        custAddress = address;
        custArea = area;
        custCity = city;
        custPincode = pincode;


        final String URL = URLInstance.getUrl();
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("productId", productId);
            params.put("orderedId", orderedId);
            params.put("productName", productName);
            params.put("productPrice", Productprice);
            params.put("quantity", productQty);
            params.put("shippingCharges", productShippingCharges);
            params.put("productTotalPrice", productTotalPrice);
            params.put("customer_name", custName);
            params.put("customer_contact", custContactNo);
            params.put("customer_email", custEmail);
            params.put("address", custAddress);
            params.put("area", custArea);
            params.put("city", custCity);
            params.put("pincode", custPincode);
        } catch (Exception e) {

        }
        JsonObjectRequest orderEmailRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response: %n %s", response.toString());
                        try {
                            returnResponse(response.getString("saveOrderEmailDetailsResponse"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.e("Error: ", error.getMessage());
                        Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                        context.startActivity(gotoTimeOutError);
                    }
                }
        );
        orderEmailRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(orderEmailRequest);
        return oerderGenerateResponse;
    }

    public SendOrderConfirmationEmail(Context context) {
        this.context = context;
    }
    public void returnResponse(String response) {
        if (response.equals("EMAIL_SUCCESSFUULY_SENT")) {
            Toast.makeText(context, "Your transaction succsessfully done.", Toast.LENGTH_SHORT).show();
            Intent gotoPet_Shop_List = new Intent(context, Pet_Shop_List.class);
            context.startActivity(gotoPet_Shop_List);
        } else {
            Toast.makeText(context, "Please try again later.", Toast.LENGTH_SHORT).show();
        }
    }
}
