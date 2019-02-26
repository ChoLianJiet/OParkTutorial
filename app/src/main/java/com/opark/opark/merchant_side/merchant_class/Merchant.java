package com.opark.opark.merchant_side.merchant_class;

import com.opark.opark.model.Address;

public class Merchant {
   //TODO add merchant objects
   public String merchSignUpPerson, merchCoRegNumber, merchCoName, merchContact, merchEmail, merchCoAddress;
   public Address merchAddress = new Address();



    public Merchant() {
    }


    public Merchant(String merchSignUpPerson, String merchCoRegNumber, String merchCoName, String merchContact, String merchEmail, Address merchAddress) {
        this.merchSignUpPerson = merchSignUpPerson;
        this.merchCoRegNumber = merchCoRegNumber;
        this.merchCoName = merchCoName;
        this.merchContact = merchContact;
        this.merchEmail = merchEmail;
        this.merchAddress = merchAddress;
    }

    public Merchant(String merchSignUpName, String merchCoRegNumber, String merchCoName, String merchContact, String merchEmail, String merchCoAddress) {
        this.merchSignUpPerson = merchSignUpName;
        this.merchCoRegNumber = merchCoRegNumber;
        this.merchCoName = merchCoName;
        this.merchContact = merchContact;
        this.merchEmail = merchEmail;
        this.merchCoAddress = merchCoAddress;
    }

    public Merchant(String merchCoName , String merchContact,  String merchEmail, Address merchAddress){
        this.merchCoName = merchCoName;
        this.merchContact = merchContact;
        this.merchEmail = merchEmail;
        this.merchAddress = merchAddress;
    }

    public String getMerchSignUpPerson() {
        return merchSignUpPerson;
    }

    public void setMerchSignUpPerson(String merchSignUpPerson) {
        this.merchSignUpPerson = merchSignUpPerson;
    }

    public String getMerchCoNumber() {
        return merchCoRegNumber;
    }

    public void setMerchCoNumber(String merchCoNumber) {
        this.merchCoRegNumber = merchCoNumber;
    }


    public String getMerchContact() {
        return merchContact;
    }

    public void setMerchContact(String merchContact) {
        this.merchContact = merchContact;
    }

    public String getMerchEmail() {
        return merchEmail;
    }

    public void setMerchEmail(String merchEmail) {
        this.merchEmail = merchEmail;
    }


    public String getMerchCoRegNumber() {
        return merchCoRegNumber;
    }

    public void setMerchCoRegNumber(String merchCoRegNumber) {
        this.merchCoRegNumber = merchCoRegNumber;
    }

    public String getMerchCoName() {
        return merchCoName;
    }

    public void setMerchCoName(String merchCoName) {
        this.merchCoName = merchCoName;
    }

    public String getMerchCoAddress() {
        return merchCoAddress;
    }

    public void setMerchCoAddress(String merchCoAddress) {
        this.merchCoAddress = merchCoAddress;
    }

    public Address getMerchAddress() {
        return merchAddress;
    }

    public void setMerchAddress(Address merchAddress) {
        this.merchAddress = merchAddress;
    }


}
