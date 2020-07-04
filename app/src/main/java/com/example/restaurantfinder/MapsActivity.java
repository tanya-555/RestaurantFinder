package com.example.restaurantfinder;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import com.jakewharton.rxbinding2.view.RxView;

import org.joda.time.DateTime;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String LOCATION = "location";
    private static final String API_KEY = "AIzaSyCT4GK9ZJ86lt9czWuwYGfjXa7BPIDedP0";
    private static final String TAG = MapsActivity.class.getName();

    private GoogleMap mMap;
    private String latitude;
    private String longitude;
    private LatLng restaurantLocation;
    private LatLng currentLocation;
    private FloatingActionButton getDirectionBtn;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getDirectionBtn = findViewById(R.id.btn_get_directions);
        disposable = new CompositeDisposable();
        initGetDirectionsListener();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Add marker at specified latitude and longitude
        latitude = getIntent().getStringExtra(LATITUDE);
        longitude = getIntent().getStringExtra(LONGITUDE);
        restaurantLocation = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
        mMap.addMarker(new MarkerOptions().position(restaurantLocation).title(LOCATION));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantLocation));
        mMap.animateCamera(CameraUpdateFactory.zoomIn());
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }

    //get GeoApiContext for Google directions api
    private GeoApiContext getGeoContext() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext.setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS)
                .setApiKey(API_KEY);
    }

    private void initGetDirectionsListener() {
        disposable.add(RxView.clicks(getDirectionBtn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(s -> {
                    getDirections();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    private void getDirections() throws InterruptedException, ApiException, IOException {
        DateTime now = new DateTime();
        DirectionsResult result = DirectionsApi.newRequest(getGeoContext())
                .mode(TravelMode.DRIVING)
                .destination(String.valueOf(restaurantLocation))
                .origin(getCurrentLocation())
                .departureTime(now)
                .await();
        addMarkersToMap(result);
    }

    private void addMarkersToMap(DirectionsResult result) {
        mMap.addMarker(new MarkerOptions().position(restaurantLocation)
                .title(result.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(currentLocation)
                .title(result.routes[0].legs[0].endAddress));
        addPolyLine(result);
    }

    private void addPolyLine(DirectionsResult result) {
        List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private String getCurrentLocation() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
