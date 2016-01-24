package com.shopfitt.android.Utils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.widget.TextView;
import android.widget.TimePicker;

public class CustomDatePicker {
    private static int hour, minute;

    private static TextView dateEdit;
    private static Context mContext;

    /**
     * Default Constructor
     */
    private CustomDatePicker() {
    }

    /**
     * Opens date picker in dialog window
     *
     * @param fragmentManager fragment manager object
     * @param date            qtyText in which date to be populate
     */

    public static void openDatePicker(FragmentManager fragmentManager, final TextView date,Context mcontext) {
        dateEdit = date;
        mContext = mcontext;
        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.show(fragmentManager, "Time");
    }

    /**
     * Custom DatePicker class
     */
//    public static class DatePickerFragment extends DialogFragment {
//        DatePickerDialog.OnDateSetListener onDateSet;
//
//        public void setCallBack(DatePickerDialog.OnDateSetListener onDateSet) {
//            this.onDateSet = onDateSet;
//        }
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), onDateSet, year, month, day);
//            datePickerDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Clear", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dateEdit.setText("");
//                }
//            });
//            return datePickerDialog;
//        }
//    }

    public static class DatePickerFragment extends DialogFragment {
        DatePickerDialog.OnDateSetListener onDateSet;


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            return new TimePickerDialog(mContext,
                    timePickerListener, hour, minute,false);
        }

        private TimePickerDialog.OnTimeSetListener timePickerListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int selectedHour,
                                          int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;

                        // set current time into textview
                        dateEdit.setText(new StringBuilder().append(pad(hour))
                                .append(":").append(pad(minute)));

                    }
                };

        private static String pad(int c) {
            if (c >= 10)
                return String.valueOf(c);
            else
                return "0" + String.valueOf(c);
        }

    }


}
