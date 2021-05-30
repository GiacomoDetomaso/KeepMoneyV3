package com.example.keepmoneyv3.ui.wishlist;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.adapters.WishListAdapter;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.example.keepmoneyv3.utility.WishLists;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class WishListsFragment extends Fragment {

    public interface WishListsFragmentListener {
         User onWishListsFragmentOpened();
         void confirmWishList(int listId, float listTotal);
    }

    private WishListsFragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (WishListsFragmentListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        User user = listener.onWishListsFragmentOpened(); // notify the NavigationActivity
        View root = inflater.inflate(R.layout.fragment_wish_lists, container, false);

        final int SPAN_COUNT = 2;
        RecyclerView recyclerView = root.findViewById(R.id.wishListRecyclerView);
        WishListAdapter wishListAdapter = new WishListAdapter(getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
        buildGridView(wishListAdapter, user);
        gridViewItemAction(wishListAdapter,user);

        recyclerView.setAdapter(wishListAdapter);

        return root;
    }

    /**
     * A method used to build the gridview with the WishLists not confirmed yet
     *
     * @param adapter       the adapter of the recycler view
     * */
    private void buildGridView(WishListAdapter adapter, @NotNull User user) {
        DbManager manager = new DbManager(getContext());

            Cursor cursor = manager.getWishListDataQuery(user.getUsername(), Keys.MiscellaneousKeys.NOT_CONFIRMED);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                        float sum = cursor.getFloat(cursor.getColumnIndex("tot"));
                        String listName = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_NAME));

                        if(sum > 0)
                            adapter.buildMap(listName, sum);
                        else
                            Toast.makeText(getContext(), "Non sono ancora presenti liste", Toast.LENGTH_LONG).show();
                }
            }
    }


    private AlertDialog.Builder buildWishListOptionsDialog(String name, int listId, float total){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Gestione lista");

        builder.setMessage("Confermare l'acquisto della lista '" + name + "'?");

        //Button One : Yes
        builder.setPositiveButton("Si", (dialog, which) -> listener.confirmWishList(listId, total));

        //Button Two : No
        builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        //Button Three : Neutral
        builder.setNeutralButton("Modifica", (dialog, which) -> {
            Toast.makeText(getContext(), "Appare la modifica", Toast.LENGTH_LONG).show();
            dialog.cancel();
        });

        return builder;
    }

    private void gridViewItemAction(@NotNull WishListAdapter wishListAdapter, User user){
        ArrayList<WishLists>wishLists = new ArrayList<>();

        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.getWishListDataQuery(user.getUsername(), Keys.MiscellaneousKeys.NOT_CONFIRMED);
        ArrayList<Float> totals = new ArrayList<>();

        // get all the non confirmed wishlists of the current user
        if (cursor != null){
            while (cursor.moveToNext()) {
                int wlId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_ID));
                String listName = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_NAME));
                String description = cursor.getString(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_DESC));
                int isConfirmed = cursor.getInt(cursor.getColumnIndex(DbStrings.TableWishListsFields.WL_IS_CONFIRMED));
                float sum = cursor.getFloat(cursor.getColumnIndex("tot"));
                wishLists.add(new WishLists(wlId, listName, description, isConfirmed));
                totals.add(sum);
            }
        }

        wishListAdapter.setOnItemClickListener((view, position) -> {
            String listName = wishLists.get(position).getName();
            int listId = wishLists.get(position).getId();

            AlertDialog alertDialog = buildWishListOptionsDialog(listName,listId, totals.get(position)).create();
            alertDialog.show();
        });
    }

}