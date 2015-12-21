package com.couragedigital.petapp.Singleton;

import java.util.ArrayList;
import java.util.List;

public class FilterPetListInstance {
    private static List<String> filterCategoryListInstance = new ArrayList<String>();
    private static List<String> filterGenderListInstance = new ArrayList<String>();

    public static List<String> getFilterCategoryListInstance() {
        return filterCategoryListInstance;
    }

    public static void setFilterCategoryListInstance(List<String> filterCategoryList) {
        filterCategoryListInstance = filterCategoryList;
    }

    public static List<String> getFilterGenderListInstance() {
        return filterGenderListInstance;
    }

    public static void setFilterGenderListInstance(List<String> filterGenderList) {
        filterGenderListInstance = filterGenderList;
    }
}
