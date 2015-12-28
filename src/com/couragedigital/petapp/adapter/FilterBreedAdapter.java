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
import com.couragedigital.petapp.model.FilterBreedList;

import java.util.ArrayList;
import java.util.List;

public class FilterBreedAdapter extends RecyclerView.Adapter<FilterBreedAdapter.ViewHolder> {

    List<FilterBreedList> filterBreedLists;
    View v;
    ViewHolder viewHolder;

    public static FilterPetListInstance filterPetListInstance = new FilterPetListInstance();

    public FilterBreedAdapter(List<FilterBreedList> filterBreedLists) {
        this.filterBreedLists = filterBreedLists;
    }

    @Override
    public FilterBreedAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfilterbreed, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterBreedAdapter.ViewHolder viewHolder, int i) {
        FilterBreedList filterBreedList = filterBreedLists.get(i);
        viewHolder.bindFilterBreedList(filterBreedList);
    }

    @Override
    public int getItemCount() {
        return filterBreedLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout filterBreedLayout;
        TextView filterBreedText;
        CheckBox filterBreedCheckBox;

        private FilterBreedList filterBreedList;

        public List<String> filterSelectedBreedList = new ArrayList<String>();

        public ViewHolder(View itemView) {
            super(itemView);
            filterBreedLayout = (RelativeLayout) itemView.findViewById(R.id.filterBreedLayout);
            filterBreedText = (TextView) itemView.findViewById(R.id.filterBreedText);
            filterBreedCheckBox = (CheckBox) itemView.findViewById(R.id.filterBreedCheckBox);

            filterBreedLayout.setOnClickListener(this);
        }

        public void bindFilterBreedList(FilterBreedList filterBreedList) {
            this.filterBreedList = filterBreedList;
            filterSelectedBreedList = filterPetListInstance.getFilterBreedListInstance();
            filterBreedText.setText(filterBreedList.getBreedText());
            if(filterSelectedBreedList.contains(filterBreedList.getBreedText())) {
                filterBreedCheckBox.setChecked(true);
            }
            else {
                filterBreedCheckBox.setChecked(false);
            }
            filterBreedCheckBox.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.filterBreedLayout) {
                filterSelectedBreedList = filterPetListInstance.getFilterBreedListInstance();
                String selectedBreed = (String) filterBreedText.getText();
                if(filterSelectedBreedList.contains(selectedBreed)) {
                    filterSelectedBreedList.remove(selectedBreed);
                    filterBreedCheckBox.setChecked(false);
                }
                else {
                    filterSelectedBreedList.add(selectedBreed);
                    filterBreedCheckBox.setChecked(true);
                }
                filterPetListInstance.setFilterBreedListInstance(filterSelectedBreedList);
            }
        }
    }
}
