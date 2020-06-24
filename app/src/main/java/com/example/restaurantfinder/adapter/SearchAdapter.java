package com.example.restaurantfinder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurantfinder.R;
import com.example.restaurantfinder.databinding.SearchItemBinding;
import com.example.restaurantfinder.model.SearchResponse;
import com.example.restaurantfinder.view_holder.SearchViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private static final String TAG = SearchAdapter.class.getName();

    private Context context;
    private SearchItemBinding binding;
    private List<SearchResponse> searchList;
    private PublishSubject<String> adapterSearchClickSubject;

    public SearchAdapter(Context context) {
        this.context = context;
        searchList = new ArrayList<>();
        adapterSearchClickSubject = PublishSubject.create();
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new SearchViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        SearchResponse response = searchList.get(position);
        holder.bindData(response);
        Glide.with(context).load(response.getImage())
                .apply(new RequestOptions().centerCrop()).into(holder.restImage);
        int color = Color.parseColor("#".concat(response.getRatingColor()));
        holder.tvRating.setBackgroundColor(color);
        RxView.clicks(holder.itemView).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    adapterSearchClickSubject.onNext(response.getUrl());
                }, e -> {
                    Log.d(TAG, e.getMessage());
                });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void setList(List<SearchResponse> searchList) {
        this.searchList = searchList;
    }

    public PublishSubject<String > getAdapterSearchClickSubject() {
        return adapterSearchClickSubject;
    }
}
