package com.example.restaurantfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.restaurantfinder.R;
import com.example.restaurantfinder.adapter.CollectionsAdapter;
import com.example.restaurantfinder.contract.CollectionsContract;
import com.example.restaurantfinder.databinding.LandingControllerBinding;
import com.example.restaurantfinder.model.CollectionResponse;
import com.example.restaurantfinder.presenter.LandingPresenter;
import com.hannesdorfmann.mosby3.mvp.conductor.lce.MvpLceController;

import java.util.List;
import java.util.Objects;

public class LandingController extends MvpLceController<SwipeRefreshLayout, List<CollectionResponse>,
                                           CollectionsContract.View, LandingPresenter> implements  CollectionsContract.View, SwipeRefreshLayout.OnRefreshListener{

    private Bundle bundle;
    private LandingControllerBinding binding;
    private int cityId;
    private RequestQueue queue;
    private CollectionsAdapter adapter;
    private RecyclerView recyclerView;
    private List<CollectionResponse> collectionList;

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
        queue = Volley.newRequestQueue(Objects.requireNonNull(getApplicationContext()));
        return binding.getRoot();
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        binding.contentView.setOnRefreshListener(this);
        initGridView();
        loadData(false);
    }

    @Override
    protected void onDetach(@NonNull View view) {
        super.onDetach(view);
        collectionList.clear();
    }

    @NonNull
    @Override
    public LandingPresenter createPresenter() {
        return new LandingPresenter();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.fetchCollections(queue, cityId, pullToRefresh);
    }

    @Override
    public void setData(List<CollectionResponse> data) {
        collectionList = data;
        String val = String.valueOf(data.size());
        Toast.makeText(getActivity(), val, Toast.LENGTH_LONG).show();
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

    private void initGridView() {
        adapter = new CollectionsAdapter(getActivity());
        recyclerView = binding.contentView.findViewById(R.id.rv_collections);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }
}
