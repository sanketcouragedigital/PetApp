package com.couragedigital.petapp.Adapter;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.Connectivity.MyListingPetMateDelete;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.ExpandableText;
import com.couragedigital.petapp.MyListingModifyPetMateDetails;
import com.couragedigital.petapp.MyListingPetMateListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.WishListPetMateListItem;

import java.util.List;

public class WishListPetMateListAdapter extends RecyclerView.Adapter
        <WishListPetMateListAdapter.ViewHolder> {

    public List<WishListPetMateListItem> wishListPetMateArrayList;
    public ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public WishListPetMateListAdapter() {
    }

    public WishListPetMateListAdapter(List<WishListPetMateListItem> modelData) {
        wishListPetMateArrayList = modelData;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlistpetmatelistitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WishListPetMateListItem wishListPetMateListItem = wishListPetMateArrayList.get(i);
        viewHolder.bindPetList(wishListPetMateListItem);
    }

    @Override
    public int getItemCount() {
        return wishListPetMateArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView mlPetMateImage;
        public TextView mlPetMateBreed;
        public Button modify;
        public Button deletebutton;
        public View wishlistPetMateDivider;
        public View cardView;
        public ExpandableText petMateListDescription;

        int statusOfFavourite = 0;
        private WishListPetMateListItem wishListPetMateListItem;

        public ViewHolder(View itemView) {
            super(itemView);

            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            mlPetMateImage = (RoundedNetworkImageView) itemView.findViewById(R.id.wishlistPetMateImage);
            mlPetMateBreed = (TextView) itemView.findViewById(R.id.wishlistPetMateBreed);
            //modify = (Button) itemView.findViewById(R.id.wishlistPetMateModify);
            wishlistPetMateDivider = itemView.findViewById(R.id.wishlistPetMateDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.wishlistPetMateDelete);

            petMateListDescription = (ExpandableText) itemView.findViewById(R.id.wishlistPetMateListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            modify.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(WishListPetMateListItem wishListPetMateListItem) {
            this.wishListPetMateListItem = wishListPetMateListItem;

            mlPetMateImage.setImageUrl(wishListPetMateListItem.getFirstImagePath(), imageLoader);
            mlPetMateBreed.setText(wishListPetMateListItem.getPetMateBreed());
            petMateListDescription.setText(wishListPetMateListItem.getPetMateDescription());
            modify.setText("Modify");
            deletebutton.setText("Delete");
            //petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
//            mlPetMateFavourite.setVisibility(View.GONE);
            wishlistPetMateDivider.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            /*if(v.getId() == R.id.myListingPetMateModify) {
                Intent petMateInformation = new Intent(v.getContext(), MyListingModifyPetMateDetails.class);
                petMateInformation.putExtra("PET_MATE_CATEGORY", wishListPetMateListItem.getPetMateCategory());
                petMateInformation.putExtra("PET_MATE_BREED", wishListPetMateListItem.getPetMateBreed());
                petMateInformation.putExtra("PET_MATE_IN_MONTH", wishListPetMateListItem.getPetMateAgeInMonth());
                petMateInformation.putExtra("PET_MATE_IN_YEAR", wishListPetMateListItem.getPetMateAgeInYear());
                petMateInformation.putExtra("PET_MATE_GENDER", wishListPetMateListItem.getPetMateGender());
                petMateInformation.putExtra("PET_MATE_DESCRIPTION", wishListPetMateListItem.getPetMateDescription());
                petMateInformation.putExtra("ID",wishListPetMateListItem.getId());
                v.getContext().startActivity(petMateInformation);
            }
            else */if(v.getId() == R.id.wishlistPetMateDelete) {
                if(this.wishListPetMateListItem != null) {
                    String url = URLInstance.getUrl();
                    int id = wishListPetMateListItem.getId();
                    String email = wishListPetMateListItem.getPetMatePostOwnerEmail();
                    url = url + "?method=deleteMyListingPetMateList&format=json&id="+ id +"&email="+ email +"";
                    new DeletePetMateFromServer().execute(url);
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
                    modify.setEnabled(false);
                }
            }
            else {
                if (this.wishListPetMateListItem != null) {
                    Intent petFullInformation = new Intent(v.getContext(), MyListingPetMateListDetails.class);
                    petFullInformation.putExtra("PET_FIRST_IMAGE", wishListPetMateListItem.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", wishListPetMateListItem.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", wishListPetMateListItem.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", wishListPetMateListItem.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_IN_MONTH", wishListPetMateListItem.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_IN_YEAR", wishListPetMateListItem.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", wishListPetMateListItem.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", wishListPetMateListItem.getPetMateDescription());

                    v.getContext().startActivity(petFullInformation);
                }
            }
        }

        public class DeletePetMateFromServer extends AsyncTask<String, String, String> {

            String urlForFetch;
            @Override
            protected String doInBackground(String... url) {
                try {
                    urlForFetch = url[0];
                    MyListingPetMateDelete.deleteFromRemoteServer(urlForFetch, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}
