package co.com.autolagos.rtaxi.local.driver.di.module;

import co.com.autolagos.rtaxi.local.driver.HomeViewModel;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainContractServices;

import co.com.autolagos.rtaxi.local.driver.MVP.MainContractMap;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainPresenterServicesImpl;
import co.com.autolagos.rtaxi.local.driver.MVP.MainPresenterMapImpl;
import co.com.autolagos.rtaxi.local.driver.ShareLocationViewModel;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;
import dagger.Module;
import dagger.Provides;

@Module
public class MvpModule {


    private MainContractServices.ViewCallBack viewLoginCallBack;

    private MainContractMap.ViewCallBack viewMapCallBack;


    public MvpModule(MainContractServices.ViewCallBack viewCallBack) {
        this.viewLoginCallBack = viewCallBack;
    }


    public MvpModule(MainContractMap.ViewCallBack viewCallBack) {
        this.viewMapCallBack = viewCallBack;
    }



    @Provides
    public MainContractMap.PresenterCallBack provideMapPresenter(MainContractMap.ViewCallBack view) {
        return new MainPresenterMapImpl(view, new FirebaseDataStore());
    }



    @Provides
    ShareLocationViewModel provideShareLocationViewModel() {
        return new ShareLocationViewModel(AppBase.getInstance(), new FirebaseDataStore());
    }


    @Provides
    HomeViewModel provideHomeViewModel() {
        return new HomeViewModel(AppBase.getInstance(), new FirebaseDataStore());
    }
}
