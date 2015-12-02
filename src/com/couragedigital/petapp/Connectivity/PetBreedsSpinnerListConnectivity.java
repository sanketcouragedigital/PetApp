package com.couragedigital.petapp.Connectivity;

import android.widget.Spinner;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.adapter.SpinnerItemsAdapter;
import com.couragedigital.petapp.app.AppController;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PetBreedsSpinnerListConnectivity {

    private static final String TAG = PetBreedsSpinnerListConnectivity.class.getSimpleName();

    public static List fetchPetBreeds(List petBreedsList, String petCategoryName, SpinnerItemsAdapter adapter) {
        String url = "http://storage.couragedigital.com/dev/api/petappapi.php?petCategory=" + petCategoryName + "&method=showPetBreedsAsPerPetCategory&format=json";
        JsonObjectRequest petCategoryReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        petBreedsList.removeAll(petBreedsList);
                        adapter.notifyDataSetChanged();
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetBreedsResponse");
                            petBreedsList.add("Select Pet Breed");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);

                                    petBreedsList.add(obj.optString("pet_breed"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(petCategoryReq);
        return petBreedsList;
    }
}
