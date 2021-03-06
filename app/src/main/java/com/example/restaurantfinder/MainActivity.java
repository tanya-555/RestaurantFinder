package com.example.restaurantfinder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.restaurantfinder.databinding.ActivityMainBinding;
import com.example.restaurantfinder.di.DaggerSharedPrefComponent;
import com.example.restaurantfinder.di.SharedPrefModule;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    private static final String IS_LOGIN = "is_login";
    private static final String MOBILE = "mobile";
    private static final String VALID_MOBILE = "Enter a valid mobile number";
    private static final String VERIFICATION_COMPLETE = "Verification completed";

    ActivityMainBinding binding;
    private CompositeDisposable disposable;
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 1000;

    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        DaggerSharedPrefComponent.builder().sharedPrefModule(
                new SharedPrefModule(getApplicationContext())).build().inject(this);
        isLoggedIn();
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
        setContentView(binding.getRoot());
        initSharedPreferences();
        setListener();
    }

    private void initSharedPreferences() {
        if (!sharedPreferences.contains(IS_LOGIN)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGIN, false);
            editor.apply();
        }
    }

    private void setListener() {
        disposable.add(RxView.clicks(binding.btnSendOtp)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    checkInputValidity();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    private void checkInputValidity() {
        if (!TextUtils.isEmpty(binding.tvMobile.getText().toString()) && binding.tvMobile.getText().toString().length() == 10) {
            Intent intent = new Intent(MainActivity.this, OTPActivity.class);
            intent.putExtra(MOBILE, binding.tvMobile.getText().toString());
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, VALID_MOBILE, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            boolean otpVerification = Objects.requireNonNull(data.getExtras()).getBoolean("isOTPVerified");
            if (otpVerification) {
                Log.d(TAG, VERIFICATION_COMPLETE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(IS_LOGIN, true);
                editor.apply();
                Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    private void isLoggedIn() {
        if (sharedPreferences.getBoolean(IS_LOGIN, false)) {
            Intent intent = new Intent(MainActivity.this, SelectCityActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
