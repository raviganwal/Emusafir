package com.emusafir.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import com.emusafir.interfaces.OnDateSelected;
import com.emusafir.utility.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static com.emusafir.utility.Constant.MIN_DATE;
import static com.emusafir.utility.Constant.WHICH_BUTTON;
import static com.emusafir.utility.Constant.dd_MMM_yy;

public class DatePickerFragment extends BottomSheetDialogFragment {
    private OnDateSelected mOnDateSelected;
    private String whichButtonDate, minDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        whichButtonDate = getArguments().getString(WHICH_BUTTON);
        minDate = getArguments().getString(MIN_DATE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        final DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
//        if (whichButtonDate.equalsIgnoreCase("tvDepartDate"))
//            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
//        if (whichButtonDate.equalsIgnoreCase("tvReturnDate"))
        if (minDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(dd_MMM_yy);
            Calendar cal = Calendar.getInstance();
            try {
                cal.setTime(sdf.parse(minDate));
                datePickerDialog.getDatePicker().setMinDate(cal.getTimeInMillis());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());

        }

        return datePickerDialog;
    }

    private DatePickerDialog.OnDateSetListener dateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {
//                    Toast.makeText(getActivity(), "selected date is " + view.getYear() +
//                            " / " + (view.getMonth() + 1) +
//                            " / " + view.getDayOfMonth(), Toast.LENGTH_SHORT).show();
                    String str = view.getYear() + "-" + (view.getMonth() + 1) + "-" + view.getDayOfMonth();
                    mOnDateSelected.onDateSelected(Utils.dateFormat_dd_MMM_yy(str), whichButtonDate);
                }
            };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        final Fragment parent = getParentFragment();
        if (parent != null) {
            mOnDateSelected = (OnDateSelected) parent;
        } else {
            mOnDateSelected = (OnDateSelected) context;
        }
    }

    @Override
    public void onDetach() {
        mOnDateSelected = null;
        super.onDetach();
    }
}