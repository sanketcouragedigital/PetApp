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
import com.couragedigital.petapp.model.DialogListInformaion;
import com.couragedigital.petapp.model.DrawerItems;

import java.util.ArrayList;
import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static ArrayList<DrawerItems> dataset;
    public View v;
    public ViewHolder viewHolder;
    public DrawerLayout drawer;

    public DrawerAdapter(ArrayList<DrawerItems> dataset, DrawerLayout drawer) {
        this.dataset = dataset;
        this.drawer = drawer;
    }

    @Override
    public DrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if(i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritems, viewGroup, false);
        }
        else if(i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritemtext, viewGroup, false);
        }
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerAdapter.ViewHolder viewHolder, int i) {
        DrawerItems itemsData = dataset.get(i);
        if(i <= 3) {
            if (i == 3) {
                viewHolder.vtextView.setText(itemsData.getTittle());
                viewHolder.vimageView.setImageResource(itemsData.getIcon());
            } else {
                viewHolder.vtextView.setText(itemsData.getTittle());
                viewHolder.vimageView.setImageResource(itemsData.getIcon());
                viewHolder.drawerDivider.setVisibility(View.INVISIBLE);
            }
        }
        else if(i > 3) {
            viewHolder.vtext.setText(itemsData.getTittle());
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0;
        if(position < 3) {
            viewType = 0;
        }
        else if(position > 3) {
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

        public ViewHolder(View itemView) {
            super(itemView);
            vtextView = (TextView) itemView.findViewById(R.id.drawerItemText);
            vimageView = (ImageView) itemView.findViewById(R.id.drawerItemIcon);
            drawerDivider = (View) itemView.findViewById(R.id.drawerDivider);
            vtext = (TextView) itemView.findViewById(R.id.itemText);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if (position == 0) {
                Intent gotoformupload = new Intent(view.getContext(), Index.class);
                view.getContext().startActivity(gotoformupload);
            } else if (position == 1) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (position == 2) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
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
                            Intent gotolistofpet = new Intent(v.getContext(), PetMetList.class);
                            v.getContext().startActivity(gotolistofpet);
                        }
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
            } else if (position == 3) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (position == 4) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (position == 5) {
                //Toast.makeText(view.getContext(), "You selected Postion " + position, Toast.LENGTH_LONG).show();
            } else if (position == 6) {
                sessionManager = new SessionManager(v.getContext());
                sessionManager.logoutUser();
            }
        }
    }
}
