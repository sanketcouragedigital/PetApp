package com.couragedigital.petapp.Adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.couragedigital.petapp.*;
import com.couragedigital.petapp.SessionManager.SessionManager;
import com.couragedigital.petapp.Singleton.DrawerListInstance;
import com.couragedigital.petapp.model.DialogListInformaion;
import com.couragedigital.petapp.model.DrawerItems;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static ArrayList<DrawerItems> itemsArrayList;
    public View v;
    public ViewHolder viewHolder;
    public DrawerLayout drawer;
    int positionOfItem = 0;

    private static ArrayList<DrawerItems> itemselectedArrayList;
    public static DrawerListInstance drawerListInstance = new DrawerListInstance();

    int imageInstance;

    public DrawerAdapter(ArrayList<DrawerItems> itemsArrayList, ArrayList<DrawerItems> itemselectedArrayList, DrawerLayout drawer) {
        this.drawer = drawer;
        this.itemsArrayList = itemsArrayList;
        this.itemselectedArrayList = itemselectedArrayList;
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritems, viewGroup, false);
        } else if (i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritemtext, viewGroup, false);
        }
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder viewHolder, int i) {
        DrawerItems itemsList = itemsArrayList.get(i);
        DrawerItems itemselectedList = itemselectedArrayList.get(i);
        viewHolder.bindDrawerItemArrayList(i, itemsList, itemselectedList);
    }

    @Override
    public int getItemCount() {
        return itemsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if (position < 3) {
            viewType = 0;
        } else if (position > 3) {
            viewType = 1;
        }
        return viewType;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView vtextView;
        public ImageView vimageView;
        public SessionManager sessionManager;
        public View drawerDivider;
        public TextView vtext;

        private DrawerItems itemsList;
        private DrawerItems itemsSelectedList;

        public ArrayList<DrawerItems> drawerItemsArrayList = new ArrayList<DrawerItems>();

        public ViewHolder(View itemView) {
            super(itemView);
            vtextView = (TextView) itemView.findViewById(R.id.drawerItemText);
            vimageView = (ImageView) itemView.findViewById(R.id.drawerItemIcon);
            drawerDivider = (View) itemView.findViewById(R.id.drawerDivider);
            vtext = (TextView) itemView.findViewById(R.id.itemText);
            itemView.setOnClickListener(this);
        }

        public void bindDrawerItemArrayList(int i, DrawerItems draweritemsList, DrawerItems draweritemselectedList) {

            this.itemsList = draweritemsList;
            this.itemsSelectedList = draweritemselectedList;

            if (drawerListInstance.getDrawerItemListImagePositionInstances() != null) {
                positionOfItem = drawerListInstance.getDrawerItemListImagePositionInstances();
            }
            if (i <= 3) {
                vimageView.setImageResource(itemsList.getIcon());
                vtextView.setText(itemsList.getTittle());
                //vimageView.setEnabled(true);
                if (i == 3) {
                    drawerDivider.setVisibility(View.VISIBLE);
                }
                if (positionOfItem == 0 && itemsList.getIcon() == R.drawable.home) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());

                } else if (positionOfItem == 1 && itemsList.getIcon() == R.drawable.profile) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());

                } else if (positionOfItem == 2 && itemsList.getIcon() == R.drawable.mylisting) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());

                } else if (positionOfItem == 3 && itemsList.getIcon() == R.drawable.setting) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                    drawerDivider.setVisibility(View.VISIBLE);
                }
            } else if (i > 3) {
                vtext.setText(itemsList.getTittle());
            }
        }

        @Override
        public void onClick(View view) {
            positionOfItem = this.getAdapterPosition();
            if (this.getAdapterPosition() == 0) {
                Intent gotoformupload = new Intent(view.getContext(), Index.class);
                view.getContext().startActivity(gotoformupload);
            } else if (this.getAdapterPosition() == 1) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (this.getAdapterPosition() == 2) {
                drawer.closeDrawers();
                AlertDialog alertDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(v.getContext(), R.style.HomePageDialogboxCustom));
                List<DialogListInformaion> dialogListForViewPetsAndPetMates = new ArrayList<DialogListInformaion>();
                final String[] title = new String[]{"List of Pets", "List of Pet Mate"};
                final int[] icons = {R.drawable.view, R.drawable.view};
                for (int i = 0; i < title.length; i++) {
                    DialogListInformaion dialogListInformaion1 = new DialogListInformaion();
                    dialogListInformaion1.setTittle(title[i]);
                    dialogListInformaion1.setIcons(icons[i]);
                    dialogListForViewPetsAndPetMates.add(dialogListInformaion1);
                }
                DialogListAdapter adapter = new DialogListAdapter(dialogListForViewPetsAndPetMates);
                builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            Intent gotoformupload = new Intent(v.getContext(), PetList.class);
                            v.getContext().startActivity(gotoformupload);
                        } else if (i == 1) {
                            Intent gotolistofpet = new Intent(v.getContext(), PetMateList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else if (this.getAdapterPosition() == 3) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (this.getAdapterPosition() == 4) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (this.getAdapterPosition() == 5) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (this.getAdapterPosition() == 6) {
                sessionManager = new SessionManager(v.getContext());
                sessionManager.logoutUser();
            }
            drawerListInstance.setDrawerItemListImagePositionInstances(positionOfItem);
            notifyDataSetChanged();
        }
    }
}
