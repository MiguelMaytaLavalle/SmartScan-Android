package com.example.scannieboi_android.services;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scannieboi_android.Constant;
import com.example.scannieboi_android.data.model.MyProductData;
import com.stomped.stomped.client.StompedClient;
import com.stomped.stomped.component.StompedFrame;
import com.stomped.stomped.component.StompedHeaders;
import com.stomped.stomped.listener.StompedListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartService {
    private static final String CURRENT_CART = "CURRENT_CART";
    private StompedClient stompedClient;
    private RequestQueue requestQueue;
    private final String TAG = "CartService";

    /**
     * Constructor for WeatherService, creates a Request queue for the application context.
     *
     * @param activity
     */
    public CartService(@NonNull final Activity activity) {
        requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
    }

    /**
     * An interface for the callback methods.
     */
    public interface CartCallback {

        @MainThread
        void onShareCartSuccess(@NonNull String cartID) throws IOException, JSONException;

        @MainThread
        void onCartSuccess(@NonNull final String cartID, List<MyProductData> products) throws IOException, JSONException;

        @MainThread
        void onCartError(@NonNull VolleyError volleyError);

    }
    /**
     * A method that makes the URL calls for the weather information, using Volley.
     * If the method gets a response, parse the data and create a weather to add to the list, callback to main activity if successful.
     * If an error occurs the methods makes a callback to the main activity and informs about the error.
     *
     * @param callback
     */
    public void shareCart(@NonNull final String username, @NonNull final CartCallback callback) {
        Log.d("URL", "URL: " );
        Log.d(TAG, "username: " + username);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Constant.URL_HEROKU+"/api/v1/sharedcart", response -> {
            Log.d(TAG, "onResponse" + response);
            try {
                System.out.println(response);
                callback.onShareCartSuccess(response);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> callback.onCartError(error)){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<>();
                params.put("owner", username);
                return params;
            }
        };
        Log.d(TAG, "getProduct: " + stringRequest);
        stringRequest.setTag(CURRENT_CART);
        requestQueue.add(stringRequest);
    }

    /**
     * A method that makes the URL calls for the weather information, using Volley.
     * If the method gets a response, parse the data and create a weather to add to the list, callback to main activity if successful.
     * If an error occurs the methods makes a callback to the main activity and informs about the error.
     *
     * @param callback
     */
    public void subscribeToCart(@NonNull final String username, @NonNull final String cartID, @NonNull final CartCallback callback) {
        Log.d("URL", "URL: ");
        // Testa med ws & http
        // fungerar ibland

        stompedClient = new StompedClient.StompedClientBuilder().build("wss://heroku-boot-kth.herokuapp.com/ws/websocket");
        //stompedClient = new StompedClient.StompedClientBuilder().build("websocket://"+Constant.URL_HEROKU);
        stompedClient.subscribe("/output/"+username+"/"+cartID, new StompedListener() {
            @Override
            public void onNotify(StompedFrame frame) {
                // Do work here
                System.out.println("stomperbody: " + frame.getStompedBody());
                System.out.println("stomperheaders: " + frame.getStompedHeaders());
                System.out.println("stomperbody header destinations: " + frame.getHeaderValueFromKey(StompedHeaders.STOMP_HEADER_DESTINATION));


                try {
                    JSONObject root = new JSONObject(frame.getStompedBody());
                    String pName = root.getString("name");
                    String pImageURL = root.getString("imageURL");
                    String pCategory = root.getString("category");
                    String pCountry = root.getString("coo");
                    String pManufacture = root.getString("manufacturer");
                    String pCO2;

                    try {
                        pCO2 = root.getString("co2") + " kg CO2e/kg";
                    } catch (Exception e){
                        pCO2 = "No Data Found";
                    }
                    String pRecommendation = root.getString("recommendation");
                    String pDescription = root.getString("description");

                    MyProductData productData = new MyProductData();
                    productData.setName(pName);
                    productData.setImageURL(pImageURL);
                    productData.setCategory(pCategory);
                    productData.setCountryOfOrigin(pCountry);
                    productData.setManufacture(pManufacture);
                    productData.setCo2(pCO2);
                    productData.setRecommendation(pRecommendation);
                    productData.setDescription(pDescription);
                    List<MyProductData> productDataList = new ArrayList<>();
                    productDataList.add(productData);
                    callback.onCartSuccess(cartID, productDataList);
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }

                /*try {
                    callback.onCartSuccess(cartID);
                    System.out.println(cartID);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }*/
            }
        });
        stompedClient.send("/input/"+username+"/"+cartID);
    }

    /**
     * Cancels the queue for URL calls.
     */
    public void cancel() {
        requestQueue.cancelAll(CURRENT_CART);
    }

}

