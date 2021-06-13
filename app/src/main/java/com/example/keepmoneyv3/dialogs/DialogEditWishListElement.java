package com.example.keepmoneyv3.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.*;
import com.example.keepmoneyv3.utility.DefaultListViewItems;
import com.example.keepmoneyv3.utility.ApplicationTags;

import org.jetbrains.annotations.NotNull;

/**
 * This class edits a single element of a Wishlist.
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 * */
public class DialogEditWishListElement extends DialogFragment {
    private final DefaultListViewItems defaultListViewItems;

    /**
     * @see com.example.keepmoneyv3.activities.NavigationActivity
     * */
    public DialogEditWishListElement(DefaultListViewItems defaultListViewItems){
        this.defaultListViewItems = defaultListViewItems;
    }

    private EditText txtCostWLEdit, txtAmountWLEdit;

    /**
     * This method creates the dialog used to edit some information of an item.
     * */
    @NonNull
    @NotNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        View root = inflater.inflate(R.layout.fragment_wish_list_edit, null);
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_ADD_WISH_LIST_TITLE);

        txtCostWLEdit = root.findViewById(R.id.txtCostWLEdit);
        txtAmountWLEdit = root.findViewById(R.id.txtAmountWLEdit);
        Button btnEditWlElement = root.findViewById(R.id.btnEditWLItem);
        btnEditWlElementAction(btnEditWlElement);

        return builder.create();
    }

    /**
     * This method edits price and amount of a WishList's item and save the updated
     * values inside the database.
     * */
    private void btnEditWlElementAction(Button button){
        button.setOnClickListener(view -> {
            String strCostWlEdit = txtCostWLEdit.getText().toString();
            String strAmountWlEdit = txtAmountWLEdit.getText().toString();

            if(!strCostWlEdit.isEmpty() && !strAmountWlEdit.isEmpty()){
                try {
                    float itemPrice = Float.parseFloat(strCostWlEdit);
                    int amount = Integer.parseInt(strAmountWlEdit);

                    DbManager dbManager = new DbManager(getContext());
                    dbManager.updateWishListItemInfo(itemPrice, amount, defaultListViewItems.getId());

                    DialogFragment dialogFragment = new DialogEditWishList();
                    dialogFragment.show(getParentFragmentManager(), ApplicationTags.DialogTags.DIALOG_EDIT_WISH_LIST_TAG);
                    dismiss();
                } catch (Exception e){
                    Toast.makeText(getContext(), "Dati non numerici. Riprovare", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "Campi vuoti!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
