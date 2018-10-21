package com.mad.sparkle.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.mad.sparkle.R;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.model.nearbysearch.NearbySearchResponse;
import com.mad.sparkle.model.nearbysearch.Photo;
import com.mad.sparkle.service.GooglePlacesService;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.utils.RetrofitClient;
import com.mad.sparkle.view.fragment.MapFragment;
import com.mad.sparkle.view.fragment.ProfileFragment;
import com.mad.sparkle.view.fragment.StoreListFragment;

import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mad.sparkle.utils.Constants.DEFAULT_LOCATION_SYDNEY;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * The home navigation activity that holds the three main fragments of the map, list and profile.
 */
public class NavigationActivity extends AppCompatActivity implements ProfileFragment.OnFragmentInteractionListener,
        StoreListFragment.OnListFragmentInteractionListener {

    private final Fragment mMapFragment = MapFragment.newInstance();
    private final Fragment mStoreListFragment = StoreListFragment.newInstance();
    private final Fragment mProfileFragment = ProfileFragment.newInstance();
    private final FragmentManager mFragmentManager = getSupportFragmentManager();

    private Fragment mActiveFragment = mMapFragment;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private LatLng mDefaultLocation = DEFAULT_LOCATION_SYDNEY;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_map:
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mMapFragment).commit();
                    mActiveFragment = mMapFragment;
                    return true;

                case R.id.navigation_list:
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mStoreListFragment).commit();
                    mActiveFragment = mStoreListFragment;
                    return true;

                case R.id.navigation_profile:
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(mProfileFragment).commit();
                    mActiveFragment = mProfileFragment;
                    return true;
            }
            return false;
        }
    };

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        // Check if the user is logged in, if not redirect to prelogin activity
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
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


        // Add the fragment instances to fragment manager
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mMapFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mProfileFragment).hide(mProfileFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.contentContainer, mStoreListFragment).hide(mStoreListFragment).commit();

        // Set the onNavigationItemSelected listener
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // Get the initial nearby car wash
        Toast.makeText(NavigationActivity.this, getString(R.string.getting_nearby_car_wash), Toast.LENGTH_SHORT).show();
        findNearbyPlaces();
    }

    /**
     * Initialize the contents of the Activity's standard options menu.
     *
     * @param menu the menu
     * @return whether inflation is successful or not
     */
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_refresh:
                new FindNearbyPlacesAsyncTask(item).execute();
                break;
            case R.id.action_sign_out:
                // Sign out the current user
                FirebaseAuth.getInstance().signOut();
                finish();
                Log.d(LOG_TAG, "User is signed out");
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Handle interactions between the fragments.
     *
     * @param uri the fragment unique key
     */
    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    /**
     * Handle the fragment list interaction.
     * Launch the store detail activity when user click on a row.
     *
     * @param store the store
     */
    @Override
    public void onListFragmentInteraction(Store store) {
        Intent storeDetailIntent = new Intent(NavigationActivity.this, StoreDetailActivity.class);

        storeDetailIntent.putExtra(Constants.STORE_ID, store.getStoreId());
        startActivity(storeDetailIntent);

        Log.d(LOG_TAG, "Launching store detail activity");
    }

    /**
     * Find the nearby car wash from the Google Places API Http request.
     */
    private void findNearbyPlaces() {
        Log.d(LOG_TAG, "Getting nearby car wash...");

        // Create an instance of Retrofit and make a request to Google Places API
        GooglePlacesService googlePlacesService = RetrofitClient.getClient().create(GooglePlacesService.class);
        Call<NearbySearchResponse> call = googlePlacesService
                .getNearbyPlaces(mDefaultLocation.latitude + "," + mDefaultLocation.longitude, getString(R.string.google_maps_key));

        // Execute the Http request
        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Google Places API request successful");

                    // Reaches the daily query limit
                    if (response.body().getStatus().equals(Constants.OVER_QUERY_LIMIT)) {
                        Log.d(LOG_TAG, "Google Places API reaches daily query limit");
                        Toast.makeText(NavigationActivity.this, getString(R.string.api_request_limit_reached), Toast.LENGTH_SHORT).show();
                    }

                    for (int i = 0; i < response.body().getResults().size(); i++) {
                        Double latitude = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                        Double longitude = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                        LatLng placeLatLng = new LatLng(latitude, longitude);

                        String placeId = response.body().getResults().get(i).getPlaceId();
                        String name = response.body().getResults().get(i).getName();
                        String address = response.body().getResults().get(i).getVicinity();
                        Double rating = response.body().getResults().get(i).getRating();

                        List<Photo> photos = response.body().getResults().get(i).getPhotos();
                        String photoReference = "";

                        if (!photos.isEmpty()) {
                            photoReference = photos.get(0).getPhotoReference();
                        }

                        // Calculate the nearest distance
                        int distance = (int) SphericalUtil.computeDistanceBetween(mDefaultLocation, placeLatLng);

                        // Generate 7 digits random phone number,
                        // because calling Google Places API for Place Details query will return OVER_QUERY_LIMIT and also require further payment charge.
                        int phoneDigits = new Random().nextInt(9000000) + 1000000;
                        String randPhone = Constants.PHONE_DIGIT_PREFIX + String.valueOf(phoneDigits);

                        Store newStore = new Store(placeId, name, address, distance, rating, randPhone, latitude, longitude, photoReference);

                        // Store car wash stores into Firebase Database
                        FirebaseDatabase.getInstance().getReference(Constants.STORES)
                                .child(placeId).setValue(newStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(LOG_TAG, "registerStoreToDatabase:success");
                                } else {
                                    Log.d(LOG_TAG, "registerStoreToDatabase:failure", task.getException());
                                }
                            }
                        });
                    }
                } else {
                    Log.d(LOG_TAG, "Google Places API request failed");
                }
            }

            @Override
            public void onFailure(@NonNull Call<NearbySearchResponse> call, @NonNull Throwable t) {
                Log.d(LOG_TAG, t.toString());
                t.printStackTrace();
            }
        });
    }

    /**
     * Animate the refresh action menu item when fetching new data.
     *
     * @param item the refresh menu item
     */
    public void animateRefresh(MenuItem item) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ImageView imageView = (ImageView) inflater.inflate(R.layout.action_refresh, null);

        Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
        rotation.setRepeatCount(Animation.INFINITE);
        imageView.startAnimation(rotation);

        MenuItemCompat.setActionView(item, imageView);
    }

    /**
     * Clear the animation of the refresh action menu item.
     *
     * @param item the refresh menu item
     */
    public void completeAnimateRefresh(MenuItem item) {
        item.getActionView().clearAnimation();
        item.setActionView(null);
    }

    /**
     * onStart lifecycle
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "Navigation Activity onStart called");
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * onStop lifecycle
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "Navigation Activity onStop called");
        mAuth.removeAuthStateListener(mAuthStateListener);
    }

    /**
     * AsyncTask to find the nearby car wash.
     * Animate the refresh menu item during the process.
     */
    private class FindNearbyPlacesAsyncTask extends AsyncTask<Void, Void, Void> {
        private MenuItem mRefreshItem;

        /**
         * Constructor to set the private local field.
         *
         * @param refreshItem the refresh menu item
         */
        public FindNearbyPlacesAsyncTask(MenuItem refreshItem) {
            mRefreshItem = refreshItem;
        }

        /**
         * Animate the refresh menu item and show a Toast message
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(LOG_TAG, "Refreshing nearby car wash...");
            animateRefresh(mRefreshItem);
            Toast.makeText(NavigationActivity.this, getString(R.string.getting_nearby_car_wash), Toast.LENGTH_SHORT).show();
        }

        /**
         * Find the nearby car wash.
         *
         * @param voids void
         * @return null
         */
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                // Sleep for 2 second
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            findNearbyPlaces();
            return null;
        }

        /**
         * Remove the refresh menu item animation.
         *
         * @param aVoid void
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            completeAnimateRefresh(mRefreshItem);
            Log.d(LOG_TAG, "Refreshing nearby car wash complete");
        }
    }
}
