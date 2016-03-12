package com.couragedigital.petapp.Singleton;

import java.util.ArrayList;

public class UserPetListWishList {
    private static ArrayList<String> wishListPetListId = new ArrayList<String>();

    public static ArrayList<String> getPetWishList() {
        return wishListPetListId;
    }
    public static void setPetWishList(ArrayList<String> wlPetlistId) {
        wishListPetListId = wlPetlistId;
    }

}
