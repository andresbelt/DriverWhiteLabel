package co.com.autolagos.rtaxi.local.driver.MVP;

import co.com.autolagos.rtaxi.local.driver.datastorage.FirebaseDataStore;

public class MainPresenterMapImpl implements MainContractMap.PresenterCallBack
        , MainContractMap.ModelCallBack.onLoginListener {

    private MainContractMap.ViewCallBack mainMapView;
    private FirebaseDataStore mFirebaseDataStore;


    public MainPresenterMapImpl(MainContractMap.ViewCallBack mainView,
                                FirebaseDataStore firebaseDataStore) {
        this.mainMapView = mainView;
        mFirebaseDataStore = firebaseDataStore;

    }


    @Override
    public void onButtonClickLogout() {
        if (mainMapView != null) {
            mainMapView.showProgress();
        }
        mFirebaseDataStore.loginAuthM();

    }


    @Override
    public void onButtonClickLoginGoogle() {

    }



    @Override
    public void onSuccess(String message) {

    }

    @Override
    public void onFailure(String message) {
        if (mainMapView != null) {
            mainMapView.hideProgress();
            mainMapView.onLoginFailure(message);

        }
    }

}