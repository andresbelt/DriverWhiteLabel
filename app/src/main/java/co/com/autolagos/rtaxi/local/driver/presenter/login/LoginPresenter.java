package co.com.autolagos.rtaxi.local.driver.presenter.login;

import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;

public interface LoginPresenter {

    void login(BodyLogin bodyLogin);

    void loginSuccess(Driver driver);
    void loginError(String error);

}
