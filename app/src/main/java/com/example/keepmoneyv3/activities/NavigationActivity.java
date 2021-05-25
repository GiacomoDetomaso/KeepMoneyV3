package com.example.keepmoneyv3.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.dialogs.DialogAddNewType;
import com.example.keepmoneyv3.dialogs.DialogEntries;
import com.example.keepmoneyv3.dialogs.DialogPurchase;
import com.example.keepmoneyv3.ui.dashboard.DashboardFragment;
import com.example.keepmoneyv3.ui.wishlist.WishListsFragment;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Items;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

/**
 * This class is the "hub" of the app. It is used to navigate through the various menus tabs
 * and it performs all the database insert queries or update queries related to
 *
 * 1) Entries
 * 2) Items
 * 3) Purchases
 * 4) WishLists
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 **/

public class NavigationActivity extends AppCompatActivity implements DialogAddNewType.DialogAddNewTypeListener,
        DialogEntries.DialogEntriesListener, DialogPurchase.DialogPurchaseListener, DashboardFragment.DashboardFragmentListener,
        WishListsFragment.WishListsFragmentListener {

    private User user; // the user passed as a bundle from login or registration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        //navView.inflateMenu(R.menu.bottom_nav_menu);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movements, R.id.navigation_dashboard, R.id.navigation_list)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(Keys.SerializableKeys.USER_KEY);

    }

    /**
     * This method is triggered when the btnAddNewMoney is tapped.
     *
     * @param view      - the view of the dialog
     * */
    public void addMoneyEvent(View view) {
        DialogFragment dialogFragment = new DialogEntries();
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_ENTRIES_TAG);
    }

    /**
     * This method is triggered when the fabAddNewPurchase is tapped.
     *
     * @param view      - the view of the dialog
     * */
    public void addPurchaseEvent(View view){
        DialogFragment dialogFragment = new DialogPurchase(user.getTotal()); // todo fix
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_PURCHASES_TAG);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the entry acquisition
     *
     * @param cat       - category
     * @see DialogEntries
     * */
    @Override
    public void onTypeChosenEntries(Category cat) {
        DialogEntries dialogEntries = (DialogEntries) getSupportFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_ENTRIES_TAG);
        if(dialogEntries != null)
            dialogEntries.setCategory(cat);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the purchase acquisition
     *
     * @param cat       - category
     *
     * */
    @Override
    public void onTypeChoosePurchases(Category cat) {
        DialogPurchase dialogPurchase = (DialogPurchase) getSupportFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_PURCHASES_TAG);
        if(dialogPurchase != null)
            dialogPurchase.setCategory(cat);
    }

    /**
     * Callback method that saves the entry inside the database
     *
     * @param val       - the value of the entry
     * @param date      - the date of the entry
     * @param idCat     - the id of the entry's category
     *
     * @see com.example.keepmoneyv3.dialogs.DialogEntries.DialogEntriesListener*/
    @Override
    public void DialogEntriesInsert(float val, String date, String idCat) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertEntries(val, date, idCat, user.getUsername()); // save the entry inside the DB

        // check if the entry has been saved
        if (testValue > 0) {
            user.setTotal(user.getTotal() + val);
            dbManager.updateUserTotal(user.getTotal(), user.getUsername());
            refreshActivity();
        }
    }

    /**
     * Callback method that saved the purchase inside the database
     *
     * @param item      - the item bought
     * @param date      - the date of the purchase
     * @param time       - the time of the purchase
     *
     * @see com.example.keepmoneyv3.dialogs.DialogPurchase.DialogPurchaseListener
     * */
    @Override
    public void DialogPurchaseInsert(Items item, String date, String time) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertItems(item.getPrice(), item.getAmount(), item.getName(), item.getValid(), item.getCatID());

        if(testValue > 0){
            item.setId((int) testValue);
            testValue = dbManager.insertPurchases(date, time, user.getUsername(), item.getId(), Keys.MiscellaneousKeys.NO_WL_DEFAULT);

            if (testValue > 0){
                Toast.makeText(getApplicationContext(), "Spesa registrata correttamente", Toast.LENGTH_LONG).show();
                // update total value
                float purchasePrice = item.getPrice() * item.getAmount();
                user.setTotal(user.getTotal() - purchasePrice);
                dbManager.updateUserTotal(user.getTotal(), user.getUsername());
                refreshActivity();
            }
        }

    }

    /**
     * This method is used to communicate with DashboardFragment. It send the user
     * object to the fragment
     *
     * @see DashboardFragment
     * */
    @Override
    public User GetUserFromSavedBundle(){
        return user;
    }


    /**
     * This method is used to refresh the activity once a new movement has been registered
     * */
    private void refreshActivity() {
        finish(); // end the current activity
        overridePendingTransition(0, 0);
        startActivity(getIntent()); // restart the activity
        overridePendingTransition(0, 0);
    }

    @Override
    public void onWishListsFragmentOpened() {
        FloatingActionButton fabPurchases = findViewById(R.id.fabAddNewPurchase);
        fabPurchases.setVisibility(View.INVISIBLE);

        FloatingActionButton fabWishList = findViewById(R.id.fabAddNewWishList);
        fabWishList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDashboardFragmentOpened() {
        FloatingActionButton fabPurchases = findViewById(R.id.fabAddNewPurchase);
        fabPurchases.setVisibility(View.VISIBLE);

        FloatingActionButton fabWishList = findViewById(R.id.fabAddNewWishList);
        fabWishList.setVisibility(View.INVISIBLE);
    }

}