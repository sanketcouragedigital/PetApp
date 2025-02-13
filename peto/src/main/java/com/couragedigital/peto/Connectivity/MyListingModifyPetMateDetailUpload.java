package com.couragedigital.peto.Connectivity;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.MyListingModifyPetMateDetails;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class MyListingModifyPetMateDetailUpload {
    //private static final String SERVER_URL = "http://storage.couragedigital.com/dev/api/petappapi.php";
     private static final String SERVER_URL = URLInstance.getUrl();

    private static Context context = null;
    int serverResponseCode;
    private static String  categoryOfPet;
    private static String breedOfPet;
    private static String petAgeInMonth;
    private static String petAgeInYear;
    private static String genderOfPet;
    private static String descriptionOfPet;
    private static String email;
    private static String method;
    private static String format;
    private static Integer petlistid;
    private static String modifyUpdatedResponse;


    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;

    public static Void uploadToRemoteServer(String petCategoryName, String petBreedName, String petMateAgeMonthSpinner, String petMateAgeYearSpinner, String petMateGender, String petMateDescription, String emailOfUser, Integer id, final MyListingModifyPetMateDetails myListingModifyPetMateDetails) throws Exception{
        method = "saveModifiedPetMateDetails";
        format = "json";
        categoryOfPet = petCategoryName;
        breedOfPet = petBreedName;
        petAgeInMonth = petMateAgeMonthSpinner;
        petAgeInYear = petMateAgeYearSpinner;
        genderOfPet = petMateGender;
        descriptionOfPet = petMateDescription;
        email = emailOfUser;
        petlistid = id;
        context = myListingModifyPetMateDetails;

        JSONObject params = new JSONObject();
        try {
            params.put("categoryOfPet", categoryOfPet);
            params.put("breedOfPet", breedOfPet);
            params.put("petAgeInMonth", petAgeInMonth);
            params.put("petAgeInYear", petAgeInYear);
            params.put("genderOfPet", genderOfPet);
            params.put("descriptionOfPet", descriptionOfPet);
            params.put("email", email);
            params.put("method", method);
            params.put("format", format);
            params.put("id", petlistid);
        } catch (Exception e) {

        }
        JsonObjectRequest myListingModifyPetMateDetailsRequest = new JsonObjectRequest(Request.Method.POST, SERVER_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        try {
                            response.getString("saveModifiedPetMateDetailsResponse");
                            Toast.makeText(context, "Succefully Uploaded", Toast.LENGTH_LONG).show();
                            myListingModifyPetMateDetails.finish();
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
        });
        myListingModifyPetMateDetailsRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myListingModifyPetMateDetailsRequest);
        return null;
    }

}
