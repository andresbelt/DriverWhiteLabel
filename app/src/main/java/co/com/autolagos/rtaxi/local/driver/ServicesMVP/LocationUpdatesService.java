package co.com.autolagos.rtaxi.local.driver.ServicesMVP;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import co.com.autolagos.rtaxi.local.driver.GpsLocationReceiver;
import co.com.autolagos.rtaxi.local.driver.di.component.ActivityComponent;
import co.com.autolagos.rtaxi.local.driver.di.component.AppComponent;
import co.com.autolagos.rtaxi.local.driver.di.component.DaggerAppComponent;
import co.com.autolagos.rtaxi.local.driver.di.module.AppModule;
import co.com.autolagos.rtaxi.local.driver.di.module.ContextModule;
import co.com.autolagos.rtaxi.local.driver.model.entities.LocationStatus;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.di.component.DaggerActivityComponent;
import co.com.autolagos.rtaxi.local.driver.di.module.MvpModule;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoLocation;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.utils.ConvertDatasnapshopCareer;
import co.com.autolagos.rtaxi.local.driver.utils.Utils;
import rx.Observable;
import rx.Subscription;

/**
 * Created by Ayush Jain on 8/31/17.
 */

public class LocationUpdatesService extends Service
        implements MainContractServices.ViewCallBack {

    private static final String TAG = LocationUpdatesService.class.getSimpleName();

    private static final String ACTION_BROADCAST =
            LocationUpdatesService.class.getPackage().getName() + ".broadcast";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION =
            LocationUpdatesService.class.getPackage().getName() +
            ".started_from_notification";

    private static final int NOTIFICATION_ID = 1;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 3;
private static LocationUpdatesService mInstance;
    private final IBinder mBinder = new LocationBinder();

    /**
     * Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;
    private GeoQueryEventListener GeoListener;
    private LocationRequest mLocationRequest;


    private Handler mServiceHandler;
    private NotificationManager mNotificationManager;
    private boolean mChangingConfiguration = false;
    private String idCarrera;
    private Subscription subsp;

    public static double rideDistance;
    public static Location mLocation;
    public static Location mLocationr;
    public static Location lastLocation;
    public static Location pickupuserLocation;
    public float speedRoute;
    public static final String EXTRA_LOCATION =
            LocationUpdatesService.class.getPackage().getName() + ".location";

    final private static String PRIMARY_CHANNEL = "default";
    public static ArrayList<LatLng> points = new ArrayList<>();

    @Inject
    MainContractServices.PresenterCallBack PresenterLoginCallBack;


    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            Bundle bundle = intent.getExtras();

            Constants.GpsStatus gps = (Constants.GpsStatus) bundle.getSerializable(GpsLocationReceiver.filter);
            if (Constants.GpsStatus.NoEnable.equals(gps)) {


            } else {
                if (Utils.checkPermission() &&  Utils.checkHighAccuracyLocationMode()
                        ) {
                   // alertGpsStatus(true);
                } else {
                    //setupLocationRequest();

                }

            }

        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
//        init FusedLocationProviderClient


       DaggerAppComponent.builder()
                .appModule(new AppModule(this)).contextModule(new ContextModule(this))
                .build()
               .inject(this);


        mInstance = this;
        PresenterLoginCallBack.onAuthListener();
        LocalBroadcastManager.getInstance(this).registerReceiver(mNotificationReceiver,
                new IntentFilter(GpsLocationReceiver.filter));

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PRIMARY_CHANNEL,
                    this.getString(R.string.default_channel),
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setLightColor(Color.GREEN);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(channel);
        }



        GeoListener = new GeoQueryEventListener() {

            //entra
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (Preferences.getSavedOnline()) {
                    getInstance().PresenterLoginCallBack.getCarreraInfo(key).subscribe
                            (LocationUpdatesService.this::getCarreras,
                                    LocationUpdatesService.this::getCarrerasError);
                }
            }

            @Override
            public void onKeyExited(String key) {
                if (Preferences.getSavedOnline()) {
                   if (key.equals(CareerPreference.show().getUid())){
                       getInstance().PresenterLoginCallBack.getCarreraInfo(key).subscribe
                                (LocationUpdatesService.this::changeCarreras,
                                        LocationUpdatesService.this::getCarrerasError);
                    }
                }
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                if (Preferences.getSavedOnline()) {
                    if (key.equals(CareerPreference.show().getUid())){
                        getInstance().PresenterLoginCallBack.getCarreraInfo(key).subscribe
                                (LocationUpdatesService.this::changeCarreras,
                                        LocationUpdatesService.this::getCarrerasError);
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        };
        locationCallback();
        createLocationRequest();
        getLastLocation();
        requestLocationUpdates();

//        HandlerThread handlerThread = new HandlerThread(TAG);
//        handlerThread.start();
//        mServiceHandler = new Handler(handlerThread.getLooper());
    }



    public static synchronized LocationUpdatesService getInstance() {
        LocationUpdatesService myApplication;
        synchronized (LocationUpdatesService.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }


    private void cambioCarrera(RxFirebaseChildEvent<DataSnapshot> dataSnapshotRxFirebaseChildEvent) {

        dataSnapshotRxFirebaseChildEvent.getEventType();

        switch (dataSnapshotRxFirebaseChildEvent.getEventType()) {

            case ADDED:
//                Toast.makeText(getActivity(),"ADDED",Toast.LENGTH_LONG).show();
//                String uidD =dataSnapshotRxFirebaseChildEvent.getValue().child(career.getUid()).child(Constants.UidConductor).getValue(String.class);
//                if(uidD.equals(mViewModel.getUid())) {
//                    Toast.makeText(getActivity(),"ajajajaja" +uidD,Toast.LENGTH_LONG).show();
//
//                }
//                if (!dataSnapshotRxFirebaseChildEvent.getValue().getKey().equals(idCarrera) && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
//                    //setupChildAdded(dataSnapshotRxFirebaseChildEvent);
//                    LocationUpdatesService.isShowing = true;
//                }
                break;

            case CHANGED:
                Career career = ConvertDatasnapshopCareer.changeDatasnapToCareer(dataSnapshotRxFirebaseChildEvent);
                    // method
                getInstance().PresenterLoginCallBack.setCareerStatus(career.getStatus());
                //PresenterLoginCallBack.setCareerStatus(career.getStatus());


                getInstance().PresenterLoginCallBack.setCarrera(career);




                Toast.makeText( getInstance(),"CHANGED",Toast.LENGTH_LONG).show();
                break;

            case MOVED:
                Toast.makeText( getInstance(),"MOVED",Toast.LENGTH_LONG).show();
                break;

            case REMOVED:
                Toast.makeText( getInstance(),"REMOVED",Toast.LENGTH_LONG).show();
                break;
        }
    }


    private void locationCallback() {

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                if (!Settings.Secure.getString(
                        getContentResolver(), Constants.mock_location).equals("0")
                        ||
                        isMockLocation(locationResult.getLastLocation())) {
                }

                mLocation = locationResult.getLastLocation();


             switch (CareerPreference.getStatusServiceCareer()) {
                    case Accepted:
                        if (lastLocation != null && mLocation != null) {
                            Location prevLoc = new Location("service Provider");
                            prevLoc.setLatitude(

                                    CareerPreference.show().getJourney().getOrigin().getLat());
                            prevLoc.setLongitude(

                                    CareerPreference.show().getJourney().getOrigin().getLon());

                            double distanceMeters = (mLocation.distanceTo(prevLoc))
                                    / Constants.kmCercano; // as distance is in meter
                            if (distanceMeters <= 1) {
                                listenCareer();

                            }

                        }
                        break;

                    case Travel:
                        if (lastLocation != null && mLocation != null) {
                            LatLng latlng =
                                    new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
                                    points.add(latlng);
                                    speedRoute += mLocation.getSpeed();

                            if (mLocationr != null) {
                                rideDistance += mLocationr.distanceTo(mLocation) / 1000;

                                Log.e("rideDistancet",String.valueOf(rideDistance));
                                Log.e("rideDistance",String.valueOf(mLocationr.distanceTo(mLocation) / 1000));
                                Log.e("rideDistancer",String.valueOf(mLocationr));
                                Log.e("rideDistancel",String.valueOf(mLocation));


                            }
                            Location prevLoc = new Location("service Provider");
                            prevLoc.setLatitude(
                                    CareerPreference.show().getJourney().getDestiny().getLat());
                            prevLoc.setLongitude(
                                    CareerPreference.show().getJourney().getDestiny().getLon());

                            double distanceMeters = (mLocation.distanceTo(prevLoc))
                                    / Constants.kmCercano; // as distance is in meter
                            if (distanceMeters <= 1) {
                                listenCareer();
                            }

                        }
                        break;
                }


                Intent intent = new Intent(ACTION_BROADCAST);
                StationBus.getBus().post(new MessageBusInformation(LocationStatus.CHANGED, mLocationr, mLocation));
                mLocationr = locationResult.getLastLocation();

                intent.putExtra(EXTRA_LOCATION, locationResult.getLastLocation());
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                getInstance().PresenterLoginCallBack.onLocation(mLocation, Preferences.getSavedOnline());

                if (lastLocation != null && mLocation != null) {
                    double distanceInKiloMeters = (mLocation.distanceTo(lastLocation))
                            / Constants.km; // as distance is in meter

                    if (distanceInKiloMeters >= 1) {
                        lastLocation = mLocation;
                        getInstance().PresenterLoginCallBack.traerCarreras(mLocation, GeoListener);

                    }
                }
                // Update notification content if running as a foreground service.
                //  if (serviceIsRunningInForeground(LocationUpdatesService.this)
                // && Preferences.getSavedOnline(getBaseContext())) {
                //   mNotificationManager.notify(NOTIFICATION_ID, getNotification());
                // showNotification();
                //  }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                super.onLocationAvailability(locationAvailability);
                // locationAvailability.isLocationAvailable();
            }
        };

    }

    public static void  setStartCareer() {

        LatLng latlng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());
        points.add(latlng);
        setPickupuserLocation(lastLocation);

    }

    public static ArrayList<LatLng> getPoints() {
        return points;
    }

    public static void setPoints(ArrayList<LatLng> points) {
        if (points != null)
            LocationUpdatesService.points = points;
        else
            LocationUpdatesService.points = new ArrayList<>();
    }


    public static void startWaitTime() {
        setTimeWaitTime(System.currentTimeMillis());

    }

    public static double getRideDistance() {
        return rideDistance;
    }

    public static long getTimeCareerTotal() {
        long totaltime = System.currentTimeMillis() - CareerPreference.getInicioCareer();
        return totaltime;
    }


    public static long getTimeWaitTime() {
        long resultime = System.currentTimeMillis() -  CareerPreference.getTimeWait();
        return resultime;
    }

    public static void setTimeWaitTime(long timeWaitTime) {
        CareerPreference.setTimeWait(timeWaitTime);
    }


    public Location getPickupuserLocation() {
        return pickupuserLocation;
    }

    public static void setPickupuserLocation(Location pickupuserLocation) {
        LocationUpdatesService.pickupuserLocation = pickupuserLocation;
    }


    private void getCarrerasError(Throwable t) {
        Dlog.d("", "LocationUpdateErr: " + t.toString());
    }

    public Observable<RxFirebaseChildEvent<DataSnapshot>> traerCarreras() {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child(Constants.trips)
                .orderByChild(Constants.status)
                .equalTo(1);

        return RxFirebaseDatabase.observeChildEvent(query);
    }

    private void getCarreras(DataSnapshot dataSnapshot) {

        carrerdAdded(dataSnapshot);

        Intent intent = new Intent(this, HomeActivity.class);

        if (AppBase.isActivityVisible()) {

            Dlog.i(TAG, "This is last activity in the stack");

        } else {
            Dlog.i(TAG, "No esta en stack");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }


    }

    private void changeCarreras(DataSnapshot dataSnapshot) {

        //carrerdAdded(dataSnapshot);
        if (dataSnapshot.exists()) {
            Career career = ConvertDatasnapshopCareer.convertDatasnapToCareer(dataSnapshot);
            if (career != null && Utils.checkHighAccuracyLocationMode()) {
                getInstance().PresenterLoginCallBack.setCarrera(career);
                StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Cambios,career.getUid(), career));
            }
        }else{
            //CareerPreference.setAcceptCareer(false);
            //CareerPreference.setStatusCareer(Career.serviceType.Canceled);
            StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Cambios,dataSnapshot.getKey(), null));

        }


    }


    private void listenCareer() {

        Intent intent = new Intent(this, HomeActivity.class);

        if (AppBase.isActivityVisible()) {

            Dlog.i(TAG, "This is last activity in the stack");

        } else {
            Dlog.i(TAG, "No esta en stack");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            this.startActivity(intent);
        }


    }


    private void carrerdAdded(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Career career = ConvertDatasnapshopCareer.convertDatasnapToCareer(dataSnapshot);
            if (career != null && Utils.checkHighAccuracyLocationMode()) {
                getInstance().PresenterLoginCallBack.setCarrera(career);
                StationBus.getBus().post(new MessageBusInformation(career.getStatus(),career.getUid(), career));
            }
        }else{

            StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Canceled,CareerPreference.show().getUid(), null));
        }

    }


    private void carrerChanged(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            Career career = ConvertDatasnapshopCareer.convertDatasnapToCareer(dataSnapshot);
            if (career != null && Utils.checkHighAccuracyLocationMode()) {
                getInstance().PresenterLoginCallBack.setCarrera(career);
                StationBus.getBus().post(new MessageBusInformation(career.getStatus(),career.getUid(), career));
            }
        }else{

            StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Canceled,CareerPreference.show().getUid(), null));
        }
    }


    @Override
    public void onDestroy() {

        getInstance().PresenterLoginCallBack.onLocation(null, false);

        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        super.onDestroy();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Dlog.i(TAG, "Service started");

        boolean startedFromNotification = intent.getBooleanExtra(Constants.carrers,
                false);

        // on tap of remove update from notification
        if (startedFromNotification) {
            removeLocationUpdates();
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mChangingConfiguration = true;
    }

    @NonNull
    @Override
    public IBinder onBind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Dlog.i(TAG, "in onBind()");
        stopForeground(true);
        mChangingConfiguration = false;
        return mBinder;
    }


    @Override
    public void onRebind(Intent intent) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Dlog.i(TAG, "in onRebind()");
        stopForeground(true);
        mChangingConfiguration = false;
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Dlog.i(TAG, "Last client unbound from service");

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Utils.requestingLocationUpdates(this)) {
            Dlog.i(TAG, "Starting foreground service");
            /*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             */


            //startForegroundService();
            if (Preferences.getSavedOnline() && Utils.checkHighAccuracyLocationMode())
                startForeground(NOTIFICATION_ID, getNotification());
            else{
                removeLocationUpdates();
                stopSelf();
            }
        }
        return true; // Ensures onRebind() is called when a client re-binds.
    }


    Notification getNotification() {

        Intent intent = new Intent(this, LocationUpdatesService.class);

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        Intent notificationIntent = new Intent(this, LocationUpdatesService.class);


        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService
                (this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity
                (this, 0,
                new Intent(this, HomeActivity.class), 0);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            NotificationCompat.Action action1 = new NotificationCompat.Action.Builder
                    (R.drawable.ic_google, getString(R.string.launch_activity)
                            , activityPendingIntent).build();
            NotificationCompat.Action action2 = new NotificationCompat.Action.Builder
                    (R.drawable.ic_google, getString(R.string.remove_location_updates)
                            , activityPendingIntent).build();


            return new NotificationCompat.Builder(this,
                    PRIMARY_CHANNEL)
                    //.addAction(action1).addAction(action2)
                    .setContentText(getResources().getString(R.string.isOnline))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(text).setContentIntent(activityPendingIntent)
                    .setWhen(System.currentTimeMillis()).build();
            //   getNotificationManager().notify(0, notificationBuilder.build());

        } else {

            return new NotificationCompat.Builder(this)
                    .addAction(R.drawable.ic_google, getString(R.string.launch_activity),
                            activityPendingIntent)
                    .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                            servicePendingIntent)
                    .setContentText(getResources().getString(R.string.isOnline))
                    .setContentTitle(getResources().getString(R.string.app_name))
                    .setOngoing(true)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setTicker(text)
                    .setWhen(System.currentTimeMillis()).build();

        }

    }


    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLocation = task.getResult();

                            lastLocation = mLocation;
                            getInstance().PresenterLoginCallBack.traerCarreras(mLocation, GeoListener);
                            StationBus.getBus().post(new MessageBusInformation(LocationStatus.LASTLOCATION));

                        } else {
                            Dlog.e("Error", "Failed to get location.");
                        }
                    });
        } catch (SecurityException e) {
            Dlog.e("Exception", "Last location permission." + e);
        }
    }

    public void removeLocationUpdates() {
        Dlog.i(TAG, "Removing location updates");

        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Dlog.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    public void requestLocationUpdates() {
        Dlog.i(TAG, "Requesting location updates");
        startService(new Intent(getApplicationContext(), LocationUpdatesService.class));
        Utils.setRequestingLocationUpdates(this, true);
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Dlog.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }


    public static boolean isMockLocation(Location location) {
        return (Build.VERSION.SDK_INT < 18 || location == null || location.isFromMockProvider());
    }

    public void listenerCareer(String uid) {
        if (subsp == null) {

                // method
             subsp = getInstance().PresenterLoginCallBack.listCareer(uid).subscribe(LocationUpdatesService.this::cambioCarrera, this::errorAcceptCareer);
              //  Query query = FirebaseDatabase.getInstance().getReference()
               //         .child(Constants.tripsAccept).child(uid);

              //  subsp =  RxFirebaseDatabase.observeChildEvent(query).subscribe(LocationUpdatesService.this::cambioCarrera, this::errorAcceptCareer);
           // }


        }

    }


    public void unSubscribe() {
        if(subsp != null)
            subsp.unsubscribe();
    }


    private void errorAcceptCareer(Throwable throwable) {

        Toast.makeText(this,throwable.getMessage(),Toast.LENGTH_LONG).show();
        //noCarrera();

    }



    public class LocationBinder extends Binder {
        public LocationUpdatesService getLocationUpdateService() {
            return LocationUpdatesService.this;
        }
    }

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
