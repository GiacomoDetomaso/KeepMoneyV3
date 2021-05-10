package com.example.keepmoneyv3.database;

public class DbStrings {

    public static class TableUsersField {
        public static final String TABLE_NAME = "users";
        static final String USERS_ID = "username";
        static final String USERS_PWD = "password";
        static final String USERS_NOME = "nome";
        static final String USERS_COGNOME = "cognome";
        static final String USERS_EMAIL = "email";
        public static final String USERS_TOT = "totale";

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        USERS_ID + " VARCHAR(100) PRIMARY KEY,\n" +
                        USERS_PWD + " VARCHAR(100) NOT NULL,\n" +
                        USERS_EMAIL + " VARCHAR(255) DEFAULT NULL, \n" +
                        USERS_NOME + " VARCHAR(100) NOT NULL,\n" +
                        USERS_COGNOME + " VARCHAR(100) NOT NULL,\n" +
                        USERS_TOT + " DECIMAL NOT NULL); ";

        static String getCreateTable(){
            return CREATE_TABLE;
        }
    }

    public static class TableCategoriesFields{
        //todo: put the fields of category's table
        public static final String TABLE_NAME = "categories";
        public static final String CATEGORIES_ID = "id";
        public static final String CATEGORIES_DESC = "descrizione";
        public static final String CATEGORIES_PICID = "picid";

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        CATEGORIES_ID + " VARCHAR(8) PRIMARY KEY,\n" +
                        CATEGORIES_DESC + " VARCHAR(255),\n" +
                        CATEGORIES_PICID + " INT NOT NULL);";

        static String getCreateTable(){
            return CREATE_TABLE;
        }

    }

    public static class TableItemsFields{
        public static final String TABLE_NAME = "items";
        public static final String ITEMS_ID = "id";
        public static final String ITEMS_PREZZO = "prezzo";
        public static final String ITEMS_QUANT = "quantita";
        public static final String ITEMS_DESC = "descrizione";
        public static final String ITEMS_VALID = "isValid";
        public static final String ITEMS_IDCAT = "idcat";

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        ITEMS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        ITEMS_PREZZO + " DECIMAL NOT NULL,\n" +
                        ITEMS_DESC + " VARCHAR(255),\n" +
                        ITEMS_QUANT + " INT DEFAULT 0,\n" + // the parameter can be null
                        ITEMS_VALID + " INT,\n" +
                        ITEMS_IDCAT + " VARCHAR(8) NOT NULL,\n" +
                        "FOREIGN KEY (" + ITEMS_IDCAT + ") REFERENCES " +  //foreign key declaration
                        TableCategoriesFields.TABLE_NAME + " (" + TableCategoriesFields.CATEGORIES_ID + "));";

        static String getCreateTable() {
            return CREATE_TABLE;
        }
    }

    public static class TableEntriesFields{
        public static final String TABLE_NAME = "entries";
        public static final String ENTRIES_ID = "id";
        public static final String ENTRIES_VAL = "valore";
        public static final String ENTRIES_DATA = "dataEntr";
        public static final String ENTRIES_ID_CAT = "idcat";
        public static final String ENTRIES_ID_USER = "userId";

        private static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        ENTRIES_ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,\n" +
                        ENTRIES_VAL + " DECIMAL NOT NULL,\n" +
                        ENTRIES_DATA + " DATE,\n" +
                        ENTRIES_ID_CAT + " VARCHAR(8) NOT NULL,\n" +
                        ENTRIES_ID_USER + " VARCHAR(100) NOT NULL,\n" +

                        "FOREIGN KEY (" + ENTRIES_ID_CAT + ") REFERENCES " +  //foreign key declaration
                        TableCategoriesFields.TABLE_NAME + " (" + TableCategoriesFields.CATEGORIES_ID + "),\n" +

                        "FOREIGN KEY (" + ENTRIES_ID_USER + ") REFERENCES " +
                        TableUsersField.TABLE_NAME + " (" + TableUsersField.USERS_ID + "));";


        static String getCreateTable() {
            return CREATE_TABLE;
        }
    }


    public static class TableWishListsFields{
        public static final String TABLE_NAME = "wishLists";
        public static final String WL_ID = "id";
        public static final String WL_DENOM = "denominazione";
        public static final String WL_DESC = "descrizione";
        public static final String WL_VALID = "isValid";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        WL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        WL_DENOM + " VARCHAR(80) NOT NULL,\n" +
                        WL_DESC + " VARCHAR(255),\n" +
                        WL_VALID + " INT);";

        static String getCreateTable() {
            return CREATE_TABLE;
        }
    }

    public static class TablePurchasesFields{
        public static final String TABLE_NAME = "purchases";
        public static final String PURCH_ID = "id";
        public static final String PURCH_DATA = "dataP";
        public static final String PURCH_ORA = "oraP";
        public static final String PURCH_ID_USER = "userId";
        public static final String PURCH_ITEM_ID = "itemId";
        public static final String PURCH_WL_ID = "listId";

        static final String CREATE_TABLE =
                "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (\n" +
                        PURCH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                        PURCH_DATA + " DATE,\n" +
                        PURCH_ORA + " TIME,\n" +
                        PURCH_ID_USER + " VARCHAR(100) NOT NULL,\n" +
                        PURCH_ITEM_ID + " INTEGER NOT NULL,\n" +
                        PURCH_WL_ID + " INTEGER DEFAULT NULL,\n" +

                        "FOREIGN KEY (" + PURCH_ID_USER + ") REFERENCES " +
                        TableUsersField.TABLE_NAME + " (" + TableUsersField.USERS_ID + "),\n" +

                        "FOREIGN KEY (" + PURCH_ITEM_ID + ") REFERENCES " +
                        TableItemsFields.TABLE_NAME + " (" + TableItemsFields.ITEMS_ID + "),\n" +

                        "FOREIGN KEY (" + PURCH_WL_ID + ") REFERENCES " +
                        TableWishListsFields.TABLE_NAME + " (" + TableWishListsFields.WL_ID + ") " +
                        "ON UPDATE CASCADE ON DELETE SET DEFAULT);";

        static String getCreateTable() {
            return CREATE_TABLE;
        }
    }

}
