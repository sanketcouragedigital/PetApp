package com.couragedigital.petapp.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.couragedigital.petapp.Connectivity.FilterFetchPetMateBreedList;
import com.couragedigital.petapp.PetMateListFilter;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.FilterPetMateListInstance;
import com.couragedigital.petapp.model.*;

import java.util.ArrayList;
import java.util.List;

public class FilterOptionsPetMateAdapter extends RecyclerView.Adapter<FilterOptionsPetMateAdapter.ViewHolder> {
    List<FilterOptionList> filterOptionsList;
    List<FilterOptionList> filterOptionsSelectedList;
    public RelativeLayout filterMenu;
    PetMateListFilter petMateListFilter;
    View v;
    ViewHolder viewHolder;

    int position = 0;
    int state = 0;
    int filterState = 0;

    public static FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();

    public FilterOptionsPetMateAdapter(List<FilterOptionList> filterOptionsList, List<FilterOptionList> filterOptionsSelectedList, RelativeLayout filterMenu, PetMateListFilter petMateListFilter) {
        this.filterOptionsList = filterOptionsList;
        this.filterOptionsSelectedList = filterOptionsSelectedList;
        this.filterMenu = filterMenu;
        this.petMateListFilter = petMateListFilter;
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

        View inflateFilterMenu;
        RecyclerView filterRecyclerViewMenu;
        TextView filterBreedEmptyView;
        FloatingActionButton applyFilterFAB;
        LinearLayoutManager layoutManager;

        List<FilterCategoryList> filterCategoryPetMateLists = new ArrayList<FilterCategoryList>();
        FilterCategoryPetMateAdapter filterCategoryPetMateAdapter;

        List<FilterBreedList> filterBreedPetMateLists = new ArrayList<FilterBreedList>();
        FilterBreedPetMateAdapter filterBreedPetMateAdapter;
        List<String> filterSelectedCategoryPetMateList = new ArrayList<String>();

        List<FilterAgeList> filterAgePetMateLists = new ArrayList<FilterAgeList>();
        FilterAgePetMateAdapter filterAgePetMateAdapter;

        List<FilterGenderList> filterGenderPetMateLists = new ArrayList<FilterGenderList>();
        FilterGenderPetMateAdapter filterGenderPetMateAdapter;

        public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
        public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
        public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
        public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();

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

            if (position == 0) {
                filterMenu.removeAllViews();
                filterCategoryPetMateLists.clear();

                filterCategoryPetMateAdapter = new FilterCategoryPetMateAdapter(filterCategoryPetMateLists);
                filterRecyclerViewMenu.setAdapter(filterCategoryPetMateAdapter);

                new FilterFetchPetMateCategory(filterCategoryPetMateAdapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterCategoryPetMateLists);

                filterMenu.addView(inflateFilterMenu);
            } else if (position == 1) {
                filterMenu.removeAllViews();

                filterBreedPetMateLists.clear();

                filterBreedPetMateAdapter = new FilterBreedPetMateAdapter(filterBreedPetMateLists);
                filterRecyclerViewMenu.setAdapter(filterBreedPetMateAdapter);

                filterSelectedCategoryPetMateList = filterPetMateListInstance.getFilterCategoryListInstance();
                new FilterFetchPetMateBreed(filterBreedPetMateAdapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterSelectedCategoryPetMateList, filterBreedPetMateLists);
                //FilterFetchPetBreedList.fetchPetBreeds(filterSelectedCategoryList, filterBreedLists, filterBreedAdapter);

                if(filterBreedPetMateLists.isEmpty() && filterSelectedCategoryPetMateList.isEmpty()) {
                    filterRecyclerViewMenu.setVisibility(View.GONE);
                    filterBreedEmptyView.setVisibility(View.VISIBLE);
                }
                else {
                    filterRecyclerViewMenu.setVisibility(View.VISIBLE);
                    filterBreedEmptyView.setVisibility(View.GONE);
                }
                filterMenu.addView(inflateFilterMenu);
            } else if (position == 2) {
                filterMenu.removeAllViews();

                filterAgePetMateLists.clear();

                filterAgePetMateAdapter = new FilterAgePetMateAdapter(filterAgePetMateLists);
                filterRecyclerViewMenu.setAdapter(filterAgePetMateAdapter);

                new FilterFetchPetMateAge(filterAgePetMateAdapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterAgePetMateLists);

                filterMenu.addView(inflateFilterMenu);
            } else if (position == 3) {
                filterMenu.removeAllViews();

                filterGenderPetMateLists.clear();

                filterGenderPetMateAdapter = new FilterGenderPetMateAdapter(filterGenderPetMateLists);
                filterRecyclerViewMenu.setAdapter(filterGenderPetMateAdapter);

                new FilterFetchPetMateGender(filterGenderPetMateAdapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterGenderPetMateLists);

                filterMenu.addView(inflateFilterMenu);
            }
        }

        public View.OnClickListener applyFilterFABClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FilterApplyList(filterPetMateListInstance, filterState).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterSelectedInstanceCategoryList, filterSelectedInstanceBreedList, filterSelectedInstanceAgeList, filterSelectedInstanceGenderList);
            }
        };

        private class FilterApplyList extends AsyncTask<List, Void, Integer> {

            List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
            List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
            List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
            List<String> filterSelectedInstanceGenderList = new ArrayList<String>();

            FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();
            Integer filterState;

            public FilterApplyList(FilterPetMateListInstance mfilterPetMateListInstance, int filterState) {
                this.filterPetMateListInstance = mfilterPetMateListInstance;
                this.filterState = filterState;
            }

            @Override
            protected Integer doInBackground(List... params) {
                filterSelectedInstanceCategoryList = params[0];
                filterSelectedInstanceBreedList = params[1];
                filterSelectedInstanceAgeList = params[2];
                filterSelectedInstanceGenderList = params[3];
                filterSelectedInstanceCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
                filterSelectedInstanceBreedList = filterPetMateListInstance.getFilterBreedListInstance();
                filterSelectedInstanceAgeList = filterPetMateListInstance.getFilterAgeListInstance();
                filterSelectedInstanceGenderList = filterPetMateListInstance.getFilterGenderListInstance();

                if(filterSelectedInstanceCategoryList.isEmpty() && filterSelectedInstanceBreedList.isEmpty() && filterSelectedInstanceAgeList.isEmpty() && filterSelectedInstanceGenderList.isEmpty()) {
                    filterState = 0;
                }
                else {
                    filterState = 1;
                }
                //petListFilter.finish();
                return filterState;
            }

            @Override
            protected void onPostExecute(Integer filterState) {
                //PetListFilter petListFilter = new PetListFilter();
                petMateListFilter.setFilterState(filterState);
                //petListFilter.finish();
            }
        }

        private class FilterFetchPetMateCategory extends AsyncTask<List, Void, Void> {

            FilterCategoryPetMateAdapter filterCategoryPetMateAdapter;
            List<FilterCategoryList> filterCategoryPetMateLists = new ArrayList<FilterCategoryList>();

            public FilterFetchPetMateCategory(FilterCategoryPetMateAdapter mfilterCategoryPetMateAdapter) {
                super();
                this.filterCategoryPetMateAdapter = mfilterCategoryPetMateAdapter;
            }

            @Override
            protected Void doInBackground(List... params) {
                filterCategoryPetMateLists = params[0];

                String[] filterCategoryText = new String[]{
                        "Dog", "Cat", "Rabbit", "Small & Furry", "Horse", "Bird", "Scales, Fins & Others", "Pig", "Barnyard"
                };
                for (int i = 0; i < filterCategoryText.length; i++) {
                    FilterCategoryList filterCategoryList = new FilterCategoryList();
                    filterCategoryList.setCategoryText(filterCategoryText[i]);
                    filterCategoryPetMateLists.add(filterCategoryList);
                }
                filterCategoryPetMateAdapter.notifyDataSetChanged();
                return null;
            }
        }

        private class FilterFetchPetMateBreed extends AsyncTask<List, Void, Void> {

            FilterBreedPetMateAdapter filterBreedPetMateAdapter;
            List<FilterBreedList> filterBreedPetMateLists = new ArrayList<FilterBreedList>();
            List<String> filterSelectedCategoryPetMateList = new ArrayList<String>();

            public FilterFetchPetMateBreed(FilterBreedPetMateAdapter mfilterBreedPetMateAdapter) {
                super();
                this.filterBreedPetMateAdapter = mfilterBreedPetMateAdapter;
            }

            @Override
            protected Void doInBackground(List... params) {
                filterSelectedCategoryPetMateList = params[0];
                filterBreedPetMateLists = params[1];
                FilterFetchPetMateBreedList.fetchPetMateBreeds(filterSelectedCategoryPetMateList, filterBreedPetMateLists, filterBreedPetMateAdapter);
                return null;
            }
        }

        private class FilterFetchPetMateAge extends AsyncTask<List, Void, Void> {

            FilterAgePetMateAdapter filterAgePetMateAdapter;
            List<FilterAgeList> filterAgePetMateLists = new ArrayList<FilterAgeList>();

            public FilterFetchPetMateAge(FilterAgePetMateAdapter mfilterAgePetMateAdapter) {
                super();
                this.filterAgePetMateAdapter = mfilterAgePetMateAdapter;
            }

            @Override
            protected Void doInBackground(List... params) {
                filterAgePetMateLists = params[0];

                String[] filterAgeText = new String[]{
                        "Age 0-100 Years"
                };
                for (int i = 0; i < filterAgeText.length; i++) {
                    FilterAgeList filterAgeList = new FilterAgeList();
                    filterAgeList.setAgeText(filterAgeText[i]);
                    filterAgePetMateLists.add(filterAgeList);
                }
                filterAgePetMateAdapter.notifyDataSetChanged();
                return null;
            }
        }

        private class FilterFetchPetMateGender extends AsyncTask<List, Void, Void> {

            FilterGenderPetMateAdapter filterGenderPetMateAdapter;
            List<FilterGenderList> filterGenderPetMateLists = new ArrayList<FilterGenderList>();

            public FilterFetchPetMateGender(FilterGenderPetMateAdapter mfilterGenderPetMateAdapter) {
                super();
                this.filterGenderPetMateAdapter = mfilterGenderPetMateAdapter;
            }

            @Override
            protected Void doInBackground(List... params) {
                filterGenderPetMateLists = params[0];

                String[] filterGender = new String[]{
                        "Male", "Female"
                };
                for (int i = 0; i < filterGender.length; i++) {
                    FilterGenderList filterGenderList = new FilterGenderList();
                    filterGenderList.setGender(filterGender[i]);
                    filterGenderPetMateLists.add(filterGenderList);
                }
                filterGenderPetMateAdapter.notifyDataSetChanged();
                return null;
            }
        }
    }
}
