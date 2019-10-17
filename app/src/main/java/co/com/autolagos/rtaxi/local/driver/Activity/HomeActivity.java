package co.com.autolagos.rtaxi.local.driver.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import co.com.autolagos.rtaxi.local.driver.ViewModelHomeFactory;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;

import android.view.Gravity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import co.com.autolagos.rtaxi.local.driver.HomeViewModel;
import co.com.autolagos.rtaxi.local.driver.fragment.MapFragment;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.utils.Utils;
import okhttp3.internal.Util;

import static co.com.autolagos.rtaxi.local.driver.utils.Constants.REQUEST_LOCATION;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {


    public LocationUpdatesService mLUService;
    // private LocationReceiver myReceiver;
    private boolean mBound = false;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @BindView(R.id.toolbar)
    public Toolbar toolbar;


    @Inject
    ViewModelHomeFactory viewModelHomeFactory;
    HomeViewModel mViewModel;

    public listenCloseMenu listenMenu;

    public void setListMenu(listenCloseMenu listenMenu) {
        this.listenMenu = listenMenu;
    }

    public interface listenCloseMenu {
        void backMenu();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_home);

        ((AppBase) getApplication()).component().inject(this);

        ButterKnife.bind(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // setupToolbar();
        navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(this);

 /*       View headerView = navigationView.getHeaderView(0);
        TextView header_title = headerView.findViewById(R.id.header_title);
        header_title.setText(Preferences.getPreferencesUser().getName());

        TextView header_nav = headerView.findViewById(R.id.header_nav);
        header_nav.setText(Preferences.getPreferencesUser().getEmail());*/
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        MapFragment contactUsFragment = new MapFragment();
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, contactUsFragment, MapFragment.TAG)
                .commit();

        initViewModel();
    }




    @SuppressLint("CheckResult")
    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, viewModelHomeFactory).get(HomeViewModel.class);
        //  mViewModel.getSharingStateLiveData().observe(this, this::sharingStateChange);

    }


    public void setupToolbar() {
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.navbar_mobile));
        toolbar.setTag(R.drawable.navbar_mobile);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setNavigationOnClickListener(v -> finish());
        }

        toolbar.setNavigationOnClickListener(view -> {
            Dlog.d("URL", String.valueOf(toolbar.getTag()));


            Integer integer = (Integer) toolbar.getTag();
            switch (integer) {
                case R.drawable.ic_arrow_back_black_24dp:
                    //STUFF
                    toolbar.setNavigationIcon(getResources().
                            getDrawable(R.drawable.navbar_mobile));
                    toolbar.setTag(R.drawable.navbar_mobile);
                    listenMenu.backMenu();

                    break;
                case R.drawable.navbar_mobile:
                    //STUFF
                    drawer.openDrawer(Gravity.LEFT);
                    break;
            }

        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    /*    RefWatcher refWatcher = AppBase.getRefWatcher(this);
        refWatcher.watch(this);*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mi_perfil) {

//            Intent intent = new Intent(this, HistoryActivity.class);
//            intent.putExtra("customerOrDriver", "Customers");
//            startActivity(intent);

        } else if (id == R.id.nav_view) {
//            Intent Home = new Intent(this, SettingsActivity.class);
//            startActivity(Home);

        } else if (id == R.id.ayuda) {


        } else if (id == R.id.cerrar) {

            signOut();
            finish();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void signOut() {
        mViewModel.logout();
        Preferences.saveLogin(false);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_LOCATION) {

            if (resultCode == RESULT_OK) {

                MapFragment mapFrag = (MapFragment) getSupportFragmentManager().
                        findFragmentByTag(MapFragment.TAG);
                mapFrag.alertGpsStatus(true);

            } else {

                MapFragment mapFrag = (MapFragment) getSupportFragmentManager().
                        findFragmentByTag(MapFragment.TAG);
                mapFrag.alertGpsStatus(false);
            }

        }
    }


    private final ServiceConnection mServiceConection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mLUService = ((LocationUpdatesService.LocationBinder) service).getLocationUpdateService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mLUService = null;
            mBound = false;
        }
    };

    @Override
    protected void onPause() {
        //    LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        runService();
    }

    @Override
    protected void onStop() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        if (mBound) {
            unbindService(mServiceConection);
            mBound = false;
        }
        super.onStop();
    }

    private void runService() {
        if (checkPermission() && Utils.checkHighAccuracyLocationMode()) {

            if (Preferences.getSavedOnline()) {
                if (mLUService == null) {
                    mLUService = new LocationUpdatesService();
                }
                // myReceiver = new LocationReceiver();
                // Bind to the service. If the service is in foreground mode, this signals to the service
                // that since this activity is in the foreground, the service can exit foreground mode.
                bindService(new Intent(this, LocationUpdatesService.class), mServiceConection,
                        Context.BIND_AUTO_CREATE);
                //  LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                //      new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
                // Unbind from the service. This signals to the service that this activity is no longer
                // in the foreground, and the service can respond by promoting itself to a foreground
                // service.


            } else {
                if (startService(new Intent(this, LocationUpdatesService.class)) == null) {
                    unbindService(mServiceConection);
                }
            }
        }
    }


    public boolean checkPermission() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) && PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
    }



    public boolean checkLocationPermission() {
        if (!checkPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onAttachedToWindow() {
        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onAttachedToWindow();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(Constants.KEY_ONLINE) && Utils.checkHighAccuracyLocationMode()) {
            runService();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length <= 0) {
                    // Permission was not granted
                    MapFragment mapFrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
                    mapFrag.alertGpsStatus(false);
                } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                    if (mLUService == null)
                        mLUService = new LocationUpdatesService();
                    MapFragment mapFrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
                    mapFrag.alertGpsStatus(true);
//                    mLUService.requestLocationUpdates();
                } else {
                    // Permission denied.
                    //     changeButtonsState(false);
                    MapFragment mapFrag = (MapFragment) getSupportFragmentManager().findFragmentByTag(MapFragment.TAG);
                    mapFrag.alertGpsStatus(false);

                }
        }
    }
}
