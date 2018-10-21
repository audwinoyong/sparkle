package com.mad.sparkle.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.sparkle.R;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;
import com.squareup.picasso.Picasso;

import static com.mad.sparkle.utils.Constants.CALL_PREFIX;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class StoreDetailActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    private ImageView mStoreImg;
    private TextView mAddressTv;
    private TextView mDistanceTv;
    private RatingBar mRatingBar;
    private TextView mPhoneTv;

    private String mStoreId;
    private String mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mStoreId = getIntent().getStringExtra(Constants.STORE_ID);

        mStoreImg = findViewById(R.id.activity_store_detail_img);
        mAddressTv = findViewById(R.id.activity_store_detail_address_tv);
        mDistanceTv = findViewById(R.id.activity_store_detail_distance_tv);
        mRatingBar = findViewById(R.id.activity_store_detail_ratingBar);
        mPhoneTv = findViewById(R.id.activity_store_detail_phone_tv);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES).child(mStoreId);

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);

                toolbar.setTitle(store.getName());

                mAddressTv.setText(store.getAddress());
                mDistanceTv.setText(String.format("%s m", String.valueOf(store.getDistance())));
                mRatingBar.setRating((float) store.getRating());

                if (!TextUtils.isEmpty(store.getPhone())) {
                    mPhone = store.getPhone();
                    mPhoneTv.setText(formatPhoneNumber(store.getPhone()));
                } else {
                    mPhoneTv.setText(getString(R.string.no_phone_available));
                }

                if (!TextUtils.isEmpty(store.getPhotoReference())) {
                    Log.d(LOG_TAG, "Fetching place photo for: " + store.name);
                    Picasso.get().load(getPhotoUrl(store.getPhotoReference())).placeholder(R.drawable.app_logo).into(mStoreImg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check call phone permission
                if (ContextCompat.checkSelfPermission(StoreDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StoreDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constants.REQUEST_CALL_PHONE);

                    Log.d(LOG_TAG, "Call phone permission requested");
                } else {
                    // If granted, execute the call
                    makePhoneCall();
                    Log.d(LOG_TAG, "Call phone permission granted");
                }
            }
        });

        Button makeBookingBtn = (Button) findViewById(R.id.activity_store_detail_book_button);
        makeBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingSelectionIntent = new Intent(StoreDetailActivity.this, BookingSelectionActivity.class);

                bookingSelectionIntent.putExtra(Constants.STORE_ID, mStoreId);
                startActivity(bookingSelectionIntent);

                Log.d(LOG_TAG, "Launching booking selection activity");
            }
        });
    }

    /**
     * Format phone number
     *
     * @param phone unformatted phone number
     * @return formatted phone number
     */
    private String formatPhoneNumber(String phone) {
        return String.format("(%s) %s %s", phone.substring(0, 2), phone.substring(2, 6), phone.substring(6, 10));
    }

    private void makePhoneCall() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(CALL_PREFIX + mPhone));
        startActivity(callIntent);
    }

    private String getPhotoUrl(String photoReference) {
        StringBuilder photoUrlStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
        photoUrlStringBuilder.append("maxwidth=" + "400");
        photoUrlStringBuilder.append("&photoreference=" + photoReference);
        photoUrlStringBuilder.append("&key=" + getString(R.string.google_maps_key));

        Log.d(LOG_TAG, "url= " + photoUrlStringBuilder.toString());
        return photoUrlStringBuilder.toString();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_CALL_PHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Call phone permission granted");
                    makePhoneCall();
                } else {
                    Log.d(LOG_TAG, "Call phone permission denied");
                }
            }

        }
    }
}
