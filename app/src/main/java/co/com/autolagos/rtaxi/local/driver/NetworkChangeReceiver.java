package co.com.autolagos.rtaxi.local.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v4.content.LocalBroadcastManager;

import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;

public class NetworkChangeReceiver extends BroadcastReceiver {

    public static String filter = "NETWORK";


    @Override
    public void onReceive(final Context context, final Intent intent) {
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable()) {
            // Do something
            Dlog.d("Network Available ", "CONNECTED");
            Intent intent1 = new Intent(filter);
            intent1.putExtra(filter,Constants.NetworkStatus.CONNECTED);//If you need extra, add: intent.putExtra("extra","something");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

        }else{
            Dlog.d("Network Available ", "DISCONNECTED");

            Intent intent1 = new Intent(filter);
            intent1.putExtra(filter,Constants.NetworkStatus.DISCONNECTED);//If you need extra, add: intent.putExtra("extra","something");
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
        }

    }
}