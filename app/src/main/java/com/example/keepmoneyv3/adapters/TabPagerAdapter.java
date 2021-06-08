package com.example.keepmoneyv3.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.keepmoneyv3.ui.movements.IncomesAndPurchasesTabFragment;
import com.example.keepmoneyv3.ui.movements.WishListsTabFragment;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.example.keepmoneyv3.utility.WishLists;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentStatePagerAdapter {

    private final int simplePurchasesRows;
    private final int incomesRows;
    private final User user;
    private final ArrayList<WishLists>confirmedWishLists;

    public TabPagerAdapter(FragmentManager fm, int simplePurchasesRows, int incomesRows, User user, ArrayList<WishLists>confirmedWishLists) {
        super(fm);
        this.simplePurchasesRows = simplePurchasesRows;
        this.incomesRows = incomesRows;
        this.user = user;
        this.confirmedWishLists = confirmedWishLists;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        final int PURCHASE_LIST_PAGE = 0;
        final int ENTRIES_LIST_PAGE = 1;
        final int WL_LIST_PAGE = 2;

        Fragment tabFragment = new Fragment();
        Bundle args = new Bundle();

        // send the correct data to the CollectionTabFragment according to the selected tab
        switch (position){
            case PURCHASE_LIST_PAGE:
                tabFragment = new IncomesAndPurchasesTabFragment();
                args.putInt(Keys.SerializableKeys.PURCHASES_ROWS_KEY,simplePurchasesRows);;
                args.putInt(Keys.SerializableKeys.POSITION_KEY,PURCHASE_LIST_PAGE);
                args.putSerializable(Keys.SerializableKeys.USERNAME_KEY, user);
                tabFragment.setArguments(args);
                break;
            case ENTRIES_LIST_PAGE:
                tabFragment = new IncomesAndPurchasesTabFragment();
                args.putSerializable(Keys.SerializableKeys.INCOMES_ROWS_KEY,incomesRows);
                args.putInt(Keys.SerializableKeys.POSITION_KEY,ENTRIES_LIST_PAGE);
                args.putSerializable(Keys.SerializableKeys.USERNAME_KEY, user);
                tabFragment.setArguments(args);
                break;
            case WL_LIST_PAGE:
                tabFragment = new WishListsTabFragment();
                tabFragment.setArguments(args);
                args.putSerializable(Keys.SerializableKeys.WISH_LIST_KEYS, confirmedWishLists);
                args.putInt(Keys.SerializableKeys.POSITION_KEY,WL_LIST_PAGE);
                args.putSerializable(Keys.SerializableKeys.USERNAME_KEY, user);
        }

        return tabFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String text = "";
        final int PURCHASE_LIST_PAGE = 0;
        final int ENTRIES_LIST_PAGE = 1;
        final int WL_LIST_PAGE = 2;

        // set the title of the tab page
        switch (position){
            case PURCHASE_LIST_PAGE:
                text = "Spese";
                break;
            case ENTRIES_LIST_PAGE:
                text = "Entrate";
                break;
            case WL_LIST_PAGE:
                text = "Liste";
        }

        return text;
    }
}
