package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.model.FilterBreedList;

import java.util.List;

public class FilterBreedAdapter extends RecyclerView.Adapter<FilterBreedAdapter.ViewHolder> {

    List<FilterBreedList> filterBreedLists;
    View v;
    ViewHolder viewHolder;

    public FilterBreedAdapter(List<FilterBreedList> filterBreedLists) {
        this.filterBreedLists = filterBreedLists;
    }

    @Override
    public FilterBreedAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.petlistfilterbreed, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterBreedAdapter.ViewHolder viewHolder, int i) {
        FilterBreedList filterBreedList = filterBreedLists.get(i);
        viewHolder.filterBreedText.setText(filterBreedList.getBreedText());
    }

    @Override
    public int getItemCount() {
        return filterBreedLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout filterBreedLayout;
        TextView filterBreedText;
        CheckBox filterBreedCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            filterBreedLayout = (RelativeLayout) itemView.findViewById(R.id.filterBreedLayout);
            filterBreedText = (TextView) itemView.findViewById(R.id.filterBreedText);
            filterBreedCheckBox = (CheckBox) itemView.findViewById(R.id.filterBreedCheckBox);
        }
    }
}
