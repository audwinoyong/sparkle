package com.mad.sparkle.adapter;

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
import com.mad.sparkle.view.StoreListFragment.OnListFragmentInteractionListener;
import com.mad.sparkle.model.Store;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * {@link RecyclerView.Adapter} that can display a and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {

    private final List<Store> mStores;
    private final OnListFragmentInteractionListener mListener;

    public StoreRecyclerViewAdapter(List<Store> stores, OnListFragmentInteractionListener listener) {
        mStores = stores;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_store, parent, false);
        return new ViewHolder(view);
    }

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
        holder.mDistanceTv.setText(String.valueOf(holder.mStore.getDistance()) + " m");

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

    @Override
    public int getItemCount() {
        return mStores.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        private ImageView mStoreImg;
        private TextView mNameTv, mAddressTv, mDistanceTv;
        private RatingBar mRatingBar;
        public Store mStore;

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

    private String getPhotoUrl(String photoReference, ViewHolder holder) {
        StringBuilder photoUrlStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/photo?");
        photoUrlStringBuilder.append("maxwidth=" + "400");
        photoUrlStringBuilder.append("&photoreference=" + photoReference);
        photoUrlStringBuilder.append("&key=" + holder.itemView.getResources().getString(R.string.google_maps_key));

        Log.d(LOG_TAG, "url= " + photoUrlStringBuilder.toString());
        return photoUrlStringBuilder.toString();
    }
}
