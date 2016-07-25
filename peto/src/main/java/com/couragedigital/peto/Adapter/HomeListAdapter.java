package com.couragedigital.peto.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.couragedigital.peto.*;
import com.couragedigital.peto.model.DialogListInformaion;
import com.couragedigital.peto.model.IndexListInfo;

import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private static ArrayList<IndexListInfo> dataSet;
    public static List<DialogListInformaion> dialogListForViewPets = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListForViewPetMets = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListForPetClinic = new ArrayList<DialogListInformaion>();
    public static CoordinatorLayout homeListCoordinatorLayout;


    public HomeListAdapter(ArrayList<IndexListInfo> indexListInfoList, List<DialogListInformaion> dialogListForViewPets,
                           List<DialogListInformaion> dialogListForViewPetMets, List<DialogListInformaion> dialogListForpetClinic, CoordinatorLayout homeListCoordinatorLayout) {
        this.dataSet = indexListInfoList;
        this.dialogListForViewPets = dialogListForViewPets;
        this.dialogListForViewPetMets = dialogListForViewPetMets;
        this.homeListCoordinatorLayout = homeListCoordinatorLayout;
        this.dialogListForPetClinic = dialogListForpetClinic;
    }

    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_homemenu, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(HomeListAdapter.ViewHolder viewHolder, int i) {
        IndexListInfo fp = dataSet.get(i);
        viewHolder.cv.setTag(i);
        viewHolder.tittle.setText(fp.getTittle());
        viewHolder.description.setText(fp.getDescription());
        viewHolder.background.setBackgroundResource(fp.getThumbnail());
        viewHolder.feed = fp;
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        public TextView tittle;
        public TextView description;
        public RelativeLayout background;
        public IndexListInfo feed;
        private AlertDialog alertDialog;
        public com.couragedigital.peto.Adapter.DialogListAdapter adapter;
        public CardView cv;

        public int state = 0;

        public ViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tittle = (TextView) itemView.findViewById(R.id.titletxt);
            description = (TextView) itemView.findViewById(R.id.descriptiontext);
            background = (RelativeLayout) itemView.findViewById(R.id.relativelayouthome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            int position = (int) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.HomePageDialogboxCustom));

            if (position == 0) {
                Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                v.getContext().startActivity(gotolistofpet);
            }
            if (position == 1) {
                Intent gotoShopProduct = new Intent(v.getContext(), Pet_Shop_List.class);
                v.getContext().startActivity(gotoShopProduct);
            }
            if (position == 2) {
                Intent gotoCampaign = new Intent(v.getContext(), Campaign_List_ForAll.class);
                v.getContext().startActivity(gotoCampaign);
            }
            if (position == 3) {
                Intent gotoPetServices = new Intent(v.getContext(), PetServices.class);
                v.getContext().startActivity(gotoPetServices);
            }
            if (position == 4) {
                adapter = new DialogListAdapter(dialogListForPetClinic);
                final Intent gotoPetClinic = new Intent(v.getContext(), PetClinic.class);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            state = 0;
                            gotoPetClinic.putExtra("STATE_OF_CLICK", state);
                            v.getContext().startActivity(gotoPetClinic);

                        } else if (i == 1) {
                            state = 1;
                            gotoPetClinic.putExtra("STATE_OF_CLICK", state);
                            v.getContext().startActivity(gotoPetClinic);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            }
            if (position == 5) {
                Intent gotolistofpet = new Intent(v.getContext(), PetMateList.class);
                v.getContext().startActivity(gotolistofpet);
            }


        }

    }
}

