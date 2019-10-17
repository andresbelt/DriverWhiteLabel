package co.com.autolagos.rtaxi.local.driver.ServicesMVP;

import android.location.Location;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;

import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import rx.Observable;


public interface MainContractServices {

    interface ViewCallBack {


    }


    interface PresenterCallBack {

        void onLocation(Location location, Boolean isOnline);
        void onAuthListener();
        void traerCarreras(Location location,GeoQueryEventListener listener);
        void traerCarreraId(String id,GeoQueryEventListener listener);
        void removeCarreras();
        void setCarrera(Career career);
        Observable<DataSnapshot> getCarreraInfo(String key);
        Observable<RxFirebaseChildEvent<DataSnapshot>> getCarreras();
        Observable<RxFirebaseChildEvent<DataSnapshot>> listCareer(String uid);
        void setCareerStatus(Career.serviceType status);
    }
}
