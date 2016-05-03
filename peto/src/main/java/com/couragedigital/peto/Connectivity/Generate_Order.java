package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.*;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Generate_Order {

    private static Context context = null;
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
    private static String orderNo;
    private static String orderId;


    private static String OrderResponse;

    private static String method;
    private static String format;
    private static String oerderGenerateResponse;
    public static Pet_Shop_List_Details productListActivity;

    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String GenarateOrder(String prodId,String nameofProduct,String price, String prodQty, String prodShippingCharges, String prodTotalPrice,String name, String contactNo, String email, String address, String area, String city, String pincode) throws Exception {

        method = "orderGenaration";
        format = "json";
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
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject obj = response.getJSONObject("saveOrdersDetailsResponse");
                            OrderResponse = obj.getString("OrderGenerateResponse");
                            orderNo = obj.getString("orderId");
                            returnResponse(OrderResponse);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        }
        );
        orderRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(orderRequest);
        return oerderGenerateResponse;
    }

    public Generate_Order(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("ORDER_GENERATED")) {
            Toast.makeText(context, "Your order Successfully Genarated..", Toast.LENGTH_SHORT).show();

            Intent gotoPayment = new Intent(context, PaymentActivity.class);
            gotoPayment.putExtra("orderNo",orderNo);
            gotoPayment.putExtra("productId", productId);
            gotoPayment.putExtra("productName",productName);
            gotoPayment.putExtra("productPrice",Productprice);
            gotoPayment.putExtra("productQty",productQty);
            gotoPayment.putExtra("shippingCharges",productShippingCharges);
            gotoPayment.putExtra("totalPrice",productTotalPrice);
            gotoPayment.putExtra("customer_name", custName);
            gotoPayment.putExtra("email",custEmail);
            gotoPayment.putExtra("contactNo",custContactNo);
            gotoPayment.putExtra("address", custAddress);
            gotoPayment.putExtra("area", custArea);
            gotoPayment.putExtra("city", custCity);
            gotoPayment.putExtra("pincode", custPincode);
            context.startActivity(gotoPayment);

        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "There is some problem,Please try ahain later.", Toast.LENGTH_SHORT).show();
            Intent gotoProductList = new Intent(context, Pet_Shop_List.class);
            context.startActivity(gotoProductList);
        }
    }

}
