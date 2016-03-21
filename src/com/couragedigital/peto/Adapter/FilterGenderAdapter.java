package com.couragedigital.peto.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.peto.R;
import com.couragedigital.peto.Singleton.FilterPetListInstance;
import com.couragedigital.peto.model.FilterGenderList;

import java.util.ArrayList;
import java.util.List;

public class FilterGenderAdapter extends RecyclerView.Adapter<FilterGenderAdapter.ViewHolder> {
    List<FilterGenderList> filterGenderLists;
    View v;
    ViewHolder viewHolder;

    public static FilterPetListInstance filterPetListInstance = new FilterPetListInstance();

    public FilterGenderAdapter(List<FilterGenderList> filterGenderLists) {
        this.filterGenderLists = filterGenderLists;
    }

    @Override
    public FilterGenderAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfiltergender, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterGenderAdapter.ViewHolder viewHolder, int i) {
        FilterGenderList filterGenderList = filterGenderLists.get(i);
        viewHolder.bindFilterCategoryList(filterGenderList);
    }

    @Override
    public int getItemCount() {
        return filterGenderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout filterGenderLayout;
        TextView filterGenderText;
        CheckBox filterGenderCheckBox;

        private FilterGenderList filterGenderList;

        public List<String> filterSelectedGenderList = new ArrayList<String>();

        public ViewHolder(View itemView) {
            super(itemView);
            filterGenderLayout = (RelativeLayout) itemView.findViewById(R.id.filterGenderLayout);
            filterGenderText = (TextView) itemView.findViewById(R.id.filterGenderText);
            filterGenderCheckBox = (CheckBox) itemView.findViewById(R.id.filterGenderCheckBox);

            filterGenderLayout.setOnClickListener(this);
        }

        public void bindFilterCategoryList(FilterGenderList filterGenderList) {
            this.filterGenderList = filterGenderList;
            filterGenderText.setText(filterGenderList.getGender());
            filterSelectedGenderList = filterPetListInstance.getFilterGenderListInstance();
            if (filterSelectedGenderList.contains(filterGenderList.getGender())) {
                filterGenderCheckBox.setChecked(true);
            } else {
                filterGenderCheckBox.setChecked(false);
            }
            filterGenderCheckBox.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.filterGenderLayout) {
                filterSelectedGenderList = filterPetListInstance.getFilterGenderListInstance();
                String selectedGender = (String) filterGenderText.getText();
                if(filterSelectedGenderList.contains(selectedGender)) {
                    filterSelectedGenderList.remove(selectedGender);
                    filterGenderCheckBox.setChecked(false);
                }
                else {
                    filterSelectedGenderList.add(selectedGender);
                    filterGenderCheckBox.setChecked(true);
                }
                filterPetListInstance.setFilterGenderListInstance(filterSelectedGenderList);
            }
        }
    }
}
