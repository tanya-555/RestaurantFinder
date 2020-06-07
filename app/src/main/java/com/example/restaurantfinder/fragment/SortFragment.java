package com.example.restaurantfinder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantfinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SortFragment extends BottomSheetDialogFragment {

    private static final String TAG = SortFragment.class.getName();

    private CompositeDisposable disposable;
    private ImageView ivClose;

    public static SortFragment newInstance() {return new SortFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_layout, container, false);
        disposable = new CompositeDisposable();
        ivClose = view.findViewById(R.id.iv_close);
        initListener();
        return view;
    }

    private void initListener() {
        disposable.add(RxView.clicks(ivClose).observeOn(AndroidSchedulers.mainThread())
                  .subscribe(s -> {
                      this.dismiss();
                  }, e -> {
                      Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                  }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
