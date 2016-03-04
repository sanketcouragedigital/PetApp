package com.couragedigital.petapp.Adapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.Connectivity.WishListPetMateListAdd;
import com.couragedigital.petapp.Connectivity.WishListPetMateListDelete;
import com.couragedigital.petapp.ExpandableText;
import com.couragedigital.petapp.PetMateListDetails;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Singleton.UserPetMateListWishList;
import com.couragedigital.petapp.model.PetMateListItems;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.app.AppController;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PetMateListAdapter extends RecyclerView.Adapter<PetMateListAdapter.ViewHolder> {
    List<PetMateListItems> petMateLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;
	String petMateListId;
    String email;
    SessionManager sessionManager;
	ProgressDialog progressDialog = null;
    private Context Context;
    public PetMateListAdapter(List<PetMateListItems> petMateLists) {
        this.petMateLists = petMateLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petmatelistsubitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        PetMateListItems petMateListItems = petMateLists.get(i);
        viewHolder.bindPetList(petMateListItems);
    }

    @Override
    public int getItemCount() {
        return petMateLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView petMateImage;
        public TextView petMateBreed;
        public TextView petMatePostOwner;
        //  public TextView petMateDescription;
        public Button petFavourite;
        public Button petMateFavourite;
        public View dividerLinePetMate;
        public View cardView;
        public ImageButton petMateShareImageButton;
        public ExpandableText petMateListDescription;

        int statusOfFavourite = 0;
        private PetMateListItems petMateListItems;

		public ArrayList<String> wlPetMateListIdArray;
        public String PetMateListIdWishList;
        UserPetMateListWishList userPetMateListWishList= new UserPetMateListWishList();
        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            petMateImage = (RoundedNetworkImageView) itemView.findViewById(R.id.petMateImage);
            petMateBreed = (TextView) itemView.findViewById(R.id.petMateBreed);
            petMatePostOwner = (TextView) itemView.findViewById(R.id.petMatePostOwner);
            petMateListDescription = (ExpandableText) itemView.findViewById(R.id.petMateListDescription);
            petMateShareImageButton = (ImageButton) itemView.findViewById(R.id.petMateShareImageButton);
            petMateFavourite = (Button) itemView.findViewById(R.id.petMateFavourite);
            dividerLinePetMate = itemView.findViewById(R.id.petMateListDividerLine);

            cardView = itemView;
            cardView.setOnClickListener(this);
            petMateShareImageButton.setOnClickListener(this);
            petMateFavourite.setOnClickListener(this);
        }

        public void bindPetList(PetMateListItems petMateListItems) {
            this.petMateListItems = petMateListItems;
				wlPetMateListIdArray = userPetMateListWishList.getPetMateListId();
            PetMateListIdWishList = petMateListItems.getListId();

            if(wlPetMateListIdArray.contains(PetMateListIdWishList) ){
                petFavourite.setBackgroundResource(R.drawable.favourite_enable);
            }else{
                petFavourite.setBackgroundResource(R.drawable.favourite_disable);
            }
            petMateImage.setImageUrl(petMateListItems.getFirstImagePath(), imageLoader);
            petMateBreed.setText(petMateListItems.getPetMateBreed());
            petMatePostOwner.setText("Posted By : " + petMateListItems.getPetMatePostOwner());
            petMateListDescription.setText(petMateListItems.getPetMateDescription());
            petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //      petMateFavourite.setVisibility(View.GONE);
            dividerLinePetMate.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.petMateShareImageButton) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String sharingText = "I want to share  Peto App Download it from Google PlayStore.";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingText);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }  else if (v.getId() == R.id.petMateFavourite) {
				SessionManager sessionManager = new SessionManager(v.getContext());
                HashMap<String, String> user = sessionManager.getUserDetails();
                email = user.get(SessionManager.KEY_EMAIL);
                petMateListId=petMateListItems.getListId();
                if (statusOfFavourite == 0) {
                    petMateFavourite.setBackgroundResource(R.drawable.favourite_enable);
                    statusOfFavourite = 1;
					try {
                        WishListPetMateListAdd.addPetMateListToWishList(petMateListId,email);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (statusOfFavourite == 1) {
                    petMateFavourite.setBackgroundResource(R.drawable.favourite_disable);
                    statusOfFavourite = 0;
					try {
                        WishListPetMateListDelete.deleteWishListPetMateFromServer(petMateListId,email);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (this.petMateListItems != null) {
                    Intent petFullInformation = new Intent(v.getContext(), PetMateListDetails.class);
					 petFullInformation.putExtra("LIST_ID", petMateListItems.getListId());
                    petFullInformation.putExtra("PET_FIRST_IMAGE", petMateListItems.getFirstImagePath());
                    petFullInformation.putExtra("PET_SECOND_IMAGE", petMateListItems.getSecondImagePath());
                    petFullInformation.putExtra("PET_THIRD_IMAGE", petMateListItems.getThirdImagePath());
                    petFullInformation.putExtra("PET_MATE_BREED", petMateListItems.getPetMateBreed());
                    petFullInformation.putExtra("PET_MATE_AGE_MONTH", petMateListItems.getPetMateAgeInMonth());
                    petFullInformation.putExtra("PET_MATE_AGE_YEAR", petMateListItems.getPetMateAgeInYear());
                    petFullInformation.putExtra("PET_MATE_GENDER", petMateListItems.getPetMateGender());
                    petFullInformation.putExtra("PET_MATE_DESCRIPTION", petMateListItems.getPetMateDescription());
                    petFullInformation.putExtra("POST_OWNER_EMAIL", petMateListItems.getPetMatePostOwnerEmail());
                    petFullInformation.putExtra("POST_OWNER_MOBILENO", petMateListItems.getPetMatePostOwnerMobileNo());

                    v.getContext().startActivity(petFullInformation);
                }
            }
        }
    }
}
