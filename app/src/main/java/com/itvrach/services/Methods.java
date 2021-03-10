package com.itvrach.services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

/**
 * Created by engineer on 12/27/2018.
 */

public class Methods {

    public static HashSet<String> getCookies(Context context) {
        SharedPreferences mcpPreferences = getDefaultSharedPreferences(context);
        return (HashSet<String>) mcpPreferences.getStringSet("cookies", new HashSet<String>());
    }

    public static boolean setCookies(Context context, HashSet<String> cookies) {
        SharedPreferences mcpPreferences = getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mcpPreferences.edit();
        return editor.putStringSet("cookies", cookies).commit();
    }

}
