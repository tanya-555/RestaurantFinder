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
        }
    }

    @Override
    public void onFailed(Throwable t) {
        if(isViewAttached()) {
        }
    }

    @Override
    public void fetchCollections(RequestQueue queue, int cityId, boolean pullToRefresh) {
        model.getCollections(queue, cityId, this);
    }
}
