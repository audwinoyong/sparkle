package com.mad.sparkle.view;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;

public class StoreDetailActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String DISTANCE = "distance";
    public static final String RATING = "rating";
    public static final String PHONE = "phone";

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

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        this.setTitle(getIntent().getStringExtra(NAME));

        mStoreImg = findViewById(R.id.activity_store_detail_img);
        mAddressTv = findViewById(R.id.activity_store_detail_address_tv);
        mDistanceTv = findViewById(R.id.activity_store_detail_distance_tv);
        mRatingBar = findViewById(R.id.activity_store_detail_ratingBar);
        mPhoneTv = findViewById(R.id.activity_store_detail_phone_tv);

        int distance = getIntent().getIntExtra(DISTANCE, 0);
        int rating = getIntent().getIntExtra(RATING, 0);
        String number = getIntent().getStringExtra(PHONE);

        mAddressTv.setText(getIntent().getStringExtra(ADDRESS));
        mDistanceTv.setText(String.valueOf(distance) + "M");
        mRatingBar.setRating(rating);
        mPhoneTv.setText(number);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check call phone permission
                if (ContextCompat.checkSelfPermission(StoreDetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(StoreDetailActivity.this, new String[]{Manifest.permission.CALL_PHONE}, Constants.REQUEST_CALL_PHONE);

                    Log.d("DEBUG", "Call phone permission requested");
                } else {
                    // If granted, execute the call
                    String number = getIntent().getStringExtra(PHONE);

                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + number));
                    startActivity(callIntent);

                    Log.d("DEBUG", "Call phone permission granted");
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
}
