package com.couragedigital.peto.Connectivity;

import android.content.Context;
import com.couragedigital.peto.MyListingModifyPetDetails;
import com.couragedigital.peto.Singleton.URLInstance;
import org.json.JSONObject;

import java.util.Map;

import com.android.volley.*;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.app.AppController;
import org.json.JSONException;

public class MyListingModifyPetDetailUpload {

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
    private static String adoptionOfPet;
    private static Integer priceOfPet;
    private static String email;
    private static String method;
    private static String format;
    private static Integer petlistid;
    private static String modifyUpdatedResponse;


    private Response.Listener<JSONObject> listener;
    private Map<String, String> params;


    public static Void uploadToRemoteServer(String petCategoryName, String petBreedName, String petAgeMonthSpinner, String petAgeYearSpinner, String petGender, String petDescription, String petAdoption, Integer petPrice, String emailOfUser, Integer id, MyListingModifyPetDetails myListingModifyPetDetails) throws Exception {

        method = "saveModifiedPetDetails";
        format = "json";
        categoryOfPet = petCategoryName;
        breedOfPet = petBreedName;
        petAgeInMonth = petAgeMonthSpinner;
        petAgeInYear = petAgeYearSpinner;
        genderOfPet = petGender;
        descriptionOfPet = petDescription;
        adoptionOfPet = petAdoption;
        priceOfPet = petPrice;
        email = emailOfUser;
        petlistid = id;

        JSONObject params = new JSONObject();
        try {
            params.put("categoryOfPet", categoryOfPet);
            params.put("breedOfPet", breedOfPet);
            params.put("petAgeInMonth", petAgeInMonth);
            params.put("petAgeInYear", petAgeInYear);
            params.put("genderOfPet", genderOfPet);
            params.put("descriptionOfPet", descriptionOfPet);
            params.put("adoptionOfPet", adoptionOfPet);
            params.put("priceOfPet", String.valueOf(priceOfPet));
            params.put("email", email);
            params.put("method", method);
            params.put("format", format);
            params.put("id", petlistid);
        } catch (Exception e) {

        }
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, SERVER_URL, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        VolleyLog.v("Response:%n %s", response.toString());
                        try {
                            response.getString("saveModifiedPetDetailsResponse");
                            myListingModifyPetDetails.finish();
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
        return null;
    }
}
