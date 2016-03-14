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
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.TabFragmentStoresDetails;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.StoresListItem;

import java.util.List;

public class StoresListAdapter extends RecyclerView.Adapter<StoresListAdapter.ViewHolder> {
    List<StoresListItem> storesListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public StoresListAdapter(List<StoresListItem> storesListArrayList) {
        this.storesListItem = storesListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petstoresitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        StoresListItem storesListArrayListItems = storesListItem.get(i);
        viewHolder.bindStoresList(storesListArrayListItems);
    }

    @Override
    public int getItemCount() {
        return storesListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView storesImage;
        public TextView storesName;
        public TextView storesAddress;
        public Button storesFavourite;
        public Button storesSeeMoreBtn;
        public View storesdividerLine;
        private StoresListItem storesListItems;
        int statusOfstoresFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            storesName = (TextView) itemView.findViewById(R.id.storesName);
            storesAddress = (TextView) itemView.findViewById(R.id.storesAddress);
            storesImage = (ImageView) itemView.findViewById(R.id.storesImage);
            storesSeeMoreBtn = (Button) itemView.findViewById(R.id.storesSeeMoreButton);
            storesFavourite = (Button) itemView.findViewById(R.id.storesFavourite);
            storesdividerLine = itemView.findViewById(R.id.storesDividerLine);

            storesSeeMoreBtn.setOnClickListener(this);
            //storesFavourite.setOnClickListener(this);
        }

        public void bindStoresList(StoresListItem storesList) {
            this.storesListItems = storesList;
            Glide.with(storesImage.getContext()).load(storesListItems.getStoresImage_path()).asBitmap().centerCrop().into(new BitmapImageViewTarget(storesImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(storesImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    storesImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            storesName.setText(storesListItems.getStoresName());
            storesAddress.setText(storesListItems.getStoresAdress());
            storesSeeMoreBtn.setText("See More");
            //storesFavourite.setBackgroundResource(R.drawable.favourite_disable);
            storesFavourite.setVisibility(View.GONE);
            storesdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.storesSeeMoreButton) {
                if (this.storesListItems != null) {
                    Intent shelterInformation = new Intent(v.getContext(), TabFragmentStoresDetails.class);
                    shelterInformation.putExtra("STORES_IMAGE", storesListItems.getStoresImage_path());
                    shelterInformation.putExtra("STORES_NAME", storesListItems.getStoresName());
                    shelterInformation.putExtra("STORES_ADDRESS", storesListItems.getStoresAdress());
                    shelterInformation.putExtra("STORES_EMAIL", storesListItems.getEmail());
                    shelterInformation.putExtra("STORES_CONTACT", storesListItems.getContact());
                    v.getContext().startActivity(shelterInformation);
                }

            }
        }
    }
}
