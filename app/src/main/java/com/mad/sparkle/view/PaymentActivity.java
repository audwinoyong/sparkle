package com.mad.sparkle.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;
import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class PaymentActivity extends AppCompatActivity {

    private CardForm mCardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mCardForm = findViewById(R.id.activity_payment_card_form);

        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .maskCvv(true)
                .actionLabel("Pay Now")
                .cardholderName(CardForm.FIELD_REQUIRED)
                .postalCodeRequired(true)
                .setup(PaymentActivity.this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.payment, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_camera_scan:
                // Check camera permission
                if (ContextCompat.checkSelfPermission(PaymentActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PaymentActivity.this, new String[]{Manifest.permission.CAMERA}, Constants.REQUEST_CAMERA);

                    Log.d(LOG_TAG, "Camera permission requested");
                } else {
                    if (mCardForm.isCardScanningAvailable()) {
                        mCardForm.scanCard(PaymentActivity.this);
                        Log.d(LOG_TAG, "Scanning card");
                    }
                    break;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Camera permission granted");

                    mCardForm.scanCard(PaymentActivity.this);
                } else {
                    Log.d(LOG_TAG, "Camera permission denied");
                }
            }

        }
    }

    public void attemptPayment(View view) {
        if (mCardForm.isValid()) {
            Intent bookingConfirmIntent = new Intent(PaymentActivity.this, BookingConfirmationActivity.class);
            startActivity(bookingConfirmIntent);
        } else {
            mCardForm.validate();
            Toast.makeText(this, "Payment form is invalid", Toast.LENGTH_SHORT).show();
        }

    }

}
