package com.labproject.keepmoneyv3.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.activities.NavigationActivity;
import com.labproject.keepmoneyv3.database.*;
import com.labproject.keepmoneyv3.utility.Category;
import com.labproject.keepmoneyv3.utility.ApplicationTags;


import java.util.ArrayList;

/**
 * A class tha represents the dialog where the user will register his entries.
 *
 *
 * @see DialogFragment
 * @see DialogIncomeListener
 *
 * @author Michelangelo De Pascale
 * */

public class DialogIncome extends DialogFragment {

    /**
     * @see NavigationActivity
     * */
    public interface DialogIncomeListener {
        void DialogIncomeInsert(float val, String date, String idCat);
    }

    /**
     * This method attach the listener to the NavigationActivity.
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DialogIncomeListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    private DialogIncomeListener listener;
    private EditText txtDate,txtType, txtIncome;
    private View root;
    private String strDate;
    private Category category;

    /**
     * This method creates the dialog to add a new income and specifies the action of its components.
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_dashboard_dialog_entries, null);
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_ENTRIES_TITLE);

        txtIncome = root.findViewById(R.id.txtIncomeValue);//the entries
        txtDate = root.findViewById(R.id.txtDateIncome);//the date of the entries
        txtType = root.findViewById(R.id.txtCategoryIncome);//the category of the entries

        txtDateAction(txtDate); // call the dialog to choose the date
        txtTypeAction(txtType); // call the category dialog to set the entry's category
        dialogIncomeAction(); // perform the insert

        return builder.create();
    }

    /**
     * This method checks if the input of the income is correct.
     * If it is the method calls the interface's method that will save the income in the database.
     * The interface method is implemented in NavigationActivity.
     *
     * @see DialogIncomeListener
     * @see NavigationActivity
     * */
    private void dialogIncomeAction(){
        Button button = root.findViewById(R.id.btnConfirmIncome);

        button.setOnClickListener(view -> {
            EditText [] txtFields = {txtIncome, txtDate,txtType};

            boolean isCorrect = true;//check if the values are correct
            float val = 0;

            for (EditText t : txtFields){
                if (t.getText().toString().equals("")){
                    isCorrect = false;//control if there are any empty edit texts
                }
            }

            if (isCorrect){
                try {
                    val = Float.parseFloat(txtIncome.getText().toString());//control if the value is a number
                }catch (Exception e){
                    isCorrect = false;
                }

                if (isCorrect && val > 0){
                    String idCat = category.getId();//extract the category id
                    listener.DialogIncomeInsert(val, strDate,idCat);
                    dismiss();//close the dialog
                }else {
                    Toast.makeText(getActivity(),"Il campo prezzo non è un numero o non è un numero maggiore di 0",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getActivity(),"Campi vuoti impossibile inserire",Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This method is used to show the DatePicker to select the income's date.
     *
     * @param txtDate       the EditText that triggers the action
     * */
    private void txtDateAction(EditText txtDate){
        txtDate.setOnClickListener(v -> {
            DialogFragment fragment = new DatePickerDialogFragment(ApplicationTags.DialogTags.DIALOG_INCOME_TAG);
            FragmentManager manager = requireActivity().getSupportFragmentManager();

            fragment.show(manager, ApplicationTags.DialogTags.DIALOG_DATE_PICKER_TAG);//show the date picker fragment
        });
    }

    /**
     * This method is used to show the dialog to select the category of the income.
     *
     * @param txtType       the EditText that triggers the action
     * */
    private void txtTypeAction(EditText txtType){

        txtType.setOnClickListener(v -> {
            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.queryGetAllRows(DbStrings.TableCategoriesFields.TABLE_NAME);//get all the rows of the table category
            ArrayList<Category> allCategories = new ArrayList<>();

            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_ID));
                String desc = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_DESC));
                int picid = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                allCategories.add(new Category(id, desc, picid));
            }

            //get the entries categories
            ArrayList<Category> categoriesEntries = new ArrayList<>();
            int pos = searchCategoryName(allCategories, "Stipendio");
            categoriesEntries.add(allCategories.get(pos));
            pos = searchCategoryName(allCategories, "Regalo");
            categoriesEntries.add(allCategories.get(pos));
            pos = searchCategoryName(allCategories, "Scommessa");
            categoriesEntries.add(allCategories.get(pos));

            DialogAddNewType dialogAddNewType = new DialogAddNewType(categoriesEntries, ApplicationTags.DialogTags.DIALOG_INCOME_TAG);
            FragmentManager manager = requireActivity().getSupportFragmentManager();

            // show the dialog to select Entry's category
            dialogAddNewType.show(manager, ApplicationTags.DialogTags.DIALOG_ADD_NEW_TYPE_TAG);

        });
    }

    /**
     * This method is used to search the name of the category, using a linear search. The method
     * returns an integer, that corresponds to the position of the category inside the ArrayList.
     * If the research is unsuccessful the return parameter will be equals to the size of the
     * Arraylist.
     *
     * @param categories        all the categories
     * @param catName           the name of the category to search
     *
     * @return i                the position inside the ArrayList of the category
     * */
    private int searchCategoryName(ArrayList<Category> categories, String catName) {
        int i;
        for (i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(catName)) break;
        }
        return i;
    }

    /**
     * This method is used to set the date string inside the EditText.
     *
     * @param strDate       the date string
     * */
    void setStrDate(String strDate){
        txtDate = root.findViewById(R.id.txtDateIncome);

        this.strDate = strDate;
        String [] dataSQL = strDate.split("/");
        strDate = dataSQL[2] + "/" + dataSQL[1] + "/" + dataSQL[0];
        txtDate.setText(strDate);
    }

    /**
     * This method is used to set the category name inside the EditText.
     *
     * @param cat       the category
     * */
    public void setCategory(Category cat){
        this.category = cat;
        txtType.setText(cat.getName());
    }
}
