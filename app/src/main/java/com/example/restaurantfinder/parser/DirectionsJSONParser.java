package com.example.restaurantfinder.parser;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DirectionsJSONParser {

    private static final String ROUTES = "routes";
    private static final String LEGS = "legs";
    private static final String STEPS = "steps";
    private static final String POLYLINE = "polyline";
    private static final String LATITUDE_KEY = "lat";
    private static final String LONGITUDE_KEY = "lng";
    private static final String POINTS = "points";

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            jRoutes = jObject.getJSONArray(ROUTES);

            // traversing all the routes
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray(LEGS);
                List path = new ArrayList<HashMap<String, String>>();

                // traversing all the legs
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray(STEPS);

                    //traversing all the steps
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline;
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get(POLYLINE)).get(POINTS);
                        List<LatLng> list = PolyUtil.decode(polyline);

                        //traversing all the points
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<>();
                            hm.put(LATITUDE_KEY, Double.toString(list.get(l).latitude));
                            hm.put(LONGITUDE_KEY, Double.toString(list.get(l).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }
        return routes;
    }
}
