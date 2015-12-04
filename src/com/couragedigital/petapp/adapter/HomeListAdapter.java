package com.couragedigital.petapp.Adapter;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.*;
import com.couragedigital.petapp.PetForm;
import com.couragedigital.petapp.PetList;
import com.couragedigital.petapp.Adapter.DialogListAdapter;
import com.couragedigital.petapp.model.DialogListInformaion;
import com.couragedigital.petapp.model.IndexListInfo;
import com.couragedigital.petapp.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder> {

    private static ArrayList<IndexListInfo> dataSet;
    public static List<DialogListInformaion> dialogListInformaionArrayList1 = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListInformaionArrayList2 = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListInformaionArrayList3 = new ArrayList<DialogListInformaion>();
    public static List<DialogListInformaion> dialogListInformaionArrayList4 = new ArrayList<DialogListInformaion>();

    public HomeListAdapter(ArrayList<IndexListInfo> indexListInfoList, List<DialogListInformaion> dialogListInformaionArrayList,
                           List<DialogListInformaion> dialogListInformaionArrayList2, List<DialogListInformaion> dialogListInformaionArrayList3, List<DialogListInformaion> dialogListInformaionArrayList4) {
        this.dataSet = indexListInfoList;
        this.dialogListInformaionArrayList1 = dialogListInformaionArrayList;
        this.dialogListInformaionArrayList2 = dialogListInformaionArrayList2;
        this.dialogListInformaionArrayList3 = dialogListInformaionArrayList3;
        this.dialogListInformaionArrayList4 = dialogListInformaionArrayList4;
    }

    @Override
    public HomeListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listview_homemenu, viewGroup, false);
        ViewHolder vh = new ViewHolder(view, dialogListInformaionArrayList1, dialogListInformaionArrayList2, dialogListInformaionArrayList3, dialogListInformaionArrayList4);
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
        public TextView textDialog;
        private AlertDialog alertDialog;
        public com.couragedigital.petapp.Adapter.DialogListAdapter adapter;
        public CardView cv;
        public List<DialogListInformaion> dialogListArrayList1 = new ArrayList<DialogListInformaion>();
        public List<DialogListInformaion> dialogListArrayList2 = new ArrayList<DialogListInformaion>();
        public List<DialogListInformaion> dialogListArrayList3 = new ArrayList<DialogListInformaion>();
        public List<DialogListInformaion> dialogListArrayList4 = new ArrayList<DialogListInformaion>();

        public ViewHolder(View itemView, List<DialogListInformaion> listInformaionArrayList1, List<DialogListInformaion> listInformaionArrayList2,
                          List<DialogListInformaion> listInformaionArrayList3, List<DialogListInformaion> listInformaionArrayList4) {
            super(itemView);
            dialogListArrayList1 = listInformaionArrayList1;
            dialogListArrayList2 = listInformaionArrayList2;
            dialogListArrayList3 = listInformaionArrayList3;
            dialogListArrayList4 = listInformaionArrayList4;
            cv = (CardView) itemView.findViewById(R.id.card_view);
            tittle = (TextView) itemView.findViewById(R.id.titletxt);
            description = (TextView) itemView.findViewById(R.id.descriptiontext);
            background = (RelativeLayout) itemView.findViewById(R.id.relativelayouthome);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = (int) v.getTag();
            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.HomePageDialogboxCustom));

            if (position == 0) {
                adapter = new DialogListAdapter(dialogListArrayList1);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetForm.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
            }
            if (position == 1) {
                adapter = new DialogListAdapter(dialogListArrayList2);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetForm.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
            }
            if (position == 2) {
                adapter = new DialogListAdapter(dialogListArrayList3);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetForm.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }

                    }
                });
            /*  //  Toast.makeText(v.getContext(), Integer.toString(position), Toast.LENGTH_SHORT).show();
                CoordinatorLayout snackbarCoordinatorLayout = (CoordinatorLayout) v.findViewById(R.id.snackbarPosition);
                Snackbar snackbar = Snackbar.make(
                        snackbarCoordinatorLayout,
                        "Snackbar",
                        Snackbar.LENGTH_LONG);
                snackbar.show();*/

            }
            if (position == 3) {
                adapter = new DialogListAdapter(dialogListArrayList4);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetForm.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
            }
            alertDialog = builder.create();
            alertDialog.show();
        }

    }
}

