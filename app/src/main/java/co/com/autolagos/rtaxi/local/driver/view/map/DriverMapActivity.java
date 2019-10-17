package co.com.autolagos.rtaxi.local.driver.view.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.squareup.otto.Subscribe;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.services.data.firebase.FirebaseService;
import co.com.autolagos.rtaxi.local.driver.model.services.gps.GpsService;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.DriverPresenter;
import co.com.autolagos.rtaxi.local.driver.presenter.preferences.MapPresenterImpl;
import co.com.autolagos.rtaxi.local.driver.view.map.preferences.PreferenceInterface;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

public class DriverMapActivity extends AppCompatActivity implements OnMapReadyCallback, PreferenceInterface {

    private Context context;
    private GoogleMap mMap;
    private CountDownTimer countDownTimer;
    //GPS System
    private FusedLocationProviderClient locationProviderClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    //Driver Data
    private DriverPresenter.MapPreferencePresenter mapPreferencePresenter;
    private Driver driver;
    private BottomSheetBehavior bottomSheetBehavior;
    private final static int REQUEST_LOCATION = 199;
    private LatLng destinationLatLng, pickupLatLng;
    private Marker pickupMarker;




    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_my_position)
    FloatingActionButton fabPosition;
    @BindView(R.id.layout_sheet)
    CoordinatorLayout layoutSheet;
    @BindView(R.id.circular_loaders)
    CircularFillableLoaders circularLoaders;

    @Override
    public void onStart() {
        super.onStart();
        StationBus.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        StationBus.getBus().unregister(this);
    }

    @Subscribe
    public void receivedMessage(MessageBusInformation messageBusInformation) {
        int option = messageBusInformation.getOption();
        DataSnapshot dataSnapshot = messageBusInformation.getJsonObject();
        switch (option) {
            case 4:
                if( messageBusInformation.getJsonObject().exists()){
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                List<Object> map = (List<Object>) dataSnapshot.getValue();
                double locationLat = 0;
                double locationLng = 0;
                if(map.get(0) != null){
                    locationLat = Double.parseDouble(map.get(0).toString());
                }
                if(map.get(1) != null){
                    locationLng = Double.parseDouble(map.get(1).toString());
                }
                pickupLatLng = new LatLng(locationLat,locationLng);
                pickupMarker = mMap.addMarker(new MarkerOptions().position(pickupLatLng).title("pickup location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher)));
                //getRouteToMarker(pickupLatLng);
                }

                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_map);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ButterKnife.bind(this);
        setupActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }

    private void setupActivity() {
        context = this;
        locationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        mapPreferencePresenter = new MapPresenterImpl(context, this);
        mapPreferencePresenter.getDriverData();
        startService(new Intent(context, FirebaseService.class));
        setupLayoutSheet();
        Nammu.init(context);
        setupToolbar();
        setupMapFragment();
        requestPermissions();

    }

    private void setupMapFragment() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    public void setupToolbar() {
        toolbar.setTitle("Mapa");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setContentInsetStartWithNavigation(0);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng bogota = new LatLng(4.66222, -74.06695);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, 16));
        moveToPosition();
    }

    private void moveToPosition() {
        if (mMap != null) {
            setupLocationRequest();
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            requestPermissions();
        }
    }

    private void requestPermissions() {
        if (!validatePermissions()) {
            String[] permissions = new String[2];
            permissions[0] = Manifest.permission.ACCESS_FINE_LOCATION;
            permissions[1] = Manifest.permission.ACCESS_COARSE_LOCATION;
            Nammu.askForPermission(this, permissions, new PermissionCallback() {
                @Override
                @SuppressLint("MissingPermission")
                public void permissionGranted() {
                    validatePermissions();
                }

                @Override
                public void permissionRefused() {
                    Snackbar.make(toolbar, "Debes aceptar los permisos para obtener tu ubicación.", Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean validatePermissions() {
        int permission_fine_location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int permission_coarse_location = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (permission_fine_location == PackageManager.PERMISSION_GRANTED && permission_coarse_location == PackageManager.PERMISSION_GRANTED) {
            locationProviderClient.requestLocationUpdates(locationRequest, createLocationCallback(), Looper.myLooper());
            if(mMap != null){
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
            startService(new Intent(context, GpsService.class));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setupLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this)
                .checkLocationSettings(settingsBuilder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response =
                            task.getResult(ApiException.class);
                } catch (ApiException ex) {
                    switch (ex.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException =
                                        (ResolvableApiException) ex;
                                resolvableApiException
                                        .startResolutionForResult(DriverMapActivity.this,
                                                REQUEST_LOCATION);
                            } catch (IntentSender.SendIntentException e) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                            break;
                    }
                }
            }
        });
    }

    public LocationCallback createLocationCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                List<Location> locations = locationResult.getLocations();
                if(!locations.isEmpty()) {
                    lastLocation = locations.get(locations.size() - 1);
                    LatLng coordinates = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
                    CameraUpdate location = CameraUpdateFactory.newLatLngZoom(coordinates, 16);
                    mMap.animateCamera(location);
                }
            }
        };
    }

    private void setupLayoutSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(layoutSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch(newState) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fabPosition.show();
                        if(countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        fabPosition.hide();
                        fabPosition.setVisibility(View.GONE);
                        countTimerSeconds();
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {}
        });
    }

    @OnClick(R.id.fab_my_position)
    public void setLocationMyPosition() {
        moveToPosition();
    }

    private void countTimerSeconds() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long milliseconds) {
                int seconds = (int)(60- ((milliseconds / 1000) % 60)) ;
                int progress = (int) (100 - (1.67 * seconds));
                circularLoaders.setProgress(progress);
                System.out.println("seconds: " + seconds+ "progrees + "+ progress);
            }
            public void onFinish() {
                System.out.println("Finish");
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                circularLoaders.setProgress(100);
            }
        }.start();
    }

    @Override
    public void showDriverData(Driver driverInterface) {
        driver = driverInterface;
        System.out.println(driverInterface.toString());
    }
}
