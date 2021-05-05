package com.example.scannieboi_android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ShopList implements Parcelable {

    private String shopListName;
    private List<ListItem> listItems;
    private String listID;
    private String userID;
    private String date;

    public ShopList(String shopListName, List<ListItem> listItems, String listID, String userID, String date) {
        this.shopListName = shopListName;
        this.listItems = listItems;
        this.listID = listID;
        this.userID = userID;
        this.date = date;
    }

    public ShopList() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected ShopList(Parcel in) {
        shopListName = in.readString();
        listItems = new ArrayList<>();
        in.readTypedList(listItems, ListItem.CREATOR);
        listID = in.readString();
        userID = in.readString();
        date = in.readString();
    }

    public static final Creator<ShopList> CREATOR = new Creator<ShopList>() {
        @Override
        public ShopList createFromParcel(Parcel in) {
            return new ShopList(in);
        }

        @Override
        public ShopList[] newArray(int size) {
            return new ShopList[size];
        }
    };

    public String getShopListName() {
        return shopListName;
    }

    public void setShopListName(String shopListName) {
        this.shopListName = shopListName;
    }

    public List<ListItem> getListItems() {
        return listItems;
    }

    public void setListItems(List<ListItem> listItems) {
        this.listItems = listItems;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(shopListName);
        parcel.writeTypedList(listItems);
        parcel.writeString(listID);
        parcel.writeString(userID);
        parcel.writeString(date);
    }
}
