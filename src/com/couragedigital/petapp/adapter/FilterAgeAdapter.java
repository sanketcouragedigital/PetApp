package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.RangeBar.RangeBar;
import com.couragedigital.petapp.model.FilterAgeList;

import java.util.List;

public class FilterAgeAdapter extends RecyclerView.Adapter<FilterAgeAdapter.ViewHolder> {
    List<FilterAgeList> filterAgeLists;
    View v;
    ViewHolder viewHolder;

    String leftValue;
    String rightValue;

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

        private FilterAgeList filterAgeList;

        public ViewHolder(View itemView) {
            super(itemView);
            filterAgeFixedRangeValue = (TextView) itemView.findViewById(R.id.filterAgeFixedRangeValue);
            filterAgeRangeBar = (RangeBar) itemView.findViewById(R.id.filterAgeRangeBar);
            filterAgeChangeRangeValue = (TextView) itemView.findViewById(R.id.filterAgeChangeRangeValue);

            filterAgeRangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
                @Override
                public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                    leftValue = leftPinValue;
                    rightValue = rightPinValue;
                    filterAgeChangeRangeValue.setText("Selected Range is " + leftValue + "-" + rightValue);
                }
            });
        }

        public void bindFilterAgeList(FilterAgeList filterAgeList) {
            this.filterAgeList = filterAgeList;
            filterAgeFixedRangeValue.setText(filterAgeList.getAgeText());
        }
    }
}
