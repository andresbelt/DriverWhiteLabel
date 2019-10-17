package co.com.autolagos.rtaxi.local.driver.MVP;

import android.location.Location;


public interface MainContractMap {

    interface ViewCallBack {
        void showProgress();
        void hideProgress();
        void onLoginSuccess();
        void onLoginFailure(String message);

    }

    interface ModelCallBack {
        interface onLoginListener {
            void onSuccess(String message);
            void onFailure(String message);
        }

        void loginEmail(Location location,
                        MainContractMap.ModelCallBack.onLoginListener listener);

    }


    interface PresenterCallBack {

        void onButtonClickLogout();
        void onButtonClickLoginGoogle();

    }
}
