package com.example.restaurantfinder.contract;

import com.android.volley.RequestQueue;
import com.hannesdorfmann.mosby3.mvp.MvpPresenter;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface SelectCityContract {

    interface Model {
        void getData(RequestQueue queue, String query, onFinishedListener listener);

        interface onFinishedListener {

            void onFinished(int cityId);

            void onFailure(Throwable t);
        }
    }

    interface View extends MvpView {
        void onDataFetched(int cityId);

        void onResponseError(Throwable t);
    }

    abstract class Presenter implements MvpPresenter<View> {

        public abstract void fetchData(RequestQueue queue, String query);
    }
}
