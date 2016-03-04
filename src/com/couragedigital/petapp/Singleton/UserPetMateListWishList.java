package com.couragedigital.petapp.Singleton;


import java.util.ArrayList;

public class UserPetMateListWishList {

    private static ArrayList<String> wishListPetMateListId = new ArrayList<String>();

    public static ArrayList<String> getPetMateListId() {
        return wishListPetMateListId;
    }
    public static void setPetMateListId(ArrayList<String> wlPetMatelistId) {
        wishListPetMateListId = wlPetMatelistId;
    }

}
