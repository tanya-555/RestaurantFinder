package com.example.restaurantfinder.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.restaurantfinder.ListingActivity;
import com.example.restaurantfinder.R;
import com.example.restaurantfinder.adapter.CollectionsAdapter;
import com.example.restaurantfinder.contract.LandingContract;
import com.example.restaurantfinder.databinding.LandingControllerBinding;
import com.example.restaurantfinder.model.CollectionResponse;
import com.example.restaurantfinder.presenter.LandingPresenter;
import com.hannesdorfmann.mosby3.mvp.conductor.lce.MvpLceController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class LandingController extends MvpLceController<LinearLayout, List<CollectionResponse>,
        LandingContract.View, LandingPresenter> implements LandingContract.View {

    private static final String TAG = LandingController.class.getName();

    private Bundle bundle;
    private LandingControllerBinding binding;
    private int cityId;
    private RequestQueue queue;
    private CollectionsAdapter adapter;
    private RecyclerView recyclerView;
    private List<CollectionResponse> collectionList;
    private CompositeDisposable disposable;
    private int collectionId;

    public LandingController(Bundle bundle) {
        super(bundle);
        this.bundle = bundle;
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        binding = LandingControllerBinding.inflate(inflater, container, false);
        cityId = bundle.getInt("city_id");
        collectionList = new ArrayList<>();
        disposable = new CompositeDisposable();
        queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        return binding.getRoot();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        initRecyclerView();
        loadData(false);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
    }

    @NonNull
    @Override
    public LandingPresenter createPresenter() {
        return new LandingPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.fetchCollections(queue, cityId);
    }

    @Override
    public void setData(List<CollectionResponse> data) {
        collectionList.clear();
        collectionList.addAll(data);
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected int getContentViewId() {
        return binding.contentView.getId();
    }

    @Override
    protected int getErrorViewId() {
        return binding.errorView.getId();
    }

    @Override
    protected int getLoadingViewId() {
        return binding.loadingView.getId();
    }

    @Override
    public void showLoading(boolean pullToRefresh) {
        super.showLoading(pullToRefresh);
    }

    @Override
    public void showContent() {
        super.showContent();
    }

    private void initRecyclerView() {
        adapter = new CollectionsAdapter(getActivity());
        recyclerView = binding.contentView.findViewById(R.id.rv_collections);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        subscribeToCollectionItemClicked(adapter.getAdapterCollectionClickSubject());
    }

    private void subscribeToCollectionItemClicked(PublishSubject<Integer> collectionItemClickSubject) {
        disposable.add(collectionItemClickSubject.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(id -> {
                    Toast.makeText(getActivity(), String.valueOf(id), Toast.LENGTH_LONG).show();
                    collectionId = id;
                    launchListingActivity();
                }, e -> {
                    Log.d(TAG, Objects.requireNonNull(e.getMessage()));
                }));
    }

    private void launchListingActivity() {
        Intent intent = new Intent(getActivity(), ListingActivity.class);
        intent.putExtra("city_id", cityId);
        intent.putExtra("collection_id", collectionId);
        startActivity(intent);
    }
}
