package com.example.scannieboi_android.data.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.scannieboi_android.R;
import com.example.scannieboi_android.ShoppingListActivity;
import com.example.scannieboi_android.data.model.ListItem;
import com.example.scannieboi_android.data.model.ShopList;

import java.util.List;

public class ShoppingListItemsAdapter extends RecyclerView.Adapter<ShoppingListItemsAdapter.ViewHolder> {
    private ShopList mShopList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ShoppingListItemsAdapter(ShopList mShopList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mShopList = mShopList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public CardView mCardView;
        private ShoppingListItemsAdapter mAdapter;

        public ViewHolder(View itemView, ShoppingListItemsAdapter adapter) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_shoppinglist_items);
            mTextView = itemView.findViewById(R.id.textview_shoplist_item);
            mAdapter = adapter;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shoppinglist_items, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        List<ListItem> parameter = mShopList.getListItems();
        holder.mTextView.setText(parameter.get(position).getProductName());
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on card item#" + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mShopList != null){
            return mShopList.getListItems().size();
        }else{
            return 0;
        }
    }

}

