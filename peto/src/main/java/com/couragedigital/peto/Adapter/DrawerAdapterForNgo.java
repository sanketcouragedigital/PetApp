package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.couragedigital.peto.*;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.Singleton.DrawerListInstance;
import com.couragedigital.peto.model.DrawerItems;

import java.util.ArrayList;

public class DrawerAdapterForNgo extends RecyclerView.Adapter<DrawerAdapterForNgo.ViewHolder> {

    private static ArrayList<DrawerItems> itemsArrayList;
    public View v;
    public ViewHolder viewHolder;
    public DrawerLayout drawer;
    int positionOfItem = 0;

    private static ArrayList<DrawerItems> itemselectedArrayList;
    public static DrawerListInstance drawerListInstance = new DrawerListInstance();

    int imageInstance;

    public DrawerAdapterForNgo(ArrayList<DrawerItems> itemArrayListForNgo, ArrayList<DrawerItems> itemSelectedArrayListForNgo, DrawerLayout drawer) {
        this.drawer = drawer;
        this.itemsArrayList = itemArrayListForNgo;
        this.itemselectedArrayList = itemSelectedArrayListForNgo;
    }

    @Override
    public DrawerAdapterForNgo.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 0) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritems, viewGroup, false);
        } else if (i == 1) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.draweritemtext, viewGroup, false);
        }
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(DrawerAdapterForNgo.ViewHolder viewHolder, int i) {
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
        if (position <= 5) {
            viewType = 0;
        } else if (position > 5) {
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
        public String oldEmail;

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
            if (i <= 5) {
                vimageView.setImageResource(itemsList.getIcon());
                vtextView.setText(itemsList.getTittle());
                //vimageView.setEnabled(true);
                if (i == 5) {
                    drawerDivider.setVisibility(View.VISIBLE);
                }
                if (positionOfItem == 0 && itemsList.getIcon() == R.drawable.home) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
                else if (positionOfItem == 1 && itemsList.getIcon() == R.drawable.profile) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
                else if (positionOfItem == 2 && itemsList.getIcon() == R.drawable.mylisting) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
                else if (positionOfItem == 3 && itemsList.getIcon() == R.drawable.favourite) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
                else if (positionOfItem == 4 && itemsList.getIcon() == R.drawable.ordertruck) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
                else if (positionOfItem == 5 && itemsList.getIcon() == R.drawable.campaign) {
                    vtextView.setText(itemsSelectedList.getTittle());
                    vimageView.setImageResource(itemsSelectedList.getIcon());
                }
            } else if (i > 5) {
                vtext.setText(itemsList.getTittle());
            }
        }

        @Override
        public void onClick(View view) {
            positionOfItem = this.getAdapterPosition();
            if (this.getAdapterPosition() == 0) {
                drawer.closeDrawers();
                Intent gotoformupload = new Intent(view.getContext(), Index.class);
                view.getContext().startActivity(gotoformupload);
            }
            else if (this.getAdapterPosition() == 1) {
                drawer.closeDrawers();
                Intent gotoPasswordForEdit = new Intent(view.getContext(), CheckPasswordForEditProfile.class);
                view.getContext().startActivity(gotoPasswordForEdit);
            }
            else if (this.getAdapterPosition() == 2) {
                drawer.closeDrawers();
                Intent gotomylisting = new Intent(v.getContext(), MyListing.class);
                v.getContext().startActivity(gotomylisting);
            }
            else if (this.getAdapterPosition() == 3) {
                drawer.closeDrawers();
                Intent gotoWishList = new Intent(v.getContext(), WishList.class);
                v.getContext().startActivity(gotoWishList);
            }
            else if (this.getAdapterPosition() == 4) {
                drawer.closeDrawers();
                Intent gotoMyOrders = new Intent(v.getContext(), MyOrders.class);
                v.getContext().startActivity(gotoMyOrders);
            }
            else if (this.getAdapterPosition() == 5) {
                drawer.closeDrawers();
                Intent gotoCampaignForm = new Intent(v.getContext(), Campaign_List.class);
                v.getContext().startActivity(gotoCampaignForm);
            }
            else if (this.getAdapterPosition() == 6) {
                drawer.closeDrawers();
                Intent gotofeedback = new Intent(v.getContext(), Feedback.class);
                v.getContext().startActivity(gotofeedback);
            }
            else if (this.getAdapterPosition() == 7) {
                drawer.closeDrawers();
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String sharingText = "Hey, check out this new app, Peto I just came across: 'https://play.google.com/store/apps/details?id=com.couragedigital.peto' ";
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, sharingText);
                v.getContext().startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
            else if (this.getAdapterPosition() == 8) {
                drawer.closeDrawers();
                Intent gotoLegal = new Intent(v.getContext(), Legal.class);
                v.getContext().startActivity(gotoLegal);
            }
            else if (this.getAdapterPosition() == 9) {
                drawer.closeDrawers();
                sessionManager = new SessionManager(v.getContext());
                sessionManager.logoutUser();
            }
            drawerListInstance.setDrawerItemListImagePositionInstances(positionOfItem);
            notifyDataSetChanged();
        }
    }
}

