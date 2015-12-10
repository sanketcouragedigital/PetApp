package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.RangeBar.RangeBar;
import com.couragedigital.petapp.model.FilterPriceList;

import java.util.List;

public class FilterPriceAdapter extends RecyclerView.Adapter<FilterPriceAdapter.ViewHolder> {
    List<FilterPriceList> filterPriceLists;
    View v;
    ViewHolder viewHolder;

    String leftValue;
    String rightValue;

    public FilterPriceAdapter(List<FilterPriceList> filterPriceLists) {
        this.filterPriceLists = filterPriceLists;
    }

    @Override
    public FilterPriceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistfilteradoption, viewGroup, false);
        }
        else if(i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistfilterprice, viewGroup, false);
        }
        viewHolder = new ViewHolder(v, i);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterPriceAdapter.ViewHolder viewHolder, int i) {
        FilterPriceList filterPriceList = filterPriceLists.get(i);
        if(i <= 0) {
            viewHolder.filterAdoptionText.setText(filterPriceList.getAdoptionText());
            viewHolder.filterAdoptionCheckBox.setChecked(false);
        }
        else if(i > 0) {
            viewHolder.filterPriceFixedRangeValue.setText(filterPriceList.getPriceText());
        }
    }

    @Override
    public int getItemCount() {
        return filterPriceLists.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if(position < 1) {
            viewType = 0;
        }
        else if(position >= 1) {
            viewType = 1;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout filterAdoptionLayout;
        TextView filterAdoptionText;
        CheckBox filterAdoptionCheckBox;
        TextView filterPriceFixedRangeValue;
        RangeBar filterPriceRangeBar;
        TextView filterPriceChangeRangeValue;

        public ViewHolder(View itemView, int i) {
            super(itemView);
            if(i == 0) {
                filterAdoptionLayout = (RelativeLayout) itemView.findViewById(R.id.filterAdoptionLayout);
                filterAdoptionText = (TextView) itemView.findViewById(R.id.filterAdoptionText);
                filterAdoptionCheckBox = (CheckBox) itemView.findViewById(R.id.filterAdoptionCheckBox);
            }
            else {
                filterPriceFixedRangeValue = (TextView) itemView.findViewById(R.id.filterPriceFixedRangeValue);
                filterPriceRangeBar = (RangeBar) itemView.findViewById(R.id.filterPriceRangeBar);
                filterPriceChangeRangeValue = (TextView) itemView.findViewById(R.id.filterPriceChangeRangeValue);
                filterPriceRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                    @Override
                    public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                        leftValue = leftPinValue;
                        rightValue = rightPinValue;
                        filterPriceChangeRangeValue.setText("Selected Range is " + leftValue + "-" + rightValue);
                    }
                });
            }
        }
    }
}
