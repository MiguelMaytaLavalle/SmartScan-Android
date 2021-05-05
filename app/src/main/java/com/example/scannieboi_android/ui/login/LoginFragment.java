package com.example.scannieboi_android.ui.login;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.scannieboi_android.R;

public class LoginFragment extends Fragment {
    private static String LOGIN_SUCCESSFUL = "LOGIN_SUCCESSFUL";
    private View root;
    private Context mContext;


    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        mContext = root.getContext();

        return root;
    }

}
