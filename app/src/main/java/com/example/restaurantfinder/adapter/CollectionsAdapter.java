package com.example.restaurantfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurantfinder.R;
import com.example.restaurantfinder.databinding.CollectionItemBinding;
import com.example.restaurantfinder.model.CollectionResponse;
import com.example.restaurantfinder.view_holder.CollectionsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsViewHolder> {

    private List<CollectionResponse> collectionList = new ArrayList<>();
    private Context context;
    private CollectionItemBinding binding;

    public CollectionsAdapter(Context context) {
        this.context = context;
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
    }

    @Override
    public int getItemCount() {
        return collectionList.size();
    }

    public void setData(List<CollectionResponse> collectionList) {
        this.collectionList = collectionList;
    }
}
