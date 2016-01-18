package com.shopfitt.android.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CustomDatePicker {
    private static int year, month, day;

    private static TextView dateEdit;

    /**
     * Default Constructor
     */
    private CustomDatePicker() {
    }

    /**
     * Opens date picker in dialog window
     *
     * @param fragmentManager fragment manager object
     * @param date            editText in which date to be populate
     */

    public static void openDatePicker(FragmentManager fragmentManager, final TextView date) {
        dateEdit = date;
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        datePickerFragment.setCallBack(new
                                               DatePickerDialog.OnDateSetListener() {
                                                   @Override
                                                   public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                                         int dayOfMonth) {
                                                       Calendar calendar = new GregorianCalendar(year, monthOfYear, dayOfMonth);
                                                       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
                                                       date.setText(simpleDateFormat.format(calendar.getTime()));
                                                   }
                                               });
        datePickerFragment.show(fragmentManager, "Date of Birth");
    }

    /**
     * Custom DatePicker class
     */
    public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener onDateSet;

        public void setCallBack(DatePickerDialog.OnDateSetListener onDateSet) {
            this.onDateSet = onDateSet;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSet, year, month, day);
            Calendar c = Calendar.getInstance();
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dateEdit.setText("");
                }
            });
            return datePickerDialog;
        }
    }
}
