package com.example.scannieboi_android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.VolleyError;
import com.example.scannieboi_android.data.model.MVCModel;
import com.example.scannieboi_android.data.model.MyProductData;
import com.example.scannieboi_android.services.CartService;
import com.example.scannieboi_android.services.ScannerService;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.ResultPoint;
import com.google.zxing.WriterException;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;
import com.journeyapps.barcodescanner.DefaultDecoderFactory;

import org.json.JSONException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

/**
 * This sample performs continuous scanning, displaying the barcode and source image whenever
 * a barcode is scanned.
 */
public class
ContinuousCaptureActivity extends Activity implements View.OnClickListener {
    private MVCModel mvcModel;
    private final String API_KEY = "6480301a-6977-43f8-a2d0-fa1a384cb1d3";

    private ScannerService scannerService;
    private CartService cartService;
    private BeepManager beepManager;

    private DecoratedBarcodeView barcodeView;
    private TextView pTextViewName;
    private String lastText;
    private ImageView imageQR;
    private Button buttonShareCart;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private QRGEncoder qrgEncoder;
    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.continuous_scan);

        pTextViewName = findViewById(R.id.text_productname);
        barcodeView = findViewById(R.id.barcode_scanner);
        buttonShareCart = findViewById(R.id.button_share_cart);

        Intent intent = getIntent();
        mvcModel = intent.getParcelableExtra("mvcmodel");

        Collection<BarcodeFormat> formats = Arrays.asList(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39);
        barcodeView.getBarcodeView().setDecoderFactory(new DefaultDecoderFactory(formats));
        barcodeView.initializeFromIntent(intent);
        barcodeView.decodeContinuous(callback);

        beepManager = new BeepManager(this);
        scannerService = new ScannerService(this);
        cartService = new CartService(this);

        buttonShareCart.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        barcodeView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        barcodeView.pause();
    }

    private void searchForProduct(@NonNull final String barcode, @NonNull final String key){
        Log.d("TEST", "Barcode: " + barcode + ", " + "Key: " + key);
        //scannerService.getProductTest(barcode, key, productCallback);
        scannerService.getProductDABAS(barcode, productCallback);
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if(result.getText() == null || result.getText().equals(lastText)) {
                // Prevent duplicate scans
                return;
            }
            searchForProduct(result.getText(), API_KEY);
            lastText = result.getText();
            System.out.println("Last text: " + lastText);
            Log.d("LASTTEXT", "barcodeResult: " + lastText);

            //beepManager.playBeepSoundAndVibrate();
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    public void pause(View view) {
        barcodeView.pause();
    }

    public void resume(View view) {
        barcodeView.resume();
    }

    public void triggerScan(View view) {
        barcodeView.decodeSingle(callback);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed(){
        returnIntent();
    }

    private void returnIntent() {
        Intent returnIntent = new Intent(this, NavigationActivity.class);
        returnIntent.putExtra("mvcmodel", mvcModel);
        setResult(RESULT_OK, returnIntent);
        finish();
    }

    private final ScannerService.ProductCallback productCallback = new ScannerService.ProductCallback() {
        @Override
        public void onProduct(@NonNull List<MyProductData> products) throws IOException, JSONException {
            Log.i("Callback", "True!");
            mvcModel.getProductDataList().add(products.get(0));
            Log.d("Callback", "onProduct: " + mvcModel.getProductDataList().size());
            for(MyProductData p : products){
                Log.d("PRODUCT", "" + p.getName());
                pTextViewName.setText(p.getName());
            }
        }
        @Override
        public void onError(@NonNull VolleyError volleyError) {
            Toast.makeText(ContinuousCaptureActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }
    };

    private final CartService.CartCallback cartCallback = new CartService.CartCallback() {

        @Override
        public void onCartSuccess(@NonNull String cartID, List<MyProductData> products) throws IOException, JSONException {

        }

        @Override
        public void onShareCartSuccess(@NonNull String cartID) throws IOException, JSONException {
            WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

            // initializing a variable for default display.
            Display display = manager.getDefaultDisplay();

            // creating a variable for point which
            // is to be displayed in QR Code.
            Point point = new Point();
            display.getSize(point);

            // getting width and
            // height of a point
            int width = point.x;
            int height = point.y;

            // generating dimension from width and height.
            int dimen = width < height ? width : height;
            dimen = dimen * 3 / 4;

            final View popupView = getLayoutInflater().inflate(R.layout.popupwindow_qrcode, null);
            imageQR = popupView.findViewById(R.id.image_QR_view);
            mvcModel.setCartID(cartID);

            qrgEncoder = new QRGEncoder(cartID, null, QRGContents.Type.TEXT, dimen);
            try {
                // getting our qrcode in the form of bitmap.
                bitmap = qrgEncoder.encodeAsBitmap();
                // the bitmap is set inside our image
                // view using .setimagebitmap method.
                imageQR.setImageBitmap(bitmap);
            } catch (WriterException e) {
                // this method is called for
                // exception handling.
                Log.e("Tag", e.toString());
            }
            dialogBuilder.setView(popupView);
            dialog = dialogBuilder.create();
            dialog.show();
        }

        @Override
        public void onCartError(@NonNull VolleyError volleyError) {
            System.out.println(volleyError);
        }
    };

    @Override
    public void onClick(View view) {
        dialogBuilder = new AlertDialog.Builder(this);
        cartService.shareCart("lampa", cartCallback);


    }
}