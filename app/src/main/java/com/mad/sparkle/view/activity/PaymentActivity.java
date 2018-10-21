package com.mad.sparkle.view.activity;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.sparkle.R;
import com.mad.sparkle.model.Booking;
import com.mad.sparkle.utils.Constants;

import static com.mad.sparkle.utils.Constants.BOOKINGS;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Show the credit card payment form and option to use the camera scanner.
 */
public class PaymentActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mDatabaseRef;

    private CardForm mCardForm;

    private String mStoreId;
    private String mStoreName;
    private String mDate;
    private String mTime;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mStoreId = getIntent().getStringExtra(Constants.STORE_ID);
        mStoreName = getIntent().getStringExtra(Constants.STORE_NAME);
        mDate = getIntent().getStringExtra(Constants.DATE);
        mTime = getIntent().getStringExtra(Constants.TIME);

        // Set up the payment form
        mCardForm = findViewById(R.id.activity_payment_card_form);
        mCardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .maskCvv(true)
                .actionLabel("Pay Now")
                .cardholderName(CardForm.FIELD_REQUIRED)
                .setup(PaymentActivity.this);
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu the menu
     * @return whether the inflation is successful or not.
     */
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

    /**
     * Returns the camera permission result, whether granted or not.
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Camera permission granted");

                    // Start camera scanning
                    mCardForm.scanCard(PaymentActivity.this);
                } else {
                    Log.d(LOG_TAG, "Camera permission denied");
                }
            }

        }
    }

    /**
     * Attempt the payment process.
     * If successful, redirect to booking confirmation activity.
     *
     * @param view the view
     */
    public void attemptPayment(View view) {
        if (mCardForm.isValid()) {
            // Store the booking into the database
            mUser = FirebaseAuth.getInstance().getCurrentUser();
            mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(BOOKINGS).child(mUser.getUid()).push();

            Booking newBooking = new Booking(mStoreId, mStoreName, mDate, mTime);

            mDatabaseRef.setValue(newBooking).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d(LOG_TAG, "addBookingToDatabase:success");

                        // Start booking confirmation activity
                        Intent bookingConfirmIntent = new Intent(PaymentActivity.this, BookingConfirmationActivity.class);
                        startActivity(bookingConfirmIntent);
                        finish();
                    } else {
                        Log.d(LOG_TAG, "addBookingToDatabase:failure", task.getException());
                    }
                }
            });

        } else {
            mCardForm.validate();
            Toast.makeText(this, "Payment form is invalid", Toast.LENGTH_SHORT).show();
        }

    }

}
