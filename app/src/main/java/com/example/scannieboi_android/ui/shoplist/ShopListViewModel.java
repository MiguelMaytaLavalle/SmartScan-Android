package com.example.scannieboi_android.ui.shoplist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ShopListViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ShopListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Inköpslistor");
    }

    public LiveData<String> getText() {
        return mText;
    }
}