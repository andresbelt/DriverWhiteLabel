package co.com.autolagos.rtaxi.local.driver.di.module;

import android.app.Application;
import android.app.Service;
import android.location.Location;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;

import javax.inject.Singleton;

import co.com.autolagos.rtaxi.local.driver.HomeViewModel;
import co.com.autolagos.rtaxi.local.driver.MVP.MainContractMap;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainContractServices;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainPresenterServicesImpl;
import co.com.autolagos.rtaxi.local.driver.ShareLocationViewModel;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import co.com.autolagos.rtaxi.local.driver.geofire.GeoQueryEventListener;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import dagger.Module;
import dagger.Provides;
import rx.Observable;

@Module
public class AppModule {

    private AppBase initApplication;

    private LocationUpdatesService initSer;
    private MainContractServices.ViewCallBack viewLoginCallBack;


    public AppModule(AppBase initApplication) {
        this.initApplication = initApplication;
    }

    @Provides
    public Application provideApplication() {
        return initApplication;
    }



    @Provides
    ShareLocationViewModel provideShareLocationViewModel() {
        return new ShareLocationViewModel(initApplication,new FirebaseDataStore());
    }


    @Provides
    HomeViewModel provideHomeViewModel() {
        return new HomeViewModel(initApplication, new FirebaseDataStore());

    }


    public AppModule(MainContractServices.ViewCallBack viewLoginCallBack) {
            this.viewLoginCallBack = viewLoginCallBack; }


        @Provides
        public MainContractServices.PresenterCallBack provideServicesPresenter(MainContractServices.ViewCallBack view) {
            return new MainPresenterServicesImpl(initSer, new FirebaseDataStore());
        }
        @Provides
        public MainContractServices.ViewCallBack provideLoginView() {
            return viewLoginCallBack;
        }





}
