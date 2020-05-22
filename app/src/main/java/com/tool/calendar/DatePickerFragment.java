package com.tool.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {


    private static final String TAG = "DatePickerFragment";


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);    //一月是0
        int day = c.get(Calendar.DAY_OF_MONTH);

        /*Log.d(TAG, "year: "+year);
        Log.d(TAG, "month: "+ month);
        Log.d(TAG, "day: "+day);*/

        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        //Toast.makeText(getActivity(), "year:  "+year+"   month+1:   "+ month+1 + "   day:   "+ day, Toast.LENGTH_SHORT).show();
        ToDoActivity.setValue(year,month+1  ,day);
    }
}
