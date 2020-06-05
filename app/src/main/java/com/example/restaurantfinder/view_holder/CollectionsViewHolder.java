package com.example.restaurantfinder.view_holder;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantfinder.databinding.CollectionItemBinding;
import com.example.restaurantfinder.model.CollectionResponse;

public class CollectionsViewHolder extends RecyclerView.ViewHolder {

    private CollectionItemBinding binding;

    public CollectionsViewHolder(@NonNull CollectionItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bindData(CollectionResponse model) {
        binding.setCollection(model);
        binding.executePendingBindings();
    }
}
