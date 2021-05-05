package com.example.scannieboi_android.ui.shoplist;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.ListItem;
import com.example.scannieboi_android.data.model.MVCModel;
import com.example.scannieboi_android.data.model.ShopList;
import com.example.scannieboi_android.data.model.adapter.ShopListAdapter;
import com.example.scannieboi_android.services.ShopListService;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/// TODO: 2021-01-11 Anrop fixa det här.
public class ShopListFragment extends Fragment implements View.OnClickListener{

    private ShopListViewModel shopListViewModel;
    private List<ShopList> mShopList;
    private RecyclerView mRecyclerView;
    private ShopListAdapter mAdapter;
    private Button createListButton;
    private DividerItemDecoration dividerItemDecoration;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText edittextShoppingListName;
    private Button buttonCreateShoppingList;

    private Context mContext;
    private View mRoot;
    private MVCModel mvcModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        shopListViewModel = new ViewModelProvider(this).get(ShopListViewModel.class);
        mRoot = inflater.inflate(R.layout.fragment_shoplist, container, false);
        mContext = this.getContext();
        //createExampleList();


        mRecyclerView = mRoot.findViewById(R.id.recyclerview_shoplist);
        mAdapter = new ShopListAdapter(mShopList, this.getContext());

        dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(this.getContext()).getOrientation());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        createListButton = mRoot.findViewById(R.id.button_createlist);
        createListButton.setOnClickListener(this);

        return mRoot;
    }


    private final ShopListService.ShopListCallback shopListCallback = new ShopListService.ShopListCallback() {

        @Override
        public void onShopListSuccess(@NonNull List<ShopList> shopList) throws IOException, JSONException {
            Log.i("Callback", "Shoplist True!");
        }

        @Override
        public void onShopListError(@NonNull VolleyError volleyError) {
        }
    };


    @Override
    public void onClick(View view) {
        Toast.makeText(mContext, "Hey", Toast.LENGTH_SHORT).show();

        dialogBuilder = new AlertDialog.Builder(mContext);
        final View popupView = getLayoutInflater().inflate(R.layout.popupwindow_createlist, null);
        buttonCreateShoppingList = popupView.findViewById(R.id.button_shoplist_create);
        edittextShoppingListName = popupView.findViewById(R.id.edittext_shoplist_name);
        buttonCreateShoppingList.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String listName = edittextShoppingListName.getText().toString();
                if(!listName.isEmpty()){
                    List<ListItem> newListItems = new ArrayList<>();
                    ShopList s1 = new ShopList(listName, newListItems, "s"+mShopList.size(), "user1", LocalDate.now().toString());
                    mShopList.add(s1);
                    dialog.cancel();
                    Toast.makeText(mContext, "Create list " + listName, Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(mContext, "Fyll i namn till inköpslistan", Toast.LENGTH_SHORT).show();
                }

            }
        });
        dialogBuilder.setView(popupView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    public void createNewShopList(){

    }

    public void createExampleList(){
        mShopList = new ArrayList<>();
        List<ListItem> l1 = new ArrayList<>();
        ListItem i1 = new ListItem("Banan");
        ListItem i2 = new ListItem("Ris");
        ListItem i3 = new ListItem("Mjölk");

        l1.add(i1);
        l1.add(i2);
        l1.add(i3);

        ShopList s1 = new ShopList("List1", l1, "s1", "user1", "2021-02-10");
        mShopList.add(s1);

        List<ListItem> l2 = new ArrayList<>();
        ShopList s2 = new ShopList("List2", l2, "s2", "user2", "2021-01-10");
        mShopList.add(s2);

        List<ListItem> l3 = new ArrayList<>();
        ShopList s3 = new ShopList("List3", l3, "s3", "user3", "2021-02-08");
        mShopList.add(s3);
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mShopList.remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
        }
    };


}