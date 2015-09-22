package com.couragedigital.petapp;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.toolbox.JsonArrayRequest;
import com.couragedigital.petapp.adapter.CustomListAdapter;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;

public class ListOfPets extends Activity {

    private static final String TAG = ListOfPets.class.getSimpleName();

    private static final String url = "http://192.168.0.4/petapp.php";
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

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest petListReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hideProgressDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                PetList petList = new PetList();
                                petList.setPetBreedOrigin(obj.getString("petBreedOrigin"));
                                petList.setImage_path(obj.getString("image_path"));

                                // adding movie to movies array
                                petLists.add(petList);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

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