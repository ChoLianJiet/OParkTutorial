package com.opark.opark.rewards_redemption;

import com.opark.opark.model.Address;

public class RewardsPocketOffer {
    private String merchantOfferTitle, merchantName,  merchantContact, preRedemptionCode;
    private String  offerImage;
    private Address merchantAddress;
    public RewardsPocketOffer() {
    }


    public RewardsPocketOffer(String merchantOfferTitle, String merchantName, Address merchantAddress, String merchantContact, String preRedemptionCode, String offerImage) {
        this.merchantOfferTitle = merchantOfferTitle;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantContact = merchantContact;
        this.preRedemptionCode = preRedemptionCode;
        this.offerImage = offerImage;
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
}
