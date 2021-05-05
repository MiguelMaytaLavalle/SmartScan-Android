package com.example.scannieboi_android;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;

import com.example.scannieboi_android.data.model.Cart;
import com.example.scannieboi_android.data.model.MyProductData;
import com.example.scannieboi_android.data.model.ShopList;
import com.example.scannieboi_android.data.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class JsonRW {
    private static final String FILE_NAME = "cart_list.json";
    private static final String FILE_USER_CRED = "user_cred.json";
    private static final String UUID_FILE = "uuid_string.json";

    public JsonRW() { }

    /**
     * Saves the forecast object in an serialized object
     * @param context
     * @param cartData
     */
    public static void writeToFile(Context context, List<MyProductData> cartData){
        JSONObject saveToFile = new JSONObject();
        FileOutputStream fos = null;
        JSONArray paramArray = new JSONArray();

        List<MyProductData> cartList = cartData;
        try {
            //saveToFile.put("approvedTime", forecast.getApprovedTime());
            //arrayCoordinates.put(0, forecast.getCoordinates().getLongitude());
            //arrayCoordinates.put(1, forecast.getCoordinates().getLatitude());
            for(int i = 0; i < cartList.size(); i++){
                JSONObject product = new JSONObject();
                product.put("name", cartList.get(i).getName());
                product.put("imageURL", cartList.get(i).getImageURL());
                product.put("category", cartList.get(i).getCategory());
                product.put("coo", cartList.get(i).getCountryOfOrigin());
                product.put("manufacturer", cartList.get(i).getManufacture());
                product.put("co2", cartList.get(i).getCo2());
                product.put("recommendation", cartList.get(i).getRecommendation());
                product.put("description", cartList.get(i).getDescription());
                paramArray.put(i, product);
            }
            saveToFile.put("Products", paramArray);

            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(saveToFile.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                fos = null;
            }
        }
    }

    /**
     * Saves the forecast object in an serialized object
     * @param context
     * @param shoppingLists
     */
    public static void writeShoppingListsToFile(Context context, List<ShopList> shoppingLists){
        JSONObject saveToFile = new JSONObject();
        FileOutputStream fos = null;
        JSONArray paramArray = new JSONArray();

        List<ShopList> cartList = shoppingLists;
        try {
            JSONObject product = new JSONObject();
            product.put("shoppinglist", shoppingLists);
            //saveToFile.put("approvedTime", forecast.getApprovedTime());
            //arrayCoordinates.put(0, forecast.getCoordinates().getLongitude());
            //arrayCoordinates.put(1, forecast.getCoordinates().getLatitude());
            saveToFile.put("product", product);

            fos = context.openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
            fos.write(saveToFile.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                fos = null;
            }
        }
    }

    /**
     * When invoked it will read from the saved file
     * and return it back as an JSONObject.
     * @param context
     * @return
     */

    public static JSONObject readFromFile(Context context){
        String ret = "";
        JSONObject jsonObject = null;
        try {
            InputStream inputStream = context.openFileInput(FILE_NAME);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                jsonObject = new JSONObject(ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            Toast.makeText(context, "Could Not Find Saved File", Toast.LENGTH_SHORT).show();
            return jsonObject;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("READ FROM FILE: " + jsonObject.toString());
        return jsonObject;
    }

    /**
     * Just used to find the correct folder
     * when searching for the file.
     * Not used for anything else.
     * @param context
     * @param filename
     * @return
     */
    public static boolean isFilePresent(Context context, String filename){
        String path = context.getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        System.out.println(path);
        return file.exists();
    }

    public static void createUUIDToFile(Context context, String UUID){
        JSONObject saveToFile = new JSONObject();
        FileOutputStream fos = null;
        try {
            saveToFile.put("UserID", UUID);
            fos = context.openFileOutput(UUID_FILE, Context.MODE_PRIVATE);
            fos.write(saveToFile.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                fos = null;
            }
        }
    }

    public static JSONObject readUserFromFile(Context context){
        String ret = "";
        JSONObject jsonObject = null;
        try {
            InputStream inputStream = context.openFileInput(FILE_USER_CRED);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }
                inputStream.close();
                ret = stringBuilder.toString();
                jsonObject = new JSONObject(ret);
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            Toast.makeText(context, "Could Not Find Saved File", Toast.LENGTH_SHORT).show();
            return jsonObject;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println("READ FROM FILE: " + jsonObject.toString());

        return jsonObject;
    }

    public static void writeToFileUserAuth(Context context, User user){
        FileOutputStream fos = null;
        try {
            JSONObject userCredentials = new JSONObject();

            userCredentials.put("username", user.getUsername());
            userCredentials.put("email", user.getEmail());
            userCredentials.put("authentication", user.getAuthToken());

            fos = context.openFileOutput(FILE_USER_CRED, Context.MODE_PRIVATE);
            fos.write(userCredentials.toString().getBytes());
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                fos = null;
            }
        }
    }


}
