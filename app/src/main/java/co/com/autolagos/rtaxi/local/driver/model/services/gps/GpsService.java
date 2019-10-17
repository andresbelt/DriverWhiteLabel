package co.com.autolagos.rtaxi.local.driver.model.services.gps;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.services.data.firebase.FirebaseHelper;
import co.com.autolagos.rtaxi.local.driver.model.services.data.local.preferences.driver.DriverPreferences;

public class GpsService extends Service {

    //Components
    private Context context;
    private FirebaseHelper firebaseHelper;
    private LocationListener locationListener;
    private LocationManager locationManager;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        System.out.println("Servicio creado...");
        setupComponents();
    }

    private void setupComponents() {
        context = this;
        firebaseHelper = new FirebaseHelper(context);
        setupLocationmanager();
    }

    @SuppressLint("MissingPermission")
    private void setupLocationmanager() {
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(gpsEnabled) {
            setupLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, locationListener);
        }
    }

    private void setupLocationListener() {
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                firebaseHelper.listenerChangesPetitions(latitude, longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                //StationBus.getBus().post(new MessageBusInformation(1, ""));
            }

            @Override
            public void onProviderEnabled(String provider) {
                StationBus.getBus().post(new MessageBusInformation(1, provider + " enabled"));
            }

            @Override
            public void onProviderDisabled(String provider) {
                StationBus.getBus().post(new MessageBusInformation(2, provider + " disabled"));
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("Servicio gps iniciado");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(locationListener);
            System.out.println("Servicio gps detenido");
        }

    }

}
