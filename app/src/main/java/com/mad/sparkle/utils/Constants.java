package com.mad.sparkle.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * @author Audwin
 * Created on 01/10/18
 * <p>
 * Constants class provides all string data
 */
public class Constants {

    // Firebase constants
    public static final String USERS = "users";
    public static final String STORES = "stores";
    public static final String BOOKINGS = "bookings";
    public static final String PROFILE_IMAGE = "profileImage";

    // Request codes
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    public static final int REQUEST_LOCATION_PERMISSION = 1002;
    public static final int REQUEST_CALL_PHONE = 1003;
    public static final int REQUEST_CAMERA = 1004;

    // Map constants
    public static final int DEFAULT_ZOOM = 14;
    public static final LatLng DEFAULT_LOCATION_SYDNEY = new LatLng(-33.8840504, 151.1992254);
    public static final String PLACES_API_BASE_URL = "https://maps.googleapis.com/maps/";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

    // Store detail constants
    public static final String CALL_PREFIX = "tel:";

    // Debug log tag
    public static final String LOG_TAG = "LOG_TAG";

    // Store intent key
    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";

    // Date and time picker tag and intent keys
    public static final String DIALOG_DATE = "DIALOG_DATE";
    public static final String DATE = "date";
    public static final String TIME = "time";

    // Authentication constants
    public static final String REGEX_NAME = "[A-Z][a-zA-Z]*";
    public static final String TYPE_IMAGE = "image/*";
}
