package com.example.restaurantfinder.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.restaurantfinder.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SortFragment extends BottomSheetDialogFragment {

    public static SortFragment newInstance() {return new SortFragment();}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.sort_layout, container, false);
        initListener();
        return view;
    }

    private void initListener() {

    }
}
