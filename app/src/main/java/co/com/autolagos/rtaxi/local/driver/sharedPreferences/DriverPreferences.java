package co.com.autolagos.rtaxi.local.driver.sharedPreferences;

import com.google.gson.Gson;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.FirebasePresenter;

public class DriverPreferences {


    private FirebasePresenter.LoginPreferencePresenter driverPresenter;
    private FirebasePresenter.MapPreferencePresenter mapPreferencePresenter;
    private boolean flag;

    public DriverPreferences(Context context, FirebasePresenter.LoginPreferencePresenter driverPresenter) {
        this.driverPresenter = driverPresenter;
        flag = true;
    }
    public DriverPreferences(Context context, FirebasePresenter.MapPreferencePresenter mapPreferencePresenter) {
        this.mapPreferencePresenter = mapPreferencePresenter;
        flag = true;
    }
    public DriverPreferences(Context context) {
        flag = false;
    }

    public void store(Driver driver) {
        String jsonDriver = new Gson().toJson(driver);

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putString(Constants.KEY_DRIVER, jsonDriver)
                .apply();

        if(flag) {
            driverPresenter.successStore(true);
        }
    }

    public Driver show() {
        Driver driver = new Driver();

        String jsonDriver = PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getString(Constants.KEY_DRIVER, "");

        if(!jsonDriver.isEmpty()) {
            driver = new Gson().fromJson(jsonDriver, Driver.class);
        }
        if(flag) {
            mapPreferencePresenter.showDriverData(driver);
        }

        return driver;
    }

    public static void setUid(String uid) {
        String jsonDriver = new Gson().toJson(uid);

        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putString(Constants.KEY_DRIVER_UID, jsonDriver)
                .apply();
    }

    public String getUid(String uid) {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getString(Constants.KEY_DRIVER_UID, "");
    }
}
