package com.couragedigital.petapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.couragedigital.petapp.R;
import com.couragedigital.petapp.app.AppController;
import com.couragedigital.petapp.model.PetList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<PetList> petListItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<PetList> petListItems) {
        this.activity = activity;
        this.petListItems = petListItems;
    }

    @Override
    public int getCount() {
        return petListItems.size();
    }

    @Override
    public Object getItem(int location) {
        return petListItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.petlistsubitems, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView image = (NetworkImageView) convertView
                .findViewById(R.id.petImage);
        TextView petBreed = (TextView) convertView.findViewById(R.id.petName);

        // getting pet list data for the row
        PetList m = petListItems.get(position);

        // thumbnail image
        image.setImageUrl(m.getImage_path(), imageLoader);

        // pet breed
        petBreed.setText(m.getPetBreedOrigin());

        return convertView;
    }
}
