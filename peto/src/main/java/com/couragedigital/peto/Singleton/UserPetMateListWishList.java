package com.couragedigital.peto.Singleton;

import java.util.ArrayList;

public class UserPetMateListWishList {

    private static ArrayList<String> wishListPetMateListId = new ArrayList<String>();

    public static ArrayList<String> getPetMateWishList() {
        return wishListPetMateListId;
    }
    public static void setPetMateWishList(ArrayList<String> wlPetMatelistId) {
        wishListPetMateListId = wlPetMatelistId;
    }

}
