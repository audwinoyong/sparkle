package com.mad.sparkle.view;

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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StoreListFragment extends Fragment {

    // TODO: Customize parameter argument names
//    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
//    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private StoreRecyclerViewAdapter mStoreRecyclerViewAdapter;

    private List<Store> mStoreList = new ArrayList<Store>();

    private DatabaseReference mDatabaseRef;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoreListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StoreListFragment newInstance() {
        StoreListFragment fragment = new StoreListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_store_list, container, false);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mStoreList.clear();

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Store store = childDataSnapshot.getValue(Store.class);
                    mStoreList.add(store);
                }

                // Sort ascending
                Collections.sort(mStoreList, new Comparator<Store>() {
                    @Override
                    public int compare(Store o1, Store o2) {
                        return o1.distance - o2.distance;
//                        return Double.compare(o1.rating, o2.rating); // ascending
//                        return o1.distance - o2.distance; // descending
//                        return Double.compare(o2.rating, o1.rating); // descending
                    }
                });

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Store store);
    }
}
