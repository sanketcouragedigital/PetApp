package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.WishListPetListAdd;
import com.couragedigital.peto.Connectivity.WishListPetListDelete;
import com.couragedigital.peto.Holder.AdMobViewHolder;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.UserPetListWishList;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.PetListItems;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PetListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    private final OnRecyclerPetListShareClickListener onRecyclerPetListShareClickListener;
    List<PetListItems> petLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    RecyclerView.ViewHolder viewHolder;

    public PetListAdapter(List<PetListItems> petLists, OnRecyclerPetListShareClickListener onRecyclerPetListShareClickListener) {
        this.petLists = petLists;
        this.onRecyclerPetListShareClickListener = onRecyclerPetListShareClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == CONTENT_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistsubitems, viewGroup, false);
            viewHolder = new ViewHolder(v);
        }
        else if (i == AD_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adnativeexpresslistitem, viewGroup, false);
            viewHolder = new AdMobViewHolder(v);
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder.getItemViewType() == CONTENT_TYPE) {
            PetListItems petListItems = petLists.get(i);
            ViewHolder viewHolderForPetList = (ViewHolder) viewHolder;
            viewHolderForPetList.bindPetList(petListItems);
        }
        else if(viewHolder.getItemViewType() == AD_TYPE) {
            AdMobViewHolder viewHolderForAdMob = (AdMobViewHolder) viewHolder;
            viewHolderForAdMob.bindAds();
        }
    }

    @Override
    public int getItemCount() {
        return petLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (petLists.get(position).getPetCategory() == null && petLists.get(position).getPetBreed() == null) {
            return AD_TYPE;
        }
        else {
            return CONTENT_TYPE;
        }
    }

    private String setListingType(PetListItems petListItems) {
        String petListingTypeString = null;
        if(petListItems.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
        }
        else {
            petListingTypeString = "TO SELL";
        }
        return petListingTypeString;
    }

    public interface OnRecyclerPetListShareClickListener {

        void onRecyclerPetListShareClick(PetListItems petListItems);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView petImage;
        public TextView petBreed;
        public TextView petPostOwner;
        public TextView petAdoptOrSell;
        public ImageButton shareImageButton;
        public Button petFavourite;
        public View dividerLine;
        public View cardView;
        public ExpandableText petListDescription;

        private PetListItems petListItems;
        int statusOfFavourite = 0;

        public ArrayList<String> wlPetListIdArray;
        public String PetListIdWishList;
        UserPetListWishList userPetListWishList= new UserPetListWishList();
        String petListId;
        String email;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (ImageView) itemView.findViewById(R.id.petImage);
            petBreed = (TextView) itemView.findViewById(R.id.petBreed);
            petAdoptOrSell = (TextView) itemView.findViewById(R.id.petAdoptOrSell);
            petPostOwner = (TextView) itemView.findViewById(R.id.petPostOwner);
            shareImageButton = (ImageButton) itemView.findViewById(R.id.petListShareImageButton);
            petFavourite = (Button) itemView.findViewById(R.id.petListFavourite);
            dividerLine = itemView.findViewById(R.id.petListDividerLine);
            petListDescription = (ExpandableText) itemView.findViewById(R.id.petListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            shareImageButton.setOnClickListener(this);
            petFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetListItems petListItems) {
            this.petListItems = petListItems;
            Glide.with(petImage.getContext()).load(petListItems.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(petImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(petImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    petImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            petBreed.setText(petListItems.getPetBreed());
            petPostOwner.setText("Posted By : " + petListItems.getPetPostOwner());
            petAdoptOrSell.setText(setListingType(petListItems));
            petListDescription.setText(petListItems.getPetDescription());
            dividerLine.setBackgroundResource(R.color.list_internal_divider);

            wlPetListIdArray = userPetListWishList.getPetWishList();
            PetListIdWishList = petListItems.getListId();

            if(wlPetListIdArray.contains(PetListIdWishList) ){
                petFavourite.setBackgroundResource(R.drawable.favourite_enable);
            }else{
                petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.petListShareImageButton) {
                onRecyclerPetListShareClickListener.onRecyclerPetListShareClick(petListItems);
            } else if (v.getId() == R.id.petListFavourite) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                email = user.get(SessionManager.KEY_EMAIL);
                petListId = petListItems.getListId();

                if (statusOfFavourite == 0) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_enable);
                    statusOfFavourite = 1;
                    try {
                        WishListPetListAdd.addPetListToWishList(email, petListId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (statusOfFavourite == 1) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_disable);
                    statusOfFavourite = 0;
                    try {
                        int pos = wlPetListIdArray.indexOf(petListId);
                        wlPetListIdArray.remove(pos);
                        WishListPetListDelete wishListPetListDelete = new WishListPetListDelete(v);
                        wishListPetListDelete.deleteWishListPetListFromServer(email, petListId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (this.petListItems != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetListDetails.class);
                    petFullInformation.putExtra("LIST_ID", petListItems.getListId());
                    petFullInformation.putExtra("PET_FIRST_IMAGE", petListItems.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", petListItems.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", petListItems.getThirdImagePath());
                    petFullInformation.putExtra("PET_BREED", petListItems.getPetBreed());
                    petFullInformation.putExtra("PET_LISTING_TYPE", petListItems.getListingType());
                    petFullInformation.putExtra("PET_AGE_MONTH", petListItems.getPetAgeInMonth());
                    petFullInformation.putExtra("PET_AGE_YEAR", petListItems.getPetAgeInYear());
                    petFullInformation.putExtra("PET_GENDER", petListItems.getPetGender());
                    petFullInformation.putExtra("PET_DESCRIPTION", petListItems.getPetDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petListItems.getPetPostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petListItems.getPetPostOwnerMobileNo());

                    v.getContext().startActivity(petFullInformation);
                }
            }

        }
    }
}
