package com.example.scannieboi_android.ui.login;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.JsonRW;
import com.example.scannieboi_android.data.LoginDataSource;
import com.example.scannieboi_android.data.LoginRepository;
import com.example.scannieboi_android.data.Result;
import com.example.scannieboi_android.data.model.LoggedInUser;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.User;

import org.json.JSONException;

import java.io.IOException;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private Activity mActivity;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password, Activity activity) {
        this.mActivity = activity;
        loginRepository.login(username, password, mActivity, userCallback);
    }

    public void setLogin(Result result){
        if (result instanceof Result.Success) {
            User data = ((Result.Success<User>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getUsername())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    private final LoginDataSource.UserCallback userCallback = new LoginDataSource.UserCallback() {
        @Override
        public void onUserSuccess(@NonNull Result result) throws IOException, JSONException {
            Log.d("CALLBACK", "onUserSuccess: " + result.toString());
            if (result instanceof Result.Success) {
                User data = ((Result.Success<User>) result).getData();
                JsonRW.writeToFileUserAuth(mActivity.getApplicationContext(), data);
                loginResult.postValue(new LoginResult(new LoggedInUserView(data.getUsername())));
            }
        }

        @Override
        public void onUserError(@NonNull VolleyError volleyError) {
            System.out.println("Error");
            loginResult.postValue(new LoginResult(R.string.login_failed));
        }
    };

}