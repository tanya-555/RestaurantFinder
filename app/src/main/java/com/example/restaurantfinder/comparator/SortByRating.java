package com.example.restaurantfinder.comparator;

import com.example.restaurantfinder.model.SearchResponse;

import java.util.Comparator;

public class SortByRating implements Comparator<SearchResponse> {

    @Override
    public int compare(SearchResponse o1, SearchResponse o2) {
        return Integer.valueOf(o1.getRating())-Integer.valueOf(o2.getRating());
    }
}
