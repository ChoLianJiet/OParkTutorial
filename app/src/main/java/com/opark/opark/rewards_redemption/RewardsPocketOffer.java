package com.opark.opark.rewards_redemption;

public class RewardsPocketOffer {
    private String merchantOfferTitle, merchantName, merchantAddress, merchantContact, preRedemptionCode;
    private String  offerImage;
    public RewardsPocketOffer() {
    }


    public RewardsPocketOffer(String merchantOfferTitle, String merchantName, String merchantAddress, String merchantContact, String preRedemptionCode, String offerImage) {
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

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
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
