package com.couragedigital.petapp;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.couragedigital.petapp.Adapter.FilterOptionsAdapter;
import com.couragedigital.petapp.model.FilterOptionList;

import java.util.ArrayList;
import java.util.List;

public class PetListFilter extends BaseActivity {

    RecyclerView petFilterRecyclerView;
    LinearLayoutManager layoutManager;
    public RelativeLayout filterMenu;
    FloatingActionButton applyFilterFAB;
    final List<FilterOptionList> filterOptionsList = new ArrayList<FilterOptionList>();
    FilterOptionsAdapter filterOptionsAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistfiltermenu);

        petFilterRecyclerView = (RecyclerView) findViewById(R.id.filterOptions);
        filterMenu = (RelativeLayout) findViewById(R.id.filterMenu);
        applyFilterFAB = (FloatingActionButton) findViewById(R.id.applyFilterFAB);

        petFilterRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        petFilterRecyclerView.setLayoutManager(layoutManager);

        filterOptionsAdapter = new FilterOptionsAdapter(filterOptionsList, filterMenu);

        petFilterRecyclerView.setAdapter(filterOptionsAdapter);




        int[] filterImages = new int[]{
          R.drawable.filter_category_color, R.drawable.filter_breed, R.drawable.filter_age, R.drawable.filter_gender, R.drawable.filter_price
        };

        for(int i = 0; i < filterImages.length; i++) {
            FilterOptionList filterOptionList = new FilterOptionList();
            filterOptionList.setImage(filterImages[i]);
            filterOptionsList.add(filterOptionList);
        }
        filterOptionsAdapter.notifyDataSetChanged();



        //filterOptions.setOnItemClickListener(filterOptionsListClickListener);

        applyFilterFAB.setOnClickListener(applyFilterFABClick);
    }

    public AdapterView.OnItemClickListener filterOptionsListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(position == 0) {
            }
            else if(position == 1) {
            }
            else if(position == 2) {
            }
            else if(position == 3) {
            }
            else if(position == 4) {
            }
        }
    };

    public void changeBackgroundImage(View view, int position) {
        //filterOptionImage.setImageResource(position == 0 ? R.drawable.filter_category : R.drawable.filter_category);
    }

    public View.OnClickListener applyFilterFABClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}