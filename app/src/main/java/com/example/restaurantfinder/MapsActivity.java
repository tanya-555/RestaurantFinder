package com.example.restaurantfinder;

import android.Manifest;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.restaurantfinder.parser.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jakewharton.rxbinding2.view.RxView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE_KEY = "lat";
    private static final String LONGITUDE_KEY = "lng";
    private static final String LOCATION = "location";
    private static final String API_KEY = "AIzaSyCT4GK9ZJ86lt9czWuwYGfjXa7BPIDedP0";
    private static final String ORIGIN = "origin=";
    private static final String DESTINATION = "destination=";
    private static final String SENSOR_FALSE = "sensor=false";
    private static final String MODE_DRIVING = "mode=driving";
    private static final String DIRECTIONS_URL = "https://maps.googleapis.com/maps/api/directions/";
    private static final String JSON_OUTPUT = "json";
    private static final String LOCATION_NOT_FOUND = "Cannot find your current location!";
    private static final String KEY = "key=";
    private static final String TAG = MapsActivity.class.getName();

    private GoogleMap mMap;
    private String latitude;
    private String longitude;
    private LatLng restaurantLocation;
    private LatLng currentLocation;
    private FloatingActionButton getDirectionBtn;
    private CompositeDisposable disposable;
    private static final int REQUEST_CODE = 100;

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
        //initGetDirectionsListener();

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
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PERMISSION_GRANTED) {
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != REQUEST_CODE) {
            return;
        }
        if (grantResults[0] == PERMISSION_GRANTED) {
            enableMyLocation();
        }
    }

    private void initGetDirectionsListener() {
        disposable.add(RxView.clicks(getDirectionBtn)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    //getDirections();
                    if (currentLocation != null) {
                        String url = getDirectionUrl(currentLocation, restaurantLocation);
                        DownloadTask downloadTask = new DownloadTask();
                        downloadTask.execute(url);
                    } else {
                        Toast.makeText(this, LOCATION_NOT_FOUND, Toast.LENGTH_LONG).show();
                    }
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    private String getDirectionUrl(LatLng origin, LatLng dest) {
        String str_origin = ORIGIN + origin.latitude + "," + origin.longitude;
        String str_dest = DESTINATION + dest.latitude + "," + dest.longitude;
        String api_key = KEY + API_KEY;
        String parameters = str_origin + "&" + str_dest + "&" + SENSOR_FALSE + "&" + MODE_DRIVING + "&" + api_key;
        return DIRECTIONS_URL + JSON_OUTPUT + "?" + parameters;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection connection = null;
        try {
            URL url = new URL(strUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            iStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d(TAG, Objects.requireNonNull(e.getMessage()));
        } finally {
            assert iStream != null;
            iStream.close();
            assert connection != null;
            connection.disconnect();
        }
        return data;
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";
            try {
                data = downloadUrl(url[0]);
            } catch (IOException e) {
                Log.d(TAG, e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            ParserTask parserTask = new ParserTask();
            parserTask.execute();
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>> routes = null;
            try {
                jsonObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser jsonParser = new DirectionsJSONParser();
                routes = jsonParser.parse(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            super.onPostExecute(result);

            ArrayList<LatLng> points;
            PolylineOptions polylineOptions = null;

            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                polylineOptions = new PolylineOptions();

                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double latitude = Double.parseDouble(Objects.requireNonNull(point.get(LATITUDE_KEY)));
                    double longitude = Double.parseDouble(Objects.requireNonNull(point.get(LONGITUDE_KEY)));
                    LatLng position = new LatLng(latitude, longitude);
                    points.add(position);
                }
                polylineOptions.addAll(points);
                polylineOptions.width(12);
                polylineOptions.color(Color.RED);
                polylineOptions.geodesic(true);
            }
            mMap.addPolyline(polylineOptions);
        }
    }
}
