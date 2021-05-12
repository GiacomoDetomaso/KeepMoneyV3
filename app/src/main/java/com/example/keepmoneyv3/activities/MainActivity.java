package com.example.keepmoneyv3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.database.DbStrings;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;

/**
 * This is the entry point of the application. It performs login and the access in the
 * RegistrationActivity and in the NavigationActivity
 *
 * @author Michelangelo De Pascale
 *
 * @see NavigationActivity*/

public class MainActivity extends AppCompatActivity {

    private DbManager dbManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager = new DbManager(getApplicationContext());
    }

    /**
     * This method perform the login action by checking with a query if the user is valid
     *
     * @author Michelangelo De Pascale
     * @see NavigationActivity
     * @see com.example.keepmoneyv3.ui.dashboard.DashboardFragment*/
    public void loginAction(View view){

        User user;

        EditText txtFromUsernameBox = findViewById(R.id.txtUsernameLog);
        EditText txtFromPasswordBox = findViewById(R.id.txtPasswordLog);

        String strUsername = txtFromUsernameBox.getText().toString();
        String strPassword = txtFromPasswordBox.getText().toString();

        if(strUsername.equals("")||strPassword.equals("")) {
            Toast.makeText(getApplicationContext(),"Errore, uno o più campi non compilati correttamente",Toast.LENGTH_LONG).show();
        } else {
            Cursor cursor = dbManager.queryCheckUserLogin(strUsername, strPassword);
            if(cursor != null) {
                while(cursor.moveToNext()) {
                    String strLoggedUser_Email = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_EMAIL));
                    String strLoggedUser_Name = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_NAME));
                    String strLoggedUser_Surname = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_SURNAME));
                    String strLoggedUser_Username = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_ID));
                    String strLoggedUser_Password = cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_PWD));
                    float fltLoggedUser_Total = Float.parseFloat(cursor.getString(cursor.getColumnIndex(DbStrings.TableUsersField.USERS_TOT)));
                    Bundle loginScreenMainActivityBundle = new Bundle();
                    user = new User(strLoggedUser_Username,strLoggedUser_Password,strLoggedUser_Name,strLoggedUser_Surname,strLoggedUser_Email,fltLoggedUser_Total);
                    loginScreenMainActivityBundle.putSerializable(Keys.SerializableKeys.USER_KEY,user);
                    newActivityRunning(NavigationActivity.class, loginScreenMainActivityBundle);
                }
            }
        }
    }

    /**
     * Used to switch to the RegistrationActivity
     *
     * @see RegistrationActivity*/
    public void fabRegistrationAction(View view){
        newActivityRunning(RegistrationActivity.class, null);
    }


    /**
     * Used to switch activity
     *
     * @author Michelangelo De Pascale
     *
     * @param s                 - the class of the activity
     * @param additionalData    - optional bundle to pass as extras*/
    private void newActivityRunning(Class s,Bundle additionalData){
        Intent intent = new Intent(this,s);

        if (additionalData != null){
            intent.putExtras(additionalData);
        }

        startActivity(intent);//start a new activity
    }

}