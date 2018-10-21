package com.mad.sparkle.view;

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
import com.google.maps.android.SphericalUtil;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.model.nearbysearch.NearbySearchResponse;
import com.mad.sparkle.model.nearbysearch.Photo;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.service.GooglePlacesService;
import com.mad.sparkle.utils.RetrofitClient;
import com.mad.sparkle.viewmodel.MapViewModel;
import com.mad.sparkle.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.mad.sparkle.utils.Constants.DEFAULT_LOCATION_SYDNEY;
import static com.mad.sparkle.utils.Constants.DEFAULT_ZOOM;
import static com.mad.sparkle.utils.Constants.LOG_TAG;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private MapViewModel mViewModel;

    private boolean mLocationPermissionGranted;
    private LatLng mLastKnownLocation;

    private List<Store> mStoreList = new ArrayList<Store>();

    private DatabaseReference mDatabaseRef;

//    private Map<String, String> mMarkerMap = new HashMap<>();

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES);

        getLocationPermission();
    }

    private void startMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        // TODO: Use the ViewModel

        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (mLocationPermissionGranted) {
                    mMap.clear();
                    mStoreList.clear();

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Store store = dataSnapshot1.getValue(Store.class);
                        mStoreList.add(store);
                    }

                    // Sort ascending
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

//        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
//        mMap.setOnMyLocationClickListener(onMyLocationClickListener);

        mMap.getUiSettings().setZoomControlsEnabled(true);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mMap.setMyLocationEnabled(true);
        }

        showCurrentLocation();


        // Clicking on the info window snippet
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int position = (int) (marker.getTag());

                Intent storeDetailIntent = new Intent(getActivity(), StoreDetailActivity.class);

                storeDetailIntent.putExtra(Constants.NAME, mStoreList.get(position).getName());
                storeDetailIntent.putExtra(Constants.ADDRESS, mStoreList.get(position).getAddress());
                storeDetailIntent.putExtra(Constants.DISTANCE, mStoreList.get(position).getDistance());
                storeDetailIntent.putExtra(Constants.RATING, mStoreList.get(position).getRating());
                storeDetailIntent.putExtra(Constants.PHONE, mStoreList.get(position).getPhone());
                storeDetailIntent.putExtra(Constants.PHOTO_REFERENCE, mStoreList.get(position).getPhotoReference());

                startActivity(storeDetailIntent);
                Log.d(LOG_TAG, "Launching store detail activity");
            }
        });
    }

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

    private void showDefaultLocation() {
        LatLng currentLocationLatLng = DEFAULT_LOCATION_SYDNEY;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
        mLastKnownLocation = currentLocationLatLng;
    }

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

//    private String getPlaceDetailUrl(String placeId) {
//        StringBuilder placeDetailStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
//        placeDetailStringBuilder.append("placeid=" + placeId);
//        placeDetailStringBuilder.append("&fields=" + "formatted_phone_number");
//        placeDetailStringBuilder.append("&key=" + getString(R.string.google_maps_key));
//
//        Log.d(LOG_TAG, "url= " + placeDetailStringBuilder.toString());
//        return placeDetailStringBuilder.toString();
//    }

//    private GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
//        @Override
//        public boolean onMarkerClick(Marker marker) {
//            Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
//            return false;
//        }
//    };

    //    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
//            new GoogleMap.OnMyLocationButtonClickListener() {
//                @Override
//                public boolean onMyLocationButtonClick() {
//                    // Add a marker in Sydney and move the camera
//                    LatLng sydney = new LatLng(-33.885504, 151.158329);
//                    mMap.addMarker(new MarkerOptions().position(sydney).title("Magic Wash"));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, DEFAULT_ZOOM));

//                    // Add a marker in Google HQ and move the camera
//
//                    LatLng latLng = new LatLng(37.422, -122.084);
//
//                    MarkerOptions googleHq = new MarkerOptions();
//                    googleHq.position(latLng);
//                    googleHq.title("user Current Location");
//                    googleHq.snippet("some text");
//                    googleHq.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//
//                    mMap.addMarker(googleHq);
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM));
//                    return false;
//                }
//            };

//    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
//            new GoogleMap.OnMyLocationClickListener() {
//                @Override
//                public void onMyLocationClick(@NonNull Location location) {
//
//                    CircleOptions circleOptions = new CircleOptions();
//                    circleOptions.center(new LatLng(location.getLatitude(),
//                            location.getLongitude()));
//
//                    circleOptions.radius(200);
//                    circleOptions.fillColor(Color.RED);
//                    circleOptions.strokeWidth(6);
//
//                    mMap.addCircle(circleOptions);
//                }
//            };
}
