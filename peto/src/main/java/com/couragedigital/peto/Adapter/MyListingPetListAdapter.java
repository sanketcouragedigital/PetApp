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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.MyListingPetListDelete;
import com.couragedigital.peto.Singleton.URLInstance;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.MyListingPetListItems;

import java.util.List;

public class MyListingPetListAdapter extends RecyclerView.Adapter<MyListingPetListAdapter.ViewHolder> {

    private final OnRecyclerMyListingPetDeleteClickListener onRecyclerMyListingPetDeleteClickListener;
    List<MyListingPetListItems> myListingpetLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public MyListingPetListAdapter(List<MyListingPetListItems> petLists, OnRecyclerMyListingPetDeleteClickListener onRecyclerMyListingPetDeleteClickListener) {
        this.myListingpetLists = petLists;
        this.onRecyclerMyListingPetDeleteClickListener = onRecyclerMyListingPetDeleteClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mylistingpetlistitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        MyListingPetListItems itemList = myListingpetLists.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return myListingpetLists.size();
    }

    private String setListingType(MyListingPetListItems myListingPetListItems) {
        String petListingTypeString = null;
        if(myListingPetListItems.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
        }
        else {
            petListingTypeString = "TO SELL";
        }
        return petListingTypeString;
    }

    public interface OnRecyclerMyListingPetDeleteClickListener {

        void onRecyclerMyListingPetDeleteClick(List<MyListingPetListItems> myListingpetLists, MyListingPetListItems myListingPetListItem, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView petImage;
        public TextView petBreed;
        public TextView petAdoptOrSell;
        public Button modify;
        public Button deletebutton;
        public View dividerLine;
        public ExpandableText myListingPetListDescription;
        public View cardView;

        private MyListingPetListItems myListingPetListItem;

        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (ImageView) itemView.findViewById(R.id.myListingViewImage);
            petBreed = (TextView) itemView.findViewById(R.id.myListingViewBreed);
            petAdoptOrSell = (TextView) itemView.findViewById(R.id.myListingPetAdoptOrSell);
            modify = (Button) itemView.findViewById(R.id.myListingPetListModifyButton);
            dividerLine = itemView.findViewById(R.id.myListingDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.myListingPetListDelete);
            myListingPetListDescription = (ExpandableText) itemView.findViewById(R.id.myListingPetListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            modify.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(MyListingPetListItems myListingPetListItems) {
            this.myListingPetListItem = myListingPetListItems;
            Glide.with(petImage.getContext()).load(myListingPetListItems.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(petImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(petImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    petImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            petBreed.setText(myListingPetListItems.getPetBreed());
            myListingPetListDescription.setText(myListingPetListItems.getPetDescription());

            modify.setText("Modify");
            deletebutton.setText("Delete");
            petAdoptOrSell.setText(setListingType(myListingPetListItem));
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.myListingPetListModifyButton) {
                //Toast.makeText(v.getContext(),"you clicked on Modify Button",Toast.LENGTH_LONG).show();
                Intent gotoEditPetList = new Intent(v.getContext(), MyListingModifyPetDetails.class);
                gotoEditPetList.putExtra("ID",myListingPetListItem.getId());
                gotoEditPetList.putExtra("PET_CATEGORY",myListingPetListItem.getPetCategory());
                gotoEditPetList.putExtra("PET_BREED", myListingPetListItem.getPetBreed());
                gotoEditPetList.putExtra("PET_AGE_IN_MONTH", myListingPetListItem.getPetAgeInMonth());
                gotoEditPetList.putExtra("PET_AGE_IN_YEAR", myListingPetListItem.getPetAgeInYear());
                gotoEditPetList.putExtra("PET_GENDER", myListingPetListItem.getPetGender());
                gotoEditPetList.putExtra("PET_LISTING_TYPE",myListingPetListItem.getListingType());
                gotoEditPetList.putExtra("PET_DESCRIPTION", myListingPetListItem.getPetDescription());
                v.getContext().startActivity(gotoEditPetList);
            } else if (v.getId() == R.id.myListingPetListDelete) {
                onRecyclerMyListingPetDeleteClickListener.onRecyclerMyListingPetDeleteClick(myListingpetLists, myListingPetListItem, this.getAdapterPosition());
            } else {
                if (this.myListingPetListItem != null) {
                    Intent myListingpetdetail = new Intent(v.getContext(), MyListingPetListDetails.class);
                    myListingpetdetail.putExtra("PET_FIRST_IMAGE", myListingPetListItem.getFirstImagePath());
                    myListingpetdetail.putExtra("PET_SECOND_IMAGE", myListingPetListItem.getSecondImagePath());
                    myListingpetdetail.putExtra("PET_THIRD_IMAGE", myListingPetListItem.getThirdImagePath());
                    myListingpetdetail.putExtra("PET_BREED", myListingPetListItem.getPetBreed());
                    myListingpetdetail.putExtra("PET_LISTING_TYPE", myListingPetListItem.getListingType());
                    myListingpetdetail.putExtra("PET_AGE_IN_MONTH", myListingPetListItem.getPetAgeInMonth());
                    myListingpetdetail.putExtra("PET_AGE_IN_YEAR", myListingPetListItem.getPetAgeInYear());
                    myListingpetdetail.putExtra("PET_GENDER", myListingPetListItem.getPetGender());
                    myListingpetdetail.putExtra("PET_DESCRIPTION", myListingPetListItem.getPetDescription());

                    v.getContext().startActivity(myListingpetdetail);
                }
            }
        }
    }
}
