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
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.model.nearbysearch.NearbySearchResponse;
import com.mad.sparkle.model.nearbysearch.Photo;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.service.GooglePlacesService;
import com.mad.sparkle.utils.RetrofitClient;
import com.mad.sparkle.viewmodel.MapViewModel;
import com.mad.sparkle.R;

import java.util.ArrayList;
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
//    private JSONObject mJsonPlaceList;

    private List<Store> mStoreList = new ArrayList<Store>();

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
                if (mLastKnownLocation != null) {
                    Toast.makeText(getContext(), "Getting nearby car wash...", Toast.LENGTH_SHORT).show();

//                new getNearbyPlacesAsyncTask(mMap, getNearbyPlacesUrl()).execute();

                    findNearbyPlaces();

                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(mLastKnownLocation).zoom(DEFAULT_ZOOM).build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                } else {
                    Toast.makeText(getContext(), "Current location is not found, please enable current location in Settings", Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Current location is not found");
                }
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
//                Toast.makeText(getContext(), marker.getTitle() + position + marker.getSnippet(), Toast.LENGTH_SHORT).show();

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
                                LatLng currentLocationLatLng = DEFAULT_LOCATION_SYDNEY;
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocationLatLng, DEFAULT_ZOOM));
                                mLastKnownLocation = currentLocationLatLng;
                            }
                        } else {
                            Toast.makeText(getContext(), getString(R.string.unable_to_find_current_location), Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "Show current location failed");
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.d(LOG_TAG, e.getMessage());
        }

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

    private void findNearbyPlaces() {
        Log.d(LOG_TAG, "Getting nearby car wash...");

        GooglePlacesService googlePlacesService = RetrofitClient.getClient().create(GooglePlacesService.class);
        Call<NearbySearchResponse> call = googlePlacesService
                .getNearbyPlaces(mLastKnownLocation.latitude + "," + mLastKnownLocation.longitude, getString(R.string.google_maps_key));

        call.enqueue(new Callback<NearbySearchResponse>() {
            @Override
            public void onResponse(@NonNull Call<NearbySearchResponse> call, @NonNull Response<NearbySearchResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(LOG_TAG, "Google Places API request successful");

                    if (response.body().getStatus().equals(Constants.OVER_QUERY_LIMIT)) {
                        Log.d(LOG_TAG, "Google Places API reaches daily query limit");
                        Toast.makeText(getContext(), "You have reached your request limit. Please wait for a few minutes.", Toast.LENGTH_SHORT).show();
                    }

                    mMap.clear();
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
                        int distance = (int) SphericalUtil.computeDistanceBetween(mLastKnownLocation, placeLatLng);

                        // Generate 7 digits random phone number,
                        // because calling Google Places API for Place Details query require payment per request.
                        int phoneDigits = new Random().nextInt(9000000) + 1000000;
                        String randPhone = "02" + "9" + String.valueOf(phoneDigits);

                        Store newStore = new Store(name, address, distance, rating, randPhone, latitude, longitude, photoReference);
                        mStoreList.add(newStore);

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

                        // Add customised markers
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(placeLatLng);
                        markerOptions.title(name);
                        markerOptions.snippet(address + " | " + rating + getString(R.string.unicode_character_star));
                        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                        Marker marker = mMap.addMarker(markerOptions);
                        marker.setTag(i);

//                        mMarkerMap.put(marker.getId(), newStore.getName());
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

//    private String getNearbyPlacesUrl() {
//        StringBuilder nearbyPlacesUrlStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
//        nearbyPlacesUrlStringBuilder.append("location=" + mLastKnownLocation.latitude + "," + mLastKnownLocation.longitude);
//        nearbyPlacesUrlStringBuilder.append("&rankby=" + "distance");
//        nearbyPlacesUrlStringBuilder.append("&keyword=" + "car+wash");
//        nearbyPlacesUrlStringBuilder.append("&key=" + getString(R.string.google_maps_key));
//
//        Log.d(LOG_TAG, "url= " + nearbyPlacesUrlStringBuilder.toString());
//        return nearbyPlacesUrlStringBuilder.toString();
//    }

//    private String getPlaceDetailUrl(String placeId) {
//        StringBuilder placeDetailStringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/place/details/json?");
//        placeDetailStringBuilder.append("placeid=" + placeId);
//        placeDetailStringBuilder.append("&fields=" + "formatted_phone_number");
//        placeDetailStringBuilder.append("&key=" + getString(R.string.google_maps_key));
//
//        Log.d(LOG_TAG, "url= " + placeDetailStringBuilder.toString());
//        return placeDetailStringBuilder.toString();
//    }
//
//    private void updateMap() {
//        try {
//            if (mJsonPlaceList == null) {
//                Log.d(LOG_TAG, "Failed to get a list of places");
//            } else {
//                JSONArray placeList = (JSONArray) mJsonPlaceList.get("results");
//                for (int i = 0; i < placeList.length(); i++) {
//                    JSONObject place = (JSONObject) placeList.get(i);
//
//                    double latitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
//                    double longitude = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
//                    LatLng placeLatLng = new LatLng(latitude, longitude);
//
//                    String placeId = place.getString("place_id");
//                    String name = place.getString("name");
//                    String address = place.getString("vicinity");
//                    double rating = place.getDouble("rating");
//                    String photoReference = "";
//
//                    // Get photo reference if the place has photo
//                    if (place.has("photos")) {
//                        JSONArray photos = place.getJSONArray("photos");
//
//                        // Grab the first photo reference
//                        JSONObject photoDetail = (JSONObject) photos.get(0);
//                        photoReference = photoDetail.getString("photo_reference");
//                    }
//
//                    // Calculate the nearest distance
//                    int distance = (int) SphericalUtil.computeDistanceBetween(mLastKnownLocation, placeLatLng);
//
//                    // Generate 7 digits random phone number,
//                    // because calling Google Places API for Place Details query require payment per request.
//                    int phoneDigits = new Random().nextInt(9000000) + 1000000;
//                    String randPhone = "02" + "9" + String.valueOf(phoneDigits);
//
//                    Store newStore = new Store(name, address, distance, rating, randPhone, latitude, longitude, photoReference);
//                    mStoreList.add(newStore);
//
//                    // Store car wash stores into Firebase Database
//                    FirebaseDatabase.getInstance().getReference(Constants.STORES)
//                            .child(placeId).setValue(newStore).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()) {
//                                Log.d(LOG_TAG, "registerStoreToDatabase:success");
//                            } else {
//                                Log.d(LOG_TAG, "registerStoreToDatabase:failure", task.getException());
//                            }
//                        }
//                    });
//
//                    // Add customised markers
//                    MarkerOptions markerOptions = new MarkerOptions();
//                    markerOptions.position(placeLatLng);
//                    markerOptions.title(name);
//                    markerOptions.snippet(address + " | " + rating + getString(R.string.unicode_character_star));
//                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//
//                    Marker marker = mMap.addMarker(markerOptions);
//                    marker.setTag(i);
//
////                    mMarkerMap.put(marker.getId(), newStore.getName());
//
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }

//    private class getNearbyPlacesAsyncTask extends AsyncTask<Void, Void, Boolean> {
//
//        private GoogleMap mGoogleMap;
//        private String mPlaceQuery;
//        private String mData;
//
//        public getNearbyPlacesAsyncTask(GoogleMap googleMap, String placeQuery) {
//            mGoogleMap = googleMap;
//            mPlaceQuery = placeQuery;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//
//            try {
//                Log.d(LOG_TAG, "Getting nearby car wash...");
//
//                URL url = new URL(mPlaceQuery);
//                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//                connection.connect();
//
//                InputStream inputStream = connection.getInputStream();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line);
//                }
//
//                mData = stringBuilder.toString();
//                Log.d(LOG_TAG, mData);
//                bufferedReader.close();
//
//                return true;
//
//            } catch (MalformedURLException e) {
//                Log.d(LOG_TAG, e.getMessage());
//                return false;
//
//            } catch (IOException e) {
//                Log.d(LOG_TAG, e.getMessage());
//                return false;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            super.onPostExecute(result);
//
//            try {
//                StringReader stringReader = new StringReader(mData);
//                BufferedReader bufferedReader = new BufferedReader(stringReader);
//
//                StringBuilder stringBuilder = new StringBuilder();
//                String line;
//
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuilder.append(line + "\n");
//                }
//
//                mJsonPlaceList = new JSONObject(stringBuilder.toString());
//
//                updateMap();
//
//            } catch (IOException e) {
//                Log.d(LOG_TAG, e.getMessage());
//            } catch (JSONException e) {
//                Log.d(LOG_TAG, e.getMessage());
//            }
//        }
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
