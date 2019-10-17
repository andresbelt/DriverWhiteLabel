package co.com.autolagos.rtaxi.local.driver.presenter.preferences;

import android.content.Context;

import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.view.login.preferences.PreferenceInterface;

public class LoginPresenterImpl implements FirebasePresenter.LoginPreferencePresenter {

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
        Preferences.saveLogin(true);

    }

    @Override
    public void successStore(boolean success) {
        preferenceInterface.successStore(success);
    }
}
