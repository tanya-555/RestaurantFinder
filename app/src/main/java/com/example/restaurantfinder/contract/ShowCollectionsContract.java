package com.example.restaurantfinder.contract;

import com.android.volley.RequestQueue;
import com.example.restaurantfinder.model.CollectionModel;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby3.mvp.lce.MvpLceView;

import java.util.List;

public interface ShowCollectionsContract {

    interface Model {
        void getCollections(RequestQueue queue, String query, onFinishedListener listener);

        interface onFinishedListener {
            void onFinished(List<CollectionModel> collectionList);

            void onFailed(Throwable t);
        }
    }

    interface View extends MvpLceView<CollectionModel> {
        void onCollectionsFetched(List<CollectionModel> collectionList);

        void onResponseError(Throwable t);
    }

    public abstract class Presenter extends MvpBasePresenter<View> {
        public abstract void fetchCollections(RequestQueue queue, String query);
    }
}
