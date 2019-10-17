package co.com.autolagos.rtaxi.local.driver.presenter.preferences;

import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;

public interface FirebasePresenter {

    interface LoginPreferencePresenter {

        void storeDriver(Driver driver);

        void successStore(boolean success);

    }

    interface MapPreferencePresenter {

        void getDriverData();

        void showDriverData(Driver driver);

        void changeStatusCareer(int status, Career career, Driver driver);

        void resultStatusCareer(boolean result);

    }

}
