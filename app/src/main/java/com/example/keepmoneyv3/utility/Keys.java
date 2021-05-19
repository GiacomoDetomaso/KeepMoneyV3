package com.example.keepmoneyv3.utility;

public class Keys {
    public static class SerializableKeys{
        public static final String USER_KEY = "user";
    }

    public static class DialogTitles{
        public static final String DIALOG_ENTRIES_TITLE = "Entrate";
        public static final String DIALOG_ADD_NEW_TYPE_TITLE = "Categorie";
        public static final String DIALOG_PURCHASES_TITLE = "Spese";
    }

    public static class DialogTags{
        public static final String DIALOG_ENTRIES_TAG = "DialogEntries";
        public static final String DIALOG_ADD_NEW_TYPE_TAG = "DialogAddNewType";
        public static final String DIALOG_DATE_PICKER_TAG = "DatePickerDialogFrag";
        public static final String DIALOG_PURCHASES_TAG = "DialogPurchase";
        public static final String DIALOG_TIME_PICKER_TAG = "TimePickerDialogFrag";
    }

    public static class MiscellaneousKeys{
        public static final String EMAIL_REGEX = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        public static final int NO_WL_DEFAULT = 0; //this value means that there is not wl for that purchase
    }

    public static class PredefinedCategories{
        // todo
    }
}
