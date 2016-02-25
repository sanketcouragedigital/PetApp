package com.couragedigital.petapp.Connectivity;


import android.content.Intent;
import android.view.View;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.PetClinicDetails;
import com.couragedigital.petapp.Singleton.ClinicReviewInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicReviewsListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShowClinicFeedback {

    private static String method;
    private static String format;
    private static String clinicId ;
    private static String nameOfClinic;
    private static String clinicImage;
    private static String addressOfClinic;
    private static String email;
    private static String contactNo;
    private static String clinicTiming;
    private static int currentPage=1;
    public static String current_page;
    private static final String TAG = PetFetchList.class.getSimpleName();
    private  static  String clinicReviews;


    //public static List showClinicReviews(List<ClinicReviewsListItems> clinicReviewsList, RecyclerView.Adapter adapter, String url) {

    public static String showClinicReviews(String clinicIdForReviews, View view, List<ClinicReviewsListItems> clinicReviewsList,String clinicName,String clinicImage_path,String clinicAddress,String Email,String ClinicContact,String clinicNotes) {
        current_page=String.valueOf(currentPage);
        clinicId = clinicIdForReviews;
        nameOfClinic = clinicName;
        clinicImage = clinicImage_path;
        addressOfClinic = clinicAddress;
        email = Email;
        contactNo = ClinicContact;
        clinicTiming = clinicNotes;

        //String url= "http://192.168.0.7/PetAppAPI/api/petappapi.php?method=showClinicReviews&format=json&currentPage=" + current_page + "&clinicId=" + clinicId + "";
        String url= "http://storage.couragedigital.com/dev/api/petappapi.php?method=showClinicReviews&format=json&currentPage=" + current_page + "&clinicId=" + clinicId + "";
        // String url = URLInstance.getUrl();
        method = "showClinicReviews";
        format = "json";



        JsonObjectRequest clinicReviewsListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showClinicReviewssResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    ClinicReviewsListItems clinicReviewsListItems = new ClinicReviewsListItems();
                                    clinicReviewsListItems.setEmail(obj.getString("name"));
                                    clinicReviewsListItems.setClinicRatings(obj.getString("ratings"));
                                    clinicReviewsListItems.setClinicReviews(obj.getString("reviews"));
                                   // clinicReviewsListItems.setTime(obj.getString("time"));

                                    clinicReviewsList.add(clinicReviewsListItems);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            ClinicReviewInstance clinicReviewInstance = new ClinicReviewInstance();
                            clinicReviewInstance.setClinicReviewsListItemsArrayList(clinicReviewsList);

                                Intent clinicInformation = new Intent(view.getContext(), PetClinicDetails.class);
                                clinicInformation.putExtra("CLINIC_ID",clinicId );
                                clinicInformation.putExtra("CLINIC_NAME",nameOfClinic);
                                clinicInformation.putExtra("CLINIC_IMAGE",clinicImage);
                                clinicInformation.putExtra("CLINIC_ADDRESS",addressOfClinic);
                                clinicInformation.putExtra("DOCTOR_NAME",email);
                                clinicInformation.putExtra("DOCTOR_CONTACT",contactNo);
                                clinicInformation.putExtra("CLINIC_NOTES",clinicTiming );
                                view.getContext().startActivity(clinicInformation);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error: " + error.getMessage());
                //progressDialog.hide();
            }
        });
        AppController.getInstance().addToRequestQueue(clinicReviewsListReq);
        return clinicReviews;
    }
}
