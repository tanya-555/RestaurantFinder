package com.example.restaurantfinder.presenter;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.contract.SelectCityContract;
import com.example.restaurantfinder.model.CityInfoModel;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public class SelectCityPresenter extends SelectCityContract.Presenter implements
        MvpPresenter<SelectCityContract.View>, SelectCityContract.Model.onFinishedListener {

    private SelectCityContract.View attachedView;
    private CityInfoModel model;

    public SelectCityPresenter() {
        model = new CityInfoModel();
    }

    @Override
    public void fetchData(RequestQueue queue, String query) {
        model.getData(queue, query, this);
    }

    @Override
    public void attachView(SelectCityContract.View view) {
        attachedView = view;
    }

    @Override
    public void detachView(boolean retainInstance) {
        attachedView = null;
    }

    @Override
    public void detachView() {
        attachedView = null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void onFinished(int cityId, String cityName) {
        attachedView.onDataFetched(cityId, cityName);
    }

    @Override
    public void onFailure(Throwable t) {
        attachedView.onResponseError(t);
    }
}
