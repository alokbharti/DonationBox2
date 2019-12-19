package com.alokbharti.donationbox;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalSettingsRepository {

    public static void setNgoId(Context context, String id){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NGO_ID, id);
        editor.apply();
    }

    public static String getNgoId(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Constants.NGO_ID, "");
    }

    public static void setNgoPassword(Context context, String password){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NGO_PASSWORD, password);
        editor.apply();
    }

    public static String getNgoPassword(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(Constants.NGO_PASSWORD, "1234");
    }

    public static void setUserType(Context context, String userType){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.USER_TYPE, userType);
        editor.apply();
    }

    public static String getUserType(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  sharedPreferences.getString(Constants.USER_TYPE, "");
    }
}
