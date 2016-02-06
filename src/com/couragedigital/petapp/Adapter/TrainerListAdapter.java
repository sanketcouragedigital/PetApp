package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
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

        public RoundedNetworkImageView trainerImage;
        public TextView trainerName;
        public TextView trainerAddress;
        public Button trainerFavourite;
        public Button trainerSeeMoreBtn;
        public View trainerdividerLine;
        private TrainerListItem trainerListItems;
        int statusOftrainerFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            trainerName = (TextView) itemView.findViewById(R.id.trainerName);
            trainerAddress = (TextView) itemView.findViewById(R.id.trainerAddress);
            trainerImage = (RoundedNetworkImageView) itemView.findViewById(R.id.trainerImage);
            trainerSeeMoreBtn = (Button) itemView.findViewById(R.id.trainerSeeMoreButton);
            trainerFavourite = (Button) itemView.findViewById(R.id.trainerFavourite);
            trainerdividerLine = itemView.findViewById(R.id.trainerDividerLine);

            trainerSeeMoreBtn.setOnClickListener(this);
            //trainerFavourite.setOnClickListener(this);
        }

        public void bindTrainerList(TrainerListItem trainerList) {
            this.trainerListItems = trainerList;
            trainerImage.setImageUrl(trainerListItems.getTrainerImage_path(), imageLoader);
            trainerName.setText(trainerListItems.getTrainerName());
            trainerAddress.setText(trainerListItems.getTrainerAdress());
            trainerSeeMoreBtn.setText("See More");
            //trainerFavourite.setBackgroundResource(R.drawable.favourite_disable);
            trainerFavourite.setVisibility(View.GONE);
            trainerdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.trainerSeeMoreButton) {
                if (this.trainerListItems != null) {
                    Intent trainerInformation = new Intent(v.getContext(), TabFragmentTrainerDetails.class);
                    trainerInformation.putExtra("TRAINER_IMAGE", trainerListItems.getTrainerImage_path());
                    trainerInformation.putExtra("TRAINER_NAME", trainerListItems.getTrainerName());
                    trainerInformation.putExtra("TRAINER_ADDRESS", trainerListItems.getTrainerAdress());
                    trainerInformation.putExtra("TRAINER_EMAIL", trainerListItems.getEmail());
                    trainerInformation.putExtra("TRAINER_CONTACT", trainerListItems.getContact());
                    v.getContext().startActivity(trainerInformation);
                }

            }
        }
    }
}
