package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.PetClinicDetails;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.ClinicListItems;
import com.couragedigital.petapp.model.ClinicReviewsListItems;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class ClinicReviewsListAdapter extends RecyclerView.Adapter<ClinicReviewsListAdapter.ViewHolder>{

    List<ClinicReviewsListItems> clinicReviewsListsItem;
    View v;
    ViewHolder viewHolder;

    public ClinicReviewsListAdapter(List<ClinicReviewsListItems> clinicReviewsListArrayList) {
        this.clinicReviewsListsItem = clinicReviewsListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.clinic_review_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ClinicReviewsListItems clinicReviewsListItems = clinicReviewsListsItem.get(i);
        viewHolder.bindClinicReviewsList(clinicReviewsListItems);
    }

    @Override
    public int getItemCount() {
        return clinicReviewsListsItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView clinicRatings;
        public TextView clinicReviews;
        public TextView email;
        public View clinicListDividerLine;
        private ClinicReviewsListItems listItems;

        public ViewHolder(View itemView) {
            super(itemView);
            clinicRatings = (TextView) itemView.findViewById(R.id.ratingNos);
            clinicReviews = (TextView) itemView.findViewById(R.id.reviews);
            email = (TextView) itemView.findViewById(R.id.usersName);
            clinicListDividerLine = itemView.findViewById(R.id.viewdividerline);
            //cardViewclinicReviewsList = itemView;
            //cardViewclinicReviewsList.setOnClickListener(this);
        }

        public void bindClinicReviewsList(ClinicReviewsListItems clinicReviewsList) {
            this.listItems = clinicReviewsList;
            clinicRatings.setText(clinicReviewsList.getClinicRatings());
            clinicReviews.setText(clinicReviewsList.getClinicReviews());
            email.setText(clinicReviewsList.getEmail());
            clinicListDividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {

//            if (this.listItems != null) {
//                Intent clinicInformation = new Intent(v.getContext(), PetClinicDetails.class);
//                clinicInformation.putExtra("CLINIC_ID", listItems.getClinicId());
//                clinicInformation.putExtra("DOCTOR_EMAIL", listItems.getEmail());
//                clinicInformation.putExtra("CLINIC_RATINGS", listItems.getClinicRatings());
//                clinicInformation.putExtra("CLINIC_REVIEWS", listItems.getClinicReviews());
//                v.getContext().startActivity(clinicInformation);
//            }
        }
    }

}
