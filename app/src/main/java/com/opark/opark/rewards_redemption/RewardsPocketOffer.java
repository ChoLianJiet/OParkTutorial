package com.opark.opark.rewards_redemption;

import com.opark.opark.model.Address;

public class RewardsPocketOffer {
    private String redeemStatus,redeemDate,pushKey,expiryDate;
    private String merchantOfferTitle, merchantName,  merchantContact, preRedemptionCode;
    private String  offerImage;
    private Address merchantAddress;
    public RewardsPocketOffer() {
    }


    public RewardsPocketOffer(String redeemStatus, String redeemDate, String pushKey, String expiryDate, String merchantOfferTitle, String merchantName, String merchantContact, String preRedemptionCode, String offerImage, Address merchantAddress) {
        this.redeemStatus = redeemStatus;
        this.redeemDate = redeemDate;
        this.pushKey = pushKey;
        this.expiryDate = expiryDate;
        this.merchantOfferTitle = merchantOfferTitle;
        this.merchantName = merchantName;
        this.merchantContact = merchantContact;
        this.preRedemptionCode = preRedemptionCode;
        this.offerImage = offerImage;
        this.merchantAddress = merchantAddress;
    }

    public RewardsPocketOffer(String redeemStatus, String redeemDate, String pushKey, String merchantOfferTitle, String merchantName, String merchantContact, String preRedemptionCode, String offerImage, Address merchantAddress) {
        this.redeemStatus = redeemStatus;
        this.redeemDate = redeemDate;
        this.pushKey = pushKey;
        this.merchantOfferTitle = merchantOfferTitle;
        this.merchantName = merchantName;
        this.merchantContact = merchantContact;
        this.preRedemptionCode = preRedemptionCode;
        this.offerImage = offerImage;
        this.merchantAddress = merchantAddress;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getMerchantOfferTitle() {
        return merchantOfferTitle;
    }

    public void setMerchantOfferTitle(String merchantOfferTitle) {
        this.merchantOfferTitle = merchantOfferTitle;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Address getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(Address merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantContact() {
        return merchantContact;
    }

    public void setMerchantContact(String merchantContact) {
        this.merchantContact = merchantContact;
    }

    public String getPreRedemptionCode() {
        return preRedemptionCode;
    }

    public void setPreRedemptionCode(String preRedemptionCode) {
        this.preRedemptionCode = preRedemptionCode;
    }

    public String getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(String offerImage) {
        this.offerImage = offerImage;
    }


    public String getRedeemStatus() {
        return redeemStatus;
    }

    public void setRedeemStatus(String redeemStatus) {
        this.redeemStatus = redeemStatus;
    }

    public String getRedeemDate() {
        return redeemDate;
    }

    public void setRedeemDate(String redeemDate) {
        this.redeemDate = redeemDate;
    }

    public String getPushKey() {
        return pushKey;
    }

    public void setPushKey(String pushKey) {
        this.pushKey = pushKey;
    }
}
