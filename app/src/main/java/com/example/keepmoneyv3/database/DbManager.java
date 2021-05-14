package com.example.keepmoneyv3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

/**
 * This is the Database manager. Its methods perform all the queries used int the application
 *
 * @author Giacomo Detomaso
 *
 * @see DbHelper        - used to get the DB
 * @see DbStrings       - used to access to all the tables
 * @see ContentValues   - used to set the values of the queries
 * @see Cursor          - used to provide the result of some queries
 * @see SQLiteDatabase  - used to perform queries
 * */
public class DbManager {
    private final DbHelper dbHelper;
    private final Context context;

    public DbManager(Context context){
        this.context = context;
        dbHelper = new DbHelper(context);
    }

    /**
     * This method is used to insert an user inside the database
     *
     *
     * @param username  - identifies an user uniquely
     * @param password  - the password of the user account
     * @param name      - the name of the user
     * @param surname   - the surname of the user
     * @param email     - email of the user
     * @param total     - total amount of money of the user */
    public long insertUser(String username, String password, String name, String surname, String email, float total) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableUsersField.USERS_ID,username);
        contentValues.put(DbStrings.TableUsersField.USERS_PWD,password);
        contentValues.put(DbStrings.TableUsersField.USERS_NAME,name);
        contentValues.put(DbStrings.TableUsersField.USERS_SURNAME,surname);
        contentValues.put(DbStrings.TableUsersField.USERS_EMAIL, email);
        contentValues.put(DbStrings.TableUsersField.USERS_TOT, total);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableUsersField.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    /**
     * This method is used to insert a category inside the database
     *
     *
     * @param id        - identifies a category
     * @param desc      - brief description of the category
     * @param picId     - identifies the picture associated with the category*/
    public void insertCategories(String id, String desc, int picId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_ID,id);
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_DESC,desc);
        contentValues.put(DbStrings.TableCategoriesFields.CATEGORIES_PIC_ID,picId);

        try {
            db.insert(DbStrings.TableCategoriesFields.TABLE_NAME, null, contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is used to insert an item inside the database
     *
     *
     * @param price     - the price of the item
     * @param amount    - indicates the amount item
     * @param name      - the name of the item
     * @param valid     - indicates if the item has been bought (valid = 1) or it's planned to (valid = 0)
     * @param idCat     - identifies the category of the item
     * */
    public long insertItems(float price, int amount, String name, int valid, String idCat){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_VALID,valid);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_NAME ,name);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_PRICE,price);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_AMOUNT,amount);
        contentValues.put(DbStrings.TableItemsFields.ITEMS_ID_CAT,idCat);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableItemsFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    /**
     * This method is used to insert an entry inside the database
     *
     *
     * @param val       - the value of the entry
     * @param date      - the date of the entry
     * @param idCat     - the category of the entry
     * @param idUser    - the id of the user*/
    public long insertEntries(float val, String date, String idCat, String idUser){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_VAL,val);
        contentValues.put(DbStrings.TableEntriesFields.ENTRIES_DATE,date);
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

    /**
     * This method is used to insert a wishlist inside the database
     *
     *
     * @param name      - the name of the list
     * @param desc      - a description of the list
     * @param valid     - indicates if the item has been bought (valid = 1) or it's planned to (valid = 0)
     * */
    public long insertWishLists(String name, String desc, int valid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableWishListsFields.WL_NAME,name);
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

    /**
     * This method is used to insert a purchase inside the database
     *
     *
     * @param dateP     - the date of the purchase
     * @param timeP     - the time of the purchase
     * @param idUser    - the id of the user
     * @param idItem    - the id of the item
     * @param idWl      - the id of the wishlist
     * */
    public long insertPurchases(String dateP, String timeP, String idUser, int idItem, int idWl){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_DATE, dateP);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_TIME, timeP);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ID_USER, idUser);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ITEM_ID, idItem);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_WL_ID, idWl);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TablePurchasesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    /**
     * This method is used to insert a simple purchase inside the database. A simple purchase
     * is a purchase with no information about the date and the time. It's used when
     * you define a wishlist, where you can't have the mentioned information when it is created.
     * Date and time will be added only when a wishlist will be bought.
     *
     **/
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

    /**
     * This method is used to update the total value of the user
     *
     *
     * @param table     - table name
     * @param tot       - total value
     * @param username  - username
     * */
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

    /**
     * This method is used to update the validity of an item
     *
     *
     * @param valid     - validity of the item
     * @param id        - item's ID
     * */
    public void updateItemsValidity(int valid, int id){
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

    /**
     * This method is used to update the validity of the WL
     *
     *
     * @param valid     - validity of the list
     * @param id        - WL ID
     * */
    public void updateWishListsValidity(int valid, int id){
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

    /**
     * A query to check if the login credentials are correct
     *
     * @param username  - identifies an user uniquely
     * @param password  - the password of the user account
     *
     * */
    public Cursor queryCheckUserLogin(String username, String password){
        String query = "SELECT users.* FROM users " +
                "WHERE username = '" + username + "' AND password = '" + password + "';";

        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    /**
     * This query is used to retrieve the user's total
     *
     * @param username  - identifies an user uniquely
     **/
    public Cursor queryGetUserTotal(String username){
        String query = "SELECT " + DbStrings.TableUsersField.USERS_TOT + " " +
                        "FROM " + DbStrings.TableUsersField.TABLE_NAME + " " +
                        "WHERE username = '" + username + "'";

        Cursor cursor = null;

        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    /**
     * Used to get all the rows of a table
     *
     * @param table         - the name of the table to access
     **/
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

    /**
     * Used to count the number of rows of a table without any specific constraint
     *
     * @param table         - the name of the table to access
     * */
    public Cursor countQuery(String table){
        String query = "SELECT COUNT(*) AS numRows FROM " + table;
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }

        return cursor;
    }

    /**
     * Used to count all the element of a wishlist
     *
     * @param wlId      - the id of the WishList
     * */
    public Cursor countQueryWLElements(int wlId){
        String query = "SELECT COUNT(*) AS numRows " +
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

    /**
     * Used to sum all the entries
     *
     * */
    public Cursor sumEntriesQuery(){
        String query = "SELECT SUM(entries.value) AS sumEntr FROM entries";
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

        }
        return cursor;
    }

    /**
     * Used to sum all the purchases value
     *
     * */
    public Cursor sumPurchasesQuery(){
        String query = "SELECT SUM(items.price) AS sumP FROM items WHERE isValid = 0";
        Cursor cursor = null;
        try{
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    /**
     * Used to get the last item bought
     *
     * @param itemID        - the id of the item
     *
     * */
    public Cursor getLastItemFromPurchasesQuery(int itemID){
        String query =
                "SELECT items.name,items.price,categories.picId " +
                "FROM items JOIN categories ON categories.id = items.idCat" +
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

    /**
     * Used to get all the data related to the entries
     *
     * @param entriesId     - the id of the entry
     *
     * */
    public Cursor getEntriesDataQuery(int entriesId){
        String query = "SELECT entries.value,entries.dateEntr,categories.picId " +
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

    /**
     * Used to get all the WL data
     *
     * @param wlID      - the id of the WishList
     *
     * */
    public Cursor getWishListDataQuery(int wlID){
        String query = "SELECT SUM(items.price) AS tot, categories.picId, wishLists.name " +
                "FROM purchases JOIN wishLists ON purchases.listId = wishLists.id " +
                "JOIN items ON items.id = purchases.itemId  " +
                "JOIN categories ON categories.id = items.idCat " +
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

    /**
     * Used to get all the WL items
     *
     * @param wlID      - the id of the WishList
     *
     * */
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

    /**
     * Used to count the number of WL by their validity
     *
     * @param valid     - indicates the validity of the WishList
     *
     * */
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
