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
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.couragedigital.peto.R;
import com.couragedigital.peto.TabFragmentShelterDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ShelterListItem;

import java.util.List;

public class ShelterListAdapter extends RecyclerView.Adapter<ShelterListAdapter.ViewHolder> {
    List<ShelterListItem> shelterListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public ShelterListAdapter(List<ShelterListItem> shelterListArrayList) {
        this.shelterListItem = shelterListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petshelteritems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ShelterListItem shelterListArrayListItems = shelterListItem.get(i);
        viewHolder.bindShelterList(shelterListArrayListItems);
    }

    @Override
    public int getItemCount() {
        return shelterListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView shelterImage;
        public TextView shelterName;
        public TextView shelterAddress;
        public Button shelterFavourite;
        public Button shelterSeeMoreBtn;
        public View shelterdividerLine;
        public View shelterCardView;
        private ShelterListItem shelterListItems;
        int statusOfshelterFavourite = 0;
        String area;
        String city;
        String areawithcity;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            shelterName = (TextView) itemView.findViewById(R.id.shelterName);
            shelterAddress = (TextView) itemView.findViewById(R.id.shelterAddress);
            shelterImage = (ImageView) itemView.findViewById(R.id.shelterImage);
            //shelterSeeMoreBtn = (Button) itemView.findViewById(R.id.shelterSeeMoreButton);
            //shelterFavourite = (Button) itemView.findViewById(R.id.shelterFavourite);
            //shelterdividerLine = itemView.findViewById(R.id.shelterDividerLine);

            shelterCardView = itemView;
            shelterCardView.setOnClickListener(this);
            //shelterSeeMoreBtn.setOnClickListener(this);
            //shelterFavourite.setOnClickListener(this);
        }

        public void bindShelterList(ShelterListItem shelterList) {
            this.shelterListItems = shelterList;
            area = shelterList.getArea();
            city = shelterList.getCity();

            Glide.with(shelterImage.getContext()).load(shelterListItems.getShelterImage_path()).asBitmap().centerCrop().into(new BitmapImageViewTarget(shelterImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(shelterImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    shelterImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            shelterName.setText(shelterListItems.getShelterName());
            if(area.equals("")) {
                area = city;
            }
            else {
                areawithcity = area + " " + city;
            }
            shelterAddress.setText(areawithcity);


            //shelterSeeMoreBtn.setText("See More");
            //shelterFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //shelterFavourite.setVisibility(View.GONE);
            //shelterdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (this.shelterListItems != null) {
                Intent shelterInformation = new Intent(v.getContext(), TabFragmentShelterDetails.class);
                shelterInformation.putExtra("SHELTER_IMAGE", shelterListItems.getShelterImage_path());
                shelterInformation.putExtra("SHELTER_NAME", shelterListItems.getShelterName());
                shelterInformation.putExtra("SHELTER_ADDRESS", shelterListItems.getShelterAdress());
                shelterInformation.putExtra("SHELTER_EMAIL", shelterListItems.getEmail());
                shelterInformation.putExtra("GROOMER_CITY", shelterListItems.getCity());
                shelterInformation.putExtra("GROOMER_AREA", shelterListItems.getArea());
                shelterInformation.putExtra("SHELTER_CONTACT", shelterListItems.getContact());
                v.getContext().startActivity(shelterInformation);
            }
        }
    }
}
