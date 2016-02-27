package com.couragedigital.petapp.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.EditProfile;
import com.couragedigital.petapp.PetList;
import com.couragedigital.petapp.PetMate;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class WishListPetMateListAdd {

    private static Context context = null;
    private static String petMatelistId;
    private static String useremail;
    private static String method;
    private static String format;
    private static String addPetMateWishList;
    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static String addPetMateListToWishList( String email,String listId ) throws Exception {
        method = "saveWishListForPetMateList";
        format = "json";
        useremail = email;
        petMatelistId = listId;
        final String URL = URLInstance.getUrl();
        //final String URL = "http://192.168.0.6/PetAppAPI/api/petappapi.php";
        JSONObject params = new JSONObject();
        try {
            params.put("method", method);
            params.put("format", format);
            params.put("email", useremail);
            params.put("listId", petMatelistId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        EditProfile editProfile = new EditProfile();
                        try {
                            returnResponse(response.getString("savePetMateWishListResponse"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        }
        );
        AppController.getInstance().addToRequestQueue(req);
        return addPetMateWishList;
    }

    public WishListPetMateListAdd(Context context) {
        this.context = context;
    }

    public static void returnResponse(String response) {
        if (response.equals("LIST_ADDED_SUCCESSFULLY")) {
            Toast.makeText(context, "Successfully AddedTto Wishlist .", Toast.LENGTH_SHORT).show();
            Intent gotoPetMateListPage = new Intent(context, PetMate.class);
            context.startActivity(gotoPetMateListPage);
        } else if (response.equals("ERROR")) {
            Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
            Intent gotoPetMateListPage = new Intent(context, PetMate.class);
            context.startActivity(gotoPetMateListPage);
        }
    }
}
