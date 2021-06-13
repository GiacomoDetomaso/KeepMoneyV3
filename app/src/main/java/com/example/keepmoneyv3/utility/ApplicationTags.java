package com.example.keepmoneyv3.utility;

/**
 * This class contains some static classes inside which there
 * are strings used to parameterize the application in many of its methods.
 *
 * @author Giacomo Detomaso
 * */
public class ApplicationTags {

    /**
     * This class contains tags used in Bundle transactions.
     */
    public static class SerializableTags {
        public static final String USER_KEY = "user";
        public static final String INCOMES_ROWS_KEY = "incomes";
        public static final String PURCHASES_ROWS_KEY = "purchases";
        public static final String USERNAME_KEY = "username";
        public static final String WISH_LIST_KEYS = "wishlist";
        public static final String POSITION_KEY = "position";
    }

    /**
     * This class contains tags used in dialogs to set their title.
     */
    public static class DialogTitles{
        public static final String DIALOG_ENTRIES_TITLE = "Entrate";
        public static final String DIALOG_ADD_NEW_TYPE_TITLE = "Categorie";
        public static final String DIALOG_PURCHASES_TITLE = "Spese";
        public static final String DIALOG_ADD_WISH_LIST_TITLE = "Aggiungi lista";
        public static final String DIALOG_EDIT_WISH_LIST_TITLE = "Modifica Lista";
    }

    /**
     * This class contains tags used at the creation of a dialog.
     * They are useful to retrieve a dialog at runtime.
     */
    public static class DialogTags{
        public static final String DIALOG_INCOME_TAG = "DialogIncome";
        public static final String DIALOG_ADD_NEW_TYPE_TAG = "DialogAddNewType";
        public static final String DIALOG_DATE_PICKER_TAG = "DatePickerDialogFrag";
        public static final String DIALOG_PURCHASES_TAG = "DialogPurchase";
        public static final String DIALOG_TIME_PICKER_TAG = "TimePickerDialogFrag";
        public static final String DIALOG_ADD_WISH_LIST_ITEMS_TAG = "DialogAddWishListItems";
        public static final String DIALOG_ADD_NAME_TO_WISH_LIST_TAG = "DialogAddNameToWishList";
        public static final String DIALOG_EDIT_WISH_LIST_TAG = "DialogEditWishList";
        public static final String DIALOG_EDIT_WISH_LIST_ELEMENT_TAG = "DialogEditWishListElement";
    }

    /**
     * This class contains some generic tags.
     */
    public static class MiscellaneousTags{
        public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        public static final int NOT_CONFIRMED = 0;
        public static final int CONFIRMED = 1;
    }

    /**
     * This class contains the IDs of the categories.
     */
    public static class PredefinedCategoriesId{
        public static final String CAT_TECH_ID = "cat01";
        public static final String CAT_FOOD_ID = "cat02";
        public static final String CAT_FAMILY_ID = "cat03";
        public static final String CAT_FUN_ID = "cat04";
        public static final String CAT_HOME_ID = "cat05";
        public static final String CAT_HOLIDAY_ID = "cat06";
        public static final String CAT_SALARY_ID = "cat07";
        public static final String CAT_PRESENT_ID = "cat08";
        public static final String CAT_BET_ID = "cat09";
        public static final String CAT_HEALTH_ID = "cat10";
    }
}
