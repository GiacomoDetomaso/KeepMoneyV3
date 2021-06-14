package com.labproject.keepmoneyv3.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.labproject.keepmoneyv3.ui.movements.IncomesAndPurchasesTabFragment;
import com.labproject.keepmoneyv3.ui.movements.WishListsTabFragment;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;
import com.labproject.keepmoneyv3.utility.WishLists;

import java.util.ArrayList;

/**
 * This adapter is used to build the three tab fragments used to visualize the
 * user movements.
 * */
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

    /**
     * This method, that overrides the standard one, returns the proper tab fragment according to the position passed to it.
     *
     * @return the proper tab fragment according to the position passed to it
     * */
    @NonNull
    @Override
    public Fragment getItem(int position) {

        final int PURCHASE_LIST_PAGE = 0;
        final int ENTRIES_LIST_PAGE = 1;
        final int WL_LIST_PAGE = 2;

        // declaring a new standard fragment
        Fragment tabFragment = new Fragment();
        Bundle args = new Bundle();

        // send the correct data to the CollectionTabFragment according to the selected tab
        switch (position){
            case PURCHASE_LIST_PAGE:
                tabFragment = new IncomesAndPurchasesTabFragment(); // tabFragment is now created as an IncomeAndPurchasesTabFragment

                // inserting in args (a Bundle) all the information that we have to pass to the TabFragment, to then allow processing using the user's information
                args.putInt(ApplicationTags.SerializableTags.PURCHASES_ROWS_KEY,simplePurchasesRows);
                args.putInt(ApplicationTags.SerializableTags.POSITION_KEY,PURCHASE_LIST_PAGE);
                args.putSerializable(ApplicationTags.SerializableTags.USERNAME_KEY, user);

                // passing these information to the fragment
                tabFragment.setArguments(args);
                break;
            case ENTRIES_LIST_PAGE:
                tabFragment = new IncomesAndPurchasesTabFragment();

                args.putSerializable(ApplicationTags.SerializableTags.INCOMES_ROWS_KEY,incomesRows);
                args.putInt(ApplicationTags.SerializableTags.POSITION_KEY,ENTRIES_LIST_PAGE);
                args.putSerializable(ApplicationTags.SerializableTags.USERNAME_KEY, user);

                tabFragment.setArguments(args);
                break;
            case WL_LIST_PAGE:
                tabFragment = new WishListsTabFragment();

                tabFragment.setArguments(args);
                args.putSerializable(ApplicationTags.SerializableTags.WISH_LIST_KEYS, confirmedWishLists);
                args.putInt(ApplicationTags.SerializableTags.POSITION_KEY,WL_LIST_PAGE);
                args.putSerializable(ApplicationTags.SerializableTags.USERNAME_KEY, user);
        }

        return tabFragment;
    }

    /**
     * This method, that overrides the standard one, returns the amount of the TabFragments expected. (Expected 3: Entrate, Uscite, Wishlist)
     * */
    @Override
    public int getCount() {
        return 3;
    }

    /**
     * This method, that overrides the standard one, returns the name of a TabFragment, according to the position integer that is passed to this function.
     * */
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
