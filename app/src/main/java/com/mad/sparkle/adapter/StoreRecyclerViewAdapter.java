package com.mad.sparkle.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.view.fragment.StoreListFragment.OnListFragmentInteractionListener;
import com.mad.sparkle.model.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Generates views for the bookings on demand, as the user scrolls through the items.
 * Makes a call to the specified OnListFragmentInteractionListener.
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {

    // Initial fields
    private Context mContext;
    private final List<Store> mStores;
    private final OnListFragmentInteractionListener mListener;

    /**
     * Constructor filling the adapter with store data from the stores list.
     *
     * @param context  The context
     * @param stores   List of stores
     * @param listener The listener
     */

    public StoreRecyclerViewAdapter(Context context, List<Store> stores, OnListFragmentInteractionListener listener) {
        mContext = context;
        mStores = stores;
        mListener = listener;
    }

    /**
     * Inflates the xml layout file.
     *
     * @param parent   The view parent
     * @param viewType Type of the view
     * @return View inflated with train items
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_store, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Initialise and assign values for each row in the RecyclerView.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position Position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mStore = mStores.get(position);

        String photoReference = holder.mStore.getPhotoReference();
        if (!TextUtils.isEmpty(photoReference)) {
            Log.d(LOG_TAG, "Fetching place photo for: " + holder.mStore.getName());
            Picasso.get().load(getPhotoUrl(photoReference, holder)).placeholder(R.drawable.app_logo).into(holder.mStoreImg);
        }

        holder.mNameTv.setText(holder.mStore.getName());
        holder.mAddressTv.setText(holder.mStore.getAddress());
        holder.mRatingBar.setRating((float) holder.mStore.getRating());
        holder.mDistanceTv.setText(mContext.getResources().getString(R.string.distance_format, holder.mStore.getDistance()));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mStore);
                }
            }
        });
    }

    /**
     * Get the number of items in the list.
     *
     * @return The size of the list
     */
    @Override
    public int getItemCount() {
        return mStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final View mView;
        private ImageView mStoreImg;
        private TextView mNameTv, mAddressTv, mDistanceTv;
        private RatingBar mRatingBar;
        private Store mStore;

        /**
         * This class holds all the GUI elements that is used in one row.
         *
         * @param view The custom view
         */
        public ViewHolder(View view) {
            super(view);
            mView = view;

            mStoreImg = (ImageView) view.findViewById(R.id.fragment_store_image);
            mNameTv = (TextView) view.findViewById(R.id.fragment_store_name_tv);
            mAddressTv = (TextView) view.findViewById(R.id.fragment_store_address_tv);
            mRatingBar = (RatingBar) view.findViewById(R.id.fragment_store_ratingBar);
            mDistanceTv = (TextView) view.findViewById(R.id.fragment_store_distance_tv);
        }

    }

    /**
     * Build the url for getting the store photo from the photo reference.
     *
     * @param photoReference The photo reference
     * @param holder         The viewholder
     * @return The url of the photo
     */
    private String getPhotoUrl(String photoReference, ViewHolder holder) {
        StringBuilder photoUrlStringBuilder = new StringBuilder(Constants.PHOTO_BASE_URL);
        photoUrlStringBuilder.append(Constants.PHOTO_MAX_WIDTH);
        photoUrlStringBuilder.append(Constants.PHOTO_REFERENCE_URL + photoReference);
        photoUrlStringBuilder.append(Constants.PHOTO_KEY + holder.itemView.getResources().getString(R.string.google_maps_key));

        Log.d(LOG_TAG, "url= " + photoUrlStringBuilder.toString());
        return photoUrlStringBuilder.toString();
    }
}
