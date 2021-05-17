package com.example.keepmoneyv3.activities;

import android.os.Bundle;
import android.view.View;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.dialogs.DialogAddNewType;
import com.example.keepmoneyv3.dialogs.DialogEntries;
import com.example.keepmoneyv3.utility.Category;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Objects;

public class NavigationActivity extends AppCompatActivity implements DialogAddNewType.DialogAddNewTypeListener,
        DialogEntries.DialogEntriesListener {

    private User user;

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

    public void addMoneyEvent(View view) {
        DialogFragment dialogFragment = new DialogEntries();
        dialogFragment.show(getSupportFragmentManager(), Keys.DialogTags.DIALOG_ENTRIES_TAG);
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

    }

    /**
     * Callback method that saves the entry inside the database
     *
     * @param val       - the value of the entry
     * @param date      - the date of the entry
     * @param idCat     - the id of the entry's category*/
    @Override
    public void DialogEntriesInsert(float val, String date, String idCat) {
        DbManager dbManager = new DbManager(getApplicationContext());
        long testValue = dbManager.insertEntries(val, date, idCat, user.getUsername()); // save the entry inside the DB

        // check if the entry has been saved
        if (testValue > 0) {
            user.setTotal(user.getTotal() + val);
            dbManager.updateUserTotal(user.getTotal(), user.getUsername());
        }
    }
}