package com.example.keepmoneyv3.ui.movements;

import android.annotation.SuppressLint;
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
import com.example.keepmoneyv3.database.*;
import com.example.keepmoneyv3.utility.DefaultListViewItems;
import com.example.keepmoneyv3.utility.ApplicationTags;
import com.example.keepmoneyv3.utility.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * This fragment displays the list of incomes or purchases of the user.
 *
 * @author Giacomo Detomaso and Michelangelo De Pascale
 * */
public class IncomesAndPurchasesTabFragment extends Fragment {

    private int sort;

    /**
     * This method describes what happens when the fragment is created
     * */
    @SuppressLint("SetTextI18n")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movements_incomes_and_purchases_list_view,container,false);

        // get the arguments sent by the adapter
        Bundle bundle = getArguments();
        assert bundle != null;
        final int position = bundle.getInt(ApplicationTags.SerializableTags.POSITION_KEY);

        ListView listView = root.findViewById(R.id.listViewTab);
        Context context = getContext();
        assert context != null;
        ArrayListViewAdapter listAdapter = new ArrayListViewAdapter(getContext());

        User user = (User) bundle.getSerializable(ApplicationTags.SerializableTags.USERNAME_KEY);
        String username = user.getUsername();

        //this fragment is a page into the view pager inside the LogActivity
        final int PURCHASE_LIST_PAGE = 0;
        final int INCOMES_LIST_PAGE = 1;

        switch (position){
            case PURCHASE_LIST_PAGE:
                Button bP = root.findViewById(R.id.button2);

                int sizeP = bundle.getInt(ApplicationTags.SerializableTags.PURCHASES_ROWS_KEY);

                if(sizeP > 0){
                    buildPurchaseListView(listAdapter, username);
                    deletePurchase(listView, listAdapter, user);
                    bP.setOnClickListener(view -> {
                        switch(sort) {
                            case 0:
                                bP.setText("Ordinamento per: prezzo crescente");
                                sort++;
                            break;
                            case 1:
                                bP.setText("Ordinamento per: prezzo decrescente");
                                sort++;
                                break;
                            case 2:
                                bP.setText("Ordinamento per: più recente");
                                sort = 0;
                                break;
                        }
                        ArrayListViewAdapter listAdapterSort = new ArrayListViewAdapter(getContext());
                        buildPurchaseListView(listAdapterSort, username);
                        listView.setAdapter(listAdapterSort);
                    });
                } else {
                    Toast.makeText(getContext(), "Non sono presenti spese semplici", Toast.LENGTH_SHORT).show();
                }
                break;

            case INCOMES_LIST_PAGE:
                Button bI = root.findViewById(R.id.button2);
                int sizeE = bundle.getInt(ApplicationTags.SerializableTags.INCOMES_ROWS_KEY);

                if(sizeE > 0){
                    buildIncomesListView(listAdapter, username);
                    deleteIncome(listView, listAdapter, user);
                    bI.setOnClickListener(view -> {
                        switch(sort) {
                            case 0:
                                bI.setText("Ordinamento per: prezzo crescente");
                                sort++;
                                break;
                            case 1:
                                bI.setText("Ordinamento per: prezzo decrescente");
                                sort++;
                                break;
                            case 2:
                                bI.setText("Ordinamento per: più recente");
                                sort = 0;
                                break;
                        }
                        ArrayListViewAdapter listAdapterSort = new ArrayListViewAdapter(getContext());
                        buildIncomesListView(listAdapterSort, username);
                        listView.setAdapter(listAdapterSort);
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
     * This method builds the purchases' ListView.
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
        Cursor cursor = dbManager.getPurchasesItemsQuery(ITEMS_LIMIT, ApplicationTags.MiscellaneousTags.NOT_CONFIRMED, username);

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

    /**
     * This method builds the incomes' ListView.
     *
     * @param adapter       the adapter of the ListView
     * @param username      the username
     * */
    private void buildIncomesListView(ArrayListViewAdapter adapter, String username){
            int picId, incomeID;
            float value;
            String date;

            DbManager dbManager = new DbManager(getContext());
            Cursor cursor = dbManager.getPurchasesDataQueryByUsername(username);

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

    /**
     * This method deletes the selected purchase from the database, updating the user total
     * according to the deleted purchase information.
     *
     * @param listView      the listView
     * @param listAdapter   the listAdapter
     * @param user          the user
     * */
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
                                    long affectedRows = dbManager.removePurchase(itemId, purchaseId);

                                    if(affectedRows > 0) {
                                        requireActivity().getSupportFragmentManager().popBackStack();
                                    } else {
                                        Toast.makeText(getContext(), "Problemi nella rimozione della spesa!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                    });
            alertDialog.show();
        });
    }

    /**
     * This method deletes the selected income from the database, updating the user total
     * according to the deleted income information.
     *
     * @param listView      the listView
     * @param listAdapter   the listAdapter
     * @param user          the user
     * */
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
                                long affectedRows = dbManager.removeIncome(itemId);

                                if(affectedRows > 0) {
                                    requireActivity().getSupportFragmentManager().popBackStack();
                                } else {
                                    Toast.makeText(getContext(), "Problemi nel rimuovere la spesa!", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(getContext(), "Impossibile rimuovere la spesa, saldo negativo!", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
            alertDialog.show();
        });
    }

    /**
     * This method updates the user budget after a purchase is deleted.
     *
     * @param itemId        the id of the item
     * @param user          the user
     * */
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

    /**
     * This method updates the user budget after an income is deleted.
     *
     * @param incomeId      the id of the income
     * @param user          the user
     * */
    private int removeMoneyFromUser(int incomeId, User user){
        int canBeRemoved = 1;
        DbManager dbManager2 = new DbManager(getContext());
        Cursor cursor2 = dbManager2.queryGetIncomeValueFromItemId(incomeId);
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

