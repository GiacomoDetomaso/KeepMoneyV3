package com.example.keepmoneyv3.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.utility.Keys;

import java.util.Calendar;

/**
 * This class is used to create a time picker inside a dialog
 *
 * @author Giacomo Detomaso
 * */
public class TimePickerDialogFrag extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    /**
     * This method describes what happens when the dialog is created
     * */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minutes = c.get(Calendar.MINUTE);

        // Create a new instance of DatePickerDialogFrag and return it
        Activity activity = getActivity();
        assert  activity != null;

        return new TimePickerDialog(getActivity(),this,hour,minutes,DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DialogPurchase dialogPurchase = (DialogPurchase) getFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_PURCHASES_TAG);
        if(dialogPurchase != null) {
            String ora = hourOfDay + ":" + minute;
            dialogPurchase.setStrTime(ora);
        }
    }
}
