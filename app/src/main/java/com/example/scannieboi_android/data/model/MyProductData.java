package com.example.scannieboi_android.data.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class MyProductData implements Parcelable {
    private String price;
    private Bitmap bitmap;
    private String name;
    private String imageURL;
    private String category;
    private String countryOfOrigin;
    private String manufacture;
    private String co2;
    private String recommendation;
    private String description;

    public MyProductData(){

    }

    public MyProductData(String pName, String pImageURL, String pCategory, String pCoo, String pManufacture, String pCO2, String pRecommendation, String pDescription){
        name = pName;
        imageURL = pImageURL;
        category = pCategory;
        countryOfOrigin = pCoo;
        manufacture = pManufacture;
        co2 = pCO2;
        recommendation = pRecommendation;
        description = pDescription;

    }

    public MyProductData(String name, String imageURL, String category, String countryOfOrigin, String manufacture, String recommendation, String description) {
        this.name = name;
        this.imageURL = imageURL;
        this.category = category;
        this.countryOfOrigin = countryOfOrigin;
        this.manufacture = manufacture;
        this.recommendation = recommendation;
        this.description = description;
    }

    public MyProductData(Bitmap bitmap, String name, String price, String countryOfOrigin, String description) {
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;
        this.countryOfOrigin = countryOfOrigin;
        this.description = description;
    }

    public MyProductData(Bitmap bitmap, String name, String price, String countryOfOrigin) {
        this.bitmap = bitmap;
        this.name = name;
        this.price = price;
        this.countryOfOrigin = countryOfOrigin;
    }

    public MyProductData(String name, String price, String countryOfOrigin, String description) {
        this.name = name;
        this.price = price;
        this.countryOfOrigin = countryOfOrigin;
        this.description = description;
    }

    public MyProductData(String name, String countryOfOrigin, String description){
        this.name = name;
        this.countryOfOrigin = countryOfOrigin;
        this.description = description;
    }

    protected MyProductData(Parcel in) {
        price = in.readString();
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        imageURL = in.readString();
        category = in.readString();
        countryOfOrigin = in.readString();
        manufacture = in.readString();
        co2 = in.readString();
        recommendation = in.readString();
        description = in.readString();
    }

    public static final Creator<MyProductData> CREATOR = new Creator<MyProductData>() {
        @Override
        public MyProductData createFromParcel(Parcel in) {
            return new MyProductData(in);
        }

        @Override
        public MyProductData[] newArray(int size) {
            return new MyProductData[size];
        }
    };

    public MyProductData(String pName, String pImageURL, String pCategory, String pCountry, String pManufecture, String pDescription) {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getManufacture() {
        return manufacture;
    }

    public void setManufacture(String manufacture) {
        this.manufacture = manufacture;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCo2() {
        return co2;
    }

    public void setCo2(String co2) {
        this.co2 = co2;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(price);
        parcel.writeValue(bitmap);
        parcel.writeString(name);
        parcel.writeString(imageURL);
        parcel.writeString(category);
        parcel.writeString(countryOfOrigin);
        parcel.writeString(manufacture);
        parcel.writeString(co2);
        parcel.writeString(recommendation);
        parcel.writeString(description);

    }

}
