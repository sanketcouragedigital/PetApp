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
import com.couragedigital.peto.model.FilterCategoryList;

import java.util.ArrayList;
import java.util.List;

public class FilterCategoryPetMateAdapter extends RecyclerView.Adapter<FilterCategoryPetMateAdapter.ViewHolder> {

    List<FilterCategoryList> filterCategoryPetMateLists;
    View v;
    ViewHolder viewHolder;

    public static FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();

    public FilterCategoryPetMateAdapter(List<FilterCategoryList> filterCategoryPetMateLists) {
        this.filterCategoryPetMateLists = filterCategoryPetMateLists;
    }

    @Override
    public FilterCategoryPetMateAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petlistfiltercategory, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterCategoryPetMateAdapter.ViewHolder viewHolder, int i) {
        FilterCategoryList filterCategoryList = filterCategoryPetMateLists.get(i);
        viewHolder.bindFilterCategoryPetMateList(filterCategoryList);
    }

    @Override
    public int getItemCount() {
        return filterCategoryPetMateLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout filterCategoryLayout;
        TextView filterCategoryText;
        CheckBox filterCategoryCheckBox;

        private FilterCategoryList filterCategoryList;

        public List<String> filterSelectedCategoryList = new ArrayList<String>();

        public ViewHolder(View itemView) {
            super(itemView);
            filterCategoryLayout = (RelativeLayout) itemView.findViewById(R.id.filterCategoryLayout);
            filterCategoryText = (TextView) itemView.findViewById(R.id.filterCategoryText);
            filterCategoryCheckBox = (CheckBox) itemView.findViewById(R.id.filterCategoryCheckBox);

            filterCategoryLayout.setOnClickListener(this);
        }

        public void bindFilterCategoryPetMateList(FilterCategoryList filterCategoryList) {
            this.filterCategoryList = filterCategoryList;
            filterCategoryText.setText(filterCategoryList.getCategoryText());
            filterSelectedCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
            if(filterSelectedCategoryList.contains(filterCategoryList.getCategoryText())) {
                filterCategoryCheckBox.setChecked(true);
            }
            else {
                filterCategoryCheckBox.setChecked(false);
            }
            filterCategoryCheckBox.setClickable(false);
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.filterCategoryLayout) {
                filterSelectedCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
                String selectedCategory = (String) filterCategoryText.getText();
                if(filterSelectedCategoryList.contains(selectedCategory)) {
                    filterSelectedCategoryList.remove(selectedCategory);
                    filterCategoryCheckBox.setChecked(false);
                }
                else {
                    filterSelectedCategoryList.add(selectedCategory);
                    filterCategoryCheckBox.setChecked(true);
                }
                filterPetMateListInstance.setFilterCategoryListInstance(filterSelectedCategoryList);
            }
        }
    }
}
