package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Petition {

    private String uid;
    private Customer customer;
    private float latitude;
    private float longitude;
    private String address;

    public Petition() {}
    public Petition(String uid, Customer customer, float latitude, float longitude, String address) {
        this.uid = uid;
        this.customer = customer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    //Getters
    public String getUid() {
        return uid;
    }
    public Customer getCustomer() {
        return customer;
    }
    public float getLatitude() {
        return latitude;
    }
    public float getLongitude() {
        return longitude;
    }
    public String getAddress() {
        return address;
    }

    //Setters
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    //ToString
    @Override
    public String toString() {
        return "Petition{" +
                "uid='" + uid + '\'' +
                ", customer=" + customer +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                '}';
    }
}
