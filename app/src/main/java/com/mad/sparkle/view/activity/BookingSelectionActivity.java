package com.mad.sparkle.view.activity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mad.sparkle.R;
import com.mad.sparkle.model.Store;
import com.mad.sparkle.utils.Constants;
import com.mad.sparkle.view.CustomTimePickerDialog;
import com.mad.sparkle.view.fragment.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.mad.sparkle.utils.Constants.LOG_TAG;

/**
 * Enable user to select the booking date and time of the selected store.
 */
public class BookingSelectionActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener {

    private DatabaseReference mDatabaseRef;

    private TextView mStoreNameTv;
    private EditText datePickerDialogEt;
    private EditText timePickerDialogEt;

    private String mStoreId;
    private String mStoreName;
    private String mDate;
    private String mTime;

    /**
     * Called when activity is first created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_selection);

        mStoreId = getIntent().getStringExtra(Constants.STORE_ID);

        mStoreNameTv = (TextView) findViewById(R.id.activity_booking_selection_store_name_tv);
        datePickerDialogEt = (EditText) findViewById(R.id.alert_dialog_date_picker_et);
        timePickerDialogEt = (EditText) findViewById(R.id.alert_dialog_time_picker_et);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child(Constants.STORES).child(mStoreId);

        // Show the store name
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Store store = dataSnapshot.getValue(Store.class);
                mStoreName = store.getName();
                mStoreNameTv.setText(mStoreName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.d(LOG_TAG, "Failed to read value.", databaseError.toException());
            }
        });

        // Show the date picker dialog when user clicks
        datePickerDialogEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragmentDialog = new DatePickerFragment();
                datePickerFragmentDialog.show(getSupportFragmentManager(), Constants.DIALOG_DATE);
            }
        });

        // Show the time picker dialog when user clicks
        timePickerDialogEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(BookingSelectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // Set the selected time
                        timePickerDialogEt.setText(formatTime(hourOfDay, minute));
                        mTime = formatTime(hourOfDay, minute);
                    }
                }, hour, minute, false);
                customTimePickerDialog.show();
            }
        });

        // Set the onClick listener for proceeding to payment activity
        Button proceedToPaymentBtn = (Button) findViewById(R.id.activity_booking_proceed_to_payment_btn);
        proceedToPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Both date and time are selected
                if (!TextUtils.isEmpty(mDate) || !TextUtils.isEmpty(mTime)) {
                    Intent paymentIntent = new Intent(BookingSelectionActivity.this, PaymentActivity.class);
                    paymentIntent.putExtra(Constants.STORE_ID, mStoreId);
                    paymentIntent.putExtra(Constants.STORE_NAME, mStoreName);
                    paymentIntent.putExtra(Constants.DATE, mDate);
                    paymentIntent.putExtra(Constants.TIME, mTime);
                    startActivity(paymentIntent);

                    Log.d(LOG_TAG, "Launching payment activity");
                } else {
                    Toast.makeText(BookingSelectionActivity.this, getString(R.string.error_select_both_date_and_time), Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "Date or time is not selected");
                }
            }
        });
    }

    /**
     * Set the selected date after the dialog finish
     *
     * @param date selected date
     */
    @Override
    public void onFinishDialog(Date date) {
        datePickerDialogEt.setText(formatDate(date));
        mDate = formatDate(date);
    }

    /**
     * Format the date into string
     *
     * @param date selected date
     * @return formatted date
     */
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

    /**
     * Format the time into string
     *
     * @param hourOfDay selected hour
     * @param minute    selected minute
     * @return formatted time
     */
    public String formatTime(int hourOfDay, int minute) {

        String timeSet;
        if (hourOfDay > 12) {
            hourOfDay -= 12;
            timeSet = "PM";
        } else if (hourOfDay == 0) {
            hourOfDay += 12;
            timeSet = "AM";
        } else if (hourOfDay == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (minute < 10)
            minutes = "0" + minute;
        else
            minutes = String.valueOf(minute);

        return String.valueOf(hourOfDay) + ':' + minutes + " " + timeSet;
    }
}
