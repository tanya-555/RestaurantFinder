package com.example.restaurantfinder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.restaurantfinder.databinding.SearchItemBinding;
import com.example.restaurantfinder.model.SearchResponse;
import com.example.restaurantfinder.view_holder.SearchViewHolder;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

    private Context context;
    private SearchItemBinding binding;
    private List<SearchResponse> searchList;

    public SearchAdapter(Context context) {
        this.context = context;
        searchList = new ArrayList<>();
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
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public void setList(List<SearchResponse> searchList) {
        this.searchList = searchList;
    }
}
