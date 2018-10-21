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

/**
 * Generates views for the bookings on demand, as the user scrolls through the items.
 */
public class BookingRecyclerViewAdapter extends RecyclerView.Adapter<BookingRecyclerViewAdapter.ViewHolder> {

    // Initial fields
    private Context mContext;
    private List<Booking> mBookings;

    /**
     * Constructor filling the adapter with booking data from the bookings list.
     *
     * @param context  Interface to global information about an application environment
     * @param bookings List of bookings
     */
    public BookingRecyclerViewAdapter(Context context, List<Booking> bookings) {
        mContext = context;
        mBookings = bookings;
    }

    /**
     * Inflates the xml layout file.
     *
     * @param parent   The view parent
     * @param viewType Type of the view
     * @return View inflated with train items
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.fragment_booking, parent, false);
        return new ViewHolder(itemView);
    }

    /**
     * Initialise and assign values for each row in the RecyclerView.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position Position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Booking booking = mBookings.get(position);

        holder.mStoreNameTv.setText(booking.getStoreName());
        holder.mDateTv.setText(booking.getDate());
        holder.mTimeTv.setText(booking.getTime());
    }

    /**
     * Get the number of items in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getItemCount() {
        return mBookings.size();
    }

    /**
     * ViewHolder inner class to add fields.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mStoreNameTv, mDateTv, mTimeTv;

        /**
         * This class holds all the GUI elements that is used in one row.
         *
         * @param view The custom view
         */
        public ViewHolder(View view) {
            super(view);

            mStoreNameTv = (TextView) view.findViewById(R.id.fragment_booking_store_name_tv);
            mDateTv = (TextView) view.findViewById(R.id.fragment_booking_date_tv);
            mTimeTv = (TextView) view.findViewById(R.id.fragment_booking_time_tv);
        }
    }

}

