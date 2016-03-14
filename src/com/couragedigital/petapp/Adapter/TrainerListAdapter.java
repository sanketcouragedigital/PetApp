package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
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
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.ExpandableText;
import com.couragedigital.petapp.PetClinicDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.TabFragmentTrainerDetails;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.TrainerListItem;

import java.util.List;

public class TrainerListAdapter extends RecyclerView.Adapter<TrainerListAdapter.ViewHolder> {
    List<TrainerListItem> trainerListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public TrainerListAdapter(List<TrainerListItem> trainerListArrayList) {
        this.trainerListItem = trainerListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.pettraineritems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        TrainerListItem trainerListArrayListItems = trainerListItem.get(i);
        viewHolder.bindTrainerList(trainerListArrayListItems);
    }

    @Override
    public int getItemCount() {
        return trainerListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView trainerImage;
        public TextView trainerName;
        public TextView trainerAddress;
        public Button trainerFavourite;
        public Button trainerSeeMoreBtn;
        public View trainerdividerLine;
        public View cardViewPetTrainer;
        public ExpandableText trainerDescription;
        private TrainerListItem trainerListItems;
        int statusOftrainerFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            trainerName = (TextView) itemView.findViewById(R.id.trainerName);
            trainerImage = (ImageView) itemView.findViewById(R.id.trainerImage);
            trainerDescription = (ExpandableText) itemView.findViewById(R.id.petServiceTrainerDescription);

            //  trainerAddress = (TextView) itemView.findViewById(R.id.trainerAddress);
            //  trainerSeeMoreBtn = (Button) itemView.findViewById(R.id.trainerSeeMoreButton);
            //  trainerFavourite = (Button) itemView.findViewById(R.id.trainerFavourite);
            //  trainerdividerLine = itemView.findViewById(R.id.trainerDividerLine);

            cardViewPetTrainer = itemView;
            cardViewPetTrainer.setOnClickListener(this);
            //trainerSeeMoreBtn.setOnClickListener(this);
            //trainerFavourite.setOnClickListener(this);
        }

        public void bindTrainerList(TrainerListItem trainerList) {
            this.trainerListItems = trainerList;
            Glide.with(trainerImage.getContext()).load(trainerListItems.getTrainerImage_path()).asBitmap().centerCrop().into(new BitmapImageViewTarget(trainerImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(trainerImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    trainerImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            trainerName.setText(trainerListItems.getTrainerName());
           // trainerAddress.setText(trainerListItems.getTrainerAdress());
            trainerDescription.setText(trainerListItems.getTrainerDescription());

           // trainerSeeMoreBtn.setText("See More");
            //  trainerFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //  trainerFavourite.setVisibility(View.GONE);
            //  trainerdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (this.trainerListItems != null) {
                Intent trainerInformation = new Intent(v.getContext(), TabFragmentTrainerDetails.class);
                trainerInformation.putExtra("TRAINER_IMAGE", trainerListItems.getTrainerImage_path());
                trainerInformation.putExtra("TRAINER_NAME", trainerListItems.getTrainerName());
                trainerInformation.putExtra("TRAINER_ADDRESS", trainerListItems.getTrainerAdress());
                trainerInformation.putExtra("TRAINER_EMAIL", trainerListItems.getEmail());
                trainerInformation.putExtra("TRAINER_CONTACT", trainerListItems.getContact());
                trainerInformation.putExtra("TRAINER_DESCRIPTION", trainerListItems.getTrainerDescription());
                v.getContext().startActivity(trainerInformation);
            }
        }
    }
}
