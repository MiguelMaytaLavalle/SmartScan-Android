package com.example.scannieboi_android.data;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.volley.ParseError;
import com.android.volley.VolleyError;
import com.example.scannieboi_android.data.model.User;
import com.example.scannieboi_android.services.UserService;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private UserService userService;
    private User user;

    public void login(String username, String password, Activity mActivity, UserCallback callback){
        userService = new UserService(mActivity);
        System.out.println("Username: " + username + ", password: " + password);
        try {
            // TODO: handle loggedInUser authentication
            userService.login(username, password, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(@NotNull Call call, @NotNull okhttp3.Response response) throws IOException {
                    if(response.code() == 200){
                        String authorizationHeader = response.header("Authorization");
                        if(authorizationHeader != null){
                            System.out.println(authorizationHeader);
                            Log.d("RESPONSE", "onResponse: " + authorizationHeader);
                            user = new User(username, password, authorizationHeader);
                            Result result =  new Result.Success<>(user);
                            try {
                                callback.onUserSuccess(result);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }else{
                        System.out.println("login failed");
                        VolleyError error = new ParseError();
                        callback.onUserError(error);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            VolleyError error = new ParseError();
            callback.onUserError(error);
        }
    }

    public interface UserCallback {
        @MainThread
        void onUserSuccess(@NonNull final Result result) throws IOException, JSONException;

        @MainThread
        void onUserError(@NonNull VolleyError volleyError);
    }

    public void logout() {
        // TODO: revoke authentication
    }
}