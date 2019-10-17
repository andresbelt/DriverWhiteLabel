package co.com.autolagos.rtaxi.local.driver.presenter.preferences;

import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;

public interface DriverPresenter {

    interface LoginPreferencePresenter {

        void storeDriver(Driver driver);

        void successStore(boolean success);

    }

    interface MapPreferencePresenter {

        void getDriverData();

        void showDriverData(Driver driver);

    }

}
