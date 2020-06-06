package com.example.restaurantfinder.view_holder;

import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.restaurantfinder.databinding.SearchItemBinding;
import com.example.restaurantfinder.model.SearchResponse;

public class SearchViewHolder extends RecyclerView.ViewHolder {

    private SearchItemBinding binding;
    public ImageView restImage;
    public TextView tvRating;

    public SearchViewHolder(SearchItemBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
        restImage = binding.ivImage;
        tvRating = binding.tvRating;
    }

    public void bindData(SearchResponse model) {
        binding.setSearch(model);
        binding.executePendingBindings();
    }
}
