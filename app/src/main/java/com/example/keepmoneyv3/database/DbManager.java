package com.example.keepmoneyv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DbManager {
    private final DbHelper dbHelper;
    private final Context context;

    public DbManager(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    public long insertUser(String username, String password, String nome, String cognome, String email, float val) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableUsersField.USERS_ID,username);
        contentValues.put(DbStrings.TableUsersField.USERS_PWD,password);
        contentValues.put(DbStrings.TableUsersField.USERS_NOME,nome);
        contentValues.put(DbStrings.TableUsersField.USERS_COGNOME,cognome);
        contentValues.put(DbStrings.TableUsersField.USERS_EMAIL, email);
        contentValues.put(DbStrings.TableUsersField.USERS_TOT, val);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableUsersField.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public void insertCategories(String id, String desc, int picId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_ID,id);
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_DESC,desc);
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_PICID,picId);

        try {
            db.insert(DbStrings.TableCategoriesFields.TABLE_NAME, null, contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public long insertItems(String desc,float prezzo,int quant,int valid,String idCat){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_VALID,valid);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_DESC,desc);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_PREZZO,prezzo);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_QUANT,quant);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_IDCAT,idCat);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableItemsFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public long insertEntries(float val,String data,String idCat,String idUser){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_VAL,val);
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_DATA,data);
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_ID_CAT,idCat);
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_ID_USER,idUser);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableEntriesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public long insertWishLists(String denom, String desc, int valid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableWishListsFields.WL_DENOM,denom);
        contentValues.put(DbStrings.TableWishListsFields.WL_DESC,desc);
        contentValues.put(DbStrings.TableWishListsFields.WL_VALID, valid);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableWishListsFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public long insertPurchases(String dataP,String oraP,String idUser,int idItem,int idWl){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_DATA,dataP);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ORA,oraP);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ID_USER,idUser);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ITEM_ID,idItem);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_WL_ID,idWl);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TablePurchasesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public long insertPurchaseSimple(String idUser,int idItem,int idWl){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ID_USER,idUser);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ITEM_ID,idItem);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_WL_ID,idWl);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TablePurchasesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    public void updateUserTotal(String table,float tot,String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "username = '" + username + "'";//the where clause
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableUsersField.USERS_TOT,tot);

        try {
             db.update(table,contentValues,whereClause,null);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void updateItemsValidity(int valid,int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = " id = " + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_VALID,valid);

        try {
            db.update(DbStrings.TableItemsFields.TABLE_NAME,contentValues,whereClause,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    public void updateWishListsValidity(int valid,int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = " id = " + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_VALID,valid);

        try {
            db.update(DbStrings.TableWishListsFields.TABLE_NAME,contentValues,whereClause,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /*public void updateDateAndTimePurchases(int id,String date,String time){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "id = " + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_DATA,date);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ORA,time);

        try {
            db.update(DbStrings.TablePurchasesFields.TABLE_NAME,contentValues,whereClause,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }*/

    public Cursor queryGetUserTotal(String username){
        String query = "SELECT totale FROM " + DbStrings.TableUsersField.TABLE_NAME + " WHERE username = '" + username + "'";
        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor queryGetAllRows(String table){
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.query(table,null,null,null,null,null,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor countQuery(String table){
        String query = "SELECT COUNT(*) AS numRighe FROM " + table;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return cursor;
    }

    public Cursor countQueryWLElements(int wlId){
        String query = "SELECT COUNT(*) AS numRighe " +
                "FROM purchases JOIN wishlists ON purchases.listId = wishLists.id " +
                "WHERE purchases.listId = " + wlId;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return cursor;
    }

    public Cursor sumEntriesQuery(){
        String query = "SELECT SUM(entries.valore) AS sommaEntr FROM entries";
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return cursor;
    }

    public Cursor sumPurchasesQuery(){
        String query = "SELECT SUM(items.prezzo) AS sommaP FROM items WHERE isValid = 0";
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor getLastItemFromPurchasesQuery(int itemID){
        String query =
                "SELECT items.descrizione,items.prezzo,categories.picid " +
                "FROM items JOIN categories ON categories.id = items.idcat" +
                        " WHERE items.id = " + itemID + ";";
        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
            //Toast.makeText(context,query,Toast.LENGTH_LONG).show();
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor getEntriesDataQuery(int entriesId){
        String query = "SELECT entries.valore,entries.dataEntr,categories.picid " +
                "FROM entries JOIN categories ON entries.idcat = categories.id " +
                "WHERE entries.id = " + entriesId + ";";
        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor getWishListDataQuery(int wlID){
        String query = "SELECT SUM(items.prezzo) AS tot, categories.picid, wishLists.denominazione " +
                "FROM purchases JOIN wishLists ON purchases.listId = wishLists.id " +
                "JOIN items ON items.id = purchases.itemId  " +
                "JOIN categories ON categories.id = items.idcat " +
                "WHERE wishLists.id = " + wlID + ";";
        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor getWishListsItems(int wlID){
        String query = "SELECT items.* " +
                "FROM purchases JOIN wishlists ON purchases.listId = wishLists.id " +
                "JOIN items ON items.id = purchases.itemId  " +
                "WHERE wishLists.id = " + wlID + ";";//+ " LIMIT 1";
        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    //count the number of wl by their validity
    public Cursor getCountWishListsByValidity(int valid){
        String query = "SELECT COUNT(*) AS cont FROM wishlists WHERE isValid = " + valid;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }
}
