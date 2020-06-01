package com.example.restaurantfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.restaurantfinder.databinding.ActivityMainBinding;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;
import java.util.logging.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private CompositeDisposable disposable;
    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.activity_main, null, false);
        setContentView(binding.getRoot());
        setListener();
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
        if(!TextUtils.isEmpty(binding.tvMobile.getText().toString()) && binding.tvMobile.getText().toString().length()==10) {
            Intent intent = new Intent(MainActivity.this, OTPActivity.class);
            intent.putExtra("mobile", binding.tvMobile.getText().toString());
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            Toast.makeText(this, "Enter a valid mobile number", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
