package com.example.scannieboi_android.ui.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.FontRequest;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.emoji.text.EmojiCompat;
import androidx.emoji.text.FontRequestEmojiCompatConfig;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.JsonRW;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.MyProductData;
import com.example.scannieboi_android.data.model.User;
import com.example.scannieboi_android.services.ScannerService;
import com.example.scannieboi_android.services.UserService;
import com.example.scannieboi_android.ui.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private UserService userService;
    private HomeViewModel homeViewModel;
    private Button loginButton;
    private Button testButton;
    private View root;
    private Context mContext;
    private NavigationView navigationView;

    private NavController navController;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = root.getContext();
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        //isUserLoggedIn();

        navigationView = root.findViewById(R.id.nav_view);
        loginButton = root.findViewById(R.id.login_button);
        loginButton.setOnClickListener(this);
        final TextView textView = root.findViewById(R.id.text_view_home_header);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, LoginActivity.class);
        // mContext.startActivity(intent);
        startActivityForResult(intent, 20);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK && requestCode == 20){
            isUserLoggedIn();
        }
    }

    private void isUserLoggedIn() {
        JSONObject userExist = JsonRW.readUserFromFile(this.getContext());
        try {
            String username = userExist.getString("username");

        } catch (JSONException | NullPointerException e) {
        }
    }
}