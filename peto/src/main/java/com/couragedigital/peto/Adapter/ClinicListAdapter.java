package com.couragedigital.peto.Adapter;

import android.app.ProgressDialog;
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
import com.couragedigital.peto.PetClinicDetails;
import com.couragedigital.peto.R;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ClinicListItems;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;

import java.util.List;

public class ClinicListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int CONTENT_TYPE = 0;
    private static final int AD_TYPE = 1;
    private int current_page = 1;
    private String url;
    String clinicId;
    ProgressDialog progressDialog = null;

    private List<ClinicListItems> clinicListsItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    RecyclerView.ViewHolder viewHolder;

    public ClinicListAdapter(List<ClinicListItems> clinicListArrayList) {
        this.clinicListsItem = clinicListArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == CONTENT_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petclinicitems, viewGroup, false);
            viewHolder = new ViewHolder(v);
        }
        else if (i == AD_TYPE) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adnativeexpresslistitem, viewGroup, false);
            viewHolder = new AdMobViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder.getItemViewType() == CONTENT_TYPE) {
            ClinicListItems clinicListItems = clinicListsItem.get(i);
            ViewHolder viewHolderForClinicList = (ViewHolder) viewHolder;
            viewHolderForClinicList.bindClinicList(clinicListItems);
        }
        else if(viewHolder.getItemViewType() == AD_TYPE) {
            AdMobViewHolder viewHolderForAdMob = (AdMobViewHolder) viewHolder;
            viewHolderForAdMob.bindAds();
        }
    }

    @Override
    public int getItemCount() {
        return clinicListsItem.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (clinicListsItem.get(position).getClinicName() == null && clinicListsItem.get(position).getClinicAddress() == null && clinicListsItem.get(position).getContact() == null) {
            return AD_TYPE;
        }
        else {
            return CONTENT_TYPE;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView clinicImage;
        public TextView clinicName;
        public TextView clinicAddress;
        public Button clinicFavourite;
        public View clinicdividerLine;
        public View cardViewclinicList;
        private ClinicListItems listItems;
        String area;
        String city;
        String areawithcity;


        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            clinicName = (TextView) itemView.findViewById(R.id.clinicName);
            clinicAddress = (TextView) itemView.findViewById(R.id.clinicAddress);
            clinicImage = (ImageView) itemView.findViewById(R.id.clinicImage);

            cardViewclinicList = itemView;
            cardViewclinicList.setOnClickListener(this);
        }

        public void bindClinicList(ClinicListItems clinicList) {
            this.listItems = clinicList;
            area = listItems.getArea();
            city = listItems.getCity();

            Glide.with(clinicImage.getContext()).load(clinicList.getClinicImage_path()).asBitmap().centerCrop().into(new BitmapImageViewTarget(clinicImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(clinicImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    clinicImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            clinicName.setText(clinicList.getClinicName());
            areawithcity = area + ", " + city;
            clinicAddress.setText(areawithcity);
        }

        @Override
        public void onClick(View view) {
            clinicId=listItems.getClinicId();
            try {
                Intent clinicInformation = new Intent(view.getContext(), PetClinicDetails.class);
                clinicInformation.putExtra("CLINIC_ID",listItems.getClinicId());
                clinicInformation.putExtra("CLINIC_NAME",listItems.getClinicName());
                clinicInformation.putExtra("CLINIC_IMAGE",listItems.getClinicImage_path());
                clinicInformation.putExtra("CLINIC_ADDRESS",listItems.getClinicAddress());
                clinicInformation.putExtra("DOCTOR_NAME",listItems.getDoctorName());
                clinicInformation.putExtra("DOCTOR_CONTACT",listItems.getContact());
                clinicInformation.putExtra("CLINIC_NOTES",listItems.getNotes());
                clinicInformation.putExtra("LATITUDE",listItems.getlatitude());
                clinicInformation.putExtra("LONGITUDE",listItems.getLongitude());
                view.getContext().startActivity(clinicInformation);
            } catch (Exception e) {
                e.printStackTrace();
                progressDialog.dismiss();
            }
        }
    }
}
