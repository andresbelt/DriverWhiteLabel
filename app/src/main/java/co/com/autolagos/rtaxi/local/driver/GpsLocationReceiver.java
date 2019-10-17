package co.com.autolagos.rtaxi.local.driver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.support.v4.content.LocalBroadcastManager;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;

import co.com.autolagos.rtaxi.local.driver.utils.Constants;


public class GpsLocationReceiver extends BroadcastReceiver {

    public static String filter = "GPS";


@Override
public void onReceive(Context context, Intent intent)
{
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED"))
        { 
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            //Start your Activity if location was enabled:
            if (isGpsEnabled || isNetworkEnabled) {
                Dlog.d("GPS","enabled");

                Intent intent1 = new Intent(filter);
                intent1.putExtra("GPS",Constants.GpsStatus.Enable);//If you need extra, add: intent.putExtra("extra","something");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);

            }else{
                Dlog.d("GPS","not enabled");

                Intent intent1 = new Intent(filter);
                intent1.putExtra("GPS",Constants.GpsStatus.NoEnable);//If you need extra, add: intent.putExtra("extra","something");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent1);
            }


        }
}



}