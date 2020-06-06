package com.example.restaurantfinder.presenter;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.contract.ListingContract;
import com.example.restaurantfinder.model.SearchInfoModel;
import com.example.restaurantfinder.model.SearchResponse;

import java.util.List;

public class ListingPresenter extends ListingContract.Presenter implements ListingContract.Model.onFinishedListener {

    private ListingContract.View attachedView;
    private SearchInfoModel model;

    public ListingPresenter() {
        model = new SearchInfoModel();
    }

    @Override
    public void onFinished(List<SearchResponse> searchResponseList) {
        if(attachedView != null) {
            attachedView.setData(searchResponseList);
            attachedView.showContent();
        }
    }

    @Override
    public void onFailed(Throwable t) {
        if(attachedView != null) {
            attachedView.showError(t, false);
        }
    }

    @Override
    public void fetchData(RequestQueue queue, int cityId, int collectionId, String order) {
        attachedView.showLoading(false);
        callApi(queue, cityId, collectionId, order);
    }

    @Override
    public void attachView(ListingContract.View view) {
        super.attachView(view);
        attachedView = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        attachedView = null;
    }

    private void callApi(RequestQueue queue, int cityId, int collectionId, String order) {
        model.getData(queue, cityId, collectionId, order, this);
    }
}
