package com.example.restaurantfinder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantfinder.R;
import com.example.restaurantfinder.event.SortOptionSelectedEvent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jakewharton.rxbinding2.view.RxView;

import org.greenrobot.eventbus.EventBus;

import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SortFragment extends BottomSheetDialogFragment {

    private static final String TAG = SortFragment.class.getName();

    private CompositeDisposable disposable;
    private ImageView ivClose;
    private RadioButton rbSortAsc;
    private RadioButton rbSortDsc;
    private RelativeLayout rlSortAsc;
    private RelativeLayout rlSortDsc;
    private String order;

    public static SortFragment newInstance() {
        return new SortFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_layout, container, false);
        disposable = new CompositeDisposable();
        ivClose = view.findViewById(R.id.iv_close);
        rlSortAsc = view.findViewById(R.id.rl_sort_asc);
        rlSortDsc = view.findViewById(R.id.rl_sort_dsc);
        rbSortAsc = view.findViewById(R.id.rb_sort_asc);
        rbSortDsc = view.findViewById(R.id.rb_sort_dsc);
        setSortOption();
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
        disposable.add(RxView.clicks(rlSortAsc).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    order = "asc";
                    setSortOption();
                    EventBus.getDefault().post(new SortOptionSelectedEvent("asc"));
                    this.dismiss();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
        disposable.add(RxView.clicks(rlSortDsc).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    order = "desc";
                    setSortOption();
                    EventBus.getDefault().post(new SortOptionSelectedEvent("desc"));
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

    private void setSortOption() {
        if("asc".equals(order)) {
            rbSortAsc.setChecked(true);
            rbSortDsc.setChecked(false);
        } else if("desc".equals(order)) {
            rbSortAsc.setChecked(false);
            rbSortDsc.setChecked(true);
        }
    }

    public void setSortOptionSelected(String order) {
        this.order = order;
    }
}
