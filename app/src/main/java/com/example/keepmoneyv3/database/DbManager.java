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
 * @see DbHelper        used to get the DB
 * @see DbStrings       used to access to all the tables
 * @see ContentValues   used to set the values of the queries
 * @see Cursor          used to provide the result of some queries
 * @see SQLiteDatabase  used to perform queries
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
     * @param username  identifies an user uniquely
     * @param password  the password of the user account
     * @param name      the name of the user
     * @param surname   the surname of the user
     * @param email     email of the user
     * @param total     total amount of money of the user */
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
     * @param id        identifies a category
     * @param desc      brief description of the category
     * @param picId     identifies the picture associated with the category*/
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
     * @param price     the price of the item
     * @param amount    indicates the amount item
     * @param name      the name of the item
     * @param valid     indicates if the item has been bought (valid = 1) or it's planned to (valid = 0)
     * @param idCat     identifies the category of the item
     * */
    public long insertItems(float price, int amount, String name, int valid, String idCat){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_IS_CONFIRMED,valid);
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
     * @param val       the value of the income
     * @param date      the date of the income
     * @param idCat     the category of the income
     * @param idUser    the id of the user*/
    public long insertIncome(float val, String date, String idCat, String idUser){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableIncomesFields.INCOMES_VAL,val);
        contentValues.put(DbStrings.TableIncomesFields.INCOMES_DATE,date);
        contentValues.put(DbStrings.TableIncomesFields.INCOMES_ID_CAT,idCat);
        contentValues.put(DbStrings.TableIncomesFields.INCOMES_ID_USER,idUser);

        long testValue = 0;

        try {
            testValue = db.insert(DbStrings.TableIncomesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return testValue;
    }

    /**
     * This method is used to insert a wishlist inside the database
     *
     *
     * @param name      the name of the list
     * @param desc      a description of the list
     * @param valid     indicates if the item has been bought (valid = 1) or it's planned to (valid = 0)
     * */
    public long insertWishLists(String name, String desc, int valid) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableWishListsFields.WL_NAME,name);
        contentValues.put(DbStrings.TableWishListsFields.WL_DESC,desc);
        contentValues.put(DbStrings.TableWishListsFields.WL_IS_CONFIRMED, valid);

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
     * @param dateP     the date of the purchase
     * @param timeP     the time of the purchase
     * @param idUser    the id of the user
     * @param idItem    the id of the item
     * @param idWl      the id of the wishlist
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
     *  @param idUser        the username
     * @param idItem        the id of the item
     * @param idWl          the id of WishList
     *
     **/
    public void insertPurchaseSimple(String idUser, int idItem, int idWl){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ID_USER,idUser);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_ITEM_ID,idItem);
        contentValues.put(DbStrings.TablePurchasesFields.PURCH_WL_ID,idWl);

        try {
            db.insert(DbStrings.TablePurchasesFields.TABLE_NAME,null,contentValues);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is used to update the total value of the user
     *
     *
     * @param tot       total value
     * @param username  username
     * */
    public void updateUserTotal(float tot,String username){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "username = '" + username + "'";//the where clause
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableUsersField.USERS_TOT,tot);

        try {
             db.update(DbStrings.TableUsersField.TABLE_NAME,contentValues,whereClause,null);

        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * This method is used to update the validity of an item
     *
     *
     * @param valid     validity of the item
     * @param id        item's ID
     * */
    public void updateItemsValidity(int valid, int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = " id = " + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_IS_CONFIRMED,valid);

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
     * @param valid     validity of the list
     * @param id        WL ID
     * */
    public void updateWishListsValidity(int valid, int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = " id = " + id;
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbStrings.TableItemsFields.ITEMS_IS_CONFIRMED,valid);

        try {
            db.update(DbStrings.TableWishListsFields.TABLE_NAME,contentValues,whereClause,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    /**
     * A query to check if the login credentials are correct
     *
     * @param username  identifies an user uniquely
     * @param password  the password of the user account
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
     * Used to get all the rows of a table
     *
     * @param table         the name of the table to access
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
     * Used to get all the rows of a table
     *
     * @param username      the username
     **/
    public Cursor queryGetAllIncomesByUsername(String username){
        Cursor cursor = null;
        String query = "SELECT incomes.* " +
                "FROM incomes JOIN users ON userId = username WHERE username = '" + username + "' ;";

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor queryGetAllConfirmedWishListsByUsername(String username){
        Cursor cursor = null;
        String query = "SELECT wishLists.* FROM wishLists JOIN purchases ON purchases.listId = wishLists.id " +
                "JOIN users ON users.username = purchases.userId " +
                " WHERE username = '" + username + "' GROUP BY listId;";

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor queryGetAllSimplePurchasesByUsername(String username){
        Cursor cursor = null;
        String query = "SELECT purchases.* FROM purchases " +
                "JOIN users ON users.username = purchases.userId " +
                " WHERE username = '" + username + "' AND listId = 0;";

        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    /**
     * Used to count the number of rows of a table without any specific constraint
     *
     * @param table         the name of the table to access
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

    public Cursor countIncomesRowsByUsername(String username){
        String query = "SELECT COUNT(*) AS numRows " +
                "FROM incomes  " +
                "WHERE userId = '" + username + "';";
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query,null);
        }catch (Exception e){
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cursor;
    }

    public Cursor countSimplePurchasesRowsByUsername(String username){
        String query = "SELECT COUNT(*) AS numRows " +
                "FROM purchases " +
                "WHERE userId = '" + username + "' AND listId = 0;";
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
     * @param wlId      the id of the WishList
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
     * @param username      the username
     * */
    public Cursor sumIncomesQuery(String username){
        String query = "SELECT SUM(incomes.value) AS sumInc FROM incomes JOIN users " +
                "ON (users.username = incomes.userId) WHERE username = '" + username + "'";
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
     * @param username      the username
     * */
    public Cursor sumPurchasesQuery(String username, int isConfirmed){
        String query = "SELECT SUM(items.price*amount) AS sumPurch FROM items JOIN " +
                "purchases ON (purchases.itemId = items.id) JOIN users " +
                "ON (users.username = purchases.userId) WHERE isConfirmed = " + isConfirmed + " " +
                "AND username = '" + username + "'";
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
     * Used to get the recent bought items from the single purchases
     *
     * @param limit     the number of items
     * @param listId    specify if the item is part of a wishlist
     * @param username  the username
     * */
    public Cursor getPurchasesItemsQuery(int limit, int listId, String username){
        String query;
        if(limit > 0) {
             query =
                    "SELECT items.id, items.name,items.price, items.amount,categories.picId " +
                            "FROM items JOIN categories ON categories.id = items.idCat" +
                            " JOIN purchases ON purchases.itemId = items.id" +
                            " JOIN users ON users.username = purchases.userId" +
                            " WHERE listId = " + listId + " AND users.username = '" + username + "'" +
                            " ORDER BY items.id DESC" +
                            " LIMIT " + limit + ";";
        } else {
            query =
                    "SELECT items.id, items.name,items.price, items.amount,categories.picId " +
                            "FROM items JOIN categories ON categories.id = items.idCat" +
                            " JOIN purchases ON purchases.itemId = items.id" +
                            " JOIN users ON users.username = purchases.userId" +
                            " WHERE listId = " + listId + " AND users.username = '" + username + "'" +
                            " ORDER BY items.id DESC;";
        }

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
     * Used to get all the data related to the entries
     *
     * @param username     the username
     *
     * */
    public Cursor getEntriesDataQueryByUsername(String username){
        String query = "SELECT incomes.id, incomes.value,incomes.dateIncome,categories.picId " +
                "FROM incomes JOIN categories ON incomes.idcat = categories.id " +
                "WHERE userId = '" + username + "';";

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
     * @param username      the username
     * @param isConfirmed   indicates if the user has confirmed the list or not
     *
     * */
    public Cursor getWishListDataQuery(String username, int isConfirmed){
        String query = "SELECT SUM(items.price * items.amount) AS tot, wishLists.* " +
                "FROM purchases JOIN wishLists ON purchases.listId = wishLists.id " +
                "JOIN items ON items.id = purchases.itemId  " +
                "JOIN categories ON categories.id = items.idCat " +
                "JOIN users ON users.username = purchases.userId " +
                "WHERE users.username = '" + username + "' AND wishLists.isConfirmed = " + isConfirmed + " " +
                "GROUP BY listId;";
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
     * @param wlID      the id of the WishList
     *
     * */
    public Cursor getWishListsItems(int wlID){
        String query = "SELECT items.*, categories.picId " +
                "FROM purchases JOIN wishlists ON purchases.listId = wishLists.id " +
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

}
