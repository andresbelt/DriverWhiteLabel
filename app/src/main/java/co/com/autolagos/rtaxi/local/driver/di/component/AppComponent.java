package co.com.autolagos.rtaxi.local.driver.di.component;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.location.LocationServices;

import javax.inject.Singleton;


import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainContractServices;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.di.module.AppModule;
import co.com.autolagos.rtaxi.local.driver.di.module.ContextModule;
import co.com.autolagos.rtaxi.local.driver.di.module.DataModule;
import dagger.Component;
import dagger.Provides;

@Singleton
@Component(modules = {AppModule.class, DataModule.class, ContextModule.class})
public interface AppComponent {


    void inject(AppBase initApplication);


    void inject(HomeActivity initApplication);

    void inject(  LocationUpdatesService initApplication);

    Context getContext();

    Application getApplication();



}
