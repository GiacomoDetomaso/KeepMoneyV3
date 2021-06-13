package com.example.keepmoneyv3.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.utility.Item;
import com.example.keepmoneyv3.utility.ApplicationTags;

import java.util.ArrayList;

/**
 * This class is used to display the form to set the name and the description of a WishList.
 *
 * @author Giacomo Detomaso
 * */
public class DialogAddNameToWishList extends DialogFragment {
    public interface DialogAddNameToWishListListener{
        void WishListInsert(ArrayList<Item>items, String listName, String listDescription);
    }

    private View root;
    private final ArrayList<Item> listItems;
    private DialogAddNameToWishListListener listener;

    /**
     * @see NavigationActivity
     * */
    DialogAddNameToWishList(ArrayList<Item>listItems){
        this.listItems = listItems;
    }

    /**
     * This method attach the listener to the NavigationActivity.
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DialogAddNameToWishListListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    /**
     * This method create a dialog to set the name and the description of a wishlist.
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_wish_lists_dialog_add_name, null);
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_ADD_WISH_LIST_TITLE);

        btnAddListAction();

        return builder.create();
    }

    /**
     * Set the name to the wishlist as a result of an event triggered by the
     * onClickListener of btnAddList.
     * */
    private void btnAddListAction(){
        Button btnAddList = root.findViewById(R.id.btnAddList);

        btnAddList.setOnClickListener(view -> {
            EditText txtListName = root.findViewById(R.id.txtListName);
            EditText txtDesc = root.findViewById(R.id.txtDesc);

            String strListName = txtListName.getText().toString();
            String strDesc = txtDesc.getText().toString();

            if(!strListName.isEmpty() && !strDesc.isEmpty()){
                listener.WishListInsert(listItems, strListName, strDesc);
                dismiss();
            }
        });
    }
}
