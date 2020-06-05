package com.example.restaurantfinder;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.restaurantfinder.controller.LandingController;
import com.example.restaurantfinder.di.DaggerSharedPrefComponent;
import com.example.restaurantfinder.di.SharedPrefModule;

import javax.inject.Inject;

public class LandingActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    private Router router;
    private int cityId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        router = Conductor.attachRouter(LandingActivity.this, findViewById(R.id.router), savedInstanceState);
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        getCityId();
        launchLandingController();
    }

    private void getCityId() {
        if (sharedPreferences.contains("city_id")) {
            cityId = sharedPreferences.getInt("city_id", 0);
        }
    }

    private void launchLandingController() {
        Bundle bundle = new Bundle();
        bundle.putInt("city_id", cityId);
        router.setRoot(RouterTransaction.with(new LandingController(bundle)));
    }
}
