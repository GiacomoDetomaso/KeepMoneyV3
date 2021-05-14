package com.example.keepmoneyv3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keepmoneyv3.R;
import com.example.keepmoneyv3.database.DbManager;
import com.example.keepmoneyv3.utility.Keys;
import com.example.keepmoneyv3.utility.User;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class used to perform the registration of a new user
 *
 * @author Giacomo Detomaso*/

public class RegistrationActivity extends AppCompatActivity {

    private DbManager dbManager; // database manager used to insert a new user
    // compile the pattern for the regex
    private static final Pattern EMAIL_REGEX_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // initialization of the global variable
        dbManager = new DbManager(getApplicationContext());
    }

    /**
     * The method that performs the registration of a new user
     *
     * @param view      - the actual view
     * */
    public void registrationAction(View view){
        // take all the EditText to retrieve the Registration Data
        EditText txtNameReg = findViewById(R.id.txtNameReg);
        EditText txtSurnameReg = findViewById(R.id.txtSurnameReg);
        EditText txtUsernameReg = findViewById(R.id.txtUsernameReg);
        EditText txtPasswordReg = findViewById(R.id.txtPasswordReg);
        EditText txtEmail = findViewById(R.id.txtEmail);

        // extract the string from the EditText
        String strNameReg = txtNameReg.getText().toString();
        String strSurnameReg = txtSurnameReg.getText().toString();
        String strUsernameReg = txtUsernameReg.getText().toString();
        String strPasswordReg = txtPasswordReg.getText().toString();
        String strEmail = txtEmail.getText().toString();

        if(strNameReg.equals("") || strSurnameReg.equals("") || strUsernameReg.equals("") || strPasswordReg.equals("") || strEmail.equals("")){
            Toast.makeText(getApplicationContext(), "Campi vuoti! Inserire correttamente prima di procedere", Toast.LENGTH_LONG).show();
        } else {
            Matcher matcher = EMAIL_REGEX_PATTERN.matcher(strEmail); // check if the mail match the pattern

            // if the matching is successful perform the entry query
            if (matcher.find()) {
                long testValue = dbManager.insertUser(strUsernameReg, strPasswordReg, strNameReg, strSurnameReg, strEmail, 0f);//insert the user in the local db

            /*
              If the query is successful, instantiate the user and put it into a bundle to send to
              the NavigationActivity.
              */
                if (testValue > 0) {
                    User user = new User(strUsernameReg, strPasswordReg, strNameReg, strSurnameReg, strEmail, 0f);
                    Bundle navActivityBundle = new Bundle();
                    navActivityBundle.putSerializable(Keys.SerializableKeys.USER_KEY, user);
                    newActivityRunning(navActivityBundle);
                } else {
                    Toast.makeText(getApplicationContext(), "Nome utente già inserito", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Indirizzo email sbagliato!", Toast.LENGTH_LONG).show();
            }
        }


    }

    /**
     * Used to switch activity
     *
     * @author Michelangelo De Pascale
     * @param additionalData    - optional bundle to pass as extras */
    private void newActivityRunning(Bundle additionalData){
        Intent intent = new Intent(this, NavigationActivity.class);

        if (additionalData != null){
            intent.putExtras(additionalData);
        }

        startActivity(intent);//start a new activity
    }
}



