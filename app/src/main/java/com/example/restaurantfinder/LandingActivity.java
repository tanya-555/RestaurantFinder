package com.example.restaurantfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.restaurantfinder.controller.LandingController;
import com.example.restaurantfinder.di.DaggerSharedPrefComponent;
import com.example.restaurantfinder.di.SharedPrefModule;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class LandingActivity extends AppCompatActivity {

    private static final String TAG = LandingActivity.class.getName();

    @Inject
    SharedPreferences sharedPreferences;

    private Router router;
    private int cityId;
    private ImageView backBtn;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        router = Conductor.attachRouter(LandingActivity.this, findViewById(R.id.router), savedInstanceState);
        backBtn = findViewById(R.id.iv_back);
        disposable = new CompositeDisposable();
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        getCityId();
        initListener();
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

    private void initListener() {
        disposable.add(RxView.clicks(backBtn).observeOn(AndroidSchedulers.mainThread())
                  .subscribe(s -> {
                      this.finish();
                  }, e -> {
                      Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                  }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
