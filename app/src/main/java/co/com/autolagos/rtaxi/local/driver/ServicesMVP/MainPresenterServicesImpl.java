package co.com.autolagos.rtaxi.local.driver.ServicesMVP;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;

import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import rx.Observable;

public class MainPresenterServicesImpl implements MainContractServices.PresenterCallBack {

    private MainContractServices.ViewCallBack mainLoginView;
    private FirebaseDataStore mFirebaseDataStore;


    public MainPresenterServicesImpl(MainContractServices.ViewCallBack mainView,
                                     FirebaseDataStore firebaseDataStore) {
        this.mainLoginView = mainView;
        mFirebaseDataStore = firebaseDataStore;

    }


    @Override
    public void onLocation(Location location, Boolean isOnline) {

        if (isOnline && Preferences.getSavedLogin()) {
            mFirebaseDataStore.availableMyLocation(location);
        } else {
            mFirebaseDataStore.removeMyLocation();

        }
    }

    @Override
    public void onAuthListener() {

    }

    @Override
    public void traerCarreras(Location location, GeoQueryEventListener listener) {

        mFirebaseDataStore.traerCarrerasCercanas(location, listener);
    }

    @Override
    public void traerCarreraId(String id, GeoQueryEventListener listener) {
    //      mFirebaseDataStore.traerCarreraInfo(location, listener);

    }

    @Override
    public void removeCarreras() {
        // mFirebaseDataStore.removeCercanas();
        mFirebaseDataStore.removeCarrerasCercanas();
    }

    @Override
    public void setCarrera(Career career) {
        CareerPreference.store(career);
    }


    @Override
    public Observable<DataSnapshot> getCarreraInfo(String key) {
        return mFirebaseDataStore.traerCarreraInfo(key);
    }

    @Override
    public Observable<RxFirebaseChildEvent<DataSnapshot>> getCarreras() {
        return null;
    }

    @Override
    public Observable<RxFirebaseChildEvent<DataSnapshot>> listCareer(String uid) {
        return mFirebaseDataStore.listCareer(uid);
    }

    @Override
    public void setCareerStatus(Career.serviceType status) {
        CareerPreference.setStatusCareer(status);

    }


}