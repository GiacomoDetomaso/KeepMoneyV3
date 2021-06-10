package com.example.keepmoneyv3.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.dialogs.DialogAddNameToWishList;
import com.example.keepmoneyv3.dialogs.DialogAddNewType;
import com.example.keepmoneyv3.dialogs.DialogAddWishListItems;
import com.example.keepmoneyv3.dialogs.DialogEditWishList;
import com.example.keepmoneyv3.dialogs.DialogIncome;
import com.example.keepmoneyv3.dialogs.DialogPurchase;
import com.example.keepmoneyv3.ui.dashboard.DashboardFragment;
import com.example.keepmoneyv3.ui.movements.MovementsFragment;
import com.example.keepmoneyv3.ui.wishlist.WishListsFragment;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Item;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * This class is the "hub" of the app. It is used to navigate through the various menus tabs
 * and it performs all the database insert queries or update queries related to:
 *
 * 1) Incomes
 * 2) Items
 * 3) Purchases
 * 4) WishLists
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 **/

public class NavigationActivity extends AppCompatActivity implements DialogAddNewType.DialogAddNewTypeListener,
        DialogIncome.DialogIncomeListener, DialogPurchase.DialogPurchaseListener, DashboardFragment.DashboardFragmentListener,
        WishListsFragment.WishListsFragmentListener, DialogAddNameToWishList.DialogAddNameToWishListListener,
        MovementsFragment.MovementsFragmentListener, DialogEditWishList.DialogEditWishListListener {

    private User user; // the user passed as a bundle from login or registration

    /**
     * This method describes what happens when the Activity is created
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        Bundle bundle = getIntent().getExtras();
        user = (User) bundle.getSerializable(Keys.SerializableKeys.USER_KEY);

    }

    /**
     * This method is triggered when the btnAddNewMoney is tapped.
     *
     * @param view      the view of the dialog
     * */
    public void addMoneyEvent(View view) {
        DialogFragment dialogFragment = new DialogIncome();
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_INCOME_TAG);
    }

    /**
     * This method is triggered when the fabAddNewPurchase is tapped.
     *
     * @param view      the view of the dialog
     * */
    public void addPurchaseEvent(View view){
        DialogFragment dialogFragment = new DialogPurchase(user.getTotal());
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_PURCHASES_TAG);
    }

    /**
     * This method is triggered when the fabAddNewWishList is tapped.
     *
     * @param view      the view of the dialog
     * */
    public void fabAddWishListAction(View view){
        DialogFragment dialogFragment = new DialogAddWishListItems();
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
    }

    public void fabStatAction(View view){
        Toast.makeText(getApplicationContext(), "La funzione di statistiche sui movimenti verrà implementata presto", Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the income acquisition
     *
     * @param cat       category
     * @see DialogIncome
     * */
    @Override
    public void onTypeChosenIncomes(Category cat) {
        DialogIncome dialogIncome = (DialogIncome) getSupportFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_INCOME_TAG);
        if(dialogIncome != null)
            dialogIncome.setCategory(cat);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the purchase acquisition
     *
     * @param cat       category
     *
     * */
    @Override
    public void onTypeChoosePurchases(Category cat) {
        DialogPurchase dialogPurchase = (DialogPurchase) getSupportFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_PURCHASES_TAG);
        if(dialogPurchase != null)
            dialogPurchase.setCategory(cat);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the addition of a new item in the wishlist
     *
     * @param cat       category
     *
     * */
    @Override
    public void onTypeChooseWishListItem(Category cat) {
        DialogAddWishListItems dialogAddWishListItems = (DialogAddWishListItems) getSupportFragmentManager().findFragmentByTag(Keys.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
        if(dialogAddWishListItems != null)
            dialogAddWishListItems.setCategory(cat);
    }

    /**
     * Callback method that saves the income inside the database
     *
     * @param val       the value of the income
     * @param date      the date of the income
     * @param idCat     the id of the income's category
     *
     * @see DialogIncome.DialogIncomeListener */
    @Override
    public void DialogIncomeInsert(float val, String date, String idCat) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertIncome(val, date, idCat, user.getUsername()); // save the entry inside the DB

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
     * @param item      the item bought
     * @param date      the date of the purchase
     * @param time      the time of the purchase
     *
     * @see com.example.keepmoneyv3.dialogs.DialogPurchase.DialogPurchaseListener
     * */
    @Override
    public void DialogPurchaseInsert(@NotNull Item item, String date, String time) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertItems(item.getPrice(), item.getAmount(), item.getName(), item.getValid(), item.getCatID());
        int listId = 0;

        if(testValue > 0){
            item.setId((int) testValue);
            testValue = dbManager.insertPurchases(date, time, user.getUsername(), item.getId(), listId);

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
     * Callback method to insert the WishList in the database
     *
     * @param items             the items of the wishlist
     * @param listName          name of the list
     * @param listDescription   description of the list
     * */
    @Override
    public void WishListInsert(@NotNull ArrayList<Item> items, String listName, String listDescription) {
        DbManager dbManager = new DbManager(getApplicationContext());
        int listId = (int) dbManager.insertWishLists(listName, listDescription, Keys.MiscellaneousKeys.NOT_CONFIRMED);

        if(listId > 0) {
            for (int i = 0; i < items.size(); i++) {
                // insert items
                int itemId = (int) dbManager.insertItems(items.get(i).getPrice(), items.get(i).getAmount(),
                        items.get(i).getName(), items.get(i).getValid(), items.get(i).getCatID());

                // insert purchases
                if (itemId > 0) {
                    dbManager.insertPurchaseSimple(user.getUsername(), itemId, listId);
                }
            }
        }

        refreshActivity();
    }

    /**
     * This method is used to communicate with the application's fragments.
     * It send to them the user object
     *
     * @see DashboardFragment
     * @return user object
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

        Bundle bundleRefreshActivity = new Bundle();
        bundleRefreshActivity.putSerializable(Keys.SerializableKeys.USER_KEY, user);

        Intent intent = getIntent().putExtras(bundleRefreshActivity);
        startActivity(intent);// restart the activity

        overridePendingTransition(0, 0);
    }

    /**
     * Change Floating Action Buttons visibility, according to the selected fragment
     * */
    @Override
    public User onWishListsFragmentOpened() {
        FloatingActionButton fabPurchases = findViewById(R.id.fabAddNewPurchase);
        fabPurchases.setVisibility(View.INVISIBLE);

        FloatingActionButton fabStat = findViewById(R.id.fabStats);
        fabStat.setVisibility(View.INVISIBLE);

        FloatingActionButton fabWishList = findViewById(R.id.fabAddNewWishList);
        fabWishList.setVisibility(View.VISIBLE);

        return user;
    }

    /**
     * Change Floating Action Buttons visibility, according to the selected fragment
     * */
    @Override
    public void onMovementsFragmentOpened() {
        FloatingActionButton fabPurchases = findViewById(R.id.fabAddNewPurchase);
        fabPurchases.setVisibility(View.INVISIBLE);

        FloatingActionButton fabWishList = findViewById(R.id.fabAddNewWishList);
        fabWishList.setVisibility(View.INVISIBLE);

        FloatingActionButton fabStat = findViewById(R.id.fabStats);
        fabStat.setVisibility(View.VISIBLE);

    }

    /**
     * Change Floating Action Buttons visibility, according to the selected fragment
     * */
    @Override
    public void onDashboardFragmentOpened() {
        FloatingActionButton fabPurchases = findViewById(R.id.fabAddNewPurchase);
        fabPurchases.setVisibility(View.VISIBLE);

        FloatingActionButton fabWishList = findViewById(R.id.fabAddNewWishList);
        fabWishList.setVisibility(View.INVISIBLE);

        FloatingActionButton fabStat = findViewById(R.id.fabStats);
        fabStat.setVisibility(View.INVISIBLE);
    }


    /**
     * This method is used to confirm the WishList.
     * It updates the isConfirmed field of the WishList's table
     * and the one of the items related to it. When a WishList is confirmed
     * the budget of the user is updated too.
     *
     * @param listId        the id of the list to confirm
     * @param listTotal     the total of the list
     * */
    @Override
    public void confirmWishList(int listId, float listTotal) {
        DbManager dbManager = new DbManager(getApplicationContext());

        ArrayList<Item>wishListItems = new ArrayList<>();
        Cursor cursor = dbManager.getWishListsItems(listId);
        
        // get all the item related to the wishlist

        if (cursor != null){
            while (cursor.moveToNext()){
                int id = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_ID));
                float price = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_PRICE));
                int amount = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_AMOUNT));
                int isConfirmed = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_IS_CONFIRMED));
                String name = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_NAME));
                String idCat = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_ID_CAT));
                wishListItems.add(new Item(id,name,amount,isConfirmed,price,idCat));//add the item inside the wl
            }
        }

        if(user.getTotal() > listTotal) {
            int purchId = 0;
            // update wishlists
            dbManager.updateAtWishListConfirmation(Keys.MiscellaneousKeys.CONFIRMED, listId);

            String format = "dd/MM/yyyy HH:mm:ss";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = new Date();
            String dateWishList = formatter.format(date).split(" ")[0];
            String timeWishList = formatter.format(date).split(" ")[1];

            // update item and purchases
            for (Item item : wishListItems) {
                cursor = dbManager.queryGetPurchaseIdFromItemId(item.getId());

                if(cursor != null) {
                    while (cursor.moveToNext()) {
                        purchId = cursor.getInt(cursor.getColumnIndex(DbStrings.TablePurchasesFields.PURCH_ID));
                    }
                    dbManager.updatePurchasesDateAndTime(dateWishList, timeWishList, purchId);
                }

                dbManager.updateItemsValidity(Keys.MiscellaneousKeys.CONFIRMED, item.getId());
            }

            // update user total
            user.setTotal(user.getTotal() - listTotal);
            dbManager.updateUserTotal(user.getTotal(), user.getUsername());

            Toast.makeText(getApplicationContext(), "Lista acquistata correttamente", Toast.LENGTH_LONG).show();
            refreshActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Impossibile completare l'acquisto della lista perchè il budget è insufficente", Toast.LENGTH_LONG).show();
        }
    }

}