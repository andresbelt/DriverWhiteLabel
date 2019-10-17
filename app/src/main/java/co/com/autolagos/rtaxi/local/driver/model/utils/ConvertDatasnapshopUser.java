package co.com.autolagos.rtaxi.local.driver.model.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Objects;

import co.com.autolagos.rtaxi.local.driver.model.entities.User;
import co.com.autolagos.rtaxi.local.driver.model.entities.Customer;
import co.com.autolagos.rtaxi.local.driver.model.entities.Journey;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;

public class ConvertDatasnapshopUser {

    @SuppressWarnings("unchecked")
    public static User convertDatasnapToCareer(DataSnapshot dataSnapshot) {
        User user = new User();
        user.setName(dataSnapshot.child(Constants.Name).getValue(String.class));
        user.setEmail(dataSnapshot.child(Constants.Email).getValue(String.class));
        user.setCalificacion(dataSnapshot.child(Constants.Calificacion).getValue(String.class));

        return user;
    }

    private static Customer convertHashmapToCustomer(HashMap<String, Objects> hashMap) {
        String json = new Gson().toJson(hashMap);
        return new Gson().fromJson(json, Customer.class);
    }

    private static Journey convertHashmapToJourney(HashMap<String, Objects> hashMap) {
        String json = new Gson().toJson(hashMap);
        return new Gson().fromJson(json, Journey.class);
    }




}
