package com.opark.opark.share_parking;

import com.google.android.gms.maps.model.Marker;

public class KeyAndMarkerPair {

    private String key;
    private Marker marker;

    public KeyAndMarkerPair(String key, Marker marker){
        this.key = key;
        this.marker = marker;
    }

    public String getKey(){
        return this.key;
    }

    public Marker getMarker() {
        return this.marker;
    }

}
