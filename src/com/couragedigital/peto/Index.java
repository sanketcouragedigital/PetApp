package com.couragedigital.peto;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.couragedigital.peto.Adapter.HomeListAdapter;
import com.couragedigital.peto.model.DialogListInformaion;
import com.couragedigital.peto.model.IndexListInfo;

import java.util.ArrayList;
import java.util.List;

public class Index extends BaseActivity {

    public ArrayList<IndexListInfo> indexListInfosArray;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeListAdapter mAdapter;

    private static Index instance = new Index();
    static Context context;

    public List<DialogListInformaion> dialogListForViewPets = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListForViewPetMets = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListForpetClinic = new ArrayList<DialogListInformaion>();
    public CoordinatorLayout homeListCoordinatorLayout;

    public static Index getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        homeListMenu();

        boolean checkLogin = sessionManager.isLoggedIn();
        if (!checkLogin) {
            // start your home screen
            Intent intent = new Intent(Index.this, SignIn.class);
            this.startActivity(intent);
            this.finish();
        }
    }
    
    private void homeListMenu() {

        homeListCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.homeListCoordinatorLayout);

        //Home menus Tittle Names
        final String[] titlename = new String[]{"Pets", "Pet Mate", "Clinics", "Services"};
        final String[] description = new String[]{"Buy, Adopt or Sell", "Find your pet the perfect partner", "Nearby Pet Clinics", "All kinds of services for your pet"};
        final int[] background = {R.drawable.pet_view_list, R.drawable.pet_mate, R.drawable.pet_doctors, R.drawable.pet_accessories};
        indexListInfosArray = new ArrayList<IndexListInfo>();
        for (int i = 0; i < titlename.length; i++) {
            IndexListInfo lf = new IndexListInfo();
            lf.setTittle(titlename[i]);
            lf.setDescription(description[i]);
            lf.setThumbnail(background[i]);
            indexListInfosArray.add(lf);
        }

        //View/List of pets Dailogbox Menu tittles Names
        final String[] title = new String[]{"Upload Details for adoption or selling", "List of Pets for sale & adoption"};
        final int[] icons = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title.length; i++) {
            DialogListInformaion dialogListInformaion1 = new DialogListInformaion();
            dialogListInformaion1.setTittle(title[i]);
            dialogListInformaion1.setIcons(icons[i]);
            dialogListForViewPets.add(dialogListInformaion1);
        }

        //Mating Dailogbox Menu tittles Names
        final String[] title2 = new String[]{"Register your pet", "Browse partner for your pet"};
        final int[] icons2 = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title2.length; i++) {
            DialogListInformaion dialogListInformaion2 = new DialogListInformaion();
            dialogListInformaion2.setTittle(title2[i]);
            dialogListInformaion2.setIcons(icons2[i]);
            dialogListForViewPetMets.add(dialogListInformaion2);
        }

        final String[] petclinictitle = new String[]{"View Home Location", "View Current Location"};
        final int[] petclinicicon = {R.drawable.home_location, R.drawable.current_location};
        for (int i = 0; i < petclinictitle.length; i++) {
            DialogListInformaion dialogListpetclinic = new DialogListInformaion();
            dialogListpetclinic.setTittle(petclinictitle[i]);
            dialogListpetclinic.setIcons(petclinicicon[i]);
            dialogListForpetClinic.add(dialogListpetclinic);
        }

        recyclerView = (RecyclerView) findViewById(R.id.indexpagelst);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeListAdapter(indexListInfosArray, dialogListForViewPets, dialogListForViewPetMets, dialogListForpetClinic, homeListCoordinatorLayout);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


}