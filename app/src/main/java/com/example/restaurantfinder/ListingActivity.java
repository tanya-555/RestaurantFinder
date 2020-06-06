package com.example.restaurantfinder;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.bluelinelabs.conductor.RouterTransaction;
import com.example.restaurantfinder.controller.ListingController;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class ListingActivity extends AppCompatActivity {

    private static final String TAG = ListingActivity.class.getName();

    private Router router;
    private ImageView backBtn;
    private ImageView sortBtn;
    private CompositeDisposable disposable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_activity);
        router = Conductor.attachRouter(ListingActivity.this, findViewById(R.id.router), savedInstanceState);
        backBtn = findViewById(R.id.iv_back);
        sortBtn = findViewById(R.id.iv_sort);
        disposable = new CompositeDisposable();
        initListener();
        launchListingController();
    }

    private void launchListingController() {
        Bundle bundle = new Bundle(getIntent().getExtras());
        router.pushController(RouterTransaction.with(new ListingController(bundle)));
    }

    private void initListener() {
        disposable.add(RxView.clicks(backBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    this.finish();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
        disposable.add(RxView.clicks(sortBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {

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
