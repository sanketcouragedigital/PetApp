package com.couragedigital.peto.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.peto.R;
import com.couragedigital.peto.Singleton.FilterPetMateListInstance;
import com.couragedigital.peto.model.FilterGenderList;

import java.util.ArrayList;
import java.util.List;

public class FilterGenderPetMateAdapter extends RecyclerView.Adapter<FilterGenderPetMateAdapter.ViewHolder> {

    List<FilterGenderList> filterGenderPetMateLists;
    View v;
    ViewHolder viewHolder;

    public static FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();

    public FilterGenderPetMateAdapter(List<FilterGenderList> filterGenderPetMateLists) {
        this.filterGenderPetMateLists = filterGenderPetMateLists;
    }

    @Override
    public FilterGenderPetMateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfiltergender, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterGenderPetMateAdapter.ViewHolder viewHolder, int i) {
        FilterGenderList filterGenderList = filterGenderPetMateLists.get(i);
        viewHolder.bindFilterCategoryPetMateList(filterGenderList);
    }

    @Override
    public int getItemCount() {
        return filterGenderPetMateLists.size();
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

        public void bindFilterCategoryPetMateList(FilterGenderList filterGenderList) {
            this.filterGenderList = filterGenderList;
            filterGenderText.setText(filterGenderList.getGender());
            filterSelectedGenderList = filterPetMateListInstance.getFilterGenderListInstance();
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
                filterSelectedGenderList = filterPetMateListInstance.getFilterGenderListInstance();
                String selectedGender = (String) filterGenderText.getText();
                if(filterSelectedGenderList.contains(selectedGender)) {
                    filterSelectedGenderList.remove(selectedGender);
                    filterGenderCheckBox.setChecked(false);
                }
                else {
                    filterSelectedGenderList.add(selectedGender);
                    filterGenderCheckBox.setChecked(true);
                }
                filterPetMateListInstance.setFilterGenderListInstance(filterSelectedGenderList);
            }
        }
    }
}
