package com.couragedigital.peto.Connectivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.DialogBox.NullRespone_DialogeBox;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.PetMateList;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.PetMateListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class FilterFetchPetMateList {
    private static final String TAG = PetMateList.class.getSimpleName();
    private static List<String> filterSelectedCategoryLists;
    private static List<String> filterSelectedBreedLists;
    private static List<String> filterSelectedAgeLists;
    private static List<String> filterSelectedGenderLists;
    private static String method;
    private static String format;
    private static Context context;

    public FilterFetchPetMateList(PetMateList petMateList) {
        this.context = petMateList;
    }

    //http://storage.couragedigital.com/dev/api/petappapi.php
    public static void filterFetchPetMateList(final List<PetMateListItems> petMateLists, final RecyclerView.Adapter adapter, String email, final int current_page, List<String> filterCategoryListInstance, List<String> filterBreedListInstance, List<String> filterAgeListInstance, List<String> filterGenderListInstance) {
        String url = URLInstance.getUrl();
        filterSelectedCategoryLists = filterCategoryListInstance;
        filterSelectedBreedLists = filterBreedListInstance;
        filterSelectedAgeLists = filterAgeListInstance;
        filterSelectedGenderLists = filterGenderListInstance;
        method = "filterPetMateList";
        format = "json";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("method", method);
        params.put("format", format);
        params.put("email", email);
        params.put("currentPage", String.valueOf(current_page));
        JSONArray arrayOfSelectedFilterCategories = new JSONArray(filterSelectedCategoryLists);
        JSONArray arrayOfSelectedFilterBreeds = new JSONArray(filterSelectedBreedLists);
        JSONArray arrayOfSelectedFilterAge = new JSONArray(filterSelectedAgeLists);
        JSONArray arrayOfSelectedFilterGender = new JSONArray(filterSelectedGenderLists);
        params.put("filterSelectedCategories", arrayOfSelectedFilterCategories.toString());
        params.put("filterSelectedBreeds", arrayOfSelectedFilterBreeds.toString());
        params.put("filterSelectedAge", arrayOfSelectedFilterAge.toString());
        params.put("filterSelectedGender", arrayOfSelectedFilterGender.toString());

        JsonObjectRequest petFilterCategoryReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(current_page == 1) {
                                petMateLists.clear();
                                adapter.notifyDataSetChanged();
                            }
                            JSONArray jsonArray = response.getJSONArray("showPetMateDetailsResponse");
                            if(jsonArray.length()!=0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    try {
                                        JSONObject obj = jsonArray.getJSONObject(i);
                                        PetMateListItems petMateListItems = new PetMateListItems();
                                        petMateListItems.setPetMateBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                        petMateListItems.setPetMatePostOwner(replaceSpecialChars(obj.getString("name")));
                                        petMateListItems.setFirstImagePath(obj.getString("first_image_path"));
                                        petMateListItems.setSecondImagePath(obj.getString("second_image_path"));
                                        petMateListItems.setThirdImagePath(obj.getString("third_image_path"));
                                        petMateListItems.setPetMateCategory(replaceSpecialChars(obj.getString("pet_category")));
                                        petMateListItems.setPetMateAgeInYear(obj.getString("pet_age_inYear"));
                                        petMateListItems.setPetMateAgeInMonth(obj.getString("pet_age_inMonth"));
                                        petMateListItems.setPetMateGender(replaceSpecialChars(obj.getString("pet_gender")));
                                        petMateListItems.setPetMateDescription(replaceSpecialChars(obj.getString("pet_description")));
                                        petMateListItems.setPetMatePostDate(obj.getString("post_date"));
                                        petMateListItems.setPetMatePostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                        petMateListItems.setPetMatePostOwnerMobileNo(obj.getString("mobileno"));
                                        // adding pet to pets array
                                        petMateLists.add(petMateListItems);
                                        adapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }else{
                                Intent gotoNullError = new Intent(context, NullRespone_DialogeBox.class);
                                context.startActivity(gotoNullError);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(petFilterCategoryReq);
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
