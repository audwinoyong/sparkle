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

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;
import com.squareup.picasso.Picasso;

import static com.mad.sparkle.utils.Constants.CALL_PREFIX;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class StoreDetailActivity extends AppCompatActivity {

    private ImageView mStoreImg;
    private TextView mAddressTv;
    private TextView mDistanceTv;
    private RatingBar mRatingBar;
    private TextView mPhoneTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String name = getIntent().getStringExtra(Constants.NAME);
        this.setTitle(name);

        mStoreImg = findViewById(R.id.activity_store_detail_img);
        mAddressTv = findViewById(R.id.activity_store_detail_address_tv);
        mDistanceTv = findViewById(R.id.activity_store_detail_distance_tv);
        mRatingBar = findViewById(R.id.activity_store_detail_ratingBar);
        mPhoneTv = findViewById(R.id.activity_store_detail_phone_tv);

        String photoReference = getIntent().getStringExtra(Constants.PHOTO_REFERENCE);
        int distance = getIntent().getIntExtra(Constants.DISTANCE, 0);
        float rating = (float) getIntent().getDoubleExtra(Constants.RATING, 0);
        String phone = getIntent().getStringExtra(Constants.PHONE);

        mAddressTv.setText(getIntent().getStringExtra(Constants.ADDRESS));
        mDistanceTv.setText(String.format("%s m", String.valueOf(distance)));
        mRatingBar.setRating(rating);
        mPhoneTv.setText(formatPhoneNumber(phone));

        if (!TextUtils.isEmpty(photoReference)) {
            Log.d(LOG_TAG, "Fetching place photo for: " + name);
            Picasso.get().load(getPhotoUrl(photoReference)).placeholder(R.drawable.app_logo).into(mStoreImg);

//            Picasso.get().load("https://image.freepik.com/free-vector/car-wash-cartoon-vector_23-2147498053.jpg").placeholder(R.drawable.app_logo).into(mStoreImg);
        }

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
                startActivity(bookingSelectionIntent);

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
        String phone = getIntent().getStringExtra(Constants.PHONE);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(CALL_PREFIX + phone));
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
