package com.couragedigital.peto.Connectivity;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.couragedigital.peto.Campaign_List_ForAll;
import com.couragedigital.peto.DialogBox.EmptyListDialoge;
import com.couragedigital.peto.DialogBox.TimeOut_DialogeBox;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.CampaignListItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Campaign_ShowList_ForAll {

    private static final String TAG = MyOrderFetchList.class.getSimpleName();
    private static Context context;
    private static String detailsResponse;
    private static String urlContact = URLInstance.getUrl();

    public Campaign_ShowList_ForAll(Campaign_List_ForAll campaign_List_ForAll) {
        context = campaign_List_ForAll;
    }

    public static List campaignFetchListForAll(final List<CampaignListItem> campaignLists, final RecyclerView.Adapter adapter, String url, final ProgressDialog progressDialog) {

        JsonObjectRequest campaignFetchReqForall = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("showCampaignDetailsForAllResponse");
                            if (jsonArray.length()==0) {
                                Intent gotoEmptyList = new Intent(context, EmptyListDialoge.class);
                                context.startActivity(gotoEmptyList);
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject obj = jsonArray.getJSONObject(i);
                                    CampaignListItem campaignListItem = new CampaignListItem();

                                    campaignListItem.setCollectedAmount(obj.getString("collectedAmount"));
                                    campaignListItem.setRemainingAmount(obj.getString("remainingAmount"));
                                    campaignListItem.setActualAmount(obj.getString("actualAmount"));
                                    campaignListItem.setEmail(obj.getString("ngo_email"));
                                    campaignListItem.setId(obj.getString("campaign_id"));
                                    campaignListItem.setCampaignName(obj.getString("campaignName"));
                                    campaignListItem.setNgoName(obj.getString("ngo_name"));
                                    campaignListItem.setDescription(obj.getString("description"));
                                    campaignListItem.setMinimumAmount(obj.getString("minimumAmount"));
                                    campaignListItem.setLastDate(obj.getString("lastDate"));
                                    campaignListItem.setPostDate(obj.getString("postDate"));
                                    campaignListItem.setNgo_url(obj.getString("ngo_url"));
                                    campaignListItem.setMobileno(obj.getString("mobileno"));
                                    campaignListItem.setFirstImagePath(obj.getString("first_image_path"));
                                    if (!obj.getString("second_image_path").isEmpty() && obj.getString("second_image_path") != null) {
                                        campaignListItem.setSecondImagePath(obj.getString("second_image_path"));
                                    }
                                    if (!obj.getString("third_image_path").isEmpty() && obj.getString("third_image_path") != null) {
                                        campaignListItem.setThirdImagePath(obj.getString("third_image_path"));
                                    }

                                    campaignLists.add(campaignListItem);
                                    adapter.notifyDataSetChanged();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.hide();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                progressDialog.hide();
                Intent gotoTimeOutError = new Intent(context, TimeOut_DialogeBox.class);
                context.startActivity(gotoTimeOutError);
            }
        });
        AppController.getInstance().addToRequestQueue(campaignFetchReqForall);
        return campaignLists;
    }

    public static String replaceSpecialChars(String str) {
        str = str.replaceAll("[+]", " ");
        return str;
    }
}