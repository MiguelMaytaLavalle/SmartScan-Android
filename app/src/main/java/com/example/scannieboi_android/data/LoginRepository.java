package com.example.scannieboi_android.data;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.JsonRW;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.User;

import org.json.JSONException;

import java.io.IOException;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository extends ViewModel {

    private static volatile LoginRepository instance;
    private LoginDataSource dataSource;
    private Result<User> mResult;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private User user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(User user) {
        this.user = user;
        //JsonRW.writeToFileUserAuth(this, user);
        Log.d("Login", "setLoggedInUser: " + user.getUsername());
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String username, String password, Activity mActivity, LoginDataSource.UserCallback userCallback){
        // handle login
        dataSource.login(username, password, mActivity, userCallback);
        //Result<User> result = dataSource.login(username, password, mActivity, userCallback);
        //mResult = dataSource.login(username, password, mActivity, userCallback);
        //return mResult;
    }
}