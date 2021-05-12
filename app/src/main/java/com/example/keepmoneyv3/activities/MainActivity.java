package com.example.keepmoneyv3.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.keepmoneyv3.R;

/**
 * This is the entry point of the application. It performs login and the access in the
 * RegistrationActivity and in the NavigationActivity
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