package com.example.restaurantfinder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bluelinelabs.conductor.Conductor;
import com.bluelinelabs.conductor.Router;
import com.example.restaurantfinder.di.DaggerSharedPrefComponent;
import com.example.restaurantfinder.di.SharedPrefModule;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class DetailsActivity extends AppCompatActivity {

    @Inject
    SharedPreferences sharedPreferences;

    private static final String TAG = DetailsActivity.class.getName();

    private ImageView backBtn;
    private TextView tvCityName;
    private CompositeDisposable disposable;
    private String cityName;
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        setContentView(R.layout.details_activity);
        backBtn = findViewById(R.id.iv_back);
        tvCityName = findViewById(R.id.tv_city_name);
        webView =  findViewById(R.id.web_view);
        disposable = new CompositeDisposable();
        getCityName();
        tvCityName.setText(cityName);
        initListener();
        webView.loadUrl(getIntent().getStringExtra("search_url"));
    }

    private void getCityName() {
        if (sharedPreferences.contains("city_name")) {
            cityName = sharedPreferences.getString("city_name", "");
        }
    }

    private void initListener() {
        disposable.add(RxView.clicks(backBtn).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    this.finish();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }
}
