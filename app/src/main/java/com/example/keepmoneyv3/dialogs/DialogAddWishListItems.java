package com.example.keepmoneyv3.dialogs;

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

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Item;
import com.example.keepmoneyv3.utility.Keys;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DialogAddWishListItems extends DialogFragment {
    private View root;
    private float subTotal;
    private final ArrayList<Item> listItems;
    private Category category;
    private EditText txtCategoryItemList;

    public DialogAddWishListItems() {
        listItems = new ArrayList<>();
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_wish_lists_dialog_add_items, null);
        builder.setView(root);
        builder.setTitle(Keys.DialogTitles.DIALOG_ADD_WISH_LIST_TITLE);

        txtCategoryItemList = root.findViewById(R.id.txtCategoryItemList);
        subTotal = 0;

        txtCategoryAction(txtCategoryItemList);
        btnAddNewItemAction();
        btnNextAction();

        return builder.create();
    }

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
                float costWl;
                String itemName = txtItem.getText().toString();
                try {
                    amount = Integer.parseInt(txtAmountWL.getText().toString());
                    costWl = Float.parseFloat(txtCostWL.getText().toString());
                    listItems.add(new Item(itemName, amount, Keys.MiscellaneousKeys.NOT_CONFIRMED, costWl, category.getId()));

                    subTotal = subTotal + (costWl * amount);

                    String message = "- Subtotale: " + subTotal + "€\n"
                            + "- Numero di elementi: " + listItems.size();

                    txtNumberItems.setText(message);

                    for (EditText editText : fields) {
                        editText.setText(""); // set empty text
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
     * Used to switch to the next Dialog
     *
     * @see DialogAddNameToWishList
     */
    private void btnNextAction() {
        Button button = root.findViewById(R.id.btnNext);
        button.setOnClickListener(view -> {
            if (listItems.size() > 0) {
                DialogFragment dialogFragment = new DialogAddNameToWishList(listItems);
                dialogFragment.show(getParentFragmentManager(), Keys.DialogTags.DIALOG_ADD_NAME_TO_WISH_LIST_TAG);
                dismiss();
            } else {
                Toast.makeText(getContext(), "Nessun oggetto inserito", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void txtCategoryAction(@NotNull EditText txtType) {
        txtType.setOnClickListener(v -> {
            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.queryGetAllRows(DbStrings.TableCategoriesFields.TABLE_NAME);
            ArrayList<Category> allCategories = new ArrayList<>();

            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_ID));
                String desc = cursor.getString(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_DESC));
                int picid = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                allCategories.add(new Category(id, desc, picid));
            }

            DialogAddNewType dialogAddNewType = new DialogAddNewType(allCategories, Keys.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
            FragmentManager manager = getFragmentManager();

            if (manager != null)
                dialogAddNewType.show(manager, Keys.DialogTags.DIALOG_ADD_NEW_TYPE_TAG);

        });
    }

    public void setCategory(@NotNull Category cat){
        this.category = cat;
        txtCategoryItemList.setText(cat.getName());
    }
}

