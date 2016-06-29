package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.Settings;
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
import com.couragedigital.peto.Holder.AdMobViewHolder;
import com.couragedigital.peto.R;
import com.couragedigital.peto.TabFragmentShelterDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ShelterListItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class ShelterListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    List<ShelterListItem> shelterListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    RecyclerView.ViewHolder viewHolder;

    public ShelterListAdapter(List<ShelterListItem> shelterListArrayList) {
        this.shelterListItem = shelterListArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == CONTENT_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petshelteritems, viewGroup, false);
            viewHolder = new ViewHolder(v);
        }
        else if (i == AD_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.smalladnativeexpresslistitem, viewGroup, false);
            viewHolder = new AdMobViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder.getItemViewType() == CONTENT_TYPE) {
            ShelterListItem shelterListArrayListItems = shelterListItem.get(i);
            ViewHolder viewHolderForShelterList = (ViewHolder) viewHolder;
            viewHolderForShelterList.bindShelterList(shelterListArrayListItems);
        }
        else if(viewHolder.getItemViewType() == AD_TYPE) {
            AdMobViewHolder viewHolderForAdMob = (AdMobViewHolder) viewHolder;
            viewHolderForAdMob.bindAds();
        }
    }

    @Override
    public int getItemCount() {
        return shelterListItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 7 == 0 && position != 0) {
            return AD_TYPE;
        }
        else {
            return CONTENT_TYPE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView shelterImage;
        public TextView shelterName;
        public TextView shelterContactno;
        public TextView shelterAddress;
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
            shelterContactno= (TextView) itemView.findViewById(R.id.shelterContactno);
            shelterAddress = (TextView) itemView.findViewById(R.id.shelterAddress);
            shelterImage = (ImageView) itemView.findViewById(R.id.shelterImage);

            shelterCardView = itemView;
            shelterCardView.setOnClickListener(this);
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
            shelterContactno.setText(shelterListItems.getContact());
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
                shelterInformation.putExtra("SHELTER_CITY", shelterListItems.getCity());
                shelterInformation.putExtra("SHELTER_AREA", shelterListItems.getArea());
                shelterInformation.putExtra("SHELTER_CONTACT", shelterListItems.getContact());
                shelterInformation.putExtra("SHELTER_ID", shelterListItems.getShelter_Id());
                shelterInformation.putExtra("SHELTER_NOTES", shelterListItems.getNotes());
                shelterInformation.putExtra("LATITUDE", shelterListItems.getlatitude());
                shelterInformation.putExtra("LONGITUDE", shelterListItems.getLongitude());
                shelterInformation.putExtra("SHELTER_DESCRIPTION", shelterListItems.getDescription());


                v.getContext().startActivity(shelterInformation);
            }
        }
    }
}
