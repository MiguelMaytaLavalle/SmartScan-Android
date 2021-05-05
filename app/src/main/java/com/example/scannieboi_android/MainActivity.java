package com.example.scannieboi_android;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.scannieboi_android.services.ScannerService;
import com.google.zxing.integration.android.IntentIntegrator;

/**
 * MainActivity should be the "start page" of the app.
 * Should be able to go to "scanning" procedure.
 *
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button scanBtn;
    private TextView tvScanFormat, tvScanContent;
    private LinearLayout llSearch;
    private ScannerService scannerService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scannerService = new ScannerService(this);

        scanBtn = (Button) findViewById(R.id.scan_button);
        tvScanFormat = (TextView) findViewById(R.id.tvScanFormat);
        tvScanContent = (TextView) findViewById(R.id.tvScanContent);
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        scanBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        llSearch.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setPrompt("Scan a barcode");
        integrator.setOrientationLocked(false);

        integrator.setDesiredBarcodeFormats(IntentIntegrator.EAN_13);
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }
}