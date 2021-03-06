package com.labproject.keepmoneyv3.dialogs;

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

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.activities.NavigationActivity;
import com.labproject.keepmoneyv3.adapters.GridViewCategoryAdapter;
import com.labproject.keepmoneyv3.utility.Category;
import com.labproject.keepmoneyv3.utility.ApplicationTags;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This class is used to display the recycler view to select a category.
 *
 * @author Michelangelo De Pascale
 * */
public class DialogAddNewType extends DialogFragment {

    private DialogAddNewTypeListener listener;
    private final String dialogTag;

    /**
     * @see NavigationActivity
     * */
    public interface DialogAddNewTypeListener {
        void onTypeChosenIncomes(Category cat);
        void onTypeChoosePurchases(Category cat);
        void onTypeChooseWishListItem(Category cat);
    }

    /**
     * This method attach the listener to the NavigationActivity.
     * */
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

    /**
     * This method is used to build the GridView layout.
     *
     * @return the dialog
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        View root = inflater.inflate(R.layout.fragment_dashboard_dialog_category, null);//inflate the layout of the view with this new layout

        GridView gridView = root.findViewById(R.id.gridViewCategories);//find the grid view
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_ADD_NEW_TYPE_TITLE);

        GridViewCategoryAdapter gridViewCategoryAdapter = new GridViewCategoryAdapter(getContext());//instantiate the image adapter

        buildGrid(gridViewCategoryAdapter);
        gridView.setAdapter(gridViewCategoryAdapter);//set the adapter for the grid view

        gridViewAction(gridView, gridViewCategoryAdapter);
        return builder.create();
    }

    /**
     * This method is used to build the grid of the GridView.
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
     * selected category title on the EditText.
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
                case ApplicationTags.DialogTags.DIALOG_INCOME_TAG:
                    listener.onTypeChosenIncomes(categoryArrayList.get(position));
                    dismiss();//close the dialog
                    break;
                case ApplicationTags.DialogTags.DIALOG_PURCHASES_TAG:
                    listener.onTypeChoosePurchases(categoryArrayList.get(position));
                    dismiss();//close the dialog
                    break;
                case ApplicationTags.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG:
                    listener.onTypeChooseWishListItem(categoryArrayList.get(position));
                    dismiss();
            }
        });
    }
}