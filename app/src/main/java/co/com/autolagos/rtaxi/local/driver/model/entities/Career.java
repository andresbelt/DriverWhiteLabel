package co.com.autolagos.rtaxi.local.driver.model.entities;

public class Career {

    private String uid;
    private Journey journey;
    private Career.serviceType status;
    private String uidConductor;
    private Long fecha;
    private String g;

    public String getUidUser() {
        return UidUser;
    }

    public void setUidUser(String uidUser) {
        UidUser = uidUser;
    }

    private String UidUser;

    public String getUidConductor() {
        return uidConductor;
    }

    public void setUidConductor(String uidConductor) {
        this.uidConductor = uidConductor;
    }


//formato timestamp
    public Long getFecha() {
        return fecha;
    }

        public void setFecha(Long fecha) {
        this.fecha = fecha;
    }



    public String getG() {
        return g;
    }

    public void setG(String g) {
        this.g = g;
    }

    public int getUidTipoServicio() {
        return UidTipoServicio;
    }

    public void setUidTipoServicio(int uidTipoServicio) {
        UidTipoServicio = uidTipoServicio;
    }

    private int UidTipoServicio;

    public Career() {}
    public Career(String uid, Journey journey, Career.serviceType status,int UidTipoServicio,String georeference,Long fecha) {
        this.uid = uid;
        this.journey = journey;
        this.status = status;
        this.fecha = fecha;
        this.UidTipoServicio = UidTipoServicio;
        this.g=georeference;
    }

    //Getters
    public String getUid() {
        return uid;
    }
    public Journey getJourney() {
        return journey;
    }
    public Career.serviceType getStatus() {
        return status;
    }

    //Setters
    public void setUid(String uid) {
        this.uid = uid;
    }
    public void setJourney(Journey journey) {
        this.journey = journey;
    }
    public void setStatus(Career.serviceType status) {
        this.status = status;
    }

    //ToString
    @Override
    public String toString() {
        return "Career{" +
                "uid='" + uid + '\'' +
                ", Journey=" + journey +
                ", status=" + status +
                ", service=" + UidTipoServicio +
                '}';
    }


    public static serviceType fromInt(int i) {
        for (serviceType typе : serviceType.values()) {
            if (typе.ordinal() == i) {
                return typе;
            }
        }
        return null;
    }

    public enum serviceType {
        NULL,
        Available,
        Accepted,
        Pickup,
        Travel,
        Finish,
        Canceled,
        Cambios
    }


}
