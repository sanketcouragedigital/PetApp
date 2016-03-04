package com.couragedigital.petapp.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.*;
import com.couragedigital.petapp.Connectivity.WishListPetListDelete;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.WishListPetListItem;


import java.util.HashMap;
import java.util.List;


public class WishListPetListAdapter extends RecyclerView.Adapter<WishListPetListAdapter.ViewHolder> {
    List<WishListPetListItem> wishListPetListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;
    String petListId;
    String email;
    ProgressDialog progressDialog = null;
    public WishListPetListAdapter() {
    }
    public WishListPetListAdapter(List<WishListPetListItem> petLists) {
        this.wishListPetListItem = petLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlistpetlistitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WishListPetListItem itemList = wishListPetListItem.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return wishListPetListItem.size();
    }

    private String setListingType(WishListPetListItem wishListPetListItem) {
        String petListingTypeString = null;
        if(wishListPetListItem.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
        }
        else {
            petListingTypeString = "TO SELL";
        }
        return petListingTypeString;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView petImage;
        public TextView petBreed;
        public TextView petAdoptOrSell;
        public Button deletebutton;
        public View dividerLine;
        public ExpandableText wishlistPetListDescription;
        public View cardView;
        private WishListPetListItem wishListPetListItem;

        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (RoundedNetworkImageView) itemView.findViewById(R.id.wishlistViewImage);
            petBreed = (TextView) itemView.findViewById(R.id.wishlistViewBreed);
            petAdoptOrSell = (TextView) itemView.findViewById(R.id.wishlistPetAdoptOrSell);
            dividerLine = itemView.findViewById(R.id.wishlistDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.wishlistPetListDelete);
            wishlistPetListDescription = (ExpandableText) itemView.findViewById(R.id.wishlistPetListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }
        public void bindPetList(WishListPetListItem wishListPetListItem) {
            this.wishListPetListItem = wishListPetListItem;
            petImage.setImageUrl(wishListPetListItem.getFirstImagePath(), imageLoader);
            petBreed.setText(wishListPetListItem.getPetBreed());
            wishlistPetListDescription.setText(wishListPetListItem.getPetDescription());
            deletebutton.setText("Delete");
            petAdoptOrSell.setText(setListingType(wishListPetListItem));
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
            //petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //petFavourite.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.wishlistPetListDelete) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                email = user.get(SessionManager.KEY_EMAIL);
                petListId= String.valueOf(wishListPetListItem.getId());

                if (this.wishListPetListItem != null) {
//                    String url = URLInstance.getUrl();
//                    int id = wishListPetListItem.getId();
//                    String email = wishListPetListItem.getPetPostOwnerEmail();
//                    url = url + "?method=deleteWishListPetList&format=json&id=" + id + "&email=" + email + "";
                    new DeletePetListFromServer().execute();
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
//                    try {
//                        WishListPetListDelete.deleteWishListPetListFromServer(email,petListId);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        progressDialog.dismiss();
//                    }
                }
            } else {
                if (this.wishListPetListItem != null) {
                    Intent wishListPetDetail = new Intent(v.getContext(), WishListPetListDetails.class);
                    wishListPetDetail.putExtra("ID", wishListPetListItem.getId());
                    wishListPetDetail.putExtra("PET_FIRST_IMAGE", wishListPetListItem.getFirstImagePath());
                    wishListPetDetail.putExtra("PET_SECOND_IMAGE", wishListPetListItem.getSecondImagePath());
                    wishListPetDetail.putExtra("PET_THIRD_IMAGE", wishListPetListItem.getThirdImagePath());
                    wishListPetDetail.putExtra("PET_BREED", wishListPetListItem.getPetBreed());
                    wishListPetDetail.putExtra("PET_LISTING_TYPE", wishListPetListItem.getListingType());
                    wishListPetDetail.putExtra("PET_AGE_IN_MONTH", wishListPetListItem.getPetAgeInMonth());
                    wishListPetDetail.putExtra("PET_AGE_IN_YEAR", wishListPetListItem.getPetAgeInYear());
                    wishListPetDetail.putExtra("PET_GENDER", wishListPetListItem.getPetGender());
                    wishListPetDetail.putExtra("PET_DESCRIPTION", wishListPetListItem.getPetDescription());
                    wishListPetDetail.putExtra("ALTERNATE_NO", wishListPetListItem.getAlternateNo());

                    v.getContext().startActivity(wishListPetDetail);
                }
            }
        }

        public class DeletePetListFromServer extends AsyncTask<String, String, String> {
            //String urlForFetch;
            @Override
            protected String doInBackground(String... url) {
                try {
                    //urlForFetch = url[0];
                    WishListPetListDelete.deleteWishListPetListFromServer(email,petListId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}
