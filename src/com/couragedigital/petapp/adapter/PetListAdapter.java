package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.PetListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;

import java.util.List;

public class PetListAdapter extends RecyclerView.Adapter<PetListAdapter.ViewHolder> {

    List<PetList> petLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public PetListAdapter(List<PetList> petLists) {
        this.petLists = petLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.petlistsubitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PetList petList = petLists.get(i);
        viewHolder.bindPetList(petList);
    }

    private String setPetListingTypeButtonName(PetList petList) {
        String petListingTypeString = null;
        if(petList.getListingType().equals("For Adoption")) {
            petListingTypeString = "ADOPT";
        }
        else {
            petListingTypeString = "SELL";
        }
        return petListingTypeString;
    }

    @Override
    public int getItemCount() {
        return petLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public RoundedNetworkImageView petImage;
        public TextView petBreed;
        public TextView petPostOwner;
        public Button petListingTypeButton;
        public Button petFavourite;
        public View dividerLine;

        private PetList petList;
        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (RoundedNetworkImageView) itemView.findViewById(R.id.petImage);
            petBreed = (TextView) itemView.findViewById(R.id.petBreed);
            petPostOwner = (TextView) itemView.findViewById(R.id.petPostOwner);
            petListingTypeButton = (Button) itemView.findViewById(R.id.petListingTypeButton);
            petFavourite = (Button) itemView.findViewById(R.id.petFavourite);
            dividerLine = itemView.findViewById(R.id.dividerLine);

            petListingTypeButton.setOnClickListener(this);
            //petFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetList petList) {
            this.petList = petList;
            petImage.setImageUrl(petList.getImage_path(), imageLoader);

            petBreed.setText(petList.getPetBreed());
            petPostOwner.setText("Posted By : "+ petList.getPetPostOwner());
            petListingTypeButton.setText(setPetListingTypeButtonName(petList));
            //petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            petFavourite.setVisibility(View.GONE);
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.petListingTypeButton) {
                if (this.petList != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetListDetails.class);
                    petFullInformation.putExtra("PET_IMAGE", petList.getImage_path());
                    petFullInformation.putExtra("PET_BREED", petList.getPetBreed());
                    petFullInformation.putExtra("PET_LISTING_TYPE", petList.getListingType());
                    petFullInformation.putExtra("PET_AGE", petList.getPetAge());
                    petFullInformation.putExtra("PET_GENDER", petList.getPetGender());
                    petFullInformation.putExtra("PET_DESCRIPTION", petList.getPetDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petList.getPetPostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petList.getPetPostOwnerMobileNo());

                    v.getContext().startActivity(petFullInformation);
                }
            }
            /*else if(v.getId() == R.id.petFavourite) {

                if(statusOfFavourite == 0) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_enable);
                    statusOfFavourite = 1;
                }
                else if(statusOfFavourite == 1) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_disable);
                    statusOfFavourite = 0;
                }
            }*/
        }
    }
}
