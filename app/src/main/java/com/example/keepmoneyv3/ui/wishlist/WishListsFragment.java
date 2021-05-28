package com.example.keepmoneyv3.ui.wishlist;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.adapters.WishListAdapter;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.WishLists;

import java.util.ArrayList;

public class WishListsFragment extends Fragment {

    //private WishListsViewModel wishListsViewModel;
    public interface WishListsFragmentListener{
         void onWishListsFragmentOpened();
    }

    private WishListsFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (WishListsFragment.WishListsFragmentListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        listener.onWishListsFragmentOpened(); // notify the NavigationActivity
        View root = inflater.inflate(R.layout.fragment_wish_lists, container, false);

        final int SPAN_COUNT = 2;
        CardView cardViewWishList = root.findViewById(R.id.cardViewWishList);
        RecyclerView recyclerView = cardViewWishList.findViewById(R.id.wishListRecyclerView);
        WishListAdapter wishListAdapter = new WishListAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        buildGridView(wishListAdapter);

        recyclerView.setAdapter(wishListAdapter);
        return root;
    }

    /**
     * A method used to build the gridview with the WishLists not confirmed yet
     *
     * @param adapter       - the adapter of the recycler view
     * */
    private void buildGridView(WishListAdapter adapter) {
        DbManager manager = new DbManager(getContext());
        ArrayList<WishLists>wishLists = new ArrayList<>();
        getAllWishLists(wishLists);

        if(wishLists.size() > 0) {
            int pos = 0;

            do {
                int wlID = wishLists.get(pos).getId();
                int valid = wishLists.get(pos).getIsValid();
                Cursor cursor = manager.getWishListDataQuery(wlID);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        if (valid == Keys.MiscellaneousKeys.NOT_CONFIRMED) {
                            float sum = cursor.getFloat(cursor.getColumnIndex("tot"));
                            String listName = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_NAME));
                            adapter.buildMap(listName, sum);
                        }
                        pos++;//increment the position
                    }
                }
            } while (pos < wishLists.size());
        }
    }

    /**
     * * A method to get all the wish list ind the db
     *
     * @param wl        - WishList arraylist*/
    private void getAllWishLists(ArrayList<WishLists> wl){
        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.queryGetAllRows(DbStrings.TableWishListsFields.TABLE_NAME);

        while (cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_ID));
            String listName = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_NAME));
            String desc = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_DESC));
            int valid = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_VALID));
            if (valid == Keys.MiscellaneousKeys.NOT_CONFIRMED) {
                wl.add(new WishLists(id, listName, desc, valid));
            }
        }
    }


}