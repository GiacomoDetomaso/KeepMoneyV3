package com.example.keepmoneyv3.dialogs;

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

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Keys;


import java.util.ArrayList;

/**
 * A class tha represents the dialog where the user will register his entries
 *
 * @author Giacomo Detomaso
 * @see DialogFragment
 * @see DialogEntriesListener
 * */

public class DialogEntries extends DialogFragment {
    public interface DialogEntriesListener{
        void DialogEntriesInsert(float val,String date,String idCat);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DialogEntriesListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + "Must implement the interface");
        }
    }

    private DialogEntriesListener listener;
    private EditText txtDate,txtType, txtEntry;
    private View root;
    private String strDate;
    private Category category;

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_dashboard_dialog_entries, null);
        builder.setView(root);
        builder.setTitle(Keys.DialogTitles.DIALOG_ENTRIES_TITLE);

        txtEntry = root.findViewById(R.id.txtEntryPrice);//the entries
        txtDate = root.findViewById(R.id.txtDateEntr);//the date of the entries
        txtType = root.findViewById(R.id.txtTypeEntr);//the category of the entries

        txtDataAction(txtDate);
        txtTypeAction(txtType);
        dialogEntriesAction();

        return builder.create();
    }

    /**
     * This method checks if the input of the entry is correct.
     * If it is the method calls the interface's method that will save the entry in the database.
     * The interface method is implemented in the NavigationActivity.
     *
     * //@see DialogEntriesListener
     * @see NavigationActivity
     * */
    private void dialogEntriesAction(){
        Button button = root.findViewById(R.id.btnAddEntry);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText [] txtFields = {txtEntry, txtDate,txtType};

                boolean isCorrect = true;//check if the values are correct
                float val = 0;

                for (EditText t : txtFields){
                    if (t.getText().toString().equals("")){
                        isCorrect = false;//control if there are any empty edit texts
                    }
                }

                if (isCorrect){
                    try {
                        val = Float.parseFloat(txtEntry.getText().toString());//control if the value is a number
                    }catch (Exception e){
                        isCorrect = false;
                    }

                    if (isCorrect){
                        String idCat = category.getId();//extract the category id
                        listener.DialogEntriesInsert(val, strDate,idCat);
                        dismiss();//close the dialog
                    }else {
                        Toast.makeText(getActivity(),"Il campo prezzo non è un numero",Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getActivity(),"Campi vuoti impossibile inserire",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void txtDataAction(EditText txtData){
        txtData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fragment = new DatePickerDialogFrag(Keys.DialogTags.DIALOG_ENTRIES_TAG);
                FragmentManager manager = getFragmentManager();

                if(manager != null)
                    fragment.show(manager,Keys.DialogTags.DIALOG_DATE_PICKER);//show the date picker fragment
            }
        });
    }

    private void txtTypeAction(EditText txtType){

        txtType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                DialogAddNewType dialogAddNewType = new DialogAddNewType(categoriesEntries, Keys.DialogTags.DIALOG_ENTRIES_TAG);
                FragmentManager manager = getFragmentManager();

                if(manager != null)
                    dialogAddNewType.show(manager,Keys.DialogTags.DIALOG_ADD_NEW_TYPE_TAG);

            }
        });
    }

    /**
     * This method is used to search the name of the category, using a linear search. The method
     * returns an integer, that corresponds to the position of the category inside the ArrayList.
     * If the research is unsuccessful the return parameter will be equals to the size of the
     * Arraylist.
     *
     * @param categories        - all the categories
     * @param catName           - the name of the category to search
     *
     * @return i                - the position inside the ArrayList of the category
     * */
    private int searchCategoryName(ArrayList<Category> categories, String catName) {
        int i;
        for (i = 0; i < categories.size(); i++) {
            if (categories.get(i).getName().equals(catName)) break;
        }
        return i;
    }

    void setStrDate(String strDate){
        txtDate = root.findViewById(R.id.txtDateEntr);

        this.strDate = strDate;
        String [] dataSQL = strDate.split("/");
        strDate = dataSQL[2] + "/" + dataSQL[1] + "/" + dataSQL[0];
        txtDate.setText(strDate);
    }

    public void setCategory(Category cat){
        this.category = cat;
        txtType.setText(cat.getName());
    }
}
