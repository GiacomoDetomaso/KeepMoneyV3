package com.labproject.keepmoneyv3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.labproject.keepmoneyv3.R;
import com.labproject.keepmoneyv3.database.*;
import com.labproject.keepmoneyv3.utility.ApplicationTags;
import com.labproject.keepmoneyv3.utility.User;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * This is the first activity of the application that the user sees. It performs the login and the
 * registration of users with the help of RegistrationActivity. Then, it launches NavigationActivity.
 *
 * @author Michelangelo De Pascale
 *
 * @see NavigationActivity
 * @see RegistrationActivity
 * */
public class LoginActivity extends AppCompatActivity {

    private DbManager dbManager;

    /**
     * This method, that overrides the standard one, describes what happens when the Activity is created.
     * If the database is not present, the categories table is created inside it.
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbManager = new DbManager(getApplicationContext());
        addPredefinedCategories(); // method to add the predefined categories if the table is empty
    }

    /**
     * This method performs the login action, by checking with a query if the user's credentials
     * are present and valid.
     *
     * @author Michelangelo De Pascale
     * @see NavigationActivity
     * @see com.labproject.keepmoneyv3.ui.dashboard.DashboardFragment
     *
     * @param view      the actual view
     */
    public void loginAction(View view) throws NoSuchAlgorithmException {

        User user;

        EditText txtFromUsernameBox = findViewById(R.id.txtUsernameLog);
        EditText txtFromPasswordBox = findViewById(R.id.txtPasswordLog);

        String strUsername = txtFromUsernameBox.getText().toString();
        String strPassword = txtFromPasswordBox.getText().toString();

        if(strUsername.equals("")||strPassword.equals("")) {
            Toast.makeText(getApplicationContext(),"Errore, uno o più campi non compilati correttamente",Toast.LENGTH_SHORT).show();
        } else {
            Cursor cursor = dbManager.queryCheckUserLogin(strUsername, encodePassword(strPassword));
            if(cursor != null) {
                if(cursor.moveToNext()) {
                    String strLoggedUser_Email = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_EMAIL));
                    String strLoggedUser_Name = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_NAME));
                    String strLoggedUser_Surname = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_FAMILY_NAME));
                    String strLoggedUser_Username = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_ID));
                    String strLoggedUser_Password = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_PWD));
                    float fltLoggedUser_Total = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_TOT)));
                    Bundle loginScreenMainActivityBundle = new Bundle();
                    user = new User(strLoggedUser_Username,strLoggedUser_Password,strLoggedUser_Name,strLoggedUser_Surname,strLoggedUser_Email,fltLoggedUser_Total);
                    loginScreenMainActivityBundle.putSerializable(ApplicationTags.SerializableTags.USER_KEY,user);
                    newActivityRunning(NavigationActivity.class, loginScreenMainActivityBundle);
                } else  {
                    Toast.makeText(getApplicationContext(), "Errore, username o password errati", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Method used to switch to the RegistrationActivity using fabRegistration FAB
     *
     * @param view      the actual view
     *
     * @see RegistrationActivity*/
    public void fabRegistrationAction(View view){
        newActivityRunning(RegistrationActivity.class, null);
    }


    /**
     * This method allows the app to switch, calling the activity
     *
     * @author Michelangelo De Pascale
     *
     * @param newActivityClass                 the class of the activity that we need to call
     * @param additionalData    optional bundle to pass as extra information to the newly
     *                          created activity
     */
    private void newActivityRunning(@SuppressWarnings("rawtypes") Class newActivityClass, Bundle additionalData){
        Intent intent = new Intent(this,newActivityClass);

        if (additionalData != null){
            intent.putExtras(additionalData);
        }

        startActivity(intent); //start a new activity
    }

    /**
     * A method used to add the predefined categories of the app in the database
     * */
    private void addPredefinedCategories(){
        Cursor cursor = dbManager.countQuery(DbStrings.TableCategoriesFields.TABLE_NAME); // call to the method to get the number of rows of the table called "categories"
        int numRows;
        if(cursor != null) {
            // insert the categories if the table is empty (no records)
            while (cursor.moveToNext()) {
                numRows = cursor.getInt(cursor.getColumnIndex("numRows")); // save the number of rows got before from the db in numRows as an integer
                if (numRows == 0) { //if the table is empty, then add the predefined categories
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_TECH_ID, getString(R.string.tech), R.drawable.ic_baseline_computer_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_FOOD_ID, getString(R.string.food), R.drawable.ic_baseline_fastfood_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_FAMILY_ID, getString(R.string.family), R.drawable.ic_baseline_family_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_FUN_ID, getString(R.string.fun), R.drawable.ic_baseline_emoji_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_HOME_ID, getString(R.string.home), R.drawable.ic_baseline_home_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_HOLIDAY_ID, getString(R.string.holiday), R.drawable.ic_baseline_beach_access_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_SALARY_ID, getString(R.string.salary), R.drawable.ic_baseline_attach_money_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_PRESENT_ID, getString(R.string.present), R.drawable.ic_baseline_card_giftcard_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_BET_ID, getString(R.string.bet), R.drawable.ic_baseline_casino_24);
                    dbManager.insertCategories(ApplicationTags.PredefinedCategoriesId.CAT_HEALTH_ID, getString(R.string.health), R.drawable.ic_baseline_healing_24);
                }
            }
        }
    }

    /**
     * This method, that overrides the standard one, is used to close
     * the app when the back button is pressed on this activity
     *
     * */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * This method is used to encode a string, using the MD5 algorithm
     *
     * @param encodingString      the string to encode
     * */
    private String encodePassword(String encodingString) throws NoSuchAlgorithmException {
        // MessageDigest instance for MD5
        final String MD5_MODE = "MD5";
        MessageDigest md = MessageDigest.getInstance(MD5_MODE);

        // Update MessageDigest with input text in bytes
        md.update(encodingString.getBytes(StandardCharsets.UTF_8));

        // Get the hashbytes
        byte[] hashBytes = md.digest();

        //Convert hash bytes to hex format
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }
}