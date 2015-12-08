package com.couragedigital.petapp.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.model.FilterGenderList;

import java.util.List;

public class FilterGenderAdapter extends RecyclerView.Adapter<FilterGenderAdapter.ViewHolder> {
    List<FilterGenderList> filterGenderLists;
    View v;
    ViewHolder viewHolder;

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
            filterGenderCheckBox.setChecked(false);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
