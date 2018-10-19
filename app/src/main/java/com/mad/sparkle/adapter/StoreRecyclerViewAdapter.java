package com.mad.sparkle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mad.sparkle.R;
import com.mad.sparkle.view.StoreListFragment.OnListFragmentInteractionListener;
import com.mad.sparkle.dummy.DummyContent.DummyItem;
import com.mad.sparkle.model.Store;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class StoreRecyclerViewAdapter extends RecyclerView.Adapter<StoreRecyclerViewAdapter.ViewHolder> {

//    private final List<DummyItem> mValues;
    private final List<Store> mStores;
    private final OnListFragmentInteractionListener mListener;

//    public StoreRecyclerViewAdapter(List<DummyItem> items, OnListFragmentInteractionListener listener) {
//        mValues = items;
//        mListener = listener;
//    }

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
//        holder.mItem = mValues.get(position);
        holder.mStore = mStores.get(position);

        holder.mNameTv.setText(holder.mStore.getName());
        holder.mAddressTv.setText(holder.mStore.getAddress());
        holder.mRatingBar.setRating((float) holder.mStore.getRating());
        holder.mDistanceTv.setText(String.valueOf(holder.mStore.getDistance()) + " m");


//        holder.mIdView.setText(mValues.get(position).id);
//        holder.mContentView.setText(mValues.get(position).content);

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
//        public final TextView mIdView;
//        public final TextView mContentView;
        private ImageView mStoreImg;
        private TextView mNameTv, mAddressTv, mDistanceTv;
        private RatingBar mRatingBar;
//        public DummyItem mItem;
        public Store mStore;

        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.item_number);
//            mContentView = (TextView) view.findViewById(R.id.content);

            mStoreImg = (ImageView) view.findViewById(R.id.fragment_store_image);
            mNameTv = (TextView) view.findViewById(R.id.fragment_store_name_tv);
            mAddressTv = (TextView) view.findViewById(R.id.fragment_store_address_tv);
            mRatingBar = (RatingBar) view.findViewById(R.id.fragment_store_ratingBar);
            mDistanceTv = (TextView) view.findViewById(R.id.fragment_store_distance_tv);

        }

//        @Override
//        public String toString() {
//            return super.toString() + " '" + mContentView.getText() + "'";
//        }
    }
}
