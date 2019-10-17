package co.com.autolagos.rtaxi.local.driver.model.services.data.remote.login;

import android.support.annotation.NonNull;
import com.google.gson.JsonObject;
import co.com.autolagos.rtaxi.local.driver.base.Constans;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.services.data.remote.ApiService;
import co.com.autolagos.rtaxi.local.driver.presenter.login.LoginPresenter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

public class RetrofitLoginAdapter implements Callback<JsonObject> {

    private LoginPresenter loginPresenter;

    public RetrofitLoginAdapter(LoginPresenter loginPresenter) {
        this.loginPresenter = loginPresenter;
    }

    private OkHttpClient setupOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    public void login(BodyLogin body) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constans.URL)
                .client(setupOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiService api = retrofit.create(ApiService.class);
        Call<JsonObject> call = api.login(body);
        call.enqueue(this);
    }

    @Override
    public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
        if(response.isSuccessful()) {
            JsonObject jsonObject = response.body();
            Driver driver = new Gson().fromJson(jsonObject, Driver.class);
            loginPresenter.loginSuccess(driver);
        } else {
            loginPresenter.loginError("Code error: " + response.code());
        }
        StationBus.getBus().post(new MessageBusInformation(1, ""));
    }

    @Override
    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
        loginPresenter.loginError(t.getMessage());
    }

}
