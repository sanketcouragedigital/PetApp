package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.PetListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicListItems;

import java.util.List;

public class ClinicListAdapter extends RecyclerView.Adapter<ClinicListAdapter.ViewHolder> {


    List<ClinicListItems> clinicListsItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public ClinicListAdapter(List<ClinicListItems> clinicListArrayList) {
        this.clinicListsItem = clinicListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petclinicitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ClinicListItems clinicListItems = clinicListsItem.get(i);
        viewHolder.bindPetList(clinicListItems);
    }

    @Override
    public int getItemCount() {
        return clinicListsItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView clinicImage;
        public TextView clinicName;
        public TextView clinicAddress;
        public Button clinicFavourite;
        public Button clinicSeeMore;
        public View clinicdividerLine;
        private ClinicListItems clinicListItems;
        int statusOfclinicFavourite = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            clinicName = (TextView) itemView.findViewById(R.id.clinicName);
            clinicAddress = (TextView) itemView.findViewById(R.id.clinicAddress);
            clinicImage = (RoundedNetworkImageView) itemView.findViewById(R.id.clinicImage);
            clinicSeeMore = (Button) itemView.findViewById(R.id.clinicSeeMoreButton);
            clinicFavourite = (Button) itemView.findViewById(R.id.clinicFavourite);
            clinicdividerLine = itemView.findViewById(R.id.clinicDividerLine);

            clinicSeeMore.setOnClickListener(this);
            clinicFavourite.setOnClickListener(this);
        }

        public void bindPetList(ClinicListItems clinicList) {
            this.clinicListItems = clinicList;
            clinicImage.setImageUrl(clinicList.getClinicImage_path(), imageLoader);
            clinicName.setText(clinicList.getClinicName());
            clinicAddress.setText(clinicList.getClinicAdress());

            clinicFavourite.setBackgroundResource(R.drawable.favourite_disable);
            clinicdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == 0) {
                if (view.getId() == R.id.clinicSeeMoreButton) {
                    Toast.makeText(view.getContext(), "Button selected & Position" + position, Toast.LENGTH_LONG).show();
                } else if (view.getId() == R.id.clinicFavourite) {
                    if (statusOfclinicFavourite == 0) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_enable);
                        statusOfclinicFavourite = 1;
                    } else if (statusOfclinicFavourite == 1) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_disable);
                        statusOfclinicFavourite = 0;
                    }
                }
            }
            else if (position == 1) {
                if (view.getId() == R.id.clinicSeeMoreButton) {
                    Toast.makeText(view.getContext(), "Button selected & Position" + position, Toast.LENGTH_LONG).show();
                } else if (view.getId() == R.id.clinicFavourite) {

                    if (statusOfclinicFavourite == 0) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_enable);
                        statusOfclinicFavourite = 1;
                    } else if (statusOfclinicFavourite == 1) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_disable);
                        statusOfclinicFavourite = 0;
                    }
                }
            }
            else if (position == 2) {
                if (view.getId() == R.id.clinicSeeMoreButton) {
                    Toast.makeText(view.getContext(), "Button selected & Position  " + position, Toast.LENGTH_LONG).show();
                } else if (view.getId() == R.id.clinicFavourite) {

                    if (statusOfclinicFavourite == 0) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_enable);
                        statusOfclinicFavourite = 1;
                    } else if (statusOfclinicFavourite == 1) {
                        clinicFavourite.setBackgroundResource(R.drawable.favourite_disable);
                        statusOfclinicFavourite = 0;
                    }
                }
            }
        }
    }
}
