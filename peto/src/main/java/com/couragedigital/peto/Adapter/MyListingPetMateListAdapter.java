package com.couragedigital.peto.Adapter;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.MyListingPetMateDelete;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.MyListingPetListItems;
import com.couragedigital.peto.model.MyListingPetMateListItem;

import java.util.List;

public class MyListingPetMateListAdapter extends RecyclerView.Adapter<MyListingPetMateListAdapter.ViewHolder> {

    private final OnRecyclerMyListingPetMateDeleteClickListener onRecyclerMyListingPetMateDeleteClickListener;
    public  List<MyListingPetMateListItem> myListingPetMateArrayList;
    public ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public MyListingPetMateListAdapter(List<MyListingPetMateListItem> modelData, OnRecyclerMyListingPetMateDeleteClickListener onRecyclerMyListingPetMateDeleteClickListener) {
        myListingPetMateArrayList = modelData;
        this.onRecyclerMyListingPetMateDeleteClickListener = onRecyclerMyListingPetMateDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mylistingpetmateitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MyListingPetMateListItem myListingPetMateListItem = myListingPetMateArrayList.get(i);
        viewHolder.bindPetList(myListingPetMateListItem);
    }

    @Override
    public int getItemCount() {
        return myListingPetMateArrayList.size();
    }

    public interface OnRecyclerMyListingPetMateDeleteClickListener {

        void onRecyclerMyListingPetMateDeleteClick(List<MyListingPetMateListItem> myListingPetMateListItems, MyListingPetMateListItem myListingPetMateListItem, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mlPetMateImage;
        public TextView mlPetMateBreed;
        public Button modify;
        public Button deletebutton;
        public View myListingPetMateDivider;
        public View cardView;
        public ExpandableText petMateListDescription;

        int statusOfFavourite = 0;
        private MyListingPetMateListItem myListingPetMateListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            mlPetMateImage = (ImageView) itemView.findViewById(R.id.myListingPetMateImage);
            mlPetMateBreed = (TextView) itemView.findViewById(R.id.myListingPetMateBreed);
            modify = (Button) itemView.findViewById(R.id.myListingPetMateModify);
            myListingPetMateDivider = itemView.findViewById(R.id.myListingPetMateDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.myListingPetMateDelete);

            petMateListDescription = (ExpandableText) itemView.findViewById(R.id.myListingPetMateListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            modify.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(MyListingPetMateListItem myListingPetMateListItem) {
            this.myListingPetMateListItem = myListingPetMateListItem;

            Glide.with(mlPetMateImage.getContext()).load(myListingPetMateListItem.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mlPetMateImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mlPetMateImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mlPetMateImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            mlPetMateBreed.setText(myListingPetMateListItem.getPetMateBreed());
            petMateListDescription.setText(myListingPetMateListItem.getPetMateDescription());
            modify.setText("Modify");
            deletebutton.setText("Delete");
            //petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
//            mlPetMateFavourite.setVisibility(View.GONE);
            myListingPetMateDivider.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.myListingPetMateModify) {
                Intent petMateInformation = new Intent(v.getContext(), MyListingModifyPetMateDetails.class);
                petMateInformation.putExtra("PET_MATE_CATEGORY", myListingPetMateListItem.getPetMateCategory());
                petMateInformation.putExtra("PET_MATE_BREED", myListingPetMateListItem.getPetMateBreed());
                petMateInformation.putExtra("PET_MATE_IN_MONTH", myListingPetMateListItem.getPetMateAgeInMonth());
                petMateInformation.putExtra("PET_MATE_IN_YEAR", myListingPetMateListItem.getPetMateAgeInYear());
                petMateInformation.putExtra("PET_MATE_GENDER", myListingPetMateListItem.getPetMateGender());
                petMateInformation.putExtra("PET_MATE_DESCRIPTION", myListingPetMateListItem.getPetMateDescription());
                petMateInformation.putExtra("ID",myListingPetMateListItem.getId());
                v.getContext().startActivity(petMateInformation);
            }
            else if(v.getId() == R.id.myListingPetMateDelete) {
                onRecyclerMyListingPetMateDeleteClickListener.onRecyclerMyListingPetMateDeleteClick(myListingPetMateArrayList, myListingPetMateListItem, this.getAdapterPosition());
            }
            else {
                if (this.myListingPetMateListItem != null) {
                    Intent petFullInformation = new Intent(v.getContext(), MyListingPetMateListDetails.class);
                    petFullInformation.putExtra("PET_FIRST_IMAGE", myListingPetMateListItem.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", myListingPetMateListItem.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", myListingPetMateListItem.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", myListingPetMateListItem.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_IN_MONTH", myListingPetMateListItem.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_IN_YEAR", myListingPetMateListItem.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", myListingPetMateListItem.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", myListingPetMateListItem.getPetMateDescription());

                    v.getContext().startActivity(petFullInformation);
                }
            }
        }
    }
}
