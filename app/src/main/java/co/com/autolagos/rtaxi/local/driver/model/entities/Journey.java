package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Journey {

    private Coordinate origin;

    public Coordinate getOrigin() {
        return origin;
    }

    public void setOrigin(Coordinate origin) {
        this.origin = origin;
    }

    public Coordinate getDestiny() {
        return destiny;
    }

    public void setDestiny(Coordinate destiny) {
        this.destiny = destiny;
    }

    private Coordinate destiny;

    public Journey() {}
    public Journey(Coordinate Origin, Coordinate Destiny) {
        this.origin = Origin;
        this.destiny = Destiny;
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
