package com.example.scannieboi_android.data.model;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class MVCModel implements Parcelable {
    private List<MyProductData> productDataList;
    private String username;
    private String uniqueID;
    private Boolean loggedIn;
    private String cartID;

    public MVCModel() {
        this.productDataList = new ArrayList<>();
        this.username = null;
        this.uniqueID = null;
        this.loggedIn = false;
        this.cartID = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected MVCModel(Parcel in) {
        productDataList = in.createTypedArrayList(MyProductData.CREATOR);
        username = in.readString();
        uniqueID = in.readString();
        //loggedIn = in.readBoolean();
        loggedIn = (Boolean) in.readValue(null);
        cartID = in.readString();
    }

    public static final Creator<MVCModel> CREATOR = new Creator<MVCModel>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public MVCModel createFromParcel(Parcel in) {
            return new MVCModel(in);
        }

        @Override
        public MVCModel[] newArray(int size) {
            return new MVCModel[size];
        }
    };

    public List<MyProductData> getProductDataList() {
        return productDataList;
    }

    public void setProductDataList(List<MyProductData> productDataList) {
        this.productDataList = productDataList;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getCartID() {
        return cartID;
    }

    public void setCartID(String cartID) {
        this.cartID = cartID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(productDataList);
        parcel.writeString(username);
        parcel.writeString(uniqueID);
        //parcel.writeBoolean(loggedIn);
        parcel.writeValue(loggedIn);
        parcel.writeString(cartID);
    }
}