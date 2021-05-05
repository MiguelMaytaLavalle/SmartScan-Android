package com.example.scannieboi_android.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scannieboi_android.Constant;
import com.example.scannieboi_android.data.model.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class UserService {
    private static final String CURRENT_USER_TAG = "CURRENT_USER";
    private RequestQueue requestQueue;
    private OkHttpClient client = new OkHttpClient();
    private final String TAG = "ROOT";

    /**
     * Constructor for UserService, creates a Request queue for the application context.
     *
     * @param activity
     */
    public UserService(@NonNull final Activity activity) {
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
    }

    /**
     * An interface for the callback methods.
     */
    public interface UserCallback {
        @MainThread
        void onUserSuccess(@NonNull final User user) throws IOException, JSONException;

        @MainThread
        void onUserError(@NonNull VolleyError volleyError);

    }

    public void registerUser(@NonNull final String username, @NonNull final String password){
        Log.d("REGISTER", "registerUser: " + username);
        JSONObject postObject = new JSONObject();
        try{
            postObject.put("username", username);
            postObject.put("password", password);
        } catch (JSONException e){
            e.printStackTrace();
        }

    }

    public void volleyPostUser(){
        //RequestQueue requestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        JSONObject postData = new JSONObject();
        try {
            postData.put("username", "mr.admin");
            postData.put("password", "password");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_HEROKU+"/login"
                , postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println("Response" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error!");
                error.printStackTrace();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void loginUser(@NonNull final String username, @NonNull final String password, @NonNull final UserService.UserCallback callback){
        Log.d("REGISTER", "registerUser: " + username);

        JSONObject postObject = new JSONObject();
        try{
            postObject.put("username", "mr.admin");
            postObject.put("password", "password");
        } catch (JSONException e){
            e.printStackTrace();
        }

        System.out.println("JSON OBJECT: " + postObject.toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_HEROKU+"/login",response -> {
            try {
                JSONObject root = new JSONObject(response);
                Log.d("AUTH", "Success loginUser: " + root.toString());

            } catch (JSONException ex) {
                Log.d(TAG, "Failure: " + ex.getMessage());
                VolleyError error = new ParseError();
                callback.onUserError(error);
            }
        }, error -> {
            Log.d("ERROR", "Error login: " + error);
            callback.onUserError(error);
        }){
            /*@Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                return params;
            }*/
        };
        Log.d(TAG, "getProduct: " + stringRequest);
        //stringRequest.setTag(CURRENT_USER_TAG);
        requestQueue.add(stringRequest);
    }
    /**
     * A method that makes the URL calls for the weather information, using Volley.
     * If the method gets a response, parse the data and create a weather to add to the list, callback to main activity if successful.
     * If an error occurs the methods makes a callback to the main activity and informs about the error.
     *
     * @param callback
     */
    /*public void createUser(@NonNull final String uuid, @NonNull final UserService.UserCallback callback) {
        Log.d("URL", "URL: " + Constant.URL_SERVER);
        JSONObject postObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            JsonObject jsonUserObject = new JsonObject();

            jsonUserObject.addProperty("userID", uuid);
            jsonArray.put(jsonUserObject);
            postObject.put("user", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_SERVER+"/login" ,response -> {
            try {
                callback.onUserSuccess(uuid);
            } catch (JSONException | IOException ex) {
                Log.d(TAG, "Failure: " + ex.getMessage());
                VolleyError error = new ParseError();
                callback.onUserError(error);
            }
        }, error -> {
            Log.d("ERROR", "createUser: " + error);
            callback.onUserError(error);
        }){
          @Override
          protected Map<String, String> getParams(){
              Map<String, String> params = new HashMap<>();
              params.put("userID", uuid);
              return params;
          }
        };
        Log.d(TAG, "getProduct: " + stringRequest);
        stringRequest.setTag(CURRENT_USER_TAG);
        requestQueue.add(stringRequest);
    }*/

    /**
     * Cancels the queue for URL calls.
     */
    public void cancel() {
        requestQueue.cancelAll(CURRENT_USER_TAG);
    }

    public void volleyPost(String uuid){
        JSONObject postObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonUserObject = new JSONObject();

            jsonUserObject.put("userID", uuid);

            jsonArray.put(jsonUserObject);

            postObject.put("user", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Constant.URL_HEROKU, postObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("RESPONSE", "onResponse: " + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("RESPONSE", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void login(String username, String password, Callback callback){
        Gson gson = new Gson();
        User user = new User(username, password);
        String json = gson.toJson(user);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url("https://heroku-boot-kth.herokuapp.com/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(callback);
    }

}

