package com.couragedigital.petapp.Holder;

import android.widget.*;

public class FilterListViewHolder {
    private TextView filterText;
    private CheckBox filterCheckBox;

    public FilterListViewHolder() {
    }

    public FilterListViewHolder(TextView filterText, CheckBox filterCheckBox) {
        this.filterText = filterText;
        this.filterCheckBox = filterCheckBox;
    }

    public CheckBox getCheckBox() {
        return filterCheckBox;
    }

    public void setCheckBox(CheckBox filterCheckBox) {
        this.filterCheckBox = filterCheckBox;
    }

    public TextView getTextView() {
        return filterText;
    }

    public void setTextView(TextView filterText) {
        this.filterText = filterText;
    }
}
