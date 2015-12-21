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
                .inflate(R.layout.petlistsubitems, viewGroup, false);
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

        public RoundedNetworkImageView petImage;
        public TextView petBreed;
        public TextView petPostOwner;
        public Button petListingTypeButton;
        public Button petFavourite;
        public View dividerLine;

        private PetList petList;
        int statusOfFavourite = 0;
        private PetMetList petMetList;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (RoundedNetworkImageView) itemView.findViewById(R.id.petImage);
            petBreed = (TextView) itemView.findViewById(R.id.petBreed);
            petListingTypeButton = (Button) itemView.findViewById(R.id.petListingTypeButton);
            petFavourite = (Button) itemView.findViewById(R.id.petFavourite);
            dividerLine = itemView.findViewById(R.id.dividerLine);

            petListingTypeButton.setOnClickListener(this);
            petFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetMetList petMetList) {
            this.petMetList = petMetList;
            petImage.setImageUrl(petMetList.getImage_path(), imageLoader);

            petBreed.setText(petMetList.getPetBreed());
            petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.petListingTypeButton) {
                if (this.petMetList != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetListDetails.class);
                    petFullInformation.putExtra("PET_IMAGE", petMetList.getImage_path());
                    petFullInformation.putExtra("PET_BREED", petMetList.getPetBreed());
                    petFullInformation.putExtra("PET_AGE", petMetList.getPetAge());
                    petFullInformation.putExtra("PET_GENDER", petMetList.getPetGender());
                    petFullInformation.putExtra("PET_DESCRIPTION", petMetList.getPetDescription());

                    v.getContext().startActivity(petFullInformation);
                }
            }
            else if(v.getId() == R.id.petFavourite) {

                if(statusOfFavourite == 0) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_enable);
                    statusOfFavourite = 1;
                }
                else if(statusOfFavourite == 1) {
                    petFavourite.setBackgroundResource(R.drawable.favourite_disable);
                    statusOfFavourite = 0;
                }
            }
        }
    }
}
