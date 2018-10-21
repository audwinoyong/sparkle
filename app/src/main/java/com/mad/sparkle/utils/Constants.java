package com.mad.sparkle.utils;

import com.google.android.gms.maps.model.LatLng;

/**
 * Constants class provides all string data
 */
public class Constants {

    // Debug log tag
    public static final String LOG_TAG = "LOG_TAG";

    // Request codes
    public static final int REQUEST_IMAGE_CAPTURE = 1001;
    public static final int REQUEST_LOCATION_PERMISSION = 1002;
    public static final int REQUEST_CALL_PHONE = 1003;
    public static final int REQUEST_CAMERA = 1004;

    // Authentication constants
    public static final String REGEX_NAME = "[A-Z][a-zA-Z]*";
    public static final String TYPE_IMAGE = "image/*";

    // Firebase constants
    public static final String USERS = "users";
    public static final String STORES = "stores";
    public static final String BOOKINGS = "bookings";
    public static final String PROFILE_IMAGE = "profileImage";

    // Map constants
    public static final int DEFAULT_ZOOM = 14;
    public static final LatLng DEFAULT_LOCATION_SYDNEY = new LatLng(-33.8840504, 151.1992254);
    public static final String PLACES_API_BASE_URL = "https://maps.googleapis.com/maps/";
    public static final String OVER_QUERY_LIMIT = "OVER_QUERY_LIMIT";

    // Url constants
    public static final String PHOTO_BASE_URL = "https://maps.googleapis.com/maps/api/place/photo?";
    public static final String PHOTO_MAX_WIDTH = "maxwidth=400";
    public static final String PHOTO_REFERENCE_URL = "&photoreference=";
    public static final String PHOTO_KEY = "&key=";

    // Store detail constants
    public static final String PHONE_DIGIT_PREFIX = "029";
    public static final String CALL_PREFIX = "tel:";
    public static final String PHONE_FORMAT = "(%s) %s %s";

    // Store intent keys
    public static final String STORE_ID = "storeId";
    public static final String STORE_NAME = "storeName";

    // Date and time picker tag, intent keys
    public static final int TIME_PICKER_INTERVAL = 30;
    public static final String DIALOG_DATE = "DIALOG_DATE";
    public static final String DATE = "date";
    public static final String TIME = "time";

}
