package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.PetMateListDetails;
import com.couragedigital.petapp.model.PetMateListItems;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.app.AppController;

import java.util.List;

public class PetMateListAdapter  extends RecyclerView.Adapter<PetMateListAdapter.ViewHolder>{
    List<PetMateListItems> petMateLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public PetMateListAdapter(List<PetMateListItems> petMateLists) {
        this.petMateLists = petMateLists;
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public RoundedNetworkImageView petMateImage;
        public TextView petMateBreed;
        public TextView petMatePostOwner;
        public Button petMateSeeMoreButton;
        public Button petMateFavourite;
        public View dividerLinePetMate;

        int statusOfFavourite = 0;
        private PetMateListItems petMateListItems;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petMateImage = (RoundedNetworkImageView) itemView.findViewById(R.id.petMateImage);
            petMateBreed = (TextView) itemView.findViewById(R.id.petMateBreed);
            petMatePostOwner = (TextView) itemView.findViewById(R.id.petMatePostOwner);
            petMateSeeMoreButton = (Button) itemView.findViewById(R.id.petMateSeeMoreButton);
            petMateFavourite = (Button) itemView.findViewById(R.id.petMateFavourite);
            dividerLinePetMate = itemView.findViewById(R.id.dividerLinePetMate);

            petMateSeeMoreButton.setOnClickListener(this);
            //petMateFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetMateListItems petMateListItems) {
            this.petMateListItems = petMateListItems;
            petMateImage.setImageUrl(petMateListItems.getFirstImagePath(), imageLoader);

            petMateBreed.setText(petMateListItems.getPetMateBreed());
            petMatePostOwner.setText("Posted By : "+ petMateListItems.getPetMatePostOwner());
            petMateSeeMoreButton.setText("See More");
            //petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
            petMateFavourite.setVisibility(View.GONE);
            dividerLinePetMate.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.petMateSeeMoreButton) {
                if (this.petMateListItems != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetMateListDetails.class);
                    petFullInformation.putExtra("PET_FIRST_IMAGE", petMateListItems.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", petMateListItems.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", petMateListItems.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", petMateListItems.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_AGE_INMONTH", petMateListItems.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_AGE_INYEAR", petMateListItems.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", petMateListItems.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", petMateListItems.getPetMateDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petMateListItems.getPetMatePostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petMateListItems.getPetMatePostOwnerMobileNo());

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
