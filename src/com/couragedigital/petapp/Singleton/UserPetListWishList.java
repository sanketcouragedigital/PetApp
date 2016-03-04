package com.couragedigital.petapp.Singleton;


import java.util.ArrayList;

public class UserPetListWishList {
    private static ArrayList<String> wishListPetListId = new ArrayList<String>();

    public static ArrayList<String> getPetListId() {
        return wishListPetListId;
    }
    public static void setPetListId(ArrayList<String> wlPetlistId) {
        wishListPetListId = wlPetlistId;
    }

}
