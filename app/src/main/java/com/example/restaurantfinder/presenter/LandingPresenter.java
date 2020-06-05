package com.example.restaurantfinder.presenter;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.contract.CollectionsContract;
import com.example.restaurantfinder.model.CollectionInfoModel;
import com.example.restaurantfinder.model.CollectionResponse;

import java.util.List;

public class LandingPresenter extends CollectionsContract.Presenter implements CollectionsContract.Model.onFinishedListener {


    private CollectionInfoModel model;
    private boolean pullToRefresh;

    public LandingPresenter() {
        model = new CollectionInfoModel();
    }

    @Override
    public void onFinished(List<CollectionResponse> collectionList) {
        if(isViewAttached()) {
            getView().setData(collectionList.subList(0,9));
            getView().showContent();
        }
    }

    @Override
    public void onFailed(Throwable t) {
        if(isViewAttached()) {
            getView().showError(t, pullToRefresh);
        }
    }

    @Override
    public void fetchCollections(RequestQueue queue, int cityId, boolean pullToRefresh) {
        this.pullToRefresh = pullToRefresh;
        getView().showLoading(pullToRefresh);
        callApi(queue, cityId);
    }

    private void callApi(RequestQueue queue, int cityId) {
        model.getCollections(queue, cityId, this);
    }
}
