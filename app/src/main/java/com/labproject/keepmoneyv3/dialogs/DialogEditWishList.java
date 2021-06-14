package com.labproject.keepmoneyv3.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.activities.NavigationActivity;
import com.labproject.keepmoneyv3.adapters.ExpandableListViewAdapter;
import com.labproject.keepmoneyv3.database.*;
import com.labproject.keepmoneyv3.utility.DefaultListViewItems;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;
import com.labproject.keepmoneyv3.utility.WishLists;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to edit the item of a WishList.
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 * */
public class DialogEditWishList extends DialogFragment {

    /**
     * @see NavigationActivity
     * */
    public interface DialogEditWishListListener{
        User GetUserFromSavedBundle();
    }

    private DialogEditWishListListener listener;

    /**
     * This method attach the listener to the NavigationActivity.
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {
            listener = (DialogEditWishListListener) context;//casting the interface
        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    /**
     * This method is used to create the dialog that will display the ExpandableListView
     * that contains all the items of a wishlist.
     * */
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();//get the layout inflater
        View root = inflater.inflate(R.layout.fragment_movements_wish_lists_expandable_list_view, null);
        builder.setView(root);
        builder.setTitle(ApplicationTags.DialogTitles.DIALOG_EDIT_WISH_LIST_TITLE);

        User user = listener.GetUserFromSavedBundle();
        ArrayList<WishLists>wishLists = new ArrayList<>();
        getWishListData(wishLists, user.getUsername());

        if(wishLists.size() > 0){
            ArrayList<String> wlTitles = new ArrayList<>();
            HashMap<String, ArrayList<DefaultListViewItems>> mapList = new HashMap<>();

            buildDefaultWishListItemsMap(wishLists, mapList);
            getWLTitles(wishLists, wlTitles);

            ExpandableListView expandableListView = root.findViewById(R.id.simpleExpandableListView);
            ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(getContext(), mapList, wlTitles);
            expandableListView.setAdapter(adapter);

            expandableListView.setOnChildClickListener((expandableListView1, view, groupPosition, childPosition, l) -> {
                DefaultListViewItems defaultListViewItems = adapter.getChild(groupPosition, childPosition);
                DialogEditWishListElement dialogEditWishListElement = new DialogEditWishListElement(defaultListViewItems);
                dialogEditWishListElement.show(getParentFragmentManager(), ApplicationTags.DialogTags.DIALOG_EDIT_WISH_LIST_ELEMENT_TAG);
                dismiss();
                return true;
            });
            requireActivity().getSupportFragmentManager().popBackStack();
        } else {
            Toast.makeText(getContext(), "Errore nel recuperare le liste!", Toast.LENGTH_SHORT).show();
        }

        return builder.create();
    }

    /**
     * This method is used to get the most important data of all the WishLists that are not confirmed.
     * */
    private void getWishListData(ArrayList<WishLists>wishLists, String username) {
        DbManager manager = new DbManager(getContext());

        Cursor cursor = manager.getWishListDataQuery(username, ApplicationTags.MiscellaneousTags.NOT_CONFIRMED);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int wlId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_ID));
                String listName = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_DESC));
                int isConfirmed = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_IS_CONFIRMED));
                wishLists.add(new WishLists(wlId, listName, description, isConfirmed));
            }
        }
    }

    /**
     * This method builds the HashMap that needs to be send to the ExpandableWishListAdapter.
     *
     * @param confirmedWl       the wishlist to populate
     * @param map               the map to populate
     * */
    private void buildDefaultWishListItemsMap(@NotNull ArrayList<WishLists> confirmedWl, HashMap<String, ArrayList<DefaultListViewItems>> map){
        DbManager dbManager = new DbManager(getContext());

        for(int i = 0; i < confirmedWl.size(); i++){
            Cursor cursor = dbManager.getWishListsItems(confirmedWl.get(i).getId());
            ArrayList<DefaultListViewItems> defaultListViewItems = new ArrayList<>();

            if(cursor != null) {
                while (cursor.moveToNext()){
                    String itemName = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_NAME));
                    float itemPrice = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_PRICE));
                    int amount = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_AMOUNT));
                    int picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                    int id = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_ID));

                    defaultListViewItems.add(new DefaultListViewItems(id, itemName, picId, itemPrice * amount));
                }
            }

            map.put(confirmedWl.get(i).getName(), defaultListViewItems); // build the map

        }

    }

    /**
     * This method is used to get the titles of the ExpandableWishListAdapter.
     *
     * @param wishLists         the wishlists used to retrieve its list names
     * @param titles            the arraylist populated with the names of all wishlists
     * */
    private void getWLTitles(@NotNull ArrayList<WishLists>wishLists, ArrayList<String>titles){
        for (WishLists wl : wishLists){
            titles.add(wl.getName());
        }
    }
}
