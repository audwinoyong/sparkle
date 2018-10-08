package com.mad.sparkle.view;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mad.sparkle.R;

public class StoreDetailActivity extends AppCompatActivity {

    public static final String NAME = "name";
    public static final String ADDRESS = "address";
    public static final String DISTANCE = "distance";
    public static final String RATING = "rating";

    private ImageView mStoreImg;
    private TextView mAddressTv;
    private TextView mDistanceTv;
    private RatingBar mRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        this.setTitle(getIntent().getStringExtra(NAME));

        mStoreImg = findViewById(R.id.activity_store_detail_img);
        mAddressTv = findViewById(R.id.activity_store_detail_address_tv);
        mDistanceTv = findViewById(R.id.activity_store_detail_distance_tv);
        mRatingBar = findViewById(R.id.activity_store_detail_ratingBar);

        int distance = getIntent().getIntExtra(DISTANCE, 0);
        int rating = getIntent().getIntExtra(RATING, 0);

        mAddressTv.setText(getIntent().getStringExtra(ADDRESS));
        mDistanceTv.setText(String.valueOf(distance) + "M");
        mRatingBar.setRating(rating);


        Button makeBookingBtn = (Button) findViewById(R.id.activity_store_detail_book_button);
        makeBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
