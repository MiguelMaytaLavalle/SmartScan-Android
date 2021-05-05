package com.example.scannieboi_android.services;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scannieboi_android.Constant;
import com.example.scannieboi_android.data.model.MyProductData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScannerService {
    private static final String CURRENT_PRODUCT_TAG = "CURRENT_PRODUCT";
    private final String TAG = "ROOT";
    private Context mContext;
    private RequestQueue requestQueue;

    /**
     * Constructor for WeatherService, creates a Request queue for the application context.
     * @param activity
     */
    public ScannerService(@NonNull final Activity activity) {
        mContext = activity.getApplicationContext();
        requestQueue = Volley.newRequestQueue(mContext);
    }

    /**
     * An interface for the callback methods.
     */
    public interface ProductCallback {
        @MainThread
        void onProduct(@NonNull final List<MyProductData> products) throws IOException, JSONException;

        @MainThread
        void onError(@NonNull VolleyError volleyError);
    }

    /**
     * A method that makes the URL calls for the weather information, using Volley.
     * If the method gets a response, parse the data and create a weather to add to the list, callback to main activity if successful.
     * If an error occurs the methods makes a callback to the main activity and informs about the error.
     * @param callback
     */

    /**
     * Cancels the queue for URL calls.
     */
    public void cancel(){
        requestQueue.cancelAll(CURRENT_PRODUCT_TAG);
    }

    public void getProductDABAS(@NonNull final String barcode, @NonNull final String key, @NonNull final ProductCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.URL_SCANNIE+"/getproduct?barcode="+barcode, response -> {
            try {
                byte[] utf8 = new String(response.getBytes(), "ISO-8859-1").getBytes("UTF-8");
                response = new String(utf8);

                String jsonString = new String(response.getBytes(), "UTF-8");
                System.out.println(jsonString);
                Log.d(TAG, "getProductTest: " + response);
                List<MyProductData> productDataList = new ArrayList<>();
                Log.d("URL", "getProduct: " + response);
                JSONObject root = new JSONObject(response);
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
                productDataList.add(productData);

                callback.onProduct(productDataList);
            } catch (JSONException | IOException ex) {
                Log.d(TAG, "Failure: " + ex.getMessage());
                VolleyError error = new ParseError();
                callback.onError(error);
            }
        }, error -> callback.onError(error)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/json; charset=utf-8");

                return params;
            }
        };
        Log.d(TAG, "getProduct: " + stringRequest);
        stringRequest.setTag(CURRENT_PRODUCT_TAG);
        requestQueue.add(stringRequest);
    }

    public void getProductDABAS(@NonNull final String barcode, @NonNull final ProductCallback callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, Constant.URL_SCANNIE + "/getproduct?barcode="+barcode, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject root = response;
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

                            callback.onProduct(productDataList);
                        } catch (JSONException | IOException ex) {
                            Log.d(TAG, "Failure: " + ex.getMessage());
                            VolleyError error = new ParseError();
                            callback.onError(error);
                        }                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        callback.onError(error);

                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    public void getBitmapFromURL(MyProductData productData){
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        String imageURL = productData.getImageURL();
        ImageRequest imageRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d("IMAGE", "onResponse: " + response);
                productData.setBitmap(response);

            }
        }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("IMAGE", "onErrorResponse: " + error);
            }
        });
        requestQueue.add(imageRequest);
    }
}
