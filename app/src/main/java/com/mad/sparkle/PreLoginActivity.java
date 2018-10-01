package com.mad.sparkle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.mad.sparkle.view.LoginActivity;

public class PreLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);
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
