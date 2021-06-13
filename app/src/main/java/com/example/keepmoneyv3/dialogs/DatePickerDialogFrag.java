package com.example.keepmoneyv3.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.utility.ApplicationTags;

import java.util.Calendar;

/**
 * This class is used to create a date picker inside a dialog.
 *
 * @author Giacomo Detomaso
 * */
public class DatePickerDialogFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private final String dialogCalled;

    public DatePickerDialogFrag(String dialogCalled){
        this.dialogCalled = dialogCalled;
    }

    /**
     * This method returns a DatePickerDialog with the current date.
     * */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialogFrag and return it
        Activity activity = getActivity();
        assert  activity != null;
        return new DatePickerDialog(activity, this, year, month, day);
    }

    /**
     * This method is called when a date is picked from the Picker. It is used to
     * set the date on the EditText.
     *
     * @param view      the view of the DatePicker
     * @param year      the year of the date
     * @param month     the month of the date
     * @param day       the day of the date*/
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (dialogCalled.equals(ApplicationTags.DialogTags.DIALOG_INCOME_TAG)) {
            DialogIncome dialogIncome = (DialogIncome) requireActivity().getSupportFragmentManager().findFragmentByTag(dialogCalled);
            assert dialogIncome != null;
            String data = day + "/" + (month + 1) + "/" + year;
            dialogIncome.setStrDate(data);
        } else {
            DialogPurchase dialogPurchase = (DialogPurchase) requireActivity().getSupportFragmentManager().findFragmentByTag(dialogCalled);
            assert dialogPurchase != null;
            String data = day + "/" + (month + 1)  + "/" + year;
            dialogPurchase.setStrDate(data);
        }
    }
}