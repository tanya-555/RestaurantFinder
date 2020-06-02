package com.example.restaurantfinder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.restaurantfinder.databinding.OtpActivityBinding;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class OTPActivity extends AppCompatActivity {

    private static final String TAG = OTPActivity.class.getName();

    private OtpActivityBinding binding;
    private FirebaseAuth firebaseAuth;
    private CompositeDisposable disposable;
    private String verificationId;
    private ProgressDialog progressDialog;
    private boolean isOTPVerified = false;
    private String mobile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        disposable = new CompositeDisposable();
        firebaseAuth = FirebaseAuth.getInstance();
        binding = DataBindingUtil.inflate(getLayoutInflater(), R.layout.otp_activity, null, false);
        setContentView(binding.getRoot());
        initListener();
        mobile = getIntent().getStringExtra("mobile");
        sendVerificationCode(mobile);
    }

    private void initListener() {
        disposable.add(RxView.clicks(binding.btnVerify)
                .throttleWithTimeout(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    handleVerifySelection();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
        disposable.add(RxView.clicks(binding.tvResendOtp)
                .throttleFirst(60, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    sendVerificationCode(mobile);
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    private void handleVerifySelection() {
        if (TextUtils.isEmpty(binding.tvOtp.getText().toString())) {
            Toast.makeText(this, "Please enter OTP!", Toast.LENGTH_LONG).show();
        } else {
            verifyVerificationCode(verificationId);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }

    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                verifyCallbacks);
        Toast.makeText(this, "Verification code sent", Toast.LENGTH_LONG).show();
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks verifyCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                binding.tvOtp.setText(code);
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(OTPActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String id, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(id, forceResendingToken);
            verificationId = id;
        }
    };

    private void verifyVerificationCode(String code) {
        showProgressDialog();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Verification in progress..");
        }
        progressDialog.show();
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.hide();
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(OTPActivity.this, task -> {
                    hideProgressDialog();
                    if (task.isSuccessful()) {
                        isOTPVerified = true;
                        Intent intent = getIntent();
                        intent.putExtra("isOTPVerified", isOTPVerified);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        String message = "Something went wrong...Try again";

                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            message = "Invalid code entered...";
                        }
                        Toast.makeText(OTPActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                });
    }
}
