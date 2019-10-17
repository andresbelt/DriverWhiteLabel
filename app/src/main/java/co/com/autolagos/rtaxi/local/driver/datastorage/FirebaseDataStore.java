package co.com.autolagos.rtaxi.local.driver.datastorage;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.firebase.BuildConfig;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.kelvinapps.rxfirebase.RxFirebaseAuth;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.kelvinapps.rxfirebase.RxFirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import co.com.autolagos.rtaxi.local.driver.interfaces.CompleteTransaction;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoFire;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoLocation;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQuery;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import rx.Observable;
import rx.schedulers.Schedulers;

public class FirebaseDataStore {

    private DatabaseReference reference;
    private FirebaseUser mUser;
    private FirebaseAuth mAtuh;
    private DatabaseReference fDatabase;
    private GeoFire geoFireCustomer;
    private GeoFire geoFire;
    private int radius = 1;
    private GeoQuery geoQuery;
    private  int cacheExpiration;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private String param = "";

    public FirebaseDataStore() {
        reference = FirebaseDatabase.getInstance().getReference();
        mAtuh = FirebaseAuth.getInstance();
        fDatabase = FirebaseDatabase.getInstance().getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);

        mFirebaseRemoteConfig.fetch(0)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // After config data is successfully fetched, it must be activated before newly fetched
                        // values are returned.
                        mFirebaseRemoteConfig.activateFetched();
                    } else {

                    }

                });

        geoFireCustomer = new GeoFire(fDatabase.child(Constants.trips));
        if (mUser == null) {
            Preferences.saveLogin(false);
            mAtuh.signOut();
            return;
        }else{
            DriverPreferences.setUid(mUser.getUid());
        }

    }


    public void availableMyLocation(Location locationFlowable) {

        DatabaseReference ref = reference.child(Constants.driversAvailable);
        geoFire = new GeoFire(ref);
        geoFire.setLocation(mUser.getUid(),locationFlowable, new GeoLocation(locationFlowable.getLatitude(), locationFlowable.getLongitude()));
        DatabaseReference refd = reference.child(Constants.users)
                .child(Constants.drivers)
                .child(mUser.getUid());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.lastLocation,locationFlowable);
        refd.updateChildren(updates);
    }


    public String getUserUid() {

     return mUser.getUid();
    }


    public void removeMyLocation() {

        DatabaseReference ref = reference.child(Constants.driversAvailable);
        GeoFire geoFireAvailable = new GeoFire(ref);
        geoFireAvailable.removeLocation(mUser.getUid());
    }


    public Observable<AuthResult> loginToken(String token) {

       return RxFirebaseAuth.signInWithCustomToken(mAtuh, token);
    }


    public Observable<RxFirebaseChildEvent<DataSnapshot>> traerCarreras() {
        Query query = fDatabase
                .child(Constants.trips)
                .orderByChild(Constants.status)
                .equalTo(1);

        return  RxFirebaseDatabase.observeChildEvent(query).subscribeOn(Schedulers.immediate());
    }


    public String fetchParam(String Constan){

       return mFirebaseRemoteConfig.getString(Constan);

    }


    public long getCacheExpiration() {
// If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }


    public Observable<DataSnapshot> traerCarreraInfo(String key) {
        Query query = fDatabase
                .child(Constants.trips)
                .child(key);

        return  RxFirebaseDatabase.observeSingleValueEvent(query);
    }




    public void getClosestDriver(Location loc,GeoQueryEventListener listener){
        geoQuery = geoFireCustomer.queryAtLocation(new GeoLocation(loc.getLatitude(), loc.getLongitude()), radius);
        geoQuery.removeAllListeners();
        geoQuery.addGeoQueryEventListener(listener);

    }


    public Observable<DataSnapshot> getUserinfo(String key){
        Query query = fDatabase
                .child(Constants.Users)
                .child(Constants.Customers).child(key);

        return  RxFirebaseDatabase.observeSingleValueEvent(query);
    }



    public void getDriverIn(String key){
        geoFireCustomer.removeLocation(key);

    }


    public void getClosestDriverR(String key){
        geoFireCustomer.removeLocation(key);

    }


    public Observable<RxFirebaseChildEvent<DataSnapshot>> traerCarreraNueva(String uidCustomer) {
        Query query = fDatabase
                .orderByChild(uidCustomer)
                .limitToFirst(1)
                .equalTo(null);

        return  RxFirebaseDatabase.observeChildEvent(query);
    }




    public Observable<FirebaseUser> loginAuth() {

        return RxFirebaseAuth.observeAuthState(mAtuh);
    }


    public void loginAuthM() {
         mAtuh.signOut();
    }




    public void traerCarrerasCercanas(Location location,GeoQueryEventListener listener) {
        if(geoQuery != null)
        geoQuery.removeAllListeners();

        geoQuery= geoFireCustomer.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 1);
        geoQuery.addGeoQueryEventListener(listener);
    }

    public void removeCarrerasCercanas() {

        if(geoQuery != null)
            geoQuery.removeAllListeners();
    }

    public void acceptCareer(CompleteTransaction transactionListener) {

        long hora = System.currentTimeMillis();

        DatabaseReference historyRef = reference.child(Constants.tripsAccept);

        Career car = CareerPreference.show();

        DatabaseReference historyTrans = reference.child(Constants.tripsAccept).child(car.getUid()).child(Constants.UidConductor);

        historyTrans.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                if(mutableData.getValue() == null) {
                    mutableData.setValue(mUser.getUid());
                }

                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
                transactionListener.onComplete(databaseError,b,dataSnapshot);
            }
        });

        DatabaseReference refd = reference.child(Constants.trips)
                .child(car.getUid());

        Map<String, Object> updates = new HashMap<>();

        updates.put(Constants.georeference, car.getG());
        updates.put(Constants.uidTipoServicio,car.getUidTipoServicio());
        updates.put(Constants.Journey,car.getJourney());
        updates.put(Constants.FechaAceptado,hora);
        updates.put(Constants.fecha,car.getFecha());
        updates.put(Constants.status,2);
        updates.put(Constants.UidUser,car.getUidUser());
        refd.removeValue();

        historyRef.child(car.getUid()).updateChildren(updates);

    }

    public void setStartCareer() {

        CareerPreference.setInicioCareer();
        CareerPreference.setStatusCareer(Career.serviceType.Travel.ordinal());
        CareerPreference.setLocationStartCareer(LocationUpdatesService.mLocation);

        DatabaseReference historyRef = reference.child(Constants.tripsAccept);

        Career car = CareerPreference.show();

        Map<String, Object> updates = new HashMap<>();

        updates.put(Constants.inicio, CareerPreference.getInicioCareer());
        updates.put(Constants.tiempoespera, LocationUpdatesService.getTimeWaitTime());
        updates.put(Constants.status, Career.serviceType.Travel.ordinal());

        historyRef.child(car.getUid()).updateChildren(updates);

        historyRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    LocationUpdatesService.setStartCareer();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void setFinishCareer() {

        CareerPreference.setStatusCareer(Career.serviceType.Finish.ordinal());
        CareerPreference.setLocationStartCareer(null);

        DatabaseReference historyRef = reference.child(Constants.tripsAccept);

        DatabaseReference history = reference.child(Constants.history);

        Career car = CareerPreference.show();

        Map<String, Object> updates = new HashMap<>();


        updates.put(Constants.distancia, LocationUpdatesService.rideDistance);
        updates.put(Constants.tiemporuta, CareerPreference.getTimeRoute() );
        updates.put(Constants.rutapoints, LocationUpdatesService.getPoints());
        LocationUpdatesService.setPoints(null);
        LocationUpdatesService.setPickupuserLocation(null);
        LocationUpdatesService.rideDistance = 0;
        updates.put(Constants.status, Career.serviceType.Finish.ordinal());

        historyRef.child(car.getUid()).updateChildren(updates);


        Map<String, Object> update = new HashMap<>();

        update.put(Constants.UidConductor, mUser.getUid());
        update.put(Constants.UidUser, CareerPreference.show().getUidUser());
        history.child(car.getUid()).updateChildren(update);

    }



    public Observable<DataSnapshot> ifacceptCareer(String key) {

        Query query = fDatabase
                .child(Constants.trips).child(key);

        return  RxFirebaseDatabase.observeSingleValueEvent(query);
    }


    public Observable<RxFirebaseChildEvent<DataSnapshot>> listCareer(String key) {

        Query query = fDatabase
                .child(Constants.tripsAccept).child(key);

        return  RxFirebaseDatabase.observeChildEvent(query);
    }



    public void setPickupUser(ValueEventListener pickupUser) {

        CareerPreference.setStatusCareer(Career.serviceType.Pickup.ordinal());

        DatabaseReference historyRef = reference.child(Constants.tripsAccept);

        Career car = CareerPreference.show();

        Map<String, Object> updates = new HashMap<>();

        updates.put(Constants.status, Career.serviceType.Pickup.ordinal());

        historyRef.child(car.getUid()).updateChildren(updates);
        historyRef.addValueEventListener(pickupUser);


    }
}