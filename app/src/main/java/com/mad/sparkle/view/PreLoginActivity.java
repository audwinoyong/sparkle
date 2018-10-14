package com.mad.sparkle.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;

public class PreLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
        }

    }

    public void launchRegisterActivity(View view) {
        Intent registerIntent = new Intent(PreLoginActivity.this, RegisterActivity.class);
        startActivity(registerIntent);
    }

    public void launchLoginActivity(View view) {
        Intent loginIntent = new Intent(PreLoginActivity.this, LoginActivity.class);
        startActivity(loginIntent);
    }
}
