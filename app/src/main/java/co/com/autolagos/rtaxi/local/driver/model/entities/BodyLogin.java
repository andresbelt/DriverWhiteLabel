package co.com.autolagos.rtaxi.local.driver.model.entities;

public class BodyLogin {

    private String placa;
    private String cedula;
    private boolean isDriver;

    public BodyLogin(String placa, String cedula, boolean isDriver) {
        this.placa = placa;
        this.cedula = cedula;
        this.isDriver = isDriver;
    }

}
