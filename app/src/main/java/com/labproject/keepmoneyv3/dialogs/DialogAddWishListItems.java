package com.labproject.keepmoneyv3.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.database.*;
import com.labproject.keepmoneyv3.utility.Category;
import com.labproject.keepmoneyv3.utility.Item;
import com.labproject.keepmoneyv3.utility.ApplicationTags;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class is used during the wishlist creation to add all its items.
 *
 * @author Giacomo Detomaso
 */

public class DialogAddWishListItems extends DialogFragment {
    private View root;
    private float subTotal;
    private final ArrayList<Item> listItems;
    private Category category;
    private EditText txtCategoryItemList;

    public DialogAddWishListItems() {
        listItems = new ArrayList<>();
    }

    /**
     * This method is used to build the layout of the dialog used to finalize the wishlist.
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_wish_lists_dialog_add_items, null);
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_ADD_WISH_LIST_TITLE);

        txtCategoryItemList = root.findViewById(R.id.txtCategoryItemList);
        subTotal = 0;

        txtCategoryAction(txtCategoryItemList);
        btnAddNewItemAction();
        btnNextAction();
        btnReadReceiptAction();

        return builder.create();
    }

    /**
     * This method adds a new item to a wishlist.
     * Requirements:
     *  1 - EditText must be filled
     *  2 - The numeric values must be correct
     *
     * If this two conditions are true, the DialogAddWishListItems will be dismissed and
     * the DialogAddNameToWishList will be called, to finalize the process.
     * */
    private void btnAddNewItemAction() {
        Button button = root.findViewById(R.id.btnAddNewItem);

        button.setOnClickListener(view -> {
            boolean isCorrect = true;

            EditText txtItem = root.findViewById(R.id.txtItem);
            EditText txtCostWL = root.findViewById(R.id.txtCostWL);
            EditText txtAmountWL = root.findViewById(R.id.txtAmountWL);
            TextView txtNumberItems = root.findViewById(R.id.txtNumberItems);

            EditText[] fields = {txtItem, txtCostWL, txtAmountWL, txtCategoryItemList};

            for (EditText editText : fields) {
                if (editText.getText().toString().equals(""))
                    isCorrect = false; // isCorrect is false if one of the EditText is empty
                break;
            }

            // check the integer and float fields
            if (isCorrect) {
                int amount;
                float itemCost;
                String itemName = txtItem.getText().toString();
                try {
                    amount = Integer.parseInt(txtAmountWL.getText().toString());
                    itemCost = Float.parseFloat(txtCostWL.getText().toString());

                    if(itemCost > 0 && amount > 0) {
                        listItems.add(new Item(itemName, amount, ApplicationTags.MiscellaneousTags.NOT_CONFIRMED, itemCost, category.getId()));

                        subTotal = subTotal + (itemCost * amount);

                        String message = "- Subtotale: " + subTotal + "€\n"
                                + "- Numero di elementi: " + listItems.size();

                        txtNumberItems.setText(message);

                        for (EditText editText : fields) {
                            editText.setText(""); // set empty text
                        }
                    } else {
                        Toast.makeText(getContext(), "Costo dell'oggetto inserito nullo", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Inserire dei valori numerici!", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getContext(), "Campi vuoti!", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * This method will perform the receipt acquisition by launching a camera activity intent
     * */
    private void btnReadReceiptAction(){
        Button btnAddReceipt = root.findViewById(R.id.btnReadReceipt);
        btnAddReceipt.setOnClickListener(view -> Toast.makeText(getContext(), "La funzione di lettura degli scontrini verrà implementata presto",
                Toast.LENGTH_LONG).show());
    }

    /**
     * Method used to switch to the next Dialog.
     *
     * @see DialogAddNameToWishList
     */
    private void btnNextAction() {
        Button button = root.findViewById(R.id.btnNext);
        button.setOnClickListener(view -> {
            if (listItems.size() > 0) {
                DialogFragment dialogFragment = new DialogAddNameToWishList(listItems);
                dialogFragment.show(getParentFragmentManager(), ApplicationTags.DialogTags.DIALOG_ADD_NAME_TO_WISH_LIST_TAG);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Nessun oggetto inserito", Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * Method used to set the category of the item.
     * */
    private void txtCategoryAction(@NotNull EditText txtType) {
        txtType.setOnClickListener(v -> {
            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.queryGetAllRows(DbStrings.TableCategoriesFields.TABLE_NAME);
            ArrayList<Category> allCategories = new ArrayList<>();

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_ID));
                String desc = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_DESC));
                int picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                allCategories.add(new Category(id, desc, picId));
            }

            DialogAddNewType dialogAddNewType = new DialogAddNewType(allCategories, ApplicationTags.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
            FragmentManager manager = requireActivity().getSupportFragmentManager();
            dialogAddNewType.show(manager, ApplicationTags.DialogTags.DIALOG_ADD_NEW_TYPE_TAG);
        });
    }

    /**
     * Writes the category in its EditText.
     * */
    public void setCategory(@NotNull Category cat){
        this.category = cat;
        txtCategoryItemList.setText(cat.getName());
    }
}


