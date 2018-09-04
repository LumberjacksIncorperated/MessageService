package com.example.developer.messageservice;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeScreenActivity extends BaseActivity {

    private final int WELCOME_SCREEN_MAIN_UI_LAYOUT = R.layout.activity_welcome_screen;
    @Override
    protected void initialiseActivity() {
        this.setContentView(WELCOME_SCREEN_MAIN_UI_LAYOUT);
    }

    @Override
    protected void whenUserTouchesScreen() {
        WelcomeScreenActivity.this.moveToLoginActivity();
    }

    private void moveToLoginActivity() {
        this.moveToActivityOfClass(UserHomeActivity.class);
    }
}
