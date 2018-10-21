package com.mad.sparkle.view.fragment;

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

/**
 * The date picker fragment dialog for user to select booking date.
 */
public class DatePickerFragment extends DialogFragment {

    private DatePicker mDatePicker;

    /**
     * Listener for Date Dialog when the dialog is finished.
     */
    public interface DateDialogListener {
        void onFinishDialog(Date date);
    }

    /**
     * Called when the dialog is created.
     *
     * @param savedInstanceState Bundle object containing the activity's previously saved state
     * @return the date dialog
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        Calendar calendar = Calendar.getInstance();

        mDatePicker = (DatePicker) v.findViewById(R.id.dialog_date_picker);
        // Set today as minimum date and disable past dates
        mDatePicker.setMinDate(calendar.getTimeInMillis());
        // Add 3 months from today
        calendar.add(Calendar.MONTH, 3);
        // Set 3 months as maximum date
        mDatePicker.setMaxDate(calendar.getTimeInMillis());

        return new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.DialogCustomTheme)
                .setView(v)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int year = mDatePicker.getYear();
                                int mon = mDatePicker.getMonth();
                                int day = mDatePicker.getDayOfMonth();
                                Date date = new GregorianCalendar(year, mon, day).getTime();
                                DateDialogListener activity = (DateDialogListener) getActivity();
                                activity.onFinishDialog(date);
                                dismiss();
                            }
                        })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .create();
    }
}