package com.example.keepmoneyv3.ui.movements;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.adapters.ExpandableListViewAdapter;
import com.example.keepmoneyv3.database.*;
import com.example.keepmoneyv3.utility.DefaultListViewItems;
import com.example.keepmoneyv3.utility.ApplicationTags;
import com.example.keepmoneyv3.utility.WishLists;


import java.util.ArrayList;
import java.util.HashMap;

/**
 * This fragment displays the list of the wishlists of the user.
 *
 * @author Giacomo Detomaso
 * */
public class WishListsTabFragment extends Fragment {

    /**
     * This method describes what happens when the fragment is created
     * */
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movements_wish_lists_expandable_list_view, container, false);
        Bundle bundle = getArguments();

        if(bundle != null){
            ArrayList<WishLists>confirmedWl = (ArrayList<WishLists>) bundle.getSerializable(ApplicationTags.SerializableTags.WISH_LIST_KEYS);
            ArrayList<String> wlTitles = new ArrayList<>();
            HashMap<String, ArrayList<DefaultListViewItems>> mapList = new HashMap<>();

            if (confirmedWl.size() > 0){
                buildDefaultWishListItemsMap(confirmedWl, mapList);
                getWLTitles(confirmedWl, wlTitles);

                ExpandableListView expandableListView = root.findViewById(R.id.simpleExpandableListView);
                ExpandableListViewAdapter adapter = new ExpandableListViewAdapter(getContext(), mapList, wlTitles);
                expandableListView.setAdapter(adapter);

            } else {
                Toast.makeText(getContext(), "Non sono ancora state acquistate delle liste", Toast.LENGTH_LONG).show();
            }

        }
        return root;
    }

    /**
     * Builds the HashMap to send to the ExpandableWishListAdapter.
     *
     * @param confirmedWl       the wishlist to populate
     * @param map               the map to populate
     * */
    private void buildDefaultWishListItemsMap(ArrayList<WishLists>confirmedWl, HashMap<String, ArrayList<DefaultListViewItems>> map){
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
     * Get the titles of the ExpandableWishListAdapter.
     *
     * @param wishLists         the wishlists used to retrieve its list names
     * @param titles            the arraylist populated with the names of all wishlists
     * */
    private void getWLTitles(ArrayList<WishLists>wishLists,ArrayList<String>titles){
        for (WishLists wl : wishLists){
            titles.add(wl.getName());
        }
    }

}
