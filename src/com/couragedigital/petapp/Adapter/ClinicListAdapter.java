package com.couragedigital.petapp.Adapter;

import android.content.Intent;
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
import com.couragedigital.petapp.PetClinicDetails;
import com.couragedigital.petapp.PetListDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicListItems;

import java.util.List;

public class ClinicListAdapter extends RecyclerView.Adapter<ClinicListAdapter.ViewHolder> {


    private List<ClinicListItems> clinicListsItem;
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
        //  public Button clinicSeeMoreBtn;
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
            clinicImage = (RoundedNetworkImageView) itemView.findViewById(R.id.clinicImage);
            // clinicSeeMoreBtn = (Button) itemView.findViewById(R.id.clinicSeeMoreButton);
            // clinicdividerLine = itemView.findViewById(R.id.clinicDividerLine);

            cardViewclinicList = itemView;
            cardViewclinicList.setOnClickListener(this);
            // clinicSeeMoreBtn.setOnClickListener(this);
            // clinicFavourite.setOnClickListener(this);
        }

        public void bindPetList(ClinicListItems clinicList) {
            this.listItems = clinicList;
            area = listItems.getArea();
            city = listItems.getCity();

            clinicImage.setImageUrl(clinicList.getClinicImage_path(), imageLoader);
            clinicName.setText(clinicList.getClinicName());
            areawithcity = area + ", " + city;
            clinicAddress.setText(areawithcity);

            // clinicdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (this.listItems != null) {
                Intent clinicInformation = new Intent(v.getContext(), PetClinicDetails.class);
                clinicInformation.putExtra("CLINIC_IMAGE", listItems.getClinicImage_path());
                clinicInformation.putExtra("CLINIC_ADDRESS", listItems.getClinicAddress());
                clinicInformation.putExtra("DOCTOR_NAME", listItems.getDoctorName());
              //  clinicInformation.putExtra("DOCTOR_EMAIL", listItems.getEmail());
                clinicInformation.putExtra("DOCTOR_CONTACT", listItems.getContact());
                clinicInformation.putExtra("CLINIC_NOTES", listItems.getNotes());
                v.getContext().startActivity(clinicInformation);
            }
        }
    }
}
