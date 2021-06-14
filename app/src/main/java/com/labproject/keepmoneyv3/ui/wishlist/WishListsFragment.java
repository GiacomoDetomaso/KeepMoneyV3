package com.labproject.keepmoneyv3.ui.wishlist;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.activities.NavigationActivity;
import com.labproject.keepmoneyv3.adapters.WishListAdapter;
import com.labproject.keepmoneyv3.database.DbManager;
import com.labproject.keepmoneyv3.database.DbStrings;
import com.labproject.keepmoneyv3.dialogs.DialogEditWishList;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;
import com.labproject.keepmoneyv3.utility.WishLists;

import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

/**
 * This class is used to add and confirm a wishlist
 *
 * @author Giacomo Detomaso
 * */
public class WishListsFragment extends Fragment {

    /**
     * @see NavigationActivity
     * */
    public interface WishListsFragmentListener {
         User onWishListsFragmentOpened();
         void confirmWishList(int listId, float listTotal);
    }

    private WishListsFragmentListener listener;

    /**
     * This method attach the listener to the NavigationActivity
     * */
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

    /**
     * This method describes what happens when the fragment is created
     *
     * @return the number of rows of the income
     * */
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

            Cursor cursor = manager.getWishListDataQuery(user.getUsername(), ApplicationTags.MiscellaneousTags.NOT_CONFIRMED);
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


    /**
     * This method builds the AlertDialog used to modify or confirm the list
     *
     * @param name      the name of the wishlist
     * @param total     the total of the wishlist
     * @param listId    the id of the wishlist
     *
     * @return the builder of the Alert dialog
     * */
    @NotNull
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
            DialogFragment dialogFragment = new DialogEditWishList();
            dialogFragment.show(getParentFragmentManager(), ApplicationTags.DialogTags.DIALOG_EDIT_WISH_LIST_TAG);
            dialog.cancel();
        });

        return builder;
    }

    /**
     * Shows the AlertDialog described in the method buildWishListOptionsDialog
     * */
    private void gridViewItemAction(@NotNull WishListAdapter wishListAdapter, @NotNull User user){
        ArrayList<WishLists>wishLists = new ArrayList<>();

        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.getWishListDataQuery(user.getUsername(), ApplicationTags.MiscellaneousTags.NOT_CONFIRMED);
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