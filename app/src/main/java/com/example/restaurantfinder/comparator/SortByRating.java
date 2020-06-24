package com.example.restaurantfinder.comparator;

import com.example.restaurantfinder.model.SearchResponse;

import java.util.Comparator;

public class SortByRating implements Comparator<SearchResponse> {

    @Override
    public int compare(SearchResponse o1, SearchResponse o2) {
        double val = Double.valueOf(o1.getRating()) - Double.valueOf(o2.getRating());
        int res;
        if (val > 0) {
            res = 1;
        } else if (val < 0) {
            res = -1;
        } else {
            res = 0;
        }
        return res;
    }
}
