package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.Connectivity.WishListPetMateListAdd;
import com.couragedigital.peto.Connectivity.WishListPetMateListDelete;
import com.couragedigital.peto.ExpandableText;
import com.couragedigital.peto.PetMateListDetails;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.UserPetMateListWishList;
import com.couragedigital.peto.model.PetListItems;
import com.couragedigital.peto.model.PetMateListItems;
import com.couragedigital.peto.R;
import com.couragedigital.peto.app.AppController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetMateListAdapter extends RecyclerView.Adapter<PetMateListAdapter.ViewHolder> {
    private final OnRecyclerPetMateListShareClickListener onRecyclerPetMateListShareClickListener;
    List<PetMateListItems> petMateLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public PetMateListAdapter(List<PetMateListItems> petMateLists, OnRecyclerPetMateListShareClickListener onRecyclerPetMateListShareClickListener) {
        this.petMateLists = petMateLists;
        this.onRecyclerPetMateListShareClickListener = onRecyclerPetMateListShareClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petmatelistsubitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PetMateListItems petMateListItems = petMateLists.get(i);
        viewHolder.bindPetList(petMateListItems);
    }

    @Override
    public int getItemCount() {
        return petMateLists.size();
    }

    public interface OnRecyclerPetMateListShareClickListener {

        void onRecyclerPetMateListShareClick(PetMateListItems petMateListItems);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView petMateImage;
        public TextView petMateBreed;
        public TextView petMatePostOwner;
        //  public TextView petMateDescription;
        public Button petFavourite;
        public Button petMateFavourite;
        public View dividerLinePetMate;
        public View cardView;
        public ImageButton petMateShareImageButton;
        public ExpandableText petMateListDescription;

        int statusOfFavourite = 0;
        private PetMateListItems petMateListItems;

        public ArrayList<String> wlPetMateListIdArray;
        public String PetMateListIdWishList;
        UserPetMateListWishList userPetMateListWishList= new UserPetMateListWishList();
        String petMateListId;
        String email;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petMateImage = (ImageView) itemView.findViewById(R.id.petMateImage);
            petMateBreed = (TextView) itemView.findViewById(R.id.petMateBreed);
            petMatePostOwner = (TextView) itemView.findViewById(R.id.petMatePostOwner);
            petMateListDescription = (ExpandableText) itemView.findViewById(R.id.petMateListDescription);
            petMateShareImageButton = (ImageButton) itemView.findViewById(R.id.petMateShareImageButton);
            petMateFavourite = (Button) itemView.findViewById(R.id.petMateFavourite);
            dividerLinePetMate = itemView.findViewById(R.id.petMateListDividerLine);

            cardView = itemView;
            cardView.setOnClickListener(this);
            petMateShareImageButton.setOnClickListener(this);
            petMateFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetMateListItems petMateListItems) {
            this.petMateListItems = petMateListItems;
            Glide.with(petMateImage.getContext()).load(petMateListItems.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(petMateImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(petMateImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    petMateImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            petMateBreed.setText(petMateListItems.getPetMateBreed());
            petMatePostOwner.setText("Posted By : " + petMateListItems.getPetMatePostOwner());
            petMateListDescription.setText(petMateListItems.getPetMateDescription());
            petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
            dividerLinePetMate.setBackgroundResource(R.color.list_internal_divider);

            wlPetMateListIdArray = userPetMateListWishList.getPetMateWishList();
            PetMateListIdWishList = petMateListItems.getListId();

            if(wlPetMateListIdArray.contains(PetMateListIdWishList)) {
                petMateFavourite.setBackgroundResource(R.drawable.favourite_enable);
            }
            else {
                petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.petMateShareImageButton) {
                onRecyclerPetMateListShareClickListener.onRecyclerPetMateListShareClick(petMateListItems);
            }  else if (v.getId() == R.id.petMateFavourite) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                email = user.get(SessionManager.KEY_EMAIL);
                petMateListId = petMateListItems.getListId();
                if (statusOfFavourite == 0) {
                    petMateFavourite.setBackgroundResource(R.drawable.favourite_enable);
                    statusOfFavourite = 1;
                    try {
                        WishListPetMateListAdd wishListPetMateListAdd = new WishListPetMateListAdd(v.getContext());
                        wishListPetMateListAdd.addPetMateListToWishList(email, petMateListId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (statusOfFavourite == 1) {
                    petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
                    statusOfFavourite = 0;
                    try {
                        int pos = wlPetMateListIdArray.indexOf(petMateListId);
                        wlPetMateListIdArray.remove(pos);
                        WishListPetMateListDelete wishListPetMateListDelete = new WishListPetMateListDelete(v);
                        wishListPetMateListDelete.deleteWishListPetMateFromServer(email, petMateListId);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (this.petMateListItems != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetMateListDetails.class);
                    petFullInformation.putExtra("LIST_ID", petMateListItems.getListId());
                    petFullInformation.putExtra("PET_FIRST_IMAGE", petMateListItems.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", petMateListItems.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", petMateListItems.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", petMateListItems.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_AGE_MONTH", petMateListItems.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_AGE_YEAR", petMateListItems.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", petMateListItems.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", petMateListItems.getPetMateDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petMateListItems.getPetMatePostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petMateListItems.getPetMatePostOwnerMobileNo());

                    v.getContext().startActivity(petFullInformation);
                }
            }
        }
    }
}
