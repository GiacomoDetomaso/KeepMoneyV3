package com.labproject.keepmoneyv3.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.database.DbManager;
import com.labproject.keepmoneyv3.database.DbStrings;
import com.labproject.keepmoneyv3.dialogs.DialogAddNameToWishList;
import com.labproject.keepmoneyv3.dialogs.DialogAddNewType;
import com.labproject.keepmoneyv3.dialogs.DialogAddWishListItems;
import com.labproject.keepmoneyv3.dialogs.DialogEditWishList;
import com.labproject.keepmoneyv3.dialogs.DialogIncome;
import com.labproject.keepmoneyv3.dialogs.DialogPurchase;
import com.labproject.keepmoneyv3.ui.dashboard.DashboardFragment;
import com.labproject.keepmoneyv3.ui.movements.MovementsFragment;
import com.labproject.keepmoneyv3.ui.wishlist.WishListsFragment;
import com.labproject.keepmoneyv3.utility.Category;
import com.labproject.keepmoneyv3.utility.Item;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;
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
 * and it performs all the database insert or update queries related to:
 *
 * 1) Incomes
 * 2) Items
 * 3) Purchases
 * 4) WishLists
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 */

public class NavigationActivity extends AppCompatActivity implements DialogAddNewType.DialogAddNewTypeListener,
        DialogIncome.DialogIncomeListener, DialogPurchase.DialogPurchaseListener, DashboardFragment.DashboardFragmentListener,
        WishListsFragment.WishListsFragmentListener, DialogAddNameToWishList.DialogAddNameToWishListListener,
        MovementsFragment.MovementsFragmentListener, DialogEditWishList.DialogEditWishListListener {

    private User user; // the variable used to store the user passed inside the bundle by the login or the registration


    /**
     * This method, that overrides the standard one, describes what happens when the Activity is created.
     * It is used to get a User object, which contains all the personal data of the logged user.
     *
     */
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
        user = (User) bundle.getSerializable(ApplicationTags.SerializableTags.USER_KEY);

    }

    /**
     * This method is triggered when the btnAddNewMoney is tapped.
     * Allows to create a custom dialog to insert an income.
     *
     * @param view      the view of the dialog
     * */
    public void addMoneyEvent(View view) {
        DialogFragment dialogFragment = new DialogIncome();
        dialogFragment.show(getSupportFragmentManager(), ApplicationTags.DialogTags.DIALOG_INCOME_TAG);
    }

    /**
     * This method is triggered when the fabAddNewPurchase is tapped.
     * Allows to create a custom dialog to insert a purchase.
     *
     * @param view      the view of the dialog
     * */
    public void addPurchaseEvent(View view){
        DialogFragment dialogFragment = new DialogPurchase(user.getTotal());
        dialogFragment.show(getSupportFragmentManager(), ApplicationTags.DialogTags.DIALOG_PURCHASES_TAG);
    }

    /**
     * This method is triggered when the fabAddNewWishList is tapped.
     * Allows to create a custom dialog to insert a wishlist.
     *
     * @param view      the view of the dialog
     * */
    public void fabAddWishListAction(View view){
        DialogFragment dialogFragment = new DialogAddWishListItems();
        dialogFragment.show(getSupportFragmentManager(), ApplicationTags.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
    }

    /**
     * This method is triggered when the fabStats is tapped.
     * It shows a message to warn of a not-yet implemented feature.
     *
     * @param view      the view
     * */
    public void fabStatAction(View view){
        Toast.makeText(getApplicationContext(), "La funzione di statistiche sui movimenti verrà implementata presto", Toast.LENGTH_SHORT).show();
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the income acquisition.
     *
     * @param cat        the selected category
     *
     * @see DialogIncome
     * */
    @Override
    public void onTypeChosenIncomes(Category cat) {
        DialogIncome dialogIncome = (DialogIncome) getSupportFragmentManager().findFragmentByTag(ApplicationTags.DialogTags.DIALOG_INCOME_TAG);
        if(dialogIncome != null)
            dialogIncome.setCategory(cat);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the purchase acquisition.
     *
     * @param cat       the selected category
     *
     * */
    @Override
    public void onTypeChoosePurchases(Category cat) {
        DialogPurchase dialogPurchase = (DialogPurchase) getSupportFragmentManager().findFragmentByTag(ApplicationTags.DialogTags.DIALOG_PURCHASES_TAG);
        if(dialogPurchase != null)
            dialogPurchase.setCategory(cat);
    }

    /**
     * Callback method that send to the parent dialog the name of the
     * selected category, during the addition of a new item in the wishlist.
     *
     * @param cat       the selected category
     *
     * */
    @Override
    public void onTypeChooseWishListItem(Category cat) {
        DialogAddWishListItems dialogAddWishListItems = (DialogAddWishListItems) getSupportFragmentManager().findFragmentByTag(ApplicationTags.DialogTags.DIALOG_ADD_WISH_LIST_ITEMS_TAG);
        if(dialogAddWishListItems != null)
            dialogAddWishListItems.setCategory(cat);
    }

    /**
     * Callback method that saves the income the user is inserting right now inside the database.
     *
     * @param val       the value of the income
     * @param date      the date of the income
     * @param idCat     the id of the income's category
     *
     * @see DialogIncome.DialogIncomeListener
     * */
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
     * Callback method that saves the purchase the user is making right now inside the database.
     *
     * @param item      the item bought
     * @param date      the date of the purchase
     * @param time      the time of the purchase
     *
     * @see com.labproject.keepmoneyv3.dialogs.DialogPurchase.DialogPurchaseListener
     * */
    @Override
    public void DialogPurchaseInsert(@NotNull Item item, String date, String time) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertItems(item.getPrice(), item.getAmount(), item.getName(), item.getValid(), item.getCatID());
        int listId = 0;

        if(testValue > 0){ // only proceed if the insert of the item in the database is successful
            item.setId((int) testValue);
            testValue = dbManager.insertPurchases(date, time, user.getUsername(), item.getId(), listId);

            if (testValue > 0){ // only proceed if the insert of the purchases in the database is successful
                Toast.makeText(getApplicationContext(), "Spesa registrata correttamente", Toast.LENGTH_SHORT).show();
                // update total value
                float purchasePrice = item.getPrice() * item.getAmount();
                user.setTotal(user.getTotal() - purchasePrice);
                dbManager.updateUserTotal(user.getTotal(), user.getUsername());
                refreshActivity();
            }
        }
    }

    /**
     * Callback method to insert a WishList in the database.
     *
     * @param items             the items of the wishlist
     * @param listName          name of the list
     * @param listDescription   description of the list
     * */
    @Override
    public void WishListInsert(@NotNull ArrayList<Item> items, String listName, String listDescription) {
        DbManager dbManager = new DbManager(getApplicationContext());
        int listId = (int) dbManager.insertWishLists(listName, listDescription, ApplicationTags.MiscellaneousTags.NOT_CONFIRMED);

        if(listId > 0) {
            for (int i = 0; i < items.size(); i++) {
                // insert items
                int itemId = (int) dbManager.insertItems(items.get(i).getPrice(), items.get(i).getAmount(),
                        items.get(i).getName(), items.get(i).getValid(), items.get(i).getCatID());

                // insert purchases
                if (itemId > 0) {
                    dbManager.insertWLElementPurchase(user.getUsername(), itemId, listId);
                }
            }
        }

        refreshActivity();
    }

    /**
     * This method is used to send some vital information to the other application's fragments, when required.
     * It send to them the user object.
     *
     * @see DashboardFragment
     * @return user object
     * */
    @Override
    public User GetUserFromSavedBundle(){
        return user;
    }

    /**
     * This method is used to refresh the activity once a new movement has been registered, to update all the values
     * */
    private void refreshActivity() {
        finish(); // end the current activity
        overridePendingTransition(0, 0);

        Bundle bundleRefreshActivity = new Bundle();
        bundleRefreshActivity.putSerializable(ApplicationTags.SerializableTags.USER_KEY, user);

        Intent intent = getIntent().putExtras(bundleRefreshActivity);
        startActivity(intent);// restart the activity

        overridePendingTransition(0, 0);
    }

    /**
     * Method used to change floating action buttons visibility, according to the selected fragment (in this case Wishlist)
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
     * Method used to change floating action buttons visibility, according to the selected fragment (in this case Movements)
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
     * Method used to change floating action buttons visibility, according to the selected fragment (in this case Dashboard)
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
     * This method is used to confirm a WishList.
     * It updates the "isConfirmed" field of the WishList's table row
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
            dbManager.updateAtWishListConfirmation(ApplicationTags.MiscellaneousTags.CONFIRMED, listId);

            String format = "dd/MM/yyyy HH:mm:ss";
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat(format);
            Date date = new Date();
            String dateWishList = formatter.format(date).split(" ")[0];
            String timeWishList = formatter.format(date).split(" ")[1];

            // updating the validity of the items in the wishlist and entering the date on which it was actually bought
            for (Item item : wishListItems) {
                cursor = dbManager.queryGetPurchaseIdFromItemId(item.getId());

                if(cursor != null) {
                    while (cursor.moveToNext()) {
                        purchId = cursor.getInt(cursor.getColumnIndex(DbStrings.TablePurchasesFields.PURCH_ID));
                    }
                    dbManager.updatePurchasesDateAndTime(dateWishList, timeWishList, purchId);
                }

                dbManager.updateItemsValidity(ApplicationTags.MiscellaneousTags.CONFIRMED, item.getId());
            }

            // update user total, subtracting the wishlist total
            user.setTotal(user.getTotal() - listTotal);
            dbManager.updateUserTotal(user.getTotal(), user.getUsername());

            Toast.makeText(getApplicationContext(), "Lista acquistata correttamente", Toast.LENGTH_SHORT).show();
            refreshActivity();
        } else {
            Toast.makeText(getApplicationContext(), "Impossibile completare l'acquisto della lista perchè il budget è insufficente!", Toast.LENGTH_LONG).show();
        }
    }

}