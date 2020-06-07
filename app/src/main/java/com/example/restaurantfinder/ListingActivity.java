package com.example.restaurantfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.restaurantfinder.controller.ListingController;
import com.example.restaurantfinder.di.DaggerSharedPrefComponent;
import com.example.restaurantfinder.di.SharedPrefModule;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ListingActivity extends AppCompatActivity {

    private static final String TAG = ListingActivity.class.getName();

    private Router router;
    private ImageView backBtn;
    private ImageView sortBtn;
    private TextView tvCityName;
    private String cityName;
    private CompositeDisposable disposable;
    private ListingController controller;
    private FragmentManager fragmentManager;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.listing_activity);
        router = Conductor.attachRouter(ListingActivity.this, findViewById(R.id.router), savedInstanceState);
        backBtn = findViewById(R.id.iv_back);
        sortBtn = findViewById(R.id.iv_sort);
        tvCityName = findViewById(R.id.tv_city_name);
        disposable = new CompositeDisposable();
        getCityName();
        tvCityName.setText(cityName);
        initListener();
        launchListingController();
    }

    private void launchListingController() {
        Bundle bundle = new Bundle(getIntent().getExtras());
        controller = new ListingController(bundle);
        router.pushController(RouterTransaction.with(controller));
    }

    private void initListener() {
        disposable.add(RxView.clicks(backBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    if (controller != null) {
                        router.popController(controller);
                    }
                    this.finish();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
        disposable.add(RxView.clicks(sortBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    controller.showSortDialog(fragmentManager);
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void getCityName() {
        if (sharedPreferences.contains("city_name")) {
            cityName = sharedPreferences.getString("city_name", "");
        }
    }
}
