package com.example.restaurantfinder.contract;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.model.SearchResponse;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

public interface ListingContract {

    interface Model {
        void getData(RequestQueue queue, int cityId, int collectionId, String order, onFinishedListener listener);

        interface onFinishedListener {
            void onFinished(List<SearchResponse> collectionList);

            void onFailed(Throwable t);
        }
    }

    interface View extends MvpLceView<List<SearchResponse>> {
    }

    abstract class Presenter extends MvpBasePresenter<View> {
        public abstract void fetchCollections(RequestQueue queue, int cityId, int collectionId, String order);
    }
}
