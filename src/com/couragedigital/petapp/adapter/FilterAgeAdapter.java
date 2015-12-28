package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.RangeBar.RangeBar;
import com.couragedigital.petapp.Singleton.FilterPetListInstance;
import com.couragedigital.petapp.model.FilterAgeList;
import com.couragedigital.petapp.model.FilterAgeRangeList;

import java.util.ArrayList;
import java.util.List;

public class FilterAgeAdapter extends RecyclerView.Adapter<FilterAgeAdapter.ViewHolder> {
    List<FilterAgeList> filterAgeLists;
    View v;
    ViewHolder viewHolder;

    String leftValue;
    String rightValue;

    public static FilterPetListInstance filterPetListInstance = new FilterPetListInstance();

    public FilterAgeAdapter(List<FilterAgeList> filterAgeLists) {
        this.filterAgeLists = filterAgeLists;
    }

    @Override
    public FilterAgeAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfilterage, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterAgeAdapter.ViewHolder viewHolder, int i) {
        FilterAgeList filterAgeList = filterAgeLists.get(i);
        viewHolder.bindFilterAgeList(filterAgeList);
    }

    @Override
    public int getItemCount() {
        return filterAgeLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView filterAgeFixedRangeValue;
        RangeBar filterAgeRangeBar;
        TextView filterAgeChangeRangeValue;

        FilterAgeList filterAgeList;
        FilterAgeRangeList filterAgeRangeList = new FilterAgeRangeList();

        public List<String> filterSelectedAgeList = new ArrayList<String>();

        public ViewHolder(View itemView) {
            super(itemView);
            filterAgeFixedRangeValue = (TextView) itemView.findViewById(R.id.filterAgeFixedRangeValue);
            filterAgeRangeBar = (RangeBar) itemView.findViewById(R.id.filterAgeRangeBar);
            filterAgeChangeRangeValue = (TextView) itemView.findViewById(R.id.filterAgeChangeRangeValue);

            filterAgeRangeBar.setDrawTicks(false);
            filterAgeRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    leftValue = leftPinValue;
                    rightValue = rightPinValue;
                    filterAgeRangeList.setLeftValueOfRangeBar(leftValue);
                    filterAgeRangeList.setRightValueOfRangeBar(rightValue);
                    filterAgeChangeRangeValue.setText("Selected Range is " + leftValue + "-" + rightValue);
                    filterSelectedAgeList.clear();
                    filterSelectedAgeList.add(filterAgeRangeList.getLeftValueOfRangeBar());
                    filterSelectedAgeList.add(filterAgeRangeList.getRightValueOfRangeBar());
                    filterPetListInstance.setFilterAgeListInstance(filterSelectedAgeList);
                }
            });
        }

        public void bindFilterAgeList(FilterAgeList filterAgeList) {
            this.filterAgeList = filterAgeList;
            filterAgeFixedRangeValue.setText(filterAgeList.getAgeText());
            filterSelectedAgeList = filterPetListInstance.getFilterAgeListInstance();
            if(filterSelectedAgeList.isEmpty()) {
                filterAgeChangeRangeValue.setText("Selected Range is " + 0 + "-" + 100);
            }
            else {
                Float leftValueOfRangeBar = Float.valueOf(filterSelectedAgeList.get(0));
                Float rightValueOfRangeBar = Float.valueOf(filterSelectedAgeList.get(1));
                filterAgeRangeBar.setRangePinsByValue(leftValueOfRangeBar, rightValueOfRangeBar);
                filterAgeChangeRangeValue.setText("Selected Range is " + filterSelectedAgeList.get(0) + "-" + filterSelectedAgeList.get(1));
            }
        }
    }
}
