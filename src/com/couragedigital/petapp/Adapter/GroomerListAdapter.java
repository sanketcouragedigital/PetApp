package com.couragedigital.petapp.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.couragedigital.petapp.CustomImageView.RoundedNetworkImageView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.TabFragmentGroomerDetails;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.GroomerListItem;

import java.util.List;

public class GroomerListAdapter extends RecyclerView.Adapter<GroomerListAdapter.ViewHolder> {
    List<GroomerListItem> groomerListItem;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    public GroomerListAdapter(List<GroomerListItem> groomerListArrayList) {
        this.groomerListItem = groomerListArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petgroomeritems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        GroomerListItem groomerListArrayListItems = groomerListItem.get(i);
        viewHolder.bindGroomerList(groomerListArrayListItems);
    }

    @Override
    public int getItemCount() {
        return groomerListItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RoundedNetworkImageView groomerImage;
        public TextView groomerName;
        public TextView groomerAddress;
        public View groomerCardView;
        public Button groomerFavourite;
        public Button groomerSeeMoreBtn;
        public View groomerdividerLine;
        private GroomerListItem groomerListItems;
        int statusOfgroomerFavourite = 0;

        String area;
        String city;
        String areawithcity;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }

            groomerName = (TextView) itemView.findViewById(R.id.groomerName);
            groomerAddress = (TextView) itemView.findViewById(R.id.groomerAddress);
            groomerImage = (RoundedNetworkImageView) itemView.findViewById(R.id.groomerImage);
            //    groomerSeeMoreBtn = (Button) itemView.findViewById(R.id.groomerSeeMoreButton);
            //   groomerFavourite = (Button) itemView.findViewById(R.id.groomerFavourite);
            //   groomerdividerLine = itemView.findViewById(R.id.groomerDividerLine);

            groomerCardView = itemView;
            groomerCardView.setOnClickListener(this);
            //groomerSeeMoreBtn.setOnClickListener(this);
            //groomerFavourite.setOnClickListener(this);
        }

        public void bindGroomerList(GroomerListItem groomerList) {
            this.groomerListItems = groomerList;
            area = groomerList.getArea();
            city = groomerList.getCity();

            groomerImage.setImageUrl(groomerListItems.getGroomerImage_path(), imageLoader);
            groomerName.setText(groomerListItems.getGroomerName());
            areawithcity = area + ", " + city;
            groomerAddress.setText(areawithcity);

            //  groomerSeeMoreBtn.setText("See More");
            //  groomerFavourite.setBackgroundResource(R.drawable.favourite_disable);
            //  groomerFavourite.setVisibility(View.GONE);
            //  groomerdividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View view) {
            if (this.groomerListItems != null) {
                Intent groomerInformation = new Intent(v.getContext(), TabFragmentGroomerDetails.class);
                groomerInformation.putExtra("GROOMER_IMAGE", groomerListItems.getGroomerImage_path());
                groomerInformation.putExtra("GROOMER_NAME", groomerListItems.getGroomerName());
                groomerInformation.putExtra("GROOMER_ADDRESS", groomerListItems.getGroomerAdress());
                groomerInformation.putExtra("GROOMER_EMAIL", groomerListItems.getEmail());
                groomerInformation.putExtra("GROOMER_CITY", groomerListItems.getCity());
                groomerInformation.putExtra("GROOMER_AREA", groomerListItems.getArea());
                groomerInformation.putExtra("GROOMER_CONTACT", groomerListItems.getContact());
                v.getContext().startActivity(groomerInformation);
            }

        }
    }
}
