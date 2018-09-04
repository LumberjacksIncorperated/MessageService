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
    private final static String SHARED_PREFERENCES_USERSPACE_KEY = "MyAppPreferences";

    private UserDataStorage(Context context) {
        this.context = context;
    }

    public static UserDataStorage userDataStorageWithContext(Context context) {
        return new UserDataStorage(context);
    }

    public String getUserDataStringWithKey(String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, Context.MODE_PRIVATE);
        return preferences.getString(key, null);
    }

    public void setUserDataStringForKey(String userDataString, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, userDataString);
        editor.commit();
    }
}