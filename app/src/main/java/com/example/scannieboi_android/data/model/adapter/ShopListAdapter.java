package com.example.scannieboi_android.data.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
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
import com.example.scannieboi_android.data.model.ShopList;

import java.util.List;

public class ShopListAdapter extends RecyclerView.Adapter<ShopListAdapter.ViewHolder> {
    private List<ShopList> mShopList;
    private Context mContext;
    private LayoutInflater mInflater;

    public ShopListAdapter(List<ShopList> mShopList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mShopList = mShopList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewShopList, textViewDate, textViewListSize;
        public CardView mCardView;
        private ShopListAdapter mAdapter;

        public ViewHolder(View itemView, ShopListAdapter adapter) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_shoplist);
            textViewShopList = itemView.findViewById(R.id.text_view_shoplist_name);
            textViewDate = itemView.findViewById(R.id.text_view_shoplist_date);
            textViewListSize = itemView.findViewById(R.id.text_view_shoplist_size);
            mAdapter = adapter;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_shoplist, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ShopList parameter = mShopList.get(position);
        holder.textViewShopList.setText(parameter.getShopListName());
        holder.textViewDate.setText(parameter.getDate());
        holder.textViewListSize.setText(parameter.getListItems().size() + " varor");
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Click on card #" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, ShoppingListActivity.class);
                //intent.putExtra("shoppinglist", (Parcelable) mShopList.get(position).getListItems());
                intent.putExtra("shoppinglist", mShopList.get(position));
                mContext.startActivity(intent);
             }
        });
    }

    @Override
    public int getItemCount() {
        if(mShopList != null){
            return mShopList.size();
        }else{
            return 0;
        }
    }

}

