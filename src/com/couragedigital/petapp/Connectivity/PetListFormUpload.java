package com.couragedigital.petapp.Connectivity;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.android.volley.*;
import com.couragedigital.petapp.PetForm;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public  class PetListFormUpload {
    private static final String SERVER_URL = URLInstance.getUrl();
    private static Context context;
    private static Map<String, String> headerPart;
    private static Map<String, File> filePartData;
    private static Map<String, String> stringPart;
    public static PetForm petFormActivity;
    //http://192.168.0.3/PetAppAPI/api/petappapi.php
    //http://storage.couragedigital.com/dev/api/petappapi.php

    public static void uploadToRemoteServer(String petCategoryName, String petBreedName, String petAgeMonthSpinner, String petAgeYearSpinner, String petGender, String petDescription, String petAdoption, Integer petPrice, String firstImagePath, String secondImagePath, String thirdImagePath, String emailOfUser, PetForm petForm) throws Exception {

        petFormActivity = petForm;
        context = petForm.getApplicationContext();
        int serverResponseCode = 0;
        String categoryOfPet = petCategoryName;
        String breedOfPet = petBreedName;
        //Integer ageOfPet = petAge;
        String petAgeInMonth= petAgeMonthSpinner;
        String petAgeInYear= petAgeYearSpinner;
        String genderOfPet = petGender;
        String descriptionOfPet = petDescription;
        String adoptionOfPet = petAdoption;
        Integer priceOfPet = petPrice;
        String firstPetImage = firstImagePath;
        String secondPetImage = secondImagePath;
        String thirdPetImage = thirdImagePath;
        String email = emailOfUser;
        String method = "savePetDetails";
        String format = "json";

        //Auth header
        headerPart = new HashMap<>();
        headerPart.put("Content-type", "multipart/form-data;");

        //File part
        filePartData = new HashMap<>();
        filePartData.put("firstPetImage", new File(firstPetImage));
        if(!secondPetImage.isEmpty() && secondPetImage != null) {
            filePartData.put("secondPetImage", new File(secondPetImage));
        }
        if(!thirdPetImage.isEmpty() && thirdPetImage != null) {
            filePartData.put("thirdPetImage", new File(thirdPetImage));
        }

        //String part
        stringPart = new HashMap<>();
        stringPart.put("categoryOfPet", categoryOfPet);
        stringPart.put("breedOfPet", breedOfPet);
        //stringPart.put("ageOfPet", String.valueOf(ageOfPet));
        stringPart.put("petAgeInMonth", petAgeInMonth);
        stringPart.put("petAgeInYear", petAgeInYear);
        stringPart.put("genderOfPet", genderOfPet);
        stringPart.put("descriptionOfPet", descriptionOfPet);
        stringPart.put("adoptionOfPet", adoptionOfPet);
        stringPart.put("priceOfPet", String.valueOf(priceOfPet));
        stringPart.put("email", email);
        stringPart.put("method", method);
        stringPart.put("format", format);

        new UploadToServerCustomRequest().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static class UploadToServerCustomRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            CustomMultipartRequest mCustomRequest = new CustomMultipartRequest(Request.Method.POST, context, SERVER_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(context, "Succefully Uploaded", Toast.LENGTH_LONG).show();
                    petFormActivity.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "Error Uploading Form", Toast.LENGTH_LONG).show();
                }
            }, filePartData, stringPart, headerPart);
            AppController.getInstance().addToRequestQueue(mCustomRequest);
            return null;
        }
    }
}
