package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetMate;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import org.json.JSONObject;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PetMateFormUpload {
    private static final String SERVER_URL = URLInstance.getUrl();
    private static Context context;
    private static Map<String, String> headerPart;
    private static Map<String, File> filePartData;
    private static Map<String, String> stringPart;
    public static PetMate petMateActivity;

    public static void uploadToRemoteServer(String emailforlatlong, String petCategoryName, String petBreedName,String petMateAgeMonthSpinner,String petMateAgeYearSpinner, String petGender, String petDescription, String firstImagePath, String secondImagePath, String thirdImagePath, String txtAlternateNo, PetMate petMate) throws Exception {

        petMateActivity = petMate;
        context = petMate.getApplicationContext();
        int serverResponseCode = 0;
        String categoryOfPet = petCategoryName;
        String breedOfPet = petBreedName;
        //Integer ageOfPet = petAge;
        String petMateAgeInMonth= petMateAgeMonthSpinner;
        String petMateAgeInYear= petMateAgeYearSpinner;
        String genderOfPet = petGender;
        String descriptionOfPet = petDescription;
        String firstPetImage = firstImagePath;
        String secondPetImage = secondImagePath;
        String thirdPetImage = thirdImagePath;
        String email = emailforlatlong;
        String alternateNo=txtAlternateNo;
        String method = "savePetMateDetails";
        String format = "json";

        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

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
        stringPart.put("petAgeInMonth", petMateAgeInMonth);
        stringPart.put("petAgeInYear", petMateAgeInYear);
        stringPart.put("genderOfPet", genderOfPet);
        stringPart.put("descriptionOfPet", descriptionOfPet);
        stringPart.put("email", email);
        stringPart.put("alternateNo", alternateNo);
        stringPart.put("deviceId", android_id);
        stringPart.put("method", method);
        stringPart.put("format", format);

        new UploadToServerCustomRequest().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

    public static class UploadToServerCustomRequest extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            CustomMultipartRequest petMateFormUploadCustomRequest = new CustomMultipartRequest(Request.Method.POST, context, SERVER_URL, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    Toast.makeText(context, "Succefully Uploaded", Toast.LENGTH_LONG).show();
                    petMateActivity.finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {
                    Toast.makeText(context, "Error Uploading Form", Toast.LENGTH_LONG).show();
                    Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                   context.startActivity(gotoTimeOutError);
                }
            }, filePartData, stringPart, headerPart);
            petMateFormUploadCustomRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(petMateFormUploadCustomRequest);
            return null;
        }
    }
}
