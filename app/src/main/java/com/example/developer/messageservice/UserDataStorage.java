package com.example.developer.messageservice;
//----------------------------------------------------------------------------------
//
// AUTHOR
// ------
// Lumberjacks Incorperated (2018)
//
//----------------------------------------------------------------------------------

import android.content.Context;
import android.content.SharedPreferences;

public class UserDataStorage {

    private final Context context;
    private  SharedPreferences.Editor editor;
    private  SharedPreferences preferences;

    private final static String SHARED_PREFERENCES_USERSPACE_KEY = "MyAppPreferences";
    private UserDataStorage(Context context) {
        this.context = context;
        this.preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, 0);
        this.editor = preferences.edit();
    }

    public static UserDataStorage userDataStorageWithContext(Context context) {
        return new UserDataStorage(context);
    }

    public String getUserDataStringWithKey(String key) {
         preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, 0);
         String string =  preferences.getString(key, null);
         return string;
    }

    public void setUserDataStringForKey(String key, String userDataString) {
        preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, 0);
        this.editor = preferences.edit();
        editor.putString(key, userDataString);
        editor.commit();
        preferences.getString(key, null);
        if(true) {}
    }
}