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
import com.example.keepmoneyv3.utility.Item;
import com.example.keepmoneyv3.utility.Keys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DialogPurchase extends DialogFragment {

    public interface DialogPurchaseListener{
        void DialogPurchaseInsert(Item item, String data, String ora);
    }

    private DialogPurchaseListener listener;
    private String strDate, strTime;
    private Category category;
    private EditText txtDate, txtTime,txtType, txtItem, txtPrice, txtAmount;
    private Button addBtn;
    private final float total;

    public DialogPurchase(float total){
        this.total = total;
    }

    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DialogPurchaseListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        View root = inflater.inflate(R.layout.fragment_dashboard_dialog_purchases, null);
        builder.setView(root);
        builder.setTitle(Keys.DialogTitles.DIALOG_PURCHASES_TITLE);

        // retrieve the EditText from the layout file
        txtTime = root.findViewById(R.id.txtTime);
        txtDate = root.findViewById(R.id.txtDate);
        txtType = root.findViewById(R.id.txtType);//The type of the purchase
        txtItem = root.findViewById(R.id.txtItem);//The object which is going to be acquired
        txtPrice = root.findViewById(R.id.txtPrice);//The price of the  item
        txtAmount = root.findViewById(R.id.txtAmount);//quantity of item
        addBtn = root.findViewById(R.id.btnAddPurch);//button used to insert a new purchase

        txtDataAction(txtDate);
        txtOraAction(txtTime);
        txtTypeAction(txtType);
        dialogPurchaseAction();

        return builder.create();
    }

    private void dialogPurchaseAction(){
        addBtn.setOnClickListener(view -> {
            EditText [] txtFields = {txtType,txtDate, txtItem, txtPrice,txtTime};

            boolean isCorrect = true;//check if the values are correct
            float val = 0;
            int amount = 0;

            for (EditText t : txtFields){
                if (t.getText().toString().equals("")){
                    isCorrect = false;//control if there are any empty edit texts
                    break;
                }
            }

            if (isCorrect){
                try {
                    val = Float.parseFloat(txtPrice.getText().toString());//control if the value is a number
                    amount = Integer.parseInt(txtAmount.getText().toString());//control if the amount is a number
                }catch (Exception e){
                    isCorrect = false;
                }

                if (isCorrect && val < total){
                    String idCat = category.getId();//extract the category id
                    String strItemName = txtItem.getText().toString();//item name
                    Item item = new Item(strItemName, amount, Keys.MiscellaneousKeys.CONFIRMED, val, idCat);
                    listener.DialogPurchaseInsert(item, strDate, strTime);
                    dismiss();//close the dialog
                }else {
                    Toast.makeText(getActivity(),"Il campo prezzo non è un numero o è maggiore rispetto alla disponibilità economica",Toast.LENGTH_LONG).show();
                }
            }else {
                Toast.makeText(getActivity(),"Campi vuoti impossibile inserire",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void txtDataAction(@NotNull EditText txtData){
        txtData.setOnClickListener(v -> {
            DialogFragment fragment = new DatePickerDialogFrag(Keys.DialogTags.DIALOG_PURCHASES_TAG);
            FragmentManager manager = getFragmentManager();

            assert manager != null;
            fragment.show(manager,Keys.DialogTags.DIALOG_DATE_PICKER_TAG);//show the date picker fragment
        });
    }

    private void txtOraAction(@NotNull EditText txtOra){
        txtOra.setOnClickListener(v -> {
            DialogFragment fragment = new TimePickerDialogFrag();
            FragmentManager manager = getFragmentManager();

            assert manager != null;
            fragment.show(manager,Keys.DialogTags.DIALOG_TIME_PICKER_TAG);//show the time picker fragment
        });
    }

    private void txtTypeAction(@NotNull EditText txtType){
        txtType.setOnClickListener(v -> {
            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.queryGetAllRows(DbStrings.TableCategoriesFields.TABLE_NAME);
            ArrayList<Category> allCategories = new ArrayList<>();

            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_ID));
                String desc = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_DESC));
                int picid = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                allCategories.add(new Category(id, desc, picid));
            }

            DialogAddNewType dialogAddNewType = new DialogAddNewType(allCategories, Keys.DialogTags.DIALOG_PURCHASES_TAG);
            FragmentManager manager = getFragmentManager();

            if(manager != null)
                dialogAddNewType.show(manager,Keys.DialogTags.DIALOG_ADD_NEW_TYPE_TAG);

        });
    }

    void setStrDate(@NotNull String strDate){
        this.strDate = strDate;
        String [] dataSQL = strDate.split("/");
        strDate = dataSQL[2] + "/" + dataSQL[1] + "/" + dataSQL[0];
        txtDate.setText(strDate);
    }

    void setStrTime(String strTime){
        this.strTime = strTime;
        txtTime.setText(strTime);
    }

    public void setCategory(@NotNull Category cat){
        this.category = cat;
        txtType.setText(cat.getName());
    }
}