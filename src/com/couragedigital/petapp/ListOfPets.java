package com.couragedigital.petapp;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.petapp.adapter.CustomListAdapter;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

public class ListOfPets extends BaseActivity {

    private static final String TAG = ListOfPets.class.getSimpleName();

    private static final String url = "http://storage.couragedigital.com/dev/api/petappapi.php?method=showPetDetails&format=json";
    private ProgressDialog progressDialog;
    private List<PetList> petLists = new ArrayList<PetList>();
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listofpets);

        listView = (ListView) findViewById(R.id.petList);
        adapter = new CustomListAdapter(this, petLists);
        listView.setAdapter(adapter);

        progressDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        progressDialog.setMessage("Fetching List Of Pets...");
        progressDialog.show();

        // Creating volley request obj
        JsonObjectRequest petListReq = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, response.toString());
                        hideProgressDialog();
                        try {
                            JSONArray jsonArray = response.getJSONArray("showPetDetailsResponse");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {

                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    PetList petList = new PetList();
                                    petList.setPetBreedOrigin(obj.getString("pet_breed"));
                                    petList.setImage_path(obj.getString("image_path"));

                                    // adding movie to movies array
                                    petLists.add(petList);

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
                        hideProgressDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(petListReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}