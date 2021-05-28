package com.example.keepmoneyv3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private final static int VERSION = 1;
    private final static String DB_NAME = "KeepMoneyDb";

    public DbHelper(Context context) {
        super(context,DB_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = DbStrings.TableUsersField.getCreateTable();//users table creation
        db.execSQL(table);

        table = DbStrings.TableCategoriesFields.getCreateTable();//categories table creation
        db.execSQL(table);

        table = DbStrings.TableItemsFields.getCreateTable();//items table creation
        db.execSQL(table);

        table = DbStrings.TableIncomesFields.getCreateTable();//entries table creation
        db.execSQL(table);

        table = DbStrings.TableWishListsFields.getCreateTable();//wishlists table creation
        db.execSQL(table);

        table = DbStrings.TablePurchasesFields.getCreateTable();//purchase table creation
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {}

}
