package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Coordinate {

    private double latitude;
    private double longitude;

    public Coordinate() {}
    public Coordinate(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //Getters
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }

    //Setters
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    //ToString
    @Override
    public String toString() {
        return "Coordinate{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
