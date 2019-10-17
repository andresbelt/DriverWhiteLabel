package co.com.autolagos.rtaxi.local.driver.utils;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String URL = "https://api-rest-local.herokuapp.com";
    public static final String DRIVER_DB = "driver_db";
    public static final String driversAvailable = "ConductoresDisponibles";
    public static final String driversWorking = "ConductoresTrabajando";
    public static final String trips = "ViajesSolicitud";
    public static final String status = "Estado";
    public static final String georeference = "g";
    public static final String location = "location";
    public static final String lastLocation = "LastLocation";
    public static final String UidConductor = "UidConductor";
    public static final String UidUser ="UidUser";
    public static final String Users = "Users";
    public static final String Customers = "Customers";
    public static final String Email = "Email";
    public static final String Name = "Name";
    public static final String Calificacion = "Calificacion";
    public static String uidTipoServicio="UidTipoServicio";

    public static final String driverUid = "DriverUid";
    public static final String carrers = "Carrers";
    public static final String locationDriver = "LocationDriver";
    public static String Lat = "lat";
    public static String Lon= "lon";
    public static String users ="Users";
    public static String drivers="Drivers";
    public static String Journey ="Trayecto";
    public static String Origin ="origin";
    public static String fecha="Fecha";
    public static String FechaAceptado ="FechaAceptado";



    //--Preference--
    public final static String KEY_LOCATION_UPDATES_RESULT = "location-update-result";
    public final static String KEY_ONLINE = "location-online";
    public final static String KEY_LOGIN = "Login";
    public static String isLogin = "isLogin";
    public static String Driver = "user";
    public static String pref = "MisPreferencias";
    public final static String PRIMARY_CHANNEL = "default";

    public static final String KEY_TARIFA_CAREER = "TARIFA_CAREER";
    public static final String KEY_CAREER = "CAREER";
    public static final String KEY_ACCEPT_CAREER = "ACCEPTCAREER";
    public static final String KEY_STATUS_CAREER = "STATUSCAREER";
    public static final String KEY_INICIO_CAREER = "INICIOCAREER";
    public static final String KEY_LOCATION_CAREER = "LOCATIONCAREER";
    public static final String KEY_TIME_START_CAREER = "TIME_START_CAREER";
    public static final String KEY_TIME_WAIT_CAREER = "TIME_WAIT_CAREER";
    public static final String KEY_DISTANCE_TOTAL_CAREER = "DISTANCE_TOTAL_CAREER";
    public static final String KEY_TIME_TOTAL_CAREER = "TIME_TOTAL_CAREER";

    public static final String KEY_DRIVER = "DRIVER";
    public static final String KEY_DRIVER_UID = "DRIVER_UID";

    //--Preference--


    public static final boolean SUCCESS = true;
    public static final boolean FAILURE = false;


    public static float km = 2000;
    public static float kmCercano = 500;


    public static String tripsAccept="ViajesAceptados";

    public static String urlBase = "http://192.168.1.133:3000/";
    public static String urlFacebook = "register";
    public static String urlGoogle = "login";
    public static String urlLogin = "loginUsuarioMovil";
    public static String urlRegister = "register";
    public final static int REQUEST_LOCATION = 199;
    public static final String KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates";
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1001;
    public static String Destiny="destiny";
    public static String inicio="FechaInicioTrayecto";
    public static String tiempoespera="TiempoEspera";
    public static String tiemporuta="TiempoRuta";
    public static String rutapoints="rutapoints";

    public static String history="History";
    public static String distancia="Distancia";

    //--Cooro--
    public static final String PP_FC = "PP_FC" ;
    public static final String PP_ = "PP_" ;
    public static final String MINIMA_FC = "MINIMA_FC" ;
    public static final String MINIMA_ = "MINIMA_" ;
    public static final String AEROPUERTO_FC = "AEROPUERTO_FC" ;
    public static final String NOCTURNO_FC = "NOCTURNO_FC" ;
    public static final String NOCTURNO_ ="NOCTURNO_" ;

    public static final String AEROPUERTO_ = "AEROPUERTO_" ;
    public static final String COSTO_KM_FC = "COSTO_KM_FC" ;
    public static final String BANDERA_ = "BANDERA_" ;
    public static final String BANDERA_FC = "BANDERA_FC" ;
    public static final String COSTO_KM_ = "COSTO_KM_" ;

    public static final String MIN_FIN_RECARGO = "MIN_FIN_RECARGO" ;
    public static final String HORA_FIN_RECARGO = "HORA_FIN_RECARGO" ;
    public static final String MIN_INI_RECARGO = "MIN_INI_RECARGO" ;
    public static final String HORA_INI_RECARGO = "HORA_INI_RECARGO" ;
    public static String mock_location="mock_location";


    public static List<LatLng> getPointsAeropuerto(){

        List<LatLng> points= new ArrayList<>();

        points.add(new LatLng( 4.7110451,-74.1722217)) ;
        points.add(new LatLng(  4.7107272,-74.1721685));
        points.add(new LatLng(  4.7105077,-74.172168));
        points.add(new LatLng( 4.7096826,-74.17205));
        points.add(new LatLng( 4.7094706, -74.1707406));
        points.add(new LatLng( 4.709183,-74.1709229));
        points.add(new LatLng( 4.7087666,-74.1707406));
        points.add(new LatLng( 4.7046184,-74.1705507));
        points.add(new LatLng( 4.7045948,-74.1704216));
        points.add(new LatLng( 4.7045306,-74.1703912));
        points.add(new LatLng(  4.7042683,-74.1701434));
        points.add(new LatLng( 4.704279,-74.1696654));
        points.add(new LatLng( 4.7044396,-74.168295));
        points.add(new LatLng( 4.7052906,-74.1677051));
        points.add(new LatLng( 4.704288,-74.1671411));
        points.add(new LatLng( 4.6975566,-74.1651164));
        points.add(new LatLng( 4.6832596,-74.1638626));
        points.add(new LatLng( 4.6835888,-74.1548064));
        points.add(new LatLng( 4.6842474,-74.1358586));
        points.add(new LatLng( 4.6861481,-74.1354712));
        points.add(new LatLng( 4.6866449,-74.1352624));
        points.add(new LatLng( 4.6893037,-74.1338438));
        points.add(new LatLng(  4.6909497,-74.1335214));
        points.add(new LatLng(  4.6909731,-74.1315354));
        points.add(new LatLng( 4.6909751,-74.130358));
        points.add(new LatLng(  4.6909408,-74.130305));
        points.add(new LatLng( 4.6908328,-74.1302489));
        points.add(new LatLng( 4.6905826,-74.130125));
        points.add(new LatLng( 4.6891951,-74.1299455));
        points.add(new LatLng( 4.6876888,-74.1298323));
        points.add(new LatLng( 4.6875299,-74.128201));
        points.add(new LatLng( 4.6869258,-74.126429));
        points.add(new LatLng(4.6891583,-74.126242));
        points.add(new LatLng( 4.6898446,-74.125532));
        points.add(new LatLng(  4.6907952,-74.12382));
        points.add(new LatLng( 4.6899584,-74.124629));
        points.add(new LatLng( 4.6899334,-74.123916));
        points.add(new LatLng( 4.6922368,-74.122800));
        points.add(new LatLng( 4.6923179,-74.122766));
        points.add(new LatLng(  4.6924499,-74.121074));
        points.add(new LatLng(  4.692612,-74.121177));
        points.add(new LatLng( 4.6927734,-74.12135));
        points.add(new LatLng(  4.6930143,-74.121610));
        points.add(new LatLng(  4.6936893,-74.12188));
        points.add(new LatLng( 4.6943599,-74.122275));
        points.add(new LatLng(  4.7043367,-74.123203));
        points.add(new LatLng(  4.7045883,-74.124086));
        points.add(new LatLng( 4.7174567,-74.137353));
        points.add(new LatLng(  4.7170058,-74.137590));
        points.add(new LatLng( 4.7170966,-74.154449));
        points.add(new LatLng(  4.7194432,-74.156118));
        points.add(new LatLng( 4.7197914,-74.156422));
        points.add(new LatLng( 4.7199428,-74.159035));
        points.add(new LatLng( 4.7198974,-74.159658));
        points.add(new LatLng( 4.7153825,-74.160280));
        points.add(new LatLng( 4.7152311,-74.160569));
        points.add(new LatLng( 4.7150192,-74.168894));
        points.add(new LatLng( 4.7113479,-74.169137));
        points.add(new LatLng( 4.7110451,-74.16934));


    return points;
    }


    public enum GpsStatus {
        Enable,
        NoEnable;
    }

    public enum NetworkStatus {
        CONNECTED,
        DISCONNECTED;
    }

}
