package com.labproject.keepmoneyv3.ui.dashboard;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.activities.NavigationActivity;
import com.labproject.keepmoneyv3.adapters.ArrayListViewAdapter;
import com.labproject.keepmoneyv3.database.*;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;


/**
 *  This class is used to create and show the fragment that displays the
 *  dashboard of the app.
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 * */
public class DashboardFragment extends Fragment {

    /**
     * @see NavigationActivity
     * */
    public interface DashboardFragmentListener{
        User GetUserFromSavedBundle();
        void onDashboardFragmentOpened();
    }

    /**
     * An enum used to choose which sum calculate.
     * **/
    private enum ChooseEP{
        INCOMES, PURCHASES
    }

    private DashboardFragmentListener listener;

    /**
     * This method attach the listener to the NavigationActivity.
     * */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        NavigationActivity activity = (NavigationActivity) getActivity();

        try {

            listener = (DashboardFragment.DashboardFragmentListener) context;//casting the interface

        }catch (ClassCastException e){
            throw new ClassCastException((activity != null ? activity.toString() : null) + "Must implement the interface");
        }
    }

    /**
     * This method describes what happens when the fragment is created
     *
     * @return the View
     * */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        listener.onDashboardFragmentOpened(); // notify the NavigationActivity

        /*
         * calling the listener we have in NavigationActivity, it is possible to get the current logged user to grab his balance
         * from the database
         * */
        User user = listener.GetUserFromSavedBundle();
        // using the ListAdapter to build the ListView with the recent purchases
        Context context = getContext();
        assert context != null;

        ArrayListViewAdapter adapter = new ArrayListViewAdapter(context);
        ListView listView = root.findViewById(R.id.listviewRecentPurchases);
        listView.setAdapter(adapter);
        buildListView(adapter, user.getUsername());

        TextView txtToEntriesBox = root.findViewById(R.id.txtBalanceIncomes);
        TextView txtToUscBox = root.findViewById(R.id.txtBalancePurchases);
        TextView txtBudgetBox = root.findViewById(R.id.txtBudget);

        float incomes,purchases;
        incomes = sumIncomesOrPurchases(user.getUsername(),ChooseEP.INCOMES);
        purchases = sumIncomesOrPurchases(user.getUsername(),ChooseEP.PURCHASES);

        String strIncomes = decimalFormat(incomes);
        String strPurchases = decimalFormat(purchases);
        String strTotal = decimalFormat(user.getTotal());

        txtToEntriesBox.setText(strIncomes);
        txtToUscBox.setText(strPurchases);
        txtBudgetBox.setText(strTotal);

        return root;
    }

    /**
     * This method is used to get the sum of the incomes or the purchases
     * for a certain user.
     *
     * @param username      the username
     * @param choice        used to select the sum of the entries or the sum of the purchases
     *
     * @return value        a float number which represents the sum
     * */
    private float sumIncomesOrPurchases(String username, ChooseEP choice){
        DbManager dbManager = new DbManager(getContext());
        Cursor cursor;
        float value = 0;
        switch (choice) {
            case INCOMES:
                cursor = dbManager.sumIncomesQuery(username);
                if (cursor!=null){
                    final String SUM_INC = "sumInc";
                    while(cursor.moveToNext()) {
                        value = cursor.getFloat(cursor.getColumnIndex(SUM_INC));
                    }
                }
                break;
            case PURCHASES:
                cursor = dbManager.sumPurchasesQuery(username, ApplicationTags.MiscellaneousTags.CONFIRMED);
                if (cursor!=null){
                    final String SUM_PURCH = "sumPurch";
                    while(cursor.moveToNext()) {
                        value = cursor.getFloat(cursor.getColumnIndex(SUM_PURCH));
                    }
                }
                break;
            default:
                break;
        }
        return value;
    }


    /**
     * This method builds the ListView with the three most recent purchases.
     *
     * @param adapter       the adapter of the ListView
     * @param username      the username
     * */
    void buildListView(ArrayListViewAdapter adapter, String username) {
        final int RECENT_ITEMS_LIMIT = 3;

        int picId, itemId;
        int amount;
        float itemPrice;
        String itemName;

        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.getPurchasesItemsQuery(RECENT_ITEMS_LIMIT, ApplicationTags.MiscellaneousTags.NOT_CONFIRMED, username);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                itemId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_ID));
                itemName = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_NAME));
                itemPrice = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_PRICE));
                amount = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_AMOUNT));
                picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                adapter.buildMap(itemId, itemName, picId, itemPrice * amount); // build the list view
            }

        } else {
            Toast.makeText(getContext(), "Errore nel reperire le informazioni", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is used to correctly format a numeric value.
     *
     * @param value  the value that needs to be formatted
     * @return       a string representation of the value
     */
    private String decimalFormat(float value){
        DecimalFormat decFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        decFormat.setMaximumFractionDigits(2);
        return decFormat.format(value) + " €";
    }
}