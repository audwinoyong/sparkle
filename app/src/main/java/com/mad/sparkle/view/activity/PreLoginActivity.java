package com.mad.sparkle.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Show the options for user to register or login before using the application.
 */
public class PreLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        // Check if the user is logged in, if yes redirect to navigation  activity
        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Log.d(LOG_TAG, "User is logged in, redirecting to Navigation Activity");
                    Intent intent = new Intent(PreLoginActivity.this, NavigationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        checkLocationPermission();
    }

    /**
     * Check location permission, if not granted, request the location permission.
     */
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
        }
    }

    /**
     * Launch the register activity.
     *
     * @param view the view
     */
    public void launchRegisterActivity(View view) {
        Intent registerIntent = new Intent(PreLoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    /**
     * Launch the login activity.
     *
     * @param view the view
     */
    public void launchLoginActivity(View view) {
        Intent loginIntent = new Intent(PreLoginActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }

    /**
     * onStart lifecycle.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LOG_TAG, "PreLogin Activity onStart called");
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    /**
     * onStop lifecycle.
     */
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LOG_TAG, "PreLogin Activity onStop called");
        mAuth.removeAuthStateListener(mAuthStateListener);
    }
}
