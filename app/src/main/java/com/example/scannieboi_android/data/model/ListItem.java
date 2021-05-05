package com.example.scannieboi_android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ListItem implements Parcelable {
    private String productName;
    private String listID;
    private int quantity;

    public ListItem(String productName, String listID, int quantity) {
        this.productName = productName;
        this.listID = listID;
        this.quantity = quantity;
    }

    public ListItem(String productName){
        this.productName = productName;
    }

    protected ListItem(Parcel in) {
        productName = in.readString();
        listID = in.readString();
        quantity = in.readInt();
    }

    public static final Creator<ListItem> CREATOR = new Creator<ListItem>() {
        @Override
        public ListItem createFromParcel(Parcel in) {
            return new ListItem(in);
        }

        @Override
        public ListItem[] newArray(int size) {
            return new ListItem[size];
        }
    };

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(productName);
        parcel.writeString(listID);
        parcel.writeInt(quantity);
    }
}
