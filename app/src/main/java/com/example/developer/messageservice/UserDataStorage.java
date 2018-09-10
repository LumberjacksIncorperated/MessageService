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

    private final SharedPreferences.Editor editor;
    private final SharedPreferences preferences;

    private final static String SHARED_PREFERENCES_USERSPACE_KEY = "MyAppPreferences";
    private UserDataStorage(Context context) {
        this.preferences = context.getSharedPreferences(SHARED_PREFERENCES_USERSPACE_KEY, 0);
        this.editor = preferences.edit();
    }

    public static UserDataStorage userDataStorageWithContext(Context context) {
        return new UserDataStorage(context);
    }

    public String getUserDataStringWithKey(String key) {
        return preferences.getString(key, null);
    }

    public void setUserDataStringForKey(String key, String userDataString) {
        editor.putString(key, userDataString);
        editor.apply();
    }
}