package co.com.autolagos.rtaxi.local.driver;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.LocationRequest;

import javax.inject.Inject;

import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import ru.solodovnikov.rx2locationmanager.LocationTime;
import ru.solodovnikov.rx2locationmanager.RxLocationManager;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Boolean> mSharingState;
    private Location mLastLocation;
    private Flowable<Location> mLocationUpdatesObserver;
    private LocationRequest locationRequest;
    private final MutableLiveData<Address> AddressLiveData = new MutableLiveData<>();
    private RxLocationManager rxLocationManager;

    private FirebaseDataStore mFirebaseDataStore;


    @Inject
    public HomeViewModel(Application application, FirebaseDataStore firebaseDataStore) {
        mFirebaseDataStore = firebaseDataStore;
        mSharingState = new MutableLiveData<>();
        rxLocationManager = new RxLocationManager(application.getApplicationContext());


    }

    public void stopSharingLocation() {
        if (isSharing()) {
            mSharingState.setValue(false);
          //  mFirebaseDataStore.stopShareMyLocation().subscribe();
        }
    }


    public Maybe<Location> lastLocationManager() {

        return rxLocationManager.getLastLocation(LocationManager.NETWORK_PROVIDER, LocationTime.oneDay());


    }

    public void shareLastLocation(Location location,boolean isOnline) {

        if (isOnline)

            mFirebaseDataStore.availableMyLocation(location);
        else
            mFirebaseDataStore.removeMyLocation();

    }




    public void startSharingLocation() {
       // mSharingState.setValue(true);
        mFirebaseDataStore.availableMyLocation(mLastLocation);
    }



    public MutableLiveData<Boolean> getCarreras() {


        return mSharingState;
    }


    public MutableLiveData<Boolean> getSharingStateLiveData() {
        return mSharingState;
    }

    public boolean isSharing() {
        return mSharingState.getValue() != null && mSharingState.getValue();
    }



    public void logout() {
        mFirebaseDataStore.loginAuthM();
    }


    public void setLastCachedLocation(Location loc) {
        mLastLocation = loc;
    }


}