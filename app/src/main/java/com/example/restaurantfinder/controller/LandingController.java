package com.example.restaurantfinder.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.restaurantfinder.contract.CollectionsContract;
import com.example.restaurantfinder.model.CollectionResponse;
import com.example.restaurantfinder.presenter.LandingPresenter;
import com.hannesdorfmann.mosby3.mvp.conductor.lce.MvpLceController;

public class LandingController extends MvpLceController<SwipeRefreshLayout, CollectionResponse,
                                           CollectionsContract.View, LandingPresenter> implements  SwipeRefreshLayout.OnRefreshListener{
    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return null;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        return null;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
    }

    @NonNull
    @Override
    public LandingPresenter createPresenter() {
        return null;
    }

    @Override
    public void setData(CollectionResponse data) {

    }

    @Override
    public void loadData(boolean pullToRefresh) {

    }

    @Override
    public void onRefresh() {

    }
}
