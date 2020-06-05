package com.example.restaurantfinder.presenter;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.contract.CollectionsContract;
import com.example.restaurantfinder.model.CollectionInfoModel;
import com.example.restaurantfinder.model.CollectionResponse;

import java.util.List;

public class LandingPresenter extends CollectionsContract.Presenter implements CollectionsContract.Model.onFinishedListener {


    private CollectionInfoModel model;
    private CollectionsContract.View attachedView;

    public LandingPresenter() {
        model = new CollectionInfoModel();
    }

    @Override
    public void onFinished(List<CollectionResponse> collectionList) {
        if(attachedView != null) {
            attachedView.setData(collectionList);
            attachedView.showContent();
        }
    }

    @Override
    public void attachView(CollectionsContract.View view) {
        super.attachView(view);
        attachedView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        attachedView = null;
    }

    @Override
    public void onFailed(Throwable t) {
        if(attachedView != null) {
            attachedView.showError(t, false);
        }
    }

    @Override
    public void fetchCollections(RequestQueue queue, int cityId) {
        attachedView.showLoading(false);
        callApi(queue, cityId);
    }

    private void callApi(RequestQueue queue, int cityId) {
        model.getCollections(queue, cityId, this);
    }
}
