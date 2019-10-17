package co.com.autolagos.rtaxi.local.driver.di.component;



import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.MainContractServices;
import co.com.autolagos.rtaxi.local.driver.MVP.MainContractMap;
import co.com.autolagos.rtaxi.local.driver.fragment.MapFragment;
import co.com.autolagos.rtaxi.local.driver.di.module.MvpModule;
import co.com.autolagos.rtaxi.local.driver.di.scope.ActivityScope;
import dagger.Component;

@ActivityScope
@Component(dependencies = AppComponent.class, modules = MvpModule.class)

public interface ActivityComponent {

    void inject(MapFragment MapFragment);

    void inject(HomeActivity homeActivity);

}



