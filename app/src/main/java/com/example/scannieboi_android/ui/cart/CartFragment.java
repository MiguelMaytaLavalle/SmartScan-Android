package com.example.scannieboi_android.ui.cart;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.JsonRW;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.MVCModel;
import com.example.scannieboi_android.data.model.MyProductData;
import com.example.scannieboi_android.data.model.adapter.CartAdapter;
import com.example.scannieboi_android.services.CartService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment{

    private CartViewModel cartViewModel;
    private CartAdapter mAdapter;
    private DividerItemDecoration dividerItemDecoration;
    private MVCModel mvcModel;
    private RecyclerView mRecyclerView;
    private Button buttonShareCart;
    private Button buttonSubCart;
    private View root;

    private CartService cartService;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        cartViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        root = inflater.inflate(R.layout.fragment_cart, container, false);
        this.mvcModel = new MVCModel();
        cartService = new CartService(this.getActivity());

        isUserLoggedIn();

        getCartList();
        buttonShareCart = root.findViewById(R.id.button_cart_share);
        buttonSubCart = root.findViewById(R.id.button_cart_subscribe);
        mRecyclerView = root.findViewById(R.id.recycle_view_cart);
        mAdapter = new CartAdapter(mvcModel.getProductDataList(), this.getContext());

        dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(this.getContext()).getOrientation());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        buttonShareCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartService.shareCart("test3", cartCallback);
            }
        });
        buttonSubCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cartService.subscribeToCart(mvcModel.getUsername(), "6048c7647d79a40df1800d8e", cartCallback);
                cartService.subscribeToCart("test2", "6048c7647d79a40df1800d8e", cartCallback);
            }
        });

        //mAdapter.notifyDataSetChanged();
        return root;
    }

    //TODO Change this readfromfile part so it does from the class instead
    public void getCartList(){
        JSONObject jsonObject = JsonRW.readFromFile(this.getContext());
        List<MyProductData> productDataList = new ArrayList<>();
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("Products");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject test = jsonArray.getJSONObject(i);
                String pName = test.getString("name");
                String pImageURL = test.getString("imageURL");
                String pCategory = test.getString("category");
                String pCoo = test.getString("coo");
                String pManufacture = test.getString("manufacturer");
                String pCO2 = test.getString("co2");
                String pRecommendation = test.getString("recommendation");
                String pDescription = test.getString("description");

                MyProductData productData = new MyProductData(pName, pImageURL, pCategory, pCoo, pManufacture, pCO2, pRecommendation, pDescription);
                productDataList.add(productData);
            }
            mvcModel.setProductDataList(productDataList);
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mvcModel.getProductDataList().remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
        }
    };

    private final CartService.CartCallback cartCallback = new CartService.CartCallback() {
        @Override
        public void onShareCartSuccess(@NonNull String cartID) throws IOException, JSONException {
            mvcModel.setCartID(cartID);
        }

        @Override
        public void onCartSuccess(@NonNull String cartID, @NonNull List<MyProductData> products) throws IOException, JSONException {
            //Toast.makeText(root.getContext(), "Success response: " + cartID, Toast.LENGTH_SHORT).show();
            // Ta emot cartid och lagra temporärt någonstans...
            // Används för att generera en QR kod men yeah
            mvcModel.setCartID(cartID);
            Log.i("Callback", "True!");
            mvcModel.getProductDataList().add(products.get(0));
            Log.d("Callback", "onProduct: " + mvcModel.getProductDataList().size());
            for(MyProductData p : products){
                Log.d("PRODUCT", "" + p.getName());
                mvcModel.getProductDataList().add(p);
                //mAdapter.notifyDataSetChanged();
                //pTextViewName.setText(p.getName());
            }
            JsonRW.writeToFile(root.getContext(), mvcModel.getProductDataList());

        }

        @Override
        public void onCartError(@NonNull VolleyError volleyError) {
            Toast.makeText(root.getContext(), "Error: " + volleyError.getMessage(), Toast.LENGTH_SHORT).show();

        }
    };

    private void isUserLoggedIn() {
        JSONObject userExist = JsonRW.readUserFromFile(root.getContext());
        try {
            String username = userExist.getString("username");
            mvcModel.setUsername(username);
            mvcModel.setLoggedIn(true);

        } catch (JSONException | NullPointerException e) {
            mvcModel.setLoggedIn(false);
        }
    }
}
