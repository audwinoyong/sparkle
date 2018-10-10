package com.mad.sparkle.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import com.mad.sparkle.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment {

    private DatePicker datePicker;

    public interface DateDialogListener {
        void onFinishDialog(Date date);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        Calendar calendar = Calendar.getInstance();

        datePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        // set today as minimum date and disable past dates
        datePicker.setMinDate(calendar.getTimeInMillis());
        // add 6 months from today
        calendar.add(Calendar.MONTH, 6);
        // set 6 months as maximum date
        datePicker.setMaxDate(calendar.getTimeInMillis());

        return new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.DialogCustomTheme)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = datePicker.getYear();
                                int mon = datePicker.getMonth();
                                int day = datePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, mon, day).getTime();
                                DateDialogListener activity = (DateDialogListener) getActivity();
                                activity.onFinishDialog(date);
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
}