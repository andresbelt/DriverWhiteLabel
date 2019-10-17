package co.com.autolagos.rtaxi.local.driver.presenter.preferences;

import android.content.Context;

import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.view.map.preferences.PreferenceInterface;

public class MapPresenterImpl implements DriverPresenter.MapPreferencePresenter {

    private Context context;
    private PreferenceInterface preferenceInterface;

    public MapPresenterImpl(Context context, PreferenceInterface preferenceInterface) {
        this.context = context;
        this.preferenceInterface = preferenceInterface;
    }

    @Override
    public void getDriverData() {
        DriverPreferences driverPreferences = new DriverPreferences(context, this);
        driverPreferences.show();
    }

    @Override
    public void showDriverData(Driver driver) {
        preferenceInterface.showDriverData(driver);
    }
}
