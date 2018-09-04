package com.example.developer.messageservice;
//--------------------------------------------------------
//
// DESCRIPTION
// -----------
// This class is designed as the base activity class
// which all other activities should extend
//
// AUTHOR
// ------
// Lumberjacks Incorperated (2018)
//
//--------------------------------------------------------

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------------------------------------------
    // SCHEMATIC FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------------
    protected void initialiseActivity() {
        /*
         *   This method is intended to be overriden by inheriting classes, and should
         *   contain the code required to initialize the activity
         */
    }

    protected void whenUserTouchesScreen() {
        /*
         *   This method is intended to be overriden by inheriting classes, and should
         *   contain the code required for when user touches the screen ever
         */
    }

    //----------------------------------------------------------------------------------------------------------------------
    // HIDDEN FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialiseActivity();
    }

    @Override
    public void onUserInteraction() {
        this.whenUserTouchesScreen();
    }

    //----------------------------------------------------------------------------------------------------------------------
    // EXPORTED FUNCTIONS
    //----------------------------------------------------------------------------------------------------------------------
    protected void showMessageToUser(final String messageToShowToUser) {
        this.runOnUiThread(new Runnable(){
            public void run() {
                Context applicationContext = BaseActivity.this.getApplicationContext();
                Toast theToastMessageToShowToUser = Toast.makeText(applicationContext, messageToShowToUser, Toast.LENGTH_LONG);
                theToastMessageToShowToUser.show();
            }
        });
    }

    protected void showPopupAlertMessageToUser(final String messageText) {
        this.runOnUiThread(new Runnable(){
            public void run() {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(BaseActivity.this);
                builder1.setMessage(messageText);
                builder1.setCancelable(true);

                builder1.setNegativeButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });
    }

    protected SystemPeriferals systemPeriferals() {
        Context someContext = this.getApplicationContext();
        SystemPeriferals theSystemPeriferals = SystemPeriferals.systemPeriferalsWithContext(someContext);
        return theSystemPeriferals;
    }

    protected void moveToActivityOfClass(Class<?> classOfActivity) {
        Context applicationContext = this.getApplicationContext();
        Intent intentToMoveToActivity = new Intent(applicationContext, classOfActivity);
        this.startActivity(intentToMoveToActivity);
    }
}
