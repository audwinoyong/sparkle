package com.mad.sparkle.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.sparkle.R;
import com.mad.sparkle.adapter.StoreRecyclerViewAdapter;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Fragment that shows the list of car wash stores.
 */
public class StoreListFragment extends Fragment {

    private DatabaseReference mDatabaseRef;

    private RecyclerView mRecyclerView;
    private StoreRecyclerViewAdapter mStoreRecyclerViewAdapter;

    private List<Store> mStoreList = new ArrayList<Store>();

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreListFragment() {
    }

    /**
     * Create new instance of the store list fragment
     *
     * @return StoreListFragment instance
     */
    public static StoreListFragment newInstance() {
        return new StoreListFragment();
    }

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflate the view of the fragment
     *
     * @param inflater           layout inflater
     * @param container          viewgroup container
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     * @return the inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                // Clear the list
                mStoreList.clear();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Store store = childDataSnapshot.getValue(Store.class);
                    mStoreList.add(store);
                }

                // Sort the store ascending from the nearest distance
                Collections.sort(mStoreList, new Comparator<Store>() {
                    @Override
                    public int compare(Store o1, Store o2) {
                        return o1.distance - o2.distance;
                    }
                });

                // Notify the adapter for changes
                mStoreRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));

            mStoreRecyclerViewAdapter = new StoreRecyclerViewAdapter(context, mStoreList, mListener);
            mRecyclerView.setAdapter(mStoreRecyclerViewAdapter);
        }
        return view;
    }


    /**
     * Attach the onListFragmentInteractionListener.
     *
     * @param context the context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + getString(R.string.must_implement_on_list_interaction_listener));
        }
    }

    /**
     * Detach the listener.
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that activity.
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Store store);
    }
}
