package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.ExpandableText;
import com.couragedigital.peto.Pet_Shop_List_Details;
import com.couragedigital.peto.R;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.ProductListItems;

import java.util.HashMap;
import java.util.List;

public class ShopProductListAdapter extends RecyclerView.Adapter<ShopProductListAdapter.ViewHolder> {


    List<ProductListItems> productLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    String email;

    public ShopProductListAdapter(List<ProductListItems> productLists) {
        this.productLists = productLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.pet_shop_list_subitems, viewGroup, false);
        viewHolder = new ViewHolder(v);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ProductListItems productListItems = productLists.get(i);
        viewHolder.bindPetList(productListItems);
    }

    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public View dividerLine;
        public View cardView;
        public ExpandableText productDescription;

        private ProductListItems productListItems;
        int statusOfFavourite = 0;



        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            productImage = (ImageView) itemView.findViewById(R.id.shopProductImage);
            productName = (TextView) itemView.findViewById(R.id.shopProductName);
            productPrice = (TextView) itemView.findViewById(R.id.shopProductPrice);
            dividerLine = itemView.findViewById(R.id.shopProductListDividerLine);
            productDescription = (ExpandableText) itemView.findViewById(R.id.shopProductListDescription);

            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindPetList(ProductListItems productListItems) {
            this.productListItems = productListItems;
//            Glide.with(productImage.getContext()).load(productListItems.getFirstImagePath()).asBitmap().centerCrop().into(new BitmapImageViewTarget(productImage) {
//                @Override
//                protected void setResource(Bitmap resource) {
//                    RoundedBitmapDrawable circularBitmapDrawable =
//                            RoundedBitmapDrawableFactory.create(productImage.getContext().getResources(), resource);
//                    circularBitmapDrawable.setCircular(true);
//                    productImage.setImageDrawable(circularBitmapDrawable);
//                }
//            });
            Glide.with(productImage.getContext()).load(productListItems.getFirstImagePath()).centerCrop().crossFade().into(productImage);
            productName.setText(productListItems.getProductName());
            productPrice.setText(productListItems.getProductPrice());
            productDescription.setText(productListItems.getProductDescription());
            dividerLine.setBackgroundResource(R.color.list_internal_divider);

//            wlPetListIdArray = userPetListWishList.getPetWishList();
//            PetListIdWishList = petListItems.getListId();
//
//            if(wlPetListIdArray.contains(PetListIdWishList) ){
//                petFavourite.setBackgroundResource(R.drawable.favourite_enable);
//            }else{
//                petFavourite.setBackgroundResource(R.drawable.favourite_disable);
//            }
        }

        @Override
        public void onClick(View v) {

                if (this.productListItems != null) {
                    Intent productFullInformation = new Intent(v.getContext(), Pet_Shop_List_Details.class);
                    productFullInformation.putExtra("LIST_ID", productListItems.getProductId());
                    productFullInformation.putExtra("PRODUCT_FIRST_IMAGE", productListItems.getFirstImagePath());
                    productFullInformation.putExtra("PRODUCT_SECOND_IMAGE", productListItems.getSecondImagePath());
                    productFullInformation.putExtra("PRODUCT_THIRD_IMAGE", productListItems.getThirdImagePath());
                    productFullInformation.putExtra("PRODUCT_NAME", productListItems.getProductName());
                    productFullInformation.putExtra("PRODUCT_PRICE", productListItems.getProductPrice());
                    productFullInformation.putExtra("PRODUCT_DESCRIPTION", productListItems.getProductDescription());

                    v.getContext().startActivity(productFullInformation);
                }
        }
    }
}

