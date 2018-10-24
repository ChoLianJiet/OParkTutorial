package com.opark.opark.model;

import com.google.android.gms.maps.model.LatLng;

public class MatchmakingRecord {
    public String sessionKey;
    public String matchmakingRecordTimestamp;
    public String kenaUid;
    public String peterUid;
    public LatLng peterLatLng;
    public LatLng kenaLatLng;
    public double timeElapsed;


    public MatchmakingRecord(String sessionKey, String matchmakingRecordTimestamp, String kenaUid, String peterUid, LatLng peterLatLng, LatLng kenaLatLng, double timeElapsed ) {
        this.sessionKey = sessionKey;
        this.matchmakingRecordTimestamp = matchmakingRecordTimestamp;
        this.kenaUid = kenaUid;
        this.peterUid= peterUid;
        this.peterLatLng = peterLatLng;
        this.kenaLatLng = kenaLatLng;
        this.timeElapsed = timeElapsed;

    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getMatchmakingRecordTimestamp() {
        return matchmakingRecordTimestamp;
    }

    public void setMatchmakingRecordTimestamp(String matchmakingRecordTimestamp) {
        this.matchmakingRecordTimestamp = matchmakingRecordTimestamp;
    }

    public String getKenaUid() {
        return kenaUid;
    }

    public void setKenaUid(String kenaUid) {
        this.kenaUid = kenaUid;
    }

    public String getPeterUid() {
        return peterUid;
    }

    public void setPeterUid(String peterUid) {
        this.peterUid = peterUid;
    }

    public LatLng getPeterLatLng() {
        return peterLatLng;
    }

    public void setPeterLatLng(LatLng peterLatLng) {
        this.peterLatLng = peterLatLng;
    }

    public LatLng getKenaLatLng() {
        return kenaLatLng;
    }

    public void setKenaLatLng(LatLng kenaLatLng) {
        this.kenaLatLng = kenaLatLng;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(double timeElapsed) {
        this.timeElapsed = timeElapsed;
    }



}