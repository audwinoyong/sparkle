package com.mad.sparkle.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mad.sparkle.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BookingSelectionActivity extends AppCompatActivity implements DatePickerFragment.DateDialogListener,
        TimePickerFragment.TimeDialogListener {

    private static final String DIALOG_DATE = "BookingSelectionActivity.DateDialog";
    private static final String DIALOG_TIME = "BookingSelectionActivity.TimeDialog";

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
                DatePickerFragment dialog = new DatePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_DATE);
            }
        });

        timePickerDialogEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = new TimePickerFragment();
                dialog.show(getSupportFragmentManager(), DIALOG_TIME);
            }
        });

        Button confirmBtn = (Button) findViewById(R.id.activity_booking_selection_button);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent bookingConfirmIntent = new Intent(BookingSelectionActivity.this, BookingConfirmationActivity.class);
                startActivity(bookingConfirmIntent);

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
        String hireDate = sdf.format(date);
        return hireDate;
    }

    @Override
    public void onFinishDialog(String time) {
        Toast.makeText(this, "Selected Time : " + time, Toast.LENGTH_SHORT).show();
        timePickerDialogEt.setText(time);
    }
}
