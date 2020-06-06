package com.example.restaurantfinder.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurantfinder.databinding.CollectionItemBinding;
import com.example.restaurantfinder.model.CollectionResponse;
import com.example.restaurantfinder.view_holder.CollectionsViewHolder;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsViewHolder> {

    private static final String TAG = CollectionsAdapter.class.getName();

    private List<CollectionResponse> collectionList = new ArrayList<>();
    private Context context;
    private CollectionItemBinding binding;
    private PublishSubject<Integer> adapterCollectionClickSubject;

    public CollectionsAdapter(Context context) {
        this.context = context;
        adapterCollectionClickSubject = PublishSubject.create();
    }

    @NonNull
    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = CollectionItemBinding.inflate(LayoutInflater.from(context),parent,false);
        return new CollectionsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final CollectionsViewHolder holder, int position) {
        CollectionResponse model = collectionList.get(position);
        holder.bindData(model);
        Glide.with(context).load(model.getImageUrl())
                .apply(new RequestOptions().centerCrop()).into(holder.collectionImage);
        RxView.clicks(holder.itemView).observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    adapterCollectionClickSubject.onNext(model.getCollectionId());
                }, e -> {
                    Log.d(TAG, e.getMessage());
                });
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public void setData(List<CollectionResponse> collectionList) {
        this.collectionList = collectionList;
    }

    public PublishSubject<Integer> getAdapterCollectionClickSubject() {
        return adapterCollectionClickSubject;
    }
}
