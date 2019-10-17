package co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver;

import com.google.gson.Gson;
import android.content.Context;
import android.content.SharedPreferences;
import co.com.autolagos.rtaxi.local.driver.base.Constans;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.DriverPresenter;

public class DriverPreferences {

    private Context context;
    private DriverPresenter.LoginPreferencePresenter driverPresenter;
    private DriverPresenter.MapPreferencePresenter mapPreferencePresenter;
    private boolean flag;

    public DriverPreferences(Context context, DriverPresenter.LoginPreferencePresenter driverPresenter) {
        this.context = context;
        this.driverPresenter = driverPresenter;
        flag = true;
    }
    public DriverPreferences(Context context, DriverPresenter.MapPreferencePresenter mapPreferencePresenter) {
        this.context = context;
        this.mapPreferencePresenter = mapPreferencePresenter;
        flag = true;
    }
    public DriverPreferences(Context context) {
        this.context = context;
        flag = false;
    }

    public void store(Driver driver) {
        SharedPreferences preferences = context.getSharedPreferences(Constans.DRIVER_DB, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String jsonDirver = new Gson().toJson(driver);
        editor.putString("driver", jsonDirver);
        editor.apply();
        if(flag) {
            driverPresenter.successStore(true);
        }
    }

    public Driver show() {
        Driver driver = new Driver();
        SharedPreferences preferences = context.getSharedPreferences(Constans.DRIVER_DB, Context.MODE_PRIVATE);
        String jsonDriver = preferences.getString("driver", "");
        if(!jsonDriver.isEmpty()) {
            driver = new Gson().fromJson(jsonDriver, Driver.class);
        }
        if(flag) {
            mapPreferencePresenter.showDriverData(driver);
        }
        return driver;
    }

}
