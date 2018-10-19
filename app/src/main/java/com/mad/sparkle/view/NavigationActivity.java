package com.mad.sparkle.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.sparkle.R;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * @author Audwin
 * Created on 14/09/18
 */
public class NavigationActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        StoreFragment.OnListFragmentInteractionListener {

    final Fragment mMapFragment = MapFragment.newInstance();
    final Fragment mStoreFragment = StoreFragment.newInstance();
    final Fragment mProfileFragment = ProfileFragment.newInstance("", "");
    final FragmentManager mFragmentManager = getSupportFragmentManager();

    private Fragment mActiveFragment = mStoreFragment;

    private FirebaseAuth mAuth;

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            if (firebaseUser == null) {
                Log.d(LOG_TAG, "No user is logged in, redirecting to PreLogin Activity");
                Intent intent = new Intent(NavigationActivity.this, PreLoginActivity.class);
                startActivity(intent);
            }
        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mMapFragment).commit();
                    mActiveFragment = mMapFragment;

//                    mFragmentManager.beginTransaction().hide(mActiveFragment).commit();

                    return true;
                case R.id.navigation_list:

                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mStoreFragment).commit();
                    mActiveFragment = mStoreFragment;
//                    mFragmentManager.beginTransaction().hide(mActiveFragment).commit();

                    return true;
                case R.id.navigation_profile:

                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mProfileFragment).commit();
                    mActiveFragment = mProfileFragment;
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        mAuth = FirebaseAuth.getInstance();

        mFragmentManager.beginTransaction().add(R.id.contentContainer, mMapFragment).hide(mMapFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mProfileFragment).hide(mProfileFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mStoreFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setSelectedItemId(R.id.navigation_list);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * This hook is called whenever an item in the options menu is selected.
     *
     * @param item The menu item that was selected.
     * @return false to allow normal menu processing to proceed.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_sign_out:
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onListFragmentInteraction(Store store) {
        Intent storeDetailIntent = new Intent(NavigationActivity.this, StoreDetailActivity.class);

        storeDetailIntent.putExtra(Constants.NAME, store.getName());
        storeDetailIntent.putExtra(Constants.ADDRESS, store.getAddress());
        storeDetailIntent.putExtra(Constants.DISTANCE, store.getDistance());
        storeDetailIntent.putExtra(Constants.RATING, store.getRating());
        storeDetailIntent.putExtra(Constants.PHONE, store.getPhone());

        startActivityForResult(storeDetailIntent, 1111);

        Log.d(LOG_TAG, "Launching store detail activity");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Navigation Activity onStart called");
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Navigation Activity onStop called");
        mAuth.removeAuthStateListener(authStateListener);
    }
}
