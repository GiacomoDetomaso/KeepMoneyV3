package com.labproject.keepmoneyv3.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.labproject.keepmoneyv3.utility.ApplicationTags;

import java.util.Calendar;

/**
 * This class is used to create a time picker inside a dialog.
 *
 * @author Giacomo Detomaso
 * */
public class TimePickerDialogFragment extends DialogFragment implements android.app.TimePickerDialog.OnTimeSetListener {


    /**
     * This method returns a TimePickerDialog with the current date.
     *
     * @return the dialog
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

        return new android.app.TimePickerDialog(getActivity(),this,hour,minutes,DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * This method is called when the time is picked from the Picker. It is used to
     * set the time on the EditText.
     * @param view      the view of the TimePicker
     * @param hourOfDay the hour picked from the TimePicker
     * @param minute    minutes picked from the TimePicker
     *
     */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        DialogPurchase dialogPurchase = (DialogPurchase) requireActivity().getSupportFragmentManager().findFragmentByTag(ApplicationTags.DialogTags.DIALOG_PURCHASES_TAG);
        if(dialogPurchase != null) {
            String time;
            if (hourOfDay < 10)
                time = "0" + hourOfDay;
            else
                time = "" + hourOfDay;
            if (minute < 10)
                time = time + ":" + "0" + minute;
            else
                time = time + ":" + minute;
            dialogPurchase.setStrTime(time);
        }
    }
}
