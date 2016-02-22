package com.couragedigital.petapp.Connectivity;

import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.PetList;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetListItems;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class FilterFetchPetList {
    private static final String TAG = PetList.class.getSimpleName();
    private static List<String> filterSelectedCategoryLists;
    private static List<String> filterSelectedBreedLists;
    private static List<String> filterSelectedAgeLists;
    private static List<String> filterSelectedGenderLists;
    private static List<String> filterSelectedAdoptionAndPriceLists;
    private static String method;
    private static String format;

    //http://storage.couragedigital.com/dev/api/petappapi.php
    public static void filterFetchPetList(List<PetListItems> petLists, RecyclerView.Adapter adapter, String email, int current_page, List<String> filterCategoryListInstance, List<String> filterBreedListInstance, List<String> filterAgeListInstance, List<String> filterGenderListInstance, List<String> filterAdoptionAndPriceListInstance) {
        String url = URLInstance.getUrl();
        filterSelectedCategoryLists = filterCategoryListInstance;
        filterSelectedBreedLists = filterBreedListInstance;
        filterSelectedAgeLists = filterAgeListInstance;
        filterSelectedGenderLists = filterGenderListInstance;
        filterSelectedAdoptionAndPriceLists = filterAdoptionAndPriceListInstance;
        method = "filterPetList";
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
        JSONArray arrayOfSelectedFilterAdoptionAndPrice = new JSONArray(filterSelectedAdoptionAndPriceLists);
        params.put("filterSelectedCategories", arrayOfSelectedFilterCategories.toString());
        params.put("filterSelectedBreeds", arrayOfSelectedFilterBreeds.toString());
        params.put("filterSelectedAge", arrayOfSelectedFilterAge.toString());
        params.put("filterSelectedGender", arrayOfSelectedFilterGender.toString());
        params.put("filterSelectedAdoptionAndPrice", arrayOfSelectedFilterAdoptionAndPrice.toString());

        JsonObjectRequest petFilterCategoryReq = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(current_page == 1) {
                                petLists.clear();
                                adapter.notifyDataSetChanged();
                            }
                            JSONArray jsonArray = response.getJSONArray("showPetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetListItems petListItems = new PetListItems();
                                    petListItems.setPetBreed(replaceSpecialChars(obj.getString("pet_breed")));
                                    petListItems.setPetPostOwner(replaceSpecialChars(obj.getString("name")));
                                    petListItems.setFirstImagePath(obj.getString("first_image_path"));
                                    petListItems.setSecondImagePath(obj.getString("second_image_path"));
                                    petListItems.setThirdImagePath(obj.getString("third_image_path"));
                                    if(!obj.getString("pet_adoption").equals("")) {
                                        petListItems.setListingType(replaceSpecialChars(obj.getString("pet_adoption")));
                                    }
                                    else if(!obj.getString("pet_price").equals("")) {
                                        petListItems.setListingType(replaceSpecialChars(obj.getString("pet_price")));
                                    }
                                    petListItems.setPetCategory(replaceSpecialChars(obj.getString("pet_category")));
                                    petListItems.setPetAgeInYear(obj.getString("pet_age_inYear"));
                                    petListItems.setPetAgeInMonth(obj.getString("pet_age_inMonth"));
                                    petListItems.setPetGender(replaceSpecialChars(obj.getString("pet_gender")));
                                    petListItems.setPetDescription(replaceSpecialChars(obj.getString("pet_description")));
                                    petListItems.setPetPostDate(obj.getString("post_date"));
                                    petListItems.setPetPostOwnerEmail(replaceSpecialChars(obj.getString("email")));
                                    petListItems.setPetPostOwnerMobileNo(obj.getString("mobileno"));

                                    // adding pet to pets array
                                    petLists.add(petListItems);
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petFilterCategoryReq);
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]"," ");
        return str;
    }
}
