package com.couragedigital.peto.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.couragedigital.peto.R;
import com.couragedigital.peto.Singleton.ClinicReviewInstance;
import com.couragedigital.peto.model.ClinicReviewsListItems;

import java.util.List;

public class ClinicReviewsListAdapter extends RecyclerView.Adapter<ClinicReviewsListAdapter.ViewHolder>{

    List<ClinicReviewsListItems> clinicReviewsListsItem;
    View v;
    ViewHolder viewHolder;
    int stateOfRatingStar = 0;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView clinicReviews;
        public TextView email;
        public View clinicListDividerLine;
        public int userRatings;
        LinearLayout layout;
        RelativeLayout relativeLayoutOfReviewItem;
        ClinicReviewInstance clinicReviewInstance = new ClinicReviewInstance();

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (LinearLayout) itemView.findViewById(R.id.ratingNos);
            clinicReviews = (TextView) itemView.findViewById(R.id.reviews);
            email = (TextView) itemView.findViewById(R.id.usersName);
            clinicListDividerLine = itemView.findViewById(R.id.viewdividerline);
            relativeLayoutOfReviewItem = (RelativeLayout) itemView.findViewById(R.id.reviewLayout);
        }

        public void bindClinicReviewsList(ClinicReviewsListItems clinicReviewsList) {
            //if(stateOfRatingStar == 0) {
                userRatings=Integer.parseInt(clinicReviewsList.getClinicRatings());
            DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
            float dp = 20f;
            float fpixels = metrics.density * dp;
            int pixels = (int) (fpixels + 0.5f);

                for(int i=0;i<userRatings;i++)
                {
                    ImageView image = new ImageView(v.getContext());
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(pixels,pixels));
                   // image.setLayoutParams(new android.view.ViewGroup.LayoutParams(R.dimen.starSize,R.dimen.starSize));
                    image.setId(i);
                    image.setImageResource(R.drawable.ratingstar_yellow);
                    // Adds the view to the layout
                    image.setPadding(3, 3, 3, 3);
                    layout.addView(image);
                }
                //stateOfRatingStar = 1;
            //}
            clinicReviews.setText(clinicReviewsList.getClinicReviews());
            email.setText(clinicReviewsList.getEmail());
            clinicListDividerLine.setBackgroundResource(R.color.list_internal_divider);

            Integer previousLayoutHeight = clinicReviewInstance.getRelativeLayoutHeightInstance();
            Integer layoutHeight = relativeLayoutOfReviewItem.getHeight();
            if(previousLayoutHeight > layoutHeight) {
                clinicReviewInstance.setRelativeLayoutHeightInstance(previousLayoutHeight);
            }
            else {
                clinicReviewInstance.setRelativeLayoutHeightInstance(layoutHeight);
            }

        }
    }

}
