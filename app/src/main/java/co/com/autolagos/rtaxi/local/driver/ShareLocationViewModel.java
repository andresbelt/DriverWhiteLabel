package co.com.autolagos.rtaxi.local.driver;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.widget.TextView;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.PolyUtil;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.geofire.RxGeocoding;
import co.com.autolagos.rtaxi.local.driver.interfaces.CompleteTransaction;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.Coordinate;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.FirebasePresenter;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.DriverPreferences;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.utils.Utils;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.solodovnikov.rx2locationmanager.LocationTime;
import ru.solodovnikov.rx2locationmanager.RxLocationManager;
import rx.Observable;

public class ShareLocationViewModel extends ViewModel {

    private MutableLiveData<Boolean> mSharingState;
    private Location mLastLocation;
    private Flowable<Location> mLocationUpdatesObserver;
    private LocationRequest locationRequest;
    private final MutableLiveData<Address> AddressLiveData = new MutableLiveData<>();
    private RxLocationManager rxLocationManager;
    private FirebasePresenter.MapPreferencePresenter mapPreferencePresenter;
    private FirebaseDataStore mFirebaseDataStore;
    private DriverPreferences driverPreferences;


    @Inject
    public ShareLocationViewModel(Application application, FirebaseDataStore firebaseDataStore) {
        mFirebaseDataStore = firebaseDataStore;
        mSharingState = new MutableLiveData<>();
        rxLocationManager = new RxLocationManager(application.getApplicationContext());
        driverPreferences = new DriverPreferences(application.getApplicationContext());
    }

    public void stopSharingLocation() {
        if (isSharing()) {
            mSharingState.setValue(false);
            //  mFirebaseDataStore.stopShareMyLocation().subscribe();
        }
    }


    public Maybe<Location> lastLocationManager() {

        return rxLocationManager.getLastLocation(LocationManager.NETWORK_PROVIDER,
                LocationTime.oneDay());


    }


    public String getUid() {

        return mFirebaseDataStore.getUserUid();


    }


    public void shareLastLocation(Location location, boolean isOnline) {

        if (isOnline)

            mFirebaseDataStore.availableMyLocation(location);
        else
            mFirebaseDataStore.removeMyLocation();

    }

    public Observable<RxFirebaseChildEvent<DataSnapshot>> getCarreras() {
        // mSharingState.setValue(true);
        return mFirebaseDataStore.traerCarreras();
    }


    public void getClosestDriver(Location loc, GeoQueryEventListener listener) {
        // mSharingState.setValue(true);
        mFirebaseDataStore.getClosestDriver(loc, listener);
    }


    public void getAddress(Location loc, GeoQueryEventListener listener) {
        // mSharingState.setValue(true);
        mFirebaseDataStore.getClosestDriver(loc, listener);
    }


    public void remove(String loc) {
        // mSharingState.setValue(true);
        mFirebaseDataStore.getClosestDriverR(loc);
    }



    public Observable<FirebaseUser> onAuthListener() {

        return mFirebaseDataStore.loginAuth();

//        mFirebaseDataStore.loginAuth().subscribe(
//                firebaseUser -> {
//                    if (firebaseUser != null)
//                        Dlog.d("RxFirebaseSample", "signin");
//                    else {
//                        Dlog.d("RxFirebaseSample", "out");
//                      //  mainMapView.onLoginFailure("");
//
//                    }
//                },
//                throwable -> {
//                    Dlog.d("RxFirebaseSample", "out");
//                });
    }


    public Career getCareerStore() {
        return CareerPreference.show();

    }


    public Observable<DataSnapshot> getUserInfo(String key) {
        return mFirebaseDataStore.getUserinfo(key);

    }


    public Single<List<Address>> getAddresswithLocation(Coordinate location) {
        return RxGeocoding
                .geocodingReverseBuilder(AppBase.getInstance().getBaseContext())
                .maxResults(1)
                .location(location.getLat(), location.getLon())
                .build()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .toSingle();

    }


    public void acceptCareer(CompleteTransaction transactionListener) {
        // mSharingState.setValue(true);
        mFirebaseDataStore.acceptCareer(transactionListener);
    }

    public Observable<DataSnapshot> ifAcceptCareer(String key) {
        // mSharingState.setValue(true);
        return mFirebaseDataStore.ifacceptCareer(key);

    }


    public void startSharingLocation() {
        // mSharingState.setValue(true);
        mFirebaseDataStore.availableMyLocation(mLastLocation);
    }

    public void isOnline(Boolean isOnline) {
        Preferences.saveOnline(isOnline);
        if (!isOnline)
            mFirebaseDataStore.removeMyLocation();
    }


    public Boolean getOnline() {
       return Preferences.getSavedOnline();
    }


    public MutableLiveData<Boolean> getSharingStateLiveData() {
        return mSharingState;
    }

    public boolean isSharing() {
        return mSharingState.getValue() != null && mSharingState.getValue();
    }


    public Location getLastCachedLocation() {
        return mLastLocation;
    }


    public void setLastCachedLocation(Location loc) {
        mLastLocation = loc;
    }


    public void showDestiny() {
    }

    public void setStartCareer() {

        mFirebaseDataStore.setStartCareer();

    }

    public Observable<RxFirebaseChildEvent<DataSnapshot>> listenerCareer(String key) {

        return mFirebaseDataStore.listCareer(key);
    }

    public void setPickupUser(ValueEventListener pickupUser) {

        mFirebaseDataStore.setPickupUser(pickupUser);

    }

    public void setSFinishCareer() {

        mFirebaseDataStore.setFinishCareer();

    }

    public double setCalculateCareer() {


        long timeroute = LocationUpdatesService.getTimeCareerTotal();
        CareerPreference.setTimeRoute(timeroute);

        double cargoair = 0;
        double cargonoche = 0;
        double recargos = 0;

        double minima = Double.parseDouble(mFirebaseDataStore.fetchParam(Constants.MINIMA_));
        double bandera = Double.parseDouble(mFirebaseDataStore.fetchParam(Constants.BANDERA_));
        double costokm = Double.parseDouble(mFirebaseDataStore.fetchParam(Constants.COSTO_KM_));
        double distanciakm = LocationUpdatesService.getRideDistance();


        long diffSeconds = timeroute / 100 %60;
        long diffMin = timeroute / (60*1000) % 60;
        long DiffHours = timeroute/ (60*60*1000) % 24;


        double fCong = 0;
        double vs = 0;

        LocationUpdatesService.rideDistance = 0;
        LatLng locato= new LatLng(LocationUpdatesService.mLocation.getLatitude(),
                LocationUpdatesService.mLocation.getLongitude());

        // Recargos
        boolean airport = PolyUtil.containsLocation(locato, Constants.getPointsAeropuerto()
                , true);
        boolean noche = Utils.checkSiNoche(mFirebaseDataStore.fetchParam(Constants.HORA_INI_RECARGO)
                , mFirebaseDataStore.fetchParam(Constants.MIN_INI_RECARGO)
                , mFirebaseDataStore.fetchParam(Constants.HORA_FIN_RECARGO)
                , mFirebaseDataStore.fetchParam(Constants.MIN_FIN_RECARGO));


        if (airport)
            cargoair = Double.parseDouble(mFirebaseDataStore.fetchParam(Constants.AEROPUERTO_));

        if (noche)
            cargonoche = Double.parseDouble(mFirebaseDataStore.fetchParam(Constants.NOCTURNO_));

        recargos = cargoair + cargonoche + 700;

        if(DiffHours == 0 && diffMin == 0){
           vs = 0;

        }
        else{
            if (DiffHours != 0){
                vs = distanciakm / DiffHours;
            }else{
                long diffminkm = diffMin/100;
                vs = distanciakm / diffminkm;

            }
        }

        if (vs >= 36)
            fCong = 0;

        if (18 <= vs)
            if (vs < 36) {
                fCong = 0.415;
            } else {
                if (vs < 18) {
                    fCong = 43.815;
                } else {
                    fCong = 0;
                }
            }

        double tarifa = bandera + ((costokm * 1.113) * (1 + fCong) * distanciakm) + recargos;

        CareerPreference.setDistance(distanciakm);


        if (tarifa < minima)
            return minima;
        else
            return tarifa;

    }


    public void changeStatusCareer(int status, Career career, Driver driver) {
        DatabaseReference databaseReference =
                FirebaseDatabase.getInstance().getReference(Constants.carrers);
        databaseReference.child(career.getUid());
        Map<String, Object> updates = new HashMap<>();
        updates.put(Constants.status, status);
        updates.put(Constants.locationDriver, createHashMaplocation(driver.getCoordinate()));
        databaseReference.updateChildren(updates);
        mapPreferencePresenter.resultStatusCareer(true);
    }

    private Map<String, Object> createHashMaplocation(Coordinate driverCoordinate) {
        Map<String, Object> mapOriginConfirm = new HashMap<>();
        mapOriginConfirm.put(Constants.Lat, driverCoordinate.getLat());
        mapOriginConfirm.put(Constants.Lon, driverCoordinate.getLon());

        Map<String, Object> map = new HashMap<>();
        map.put("originConfirm", mapOriginConfirm);

        return map;
    }

    public Driver getDriverData() {
 // DriverPreferences driverPreferences = new DriverPreferences(context, this);
     return driverPreferences.show();
    }


    public void setCareer(Career career) {

        CareerPreference.store(career);
    }

    public void setTarifa(double tarifa) {

        CareerPreference.setTarifa(tarifa);
    }


}