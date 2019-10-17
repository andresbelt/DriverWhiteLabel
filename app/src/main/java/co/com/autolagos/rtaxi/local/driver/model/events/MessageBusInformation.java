package co.com.autolagos.rtaxi.local.driver.model.events;

import com.google.firebase.database.DataSnapshot;
import com.google.gson.JsonObject;

public class MessageBusInformation {

    private int option;
    private String message;
    private DataSnapshot jsonObject;

    public MessageBusInformation(int option, String message) {
        this.option = option;
        this.message = message;
    }

    public MessageBusInformation(int option, String message, DataSnapshot jsonObject) {
        this.option = option;
        this.message = message;
        this.jsonObject = jsonObject;
    }

    //region Getters
    public int getOption() {
        return option;
    }
    public String getMessage() {
        return message;
    }
    public DataSnapshot getJsonObject() {
        return jsonObject;
    }
    //endregion

    //

}
