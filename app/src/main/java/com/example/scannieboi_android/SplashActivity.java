package com.example.scannieboi_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.services.UserService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 3000;
    private String UUID_STRING;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //userService = new UserService(this);
        ifUserExist();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
            intent.putExtra("uuid", UUID_STRING);
            startActivity(intent);
            finish();
        }, SPLASH_TIME_OUT);
    }

    private void ifUserExist(){
        /*JSONObject userExist = JsonRW.readUserFromFile(this);
        try {
            UUID_STRING = userExist.getString("UserID");
        } catch (JSONException | NullPointerException e) {
            createUserID();
        }*/

    }

    private void createUserID(){
        String uuid = UUID.randomUUID().toString();
        UUID_STRING = uuid;
        JsonRW.createUUIDToFile(this, uuid);
        //userService.createUser(uuid, userCallback);
        userService.volleyPost(uuid);
    }

    /*private final UserService.UserCallback userCallback = new UserService.UserCallback() {

        @Override
        public void onUserSuccess(@NonNull String uuid) throws IOException, JSONException {
            Log.i("Callback", "True!");
        }

        @Override
        public void onUserError(@NonNull VolleyError volleyError) {
        }
    };*/
}