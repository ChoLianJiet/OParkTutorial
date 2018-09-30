package com.opark.opark.model.merchant_offer;

import android.widget.ImageView;

import com.opark.opark.R;

import java.util.ArrayList;
import java.util.List;

public class MerchantOffer {
    private String merchantOfferTitle, merchantName, merchantAddress, merchantContact, offerCost;
    private int offerImage;
    public MerchantOffer() {
    }

    public MerchantOffer(String merchantOfferTitle, String merchantName, String merchantAddress, String merchantContact, String offerCost, int offerImage) {
        this.merchantOfferTitle = merchantOfferTitle;
        this.merchantName = merchantName;
        this.merchantAddress = merchantAddress;
        this.merchantContact = merchantContact;
        this.offerCost = offerCost;
        this.offerImage = offerImage;
    }

    public int getOfferImage() {
        return offerImage;
    }

    public void setOfferImage(int offerImage) {
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



    // This method creates an ArrayList that has three Person objects
// Checkout the project associated with this tutorial on Github if
// you want to use the same images.


}
