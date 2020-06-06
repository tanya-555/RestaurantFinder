package com.example.restaurantfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.example.restaurantfinder.contract.ListingContract;
import com.example.restaurantfinder.model.SearchResponse;
import com.example.restaurantfinder.presenter.ListingPresenter;
import com.hannesdorfmann.mosby3.mvp.conductor.lce.MvpLceController;

import java.util.List;

public class ListingController extends MvpLceController<LinearLayout, List<SearchResponse>,
        ListingContract.View, ListingPresenter> implements ListingContract.View {

    public ListingController(Bundle bundle) {
        super(bundle);
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @NonNull
    @Override
    public ListingPresenter createPresenter() {
        return null;
    }

    @Override
    public void setData(List<SearchResponse> data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }
}
