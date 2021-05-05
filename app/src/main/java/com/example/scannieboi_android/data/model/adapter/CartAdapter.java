package com.example.scannieboi_android.data.model.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.scannieboi_android.OverviewProductActivity;
import com.example.scannieboi_android.R;
import com.example.scannieboi_android.data.model.MyProductData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<MyProductData> mCartList;
    private Context mContext;
    private LayoutInflater mInflater;


    public CartAdapter(List<MyProductData> cartList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.mCartList = cartList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView primage;
        public CardView mCardView;
        public TextView textViewPrice, textViewName, textViewCountry, textViewDesc;
        public CartAdapter mAdapter;

        public ViewHolder(View itemView, CartAdapter adapter) {
            super(itemView);
            mCardView = itemView.findViewById(R.id.card_cart);
            primage = (ImageView) itemView.findViewById(R.id.primage);
            textViewPrice = (TextView) itemView.findViewById(R.id.txtprprice);
            textViewName = (TextView) itemView.findViewById(R.id.txtprname);
            textViewCountry = (TextView) itemView.findViewById(R.id.txtprcountry);
            mAdapter = adapter;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        MyProductData parameter = mCartList.get(position);
        holder.textViewName.setText(parameter.getName());
        holder.textViewCountry.setText(parameter.getCountryOfOrigin());
        if(parameter.getBitmap() == null){
            holder.primage.setImageResource(R.drawable.no_image_icon);
        }else{
            holder.primage.setImageBitmap(parameter.getBitmap());
        }

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Card number" + position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, OverviewProductActivity.class);
                intent.putExtra("product", mCartList.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mCartList != null){
            return mCartList.size();
        }else{
            return 0;
        }
    }


}

