package com.example.keepmoneyv3.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.adapters.GridViewCategoryAdapter;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Keys;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DialogAddNewType extends DialogFragment {

    private DialogAddNewTypeListener listener;
    private final String dialogTag;

    public interface DialogAddNewTypeListener {
        void onTypeChosenEntries(Category cat);
        void onTypeChoosePurchases(Category cat);
        void onTypeChooseWishListItem(Category cat);
    }

    @Override
    public void onAttach(@NotNull Context context){
        super.onAttach(context);

        NavigationActivity navigationActivity = (NavigationActivity) getActivity();
            try {
                listener = (DialogAddNewTypeListener) context;//casting the interface for home activity
            }catch (ClassCastException e){
                throw new ClassCastException((navigationActivity != null ? navigationActivity.toString() : null) + "Must implement the interface");
            }
    }


    private final ArrayList<Category> categories;

   public DialogAddNewType(ArrayList<Category> categories, String dialogTag){
        this.categories = categories;
        this.dialogTag = dialogTag;
    }


    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        View root = inflater.inflate(R.layout.fragment_dashboard_dialog_category, null);//inflate the layout of the view with this new layout

        GridView gridView = root.findViewById(R.id.gridViewCategories);//find the grid view
        builder.setView(root);
        builder.setTitle(Keys.DialogTitles.DIALOG_ADD_NEW_TYPE_TITLE);

        GridViewCategoryAdapter gridViewCategoryAdapter = new GridViewCategoryAdapter(getContext());//instantiate the image adapter

        buildGrid(gridViewCategoryAdapter);
        gridView.setAdapter(gridViewCategoryAdapter);//set the adapter for the grid view

        gridViewAction(gridView, gridViewCategoryAdapter);
        return builder.create();
    }

    /**
     * This method is used to build the grid of the gridview
     *
     * @param gridViewCategoryAdapter       the adapter of the gridview
     *
     * @see GridViewCategoryAdapter
     * @see Category*/
    private void buildGrid(GridViewCategoryAdapter gridViewCategoryAdapter){
        for (Category c : categories){
            gridViewCategoryAdapter.buildMap(c.getId(),c.getName(),c.getPictureId());
        }
    }

    /**
     * This method is called when an item of the gridview is tapped.
     * It is used to call the interface's method, in order to set the corresponding
     * category title on the EditText.
     *
     * @param gridView      the gridview
     * @param adapter       the adapter of the gridview
     *
     * @see DialogAddNewTypeListener
     * @see NavigationActivity*/
    private void gridViewAction(GridView gridView, final GridViewCategoryAdapter adapter){
        gridView.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<Category> categoryArrayList = adapter.getCategories();
            switch (dialogTag) {
                case Keys.DialogTags.DIALOG_INCOME_TAG:
                    listener.onTypeChosenEntries(categoryArrayList.get(position));
                    dismiss();//close the dialog
                    break;
                case Keys.DialogTags.DIALOG_PURCHASES_TAG:
                    listener.onTypeChoosePurchases(categoryArrayList.get(position));
                    dismiss();//close the dialog
                    break;
                case Keys.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG:
                    listener.onTypeChooseWishListItem(categoryArrayList.get(position));
                    dismiss();
            }
        });
    }
}