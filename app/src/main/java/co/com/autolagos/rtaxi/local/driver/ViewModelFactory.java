package co.com.autolagos.rtaxi.local.driver;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import javax.inject.Inject;

public class ViewModelFactory implements ViewModelProvider.Factory {

    private ShareLocationViewModel mShareLocationViewModel;

    @Inject
    public ViewModelFactory(ShareLocationViewModel shareLocationViewModel) {
        mShareLocationViewModel = shareLocationViewModel;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ShareLocationViewModel.class)) {
            return (T) mShareLocationViewModel;
        }

        throw new IllegalArgumentException("Unknown view model type");
    }
}