package com.opark.opark.model;

public class RewardsRecord {
    String rewardsTitle;
    String rewardsMerchant;
    String rewardsTimeOfRedemption;
    int rewardsOfferCost;
    int rewardsPointBeforeRedemption;
    int rewardsPointAfterRedemption;

    public RewardsRecord(String rewardsTitle, String rewardsMerchant, String rewardsTimeOfRedemption, int rewardsOfferCost, int rewardsPointBeforeRedemption, int rewardsPointAfterRedemption) {
        this.rewardsTitle = rewardsTitle;
        this.rewardsMerchant = rewardsMerchant;
        this.rewardsTimeOfRedemption = rewardsTimeOfRedemption;
        this.rewardsOfferCost = rewardsOfferCost;
        this.rewardsPointBeforeRedemption = rewardsPointBeforeRedemption;
        this.rewardsPointAfterRedemption = rewardsPointAfterRedemption;
    }

    public String getRewardsTitle() {
        return rewardsTitle;
    }

    public void setRewardsTitle(String rewardsTitle) {
        this.rewardsTitle = rewardsTitle;
    }

    public String getRewardsMerchant() {
        return rewardsMerchant;
    }

    public void setRewardsMerchant(String rewardsMerchant) {
        this.rewardsMerchant = rewardsMerchant;
    }

    public String getRewardsTimeOfRedemption() {
        return rewardsTimeOfRedemption;
    }

    public void setRewardsTimeOfRedemption(String rewardsTimeOfRedemption) {
        this.rewardsTimeOfRedemption = rewardsTimeOfRedemption;
    }

    public int getRewardsOfferCost() {
        return rewardsOfferCost;
    }

    public void setRewardsOfferCost(int rewardsOfferCost) {
        this.rewardsOfferCost = rewardsOfferCost;
    }

    public int getRewardsPointBeforeRedemption() {
        return rewardsPointBeforeRedemption;
    }

    public void setRewardsPointBeforeRedemption(int rewardsPointBeforeRedemption) {
        this.rewardsPointBeforeRedemption = rewardsPointBeforeRedemption;
    }

    public int getRewardsPointAfterRedemption() {
        return rewardsPointAfterRedemption;
    }

    public void setRewardsPointAfterRedemption(int rewardsPointAfterRedemption) {
        this.rewardsPointAfterRedemption = rewardsPointAfterRedemption;
    }
}
