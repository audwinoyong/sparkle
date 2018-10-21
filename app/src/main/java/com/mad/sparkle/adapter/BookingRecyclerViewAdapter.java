package com.mad.sparkle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mad.sparkle.R;
import com.mad.sparkle.model.Booking;

import java.util.List;

public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Booking> mBookings;

    public BookingRecyclerViewAdapter(Context context, List<Booking> bookings) {
        mContext = context;
        mBookings = bookings;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_booking, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Booking booking = mBookings.get(position);

        holder.mStoreNameTv.setText(booking.getStoreName());
        holder.mDateTv.setText(booking.getDate());
        holder.mTimeTv.setText(booking.getTime());
    }

    @Override
    public int getItemCount() {
        return mBookings.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mStoreNameTv, mDateTv, mTimeTv;

        public ViewHolder(View view) {
            super(view);

            mStoreNameTv = (TextView) view.findViewById(R.id.fragment_booking_store_name_tv);
            mDateTv = (TextView) view.findViewById(R.id.fragment_booking_date_tv);
            mTimeTv = (TextView) view.findViewById(R.id.fragment_booking_time_tv);

        }
    }

}

