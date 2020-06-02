package com.example.restaurantfinder.model;

import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.restaurantfinder.contract.SelectCityContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CityInfoModel implements SelectCityContract.Model {

    private static final String TAG = CityInfoModel.class.getName();
    private static final String URL ="https://developers.zomato.com/api/v2.1/cities?q=";

    @Override
    public void getData(RequestQueue queue, String query, onFinishedListener listener) {
        String url = URL.concat(query);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int city_id;
                        try {
                            if(response.getJSONArray("location_suggestions").length() == 0) {
                                city_id = 0;
                            } else {
                                city_id = response.getJSONArray("location_suggestions").
                                        getJSONObject(0).getInt("id");
                            }
                            listener.onFinished(city_id);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, Objects.requireNonNull(error.getMessage()));
                listener.onFailure(error.getCause());
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
}
