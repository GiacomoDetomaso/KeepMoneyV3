package com.example.keepmoneyv3.ui.movements;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.activities.NavigationActivity;
import com.example.keepmoneyv3.adapters.TabPagerAdapter;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.example.keepmoneyv3.utility.WishLists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MovementsFragment extends Fragment {

    public interface MovementsFragmentListener{
        User GetUserFromSavedBundle();
        FloatingActionButton onMovementsFragmentOpened();
    }

    private MovementsFragment.MovementsFragmentListener listener;
    private int sort = 0;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {
            listener = (MovementsFragment.MovementsFragmentListener) context;//casting the interface
        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movements, container, false);

        User user = listener.GetUserFromSavedBundle();
        FloatingActionButton fab= listener.onMovementsFragmentOpened();


        // tab pager data
        String username = user.getUsername();
        int incomesRows = getIncomesRows(username);
        int simplePurchasesRows = getSimplePurchasesRows(username);
        ArrayList<WishLists>confirmedWishLists = new ArrayList<>();
        getWishListData(confirmedWishLists, username);

        // set the tab pager
        ViewPager viewPager = root.findViewById(R.id.pager);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getParentFragmentManager(), simplePurchasesRows, incomesRows, user, confirmedWishLists, sort, fab);


        viewPager.setAdapter(tabPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    int getIncomesRows(String username){
        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.countIncomesRowsByUsername(username);
        final String NUM_ROWS_FIELD = "numRows";
        int numRows = 0;

        if(cursor != null){
            while (cursor.moveToNext()){
                numRows = cursor.getInt(cursor.getColumnIndex(NUM_ROWS_FIELD));
            }
        }
        return numRows;
    }

    int getSimplePurchasesRows(String username){
        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.countSimplePurchasesRowsByUsername(username);
        final String NUM_ROWS_FIELD = "numRows";
        int numRows = 0;

        if(cursor != null){
            while (cursor.moveToNext()){
                numRows = cursor.getInt(cursor.getColumnIndex(NUM_ROWS_FIELD));
            }
        }
        return numRows;
    }

    /**
     *
     * */
    private void getWishListData(ArrayList<WishLists>wishLists, String username) {
        DbManager manager = new DbManager(getContext());

        Cursor cursor = manager.getWishListDataQuery(username, Keys.MiscellaneousKeys.CONFIRMED);
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

}