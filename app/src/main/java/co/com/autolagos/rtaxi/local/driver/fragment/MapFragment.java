package co.com.autolagos.rtaxi.local.driver.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.kelvinapps.rxfirebase.RxFirebaseChildEvent;
import com.mikhaellopez.circularfillableloaders.CircularFillableLoaders;
import com.ncorti.slidetoact.SlideToActView;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import co.com.autolagos.rtaxi.local.driver.Activity.HomeActivity;
import co.com.autolagos.rtaxi.local.driver.GpsLocationReceiver;
import co.com.autolagos.rtaxi.local.driver.MVP.MainContractMap;
import co.com.autolagos.rtaxi.local.driver.NetworkChangeReceiver;
import co.com.autolagos.rtaxi.local.driver.R;
import co.com.autolagos.rtaxi.local.driver.ServicesMVP.LocationUpdatesService;
import co.com.autolagos.rtaxi.local.driver.ShareLocationViewModel;
import co.com.autolagos.rtaxi.local.driver.ViewModelFactory;
import co.com.autolagos.rtaxi.local.driver.base.AppBase;
import co.com.autolagos.rtaxi.local.driver.di.component.DaggerActivityComponent;
import co.com.autolagos.rtaxi.local.driver.di.module.MvpModule;
import co.com.autolagos.rtaxi.local.driver.interfaces.CompleteTransaction;
import co.com.autolagos.rtaxi.local.driver.model.entities.Career;
import co.com.autolagos.rtaxi.local.driver.model.entities.Coordinate;
import co.com.autolagos.rtaxi.local.driver.model.entities.Driver;
import co.com.autolagos.rtaxi.local.driver.model.entities.LocationStatus;
import co.com.autolagos.rtaxi.local.driver.model.events.MessageBusInformation;
import co.com.autolagos.rtaxi.local.driver.model.events.StationBus;
import co.com.autolagos.rtaxi.local.driver.model.utils.ConvertDatasnapshopCareer;
import co.com.autolagos.rtaxi.local.driver.model.utils.ConvertDatasnapshopUser;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.Preferences;
import co.com.autolagos.rtaxi.local.driver.sharedPreferences.career.CareerPreference;
import co.com.autolagos.rtaxi.local.driver.utils.Constants;
import co.com.autolagos.rtaxi.local.driver.utils.Dlog;
import co.com.autolagos.rtaxi.local.driver.utils.Utils;
import co.com.autolagos.rtaxi.local.driver.view.ViewGpsPermision;
import co.com.autolagos.rtaxi.local.driver.view.dialogs.DialogCustom;
import io.reactivex.Maybe;
import io.reactivex.disposables.CompositeDisposable;
import rx.Observable;
import rx.Subscription;

import static co.com.autolagos.rtaxi.local.driver.utils.Constants.REQUEST_LOCATION;


@SuppressLint("RestrictedApi")
public class MapFragment extends Fragment implements OnMapReadyCallback,
        RoutingListener, HomeActivity.listenCloseMenu
        , MainContractMap.ViewCallBack, CobroFragment.OnFragmentInteractionListener {

    //Components
    private Context context;
    private DialogCustom dialog;
    public static GoogleMap mMap;
    private CountDownTimer countDownTimer;
    //Driver Data
    private Driver driver;
    private BottomSheetBehavior bottomSheetBehavior;
    //Career
    private Career career;
    private List<Marker> markers = new ArrayList<Marker>();
    //Drawn
    private Marker carMarker, pickupMarker;
    private List<Polyline> polylines = new ArrayList<>();
    public static final String TAG = MapFragment.class.getSimpleName();
    private static final float DEFAULT_ZOOM = 14f;
    private boolean accept = false;
    private static final int[] COLORS = new int[]{R.color.primary_dark_material_light};
    private String idCarrera;
    private Routing routing;
    private CompositeDisposable compositeDisposable;
    private LatLng startPosition;
    private LatLng endPosition;
    private CompleteTransaction transactionListener;
    private Subscription subsp;

    @Inject
    ViewModelFactory mViewModelFactory;
    ShareLocationViewModel mViewModel;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fab_my_position)
    FloatingActionButton fabPosition;
    @BindView(R.id.layout_sheet)
    CoordinatorLayout layoutSheet;
    @BindView(R.id.content)
    RelativeLayout content;
    @BindView(R.id.cardView)
    CardView cardViewtool;
    @BindView(R.id.circular_loaders)
    CircularFillableLoaders circularLoaders;
    @BindView(R.id.lbl_distance)
    TextView lblDistance;
    @BindView(R.id.lbl_score)
    TextView lblScore;
    @BindView(R.id.lbl_time)
    TextView lblTime;
    @BindView(R.id.lbl_name_client)
    TextView lblNameClient;
    @BindView(R.id.fab_no)
    FloatingActionButton fabNo;
    @BindView(R.id.fab_career)
    FloatingActionButton fab_career;
    @BindView(R.id.fab_careerpickupuser)
    FloatingActionButton fab_careerpickupuser;
    @BindView(R.id.fab_careerbegin)
    FloatingActionButton fab_careerbegin;
    @BindView(R.id.fab_finish)
    FloatingActionButton fab_finish;
    @BindView(R.id.content_info_driver)
    LinearLayout content_info_driver;
    @BindView(R.id.btn_online)
    Switch btn_online;
    @BindView(R.id.content_alert)
    ViewGpsPermision content_alert;
    @BindView(R.id.btn_dialog)
    SlideToActView slide;
    //Datos conductor
    @BindView(R.id.nombre_conductor)
    TextView nombre_conductor;
    @BindView(R.id.placa)
    TextView placa;
    @BindView(R.id.score)
    TextView score;
    @BindView(R.id.content_gps)
    FrameLayout content_gps;


    private BroadcastReceiver mNotificationReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            Bundle bundle = intent.getExtras();

            Constants.GpsStatus gps = (Constants.GpsStatus) bundle.getSerializable(GpsLocationReceiver.filter);
            if (Constants.GpsStatus.NoEnable.equals(gps)) {

                alertGpsStatus(false);
            } else {
                if (Utils.checkGooglePlayServicesAvailability(getActivity()) &&
                        ((HomeActivity) getActivity()).checkPermission() && Utils.checkHighAccuracyLocationMode()
                        ) {
                    alertGpsStatus(true);
                } else {
                    setupLocationRequest();

                }

            }

        }
    };

    private BroadcastReceiver mNetworkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, android.content.Intent intent) {
            Bundle bundle = intent.getExtras();

            Constants.NetworkStatus network = (Constants.NetworkStatus) bundle.getSerializable(NetworkChangeReceiver.filter);
            if (Constants.NetworkStatus.CONNECTED.equals(network)) {
                //show dialog
                Toast.makeText(getActivity(), R.string.networkconnect, Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(getActivity(), R.string.networkdisconnet, Toast.LENGTH_SHORT).show();

            }

        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        compositeDisposable = new CompositeDisposable();

        DaggerActivityComponent.builder()
                .appComponent(AppBase.get(getContext()).component())
                .mvpModule(new MvpModule(this))
                .build()
                .inject(this);


        ((HomeActivity) getActivity()).setListMenu(this);
        toolbar = ((HomeActivity) getActivity()).toolbar;

    }


    @Override
    public void onStart() {
        super.onStart();
        StationBus.getBus().register(this);

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_driver_map, container, false);
        ButterKnife.bind(this, v);

        // startService(new Intent(context, FirebaseService.class));
        btn_online.setChecked(Preferences.getSavedOnline());


        btn_online.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Dlog.v("Switch State=", "" + isChecked);
                mViewModel.isOnline(isChecked);

            }
        });
        initViewModel();
        setupLayoutSheet();
        dialog = new DialogCustom(context);
        driver = mViewModel.getDriverData();


        nombre_conductor.setText(driver.getFullName());
        score.setText(driver.getQualification());
        placa.setText(driver.getPlaca());

        if (LocationUpdatesService.mLocation == null && Utils.checkGooglePlayServicesAvailability(getActivity()) &&
                ((HomeActivity) getActivity()).checkLocationPermission() && Utils.checkHighAccuracyLocationMode()) {
            positionLastLocation(mViewModel.lastLocationManager(), "requestBuild");
        }
        return v;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        slide.setOnSlideCompleteListener(view1 -> {
            setCareer();
            slide.resetSlider();
        });
        slide.setOnSlideResetListener(new SlideToActView.OnSlideResetListener() {
            @Override
            public void onSlideReset(@NonNull SlideToActView view) {
                // log.append("\n" + getTime() + " onSlideReset");
            }
        });
        slide.setOnSlideUserFailedListener(new SlideToActView.OnSlideUserFailedListener() {
            @Override
            public void onSlideFailed(@NonNull SlideToActView view, boolean isOutside) {
                //log.append("\n" + getTime() + " onSlideUserFailed - Clicked outside: " + isOutside);
            }
        });
        slide.setOnSlideToActAnimationEventListener(new SlideToActView.OnSlideToActAnimationEventListener() {
            @Override
            public void onSlideCompleteAnimationStarted(@NonNull SlideToActView view, float threshold) {
                //   log.append("\n" + getTime() + " onSlideCompleteAnimationStarted - " + threshold + "");
            }

            @Override
            public void onSlideCompleteAnimationEnded(@NonNull SlideToActView view) {
                // log.append("\n" + getTime() + " onSlideCompleteAnimationEnded");

            }

            @Override
            public void onSlideResetAnimationStarted(@NonNull SlideToActView view) {
                // log.append("\n" + getTime() + " onSlideResetAnimationStarted");
            }

            @Override
            public void onSlideResetAnimationEnded(@NonNull SlideToActView view) {
                // log.append("\n" + getTime() + " onSlideResetAnimationEnded");
            }
        });
    }

    private void checkStatusService() {
        accept = CareerPreference.getAcceptCareer();
        career = CareerPreference.show();
        if (CareerPreference.getStatusServiceCareer() != null && mViewModel.getOnline() && career != null && career.getUid() != null
                )
            StationBus.getBus().post(new MessageBusInformation(CareerPreference.getStatusServiceCareer(), career.getUid(), career));
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng bogota = new LatLng(4.66222, -74.06695);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(bogota, DEFAULT_ZOOM));
        if (Utils.checkGooglePlayServicesAvailability(getActivity()) &&
                ((HomeActivity) getActivity()).checkPermission() && Utils.checkHighAccuracyLocationMode()) {
            moveToPosition();

        }
        else{
            alertGpsStatus(false);

        }
        if (Utils.checkGooglePlayServicesAvailability(getActivity()) &&
                ((HomeActivity) getActivity()).checkPermission()) {
            if (Utils.checkHighAccuracyLocationMode()) {
                checkStatusService();
            }
        }

    }


    @Override
    public void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mNotificationReceiver);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mNetworkReceiver);
        AppBase.activityPaused();
    }


    @Override
    public void onResume() {
        super.onResume();
        AppBase.activityResumed();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mNotificationReceiver,
                new IntentFilter(GpsLocationReceiver.filter));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mNetworkReceiver,
                new IntentFilter(NetworkChangeReceiver.filter));
        SupportMapFragment mapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);


    }


    @Override
    public void onStop() {
        super.onStop();
        StationBus.getBus().unregister(this);
        compositeDisposable.dispose();
    }


    private void getAssignedCustomer() {
        // mViewModel.getCarreras().subscribe(this::getCarreras, this::getCarrerasError);
        if (accept)
            StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Available, CareerPreference.show().getUid(), CareerPreference.show()));
    }


    private void setupChildAdded(RxFirebaseChildEvent<DataSnapshot> dataSnapshot) {
        // Driver driver = driverPreferences.show();
        if (dataSnapshot.getValue().exists()) {
            Career career = ConvertDatasnapshopCareer.convertDatasnapToCareer(dataSnapshot.getValue());
            if (career != null) {
                //   careerPreference.store(career);
                System.out.println(career.toString());
            }
        }
    }

    private void setupChildChanged(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            // StationBus.getBus().post(new MessageBusInformation(Career.serviceType.Cambios, "", dataSnapshot));
        } else {
            System.out.println("Esa wea no existe");
        }
    }


    public void alertGpsStatus(boolean b) {
        if (b) {
            new Handler().postDelayed(() -> {
                //----------------------------
                moveToPosition();
                //----------------------------

            }, 2000);

            content_alert.setVisibility(View.GONE);
        } else {
            content_alert.setVisibility(View.VISIBLE);
        }
    }


    private void setupLocationRequest() {

        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)
                .setFastestInterval(1 * 1000);

        LocationSettingsRequest.Builder settingsBuilder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        settingsBuilder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getActivity())
                .checkLocationSettings(settingsBuilder.build());

        result.addOnCompleteListener(task -> {
            try {
                LocationSettingsResponse response =
                        task.getResult(ApiException.class);

            } catch (ApiException ex) {
                switch (ex.getStatusCode()) {


                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            ResolvableApiException resolvableApiException = (ResolvableApiException) ex;
                            resolvableApiException
                                    .startResolutionForResult(getActivity(),
                                            REQUEST_LOCATION);

                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });


    }


    private void initViewModel() {
        mViewModel = ViewModelProviders.of(this, mViewModelFactory).get(ShareLocationViewModel.class);
        mViewModel.onAuthListener().subscribe(this::listenerUser, this::errorListenerUser);
    }


    private void errorListenerUser(Throwable throwable) {


    }

    private void listenerUser(FirebaseUser firebaseUser) {

        if (firebaseUser != null)
            Dlog.d("RxFirebaseSample", "signin");
        else {
            Dlog.d("RxFirebaseSample", "out");
            onLoginFailure("");
        }
    }


    private void positionLastLocation(Maybe<Location> maybe, final String methodName) {
        maybe.subscribe(
                loc -> {
                    onLocationUpdatedLastLocation(null, loc);
                    Dlog.e("URL", loc.toString());
                },

                throwable -> {
                    onLocationUpdateError(throwable);
                }, () -> {
                    final String pattern = "%s Completed:";
                    Dlog.e("URL", pattern);
                    //  showSnackbar(String.format(pattern, methodName));
                });
    }


    private void onLocationUpdateError(Throwable t) {
        if (t instanceof SecurityException) {
            // Access to coarse or fine location are not allowed by the user
            ((HomeActivity) getActivity()).checkLocationPermission();
        }
        // backMarker();
        // Toast.makeText(getActivity(), "LocationUpdateErr: " + t.toString(), Toast.LENGTH_LONG).show();

    }


    private void onLocationUpdatedLastLocation(Location lastloc, Location loc) {

        LocationUpdatesService.mLocation = loc;
        mViewModel.shareLastLocation(loc, Preferences.getSavedOnline());
        showMyLocationwithCustomer(lastloc, loc);


    }


    private void showMyLocationwithCustomer(Location loc, Location location) {
        if (loc == null)
            startPosition = null;
        else
            startPosition = new LatLng(loc.getLatitude(), loc.getLongitude());

        endPosition = new LatLng(location.getLatitude(), location.getLongitude());

        startBikeAnimation(location, startPosition, endPosition);
    }

    private void getDriverLocationUpdate() {


        endPosition = new LatLng(LocationUpdatesService.mLocation.getLatitude(), LocationUpdatesService.mLocation.getLongitude());

        Dlog.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);

        if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {

            Dlog.e(TAG, "NOT SAME");
            // startBikeAnimation(startPosition, endPosition);

        } else {

            Dlog.e(TAG, "SAMME");
        }
    }


    public static float getBearing(LatLng begin, LatLng end) {
        double lat = Math.abs(begin.latitude - end.latitude);
        double lng = Math.abs(begin.longitude - end.longitude);

        if (begin.latitude < end.latitude && begin.longitude < end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)));
        else if (begin.latitude >= end.latitude && begin.longitude < end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 90);
        else if (begin.latitude >= end.latitude && begin.longitude >= end.longitude)
            return (float) (Math.toDegrees(Math.atan(lng / lat)) + 180);
        else if (begin.latitude < end.latitude && begin.longitude >= end.longitude)
            return (float) ((90 - Math.toDegrees(Math.atan(lng / lat))) + 270);
        return -1;
    }

    private void startBikeAnimation(Location location, final LatLng start, final LatLng end) {

        if (mMap == null)
            return;


        if (carMarker == null) {

            MarkerOptions options = new MarkerOptions().position(end).
                    flat(true).icon(BitmapDescriptorFactory.fromResource(R.mipmap.new_car_small));

            getActivity().runOnUiThread(() -> carMarker = mMap.addMarker(options));
            carMarker.setAnchor(0.5f, 0.5f);

            Dlog.e("URL", "jajaja que raro");
        }


        if(start != null){

            if ((start.latitude != end.latitude) || (start.longitude != end.longitude)) {

                Log.e(TAG, "NOT SAME");

                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.setDuration(2000);
                valueAnimator.setInterpolator(new LinearInterpolator());
                valueAnimator.addUpdateListener(valueAnimator1 -> {

                    //LogMe.i(TAG, "Car Animation Started...");
                    float v = valueAnimator1.getAnimatedFraction();
                    double lng = v * end.longitude + (1 - v)
                            * start.longitude;
                    double lat = v * end.latitude + (1 - v)
                            * start.latitude;

                    LatLng newPos = new LatLng(lat, lng);
                    carMarker.setPosition(newPos);
                    carMarker.setAnchor(0.5f, 0.5f);
                    carMarker.setRotation(getBearing(start, end));

                    // todo : Shihab > i can delay here
                    startPosition = carMarker.getPosition();

                });
                valueAnimator.start();
            } else{
                Log.e(TAG, "SAMME");

                carMarker.setPosition(end);
                Location prevLoc = new Location("service Provider");

                float bearing = prevLoc.bearingTo(location);

                carMarker.setRotation(bearing);
            }

        }else {

            Log.e(TAG, "SAMME");

            carMarker.setPosition(end);
            Location prevLoc = new Location("service Provider");

            float bearing = prevLoc.bearingTo(location);

            carMarker.setRotation(bearing);
        }


        movePositionCam(end);

    }


    private void movePositionCam(LatLng location) {
        if (mMap == null) return;
        if (carMarker == null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, DEFAULT_ZOOM));
        } else {

            String carrera = CareerPreference.show().getUid();
            if (carrera != null)
                if (carrera.equals(idCarrera) && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    LatLng pos = new LatLng(LocationUpdatesService.mLocation.getLatitude(), LocationUpdatesService.mLocation.getLongitude());
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));
                } else {
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16));
                }
        }


    }


    @Subscribe
    public void receivedMessage(MessageBusInformation messageBusInformation) {
        //Crashlytics.getInstance().crash();

        if (messageBusInformation.getOption() instanceof LocationStatus) {
            LocationStatus option = (LocationStatus) messageBusInformation.getOption();
            switch (option) {
                case CHANGED:
                    onLocationUpdatedLastLocation(messageBusInformation.getLastLocation(), messageBusInformation.getmLocation());
                    drawRoute();
                    break;
                case NOTAVAILABLE:
                    dialog.dialogQuestionGps();
                    break;
                case LASTLOCATION:
                    moveToPosition();
                    break;
                case OFFLOCATION:
                    if (!accept && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        idCarrera = "";
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        moveToPosition();
                    }
                    break;
            }
        }

        if (messageBusInformation.getOption() instanceof Career.serviceType) {
            Career.serviceType optionCareer = (Career.serviceType) messageBusInformation.getOption();
            //Carrera estado

            switch (optionCareer) {
                case Available:
                    career = messageBusInformation.getCareer();
                    if (career != null && !career.getUid().equals(idCarrera) && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        content_info_driver.setVisibility(View.GONE);
                        content_gps.setVisibility(View.GONE);
                        setupInfoInDialog(career);
                        countTimerSeconds();
                        CareerPreference.setStatusCareer(optionCareer);
                    }
                    break;

                case Accepted:
                    career = messageBusInformation.getCareer();
                    if (career != null && !career.getUid().equals(idCarrera)) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        content_info_driver.setVisibility(View.GONE);
                        setupInfoInDialog(career);
                        CareerPreference.setStatusCareer(optionCareer);
                        acceptCareerinMap();
                    }
                    break;


                case Pickup:

                    career = messageBusInformation.getCareer();
                    if (career != null) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        content_info_driver.setVisibility(View.GONE);
                        setupInfoInDialog(career);
                        CareerPreference.setStatusCareer(optionCareer);
                        setPickupUser();
                    }
                    break;


                case Travel:
                    career = messageBusInformation.getCareer();
                    if (career != null) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        content_info_driver.setVisibility(View.GONE);
                        setupInfoInDialog(career);
                        CareerPreference.setStatusCareer(optionCareer);
                        setStartCareer();
                    }
                    break;


                case Canceled:
                    idCarrera = "";
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

                    break;

                case Cambios:

                    if (messageBusInformation.getMessage().equals(idCarrera) && !accept && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                        idCarrera = "";
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        moveToPosition();
                        CareerPreference.setStatusCareer(optionCareer);

                    }

                    break;
                case Finish:

                    idCarrera = "";
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    moveToPosition();
                    CareerPreference.setStatusCareer(optionCareer);
                    showCosto();

                    break;

            }
        }


    }

    public void showCosto() {

        CobroFragment contactUsFragment = new CobroFragment(this);
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.mainLayout, contactUsFragment, CobroFragment.TAG).addToBackStack(null).commit();
    }


    //Primera vez muestra el marker en la ultima posicion del celular
    private void moveToPosition() {

        if (Utils.checkGooglePlayServicesAvailability(getActivity()) &&
                ((HomeActivity) getActivity()).checkLocationPermission()) {
            if (Utils.checkHighAccuracyLocationMode()) {
                // mMap.setMyLocationEnabled(true);
                mMap.clear();
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setPadding(0, 0, 0, 0);
                carMarker = null;

                if (LocationUpdatesService.mLocation != null) {
                    onLocationUpdatedLastLocation(null, LocationUpdatesService.mLocation);
                } else {
                    positionLastLocation(mViewModel.lastLocationManager(), "requestBuild");

                }
            } else {

                setupLocationRequest();

            }

        }
    }


    private void setupLayoutSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(layoutSheet);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_SETTLING:
                        getActivity().runOnUiThread(() -> {
                            content_info_driver.setVisibility(View.GONE);
                        });
                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:
                        fabPosition.show();
                        content_info_driver.setVisibility(View.VISIBLE);
                        content_gps.setVisibility(View.VISIBLE);
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            countDownTimer = null;
                        }
                        if (isVisible()) {
                            moveToPosition();
                        }
                        //n//¡¡oCarrera();
                        //getActivity().runOnUiThread(() ->{                        content_info_driver.setVisibility(View.VISIBLE);
                        //  } );
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:

                            content_info_driver.setVisibility(View.GONE);
                            drawRoute();

                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });
    }

    @OnClick(R.id.fab_my_position)
    public void setLocationMyPosition() {
        moveToPosition();
    }


    private void countTimerSeconds() {

        getActivity().runOnUiThread(() ->

                countDownTimer = new CountDownTimer(60000, 1000) {
                    public void onTick(long milliseconds) {
                        int seconds = (int) (60 - ((milliseconds / 1000) % 60));
                        int progress = (int) (100 - (1.67 * seconds));
                        circularLoaders.setProgress(progress);
                        System.out.println("seconds: " + seconds + "; progrees + " + progress);
                    }

                    public void onFinish() {
                        System.out.println("Finish");
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        circularLoaders.setProgress(100);
                    }
                }.start());
    }

    private void setupInfoInDialog(Career career) {
        mViewModel.getUserInfo(career.getUidUser()).subscribe(this::infoUser, this::errorUser);
        mViewModel.getAddresswithLocation(career.getJourney().getOrigin()).subscribe(this::onAddressUpdate, this::onLocationUpdateError);
        idCarrera = career.getUid();
        fabPosition.hide();
        fabPosition.setVisibility(View.GONE);
    }


    public void onAddressUpdate(List<Address> address) {
        if (address.size() > 0) {
            Address add = address.get(0);
            toolbar.setTitle(getAddressText(add));
            compositeDisposable.dispose();
        }
    }


    private String getAddressText(Address address) {
        String addressText = "";
        final int maxAddressLineIndex = address.getMaxAddressLineIndex();

        for (int i = 0; i <= maxAddressLineIndex; i++) {
            addressText += address.getAddressLine(i);
            if (i != maxAddressLineIndex) {
                addressText += "\n";
            }
        }

        return addressText;
    }


    private void infoUser(DataSnapshot dataSnapshot) {

        String calificacion = ConvertDatasnapshopUser.convertDatasnapToCareer(dataSnapshot).getCalificacion();

        lblScore.setText(String.valueOf(calificacion.isEmpty() ? "0" : calificacion));
        lblNameClient.setText(ConvertDatasnapshopUser.convertDatasnapToCareer(dataSnapshot).getName());
        // toolbar.setTitle(mViewModel.getClosestDriver(););
        setupAvatarCustomer(ConvertDatasnapshopUser.convertDatasnapToCareer(dataSnapshot).getPicture());
    }

    private void errorUser(Throwable throwable) {


    }

    private void setupAvatarCustomer(String avatar) {
        if (avatar == null || avatar.isEmpty()) {
            avatar = "http://2.bp.blogspot.com/-89CYtalpstQ/T8eSa-ylrAI/AAAAAAAABi0/oI8MbQwUVb4/s1600/imagesCAXA8QKW.jpg";
        }
        Picasso.get()
                .load(avatar)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_user)
                .into(circularLoaders);
    }

    private void drawRoute() {

        if (mMap == null || carMarker == null)
            return;

        if (career != null && bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            Coordinate direccion;


            if (CareerPreference.getStatusServiceCareer() == Career.serviceType.Available || CareerPreference.getStatusServiceCareer() == Career.serviceType.Accepted)
                direccion = career.getJourney().getOrigin();
            else direccion = career.getJourney().getDestiny();

            LatLng originLatLng = new LatLng(direccion.getLat(), direccion.getLon());

            if (pickupMarker != null)
                pickupMarker.remove();

            pickupMarker = mMap.addMarker(new MarkerOptions().position(originLatLng).title("pickup location").icon(Utils.bitmapDescriptorFromVector(getActivity(), R.mipmap.ic_launcher)));

            markers.clear();

            markers.add(carMarker);
            markers.add(pickupMarker);

            //googleMap.setPadding(left, top, right, bottom);9

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (Marker marker : markers) {
                if (marker != null)
                    builder.include(marker.getPosition());
                else
                    return;
            }

            LatLngBounds bounds = builder.build();

            int padding = 50; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);


            mMap.setPadding(0, cardViewtool.getHeight() + 200, 0, layoutSheet.getHeight());
            mMap.animateCamera(
                    cu
                    , 2000
                    , new GoogleMap.CancelableCallback() {
                        @Override
                        public void onFinish() {

                        }

                        @Override
                        public void onCancel() {

                        }
                    });

            getRouteToMarker(originLatLng);

        }
    }

    private void getRouteToMarker(LatLng originLatLng) {
        routing = new Routing.Builder()
                .key(getResources().getString(R.string.API_KEY))
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(new LatLng(LocationUpdatesService.mLocation.getLatitude(), LocationUpdatesService.mLocation.getLongitude()), originLatLng)
                .build();


        routing.execute();
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        if (e != null) {
            // Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        try {
            if (polylines.size() > 0) {
                for (Polyline poly : polylines) {
                    poly.remove();
                }
            }
            polylines = new ArrayList<>();
            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_COLLAPSED) {

                lblTime.setText(route.get(0).getDurationText());
                lblDistance.setText(route.get(0).getDistanceText());

                for (int i = 0; i < route.size(); i++) {
                    //googleMap.setPadding(left, top, right, bottom);9

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (Marker marker : markers) {
                        if (marker != null)
                            builder.include(marker.getPosition());
                        else
                            return;
                    }

                    for (LatLng marker : route.get(i).getPoints()) {
                        if (marker != null)
                            builder.include(marker);
                        else
                            return;
                    }


                    LatLngBounds bounds = builder.build();

                    int padding = 50; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);

                    int colorIndex = i % COLORS.length;
                    PolylineOptions polyOptions = new PolylineOptions();
                    polyOptions.color(getResources().getColor(COLORS[colorIndex]));
                    polyOptions.width(10 + i * 3);
                    polyOptions.addAll(route.get(i).getPoints());
                    Polyline polyline = mMap.addPolyline(polyOptions);
                    polylines.add(polyline);

                    mMap.setPadding(0, cardViewtool.getHeight() + 200, 0, layoutSheet.getHeight());
                    mMap.animateCamera(
                            cu
                            , 2000
                            , new GoogleMap.CancelableCallback() {
                                @Override
                                public void onFinish() {

                                }

                                @Override
                                public void onCancel() {

                                }
                            });
                }
                routing.cancel(true);
            }
        } catch (Exception e) {
            Dlog.e(TAG, "error route");

        }

    }

    private void erasePolylines() {
        //  polylines.clear();
        //MapAnimator.getInstance().animateRoute(mMap, polylines);

    }

    @Override
    public void onRoutingCancelled() {
    }

//************************************************************************************************//


    @OnClick(R.id.cancelar_career)
    public void setCancel() {

        switch (CareerPreference.getStatusServiceCareer()) {
            case Available:
                setAvailableCareer();

                break;

            case Accepted:
                setPickupUser();
                break;

            case Pickup:
                setStartCareer();
                break;

            case Travel:
                setFinishCareer();
                break;

//            case Canceled:
//
//                break;
//
//            case Cambios:
//
//                break;
//            case Finish:
//
//                break;

        }
    }

    // @OnClick(R.id.btn_dialog)
    public void setCareer() {

        switch (CareerPreference.getStatusServiceCareer()) {
            case Available:
                setAvailableCareer();

                break;

            case Accepted:


                ValueEventListener pickupUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            LocationUpdatesService.startWaitTime();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                        Toast.makeText(getActivity(), getResources().getString(R.string.intentelo), Toast.LENGTH_SHORT).show();
                    }
                };

                mViewModel.setPickupUser(pickupUser);

                setPickupUser();
                break;

            case Pickup:
                mViewModel.setStartCareer();
                setStartCareer();
                break;

            case Travel:
                setFinishCareer();
                //muestra recibo
                break;

//            case Canceled:
//
//                break;
//
//            case Cambios:
//
//                break;
        }
    }

    // @OnClick(R.id.fab_career)
    public void setAvailableCareer() {


        transactionListener = (databaseError, b, dataSnapshot) -> {

            if (dataSnapshot.getValue().equals(mViewModel.getUid())) {
                accept = true;
                continuarCarrera(dataSnapshot);
            } else {
                noCarrera();
            }
        };

        if (!accept) {

            fab_career.setVisibility(View.GONE);

            Observable<DataSnapshot> ifcareer = mViewModel.ifAcceptCareer(career.getUid());

            ifcareer.subscribe(dataSnapshot -> {
                int ifaccept = dataSnapshot.child(Constants.status).getValue(Integer.class);
                if (ifaccept == Career.serviceType.Available.ordinal()) {
                    idCarrera = career.getUid();
                    accept = true;
                    CareerPreference.setAcceptCareer(true);
                    mViewModel.acceptCareer(transactionListener);
                }

            }, throwable -> {
                noCarrera();
            });
        }

    }


    // @OnClick(R.id.fab_careerpickupuser)
    public void setPickupUser() {

        fab_careerpickupuser.setVisibility(View.GONE);
        slide.setText(getResources().getString(R.string.startcareer));
        CareerPreference.setStatusCareer(Career.serviceType.Pickup);
        ((HomeActivity) getActivity()).mLUService.listenerCareer(career.getUid());
        drawRoute();

    }


    //@OnClick(R.id.fab_careerbegin)
    public void setStartCareer() {

        //logic view
        ((HomeActivity) getActivity()).mLUService.listenerCareer(career.getUid());

        fab_careerbegin.setVisibility(View.GONE);
        slide.setText(getResources().getString(R.string.finishcareer));
        CareerPreference.setStatusCareer(Career.serviceType.Travel);
        drawRoute();
    }


    //@OnClick(R.id.fab_finish)
    public void setFinishCareer() {

        double tarifa = mViewModel.setCalculateCareer();

        mViewModel.setTarifa(tarifa);
        mViewModel.setSFinishCareer();
        CareerPreference.setAcceptCareer(false);
        CareerPreference.setStatusCareer(Career.serviceType.Finish);
        slide.setText(getResources().getString(R.string.aceptar));


        fab_careerbegin.setVisibility(View.VISIBLE);
        fab_careerpickupuser.setVisibility(View.VISIBLE);
        fab_career.setVisibility(View.VISIBLE);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        accept = false;
        idCarrera = "";
        ((HomeActivity) getActivity()).mLUService.unSubscribe();
        showCosto();


    }

    private void noCarrera() {

        CareerPreference.setStatusCareer(Career.serviceType.Canceled);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        CareerPreference.setAcceptCareer(false);
        accept = false;
        idCarrera = "";
        ((HomeActivity) getActivity()).mLUService.unSubscribe();


    }


    private void continuarCarrera(DataSnapshot dataSnapshot) {


        if (dataSnapshot.exists()) {

            Toast.makeText(getActivity(), "complete" + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();

            String uidD = dataSnapshot.child(career.getUid()).child(Constants.UidConductor).getValue(String.class);

            Toast.makeText(getActivity(), "t" + uidD, Toast.LENGTH_LONG).show();
            circularLoaders.setProgress(100);
            acceptCareerinMap();
            if (countDownTimer != null)
                countDownTimer.cancel();

        }

    }

    private void acceptCareerinMap() {

        slide.setText(getResources().getString(R.string.yallegue));
        LatLng pos = new LatLng(LocationUpdatesService.mLocation.getLatitude(), LocationUpdatesService.mLocation.getLongitude());
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 18));
        CareerPreference.setStatusCareer(Career.serviceType.Accepted);
        ((HomeActivity) getActivity()).mLUService.listenerCareer(career.getUid());

    }


    private void errorAcceptCareer(Throwable throwable) {

        Toast.makeText(getActivity(), throwable.getMessage(), Toast.LENGTH_LONG).show();
        //noCarrera();

    }


    @OnClick(R.id.fab_no)
    public void clickDeclineCareer() {
        noCarrera();
        erasePolylines();
        //mapPreferencePresenter.changeStatusCareer(3, career, driver);
    }


    @Override
    public void backMenu() {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void onLoginSuccess() {

    }

    @Override
    public void onLoginFailure(String message) {
        getActivity().stopService(new Intent(getActivity(), LocationUpdatesService.class));

    }


    @Override
    public void onFragmentInteraction() {
        noCarrera();
    }
}

