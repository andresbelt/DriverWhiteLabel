package co.com.autolagos.rtaxi.local.driver.sharedPreferences.career;

import android.location.Location;
import android.preference.PreferenceManager;

import com.google.gson.Gson;

import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;

public class CareerPreference {





    /**
     * Saves location result as a string to {@link android.content.SharedPreferences}.
     */
    public static void store(Career career) {

        String jsonCareer = new Gson().toJson(career);

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putString(Constants.KEY_CAREER, jsonCareer)
                .apply();
    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static Career show() {
        Career career = new Career();
        String preferences = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getString(Constants.KEY_CAREER, "");

        if (!preferences.isEmpty()) {
            career = new Gson().fromJson(preferences, Career.class);
        }

        return career;
    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static boolean getAcceptCareer() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getBoolean(Constants.KEY_ACCEPT_CAREER, false);
    }

    public static void setAcceptCareer(boolean accept) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putBoolean(Constants.KEY_ACCEPT_CAREER, accept)
                .apply();

    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static long getInicioCareer() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getLong(Constants.KEY_INICIO_CAREER, 0);
    }

    public static void setInicioCareer() {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putLong(Constants.KEY_INICIO_CAREER, System.currentTimeMillis())
                .apply();

    }




    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static Career.serviceType getStatusServiceCareer() {

        Career.serviceType c = Career.serviceType.Available;


        int po = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getInt(Constants.KEY_STATUS_CAREER, 1);

        switch (po) {

            case 1:
                return Career.serviceType.Available;

            case 2:
                return Career.serviceType.Accepted;

            case 3:
                return Career.serviceType.Pickup;

            case 4:
                return Career.serviceType.Travel;

            case 5:
                return Career.serviceType.Finish;

            case 6:
                return Career.serviceType.Canceled;

            case 7:
                return Career.serviceType.Cambios;

             default:
                 return Career.serviceType.Canceled;

        }


    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static void setStatusCareer(int status) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putInt(Constants.KEY_STATUS_CAREER, status)
                .apply();

    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static int getStatusCareer() {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getInt(Constants.KEY_STATUS_CAREER, 1);

    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static void setLocationStartCareer(Location location) {

        if (location != null) {
            Gson gson = new Gson();
            String json = gson.toJson(location);

            PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                    .edit()
                    .putString(Constants.KEY_LOCATION_CAREER, json)
                    .apply();
        }else{

            PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                    .edit()
                    .putString(Constants.KEY_LOCATION_CAREER, "")
                    .apply();
        }
    }


    /**
     * Fetches location results from {@link android.content.SharedPreferences}.
     */
    public static Location getLocationStartCareer() {

        Gson gson = new Gson();
        String json = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getString(Constants.KEY_LOCATION_CAREER, "");
        Location obj = gson.fromJson(json, Location.class);

        return obj;

    }


    public static void setStatusCareer(Career.serviceType canceled) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putInt(Constants.KEY_STATUS_CAREER, canceled.ordinal())
                .apply();
    }


    public static void setTimeWait(long timeWaitTime) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putLong(Constants.KEY_TIME_WAIT_CAREER, timeWaitTime)
                .apply();
    }

    public static long getTimeWait() {

        long json = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getLong(Constants.KEY_TIME_WAIT_CAREER, 0);
        return json;
    }

    public static double getDistance() {

        long json = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getLong(Constants.KEY_DISTANCE_TOTAL_CAREER, 0);
        return Double.longBitsToDouble(json);

    }

    public static long getTimeRoute() {

        long json = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getLong(Constants.KEY_TIME_TOTAL_CAREER, 0);
        return json;

    }


    public static void setDistance(double distanciakm) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putLong(Constants.KEY_DISTANCE_TOTAL_CAREER, Double.doubleToRawLongBits(distanciakm))
                .apply();
    }

    public static void setTimeRoute(long timeroute) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putLong(Constants.KEY_TIME_TOTAL_CAREER, timeroute)
                .apply();
    }

    public static double getTarifa() {

        long json = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getLong(Constants.KEY_TARIFA_CAREER, 0);
        return Double.longBitsToDouble(json);

    }

    public static void setTarifa(double tarifa) {

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putLong(Constants.KEY_TARIFA_CAREER, Double.doubleToRawLongBits(tarifa))
                .apply();
    }
}
