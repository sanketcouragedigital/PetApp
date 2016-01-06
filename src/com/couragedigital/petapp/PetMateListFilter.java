package com.couragedigital.petapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.couragedigital.petapp.Adapter.*;
import com.couragedigital.petapp.Singleton.FilterPetMateListInstance;
import com.couragedigital.petapp.model.FilterOptionList;

import java.util.ArrayList;
import java.util.List;

public class PetMateListFilter extends AppCompatActivity {
    RecyclerView petMateFilterRecyclerView;
    LinearLayoutManager layoutManager;
    public RelativeLayout filterMenu;

    final List<FilterOptionList> filterOptionLists = new ArrayList<FilterOptionList>();
    final List<FilterOptionList> filterOptionsSelectedList = new ArrayList<FilterOptionList>();
    FilterOptionsPetMateAdapter filterOptionsPetMateAdapter;
    private Toolbar petMateListFilterToolbar;

    FilterOptionsAdapter filterViewHolder;

    int filterState;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();

    public FilterCategoryAdapter filterCategoryAdapterInstance;
    public FilterBreedAdapter filterBreedAdapterInstance;
    public FilterAgeAdapter filterAgeAdapterInstance;
    public FilterGenderAdapter filterGenderAdapterInstance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistfiltermenu);

        petMateListFilterToolbar = (Toolbar) findViewById(R.id.petListFilterToolbar);
        setSupportActionBar(petMateListFilterToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.filter_close);
        petMateListFilterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        petMateFilterRecyclerView = (RecyclerView) findViewById(R.id.filterOptions);
        filterMenu = (RelativeLayout) findViewById(R.id.filterMenu);

        petMateFilterRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        petMateFilterRecyclerView.setLayoutManager(layoutManager);

        filterOptionsPetMateAdapter = new FilterOptionsPetMateAdapter(filterOptionLists, filterOptionsSelectedList, filterMenu, PetMateListFilter.this);

        petMateFilterRecyclerView.setAdapter(filterOptionsPetMateAdapter);

        new FilterOptionsPetMate(filterOptionsPetMateAdapter).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, filterOptionLists, filterOptionsSelectedList);
    }

    @Override
    public void onBackPressed() {
        PetMateListFilter.this.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.petlistfiltermenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_filter_reset) {
            this.filterState = 0;
            FilterPetMateListInstance filterPetMateListInstance = new FilterPetMateListInstance();
            filterSelectedInstanceCategoryList = filterPetMateListInstance.getFilterCategoryListInstance();
            filterSelectedInstanceBreedList = filterPetMateListInstance.getFilterBreedListInstance();
            filterSelectedInstanceAgeList = filterPetMateListInstance.getFilterAgeListInstance();
            filterSelectedInstanceGenderList = filterPetMateListInstance.getFilterGenderListInstance();
            filterSelectedInstanceCategoryList.clear();
            filterSelectedInstanceBreedList.clear();
            filterSelectedInstanceAgeList.clear();
            filterSelectedInstanceGenderList.clear();
            filterPetMateListInstance.setFilterCategoryListInstance(filterSelectedInstanceCategoryList);
            filterPetMateListInstance.setFilterBreedListInstance(filterSelectedInstanceBreedList);
            filterPetMateListInstance.setFilterAgeListInstance(filterSelectedInstanceAgeList);
            filterPetMateListInstance.setFilterGenderListInstance(filterSelectedInstanceGenderList);

            recreate();

            filterOptionsPetMateAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public void setFilterState(int stateOfFilter) {
        this.filterState = stateOfFilter;
        Intent filterIntent = new Intent();
        filterIntent.putExtra("Filter_State", this.filterState);
        setResult(RESULT_OK, filterIntent);
        PetMateListFilter.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PetMateListFilter.this.finish();
            }
        });
        //PetListFilter.this.finish();
    }

    private class FilterOptionsPetMate extends AsyncTask<List, Void, Void> {

        FilterOptionsPetMateAdapter filterOptionsPetMateAdapter;
        List<FilterOptionList> filterOptionLists = new ArrayList<FilterOptionList>();
        List<FilterOptionList> filterOptionSelectedList = new ArrayList<FilterOptionList>();

        public FilterOptionsPetMate(FilterOptionsPetMateAdapter mfilterOptionsPetMateAdapter) {
            super();
            this.filterOptionsPetMateAdapter = mfilterOptionsPetMateAdapter;
        }

        @Override
        protected Void doInBackground(List... params) {
            filterOptionLists = params[0];
            filterOptionSelectedList = params[1];

            int[] filterImages = new int[]{
                    R.drawable.filter_category, R.drawable.filter_breed, R.drawable.filter_age, R.drawable.filter_gender
            };

            for(int i = 0; i < filterImages.length; i++) {
                FilterOptionList filterOptionList = new FilterOptionList();
                filterOptionList.setImage(filterImages[i]);
                filterOptionLists.add(filterOptionList);
            }

            int[] filterSelectedImages = new int[]{
                    R.drawable.filter_category_color, R.drawable.filter_breed_color, R.drawable.filter_age_color, R.drawable.filter_gender_color
            };

            for(int i = 0; i < filterSelectedImages.length; i++) {
                FilterOptionList filterOptionList = new FilterOptionList();
                filterOptionList.setImage(filterSelectedImages[i]);
                filterOptionsSelectedList.add(filterOptionList);
            }
            filterOptionsPetMateAdapter.notifyDataSetChanged();
            return null;
        }
    }
}
