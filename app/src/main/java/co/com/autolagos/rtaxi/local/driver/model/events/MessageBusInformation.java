package co.com.autolagos.rtaxi.local.driver.model.events;

import android.location.Location;

import com.google.android.gms.maps.CameraUpdate;
import com.google.firebase.database.DataSnapshot;
import com.google.gson.JsonObject;

import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.LocationStatus;
import co.com.autolagos.rtaxi.local.driver.model.entities.User;

public class MessageBusInformation {

    private Enum option;
    private String message;
    private DataSnapshot jsonObject;
    private Career career;
    private CameraUpdate location;
    private Location mlocation;

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(Location lastLocation) {
        this.lastLocation = lastLocation;
    }

    private Location lastLocation;

    public MessageBusInformation(Career.serviceType option) {
        this.option = option;
    }
    public MessageBusInformation(Career.serviceType option, CameraUpdate location) {
        this.option = option;
        this.location = location;
    }
    public MessageBusInformation(User.serviceType optionUser, String message) {
        this.option = optionUser;
        this.message = message;
    }

    public MessageBusInformation(Career.serviceType option, String message, Career career) {
        this.option = option;
        this.career = career;
        this.message = message;
    }

    public MessageBusInformation(LocationStatus option, Location lastLocation, Location location) {
        this.mlocation = location;
        this.lastLocation = lastLocation;
        this.option = option;
    }




    public MessageBusInformation(LocationStatus option) {
        this.option = option;
    }


    //region Getters
    public Enum getOption() {
        return option;
    }
    public String getMessage() {
        return message;
    }
    public DataSnapshot getJsonObject() {
        return jsonObject;
    }
    public Career getCareer() {
        return career;
    }
    public CameraUpdate getLocation() {
        return location;
    }
    public Location getmLocation() {
        return mlocation;
    }
    //endregion

}
