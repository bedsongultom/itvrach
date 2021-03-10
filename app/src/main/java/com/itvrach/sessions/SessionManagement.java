package com.itvrach.sessions;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.itvrach.www.itvrach.MainActivity;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "MyPrefs";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)





    public static final String KEY_USERID    ="user_id";
    public static final String KEY_USERNAME  ="username";
    public static final String KEY_TYPE      ="type";
    public static final String KEY_FIRSTNAME ="firstname";
    public static final String KEY_LASTNAME  ="lastname";
    public static final String KEY_FULLNAME  ="fullname";
    public static final String KEY_EMAIL = "emails";
    public static final String KEY_SESSIONID = "session_id";
    public static final String KEY_FILE  ="file";
    public static final String KEY_HP = "hp";
    public static final String KEY_ADDRESS = "address";


    // Email address (make variable public to access from outside)






    // Constructor
    public SessionManagement(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Create login session
     * */
    public void createLoginSession(String user_id,String username, String type, String firstname,
                                   String lastname,  String fullname, String emails, String session_id, String file,
                                   String hp, String address){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing name in pref
        editor.putString(KEY_USERID, user_id);
        editor.putString(KEY_USERNAME  ,username);
        editor.putString(KEY_TYPE      ,type);
        editor.putString(KEY_FIRSTNAME ,firstname);
        editor.putString(KEY_LASTNAME  ,lastname);
        editor.putString(KEY_FULLNAME, fullname);
        editor.putString(KEY_EMAIL ,emails);
        editor.putString(KEY_SESSIONID ,session_id);
        editor.putString(KEY_FILE  ,file);
        editor.putString(KEY_HP ,hp);
        editor.putString(KEY_ADDRESS ,address);
        // commit changes
        editor.commit();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(){
        // Check login status
        if(!this.isLoggedIn()){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, MainActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }



    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();

        user.put(KEY_USERID, pref.getString(KEY_USERID, null));
        user.put(KEY_USERNAME  ,pref.getString(KEY_USERNAME, null));
        user.put(KEY_TYPE      ,pref.getString(KEY_TYPE, null));
        user.put(KEY_FIRSTNAME ,pref.getString(KEY_FIRSTNAME, null));
        user.put(KEY_LASTNAME  ,pref.getString(KEY_LASTNAME, null));
        user.put(KEY_FULLNAME  ,pref.getString(KEY_FULLNAME, null));
        user.put(KEY_EMAIL     ,pref.getString(KEY_EMAIL, null));
        user.put(KEY_SESSIONID ,pref.getString(KEY_SESSIONID, null));
        user.put(KEY_FILE  ,pref.getString(KEY_FILE, null));
        user.put(KEY_HP ,pref.getString(KEY_HP, null));
        user.put(KEY_ADDRESS ,pref.getString(KEY_ADDRESS, null));

        // return user
        return user;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }
}