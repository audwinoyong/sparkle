package com.mad.sparkle.view;

import android.Manifest;
import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.viewmodel.MapViewModel;
import com.mad.sparkle.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private MapViewModel mViewModel;

    private boolean mLocationPermissionGranted;
    public static final int DEFAULT_ZOOM = 14;
    private LatLng mDefaultLocation = new LatLng(-33.8840504, 151.1992254);
    private LatLng mLastKnownLocation;
    private StringBuilder mPlaceQueryStringBuilder;
    private JSONObject mJsonPlaceList;

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

        getLocationPermission();
    }

    private void startMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        CircleImageView storeButton = (CircleImageView) getView().findViewById(R.id.fragment_map_store_button);
        storeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Getting nearby car wash...", Toast.LENGTH_SHORT).show();

                createPlaceQuery();

                new getNearbyPlacesAsyncTask(mMap, mPlaceQueryStringBuilder.toString()).execute();

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastKnownLocation).zoom(DEFAULT_ZOOM).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
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

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
            Log.d("DEBUG", "Location permission requested");
        } else {
            mLocationPermissionGranted = true;
            startMap();
        }
    }

    private void showCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        try {
            if (mLocationPermissionGranted) {
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                LatLng currentLocationLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
                                mLastKnownLocation = currentLocationLatLng;

                                Log.d("DEBUG", "Show current location is successful");
                            } else {
                                LatLng currentLocationLatLng = mDefaultLocation;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
                                mLastKnownLocation = currentLocationLatLng;
                            }
                        } else {
                            Toast.makeText(getContext(), R.string.unable_to_find_current_location, Toast.LENGTH_SHORT).show();
                            Log.d("DEBUG", "Show current location failed");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("EXCEPTION", e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.REQUEST_LOCATION_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    Log.d("DEBUG", "Location permission is granted");
                    startMap();

                } else {
                    mLocationPermissionGranted = false;
                    Log.d("DEBUG", "Location permission is not granted");
                    startMap();
                }
            }

        }
    }

    private void createPlaceQuery() {
        mPlaceQueryStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        mPlaceQueryStringBuilder.append("location=" + mLastKnownLocation.latitude + "," + mLastKnownLocation.longitude);
        mPlaceQueryStringBuilder.append("&rankby=" + "distance");
        mPlaceQueryStringBuilder.append("&keyword=" + "car+wash");
        mPlaceQueryStringBuilder.append("&key=" + getResources().getString(R.string.google_maps_key));

        Log.d("DEBUG", "url= " + mPlaceQueryStringBuilder.toString());
    }

    private void updateMap() {
        try {
            if (mJsonPlaceList == null) {
                Log.e("ERROR", "Failed to get a list of places");
            } else {
                JSONArray placeList = (JSONArray) mJsonPlaceList.get("results");
                for (int i = 0; i < placeList.length(); i++) {
                    JSONObject place = (JSONObject) placeList.get(i);

                    double latitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                    double longitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");

                    String placeId = place.getString("place_id");
                    String name = place.getString("name");
                    String address = place.getString("vicinity");
                    int rating = place.getInt("rating");

                    // Get photo reference
                    if (place.has("photos")) {
                        JSONArray photos = place.getJSONArray("photos");
                        String photoReference;

                        JSONObject photoDetail = (JSONObject) photos.get(0);
                        photoReference = photoDetail.getString("photo_reference");
                        Log.d("DEBUG", "Photo Ref: " + photoReference);

                    }

//                    int randDistance = new Random().nextInt(20) + 1;


                    Store newStore = new Store(name, address, 100, rating, "+612123456789");

                    // Store car wash stores into Firebase Database
                    FirebaseDatabase.getInstance().getReference(Constants.STORES)
                            .child(placeId).setValue(newStore).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("DEBUG", "registerStoreToDatabase:success");
                            } else {
                                Log.w("DEBUG", "registerStoreToDatabase:failure", task.getException());
                            }
                        }
                    });

                    // Add customised markers
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position((new LatLng(latitude, longitude)));
                    markerOptions.title(name);
                    markerOptions.snippet(address + " | " + rating + " Star Rating");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    mMap.addMarker(markerOptions);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getNearbyPlacesAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private GoogleMap mGoogleMap;
        private String mPlaceQuery;
        private String mData;
        private BufferedReader mBufferedReader;

        public getNearbyPlacesAsyncTask(GoogleMap googleMap, String placeQuery) {
            mGoogleMap = googleMap;
            mPlaceQuery = placeQuery;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                Log.d("DEBUG", "Getting nearby car wash...");

                URL url = new URL(mPlaceQuery);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                StringBuilder stringBuilder = new StringBuilder();

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                mData = stringBuilder.toString();
                Log.d("DEBUG", mData);
                bufferedReader.close();

                return true;

            } catch (MalformedURLException e) {
                Log.e("ERROR", e.getMessage());
                return false;

            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            try {
                StringReader stringReader = new StringReader(mData);

                mBufferedReader = new BufferedReader(stringReader);

                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = mBufferedReader.readLine()) != null) {
                    stringBuilder.append(line + "\n");
                }

                mJsonPlaceList = new JSONObject(stringBuilder.toString());

                updateMap();

            } catch (IOException e) {
                Log.e("ERROR", e.getMessage());
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
            }
        }
    }

    private GoogleMap.OnMarkerClickListener mOnMarkerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Toast.makeText(getContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            return false;
        }
    };

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
