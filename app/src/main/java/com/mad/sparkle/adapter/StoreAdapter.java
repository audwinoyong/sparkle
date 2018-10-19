package com.mad.sparkle.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mad.sparkle.R;
import com.mad.sparkle.model.Store;

import java.util.ArrayList;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    // Initial fields
    private Context mContext;
    private ArrayList<Store> mStores;

    /**
     * Private inner class to add fields.
     */
    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView mStoreImg;
        private TextView mNameTv, mAddressTv, mDistanceTv;
        private RatingBar mRatingBar;
        private RelativeLayout mStoreRelativeLayout;

        /**
         * This class holds all the GUI elements that we use in one row.
         *
         * @param view The custom view
         */
        public ViewHolder(View view) {
            super(view);

            mStoreImg = (ImageView) view.findViewById(R.id.item_store_image);
            mNameTv = (TextView) view.findViewById(R.id.item_store_name_tv);
            mAddressTv = (TextView) view.findViewById(R.id.item_store_address_tv);
            mRatingBar = (RatingBar) view.findViewById(R.id.item_store_ratingBar);
            mDistanceTv = (TextView) view.findViewById(R.id.item_store_distance_tv);

            mStoreRelativeLayout = (RelativeLayout) view.findViewById(R.id.store_item_store_rl);
        }
    }

    /**
     * Constructor filling the adapter with train data from the trains list.
     *
     * @param context Interface to global information about an application environment
     * @param stores  ArrayList of Train objects
     */
    public StoreAdapter(Context context, ArrayList<Store> stores) {
        mContext = context;
        mStores = stores;
    }

    /**
     * Inflates the xml layout file.
     *
     * @param parent   The view parent
     * @param viewType Type of the view
     * @return View inflated with store items
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext)
                .inflate(R.layout.item_store, parent, false);

        return new ViewHolder(itemView);
    }

    /**
     * Initialise and assign values for each row in the RecyclerView.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position Position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Store store = mStores.get(position);

        holder.mNameTv.setText(store.getName());
        holder.mAddressTv.setText(store.getAddress());
        holder.mRatingBar.setRating((float) store.getRating());
        holder.mDistanceTv.setText(store.getDistance());

        // OnClickListener to refresh the LinearLayout view.
        holder.mStoreRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * Get the number of items in the train list.
     *
     * @return The size of the train list
     */
    @Override
    public int getItemCount() {
        return mStores.size();
    }
}