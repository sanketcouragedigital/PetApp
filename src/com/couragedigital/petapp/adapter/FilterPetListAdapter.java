package com.couragedigital.petapp.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.couragedigital.petapp.Holder.FilterListViewHolder;
import com.couragedigital.petapp.PetList;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.Singleton.PetListInstance;
import com.couragedigital.petapp.model.FilterPetList;

import java.util.ArrayList;
import java.util.List;

public class FilterPetListAdapter extends ArrayAdapter<FilterPetList> implements Filterable {

    private Activity activity;
    private View inflateFilterMenu;
    private LayoutInflater inflater;
    CheckBox filterCheckBox;
    TextView filterText;

    List<String> filterSelectedMenus = new ArrayList<String>();

    PetList petList = new PetList();
    PetListAdapter adapter;
    private List<com.couragedigital.petapp.model.PetList> petLists = new ArrayList<com.couragedigital.petapp.model.PetList>();
    private List<com.couragedigital.petapp.model.PetList> filterPetLists = new ArrayList<com.couragedigital.petapp.model.PetList>();
    private ListView petListView;

    String selectedFilterValue;

    public FilterPetListAdapter(View inflateFilter, List<FilterPetList> filterPetLists, List<String> filterSelectedMenus) {
        super(inflateFilter.getContext(), R.layout.petlistfiltercontent, R.id.filterListText, filterPetLists);
        //this.activity = (Activity) inflateFilter.getContext();
        inflater = LayoutInflater.from(inflateFilter.getContext());
        //this.filterPetLists = filterPetLists;
        this.filterSelectedMenus = filterSelectedMenus;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FilterPetList filterPetList = (FilterPetList) this.getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.petlistfiltercontent, null);



            filterText = (TextView) convertView.findViewById(R.id.filterListText);
            filterCheckBox = (CheckBox) convertView.findViewById(R.id.filterListCheckBox);

            convertView.setTag(new FilterListViewHolder(filterText, filterCheckBox));

            // If CheckBox is toggled, update the planet it is tagged with.
            filterCheckBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    FilterPetList filter = (FilterPetList) cb.getTag();
                    filter.setChecked(cb.isChecked());
                    if(cb.isChecked()) {
                        //filterSelectedMenus.add(filter.toString());
                        selectedFilterValue = filter.toString();
                        filter(selectedFilterValue);
                    }
                    else {
                        //filterSelectedMenus.remove(filter.toString());
                    }
                  }
            });
        }
        // Re-use existing row view
        else {

            FilterListViewHolder viewHolder = (FilterListViewHolder) convertView
                    .getTag();
            filterCheckBox = viewHolder.getCheckBox();
            filterText = viewHolder.getTextView();
        }

        filterCheckBox.setTag(filterPetList);

        // Display planet data
        filterCheckBox.setChecked(filterPetList.isChecked());
        filterText.setText(filterPetList.getName());

        return convertView;
    }

    public void filter(String selectedFilterValue) {
        //PetList activity Adapter and petlists call
        adapter = PetListInstance.getAdapterInstance();
        petLists = PetListInstance.getListInstance();
        filterPetLists = PetListInstance.getListInstance();

        if (selectedFilterValue.length() == 0) {
            petLists = filterPetLists;
        }
        else
        {
            String filterableString ;
            final ArrayList<com.couragedigital.petapp.model.PetList> filterListValue = new ArrayList<com.couragedigital.petapp.model.PetList>(petLists.size());
            for (int i = 0; i < petLists.size(); i++) {
                filterableString = ""+petLists.get(i).getPetGender();
                if (filterableString.contains(selectedFilterValue)) {
                    com.couragedigital.petapp.model.PetList petList = petLists.get(i);
                    filterListValue.add(petList);
                }
            }
            petLists = (ArrayList<com.couragedigital.petapp.model.PetList>) filterListValue;
        }
        //Adapter.notifyDataSetChanged();
        PetListInstance petListInstance = new PetListInstance(petLists);
    }
}
