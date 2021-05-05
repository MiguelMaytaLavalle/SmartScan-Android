package com.example.scannieboi_android.data.model;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private List<MyProductData> products;
    private Double subtotal;

    public Cart() {
        this.products = new ArrayList<>();
        this.subtotal = 0.0;
    }

    public List<MyProductData> getProducts() {
        return products;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }
}
