package com.opark.opark.merchant_side.merchant_offer;

public class MerchantOffer {
  private String merchantOfferTitle, merchantName, merchantAddress, merchantContact, offerCost, merchantEmail;
  private String  offerImage, expiryDate, offerCategories;
  public MerchantOffer() {
  }

  public MerchantOffer(String merchantOfferTitle, String merchantName, String merchantAddress, String merchantContact, String offerCost, String merchantEmail, String offerImage, String expiryDate, String offerCategories) {
    this.merchantOfferTitle = merchantOfferTitle;
    this.merchantName = merchantName;
    this.merchantAddress = merchantAddress;
    this.merchantContact = merchantContact;
    this.offerCost = offerCost;
    this.merchantEmail = merchantEmail;
    this.offerImage = offerImage;
    this.expiryDate = expiryDate;
    this.offerCategories = offerCategories;
  }

  public MerchantOffer(String merchantOfferTitle, String merchantName, String merchantAddress, String merchantContact, String offerCost, String offerImage, String expiryDate, String offerCategories) {
        this.merchantOfferTitle = merchantOfferTitle;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantContact = merchantContact;
        this.offerCost = offerCost;
        this.offerImage = offerImage;
        this.expiryDate = expiryDate;
        this.offerCategories = offerCategories;
    }

    public MerchantOffer(String merchantOfferTitle, String merchantName, String merchantAddress, String merchantContact, String offerCost ) {
    this.merchantOfferTitle = merchantOfferTitle;
    this.merchantName = merchantName;
    this.merchantAddress = merchantAddress;
    this.merchantContact = merchantContact;
    this.offerCost = offerCost;
  }

  public String  getOfferImage() {
    return offerImage;
  }

  public void setOfferImage(String  offerImage) {
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

  public String getOfferCost() {
    return offerCost;
  }

  public void setOfferCost(String offerCost) {
    this.offerCost = offerCost;
  }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getOfferCategories() {
        return offerCategories;
    }

    public void setOfferCategories(String offerCategories) {
        this.offerCategories = offerCategories;
    }

  public String getMerchantEmail() {
    return merchantEmail;
  }

  public void setMerchantEmail(String merchantEmail) {
    this.merchantEmail = merchantEmail;
  }
// This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.


}