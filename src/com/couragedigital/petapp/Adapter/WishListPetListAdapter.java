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
import com.couragedigital.petapp.Connectivity.MyListingPetListDelete;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.ExpandableText;
import com.couragedigital.petapp.MyListingModifyPetDetails;
import com.couragedigital.petapp.MyListingPetListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.URLInstance;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.WishListPetListItem;

import java.util.List;


public class WishListPetListAdapter extends RecyclerView.Adapter<WishListPetListAdapter.ViewHolder> {
    List<WishListPetListItem> wishListPetListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public WishListPetListAdapter() {
    }

    public WishListPetListAdapter(List<WishListPetListItem> petLists) {
        this.wishListPetListItem = petLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wishlistpetlistitem, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        WishListPetListItem itemList = wishListPetListItem.get(i);
        viewHolder.bindPetList(itemList);
    }

    @Override
    public int getItemCount() {
        return wishListPetListItem.size();
    }

    private String setListingType(WishListPetListItem wishListPetListItem) {
        String petListingTypeString = null;
        if(wishListPetListItem.getListingType().equals("For Adoption")) {
            petListingTypeString = "TO ADOPT";
        }
        else {
            petListingTypeString = "TO SELL";
        }
        return petListingTypeString;
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView petImage;
        public TextView petBreed;
        public TextView petAdoptOrSell;
        public Button modify;
        public Button deletebutton;
        public View dividerLine;
        public ExpandableText wishlistPetListDescription;
        public View cardView;

        private WishListPetListItem wishListPetListItem;

        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (RoundedNetworkImageView) itemView.findViewById(R.id.wishlistViewImage);
            petBreed = (TextView) itemView.findViewById(R.id.wishlistViewBreed);
            petAdoptOrSell = (TextView) itemView.findViewById(R.id.wishlistPetAdoptOrSell);
            //modify = (Button) itemView.findViewById(R.id.wishlistPetListModifyButton);
            dividerLine = itemView.findViewById(R.id.wishlistDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.wishlistPetListDelete);
            wishlistPetListDescription = (ExpandableText) itemView.findViewById(R.id.wishlistPetListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
            modify.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(WishListPetListItem wishListPetListItem) {
            this.wishListPetListItem = wishListPetListItem;
            petImage.setImageUrl(wishListPetListItem.getFirstImagePath(), imageLoader);
            petBreed.setText(wishListPetListItem.getPetBreed());
            wishlistPetListDescription.setText(wishListPetListItem.getPetDescription());

            modify.setText("Modify");
            deletebutton.setText("Delete");
            petAdoptOrSell.setText(setListingType(wishListPetListItem));
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
            //petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //petFavourite.setVisibility(View.GONE);
        }

        @Override
        public void onClick(View v) {
            /*if (v.getId() == R.id.myListingPetListModifyButton) {
                //Toast.makeText(v.getContext(),"you clicked on Modify Button",Toast.LENGTH_LONG).show();
                Intent gotoEditPetList = new Intent(v.getContext(), MyListingModifyPetDetails.class);
                gotoEditPetList.putExtra("ID",wishListPetListItem.getId());
                gotoEditPetList.putExtra("PET_CATEGORY",wishListPetListItem.getPetCategory());
                gotoEditPetList.putExtra("PET_BREED", wishListPetListItem.getPetBreed());
                gotoEditPetList.putExtra("PET_AGE_IN_MONTH", wishListPetListItem.getPetAgeInMonth());
                gotoEditPetList.putExtra("PET_AGE_IN_YEAR", wishListPetListItem.getPetAgeInYear());
                gotoEditPetList.putExtra("PET_GENDER", wishListPetListItem.getPetGender());
                gotoEditPetList.putExtra("PET_LISTING_TYPE",wishListPetListItem.getListingType());
                gotoEditPetList.putExtra("PET_DESCRIPTION", wishListPetListItem.getPetDescription());
                v.getContext().startActivity(gotoEditPetList);
            } else */ if (v.getId() == R.id.wishlistPetListDelete) {
                if (this.wishListPetListItem != null) {
                    String url = URLInstance.getUrl();
                    int id = wishListPetListItem.getId();
                    String email = wishListPetListItem.getPetPostOwnerEmail();
                    url = url + "?method=deleteMyListingPetList&format=json&id=" + id + "&email=" + email + "";
                    new DeletePetListFromServer().execute(url);
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
                    modify.setEnabled(false);
                }
            } else {
                if (this.wishListPetListItem != null) {
                    Intent myListingpetdetail = new Intent(v.getContext(), MyListingPetListDetails.class);
                    myListingpetdetail.putExtra("PET_FIRST_IMAGE", wishListPetListItem.getFirstImagePath());
                    myListingpetdetail.putExtra("PET_SECOND_IMAGE", wishListPetListItem.getSecondImagePath());
                    myListingpetdetail.putExtra("PET_THIRD_IMAGE", wishListPetListItem.getThirdImagePath());
                    myListingpetdetail.putExtra("PET_BREED", wishListPetListItem.getPetBreed());
                    myListingpetdetail.putExtra("PET_LISTING_TYPE", wishListPetListItem.getListingType());
                    myListingpetdetail.putExtra("PET_AGE_IN_MONTH", wishListPetListItem.getPetAgeInMonth());
                    myListingpetdetail.putExtra("PET_AGE_IN_YEAR", wishListPetListItem.getPetAgeInYear());
                    myListingpetdetail.putExtra("PET_GENDER", wishListPetListItem.getPetGender());
                    myListingpetdetail.putExtra("PET_DESCRIPTION", wishListPetListItem.getPetDescription());

                    v.getContext().startActivity(myListingpetdetail);
                }
            }
        }

        public class DeletePetListFromServer extends AsyncTask<String, String, String> {

            String urlForFetch;

            @Override
            protected String doInBackground(String... url) {
                try {
                    urlForFetch = url[0];
                    MyListingPetListDelete.deleteFromRemoteServer(urlForFetch, v);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}
