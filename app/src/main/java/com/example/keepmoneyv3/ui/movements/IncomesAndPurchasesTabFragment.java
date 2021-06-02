package com.example.keepmoneyv3.ui.movements;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.adapters.ListAdapter;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Keys;

public class IncomesAndPurchasesTabFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movements_incomes_and_purchases_list_view,container,false);

        // get the arguments sent by the adapter
        Bundle bundle = getArguments();
        assert bundle != null;
        final int position = bundle.getInt(Keys.SerializableKeys.POSITION_KEY);

        ListView listView = root.findViewById(R.id.listViewTab);
        ListAdapter listAdapter = new ListAdapter(getContext());
        String username = bundle.getString(Keys.SerializableKeys.USERNAME_KEY);

        //this fragment is a page into the view pager inside the LogActivity
        final int PURCHASE_LIST_PAGE = 0;
        final int INCOMES_LIST_PAGE = 1;

        switch (position){
            case PURCHASE_LIST_PAGE:

                int sizeP = bundle.getInt(Keys.SerializableKeys.PURCHASES_ROWS_KEY);

                if(sizeP > 0){
                    buildPurchaseListView(listAdapter, username);
                    Button button = root.findViewById(R.id.button2);
                    button.setOnClickListener(view -> {
                        // todo sort arraylist
                    });
                } else {
                    Toast.makeText(getContext(), "Non sono presenti spese semplici", Toast.LENGTH_SHORT).show();
                }
                break;

            case INCOMES_LIST_PAGE:
                int sizeE = bundle.getInt(Keys.SerializableKeys.INCOMES_ROWS_KEY);

                if(sizeE > 0){
                    buildEntriesListView(listAdapter, username);
                } else {
                    Toast.makeText(getContext(),"Nono sono presenti ancora entrate",Toast.LENGTH_LONG).show();
                }
                break;
        }

        listView.setAdapter(listAdapter);

        return root;
    }

    /**
     * This method builds the ListView with the three recent purchases
     *
     * @param adapter       the adapter of the ListView
     * @param username      the username
     * */
    void buildPurchaseListView(ListAdapter adapter, String username) {
        final int ITEMS_LIMIT = 0; // no limit

        int picId;
        int amount;
        float itemPrice;
        String itemName;

        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.getPurchasesItemsQuery(ITEMS_LIMIT, Keys.MiscellaneousKeys.NOT_CONFIRMED, username);

        if (cursor != null) {

            while (cursor.moveToNext()) {
                itemName = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_NAME));
                itemPrice = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_PRICE));
                amount = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_AMOUNT));
                picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                adapter.buildMap(itemName, picId, itemPrice * amount); // build the list view
            }

        } else {
            Toast.makeText(getContext(), "Errore nel reperire le informazioni", Toast.LENGTH_LONG).show();
        }
    }

    private void buildEntriesListView(ListAdapter adapter, String username){
            int picId;
            float value;
            String date;

            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.getEntriesDataQueryByUsername(username);

            if (cursor == null) {
                Toast.makeText(getContext(),"Errore nel reperire le informazioni",Toast.LENGTH_LONG).show();
            } else {

                while (cursor.moveToNext()) {
                    date = cursor.getString(cursor.getColumnIndex(DbStrings.TableIncomesFields.INCOMES_DATE));
                    value = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableIncomesFields.INCOMES_VAL));
                    picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                    adapter.buildMap(date, picId, value);
                }
            }
    }

    /*private void listViewPurchasesAction(ListView listView){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Purchase purchase = confirmedPurchases.get(i);
                //Toast.makeText(getContext(),"i " + i + "id: " + purchase.getId(),Toast.LENGTH_LONG).show();
                DialogPurchasesInfo dialogPurchasesInfo = new DialogPurchasesInfo(purchase);
                FragmentManager fragmentManager = getFragmentManager();
                assert fragmentManager != null;
                dialogPurchasesInfo.show(fragmentManager,"DialogPurchasesInfo");
            }
        });
    }*/
}

