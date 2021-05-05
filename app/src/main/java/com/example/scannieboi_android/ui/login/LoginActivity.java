package com.example.scannieboi_android.ui.login;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.NavigationActivity;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.LoginDataSource;
import com.example.scannieboi_android.data.Result;
import com.example.scannieboi_android.data.model.User;
import com.example.scannieboi_android.services.UserService;
import com.example.scannieboi_android.ui.home.HomeFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;

public class LoginActivity extends AppCompatActivity {
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    private UserService userService;
    private LoginViewModel loginViewModel;
    private Button testButton;
    private Context mContext;
    private Activity mActivity;
    private TextView mTextViewUsername;
    private View mInflater;
    private LinearLayout mView;
    private NavigationView navigationView;
    private View headerView;
    private TextView navUsername;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);
        mContext = this.getApplicationContext();
        mActivity = this;

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.button_login);
        final Button trialButton = findViewById(R.id.button_signup);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        mInflater = getLayoutInflater().inflate(R.layout.nav_header_main, null);
        mView = mInflater.findViewById(R.id.nav_header_main);
        mTextViewUsername = mView.findViewById(R.id.textview_username_nav);

        userService = new UserService(this);

        testButton = findViewById(R.id.button_login_facebook);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userService.login("mr.admin", "password", new Callback() {
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
                            }
                        }else{
                            System.out.println("login failed");
                        }
                    }
                });
            }
        });

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
                loadingProgressBar.setVisibility(View.GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(), mActivity);
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), mActivity);
            }
        });

        trialButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        //mTextViewUsername.setText(model.getDisplayName());

        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
        Intent returnIntent = new Intent(this, NavigationActivity.class);
        // returnIntent.putExtra("username", model.getDisplayName());

        //Complete and destroy login activity once successful
        //setResult(RESULT_OK, returnIntent);
        finish();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    private final LoginDataSource.UserCallback userCallback = new LoginDataSource.UserCallback() {
        @Override
        public void onUserSuccess(@NonNull Result result) throws IOException, JSONException {
            Log.d("CALLBACK", "onUserSuccess: " + result.toString());
            if (result instanceof Result.Success) {
                User data = ((Result.Success<User>) result).getData();
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