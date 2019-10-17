package co.com.autolagos.rtaxi.local.driver.view.login.network;

import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;

public interface LoginInterface {

    void loginSuccess(Driver driver);
    void loginError(String error);

}
