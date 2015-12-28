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
import com.couragedigital.petapp.Singleton.FilterPetListInstance;
import com.couragedigital.petapp.model.*;

import java.util.ArrayList;
import java.util.List;

public class PetListFilter extends AppCompatActivity {

    RecyclerView petFilterRecyclerView;
    LinearLayoutManager layoutManager;
    public RelativeLayout filterMenu;

    final List<FilterOptionList> filterOptionsList = new ArrayList<FilterOptionList>();
    final List<FilterOptionList> filterOptionsSelectedList = new ArrayList<FilterOptionList>();
    FilterOptionsAdapter filterOptionsAdapter;
    private Toolbar petListFilterToolbar;

    FilterOptionsAdapter filterViewHolder;

    int filterState;

    public List<String> filterSelectedInstanceCategoryList = new ArrayList<String>();
    public List<String> filterSelectedInstanceBreedList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAgeList = new ArrayList<String>();
    public List<String> filterSelectedInstanceGenderList = new ArrayList<String>();
    public List<String> filterSelectedInstanceAdoptionAndPriceList = new ArrayList<String>();

    public FilterCategoryAdapter filterCategoryAdapterInstance;
    public FilterBreedAdapter filterBreedAdapterInstance;
    public FilterAgeAdapter filterAgeAdapterInstance;
    public FilterGenderAdapter filterGenderAdapterInstance;
    public FilterAdoptionAndPriceAdapter filterAdoptionAndPriceAdapterInstance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistfiltermenu);

        petListFilterToolbar = (Toolbar) findViewById(R.id.petListFilterToolbar);
        setSupportActionBar(petListFilterToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.filter_close);
        petListFilterToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        petFilterRecyclerView = (RecyclerView) findViewById(R.id.filterOptions);
        filterMenu = (RelativeLayout) findViewById(R.id.filterMenu);

        petFilterRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        petFilterRecyclerView.setLayoutManager(layoutManager);

        filterOptionsAdapter = new FilterOptionsAdapter(filterOptionsList, filterOptionsSelectedList, filterMenu, PetListFilter.this);

        petFilterRecyclerView.setAdapter(filterOptionsAdapter);

        int[] filterImages = new int[]{
                R.drawable.filter_category, R.drawable.filter_breed, R.drawable.filter_age, R.drawable.filter_gender, R.drawable.filter_price
        };

        for(int i = 0; i < filterImages.length; i++) {
            FilterOptionList filterOptionList = new FilterOptionList();
            filterOptionList.setImage(filterImages[i]);
            filterOptionsList.add(filterOptionList);
        }

        int[] filterSelectedImages = new int[]{
                R.drawable.filter_category_color, R.drawable.filter_breed_color, R.drawable.filter_age_color, R.drawable.filter_gender_color, R.drawable.filter_price_color
        };

        for(int i = 0; i < filterSelectedImages.length; i++) {
            FilterOptionList filterOptionList = new FilterOptionList();
            filterOptionList.setImage(filterSelectedImages[i]);
            filterOptionsSelectedList.add(filterOptionList);
        }
        filterOptionsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        PetListFilter.this.finish();
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
            FilterPetListInstance filterPetListInstance = new FilterPetListInstance();
            filterSelectedInstanceCategoryList = filterPetListInstance.getFilterCategoryListInstance();
            filterSelectedInstanceBreedList = filterPetListInstance.getFilterBreedListInstance();
            filterSelectedInstanceAgeList = filterPetListInstance.getFilterAgeListInstance();
            filterSelectedInstanceGenderList = filterPetListInstance.getFilterGenderListInstance();
            filterSelectedInstanceAdoptionAndPriceList = filterPetListInstance.getFilterAdoptionAndPriceListInstance();
            filterSelectedInstanceCategoryList.clear();
            filterSelectedInstanceBreedList.clear();
            filterSelectedInstanceAgeList.clear();
            filterSelectedInstanceGenderList.clear();
            filterSelectedInstanceAdoptionAndPriceList.clear();
            filterPetListInstance.setFilterCategoryListInstance(filterSelectedInstanceCategoryList);
            filterPetListInstance.setFilterBreedListInstance(filterSelectedInstanceBreedList);
            filterPetListInstance.setFilterAgeListInstance(filterSelectedInstanceAgeList);
            filterPetListInstance.setFilterGenderListInstance(filterSelectedInstanceGenderList);
            filterPetListInstance.setFilterAdoptionAndPriceListInstance(filterSelectedInstanceAdoptionAndPriceList);

            recreate();

            filterOptionsAdapter.notifyDataSetChanged();
        }
        return true;
    }

    public void setFilterState(int stateOfFilter) {
        this.filterState = stateOfFilter;
        PetListFilter.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                PetListFilter.this.finish();
            }
        });
    }
}