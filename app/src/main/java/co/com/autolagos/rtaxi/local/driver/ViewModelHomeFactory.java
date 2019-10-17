package co.com.autolagos.rtaxi.local.driver;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class ViewModelHomeFactory implements ViewModelProvider.Factory {

    private HomeViewModel mShareLocationViewModel;

    @Inject
    public ViewModelHomeFactory(HomeViewModel HomeViewModel) {
        mShareLocationViewModel = HomeViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(HomeViewModel.class)) {
            return (T) mShareLocationViewModel;
        }

        throw new IllegalArgumentException("Unknown view model type");
    }
}