package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Journey {

    private Coordinate origin;
    private Coordinate destiny;

    public Journey() {}
    public Journey(Coordinate origin, Coordinate destiny) {
        this.origin = origin;
        this.destiny = destiny;
    }

    //Getters
    public Coordinate getOrigin() {
        return origin;
    }
    public Coordinate getDestiny() {
        return destiny;
    }

    //Setters
    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }
    public void setDestiny(Coordinate destiny) {
        this.destiny = destiny;
    }

    //ToString
    @Override
    public String toString() {
        return "Journey{" +
                "origin=" + origin +
                ", destiny=" + destiny +
                '}';
    }
}
