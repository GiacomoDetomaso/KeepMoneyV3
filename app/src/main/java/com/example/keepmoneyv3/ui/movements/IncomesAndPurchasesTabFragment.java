package com.example.keepmoneyv3.ui.movements;

import android.app.AlertDialog;
import android.content.Context;
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
import com.example.keepmoneyv3.adapters.ArrayListViewAdapter;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.DefaultListViewItems;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;


public class IncomesAndPurchasesTabFragment extends Fragment {

    private int sort;


    public IncomesAndPurchasesTabFragment (int sort) {
        this.sort = sort;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movements_incomes_and_purchases_list_view,container,false);

        // get the arguments sent by the adapter
        Bundle bundle = getArguments();
        assert bundle != null;
        final int position = bundle.getInt(Keys.SerializableKeys.POSITION_KEY);

        ListView listView = root.findViewById(R.id.listViewTab);
        Context context = getContext();
        assert context != null;
        ArrayListViewAdapter listAdapter = new ArrayListViewAdapter(getContext());

        User user = (User) bundle.getSerializable(Keys.SerializableKeys.USERNAME_KEY);
        String username = user.getUsername();

        //this fragment is a page into the view pager inside the LogActivity
        final int PURCHASE_LIST_PAGE = 0;
        final int INCOMES_LIST_PAGE = 1;

        switch (position){
            case PURCHASE_LIST_PAGE:
                Button bP = root.findViewById(R.id.button2);

                int sizeP = bundle.getInt(Keys.SerializableKeys.PURCHASES_ROWS_KEY);

                if(sizeP > 0){
                    buildPurchaseListView(listAdapter, username);
                    deletePurchase(listView, listAdapter, user);
                    bP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                    if(sort < 2) {
                                        sort++;
                                    } else {
                                        sort = 0;
                                    }
                                    ArrayListViewAdapter listAdapterSort = new ArrayListViewAdapter(getContext());
                                    buildPurchaseListView(listAdapterSort, username);
                                    listView.setAdapter(listAdapterSort);
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Non sono presenti spese semplici", Toast.LENGTH_SHORT).show();
                }
                break;

            case INCOMES_LIST_PAGE:
                Button bI = root.findViewById(R.id.button2);
                int sizeE = bundle.getInt(Keys.SerializableKeys.INCOMES_ROWS_KEY);

                if(sizeE > 0){
                    buildIncomesListView(listAdapter, username);
                    deleteIncome(listView, listAdapter, user);
                    bI.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(sort < 2) {
                                sort++;
                            } else {
                                sort = 0;
                            }
                            ArrayListViewAdapter listAdapterSort = new ArrayListViewAdapter(getContext());
                            buildIncomesListView(listAdapterSort, username);
                            listView.setAdapter(listAdapterSort);
                        }
                    });

                } else {
                    Toast.makeText(getContext(),"Nono sono presenti ancora entrate",Toast.LENGTH_SHORT).show();
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
    void buildPurchaseListView(ArrayListViewAdapter adapter, String username) {
        final int ITEMS_LIMIT = 0; // no limit

        int picId, itemId;
        int amount;
        float itemPrice;
        String itemName;

        DbManager dbManager = new DbManager(getContext());
        Cursor cursor = dbManager.getPurchasesItemsQuery(ITEMS_LIMIT, Keys.MiscellaneousKeys.NOT_CONFIRMED, username);

        if (cursor != null) {
            ArrayList<DefaultListViewItems> listToOrder = new ArrayList<>();
            while (cursor.moveToNext()) {
                itemId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_ID));
                itemName = cursor.getString(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_NAME));
                itemPrice = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_PRICE));
                amount = cursor.getInt(cursor.getColumnIndex(DbStrings.TableItemsFields.ITEMS_AMOUNT));
                picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                listToOrder.add(new DefaultListViewItems(itemId, itemName, picId, itemPrice * amount));
            }
            adapter.buildMap(listToOrder, sort);
        } else {
            Toast.makeText(getContext(), "Errore nel reperire le informazioni", Toast.LENGTH_LONG).show();
        }
    }

    private void buildIncomesListView(ArrayListViewAdapter adapter, String username){
            int picId, incomeID;
            float value;
            String date;

            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.getEntriesDataQueryByUsername(username);

            if (cursor == null) {
                Toast.makeText(getContext(),"Errore nel reperire le informazioni",Toast.LENGTH_LONG).show();
            } else {
                ArrayList<DefaultListViewItems> listToOrder = new ArrayList<>();
                adapter.notifyDataSetChanged();
                while (cursor.moveToNext()) {
                    incomeID = cursor.getInt(cursor.getColumnIndex(DbStrings.TableIncomesFields.INCOMES_ID));
                    date = cursor.getString(cursor.getColumnIndex(DbStrings.TableIncomesFields.INCOMES_DATE));
                    value = cursor.getFloat(cursor.getColumnIndex(DbStrings.TableIncomesFields.INCOMES_VAL));
                    picId = cursor.getInt(cursor.getColumnIndex(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID));
                    listToOrder.add(new DefaultListViewItems(incomeID, date, picId, value));
                }
                adapter.buildMap(listToOrder, sort);
            }
    }

    private void deletePurchase(@NotNull ListView listView, ArrayListViewAdapter listAdapter, User user){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Conferma eliminazione");
            alertDialog.setMessage("Confermi l'eliminazione della seguente spesa?");

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annulla",
                    (dialog, which) -> dialog.dismiss());

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Conferma",
                    (dialog, which) -> {
                        DefaultListViewItems defaultListViewItem = listAdapter.getItem(position);
                        if(defaultListViewItem != null) {
                            int itemId = defaultListViewItem.getId();
                            DbManager dbManager = new DbManager(getContext());
                            Cursor cursor = dbManager.queryGetPurchaseIdFromItemId(itemId);
                            if (cursor == null) {
                                Toast.makeText(getContext(), "Si è verificato un errore nell'ottenimento delle informazioni necessarie all'eliminazione dell'oggetto", Toast.LENGTH_LONG).show();
                            } else {
                                while (cursor.moveToNext()) {
                                    int purchaseId = cursor.getInt(cursor.getColumnIndex("id"));
                                    addBackMoneyToUser(itemId, user);
                                    dbManager.removePurchase(itemId, purchaseId);

                                    requireActivity().getSupportFragmentManager().popBackStack();
                                }
                            }
                        }
                    });
            alertDialog.show();
        });
    }

    private void deleteIncome(@NotNull ListView listView, ArrayListViewAdapter listAdapter, User user){
        listView.setOnItemClickListener((parent, view, position, id) -> {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
            alertDialog.setTitle("Conferma eliminazione");
            alertDialog.setMessage("Confermi l'eliminazione della seguente entrata?");

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Annulla",
                    (dialog, which) -> dialog.dismiss());

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Conferma",
                    (dialog, which) -> {
                        DefaultListViewItems defaultListViewItem = listAdapter.getItem(position);
                        if(defaultListViewItem != null) {
                            int itemId = defaultListViewItem.getId();
                            DbManager dbManager = new DbManager(getContext());
                            if (removeMoneyFromUser(itemId, user) == 1) {
                                dbManager.removeIncome(itemId);

                                requireActivity().getSupportFragmentManager().popBackStack();

                            } else {
                                Toast.makeText(getContext(), "Impossibile rimuovere la spesa, saldo negativo!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            alertDialog.show();
        });
    }

    private void addBackMoneyToUser(int itemId, User user){
        DbManager dbManager2 = new DbManager(getContext());
        Cursor cursor2 = dbManager2.queryGetCostFromItemId(itemId);
        if (cursor2 == null) {
            Toast.makeText(getContext(),"Si è verificato un errore nell'ottenimento delle informazioni necessarie all'eliminazione dell'oggetto",Toast.LENGTH_LONG).show();
        } else {
            while (cursor2.moveToNext()) {
                user.setTotal(user.getTotal()+cursor2.getFloat(cursor2.getColumnIndex("cost")));
                dbManager2.updateUserTotal(user.getTotal(),user.getUsername());
            }
        }
    }

    private int removeMoneyFromUser(int itemId, User user){
        int canBeRemoved = 1;
        DbManager dbManager2 = new DbManager(getContext());
        Cursor cursor2 = dbManager2.queryGetIncomeValueFromItemId(itemId);
        if (cursor2 == null) {
            Toast.makeText(getContext(),"Si è verificato un errore nell'ottenimento delle informazioni necessarie all'eliminazione dell'oggetto",Toast.LENGTH_LONG).show();
        } else {
            while (cursor2.moveToNext()) {
                float toRemove = cursor2.getFloat(cursor2.getColumnIndex("value"));
                if(toRemove <= user.getTotal()){
                    user.setTotal(user.getTotal()-toRemove);
                    dbManager2.updateUserTotal(user.getTotal(),user.getUsername());
                } else {
                    canBeRemoved = 0;
                }
            }
        }
        return canBeRemoved;
    }
}

