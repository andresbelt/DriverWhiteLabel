package co.com.autolagos.rtaxi.local.driver.model.utils;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.Gson;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;

import java.util.HashMap;
import java.util.Objects;

import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.Customer;
import co.com.autolagos.rtaxi.local.driver.model.entities.Journey;

public class ConvertDatasnapshopCareer {

    public ConvertDatasnapshopCareer() {
    }

    @SuppressWarnings("unchecked")
    public static Career convertDatasnapToCareer(DataSnapshot dataSnapshot) {
        Career career = new Career();
        career.setUid(dataSnapshot.getKey());
        career.setG(dataSnapshot.child(Constants.georeference).getValue(String.class));
        career.setUidTipoServicio(dataSnapshot.child(Constants.uidTipoServicio).getValue(Integer.class));
        career.setJourney(convertHashmapToJourney((HashMap<String, Objects>) dataSnapshot.child(Constants.Journey).getValue()));
        career.setFecha(dataSnapshot.child(Constants.fecha).getValue(Long.class));
        career.setUidUser(dataSnapshot.child(Constants.UidUser).getValue(String.class));

        if (dataSnapshot.child(Constants.UidConductor).getValue(String.class) != null)
            career.setUidConductor(dataSnapshot.child(Constants.UidConductor).getValue(String.class));
        else
            career.setUidConductor(null);
//        career.setAddress((String) dataSnapshot.child("address").getValue());
        int status = dataSnapshot.child(Constants.status).getValue(Integer.class);
        Career.serviceType stado = Career.fromInt(status);
        career.setStatus(stado);
        return career;
    }


    public static Career changeDatasnapToCareer(RxFirebaseChildEvent<DataSnapshot> dataSnapshot) {
        Career career = new Career();
        Career careerpreference= CareerPreference.show();

        career.setUid(careerpreference.getUid());

        if (dataSnapshot.getKey().equals(Constants.georeference))
            career.setG(dataSnapshot.getValue().getValue(String.class));
        else
            career.setG(careerpreference.getG());

        if (dataSnapshot.getKey().equals(Constants.uidTipoServicio))
            career.setUidTipoServicio(dataSnapshot.getValue().getValue(Integer.class));
        else
            career.setUidTipoServicio(careerpreference.getUidTipoServicio());

        if (dataSnapshot.getKey().equals(Constants.Journey))
            career.setJourney(convertHashmapToJourney((HashMap<String, Objects>) dataSnapshot.getValue().getValue()));
        else
            career.setJourney(careerpreference.getJourney());

        if (dataSnapshot.getKey().equals(Constants.fecha))
            career.setFecha(dataSnapshot.getValue().getValue(Long.class));
        else
            career.setFecha(careerpreference.getFecha());

        if (dataSnapshot.getKey().equals(Constants.UidUser))
            career.setUidUser(dataSnapshot.getValue().getValue(String.class));
        else
            career.setUidUser(careerpreference.getUidUser());


        if (dataSnapshot.getKey().equals(Constants.UidConductor)){
            if (dataSnapshot.getValue().getValue(String.class) != null)
                career.setUidConductor(dataSnapshot.getValue().getValue(String.class));
            else
                career.setUidConductor(null);
        }else
            career.setUidConductor(careerpreference.getUidConductor());
//        career.setAddress((String) dataSnapshot.child("address").getValue());


        if (dataSnapshot.getKey().equals(Constants.status)) {
            int status = dataSnapshot.getValue().getValue(Integer.class);

            Career.serviceType stado = Career.fromInt(status);
            career.setStatus(stado);
        }else
            career.setStatus(careerpreference.getStatus());

        return career;
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
