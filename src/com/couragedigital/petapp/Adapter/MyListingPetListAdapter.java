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
import com.couragedigital.petapp.Connectivity.PetRefreshFetchList;
import com.couragedigital.petapp.MyListingPetListDetails;
import com.couragedigital.petapp.PetListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.MyListingPetListItems;
import com.couragedigital.petapp.model.MyListingPetMateListItem;

import java.util.List;

public class MyListingPetListAdapter extends RecyclerView.Adapter<MyListingPetListAdapter.ViewHolder> {

    List<MyListingPetListItems> myListingpetLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public MyListingPetListAdapter() {
    }

    public MyListingPetListAdapter(List<MyListingPetListItems> petLists) {
        this.myListingpetLists = petLists;
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

    private String setPetListingTypeButtonName(MyListingPetListItems myListingPetListItems) {
        String petListingTypeString = null;
        if (myListingPetListItems.getListingType().equals("For Adoption")) {
            petListingTypeString = "ADOPT";
        } else {
            petListingTypeString = "SELL";
        }
        return petListingTypeString;
    }

    @Override
    public int getItemCount() {
        return myListingpetLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView petImage;
        public TextView petBreed;
        public Button seeMoreButton;
        public Button deletebutton;
        public View dividerLine;

        private MyListingPetListItems myListingPetListItem;

        int statusOfFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petImage = (RoundedNetworkImageView) itemView.findViewById(R.id.myListingViewImage);
            petBreed = (TextView) itemView.findViewById(R.id.myListingViewBreed);
            seeMoreButton = (Button) itemView.findViewById(R.id.myListingPetListSeeMore);
            dividerLine = itemView.findViewById(R.id.myListingDividerLine);
            deletebutton = (Button) itemView.findViewById(R.id.myListingPetListDelete);
            seeMoreButton.setOnClickListener(this);
            deletebutton.setOnClickListener(this);
        }

        public void bindPetList(MyListingPetListItems myListingPetListItems) {
            this.myListingPetListItem = myListingPetListItems;
            petImage.setImageUrl(myListingPetListItems.getFirstImagePath(), imageLoader);
            petBreed.setText(myListingPetListItems.getPetBreed());
            seeMoreButton.setText("See More");
            deletebutton.setText("Delete");
            //petListingTypeButton.setText(setPetListingTypeButtonName(myListingPetListItems));
            //petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //petFavourite.setVisibility(View.GONE);
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.myListingPetListSeeMore) {
                if (this.myListingPetListItem != null) {
                    Intent myListingpetdetail = new Intent(v.getContext(), MyListingPetListDetails.class);
                    myListingpetdetail.putExtra("PET_FIRST_IMAGE", myListingPetListItem.getFirstImagePath());
                    myListingpetdetail.putExtra("PET_SECOND_IMAGE", myListingPetListItem.getSecondImagePath());
                    myListingpetdetail.putExtra("PET_THIRD_IMAGE", myListingPetListItem.getThirdImagePath());
                    myListingpetdetail.putExtra("PET_BREED", myListingPetListItem.getPetBreed());
                    myListingpetdetail.putExtra("PET_LISTING_TYPE", myListingPetListItem.getListingType());
                    myListingpetdetail.putExtra("PET_AGE", myListingPetListItem.getPetAge());
                    myListingpetdetail.putExtra("PET_GENDER", myListingPetListItem.getPetGender());
                    myListingpetdetail.putExtra("PET_DESCRIPTION", myListingPetListItem.getPetDescription());

                    v.getContext().startActivity(myListingpetdetail);
                }
            }
            else if (v.getId() == R.id.myListingPetListDelete) {
                if(this.myListingPetListItem != null) {
                    String url = "http://storage.couragedigital.com/dev/api/petappapi.php";
                    int id = myListingPetListItem.getId();
                    String email = myListingPetListItem.getPetPostOwnerEmail();
                    url = url + "?method=deleteMyListingPetList&format=json&id="+ id +"&email="+ email +"";
                    new DeletePetListFromServer().execute(url);
                    deletebutton.setText("Deleted");
                    deletebutton.setEnabled(false);
                    seeMoreButton.setEnabled(false);
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
