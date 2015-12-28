package com.couragedigital.petapp.Adapter;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.Connectivity.FilterFetchPetBreedList;
import com.couragedigital.petapp.PetListFilter;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.FilterPetListInstance;
import com.couragedigital.petapp.Singleton.PetListInstance;
import com.couragedigital.petapp.model.*;

import java.util.ArrayList;
import java.util.List;

public class FilterOptionsAdapter extends RecyclerView.Adapter<FilterOptionsAdapter.ViewHolder> {

    List<FilterOptionList> filterOptionsList;
    List<FilterOptionList> filterOptionsSelectedList;
    public RelativeLayout filterMenu;
    PetListFilter petListFilter;
    View v;
    ViewHolder viewHolder;

    int position = 0;
    int state = 0;
    int filterState = 0;

    public static FilterPetListInstance filterPetListInstance = new FilterPetListInstance();

    public FilterOptionsAdapter(List<FilterOptionList> filterOptionsList, List<FilterOptionList> filterOptionsSelectedList, RelativeLayout filterMenu, PetListFilter petListFilter) {
        this.filterOptionsList = filterOptionsList;
        this.filterOptionsSelectedList = filterOptionsSelectedList;
        this.filterMenu = filterMenu;
        this.petListFilter = petListFilter;
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
        FilterOptionList filterOptionSelectedList = filterOptionsSelectedList.get(i);
        viewHolder.bindFilterOptionList(filterOptionList, filterOptionSelectedList);
    }

    @Override
    public int getItemCount() {
        return filterOptionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView filterOptionImage;
        private FilterOptionList filterOptionList;
        private FilterOptionList filterOptionSelectedList;
        public View itemView;

        PetListInstance petListInstance;

        View inflateFilterMenu;
        RecyclerView filterRecyclerViewMenu;
        TextView filterBreedEmptyView;
        FloatingActionButton applyFilterFAB;
        LinearLayoutManager layoutManager;

        List<FilterCategoryList> filterCategoryLists = new ArrayList<FilterCategoryList>();
        FilterCategoryAdapter filterCategoryAdapter;

        List<FilterBreedList> filterBreedLists = new ArrayList<FilterBreedList>();
        FilterBreedAdapter filterBreedAdapter;
        List<String> filterSelectedCategoryList = new ArrayList<String>();

        List<FilterAgeList> filterAgeLists = new ArrayList<FilterAgeList>();
        FilterAgeAdapter filterAgeAdapter;

        List<FilterGenderList> filterGenderLists = new ArrayList<FilterGenderList>();
        FilterGenderAdapter filterGenderAdapter;

        List<FilterAdoptionAndPriceList> filterAdoptionAndPriceLists = new ArrayList<FilterAdoptionAndPriceList>();
        FilterAdoptionAndPriceAdapter filterAdoptionAndPriceAdapter;

        public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
        public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
        public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
        public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();
        public List<String> filterSelectedInstanceAdoptionAndPriceList = new ArrayList<String>();

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            filterOptionImage = (ImageView) itemView.findViewById(R.id.filterOptionImage);
            filterOptionImage.setOnClickListener(this);
        }

        public void bindFilterOptionList(FilterOptionList filterOptionList, FilterOptionList filterOptionSelectedList) {
            this.filterOptionList = filterOptionList;
            this.filterOptionSelectedList = filterOptionSelectedList;
            filterOptionImage.setImageResource(filterOptionList.getImage());
            filterOptionImage.setEnabled(true);

            if(position == 0 && filterOptionList.getImage() == R.drawable.filter_category) {
                filterOptionImage.setImageResource(filterOptionSelectedList.getImage());
                if(state == 0) {
                    state++;
                    fetchFilterMenusOptionWise(position);
                }
            }
            else if(position == 1 && filterOptionList.getImage() == R.drawable.filter_breed) {
                filterOptionImage.setImageResource(filterOptionSelectedList.getImage());
            }
            else if(position == 2 && filterOptionList.getImage() == R.drawable.filter_age) {
                filterOptionImage.setImageResource(filterOptionSelectedList.getImage());
            }
            else if(position == 3 && filterOptionList.getImage() == R.drawable.filter_gender) {
                filterOptionImage.setImageResource(filterOptionSelectedList.getImage());
            }
            else if(position == 4 && filterOptionList.getImage() == R.drawable.filter_price) {
                filterOptionImage.setImageResource(filterOptionSelectedList.getImage());
            }
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.filterOptionImage) {
                position = this.getAdapterPosition();
                fetchFilterMenusOptionWise(position);
                notifyDataSetChanged();
            }
        }

        public void fetchFilterMenusOptionWise(int position) {
            LayoutInflater inflater = (LayoutInflater) v.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);
            filterRecyclerViewMenu = (RecyclerView) inflateFilterMenu.findViewById(R.id.filterRecyclerViewMenu);
            filterBreedEmptyView = (TextView) inflateFilterMenu.findViewById(R.id.filterBreedEmptyView);
            applyFilterFAB = (FloatingActionButton) inflateFilterMenu.findViewById(R.id.applyFilterFAB);

            applyFilterFAB.setOnClickListener(applyFilterFABClick);

            layoutManager = new LinearLayoutManager(inflateFilterMenu.getContext());
            filterRecyclerViewMenu.setLayoutManager(layoutManager);

            if (this.getAdapterPosition() == 0) {
                filterMenu.removeAllViews();

                filterCategoryLists.clear();

                filterCategoryAdapter = new FilterCategoryAdapter(filterCategoryLists);
                filterRecyclerViewMenu.setAdapter(filterCategoryAdapter);

                String[] filterCategoryText = new String[]{
                        "Dog", "Cat", "Rabbit", "Small & Furry", "Horse", "Bird", "Scales, Fins & Others", "Pig", "Barnyard"
                };
                for (int i = 0; i < filterCategoryText.length; i++) {
                    FilterCategoryList filterCategoryList = new FilterCategoryList();
                    filterCategoryList.setCategoryText(filterCategoryText[i]);
                    filterCategoryLists.add(filterCategoryList);
                }
                filterCategoryAdapter.notifyDataSetChanged();
                filterMenu.addView(inflateFilterMenu);
            } else if (this.getAdapterPosition() == 1) {
                filterMenu.removeAllViews();

                filterBreedLists.clear();

                filterBreedAdapter = new FilterBreedAdapter(filterBreedLists);
                filterRecyclerViewMenu.setAdapter(filterBreedAdapter);

                filterSelectedCategoryList = FilterPetListInstance.getFilterCategoryListInstance();
                FilterFetchPetBreedList.fetchPetBreeds(filterSelectedCategoryList, filterBreedLists, filterBreedAdapter);

                if(filterBreedLists.isEmpty() && filterSelectedCategoryList.isEmpty()) {
                    filterRecyclerViewMenu.setVisibility(View.GONE);
                    filterBreedEmptyView.setVisibility(View.VISIBLE);
                }
                else {
                    filterRecyclerViewMenu.setVisibility(View.VISIBLE);
                    filterBreedEmptyView.setVisibility(View.GONE);
                }
                filterMenu.addView(inflateFilterMenu);
            } else if (this.getAdapterPosition() == 2) {
                filterMenu.removeAllViews();

                filterAgeLists.clear();

                filterAgeAdapter = new FilterAgeAdapter(filterAgeLists);
                filterRecyclerViewMenu.setAdapter(filterAgeAdapter);
                String[] filterAgeText = new String[]{
                        "Age 0-100 Years"
                };
                for (int i = 0; i < filterAgeText.length; i++) {
                    FilterAgeList filterAgeList = new FilterAgeList();
                    filterAgeList.setAgeText(filterAgeText[i]);
                    filterAgeLists.add(filterAgeList);
                }
                filterAgeAdapter.notifyDataSetChanged();
                filterMenu.addView(inflateFilterMenu);
            } else if (this.getAdapterPosition() == 3) {
                filterMenu.removeAllViews();

                filterGenderLists.clear();

                filterGenderAdapter = new FilterGenderAdapter(filterGenderLists);
                filterRecyclerViewMenu.setAdapter(filterGenderAdapter);

                String[] filterGender = new String[]{
                        "Male", "Female"
                };
                for (int i = 0; i < filterGender.length; i++) {
                    FilterGenderList filterGenderList = new FilterGenderList();
                    filterGenderList.setGender(filterGender[i]);
                    filterGenderLists.add(filterGenderList);
                }
                filterGenderAdapter.notifyDataSetChanged();
                filterMenu.addView(inflateFilterMenu);
            } else if (this.getAdapterPosition() == 4) {
                filterMenu.removeAllViews();

                filterAdoptionAndPriceLists.clear();

                filterAdoptionAndPriceAdapter = new FilterAdoptionAndPriceAdapter(filterAdoptionAndPriceLists);
                filterRecyclerViewMenu.setAdapter(filterAdoptionAndPriceAdapter);
                String[] filterPriceText = new String[]{
                        "For Adoption", "Price", "0 - 10000", "10000 - 25000", "25000 - 50000", "50000 Onwards"
                };
                for (int i = 0; i < filterPriceText.length; i++) {
                    FilterAdoptionAndPriceList filterAdoptionAndPriceList = new FilterAdoptionAndPriceList();
                    filterAdoptionAndPriceList.setAdoptionAndPriceText(filterPriceText[i]);
                    filterAdoptionAndPriceLists.add(filterAdoptionAndPriceList);
                }
                filterAdoptionAndPriceAdapter.notifyDataSetChanged();
                filterMenu.addView(inflateFilterMenu);
            }
        }

        public View.OnClickListener applyFilterFABClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterSelectedInstanceCategoryList = filterPetListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetListInstance.getFilterGenderListInstance();
                filterSelectedInstanceAdoptionAndPriceList = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
                PetListFilter petListFilter = new PetListFilter();
                if(filterSelectedInstanceCategoryList.isEmpty() && filterSelectedInstanceBreedList.isEmpty() && filterSelectedInstanceAgeList.isEmpty() && filterSelectedInstanceGenderList.isEmpty() && filterSelectedInstanceAdoptionAndPriceList.isEmpty()) {
                    filterState = 0;
                    petListFilter.setFilterState(filterState);
                }
                else {
                    filterState = 1;
                    petListFilter.setFilterState(filterState);
                }
            }
        };
    }
}
