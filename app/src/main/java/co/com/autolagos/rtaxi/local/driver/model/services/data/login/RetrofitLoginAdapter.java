package co.com.autolagos.rtaxi.local.driver.model.services.data.login;

import android.support.annotation.NonNull;
import com.google.gson.JsonObject;

import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.services.data.ApiService;
import co.com.autolagos.rtaxi.local.driver.presenter.login.LoginPresenter;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.gson.Gson;

import org.json.JSONObject;

public class RetrofitLoginAdapter implements Callback<JsonObject> {

    private LoginPresenter loginPresenter;
    private FirebaseDataStore mFirebaseDataStore;


    public RetrofitLoginAdapter(LoginPresenter loginPresenter, FirebaseDataStore FirebaseDataStore) {
        this.loginPresenter = loginPresenter;
        mFirebaseDataStore = FirebaseDataStore;
    }

    private OkHttpClient setupOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder().addInterceptor(interceptor).build();
    }

    public void login(BodyLogin body) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL)
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
            JSONObject json = new JSONObject();
            Driver driver = new Gson().fromJson(jsonObject, Driver.class);
            mFirebaseDataStore.loginToken(driver.getToken()).subscribe(authResult -> {
                loginPresenter.loginSuccess(driver);}
            ,throwable -> {
                loginPresenter.loginError("Code error: " + response.code());
            });


        } else {
            loginPresenter.loginError("Code error: " + response.code());
        }
    }

    @Override
    public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
        loginPresenter.loginError(t.getMessage());
    }

}
