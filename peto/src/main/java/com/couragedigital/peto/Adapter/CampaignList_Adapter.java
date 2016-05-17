package com.couragedigital.peto.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.CampaignDelete;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.ContactNoInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.CampaignListItem;


import java.util.HashMap;
import java.util.List;

public class CampaignList_Adapter extends RecyclerView.Adapter<CampaignList_Adapter.ViewHolder>{
   // private final OnRecyclerCampaignListDeleteClickListener onRecyclerCampaignListDeleteClickListener;

    List<CampaignListItem> campaignListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    String campaignIdtxt;
    String campaignNametxt;
    String ngoNametxt;
    String ngoEmailtxt;
    String lastDatetxt;
    String mobileNotxt;

    String userEmail;

    public CampaignList_Adapter(List<CampaignListItem> campaignListItem) {
        this.campaignListItem = campaignListItem;
    }
//    public CampaignList_Adapter(List<CampaignListItem> campaignListItem,OnRecyclerCampaignListDeleteClickListener onRecyclerCampaignListDeleteClickListener) {
//        this.campaignListItem = campaignListItem;
//        this.onRecyclerCampaignListDeleteClickListener =onRecyclerCampaignListDeleteClickListener;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.campaign_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        userEmail = user.get(SessionManager.KEY_EMAIL);

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
//    public interface OnRecyclerWishListPetDeleteClickListener {
//
//        void onRecyclerCampaignListDeleteClick(List<CampaignListItem> campaignListItems, CampaignListItem campaignListItem, int position);
//    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView campaignProductImage;
        public TextView ngoName;
        public TextView campaignName;
        public TextView campaignLastDate;
        public View dividerLine;
        public View cardView;
        public ExpandableText campaignDescription;
        private CampaignListItem CampaignListItem;
        Button modifyCampaignButton;
        Button modifyCampaignDeletebtn;


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
            modifyCampaignButton = (Button)itemView.findViewById(R.id.campaignModify);
            modifyCampaignDeletebtn = (Button)itemView.findViewById(R.id.campaignDeletebtn);
            cardView = itemView;
            cardView.setOnClickListener(this);
            modifyCampaignButton.setOnClickListener(this);
            modifyCampaignDeletebtn.setOnClickListener(this);
        }

        public void bindCampaignList(CampaignListItem CampaignListItem) {
            this.CampaignListItem = CampaignListItem;
            Glide.with(campaignProductImage.getContext()).load(CampaignListItem.getFirstImagePath()).centerCrop().crossFade().into(campaignProductImage);
            campaignName.setText(CampaignListItem.getNgoName());
            ngoName.setText(CampaignListItem.getCampaignName());
            campaignLastDate.setText(CampaignListItem.getLastDate());
            campaignDescription.setText(CampaignListItem.getDescription());

             campaignIdtxt = CampaignListItem.getId();
             campaignNametxt = CampaignListItem.getCampaignName();
             ngoNametxt = CampaignListItem.getNgoName();
             ngoEmailtxt = CampaignListItem.getEmail();
             lastDatetxt = CampaignListItem.getLastDate();
            mobileNotxt = CampaignListItem.getMobileno();

            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.campaignModify) {
                Intent campaignListFullInformation = new Intent(v.getContext(), Campaign_Modify.class);
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
                campaignListFullInformation.putExtra("CAMPAIGN_COLLECTED_AMOUNT", CampaignListItem.getCollectedAmount());
                campaignListFullInformation.putExtra("CAMPAIGN_REMAINING_AMOUNT", CampaignListItem.getRemainingAmount());
                campaignListFullInformation.putExtra("NGO_URL", CampaignListItem.getNgo_url());
                campaignListFullInformation.putExtra("MOBILE_NO", CampaignListItem.getMobileno());

                v.getContext().startActivity(campaignListFullInformation);
            }
            else if(v.getId() == R.id.campaignDeletebtn) {
                AlertDialog diaBox = AskOption(v);
                diaBox.show();
                /*try {

                    CampaignDelete.campaignDeleteFromServer(campaignIdtxt, campaignNametxt,ngoNametxt,ngoEmailtxt,lastDatetxt,userEmail,mobileNotxt,);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
            }
            else {
                if (this.CampaignListItem != null) {
                    Intent campaignListFullInformation = new Intent(v.getContext(), Campaign_List_Details.class);
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
                    campaignListFullInformation.putExtra("CAMPAIGN_COLLECTED_AMOUNT", CampaignListItem.getCollectedAmount());
                    campaignListFullInformation.putExtra("CAMPAIGN_REMAINING_AMOUNT", CampaignListItem.getRemainingAmount());
                    campaignListFullInformation.putExtra("NGO_URL", CampaignListItem.getNgo_url());
                    campaignListFullInformation.putExtra("MOBILE_NO", CampaignListItem.getMobileno());

                    v.getContext().startActivity(campaignListFullInformation);
                }
            }
        }
        private AlertDialog AskOption(View v) {
            final View view = v;
            AlertDialog myQuittingDialogBox =new AlertDialog.Builder(v.getContext())
                    //set message, title, and icon
                    .setTitle("Confirmation")
                    .setMessage("Do you want to Delete")
//                    .setIcon(R.drawable.delete)
                    .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //your deleting code
                            modifyCampaignDeletebtn.setEnabled(false);
                            try {
                                CampaignDelete campaignDelete = new CampaignDelete(view.getContext());
                                campaignDelete.campaignDeleteFromServer(campaignIdtxt, campaignNametxt,ngoNametxt,ngoEmailtxt,lastDatetxt,userEmail,mobileNotxt);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();


                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create();
            return myQuittingDialogBox;
        }
    }
}
