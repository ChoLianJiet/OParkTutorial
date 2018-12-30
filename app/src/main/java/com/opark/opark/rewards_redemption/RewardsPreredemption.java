package com.opark.opark.rewards_redemption;

public class RewardsPreredemption {
    String redeemedUserId, pushKey;

    public RewardsPreredemption(String redeemedUserId, String pushKey) {
        this.redeemedUserId = redeemedUserId;
        this.pushKey = pushKey;
    }

    public String getRedeemedUserId() {
        return redeemedUserId;
    }

    public void setRedeemedUserId(String redeemedUserId) {
        this.redeemedUserId = redeemedUserId;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }
}
