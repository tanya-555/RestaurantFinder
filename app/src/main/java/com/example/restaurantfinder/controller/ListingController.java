package com.example.restaurantfinder.controller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.restaurantfinder.R;
import com.example.restaurantfinder.adapter.SearchAdapter;
import com.example.restaurantfinder.contract.ListingContract;
import com.example.restaurantfinder.databinding.ListingControllerBinding;
import com.example.restaurantfinder.fragment.SortFragment;
import com.example.restaurantfinder.model.SearchResponse;
import com.example.restaurantfinder.presenter.ListingPresenter;
import com.hannesdorfmann.mosby3.mvp.conductor.lce.MvpLceController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListingController extends MvpLceController<LinearLayout, List<SearchResponse>,
        ListingContract.View, ListingPresenter> implements ListingContract.View {

    private static final String TAG = ListingController.class.getName();

    private Bundle bundle;
    private int cityId;
    private RequestQueue queue;
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private List<SearchResponse> searchResponses;
    private int collectionId;
    private ListingControllerBinding binding;
    private String order = "asc";

    public ListingController(Bundle bundle) {
        super(bundle);
        this.bundle = bundle;
    }

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        binding = DataBindingUtil.inflate(inflater, R.layout.listing_controller, null, false);
        cityId = bundle.getInt("city_id");
        collectionId = bundle.getInt("collection_id");
        searchResponses = new ArrayList<>();
        queue = Volley.newRequestQueue(Objects.requireNonNull(getActivity()));
        return binding.getRoot();
    }

    @Override
    protected String getErrorMessage(Throwable e, boolean pullToRefresh) {
        return e.getMessage();
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
    public ListingPresenter createPresenter() {
        return new ListingPresenter();
    }

    @Override
    public void setData(List<SearchResponse> data) {
        searchResponses.clear();
        searchResponses.addAll(data);
        adapter.setList(searchResponses);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void loadData(boolean pullToRefresh) {
        presenter.fetchData(queue, cityId, collectionId, order);
    }

    private void initRecyclerView() {
        adapter = new SearchAdapter(getActivity());
        recyclerView = binding.contentView.findViewById(R.id.rv_search_results);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
    }

    public void showSortDialog(FragmentManager fragmentManager) {
        SortFragment sortFragment = SortFragment.newInstance();
        sortFragment.show(fragmentManager, TAG);
    }
}
