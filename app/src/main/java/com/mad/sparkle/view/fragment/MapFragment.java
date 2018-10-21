package com.mad.sparkle.view.fragment;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.view.activity.StoreDetailActivity;
import com.mad.sparkle.viewmodel.MapViewModel;
import com.mad.sparkle.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.mad.sparkle.utils.Constants.DEFAULT_LOCATION_SYDNEY;
import static com.mad.sparkle.utils.Constants.DEFAULT_ZOOM;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Fragment that shows the map for user to locate the stores.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel mViewModel;

    private DatabaseReference mDatabaseRef;

    private boolean mLocationPermissionGranted;
    private LatLng mLastKnownLocation;

    private List<Store> mStoreList = new ArrayList<Store>();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MapFragment() {
    }

    /**
     * Create new instance of the map fragment
     *
     * @return MapFragment instance
     */
    public static MapFragment newInstance() {
        return new MapFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    /**
     * Called after the view is created
     *
     * @param view               the view
     * @param savedInstanceState bundle object
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES);
        getLocationPermission();
    }

    /**
     * Starts the map.
     */
    private void startMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Called when the activity is created.
     * Add the markers of the store locations into the map from the database.
     *
     * @param savedInstanceState Bundle object
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mLocationPermissionGranted) {

                    // Clear markers and the list
                    mMap.clear();
                    mStoreList.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Store store = dataSnapshot1.getValue(Store.class);
                        mStoreList.add(store);
                    }

                    // Sort the store ascending from the nearest distance
                    Collections.sort(mStoreList, new Comparator<Store>() {
                        @Override
                        public int compare(Store o1, Store o2) {
                            return o1.distance - o2.distance;
                        }
                    });

                    // Check if the fragment is currently added to its activity
                    if (isAdded()) {
                        // Add customised markers
                        for (int i = 0; i < mStoreList.size(); i++) {
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(new LatLng(mStoreList.get(i).latitude, mStoreList.get(i).longitude));
                            markerOptions.title(mStoreList.get(i).name);
                            markerOptions.snippet(mStoreList.get(i).address + " | " + mStoreList.get(i).rating + getString(R.string.unicode_character_star));
                            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                            Marker marker = mMap.addMarker(markerOptions);
                            marker.setTag(i);
                        }
                    }

                    if (mLastKnownLocation != null) {
                        // For zooming automatically to the location of the marker
                        CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastKnownLocation).zoom(DEFAULT_ZOOM).build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set the zoom controls
        mMap.getUiSettings().setZoomControlsEnabled(true);

        // Enable my location button if location permission is granted
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }

        showCurrentLocation();

        // Set the onClick listener when clicking on the info window snippet of the marker, start the store detail activity
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int position = (int) (marker.getTag());

                Intent storeDetailIntent = new Intent(getActivity(), StoreDetailActivity.class);
                storeDetailIntent.putExtra(Constants.STORE_ID, mStoreList.get(position).getStoreId());

                startActivity(storeDetailIntent);
                Log.d(LOG_TAG, "Launching store detail activity");
            }
        });
    }

    /**
     * Request the location permission.
     */
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
            Log.d(LOG_TAG, "Location permission requested");
        } else {
            mLocationPermissionGranted = true;
            startMap();
        }
    }

    /**
     * Show the current location of the device using GPS and other sensors.
     */
    private void showCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if (mLocationPermissionGranted) {
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                // Move the camera to the current location
                                LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
                                mLastKnownLocation = currentLocationLatLng;

                                Log.d(LOG_TAG, "Show current location successful");
                            } else {
                                showDefaultLocation();
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.unable_to_find_current_location), Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Show current location failed");
                        }
                    }
                });
            } else {
                Toast.makeText(getContext(), getString(R.string.permission_denied_default_location), Toast.LENGTH_SHORT).show();
                Log.d(LOG_TAG, "Location permission denied, using default location in Sydney");
                showDefaultLocation();
            }
        } catch (SecurityException e) {
            Log.d(LOG_TAG, e.getMessage());
        }
    }

    /**
     * Show the default location to Sydney if permission is not granted or current location is not available.
     */
    private void showDefaultLocation() {
        LatLng currentLocationLatLng = DEFAULT_LOCATION_SYDNEY;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
        mLastKnownLocation = currentLocationLatLng;
    }

    /**
     * Returns the location permission results, whether granted or not.
     *
     * @param requestCode  the request code
     * @param permissions  the permissions
     * @param grantResults the grant results
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(LOG_TAG, "Location permission granted");

                    mLocationPermissionGranted = true;
                    startMap();

                } else {
                    Log.d(LOG_TAG, "Location permission denied");

                    mLocationPermissionGranted = false;
                    startMap();
                }
            }

        }
    }

}
