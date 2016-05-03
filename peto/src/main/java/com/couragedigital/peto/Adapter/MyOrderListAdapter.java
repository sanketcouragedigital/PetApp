package com.couragedigital.peto.Adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.couragedigital.peto.*;
import com.couragedigital.peto.SessionManager.SessionManager;
import com.couragedigital.peto.app.AppController;
import com.couragedigital.peto.model.OrderListItems;
import java.util.HashMap;
import java.util.List;

public class MyOrderListAdapter extends RecyclerView.Adapter<MyOrderListAdapter.ViewHolder> {

    List<OrderListItems> orderLists;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    View v;
    ViewHolder viewHolder;

    String email;

    public MyOrderListAdapter(List<OrderListItems> orderLists) {
        this.orderLists = orderLists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.myorders_items, viewGroup, false);
        viewHolder = new ViewHolder(v);

        SessionManager sessionManager = new SessionManager(v.getContext());
        HashMap<String, String> user = sessionManager.getUserDetails();
        email = user.get(SessionManager.KEY_EMAIL);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        OrderListItems OrderListItems = orderLists.get(i);
        viewHolder.bindPetList(OrderListItems);
    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView orderProductImage;
        public TextView orderProductName;
        public TextView orderProductPrice;
        public TextView orderDate;
        public View dividerLine;
        public View cardView;
        public ExpandableText orderProductDescription;
        private OrderListItems OrderListItems;

        public ViewHolder(View itemView) {
            super(itemView);
            if (imageLoader == null) {
                imageLoader = AppController.getInstance().getImageLoader();
            }
            orderProductImage = (ImageView) itemView.findViewById(R.id.orderProductImage);
            orderProductName = (TextView) itemView.findViewById(R.id.orderProductName);
            orderProductPrice = (TextView) itemView.findViewById(R.id.orderProductPrice);
            dividerLine = itemView.findViewById(R.id.orderProductListDividerLine);
            orderDate = (TextView) itemView.findViewById(R.id.orderDate);
            orderProductDescription = (ExpandableText) itemView.findViewById(R.id.orderProductListDescription);
            cardView = itemView;
            cardView.setOnClickListener(this);
        }

        public void bindPetList(OrderListItems OrderListItems) {
            this.OrderListItems = OrderListItems;
            Glide.with(orderProductImage.getContext()).load(OrderListItems.getFirstImagePath()).centerCrop().crossFade().into(orderProductImage);
            orderProductName.setText(OrderListItems.getOrderProductName());
            orderProductPrice.setText(OrderListItems.getOrderProductPrice());
            orderDate.setText(OrderListItems.getOrderProductPostDate());
            orderProductDescription.setText(OrderListItems.getOrderProductDescription());
            dividerLine.setBackgroundResource(R.color.list_internal_divider);
        }

        @Override
        public void onClick(View v) {

            if (this.OrderListItems != null) {
                Intent orderProductFullInformation = new Intent(v.getContext(), MyOrder_List_Details.class);
                orderProductFullInformation.putExtra("ORDER_ID", OrderListItems.getOrderListId());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_FIRST_IMAGE", OrderListItems.getFirstImagePath());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_SECOND_IMAGE", OrderListItems.getSecondImagePath());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_THIRD_IMAGE", OrderListItems.getThirdImagePath());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_NAME", OrderListItems.getOrderProductName());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_PRICE", OrderListItems.getOrderProductPrice());
                orderProductFullInformation.putExtra("ORDER_PRODUCT_DESCRIPTION", OrderListItems.getOrderProductDescription());
                orderProductFullInformation.putExtra("ORDER_POST_DATE", OrderListItems.getOrderProductPostDate());
                orderProductFullInformation.putExtra("ORDER_QUANTITY", OrderListItems.getOrderProductQuantity());
                orderProductFullInformation.putExtra("ORDER_SHIPPING_CHARGES", OrderListItems.getOrderProductShipping_charges());
                orderProductFullInformation.putExtra("ORDER_TOTAL_PRICE", OrderListItems.getOrderProductTotal_price());
                orderProductFullInformation.putExtra("ORDER_NAME", OrderListItems.getOrderProductCustomer_name());
                orderProductFullInformation.putExtra("ORDER_CONTACT_NO", OrderListItems.getOrderProductCustomer_contact());
                orderProductFullInformation.putExtra("ORDER_EMAIL", OrderListItems.getOrderProductCustomer_email());
                orderProductFullInformation.putExtra("ORDER_BUILDING_NAME", OrderListItems.getOrderProductBuidling_name());
                orderProductFullInformation.putExtra("ORDER_AREA", OrderListItems.getOrderProductArea());
                orderProductFullInformation.putExtra("ORDER_CITY", OrderListItems.getOrderProductCity());
                orderProductFullInformation.putExtra("PRODUCT_LIST_ID", OrderListItems.getOrderProductId());
                orderProductFullInformation.putExtra("PIN_CODE", OrderListItems.getOrderPinCode());

                v.getContext().startActivity(orderProductFullInformation);
            }
        }
    }
}

