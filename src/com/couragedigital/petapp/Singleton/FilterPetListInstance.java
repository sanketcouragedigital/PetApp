package com.couragedigital.petapp.Singleton;

import java.util.ArrayList;
import java.util.List;

public class FilterPetListInstance {
    private static List<String> filterCategoryListInstance = new ArrayList<String>();
    private static List<String> filterBreedListInstance = new ArrayList<String>();
    private static List<String> filterAgeListInstance = new ArrayList<String>();
    private static List<String> filterGenderListInstance = new ArrayList<String>();
    private static List<String> filterAdoptionAndPriceListInstance = new ArrayList<String>();

    public static List<String> getFilterCategoryListInstance() {
        return filterCategoryListInstance;
    }

    public static void setFilterCategoryListInstance(List<String> filterCategoryList) {
        filterCategoryListInstance = filterCategoryList;
    }

    public static List<String> getFilterBreedListInstance() {
        return filterBreedListInstance;
    }

    public static void setFilterBreedListInstance(List<String> filterBreedList) {
        filterBreedListInstance = filterBreedList;
    }

    public static List<String> getFilterAgeListInstance() {
        return filterAgeListInstance;
    }

    public static void setFilterAgeListInstance(List<String> filterAgeList) {
        filterAgeListInstance = filterAgeList;
    }

    public static List<String> getFilterGenderListInstance() {
        return filterGenderListInstance;
    }

    public static void setFilterGenderListInstance(List<String> filterGenderList) {
        filterGenderListInstance = filterGenderList;
    }

    public static List<String> getFilterAdoptionAndPriceListInstance() {
        return filterAdoptionAndPriceListInstance;
    }

    public static void setFilterAdoptionAndPriceListInstance(List<String> filterAdoptionAndPriceList) {
        filterAdoptionAndPriceListInstance = filterAdoptionAndPriceList;
    }
}
