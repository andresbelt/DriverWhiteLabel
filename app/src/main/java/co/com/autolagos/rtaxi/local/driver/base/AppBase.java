package co.com.autolagos.rtaxi.local.driver.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.crashlytics.android.Crashlytics;

import java.util.List;

import co.com.autolagos.rtaxi.local.driver.Activity.LoginActivity;
import co.com.autolagos.rtaxi.local.driver.di.component.AppComponent;
import co.com.autolagos.rtaxi.local.driver.di.component.DaggerAppComponent;
import co.com.autolagos.rtaxi.local.driver.di.module.AppModule;
import co.com.autolagos.rtaxi.local.driver.di.module.ContextModule;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;
import io.fabric.sdk.android.Fabric;

public class AppBase extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    public static boolean DEBUG;
    private static AppBase mInstance;
    private AppComponent component;
    private static boolean activityVisible;


    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }


    public static synchronized AppBase getInstance() {
        AppBase myApplication;
        synchronized (AppBase.class) {
            myApplication = mInstance;
        }
        return myApplication;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        DEBUG = isDebuggable();

        Fabric.with(this, new Crashlytics());

        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .contextModule(new ContextModule(this))
                .build();


        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);

    }


    public static AppBase get(Context context) {
        return (AppBase) context.getApplicationContext();
    }

    public AppComponent component() {
        return component;
    }


    /**
     * return debug mode or not
     *
     * @return
     */
    private boolean isDebuggable() {
        boolean debuggable = false;
        PackageManager pm = mInstance.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(mInstance.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }
        return debuggable;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        ActivityManager am = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE);
        List< ActivityManager.RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        String currentActivity = taskInfo.get(0).topActivity.getShortClassName();
        Dlog.i( "URL",  currentActivity);

        if (key.equals(Constants.KEY_LOGIN) && !currentActivity.equals(".Activity."+LoginActivity.class.getSimpleName())) {
            Intent inte= new Intent(mInstance, LoginActivity.class);
            inte.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(!Preferences.getSavedLogin())
                startActivity(inte);
        }

    }
}
