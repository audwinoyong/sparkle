package com.mad.sparkle.view;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mad.sparkle.R;
import com.mad.sparkle.utils.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookingSelectionActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener {

    private EditText datePickerDialogEt;
    private EditText timePickerDialogEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_selection);

        datePickerDialogEt = (EditText) findViewById(R.id.alert_dialog_date_picker_et);
        timePickerDialogEt = (EditText) findViewById(R.id.alert_dialog_time_picker_et);

        datePickerDialogEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment datePickerFragmentDialog = new DatePickerFragment();
                datePickerFragmentDialog.show(getSupportFragmentManager(), Constants.DIALOG_DATE);
            }
        });

        timePickerDialogEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                CustomTimePickerDialog customTimePickerDialog = new CustomTimePickerDialog(BookingSelectionActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(BookingSelectionActivity.this, "Selected Time : " + formatTime(hourOfDay, minute), Toast.LENGTH_SHORT).show();
                        timePickerDialogEt.setText(formatTime(hourOfDay, minute));
                    }
                }, hour, minute, false);
                customTimePickerDialog.show();
            }
        });

        Button confirmBtn = (Button) findViewById(R.id.activity_booking_selection_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(BookingSelectionActivity.this, PaymentActivity.class);
                startActivity(paymentIntent);

            }
        });
    }

    @Override
    public void onFinishDialog(Date date) {
        Toast.makeText(this, "Selected Date : " + formatDate(date), Toast.LENGTH_SHORT).show();
        datePickerDialogEt.setText(formatDate(date));
    }

    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(date);
    }

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
