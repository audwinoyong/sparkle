package com.mad.sparkle.view;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.sparkle.R;
import com.mad.sparkle.adapter.BookingRecyclerViewAdapter;
import com.mad.sparkle.model.Booking;
import com.mad.sparkle.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class BookingHistoryActivity extends AppCompatActivity {

    // Initial widget fields
    private RecyclerView mBookingRecyclerView;
    private BookingRecyclerViewAdapter mBookingRecyclerViewAdapter;

    private List<Booking> mBookingList = new ArrayList<Booking>();

    private DatabaseReference mDatabaseRef;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_history);

        prepareBookingAdapter();

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.BOOKINGS).child(mUser.getUid());

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Booking booking = childDataSnapshot.getValue(Booking.class);
                    mBookingList.add(booking);
                }
                mBookingRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    private void prepareBookingAdapter() {
        mBookingRecyclerView = (RecyclerView) findViewById(R.id.activity_booking_history_recycler_view);
        mBookingRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mBookingRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        mBookingRecyclerViewAdapter = new BookingRecyclerViewAdapter(this, mBookingList);
        mBookingRecyclerView.setAdapter(mBookingRecyclerViewAdapter);
    }

}