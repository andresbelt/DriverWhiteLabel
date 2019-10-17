package co.com.autolagos.rtaxi.local.driver.presenter.login;

import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.services.data.login.RetrofitLoginAdapter;
import co.com.autolagos.rtaxi.local.driver.view.login.network.LoginInterface;

public class LoginPresenterImpl implements LoginPresenter {

    private LoginInterface loginInterface;

    public LoginPresenterImpl(LoginInterface loginInterface) {
        this.loginInterface = loginInterface;
    }

    @Override
    public void login(BodyLogin body) {
        RetrofitLoginAdapter loginAdapter = new RetrofitLoginAdapter(this,new FirebaseDataStore());
        loginAdapter.login(body);
    }

    @Override
    public void loginSuccess(Driver driver) {
        loginInterface.loginSuccess(driver);
    }

    @Override
    public void loginError(String error) {
        loginInterface.loginError(error);
    }
}
