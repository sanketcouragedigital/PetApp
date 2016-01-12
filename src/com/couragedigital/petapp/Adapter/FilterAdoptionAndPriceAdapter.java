package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.FilterPetListInstance;
import com.couragedigital.petapp.model.FilterAdoptionAndPriceList;

import java.util.ArrayList;
import java.util.List;

public class FilterAdoptionAndPriceAdapter extends RecyclerView.Adapter<FilterAdoptionAndPriceAdapter.ViewHolder> {
    List<FilterAdoptionAndPriceList> filterAdoptionAndPriceLists;
    View v;
    ViewHolder viewHolder;

    public static FilterPetListInstance filterPetListInstance = new FilterPetListInstance();

    public FilterAdoptionAndPriceAdapter(List<FilterAdoptionAndPriceList> filterAdoptionAndPriceLists) {
        this.filterAdoptionAndPriceLists = filterAdoptionAndPriceLists;
    }

    @Override
    public FilterAdoptionAndPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistfilteradoptionandprice, viewGroup, false);
        }
        else if(i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistfilterpriceview, viewGroup, false);
        }
        viewHolder = new ViewHolder(v, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterAdoptionAndPriceAdapter.ViewHolder viewHolder, int i) {
        FilterAdoptionAndPriceList filterAdoptionAndPriceList = filterAdoptionAndPriceLists.get(i);
        viewHolder.bindFilterAdoptionAndPriceList(filterAdoptionAndPriceList, i);
    }

    @Override
    public int getItemCount() {
        return filterAdoptionAndPriceLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if(position < 1 || position > 1) {
            viewType = 0;
        }
        else if(position == 1) {
            viewType = 1;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout filterAdoptionAndPriceLayout;
        TextView filterAdoptionAndPriceText;
        CheckBox filterAdoptionAndPriceCheckBox;
        TextView filterPriceTextView;

        private FilterAdoptionAndPriceList filterAdoptionAndPriceList;

        public List<String> filterSelectedAdoptionAndPriceLists = new ArrayList<String>();

        public ViewHolder(View itemView, int i) {
            super(itemView);
            if(i == 0 || i > 1) {
                filterAdoptionAndPriceLayout = (RelativeLayout) itemView.findViewById(R.id.filterAdoptionAndPriceLayout);
                filterAdoptionAndPriceText = (TextView) itemView.findViewById(R.id.filterAdoptionAndPriceText);
                filterAdoptionAndPriceCheckBox = (CheckBox) itemView.findViewById(R.id.filterAdoptionAndPriceCheckBox);

                filterAdoptionAndPriceLayout.setOnClickListener(this);
            }
            else {
                filterPriceTextView = (TextView) itemView.findViewById(R.id.filterPriceTextView);
            }
        }

        public void bindFilterAdoptionAndPriceList(FilterAdoptionAndPriceList filterAdoptionAndPriceList, int i) {
            this.filterAdoptionAndPriceList = filterAdoptionAndPriceList;
            if(i == 0 || i > 1) {
                filterAdoptionAndPriceText.setText(filterAdoptionAndPriceList.getAdoptionAndPriceText());
                filterSelectedAdoptionAndPriceLists = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
                if(filterSelectedAdoptionAndPriceLists.contains(filterAdoptionAndPriceList.getAdoptionAndPriceText())) {
                    filterAdoptionAndPriceCheckBox.setChecked(true);
                }
                else {
                    filterAdoptionAndPriceCheckBox.setChecked(false);
                }
                filterAdoptionAndPriceCheckBox.setClickable(false);
            }
            else if(i == 1) {
                filterPriceTextView.setText("Price");
            }
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.filterAdoptionAndPriceLayout) {
                filterSelectedAdoptionAndPriceLists = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
                String selectedAdoptionAndPrice = (String) filterAdoptionAndPriceText.getText();
                if(filterSelectedAdoptionAndPriceLists.contains(selectedAdoptionAndPrice)) {
                    filterSelectedAdoptionAndPriceLists.remove(selectedAdoptionAndPrice);
                    filterAdoptionAndPriceCheckBox.setChecked(false);
                }
                else {
                    filterSelectedAdoptionAndPriceLists.add(selectedAdoptionAndPrice);
                    filterAdoptionAndPriceCheckBox.setChecked(true);
                }
                filterPetListInstance.setFilterAdoptionAndPriceListInstance(filterSelectedAdoptionAndPriceLists);
            }
        }
    }
}
