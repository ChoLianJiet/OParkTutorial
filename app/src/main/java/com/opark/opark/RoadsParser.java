package com.opark.opark;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by NgocTri on 12/11/2017.
 */

public class RoadsParser{
    final static String TAG = "RoadsParser";
    /**
     * Returns a list of lists containing latitude and longitude from a JSONObject
     */
    private LatLng mLatLng;

    public static RoadsParser fromJson(JSONObject jObject) {

        try {
            RoadsParser routes = new RoadsParser();
            routes.mLatLng = new LatLng(jObject.getJSONArray("snappedPoints").getJSONObject(0).getJSONObject("location").getDouble("latitude"),jObject.getJSONArray("snappedPoints").getJSONObject(0).getJSONObject("location").getDouble("longitude"));

            Log.d(TAG, "fromJson: routes in RoadsParser is " + routes);

            return routes;
        } catch (JSONException e) {
            return null;
        }
    }

    public LatLng getLatLng() {
        return mLatLng;
    }
}