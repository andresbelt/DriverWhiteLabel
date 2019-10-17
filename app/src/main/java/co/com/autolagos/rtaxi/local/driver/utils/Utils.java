package co.com.autolagos.rtaxi.local.driver.utils;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.firebase.database.ServerValue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;

import static co.com.autolagos.rtaxi.local.driver.utils.Constants.KEY_REQUESTING_LOCATION_UPDATES;


public class Utils {

    private ProgressDialog progressDialog;
    private static final int REQ_PERM_CODE = 9874;


    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    //Check password with minimum requirement here(it should be minimum 6 characters)
    public static boolean isPasswordValid(String password){
        return password.length() >= 6;
    }


    public static boolean isNetworkAvailable() {

        ConnectivityManager cn = (ConnectivityManager) AppBase.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            return true;
        } else {
            return false;
        }
    }


    public static String getHash(String original ) {

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.update(original.getBytes());
        byte[] digest = md.digest();
        StringBuffer sb = new StringBuffer();
        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }

        return sb.toString();
    }


    public static boolean checkGooglePlayServicesAvailability(Activity activity) {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int resultCode = api.isGooglePlayServicesAvailable(activity);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (api.isUserResolvableError(resultCode)) {
                Dialog dialog = api.getErrorDialog(activity, resultCode, 1234);
                dialog.setCancelable(false);
                dialog.setOnCancelListener(dialogInterface -> activity.finish());
                dialog.show();
            } else {
                Toast.makeText(activity, "Device unsupported", Toast.LENGTH_LONG).show();
                activity.finish();
            }

            return false;
        }

        return true;
    }

    public static boolean requestingLocationUpdates(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false);
    }

    /**
     * Stores the location updates state in SharedPreferences.
     * @param requestingLocationUpdates The location updates state.
     */
    public static void setRequestingLocationUpdates(Context context, boolean requestingLocationUpdates) {
        PreferenceManager.getDefaultSharedPreferences(AppBase.getInstance().getApplicationContext())
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply();
    }

    /**
     * Returns the {@code location} object as a human readable string.
     * @param location  The {@link Location}.
     */
    public static String getLocationText(Location location) {
        return location == null ? "Unknown location" :
                "(" + location.getLatitude() + ", " + location.getLongitude() + ")";
    }

    public static String getLocationTitle(Context context) {
        return context.getString(R.string.location_updated,
                DateFormat.getDateTimeInstance().format(new Date()));
    }


    public static void showKeyboard(EditText mEtSearch) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) AppBase.getInstance().getBaseContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public static void hideSoftKeyboard(EditText mEtSearch) {
        mEtSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) AppBase.getInstance().getBaseContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);


    }

    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable background = ContextCompat.getDrawable(context, R.drawable.ic_launcher);
        background.setBounds(0, 0, background.getIntrinsicWidth(), background.getIntrinsicHeight());
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorDrawableResourceId);
        vectorDrawable.setBounds(40, 20, vectorDrawable.getIntrinsicWidth() + 40, vectorDrawable.getIntrinsicHeight() + 20);
        Bitmap bitmap = Bitmap.createBitmap(background.getIntrinsicWidth(), background.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        background.draw(canvas);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

        public static double getDistanceFromLatLonInKm(Location last,
                Location current) {

        double R = 6371; // Radius of the earth in km
        double dLat = deg2rad(current.getLatitude() - last.getLatitude()); // deg2rad below
        double dLon = deg2rad(current.getLongitude() - last.getLongitude());
        double initialLat = deg2rad(last.getLatitude());
        double finalLat = deg2rad(current.getLatitude());
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                    Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(initialLat) * Math.cos(finalLat);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c; // Distance in km


        return d;

    }


    public static boolean checkHighAccuracyLocationMode() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //Equal or higher than API 19/KitKat
            try {
                locationMode = Settings.Secure.getInt(AppBase.getInstance().getContentResolver(), Settings.Secure.LOCATION_MODE);
                if (locationMode == Settings.Secure.LOCATION_MODE_HIGH_ACCURACY) {
                    return true;
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            //Lower than API 19
            locationProviders = Settings.Secure.getString(AppBase.getInstance().getApplicationContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            if (locationProviders.contains(LocationManager.GPS_PROVIDER) && locationProviders.contains(LocationManager.NETWORK_PROVIDER)) {
                return true;
            }
        }
        return false;
    }

    public static double deg2rad(double deg) {
        return deg * (Math.PI / 180);
    }

    public static boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(AppBase.getInstance().getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(AppBase.getInstance().getBaseContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public static boolean checkSiNoche(String inicioH, String inicioM, String finH, String finM) {
        int horaI = Integer.parseInt(inicioH);
        int minI = Integer.parseInt(inicioM);
        int horaF = Integer.parseInt(finH);
        int minF = Integer.parseInt(finM);

        int hours = 24 - horaI + horaF;

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, horaI);
        cal.set(Calendar.MINUTE, minI);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        long startHourMilli = cal.getTimeInMillis();
        Dlog.e("URL", cal.getTime().toString());

        cal.add(Calendar.HOUR_OF_DAY, hours);
        long endHourMilli = cal.getTimeInMillis();

        long currentMilli = Calendar.getInstance().getTimeInMillis();

        if (currentMilli >= startHourMilli && currentMilli <= endHourMilli)
            return true;
            else return false;

    }
}
