package com.opark.opark.model;

public class UserSeasonalRanking {

    String userRank;
    int successfulShareCount,    totalSharePointsAccummulated            ,selfShareFFKCount    ,youGotFFKedCount            ,selfFindFFKCount;

    public UserSeasonalRanking() {
    }

    public UserSeasonalRanking(String userRank, int successfulShareCount, int totalSharePointsAccummulated, int selfShareFFKCount, int youGotFFKedCount, int selfFindFFKCount) {
        this.userRank = userRank;
        this.totalSharePointsAccummulated = totalSharePointsAccummulated;
        this.successfulShareCount = successfulShareCount;
        
        this.selfShareFFKCount = selfShareFFKCount;
        this.youGotFFKedCount = youGotFFKedCount;
        this.selfFindFFKCount = selfFindFFKCount;
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
        return selfShareFFKCount;
    }

    public void setSelfFFK(int selfShareFFKCount) {
        this.selfShareFFKCount = selfShareFFKCount;
    }

    public int getPeterFFK() {
        return youGotFFKedCount;
    }

    public void setPeterFFK(int youGotFFKedCount) {
        this.youGotFFKedCount = youGotFFKedCount;
    }

    public int getSelfFindFFK() {
        return selfFindFFKCount;
    }

    public void setSelfFindFFK(int selfFindFFKCount) {
        this.selfFindFFKCount = selfFindFFKCount;
    }
}
