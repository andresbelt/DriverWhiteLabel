package co.com.autolagos.rtaxi.local.driver.sharedPreferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;


public class Preferences {




    public static void setPreferencesLogin(boolean Islogin) {

        SharedPreferences prefs =
                AppBase.getInstance().getBaseContext().getSharedPreferences(Constants.pref, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Constants.isLogin, Islogin);
        editor.commit();

    }

    public static boolean getPreferencesLogin() {

        SharedPreferences prefs =
                AppBase.getInstance().getBaseContext().getSharedPreferences(Constants.pref, Context.MODE_PRIVATE);

        boolean Islogin = prefs.getBoolean(Constants.isLogin, false);
        return Islogin;

    }


    public static void setPreferencesUser(Driver driver) {

        SharedPreferences prefs =
                AppBase.getInstance().getBaseContext().getSharedPreferences(Constants.pref, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(driver);
        editor.putString(Constants.Driver, json);
        editor.commit();

    }

    public static Driver getPreferencesUser() {

        SharedPreferences prefs =
                AppBase.getInstance().getBaseContext().getSharedPreferences(Constants.pref, Context.MODE_PRIVATE);

        Gson gson = new Gson();
        String json = prefs.getString(Constants.Driver, "");
        Driver obj = gson.fromJson(json, Driver.class);

        return obj;

    }


    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public static void saveLogin(Boolean login) {
        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putBoolean(Constants.KEY_LOGIN, login)
                .apply();
    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static Boolean getSavedLogin() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getBaseContext())
                .getBoolean(Constants.KEY_LOGIN, false);
    }





    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public static void saveOnline(Boolean isOnline) {
        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putBoolean(Constants.KEY_ONLINE, isOnline)
                .apply();
    }

    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static Boolean getSavedOnline() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getBaseContext())
                .getBoolean(Constants.KEY_ONLINE, false);
    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    static String getSavedLocationResult() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getBaseContext())
                .getString(Constants.KEY_LOCATION_UPDATES_RESULT, "");
    }


    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
   public static void saveResults(String result) {
        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getBaseContext())
                .edit()
                .putString(Constants.KEY_LOCATION_UPDATES_RESULT, result)
                .apply();
    }


    public void clear(){

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getBaseContext())
                .edit().clear().apply();
    }



}
