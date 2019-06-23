package com.ck.corp.opticalattendence.Utils;


import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PrefrenceManager {

    public PrefrenceManager() {}

    private static SharedPreferences getSharedPrefrences(Context c){
         return c.getSharedPreferences(Config.APP_PREFRENCES,MODE_PRIVATE);
    }

    /**
     * Strings
     */

    public static String getString(Context c,String Key){
        return getSharedPrefrences(c).getString(Key, null);
    }

    public static void setString(Context c,String Key,String value){
        final SharedPreferences.Editor editor = getSharedPrefrences(c).edit();
        editor.putString(Key,value);
        editor.apply();
    }
}
