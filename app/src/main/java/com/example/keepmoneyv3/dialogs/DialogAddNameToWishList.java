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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.utility.Item;
import com.example.keepmoneyv3.utility.Keys;

import java.util.ArrayList;


public class DialogAddNameToWishList extends DialogFragment {
    public interface DialogAddNameToWishListListener{
        void WishListInsert(ArrayList<Item>items, String listName, String listDescription);
    }

    private View root;
    private final ArrayList<Item> listItems;
    private DialogAddNameToWishListListener listener;

    DialogAddNameToWishList(ArrayList<Item>listItems){
        this.listItems = listItems;
    }

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

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        root = inflater.inflate(R.layout.fragment_wish_lists_dialog_add_name, null);
        builder.setView(root);
        builder.setTitle(Keys.DialogTitles.DIALOG_ADD_WISH_LIST_TITLE);

        Toast.makeText(getContext(), listItems.get(0).getCatID(), Toast.LENGTH_SHORT).show();

        btnAddListAction();

        return builder.create();
    }

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
