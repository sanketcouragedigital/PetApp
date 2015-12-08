package com.couragedigital.petapp.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.couragedigital.petapp.PetList;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.PetListInstance;
import com.couragedigital.petapp.model.FilterCategoryList;
import com.couragedigital.petapp.model.FilterGenderList;
import com.couragedigital.petapp.model.FilterOptionList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.view.*;

public class FilterOptionsAdapter extends RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder> {

    List<FilterOptionList> filterOptionsList;
    RelativeLayout filterMenu;
    View v;
    ViewHolder viewHolder;

    public static int selected_item = 0;

    public FilterOptionsAdapter(List<FilterOptionList> filterOptionsList, RelativeLayout filterMenu) {
        this.filterOptionsList = filterOptionsList;
        this.filterMenu = filterMenu;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.petfilteroptionsubitems, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        FilterOptionList filterOptionList = filterOptionsList.get(i);
        viewHolder.bindFilterOptionList(filterOptionList);
    }

    @Override
    public int getItemCount() {
        return filterOptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView filterOptionImage;
        private FilterOptionList filterOptionList;
        public View itemView;

        PetListInstance petListInstance;

        View inflateFilterMenu;
        RecyclerView filterRecyclerViewMenu;
        LinearLayoutManager layoutManager;

        final List<FilterCategoryList> filterCategoryLists = new ArrayList<FilterCategoryList>();
        FilterCategoryAdapter filterCategoryAdapter;

        final List<FilterGenderList> filterGenderLists = new ArrayList<FilterGenderList>();
        FilterGenderAdapter filterGenderAdapter;

        int position = 0;
        private ViewParent abc;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            filterOptionImage = (ImageView) itemView.findViewById(R.id.filterOptionImage);
            filterOptionImage.setOnClickListener(this);
        }

        public void bindFilterOptionList(FilterOptionList filterOptionList) {
            this.filterOptionList = filterOptionList;
            filterOptionImage.setImageResource(filterOptionList.getImage());
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.filterOptionImage) {
                LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                if(this.getAdapterPosition() == 0) {
                    filterOptionImage.setImageResource(R.drawable.filter_category_color);
                    inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);
                    filterRecyclerViewMenu = (RecyclerView) inflateFilterMenu.findViewById(R.id.filterRecyclerViewMenu);
                    filterRecyclerViewMenu.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(inflateFilterMenu.getContext());
                    filterRecyclerViewMenu.setLayoutManager(layoutManager);
                    filterCategoryAdapter = new FilterCategoryAdapter(filterCategoryLists);
                    filterRecyclerViewMenu.setAdapter(filterCategoryAdapter);

                    String[] filterCategoryText = new String[] {
                            "Dog", "Cat", "Rabbit", "Small & Furry", "Horse", "Bird", "Scales, Fins & Others", "Pig", "Barnyard"

                    };
                    for(int i = 0; i < filterCategoryText.length; i++) {
                        FilterCategoryList filterCategoryList = new FilterCategoryList();
                        filterCategoryList.setCategoryText(filterCategoryText[i]);
                        filterCategoryLists.add(filterCategoryList);
                    }
                    filterCategoryAdapter.notifyDataSetChanged();

                    filterMenu.addView(inflateFilterMenu);
                }
                else if(this.getAdapterPosition() == 1) {
                    filterMenu.removeView(inflateFilterMenu);
                    filterOptionImage.setImageResource(R.drawable.filter_breed_color);
                }
                else if(this.getAdapterPosition() == 2) {
                    filterOptionImage.setImageResource(R.drawable.filter_age_color);
                }
                else if(this.getAdapterPosition() == 3) {
                    filterOptionImage.setImageResource(R.drawable.filter_gender_color);
                    inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);
                    filterRecyclerViewMenu = (RecyclerView) inflateFilterMenu.findViewById(R.id.filterRecyclerViewMenu);
                    filterRecyclerViewMenu.setHasFixedSize(true);
                    layoutManager = new LinearLayoutManager(inflateFilterMenu.getContext());
                    filterRecyclerViewMenu.setLayoutManager(layoutManager);
                    filterGenderAdapter = new FilterGenderAdapter(filterGenderLists);
                    filterRecyclerViewMenu.setAdapter(filterGenderAdapter);

                    String[] filterGender = new String[] {
                            "Male", "Female"
                     };
                    for(int i = 0; i < filterGender.length; i++) {
                        FilterGenderList filterGenderList = new FilterGenderList();
                        filterGenderList.setGender(filterGender[i]);
                        filterGenderLists.add(filterGenderList);
                    }
                    filterGenderAdapter.notifyDataSetChanged();

                    filterMenu.addView(inflateFilterMenu);
                }
                else if(this.getAdapterPosition() == 4) {
                    filterOptionImage.setImageResource(R.drawable.filter_price_color);
                }
            }
        }

        /*public void changeOtherImageBackground(View v, int position) {
            if(position == 0) {
                filterOptionImage = petListInstance.getFilterOptionSelectedImage();
                if(filterOptionImage == null) {
                    filterOptionImage = (ImageView) itemView.findViewById(R.id.filterOptionImage);
                }
                filterOptionImage.setImageResource(R.drawable.filter_category);

            }
            else if(position == 1) {
                filterOptionImage = petListInstance.getFilterOptionSelectedImage();
                if(filterOptionImage == null) {
                    filterOptionImage = (ImageView) itemView.findViewById(R.id.filterOptionImage);
                }
                filterOptionImage.setImageResource(R.drawable.filter_breed);
            }
            else if(position == 2) {
                filterOptionImage.setImageResource(R.drawable.filter_age);
            }
            else if(position == 3) {
                filterOptionImage.setImageResource(R.drawable.filter_gender);
            }
            else if(position == 4) {
                filterOptionImage.setImageResource(R.drawable.filter_price);
            }
        }*/
    }
}
