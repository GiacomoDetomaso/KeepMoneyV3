package com.example.keepmoneyv3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.keepmoneyv3.R;

/**
 * This is the entry point of the application. It performs login and the access int the
 * RegistrationActivity and int the NavigationActivity
 *
 * @author Michelangelo De Pascale
 *
 * @see NavigationActivity*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method perform the login action by checking with a query if the user is valid
     *
     * @author Michelangelo De Pascale
     * @see NavigationActivity
     * @see com.example.keepmoneyv3.ui.dashboard.DashboardFragment*/
    public void loginAction(View view){
        newActivityRunning(NavigationActivity.class, null);

        EditText txtFromUsernameBox = findViewById(R.id.txtName);
        EditText txtFromPasswordBox = findViewById(R.id.txtPassword);

        String strUsername = txtFromUsernameBox.getText().toString();
        String strPassword = txtFromPasswordBox.getText().toString();

        if(strUsername.equals("")||strPassword.equals("")) {
            Toast.makeText(getApplicationContext(),"Errore, uno o più campi non compilati correttamente",Toast.LENGTH_LONG).show();
        } else {

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