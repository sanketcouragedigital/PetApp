package com.couragedigital.petapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import com.couragedigital.petapp.Adapter.FilterPetListAdapter;
import com.couragedigital.petapp.model.FilterPetList;
import com.couragedigital.petapp.model.PetList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PetListFilter extends AppCompatActivity {

    private List<String> petFilterCategoryList = new ArrayList<String>();
    String[] petFilterCategoryArrayList;
    ArrayAdapter<String> petListFilterAdapter;

    ListView filterOptionsList;
    RelativeLayout filterMenu;

    private List<FilterPetList> filterPetLists = new ArrayList<FilterPetList>();
    private FilterPetList[] filterPetList;
    FilterPetListAdapter filterPetListAdapter;
    ListView filterListMenu;

    List<String> filterSelectedMenus = new ArrayList<String>();

    private List<PetList> petLists = new ArrayList<PetList>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.petlistfiltermenu);

        filterOptionsList = (ListView) findViewById(R.id.filterOptionsList);
        filterMenu = (RelativeLayout) findViewById(R.id.filterMenu);

        petFilterCategoryArrayList = new String[]{
                "Category", "Breed", "Age", "Gender", "Listing Type"
        };
        petFilterCategoryList = new ArrayList<>(Arrays.asList(petFilterCategoryArrayList));
        petListFilterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, petFilterCategoryList);
        filterOptionsList.setAdapter(petListFilterAdapter);

        filterOptionsList.setOnItemClickListener(filterListClickListener);
    }

    AdapterView.OnItemClickListener filterListClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String listValue = filterOptionsList.getItemAtPosition(position).toString();
            LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            filterMenu.removeAllViews();
            if(listValue.equals("Category")) {
                final View inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);

                /*CheckBox petCategoryDog = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryDog);
                CheckBox petCategoryCat = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryCat);
                CheckBox petCategoryRabbit = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryRabbit);
                CheckBox petCategorySmallAndFurry = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategorySmallAndFurry);
                CheckBox petCategoryHorse = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryHorse);
                CheckBox petCategoryBird = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryBird);
                CheckBox petCategoryScalesFinsAndOthers = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryScalesFinsAndOthers);
                CheckBox petCategoryPig = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryPig);
                CheckBox petCategoryBarnyard = (CheckBox) inflateFilterMenu.findViewById(R.id.petCategoryBarnyard);*/
                filterMenu.addView(inflateFilterMenu);
            }
            else if(listValue.equals("Breed")) {
                final View inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);

                filterMenu.addView(inflateFilterMenu);
            }
            else if(listValue.equals("Age")) {
                final View inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);

                /*CheckBox zeroToFiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.zeroToFiveAge);
                CheckBox sixToTenAge = (CheckBox) inflateFilterMenu.findViewById(R.id.sixToTenAge);
                CheckBox elevenToFifteenAge = (CheckBox) inflateFilterMenu.findViewById(R.id.elevenToFifteenAge);
                CheckBox sixteenToTwentyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.sixteenToTwentyAge);
                CheckBox twentyoneToTwentyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.twentyoneToTwentyfiveAge);
                CheckBox twentysixToThirtyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.twentysixToThirtyAge);
                CheckBox thirtyoneToThirtyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.thirtyoneToThirtyfiveAge);
                CheckBox thirtysixToFourtyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.thirtysixToFourtyAge);
                CheckBox fourtyoneToFourtyFiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.fourtyoneToFourtyFiveAge);
                CheckBox fourtysixToFiftyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.fourtysixToFiftyAge);
                CheckBox fiftyoneToFiftyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.fiftyoneToFiftyfiveAge);
                CheckBox fiftysixToSixtyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.fiftysixToSixtyAge);
                CheckBox sixtyoneToSixtyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.sixtyoneToSixtyfiveAge);
                CheckBox sixtysixToSeventyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.sixtysixToSeventyAge);
                CheckBox seventyoneToSeventyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.seventyoneToSeventyfiveAge);
                CheckBox seventysixToEightyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.seventysixToEightyAge);
                CheckBox eightyoneToEightyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.eightyoneToEightyfiveAge);
                CheckBox eightysixToNintyAge = (CheckBox) inflateFilterMenu.findViewById(R.id.eightysixToNintyAge);
                CheckBox ninetyoneToNinetyfiveAge = (CheckBox) inflateFilterMenu.findViewById(R.id.ninetyoneToNinetyfiveAge);
                CheckBox ninetysixToHundredAge = (CheckBox) inflateFilterMenu.findViewById(R.id.ninetysixToHundredAge);*/
                filterMenu.addView(inflateFilterMenu);
            }
            else if(listValue.equals("Gender")) {
                final View inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);

                filterListMenu = (ListView) inflateFilterMenu.findViewById(R.id.filterListviewMenu);
                filterPetLists = new ArrayList<FilterPetList>();
                filterPetList = null;
                if (filterPetList == null) {
                    filterPetList = new FilterPetList[] { new FilterPetList("Male"), new FilterPetList("Female") };
                }
                filterPetLists.addAll(Arrays.asList(filterPetList));
                filterPetListAdapter = new FilterPetListAdapter(inflateFilterMenu, filterPetLists, filterSelectedMenus);
                filterListMenu.setAdapter(filterPetListAdapter);

                filterMenu.addView(inflateFilterMenu);
            }
            else if(listValue.equals("Listing Type")) {
                final View inflateFilterMenu = inflater.inflate(R.layout.petlistfilterviewmenu, null);

                filterListMenu = (ListView) inflateFilterMenu.findViewById(R.id.filterListviewMenu);
                filterPetLists = new ArrayList<FilterPetList>();
                filterPetList = null;
                if (filterPetList == null) {
                    filterPetList = new FilterPetList[] { new FilterPetList("For Adoption"), new FilterPetList("Give Away"),
                            new FilterPetList("0-1000"), new FilterPetList("1001-5000"),
                            new FilterPetList("5001-10000"), new FilterPetList("10001-15000"),
                            new FilterPetList("15001-20000"), new FilterPetList("20001-50000"),
                            new FilterPetList("50001-100000") };
                }
                filterPetLists.addAll(Arrays.asList(filterPetList));
                filterPetListAdapter = new FilterPetListAdapter(inflateFilterMenu, filterPetLists, filterSelectedMenus);
                filterListMenu.setAdapter(filterPetListAdapter);

                filterMenu.addView(inflateFilterMenu);
            }
        }
    };

    /*public static class FilterableData {
        public List<PetList> petListsForFilter = new ArrayList<PetList>();
        public PetListAdapter adapterForFilter;

        public void onRestoreInstanceState(Bundle savedInstanceState){
            petListsForFilter = savedInstanceState.getParcelable("PET_LIST");
            adapterForFilter = savedInstanceState.getParcelable("PET_ADAPTER");
        }

        public Adapter getAdapter() {
            return adapterForFilter;
        }

        public List<PetList> getPetLists() {
            return petListsForFilter;
        }
    }*/
}