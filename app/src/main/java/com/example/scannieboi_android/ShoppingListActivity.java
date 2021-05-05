package com.example.scannieboi_android;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scannieboi_android.data.model.ListItem;
import com.example.scannieboi_android.data.model.ShopList;
import com.example.scannieboi_android.data.model.adapter.ShoppingListItemsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {
    private Button mButtonActive;
    private TextView mListName;
    private RecyclerView mRecyclerView;
    private SearchView mSearchView;
    private ShoppingListItemsAdapter mAdapter;
    private ShopList listItems;
    private DividerItemDecoration dividerItemDecoration;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoppinglist);
        Intent intent = getIntent();
        listItems = intent.getParcelableExtra("shoppinglist");

        mListName = findViewById(R.id.textview_shoppinglist_name);
        mButtonActive = findViewById(R.id.button_shoppinglist_active);
        mRecyclerView = findViewById(R.id.recyclerview_shoppinglist_items);
        mSearchView = findViewById(R.id.searchview_shoppinglist_items);

        mListName.setText(listItems.getShopListName());
        mAdapter = new ShoppingListItemsAdapter(listItems, this);
        dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mAdapter.notifyDataSetChanged();

        mButtonActive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make active for cart?
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                addItem(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }

        });
    }

    private void addItem(String stringItem){
        ListItem newItem = new ListItem(stringItem);
        listItems.getListItems().add(newItem);
        mAdapter.notifyDataSetChanged();
    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            // mvcModel.getProductDataList().remove(viewHolder.getAdapterPosition());
            listItems.getListItems().remove(viewHolder.getAdapterPosition());
            //mShopList.get(viewHolder.getAdapterPosition()).getListItems().remove(viewHolder.getAdapterPosition());
            mAdapter.notifyDataSetChanged();
        }
    };
    
}
