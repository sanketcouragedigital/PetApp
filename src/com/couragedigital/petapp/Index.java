package com.couragedigital.petapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Index extends BaseActivity {

    String[] tittleName = new String[] {"View/List Pets","Pet Mate","Pet Doctors","Pet Accessories" };
    int[] imageList = new int[] { R.drawable.pet_view,R.drawable.pet_mate,R.drawable.pet_doctor,R.drawable.pet_accessories };
    String[] description = new String[]{"sample1","sample2","sample3","sample4" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.index);

        List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
        for(int i=0;i<4;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("titletxt", tittleName[i]);
            hm.put("descriptiotext","Description : " + description[i]);
            hm.put("imagedisplay", Integer.toString(imageList[i]) );
            if(aList == null) {
                aList = new ArrayList<HashMap<String, String>>();
            }
            aList.add(hm);
        }
        // Keys used in Hashmap
        String[] from = { "imagedisplay","titletxt","descriptiotext" };

        // Ids of views in listview_layout
        int[] to = { R.id.imagedisplay,R.id.titletxt,R.id.descriptiotext};

        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.listview_homemenu, from, to);

        // Getting a reference to listview of main.xml layout file
        ListView listView = ( ListView )findViewById(R.id.indexpagelist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if(i==0){
                  Intent vieworlistOfPetsIntent = new Intent(Index.this, ViewOrListOfPet.class);
                  startActivity(vieworlistOfPetsIntent);
              }
                else if(i==1){
                  Toast.makeText(getApplicationContext(), ((TextView) view.findViewById(R.id.titletxt))
                                  .getText(), Toast.LENGTH_SHORT).show();
              }
                else if(i==2){
                  Toast.makeText(getApplicationContext(), ((TextView) view.findViewById(R.id.titletxt))
                          .getText(), Toast.LENGTH_SHORT).show();
              }
                else if(i==3){
                  Toast.makeText(getApplicationContext(), ((TextView) view.findViewById(R.id.titletxt))
                          .getText(), Toast.LENGTH_SHORT).show();
              }
            }
        });

    }
}