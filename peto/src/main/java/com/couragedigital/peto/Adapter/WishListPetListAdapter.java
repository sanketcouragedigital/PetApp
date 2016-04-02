package com.couragedigital.peto.Adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.WishListPetListDelete;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.WishListPetListItem;


import java.util.HashMap;
import java.util.List;


public class WishListPetListAdapter extends RecyclerView.Adapter<WishListPetListAdapter.ViewHolder> {
    private final OnRecyclerWishListPetDeleteClickListener onRecyclerWishListPetDeleteClickListener;
    List<WishListPetListItem> wishListPetListItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;
    String email;
    ProgressDialog progressDialog = null;

    public WishListPetListAdapter(List<WishListPetListItem> wishListPetListItems, OnRecyclerWishListPetDeleteClickListener onRecyclerWishListPetDeleteClickListener) {
        this.wishListPetListItems = wishListPetListItems;
        this.onRecyclerWishListPetDeleteClickListener = onRecyclerWishListPetDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlistpetlistitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WishListPetListItem itemList = wishListPetListItems.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return wishListPetListItems.size();
    }

    private String setListingType(WishListPetListItem wishListPetListItem) {
        String petListingTypeString = null;
        if (wishListPetListItem.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
        } else {
            petListingTypeString = "TO SELL";
        }
        return petListingTypeString;
    }

    public interface OnRecyclerWishListPetDeleteClickListener {

        void onRecyclerWishListPetDeleteClick(List<WishListPetListItem> wishListPetListItems, WishListPetListItem wishListPetListItem, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView petImage;
        public TextView petBreed;
        public TextView petAdoptOrSell;
        public Button deletebutton;
        public View dividerLine;
        public TextView nameForPetPost;
        public ExpandableText wishlistPetListDescription;
        public View cardView;
        private WishListPetListItem wishListPetListItem;

        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (ImageView) itemView.findViewById(R.id.wishlistViewImage);
            petBreed = (TextView) itemView.findViewById(R.id.wishlistViewBreed);
            nameForPetPost = (TextView) itemView.findViewById(R.id.petPostedBy);
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
            Glide.with(petImage.getContext()).load(wishListPetListItem.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(petImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(petImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    petImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            petBreed.setText(wishListPetListItem.getPetBreed());
            wishlistPetListDescription.setText(wishListPetListItem.getPetDescription());
            deletebutton.setText("Delete");
            nameForPetPost.setText("Posted By : " + wishListPetListItem.getName());
            petAdoptOrSell.setText(setListingType(wishListPetListItem));
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.wishlistPetListDelete) {
                onRecyclerWishListPetDeleteClickListener.onRecyclerWishListPetDeleteClick(wishListPetListItems, wishListPetListItem, this.getAdapterPosition());
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
                    wishListPetDetail.putExtra("NAME", wishListPetListItem.getName());

                    v.getContext().startActivity(wishListPetDetail);
                }
            }
        }
    }
}
