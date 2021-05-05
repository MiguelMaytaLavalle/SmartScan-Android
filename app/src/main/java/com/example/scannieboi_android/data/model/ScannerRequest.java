package com.example.scannieboi_android.data.model;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

public class ScannerRequest extends Request<byte[]> {

    public ScannerRequest(int method, String url, @Nullable Response.ErrorListener listener) {
        super(method, url, listener);

    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        byte[] data = response.data;
        return null;
    }

    @Override
    protected void deliverResponse(byte[] response) {

    }
}
