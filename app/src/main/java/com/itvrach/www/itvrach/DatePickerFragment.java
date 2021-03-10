package com.itvrach.www.itvrach;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import java.util.Calendar;
/**
 * Created by engineer on 7/16/2018.
 */

public class DatePickerFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day   = c.get(Calendar.DAY_OF_MONTH);
       // int hour  = c.get(Calendar.HOUR_OF_DAY);
        //int minute = c.get(Calendar.MINUTE);
        //int second = c.get(Calendar.SECOND);


        return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener) getActivity(), year,month,day);

    }
}
