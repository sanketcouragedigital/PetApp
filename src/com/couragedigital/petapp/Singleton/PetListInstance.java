package com.couragedigital.petapp.Singleton;

import android.widget.ListView;
import com.couragedigital.petapp.Adapter.PetListAdapter;
import com.couragedigital.petapp.model.PetListItems;

import java.util.ArrayList;
import java.util.List;

public class PetListInstance {
    private static PetListAdapter adapterInstance;
    private static List<PetListItems> listInstance = new ArrayList<PetListItems>();
    private static ListView petListView;

    public PetListInstance(PetListAdapter adapter, List<PetListItems> petList, ListView petlistView) {
        adapterInstance = adapter;
        listInstance = petList;
        petListView = petlistView;
    }

    public PetListInstance(List<PetListItems> petLists) {
        listInstance = petLists;
    }

    public static PetListAdapter getAdapterInstance() {
        return adapterInstance;
    }

    public static List<PetListItems> getListInstance() {
        return listInstance;
    }

    public static ListView getListViewInstance() {
        return petListView;
    }

}