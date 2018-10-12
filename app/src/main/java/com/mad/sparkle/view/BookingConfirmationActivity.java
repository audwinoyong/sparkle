package com.mad.sparkle.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.mad.sparkle.R;

public class BookingConfirmationActivity extends AppCompatActivity {

    private Button mHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        mHomeBtn = (Button) findViewById(R.id.activity_booking_confirmation_home_button);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(homeIntent);
                finish();
            }
        });
    }
}
