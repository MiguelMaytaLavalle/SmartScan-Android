package com.example.scannieboi_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.scannieboi_android.data.model.MyProductData;

public class OverviewProductActivity extends AppCompatActivity {
    private TextView mTitleText, mCategoryText, mCOOText, mRecommendationText, mPollutionText, mDescriptionText;
    private MyProductData productData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview_product);
        mTitleText = findViewById(R.id.txt_title);
        mCategoryText = findViewById(R.id.txt_category);
        mCOOText = findViewById(R.id.txt_country);
        mRecommendationText = findViewById(R.id.txt_recommendation);
        mPollutionText = findViewById(R.id.txt_pollution);
        mDescriptionText = findViewById(R.id.txt_description);

        Intent intent = getIntent();
        productData = intent.getParcelableExtra("product");
        setupText();

    }

    private void setupText(){
        mTitleText.setText(productData.getName());
        mCategoryText.setText("Kategori: " + productData.getCategory());
        mCOOText.setText("Tillverkningsland: " + productData.getCountryOfOrigin());
        mRecommendationText.setText("Rekommendation: " + productData.getRecommendation());
        mPollutionText.setText("Utsläpp: " + productData.getCo2());
    }
}
