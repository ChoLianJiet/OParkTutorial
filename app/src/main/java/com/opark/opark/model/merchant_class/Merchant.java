package com.opark.opark.model.merchant_class;

public class Merchant {
   //TODO add merchant objects
   public String merchSignUpPerson, merchCoRegNumber, merchCoName, merchContact, merchEmail, merchCoAddress;




    public Merchant() {
    }

    public Merchant(String merchSignUpName, String merchCoRegNumber, String merchCoName, String merchContact, String merchEmail, String merchCoAddress) {
        this.merchSignUpPerson = merchSignUpName;
        this.merchCoRegNumber = merchCoRegNumber;
        this.merchCoName = merchCoName;
        this.merchContact = merchContact;
        this.merchEmail = merchEmail;
        this.merchCoAddress = merchCoAddress;
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

    public String getMerchBusiness() {
        return merchCoName;
    }

    public void setMerchBusiness(String merchCoName) {
        this.merchCoName = merchCoName;
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
}
