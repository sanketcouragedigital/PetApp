package com.couragedigital.peto.Adapter;


import android.app.ProgressDialog;
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
import com.couragedigital.peto.*;
import com.couragedigital.peto.Connectivity.WishListPetMateListDelete;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.WishListPetMateListItem;

import java.util.HashMap;
import java.util.List;

public class WishListPetMateListAdapter extends RecyclerView.Adapter
        <WishListPetMateListAdapter.ViewHolder> {

    public List<WishListPetMateListItem> wishListPetMateArrayList;
    public ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;
    String petMateListId;
    String email;
    ProgressDialog progressDialog = null;
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

        public ImageView mlPetMateImage;
        public TextView mlPetMateBreed;
        public TextView nameForPetPost;
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
            mlPetMateImage = (ImageView) itemView.findViewById(R.id.wishlistPetMateImage);
            mlPetMateBreed = (TextView) itemView.findViewById(R.id.wishlistPetMateBreed);
            nameForPetPost  = (TextView) itemView.findViewById(R.id.petMatePostedBy);
            //modify = (Button) itemView.findViewById(R.id.wishlistPetMateModify);
            wishlistPetMateDivider = itemView.findViewById(R.id.wishlistPetMateDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.wishlistPetMateDelete);

            petMateListDescription = (ExpandableText) itemView.findViewById(R.id.wishlistPetMateListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(WishListPetMateListItem wishListPetMateListItem) {
            this.wishListPetMateListItem = wishListPetMateListItem;

            Glide.with(mlPetMateImage.getContext()).load(wishListPetMateListItem.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(mlPetMateImage) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(mlPetMateImage.getContext().getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    mlPetMateImage.setImageDrawable(circularBitmapDrawable);
                }
            });
            mlPetMateBreed.setText(wishListPetMateListItem.getPetMateBreed());
            petMateListDescription.setText(wishListPetMateListItem.getPetMateDescription());
            nameForPetPost.setText("Posted By : "+wishListPetMateListItem.getName());
            deletebutton.setText("Delete");
            //petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
//            mlPetMateFavourite.setVisibility(View.GONE);
            wishlistPetMateDivider.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.wishlistPetMateDelete) {
                SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                email = user.get(SessionManager.KEY_EMAIL);
                petMateListId= String.valueOf(wishListPetMateListItem.getId());

                if(this.wishListPetMateListItem != null) {
//                    String url = URLInstance.getUrl();
//                    int id = wishListPetMateListItem.getId();
//                    String email = wishListPetMateListItem.getPetMatePostOwnerEmail();
//                    url = url + "?method=deleteMyListingPetMateList&format=json&id="+ id +"&email="+ email +"";
                    //new DeletePetMateFromServer().execute();
                    try {
                        WishListPetMateListDelete wishListPetMateListDelete = new WishListPetMateListDelete(v);
                        wishListPetMateListDelete.deleteWishListPetMateFromServer(email,petMateListId);
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
                }
            }
            else {
                if (this.wishListPetMateListItem != null) {
                    Intent petFullInformation = new Intent(v.getContext(), WishListPetMateDetails.class);
                    petFullInformation.putExtra("ID", wishListPetMateListItem.getId());
                    petFullInformation.putExtra("PET_FIRST_IMAGE", wishListPetMateListItem.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", wishListPetMateListItem.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", wishListPetMateListItem.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", wishListPetMateListItem.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_IN_MONTH", wishListPetMateListItem.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_IN_YEAR", wishListPetMateListItem.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", wishListPetMateListItem.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", wishListPetMateListItem.getPetMateDescription());
                    petFullInformation.putExtra("ALTERNATE_NO", wishListPetMateListItem.getAlternateNo());
                    petFullInformation.putExtra("NAME", wishListPetMateListItem.getName());

                    v.getContext().startActivity(petFullInformation);
                }
            }
        }

//        public class DeletePetMateFromServer extends AsyncTask<String, String, String> {
//
//            String urlForFetch;
//            @Override
//            protected String doInBackground(String... url) {
//                try {
//                    urlForFetch = url[0];
//                    WishListPetMateListDelete.deleteFromRemoteServer(email,petMateListId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }
    }
}
