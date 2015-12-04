package com.couragedigital.petapp.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.model.DialogListInformaion;

import java.util.ArrayList;
import java.util.List;


public class DialogListAdapter extends BaseAdapter {
    List<DialogListInformaion> myList = new ArrayList<DialogListInformaion>();
    LayoutInflater inflater;
    Context context;

    public DialogListAdapter(List<DialogListInformaion> arraylist) {
        this.myList = arraylist;
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyViewHolder mViewHolder;

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.dialoglistmenu, viewGroup, false);

            mViewHolder = new MyViewHolder(view);
            view.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) view.getTag();

        }
        DialogListInformaion currentListData = (DialogListInformaion) getItem(i);
        if (i == myList.size() - 1) {
            mViewHolder.tvTitle.setText(currentListData.getTittle());
            mViewHolder.ivIcon.setImageResource(currentListData.getIcons());
        } else {
            mViewHolder.tvTitle.setText(currentListData.getTittle());
            mViewHolder.ivIcon.setImageResource(currentListData.getIcons());
            mViewHolder.divider.setBackgroundResource(R.color.drawer_header_text);
        }
        return view;
    }

    private class MyViewHolder {
        TextView tvTitle;
        ImageView ivIcon;
        View divider;

        public MyViewHolder(View item) {
            tvTitle = (TextView) item.findViewById(R.id.listItemText);
            ivIcon = (ImageView) item.findViewById(R.id.iconimage);
            divider = (View) item.findViewById(R.id.viewDialogDivider);
        }

    }
}
