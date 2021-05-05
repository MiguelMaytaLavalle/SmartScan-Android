package com.example.scannieboi_android.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scannieboi_android.data.model.ShopList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopListService {
    private String URL_GET_LIST = "http://192.168.1.185:3080/user_service/";
    private static final String CURRENT_SHOPLIST_TAG = "CURRENT_SHOPLIST";
    private RequestQueue requestQueue;
    private final String TAG = "ROOT";

    /**
     * Constructor for WeatherService, creates a Request queue for the application context.
     *
     * @param activity
     */
    public ShopListService(@NonNull final Activity activity) {
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
    }

    /**
     * An interface for the callback methods.
     */
    public interface ShopListCallback {
        @MainThread
        void onShopListSuccess(@NonNull final List<ShopList> shopList) throws IOException, JSONException;

        @MainThread
        void onShopListError(@NonNull VolleyError volleyError);

    }
    /**
     * A method that makes the URL calls for the weather information, using Volley.
     * If the method gets a response, parse the data and create a weather to add to the list, callback to main activity if successful.
     * If an error occurs the methods makes a callback to the main activity and informs about the error.
     *
     * @param callback
     */
    public void getUserLists(@NonNull final String userID, @NonNull final ShopListCallback callback) {
        String url = URL_GET_LIST + userID + "/lists";
        Log.d("URL", "URL: " + url);

        List<ShopList> shopLists = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,response -> {
            try {
                JSONArray listArray = new JSONArray(response);
                for(int i = 0; i < listArray.length(); i++){
                    JSONObject parameter = listArray.getJSONObject(i);

                    String title = parameter.getString("title");
                    String listID = parameter.getString("listID");
                    String date = parameter.getString("date");

                    ShopList shopList = new ShopList(title, null, listID, userID, date);
                    shopLists.add(shopList);
                }
                callback.onShopListSuccess(shopLists);
            } catch (JSONException | IOException ex) {
                Log.d(TAG, "Failure: " + ex.getMessage());
                VolleyError error = new ParseError();
                callback.onShopListError(error);
            }
        }, error -> {
            Log.d("ERROR", "createUser: " + error);
            callback.onShopListError(error);
        });

        Log.d(TAG, "getProduct: " + stringRequest);
        stringRequest.setTag(CURRENT_SHOPLIST_TAG);
        requestQueue.add(stringRequest);
    }

    /**
     * Cancels the queue for URL calls.
     */
    public void cancel() {
        requestQueue.cancelAll(CURRENT_SHOPLIST_TAG);
    }

}

