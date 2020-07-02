package com.example.restaurantfinder.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantfinder.contract.ListingContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SearchInfoModel implements ListingContract.Model {

    private static final String TAG = SearchInfoModel.class.getName();
    private static final String URL = "https://developers.zomato.com/api/v2.1/search?entity_type=city&sort=rating";
    private List<SearchResponse> searchList = new ArrayList<>();

    @Override
    public void getData(RequestQueue queue, int cityId, int collectionId, String order, onFinishedListener listener) {
        String city_id = String.valueOf(cityId);
        String collection_id = String.valueOf(collectionId);
        String fields = String.format("&entity_id=%s&collection_id=%s&order=%s",city_id,collection_id,order);
        String url = URL.concat(fields);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getJSONArray("restaurants").length() > 0){
                                getSearchList(response.getJSONArray("restaurants"));
                                listener.onFinished(searchList);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, Objects.requireNonNull(error.getMessage()));
                listener.onFailed(error.getCause());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user-key", "5b668a442e14521f33b9ee9e0860a75b");
                params.put("Accept", "application/json");
                return params;
            }
        };
        queue.add(request);
    }

    private void getSearchList(JSONArray array) throws JSONException {
        for(int i =0; i< array.length(); i++) {
            JSONObject rest = array.getJSONObject(i).getJSONObject("restaurant");
            String name = rest.getString("name");
            String rating = rest.getJSONObject("user_rating").getString("aggregate_rating");
            String ratingColor = rest.getJSONObject("user_rating").getString("rating_color");
            String contact = rest.getString("phone_numbers");
            String locality = rest.getJSONObject("location").getString("locality_verbose");
            String image = rest.getString("featured_image");
            String cuisines = rest.getString("cuisines");
            String url = rest.getString("url");
            String latitude = rest.getJSONObject("location").getString("latitude");
            String longitude = rest.getJSONObject("location").getString("longitude");
            searchList.add(new SearchResponse(name, rating, ratingColor, contact,
                                    locality, image, cuisines, url, latitude, longitude));
        }
    }
}
