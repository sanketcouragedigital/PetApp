package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.Campaign_List_Details;
import com.couragedigital.peto.Campaign_List_ForAll_Details;
import com.couragedigital.peto.ExpandableText;
import com.couragedigital.peto.R;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.ContactNoInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.CampaignListItem;

import java.util.HashMap;
import java.util.List;

public class CampaignList_ForAll_Adapter extends RecyclerView.Adapter<CampaignList_ForAll_Adapter.ViewHolder>{

    List<CampaignListItem> campaignListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    String email;

    public CampaignList_ForAll_Adapter(List<CampaignListItem> campaignListItem) {
        this.campaignListItem = campaignListItem;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.campaign_list_forall_items, viewGroup, false);
        viewHolder = new ViewHolder(v);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CampaignListItem CampaignListItem = campaignListItem.get(i);
        viewHolder.bindCampaignList(CampaignListItem);
    }

    @Override
    public int getItemCount() {
        return campaignListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView campaignProductImage;
        public TextView ngoName;
        public TextView campaignName;
        public TextView campaignLastDate;
        public View dividerLine;
        public View cardView;
        public ExpandableText campaignDescription;
        private CampaignListItem CampaignListItem;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            campaignProductImage = (ImageView) itemView.findViewById(R.id.campaignImage );
            campaignName = (TextView) itemView.findViewById(R.id.campaignName);
            ngoName = (TextView) itemView.findViewById(R.id.ngoName);
            dividerLine = itemView.findViewById(R.id.campaignListDividerLine);
            campaignLastDate = (TextView) itemView.findViewById(R.id.campaignLastDate);
            campaignDescription = (ExpandableText) itemView.findViewById(R.id.campaignDescription);
            cardView = itemView;
            cardView.setOnClickListener(this);

        }
        public void bindCampaignList(CampaignListItem CampaignListItem) {
            this.CampaignListItem = CampaignListItem;
            Glide.with(campaignProductImage.getContext()).load(CampaignListItem.getFirstImagePath()).centerCrop().crossFade().into(campaignProductImage);
            campaignName.setText(CampaignListItem.getNgoName());
            ngoName.setText(CampaignListItem.getCampaignName());
            campaignLastDate.setText(CampaignListItem.getLastDate());
            campaignDescription.setText(CampaignListItem.getDescription());

            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {

            if (this.CampaignListItem != null) {
                Intent campaignListFullInformation = new Intent(v.getContext(), Campaign_List_ForAll_Details.class);
                campaignListFullInformation.putExtra("CAMPAIGN_ID", CampaignListItem.getId());
                campaignListFullInformation.putExtra("CAMPAIGN_FIRST_IMAGE", CampaignListItem.getFirstImagePath());
                campaignListFullInformation.putExtra("CAMPAIGN_SECOND_IMAGE", CampaignListItem.getSecondImagePath());
                campaignListFullInformation.putExtra("CAMPAIGN_THIRD_IMAGE", CampaignListItem.getThirdImagePath());
                campaignListFullInformation.putExtra("CAMPAIGN_NGO_NAME", CampaignListItem.getNgoName());
                campaignListFullInformation.putExtra("CAMPAIGN_NAME", CampaignListItem.getCampaignName());
                campaignListFullInformation.putExtra("CAMPAIGN_DESCRIPTION", CampaignListItem.getDescription());
                campaignListFullInformation.putExtra("CAMPAIGN_ACTUAL_AMOUNT", CampaignListItem.getActualAmount());
                campaignListFullInformation.putExtra("CAMPAIGN_MINIMUM_AMOUNT", CampaignListItem.getMinimumAmount());
                campaignListFullInformation.putExtra("CAMPAIGN_LAST_DATE", CampaignListItem.getLastDate());
                campaignListFullInformation.putExtra("CAMPAIGN_POST_DATE", CampaignListItem.getPostDate());
                campaignListFullInformation.putExtra("CAMPAIGN_OWNER_EMAIL", CampaignListItem.getEmail());
                campaignListFullInformation.putExtra("NGO_URL", CampaignListItem.getNgo_url());

                v.getContext().startActivity(campaignListFullInformation);
            }
        }
    }
}