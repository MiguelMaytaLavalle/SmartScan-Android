package com.example.scannieboi_android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.scannieboi_android.data.model.MVCModel;
import com.example.scannieboi_android.data.model.MyProductData;
import com.example.scannieboi_android.services.ScannerService;
import com.example.scannieboi_android.services.UserService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class NavigationActivity extends AppCompatActivity implements View.OnClickListener{
    private AppBarConfiguration mAppBarConfiguration;
    private UserService userService;
    private MVCModel mvcModel;
    FragmentManager fm;
    private ScannerService scannerService;
    private Fragment cartFragment;
    private Fragment prevFragment;
    private Bundle bundle;

    TextView usernameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mvcModel = new MVCModel();
        Intent intent = getIntent();
        mvcModel.setUniqueID(intent.getStringExtra("uuid"));
        Toast.makeText(this, "UserID" + mvcModel.getUniqueID(), Toast.LENGTH_SHORT).show();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Intent navIntent = new Intent();
        //userService = new UserService(this);

        View view = navigationView.getHeaderView(0).findViewById(R.id.nav_header_main);
        usernameText = view.findViewById(R.id.textview_username_nav);
        isUserLoggedIn();

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_shoplist, R.id.nav_cart, R.id.nav_login)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Log.d("MENU", "onCreateOptionsMenu: " + menu.size());
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, ContinuousCaptureActivity.class);
        intent.putExtra("mvcmodel", mvcModel);
        startActivityForResult(intent, 20);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 20){
            mvcModel = data.getParcelableExtra("mvcmodel");
            if(mvcModel.getProductDataList().size() > 0){
                Log.d("WRITE", "onActivityResult: ");
                JsonRW.writeToFile(this, mvcModel.getProductDataList());
            }else{
                Log.d("WRITE", "onActivityResult: " + mvcModel.getProductDataList().size());
            }
        }
    }

    private void isUserLoggedIn() {
        JSONObject userExist = JsonRW.readUserFromFile(this);
        try {
            String username = userExist.getString("username");
            usernameText.setText(username);
            mvcModel.setUsername(username);
            mvcModel.setLoggedIn(true);

        } catch (JSONException | NullPointerException e) {
            usernameText.setText("User");
        }
    }

}