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
import com.couragedigital.petapp.PetMetListDetails;
import com.couragedigital.petapp.model.PetMetList;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;

import java.util.List;

public class PetMetListAdapter  extends RecyclerView.Adapter<PetMetListAdapter.ViewHolder>{
    List<PetMetList> petMetLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public PetMetListAdapter(List<PetMetList> petMetLists) {
        this.petMetLists = petMetLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petmetlistsubitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PetMetList petMetList = petMetLists.get(i);
        viewHolder.bindPetList(petMetList);
    }

    @Override
    public int getItemCount() {
        return petMetLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public RoundedNetworkImageView petMetImage;
        public TextView petMetBreed;
        public TextView petMetPostOwner;
        public Button petMetSeeMoreButton;
        public Button petMetFavourite;
        public View dividerLinePetMet;

        int statusOfFavourite = 0;
        private PetMetList petMetList;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petMetImage = (RoundedNetworkImageView) itemView.findViewById(R.id.petMetImage);
            petMetBreed = (TextView) itemView.findViewById(R.id.petMetBreed);
            petMetPostOwner = (TextView) itemView.findViewById(R.id.petMetPostOwner);
            petMetSeeMoreButton = (Button) itemView.findViewById(R.id.petMetSeeMoreButton);
            petMetFavourite = (Button) itemView.findViewById(R.id.petMetFavourite);
            dividerLinePetMet = itemView.findViewById(R.id.dividerLinePetMet);

            petMetSeeMoreButton.setOnClickListener(this);
            //petMetFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetMetList petMetList) {
            this.petMetList = petMetList;
            petMetImage.setImageUrl(petMetList.getImage_path(), imageLoader);

            petMetBreed.setText(petMetList.getPetMetBreed());
            petMetPostOwner.setText("Posted By : "+ petMetList.getPetMetPostOwner());
            petMetSeeMoreButton.setText("See More");
            //petMetFavourite.setBackgroundResource(R.drawable.favourite_disable);
            petMetFavourite.setVisibility(View.GONE);
            dividerLinePetMet.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.petMetSeeMoreButton) {
                if (this.petMetList != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetMetListDetails.class);
                    petFullInformation.putExtra("PET_MET_IMAGE", petMetList.getImage_path());
                    petFullInformation.putExtra("PET_MET_BREED", petMetList.getPetMetBreed());
                    petFullInformation.putExtra("PET_MET_AGE", petMetList.getPetMetAge());
                    petFullInformation.putExtra("PET_MET_GENDER", petMetList.getPetMetGender());
                    petFullInformation.putExtra("PET_MET_DESCRIPTION", petMetList.getPetMetDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petMetList.getPetMetPostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petMetList.getPetMetPostOwnerMobileNo());

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
