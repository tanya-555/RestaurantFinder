package com.example.restaurantfinder.model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantfinder.contract.LandingContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CollectionInfoModel implements LandingContract.Model {

    private static final String TAG = CollectionInfoModel.class.getName();
    private static final String URL = "https://developers.zomato.com/api/v2.1/collections?city_id=";
    private List<CollectionResponse> collectionList = new ArrayList<>();

    @Override
    public void getCollections(RequestQueue queue, int cityId, onFinishedListener listener) {

        String url = URL.concat(String.valueOf(cityId));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getJSONArray("collections").length() != 0) {
                                getCollectionList(response.getJSONArray("collections"));
                                listener.onFinished(collectionList);
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

    private void getCollectionList(JSONArray array) throws JSONException {
        for(int i =0; i< array.length(); i++) {
            String title = array.getJSONObject(i).getJSONObject("collection").getString("title");
            String imageUrl = array.getJSONObject(i).getJSONObject("collection").getString("image_url");
            String description = array.getJSONObject(i).getJSONObject("collection").getString("description");
            int collectionId = array.getJSONObject(i).getJSONObject("collection").getInt("collection_id");
            CollectionResponse response = new CollectionResponse(title,imageUrl,description,collectionId);
            collectionList.add(response);
        }
    }
}
