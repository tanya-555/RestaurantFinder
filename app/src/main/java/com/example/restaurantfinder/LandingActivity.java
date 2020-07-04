package com.example.restaurantfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;

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
    private static final String CITY_ID = "city_id";
    private static final String CITY_NAME = "city_name";
    private static final String LOGOUT = "Logout";

    @Inject
    SharedPreferences sharedPreferences;

    private Router router;
    private int cityId;
    private String cityName;
    private ImageView backBtn;
    private ImageView optionsBtn;
    private TextView tvCityName;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landing_activity);
        router = Conductor.attachRouter(LandingActivity.this, findViewById(R.id.router), savedInstanceState);
        backBtn = findViewById(R.id.iv_back);
        optionsBtn = findViewById(R.id.iv_options);
        tvCityName = findViewById(R.id.tv_city_name);
        disposable = new CompositeDisposable();
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        getCityDetails();
        tvCityName.setText(cityName);
        initListener();
        launchLandingController();
    }

    private void getCityDetails() {
        if (sharedPreferences.contains(CITY_ID)) {
            cityId = sharedPreferences.getInt(CITY_ID, 0);
        }
        if (sharedPreferences.contains(CITY_NAME)) {
            cityName = sharedPreferences.getString(CITY_NAME, "");
        }
    }

    private void launchLandingController() {
        Bundle bundle = new Bundle();
        bundle.putInt(CITY_ID, cityId);
        router.setRoot(RouterTransaction.with(new LandingController(bundle)));
    }

    private void initListener() {
        disposable.add(RxView.clicks(backBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    this.finish();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
        disposable.add(RxView.clicks(optionsBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    showPopupMenu();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void showPopupMenu() {
        PopupMenu menu = new PopupMenu(LandingActivity.this, optionsBtn);
        menu.getMenuInflater().inflate(R.menu.menu, menu.getMenu());
        menu.setOnMenuItemClickListener(item -> {
            if (LOGOUT.contentEquals(item.getTitle())) {
                clearPreferences();
                goToMainActivity();
                LandingActivity.this.finish();
            }
            return true;
        });
        menu.show();
    }

    private void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private void goToMainActivity() {
        Intent intent = new Intent(LandingActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
