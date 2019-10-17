package co.com.autolagos.rtaxi.local.driver.presenter.preferences;

import android.content.Context;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.view.login.preferences.PreferenceInterface;

public class LoginPresenterImpl implements DriverPresenter.LoginPreferencePresenter {

    private Context context;
    private PreferenceInterface preferenceInterface;

    public LoginPresenterImpl(Context context, PreferenceInterface preferenceInterface) {
        this.context = context;
        this.preferenceInterface = preferenceInterface;
    }

    @Override
    public void storeDriver(Driver driver) {
        DriverPreferences driverPreferences = new DriverPreferences(context, this);
        driverPreferences.store(driver);
    }

    @Override
    public void successStore(boolean success) {
        preferenceInterface.successStore(success);
    }
}
