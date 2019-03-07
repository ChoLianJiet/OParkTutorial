package com.opark.opark.model;

public class UserSeasonalRanking {

    String userRank;
    int successfulShareCount,    totalSharePointsAccummulated            ,selfFFK    ,peterFFK            ,selfFindFFK;

    public UserSeasonalRanking() {
    }

    public UserSeasonalRanking(String userRank, int successfulShareCount, int totalSharePointsAccummulated, int selfFFK, int peterFFK, int selfFindFFK) {
        this.userRank = userRank;
        this.successfulShareCount = successfulShareCount;
        this.totalSharePointsAccummulated = totalSharePointsAccummulated;
        this.selfFFK = selfFFK;
        this.peterFFK = peterFFK;
        this.selfFindFFK = selfFindFFK;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public int getSuccessfulShareCount() {
        return successfulShareCount;
    }

    public void setSuccessfulShareCount(int successfulShareCount) {
        this.successfulShareCount = successfulShareCount;
    }

    public int getTotalSharePointsAccummulated() {
        return totalSharePointsAccummulated;
    }

    public void setTotalSharePointsAccummulated(int totalSharePointsAccummulated) {
        this.totalSharePointsAccummulated = totalSharePointsAccummulated;
    }

    public int getSelfFFK() {
        return selfFFK;
    }

    public void setSelfFFK(int selfFFK) {
        this.selfFFK = selfFFK;
    }

    public int getPeterFFK() {
        return peterFFK;
    }

    public void setPeterFFK(int peterFFK) {
        this.peterFFK = peterFFK;
    }

    public int getSelfFindFFK() {
        return selfFindFFK;
    }

    public void setSelfFindFFK(int selfFindFFK) {
        this.selfFindFFK = selfFindFFK;
    }
}
