package com.example.restaurantfinder.view_holder;

import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantfinder.databinding.CollectionItemBinding;
import com.example.restaurantfinder.model.CollectionResponse;

public class CollectionsViewHolder extends RecyclerView.ViewHolder {

    private CollectionItemBinding binding;
    public ImageView collectionImage;

    public CollectionsViewHolder(@NonNull CollectionItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        collectionImage = binding.ivImage;
    }

    public void bindData(CollectionResponse model) {
        binding.setCollection(model);
        binding.executePendingBindings();
    }
}
