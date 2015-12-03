package com.couragedigital.petapp.Singleton;

import android.widget.ListView;
import com.couragedigital.petapp.Adapter.PetListAdapter;
import com.couragedigital.petapp.model.PetList;

import java.util.ArrayList;
import java.util.List;

public class PetListInstance {
    private static PetListAdapter adapterInstance;
    private static List<PetList> listInstance = new ArrayList<PetList>();
    private static ListView petListView;

    public PetListInstance(PetListAdapter adapter, List<PetList> petList, ListView petlistView) {
        adapterInstance = adapter;
        listInstance = petList;
        petListView = petlistView;
    }

    public PetListInstance(List<PetList> petLists) {
        listInstance = petLists;
    }

    public static PetListAdapter getAdapterInstance() {
        return adapterInstance;
    }

    public static List<PetList> getListInstance() {
        return listInstance;
    }

    public static ListView getListViewInstance() {
        return petListView;
    }

}