package com.couragedigital.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.adapter.HomeListAdapter;
import com.couragedigital.petapp.model.DialogListInformaion;
import com.couragedigital.petapp.model.IndexListInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index extends BaseActivity {

    public ArrayList<IndexListInfo> indexListInfosArray;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    HomeListAdapter mAdapter;

    public List<DialogListInformaion> dialogListArrayList1 = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListArrayList2 = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListArrayList3 = new ArrayList<DialogListInformaion>();
    public List<DialogListInformaion> dialogListArrayList4 = new ArrayList<DialogListInformaion>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);
        homeListMenu();

        boolean checkLogin = sessionManager.isLoggedIn();
        if (!checkLogin)
        {
            // start your home screen
            Intent intent = new Intent (Index.this, SignIn.class);
            this.startActivity(intent);
            this.finish();
        }
    }

    private void homeListMenu() {

        //Home menus Tittle Names
        final String[] titlename = new String[]{"View/List Pets", "Mating", "Doctors", "Accessories"};
        final String[] description = new String[]{"sample1", "sample2", "sample3", "sample4", "sample5"};
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
        final String[] title = new String[]{"Upload Pet Detail", "List of Pets", "AddMore"};
        final int[] icons = {R.drawable.addpet, R.drawable.view, R.drawable.ic_launcher};
        for (int i = 0; i < title.length; i++) {
            DialogListInformaion dialogListInformaion1 = new DialogListInformaion();
            dialogListInformaion1.setTittle(title[i]);
            dialogListInformaion1.setIcons(icons[i]);
            dialogListArrayList1.add(dialogListInformaion1);
        }

        //Mating Dailogbox Menu tittles Names
        final String[] title2 = new String[]{"Sample 1", "Sample 2"};
        final int[] icons2 = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title2.length; i++) {
            DialogListInformaion dialogListInformaion2 = new DialogListInformaion();
            dialogListInformaion2.setTittle(title2[i]);
            dialogListInformaion2.setIcons(icons2[i]);
            dialogListArrayList2.add(dialogListInformaion2);
        }

        //Doctors Dailogbox Menu tittles Names
        final String[] title3 = new String[]{"Sample 3", "Sample 4"};
        final int[] icons3 = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title3.length; i++) {
            DialogListInformaion dialogListInformaion3 = new DialogListInformaion();
            dialogListInformaion3.setTittle(title3[i]);
            dialogListInformaion3.setIcons(icons3[i]);
            dialogListArrayList3.add(dialogListInformaion3);
        }

        //Accessories Dailogbox Menu tittles Names
        final String[] title4 = new String[]{"Sample 5", "Sample 6"};
        final int[] icons4 = {R.drawable.addpet, R.drawable.view};
        for (int i = 0; i < title4.length; i++) {
            DialogListInformaion dialogListInformaion4 = new DialogListInformaion();
            dialogListInformaion4.setTittle(title4[i]);
            dialogListInformaion4.setIcons(icons4[i]);
            dialogListArrayList4.add(dialogListInformaion4);
        }

        recyclerView = (RecyclerView) findViewById(R.id.indexpagelst);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new HomeListAdapter(indexListInfosArray, dialogListArrayList1, dialogListArrayList2, dialogListArrayList3, dialogListArrayList4);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}