package com.mad.sparkle.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mad.sparkle.R;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Show booking confirmation activity after booking is successful
 */
public class BookingConfirmationActivity extends AppCompatActivity {

    private Button mHomeBtn;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_confirmation);

        // onClick listener for the home button
        mHomeBtn = (Button) findViewById(R.id.activity_booking_confirmation_home_button);
        mHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchNavigationActivity();
            }
        });
    }

    /**
     * Navigate back to navigation activity.
     * Sets the flags to clear all above activities on the stack and
     * without reloading the first instance.
     */
    public void launchNavigationActivity() {
        Intent homeIntent = new Intent(getApplicationContext(), NavigationActivity.class);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(homeIntent);
        finish();
        Log.d(LOG_TAG, "Launching navigation activity");
    }

    /**
     * Override the onBackPressed method on the navigation bar.
     * Returns to navigation activity instead of the previous activity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        launchNavigationActivity();
    }
}
