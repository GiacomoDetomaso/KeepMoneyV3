package com.example.keepmoneyv3.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class DatePickerDialogFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private String dialogCalled;

    public DatePickerDialogFrag(String dialogCalled){
        this.dialogCalled = dialogCalled;
    }

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

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (dialogCalled.equals("DialogEntries")) {
            DialogEntries dialogEntries = (DialogEntries) getFragmentManager().findFragmentByTag(dialogCalled);
            assert dialogEntries != null;
            String data = day + "/" + month + "/" + year;
            dialogEntries.setStrDate(data);
        }/*else {
            DialogPurchase dialogPurchase = (DialogPurchase) getFragmentManager().findFragmentByTag(dialogCalled);
            assert dialogPurchase != null;
            String data = day + "/" + month + "/" + year;
            dialogPurchase.setData(data);
        }*/
    }
}
