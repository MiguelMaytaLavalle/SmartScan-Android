package com.example.scannieboi_android.data.model.singleton;


import com.example.scannieboi_android.data.model.Cart;

public class CartSingleton {
    private static Cart theCart;

    private CartSingleton() {
    }

    public static Cart getInstance()
    {
        if (theCart == null)
            theCart = new Cart();
        return theCart;
    }
}
