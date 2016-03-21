package com.couragedigital.peto.Connectivity;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.PetList;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONObject;

import java.util.Map;

public class WishListPetListAdd {
    private static Context context = null;
    private static String petlistId;
    private static String email;
    private static String method;
    private static String format;
    private static String addPetWishList;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String addPetListToWishList( String useremail,String petListId) throws Exception {
        method = "saveWishListForPetList";
        format = "json";
        email= useremail;
        petlistId = petListId;
        final String URL = URLInstance.getUrl();
        //final String URL = "http://192.168.0.6/PetAppAPI/api/petappapi.php";
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("userEmail", email);
            params.put("petListId", petlistId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        PetList petList = new PetList();
//                        try {
//                            returnResponse(response.getString("savePetWishListResponse"));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        AppController.getInstance().addToRequestQueue(req);
        return addPetWishList;
    }

    public WishListPetListAdd(Context context) {
        this.context = context;
    }

//    public static void returnResponse(String response) {
//        if (response.equals("LIST_ADDED_SUCCESSFULLY")) {
//            Toast.makeText(context, "Successfully AddedTto Wishlist .", Toast.LENGTH_SHORT).show();
//            Intent gotoPetListPage = new Intent(context, PetList.class);
//            context.startActivity(gotoPetListPage);
//        } else if (response.equals("ERROR")) {
//            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
//            Intent gotopetistage = new Intent(context, PetList.class);
//            context.startActivity(gotopetistage);
//        }
//    }
}
