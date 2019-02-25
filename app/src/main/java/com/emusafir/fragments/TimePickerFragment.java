package com.emusafir.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;


import com.emusafir.interfaces.OnTimeSelected;

import java.util.Calendar;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    private OnTimeSelected mOnTimeSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Use the current time as the default values for the time picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        //Create and return a new instance of TimePickerDialog
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    //onTimeSet() callback method
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String str = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
        mOnTimeSelected.onTimeSelected(str);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mOnTimeSelected = (OnTimeSelected) parent;
        } else {
            mOnTimeSelected = (OnTimeSelected) context;
        }
    }

    @Override
    public void onDetach() {
        mOnTimeSelected = null;
        super.onDetach();
    }
}