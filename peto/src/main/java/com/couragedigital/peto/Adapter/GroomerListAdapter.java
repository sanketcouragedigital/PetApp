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
import com.couragedigital.peto.TabFragmentGroomerDetails;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicListItems;
import com.couragedigital.peto.model.GroomerListItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class GroomerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    List<GroomerListItem> groomerListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    RecyclerView.ViewHolder viewHolder;

    public GroomerListAdapter(List<GroomerListItem> groomerListArrayList) {
        this.groomerListItem = groomerListArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == CONTENT_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petgroomeritems, viewGroup, false);
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
            GroomerListItem groomerListArrayListItems = groomerListItem.get(i);
            ViewHolder viewHolderForGroomerList = (ViewHolder) viewHolder;
            viewHolderForGroomerList.bindGroomerList(groomerListArrayListItems);
        }
        else if(viewHolder.getItemViewType() == AD_TYPE) {
            AdMobViewHolder viewHolderForAdMob = (AdMobViewHolder) viewHolder;
            viewHolderForAdMob.bindAds();
        }
    }

    @Override
    public int getItemCount() {
        return groomerListItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (groomerListItem.get(position).getGroomerName() == null && groomerListItem.get(position).getGroomerAdress() == null && groomerListItem.get(position).getContact() == null) {
            return AD_TYPE;
        }
        else {
            return CONTENT_TYPE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView groomerImage;
        public TextView groomerName;
        public TextView groomerContactno;
        public TextView groomerAddress;
        public View groomerCardView;
        public Button groomerFavourite;
        public Button groomerSeeMoreBtn;
        public View groomerdividerLine;
        private GroomerListItem groomerListItems;
        int statusOfgroomerFavourite = 0;

        String area;
        String city;
        String areawithcity;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            groomerName = (TextView) itemView.findViewById(R.id.groomerName);
            groomerContactno= (TextView) itemView.findViewById(R.id.groomerContactno);
            groomerAddress = (TextView) itemView.findViewById(R.id.groomerAddress);
            groomerImage = (ImageView) itemView.findViewById(R.id.groomerImage);
            //    groomerSeeMoreBtn = (Button) itemView.findViewById(R.id.groomerSeeMoreButton);
            //   groomerFavourite = (Button) itemView.findViewById(R.id.groomerFavourite);
            //   groomerdividerLine = itemView.findViewById(R.id.groomerDividerLine);

            groomerCardView = itemView;
            groomerCardView.setOnClickListener(this);
            //groomerSeeMoreBtn.setOnClickListener(this);
            //groomerFavourite.setOnClickListener(this);
        }

        public void bindGroomerList(GroomerListItem groomerList) {
            this.groomerListItems = groomerList;
            area = groomerList.getArea();
            city = groomerList.getCity();

            Glide.with(groomerImage.getContext()).load(groomerList.getGroomerImage_path()).asBitmap().centerCrop().into(new BitmapImageViewTarget(groomerImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(groomerImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    groomerImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            groomerName.setText(groomerListItems.getGroomerName());
            groomerContactno.setText(groomerListItems.getContact());
            if(area.equals("")) {
                areawithcity = city;
            }
            else {
                areawithcity = area + " " + city;
            }
            groomerAddress.setText(areawithcity);

            //  groomerSeeMoreBtn.setText("See More");
            //  groomerFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //  groomerFavourite.setVisibility(View.GONE);
            //  groomerdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (this.groomerListItems != null) {
                Intent groomerInformation = new Intent(v.getContext(), TabFragmentGroomerDetails.class);
                groomerInformation.putExtra("GROOMER_IMAGE", groomerListItems.getGroomerImage_path());
                groomerInformation.putExtra("GROOMER_NAME", groomerListItems.getGroomerName());
                groomerInformation.putExtra("GROOMER_ADDRESS", groomerListItems.getGroomerAdress());
                groomerInformation.putExtra("GROOMER_EMAIL", groomerListItems.getEmail());
                groomerInformation.putExtra("GROOMER_CITY", groomerListItems.getCity());
                groomerInformation.putExtra("GROOMER_AREA", groomerListItems.getArea());
                groomerInformation.putExtra("GROOMER_CONTACT", groomerListItems.getContact());

                groomerInformation.putExtra("GROOMER_ID", groomerListItems.getGroomer_Id());
                groomerInformation.putExtra("GROOMER_NOTES", groomerListItems.getNotes());
                groomerInformation.putExtra("LATITUDE", groomerListItems.getlatitude());
                groomerInformation.putExtra("LONGITUDE", groomerListItems.getLongitude());
                groomerInformation.putExtra("GROOMER_DESCRIPTION", groomerListItems.getDescription());
                v.getContext().startActivity(groomerInformation);
            }

        }
    }
}
