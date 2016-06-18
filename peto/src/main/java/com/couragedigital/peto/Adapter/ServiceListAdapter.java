package com.couragedigital.peto.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.peto.R;
import com.couragedigital.peto.Singleton.ReviewInstance;
import com.couragedigital.peto.model.ReviewsListItems;

import java.util.List;

public class ServiceListAdapter extends RecyclerView.Adapter<ServiceListAdapter.ViewHolder> {
    List<ReviewsListItems> serviceReviewsListsItem;
    View v;
    ViewHolder viewHolder;
    int stateOfRatingStar = 0;

    public ServiceListAdapter(List<ReviewsListItems> serviceReviewsListArrayList) {
        this.serviceReviewsListsItem = serviceReviewsListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.service_review_list_items, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ReviewsListItems serviceReviewsListItems = serviceReviewsListsItem.get(i);
        viewHolder.bindClinicReviewsList(serviceReviewsListItems);
    }

    @Override
    public int getItemCount() {
        return serviceReviewsListsItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView serviceReviews;
        public TextView email;
        public TextView EmptyKeyResponse;
        public View serviceListDividerLine;
        public int userRatings;
        String emptyResponse;
        LinearLayout layout;
        RelativeLayout relativeLayoutOfReviewItem;
        ReviewInstance serviceReviewInstance = new ReviewInstance();

        public ViewHolder(View itemView) {
            super(itemView);

            layout = (LinearLayout) itemView.findViewById(R.id.ratingNos);
            serviceReviews = (TextView) itemView.findViewById(R.id.reviews);
            email = (TextView) itemView.findViewById(R.id.usersName);
            EmptyKeyResponse = (TextView) itemView.findViewById(R.id.emptyText);
            serviceListDividerLine = itemView.findViewById(R.id.viewdividerline);
            relativeLayoutOfReviewItem = (RelativeLayout) itemView.findViewById(R.id.reviewLayout);
        }

        public void bindClinicReviewsList(ReviewsListItems serviceReviewsListItems) {

            if (serviceReviewsListItems.getEmptyKey().equals("Empty")) {
                EmptyKeyResponse.setVisibility(View.VISIBLE);
                EmptyKeyResponse.setText("No reviews yet.");
                relativeLayoutOfReviewItem.setVisibility(View.GONE);
            } else {

                userRatings = Integer.parseInt(serviceReviewsListItems.getRatings());
                DisplayMetrics metrics = v.getContext().getResources().getDisplayMetrics();
                float dp = 20f;
                float fpixels = metrics.density * dp;
                int pixels = (int) (fpixels + 0.5f);

                for (int i = 0; i < userRatings; i++) {
                    ImageView image = new ImageView(v.getContext());
                    image.setLayoutParams(new android.view.ViewGroup.LayoutParams(pixels, pixels));
                    // image.setLayoutParams(new android.view.ViewGroup.LayoutParams(R.dimen.starSize,R.dimen.starSize));
                    image.setId(i);
                    image.setImageResource(R.drawable.ratingstar_yellow);
                    // Adds the view to the layout
                    image.setPadding(3, 3, 3, 3);
                    layout.addView(image);
                }
                serviceReviews.setText(serviceReviewsListItems.getReviews());
                email.setText(serviceReviewsListItems.getEmail());
                serviceListDividerLine.setBackgroundResource(R.color.list_internal_divider);

                Integer previousLayoutHeight = serviceReviewInstance.getRelativeLayoutHeightInstance();
                Integer layoutHeight = relativeLayoutOfReviewItem.getHeight();
                if (previousLayoutHeight > layoutHeight) {
                    serviceReviewInstance.setRelativeLayoutHeightInstance(previousLayoutHeight);
                } else {
                    serviceReviewInstance.setRelativeLayoutHeightInstance(layoutHeight);
                }

            }
        }
    }

}

