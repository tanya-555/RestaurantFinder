package com.example.restaurantfinder.contract;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.model.CollectionResponse;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

public interface CollectionsContract {

    interface Model {
        void getCollections(RequestQueue queue, int cityId, onFinishedListener listener);

        interface onFinishedListener {
            void onFinished(List<CollectionResponse> collectionList);

            void onFailed(Throwable t);
        }
    }

    interface View extends MvpLceView<List<CollectionResponse>> {
    }

    abstract class Presenter extends MvpBasePresenter<View> {
        public abstract void fetchCollections(RequestQueue queue, int cityId);
    }
}
