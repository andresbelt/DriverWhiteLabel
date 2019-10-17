package co.com.autolagos.rtaxi.local.driver.model.entities;

public class User {


    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getmDevId() {
        return mDevId;
    }

    public void setmDevId(String mDevId) {
        this.mDevId = mDevId;
    }

    public LatLong getmLocation() {
        return mLocation;
    }

    public void setmLocation(LatLong mLocation) {
        this.mLocation = mLocation;
    }

    private  String calificacion= "";

    private  String picture = "";
    private  String name= "";
    private  String id= "";
    private  String email= "";
    private  String account= "";
    private String mDevId;
    private LatLong mLocation;


    public User(String picture, String displayName, String uid, String email, String account) {
        this.picture= picture;
        this.name= displayName;
        this.picture= uid;
        this.mDevId = "";
        mLocation = new LatLong();
        this.email= email;
        this.account= account;
    }



    public User() {
        this.picture= "";
        this.name= "";
        this.picture= "";
        this.mDevId = "";
        mLocation = new LatLong();
        this.email= "";
        this.account= "";
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public void setDevId(String devId) {
        mDevId = devId;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevId() {
        return mDevId;
    }


    public static class LatLong {
        public double latitude;
        public double longitude;

        public LatLong() {
            this(0.0, 0.0);
        }

        public LatLong(double lat, double lng) {
            latitude = lat;
            longitude = lng;
        }
    }
    public LatLong getLocation() {
        return mLocation;
    }


    public void setLocation(LatLong location) {
        mLocation = location;
    }


    public String getPicture() {
        return picture;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }


    public enum serviceType {
        NULL,
        LOGIN,
        LOGOUT
    }

}