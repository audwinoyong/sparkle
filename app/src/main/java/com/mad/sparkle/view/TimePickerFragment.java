package com.mad.sparkle.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import com.mad.sparkle.R;

public class TimePickerFragment extends DialogFragment {

    private TimePicker timePicker;

    public interface TimeDialogListener {
        void onFinishDialog(String time);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        timePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);

        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int hour = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    hour = timePicker.getHour();
                                } else {
                                    hour = timePicker.getCurrentHour();
                                }
                                int minute = 0;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    minute = timePicker.getMinute();
                                } else {
                                    minute = timePicker.getCurrentMinute();
                                }
                                TimeDialogListener activity = (TimeDialogListener) getActivity();
                                activity.onFinishDialog(updateTime(hour, minute));
                                dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
    }

    private String updateTime(int hours, int mins) {

        String timeSet;
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";

        String minutes;
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        return String.valueOf(hours) + ':' + minutes + " " + timeSet;
    }
}