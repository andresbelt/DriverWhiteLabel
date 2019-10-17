package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Coordinate {

    private double lat;
    private double lon;

    public Coordinate() {}
    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;

    }

    //Getters
    public double getLat() {
        return lat;
    }
    public double getLon() {
        return lon;
    }

    //Setters
    public void setLat(double latitude) {
        this.lat = latitude;
    }
    public void setLon(double longitude) {
        this.lon = longitude;
    }

    //ToString
    @Override
    public String toString() {
        return "Coordinate{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
