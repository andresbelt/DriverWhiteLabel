package co.com.autolagos.rtaxi.local.driver.model.services.data;

import com.google.gson.JsonObject;

import co.com.autolagos.rtaxi.local.driver.model.entities.BodyLogin;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @POST("login")
    @Headers("Content-Type: application/json")
    Call<JsonObject> login(
            @Body BodyLogin body
    );

}
