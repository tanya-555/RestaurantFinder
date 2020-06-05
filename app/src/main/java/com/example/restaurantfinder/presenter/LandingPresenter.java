package com.example.restaurantfinder.presenter;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.contract.CollectionsContract;
import com.example.restaurantfinder.model.CollectionInfoModel;
import com.example.restaurantfinder.model.CollectionResponse;

import java.util.List;

public class LandingPresenter extends CollectionsContract.Presenter implements CollectionsContract.Model.onFinishedListener {


    private CollectionInfoModel model;

    public LandingPresenter() {
        model = new CollectionInfoModel();
    }

    @Override
    public void onFinished(List<CollectionResponse> collectionList) {
        if(isViewAttached()) {
            getView().setData(collectionList);
            getView().showContent();
        }
    }

    @Override
    public void onFailed(Throwable t) {
        if(isViewAttached()) {
            getView().showError(t, false);
        }
    }

    @Override
    public void fetchCollections(RequestQueue queue, int cityId) {
        getView().showLoading(false);
        callApi(queue, cityId);
    }

    private void callApi(RequestQueue queue, int cityId) {
        model.getCollections(queue, cityId, this);
    }
}
